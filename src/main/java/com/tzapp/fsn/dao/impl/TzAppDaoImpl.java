package com.tzapp.fsn.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.tzapp.fsn.dao.TzAppDao;
import com.tzapp.fsn.util.StringUtil;
import com.tzapp.fsn.vo.TzAppBusInfoVO;
import com.tzapp.fsn.vo.TzAppProductDetailVO;
import com.tzapp.fsn.vo.TzAppReceiptVO;
import com.tzapp.fsn.vo.TzAppReportAndProductDetailVO;
import com.tzapp.fsn.vo.TzAppRequestParamVO;
import com.tzapp.fsn.vo.TzAppSearchAndScanVO;
import com.tzapp.fsn.vo.TzAppTestProperty;

@Repository("tzAppDao")
public class TzAppDaoImpl implements TzAppDao{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 根据当前登录组织机构ID获取企业名称
	 * desc:用于台账app登陆成功后显示企业名称
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TzAppSearchAndScanVO getBusNameByOrg(TzAppSearchAndScanVO vo) throws DaoException {
		try {
			String sql = "SELECT id,name FROM business_unit WHERE organization = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, vo.getOrg());
			List<Object[]> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				Object[] object = objects.get(0);
				TzAppSearchAndScanVO busVo = new TzAppSearchAndScanVO();
				busVo.setCurBusId(object[0]!=null?Long.parseLong(object[0].toString()):0l);
				busVo.setCurBusName(object[1]!=null?object[1].toString():"");
				return busVo;
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("根据当前登录组织机构ID获取企业名称，出现异常！",e);
		}
	}
	
	/**
	 * 根据扫描条码或条件加载收货单列表(总方法)
	 */
	@Override
	public List<TzAppReceiptVO> loadReceipt(TzAppSearchAndScanVO paramVo) throws DaoException{
		try {
			StringBuffer sb = new StringBuffer();
			if(paramVo.getIsReceipt()==0){//收货查询
				sb.append(" SELECT * FROM (");
				sb.append(" SELECT ac.id id,ac.create_time time,IF(ac.return_status=1,2,ac.in_status ) inStatus,bus.name busName,ac.return_status reStatus,ac.refuseReason season,pro.`name` proName,pro.barcode barcode");
				sb.append(" FROM product pro");
				sb.append(" INNER JOIN tz_business_account_info acInfo ON pro.id = acInfo.product_id");
				sb.append(" INNER JOIN tz_business_account ac ON ac.id = acInfo.business_account_id");
				sb.append(" INNER JOIN business_unit bus ON bus.id = ac.out_business_id");
				sb.append(" WHERE ac.in_business_id = ").append(paramVo.getCurBusId());
			}else if(paramVo.getIsReceipt()==1){//退货查询
				sb.append(" SELECT * FROM (");
				sb.append(" SELECT ac.id id,ac.create_time time,ac.out_status inStatus,bus.name busName,0,1,pro.`name` proName,pro.barcode barcode");
				sb.append(" FROM product pro");
				sb.append(" INNER JOIN tz_business_account_info_out acInfo ON pro.id = acInfo.product_id");
				sb.append(" INNER JOIN tz_business_account_out ac ON ac.id = acInfo.business_account_id");
				sb.append(" RIGHT JOIN business_unit bus ON bus.id = ac.in_business_id");
				sb.append(" WHERE ac.out_business_id = ").append(paramVo.getCurBusId());
			}
			return tzAppReceip(paramVo,sb.toString());
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->loadReceipt()根据扫描条码加载收货单列表,出现异常！", e);
		}
	}

	/**
	 * 根据扫描条码或条件加载收货单列表(分方法1)
	 * @param list
	 * @param paramVo
	 * @param busIdList 
	 * @return
	 * @throws DaoException
	 */
	private List<TzAppReceiptVO> tzAppReceip(TzAppSearchAndScanVO paramVo,String sql) throws DaoException{
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append(sql);
			//扫描或搜索条形码查询
			if(StringUtil.isNotEmpty(paramVo.getScanBarcode())||StringUtil.isNotEmpty(paramVo.getSearchBarcode())){
				if(paramVo.getIsScan()==0){//扫描
					sbSql.append(" and pro.barcode = '").append(paramVo.getScanBarcode()).append("'");
				}else if(paramVo.getIsScan()==1){//搜索
					sbSql.append(" and pro.barcode LIKE '%").append(paramVo.getSearchBarcode()).append("%'");
				}
			}
			//产品名称查询
			if(StringUtil.isNotEmpty(paramVo.getsProName())){
				sbSql.append(" and pro.name LIKE '%").append(paramVo.getsProName()).append("%'");
			}
			//供应商名称查询
			if(StringUtil.isNotEmpty(paramVo.getsBusName())){
				sbSql.append(" and bus.name LIKE '%").append(paramVo.getsBusName()).append("%'");
			}
			//营业执照号查询
			if(StringUtil.isNotEmpty(paramVo.getsLicNo())){
				sbSql.append(" and bus.license_no LIKE '%").append(paramVo.getsLicNo()).append("%'");
			}
			//进货 或交易时间查询处理
			if(StringUtil.isNotEmpty(paramVo.getsDealDate())){
				sbSql.append(" and ac.create_time >= '").append(paramVo.getsDealDate()).append(" 00:00:00").append("'");
				sbSql.append(" and ac.create_time <= '").append(paramVo.getsDealDate()).append(" 23:59:59").append("'");
			}
			//收货或处理状态查询处理
			if(StringUtil.isNotEmpty(paramVo.getsStatus())){
				if(paramVo.getIsReceipt()==1){//退货查询
					sbSql.append(" and ac.out_status = ").append(paramVo.getsStatus());//out_status:退货确认标识 0：未确认 1：已确认 
				}else if(paramVo.getIsReceipt()==0){//收货查询
					if(paramVo.getsStatus()==2){//拒收
						sbSql.append(" and ac.out_status = 0 and ac.return_status = 1");
					}else{//已收或待收
						sbSql.append(" and ac.out_status = 1");
						if(paramVo.getsStatus()==0){//待收货
							sbSql.append(" and (ac.in_status is null or ac.in_status = ").append(paramVo.getsStatus()).append(")");
						}else if(paramVo.getsStatus()==1){//已收货
							sbSql.append(" and ac.in_status = ").append(paramVo.getsStatus());
						}
					}
				}
			}
			sbSql.append(" GROUP BY ac.id) test");
			sbSql.append(" ORDER BY test.inStatus,test.time desc");
			return excuteReceipteSql(sbSql.toString(),paramVo);
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->tzAppReceip()根据扫描条码加载收货单列表,出现异常！", e);
		}
	}

	/**
	 * 根据扫描条码或条件加载收货单列表(分方法2)
	 * @param sql
	 * @param busInfo
	 * @return
	 * @throws DaoException 
	 */
	@SuppressWarnings("unchecked")
	private List<TzAppReceiptVO> excuteReceipteSql(String sql,TzAppSearchAndScanVO paramVo) throws DaoException {
		try {
			Query query = entityManager.createNativeQuery(sql);
			if(paramVo.getPage()>0&&paramVo.getPageSize()>0){
				query.setFirstResult((paramVo.getPage()-1)*paramVo.getPageSize());
				query.setMaxResults(paramVo.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setReceipteList(objects,paramVo);
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->excuteReceipteSql()根据扫描条码加载收货单列表,出现异常！", e);
		}
	}

	/**
	 * 封装查询返回的List
	 * @param objects
	 * @param paramVo
	 * @return
	 * @throws ParseException
	 */
	private List<TzAppReceiptVO> setReceipteList(List<Object[]> objects, TzAppSearchAndScanVO paramVo) throws ParseException {
		if(objects!=null&&objects.size()>0){
			List<TzAppReceiptVO> list = new ArrayList<TzAppReceiptVO>();
			TzAppReceiptVO tzAppReceiptVO = null;
			for(int i=0;i<objects.size();i++){
				Object[] object  = objects.get(i);
				tzAppReceiptVO = new TzAppReceiptVO();
				tzAppReceiptVO.setId(Long.parseLong(object[0].toString()));
				tzAppReceiptVO.setDealDate(object[1]!=null?SDF.format(SDF.parse(object[1].toString())):"");
				tzAppReceiptVO.setBusName(object[3]!=null?object[3].toString():"");//供应商名称
				tzAppReceiptVO.setName(object[6]!=null?object[6].toString():"");//产品名称
				tzAppReceiptVO.setBarcode(object[7]!=null?object[7].toString():"");//条形码
				if(paramVo.getIsReceipt()==0){//收货查询:待收货、已收货、拒收货
					tzAppReceiptVO.setInStatus(object[2]!=null?Integer.parseInt(object[2].toString()):0);
				}else if(paramVo.getIsReceipt()==1){//退货查询 ：未确认、已确认
					tzAppReceiptVO.setReturnStatus(object[2]!=null?Integer.parseInt(object[2].toString()):0);
				}
				list.add(tzAppReceiptVO);
			}
			return list;
		}
		return null;
	}

	/**
	 * 点击收货单的某行查看收货单详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzAppProductDetailVO> loadReceiptDetail(TzAppRequestParamVO busInfo)
			throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			if(busInfo.getStatus()==0){//收货详情
				sb.append(" SELECT * from (SELECT pro.id proId,pro.name proName,acInfo.product_batch proBatch,");
				sb.append(" IF(ac.in_status=1,acInfo.real_num,acInfo.product_num) proNum,acInfo.product_price proPrice,pro.barcode proBarcode,acInfo.id acInfoId");
				sb.append(" FROM product pro");
				sb.append(" LEFT JOIN tz_business_account_info acInfo ON acInfo.product_id = pro.id");
				sb.append(" LEFT JOIN tz_business_account ac ON ac.id = acInfo.business_account_id");
			}else if(busInfo.getStatus()==1){//退货详情
				sb.append(" SELECT * from (SELECT pro.id proId,pro.name proName,acInfo.product_batch proBatch,");
				sb.append(" acInfo.product_num proNum,acInfo.product_price proPrice,pro.barcode proBarcode,acInfo.id acInfoId");
				sb.append(" FROM product pro");
				sb.append(" LEFT JOIN tz_business_account_info_out acInfo ON acInfo.product_id = pro.id");
				sb.append(" LEFT JOIN tz_business_account_out ac ON ac.id = acInfo.business_account_id");
			}
			sb.append(" WHERE ac.id = ?1) test");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, busInfo.getId());
			if(busInfo.getPage()>0&&busInfo.getPageSize()>0){
				query.setFirstResult((busInfo.getPage()-1)*busInfo.getPageSize());
				query.setMaxResults(busInfo.getPageSize());
			}
			List<Object[]> list = query.getResultList();
			return setProductDetail(busInfo,list);
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->loadReceiptDetail点击收货单的某行查看收货单详情,出现异常！", e);
		}
	}

	/**
	 * @param paramVO 
	 * @param list
	 * @return
	 * @throws DaoException 
	 */
	private List<TzAppProductDetailVO> setProductDetail(TzAppRequestParamVO paramVO, List<Object[]> list) throws DaoException {
		TzAppProductDetailVO productDetail = null;
		List<TzAppProductDetailVO> proList = new ArrayList<TzAppProductDetailVO>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] object = list.get(i);
				productDetail = new TzAppProductDetailVO();
				productDetail.setProId(object[0]!=null?Long.parseLong(object[0].toString()):0);
				productDetail.setName(object[1]!=null?object[1].toString():"");
				productDetail.setBatch(object[2]!=null?object[2].toString():"");
				productDetail.setCount(object[3]!=null?Long.parseLong(object[3].toString()):0);
				productDetail.setPrice(object[4]!=null?Double.parseDouble(object[4].toString()):0);
				productDetail.setBarcode(object[5]!=null?object[5].toString():"");
				productDetail.setId(object[6]!=null?Long.parseLong(object[6].toString()):0);
				//根据产品ID获取产品图片(只取一张)
				String url = getProUrl(productDetail.getProId());
				productDetail.setProFullUrl(StringUtil.isNotEmpty(url)?url:StringUtil.getImgPath("",0,0));//产品原图
				productDetail.setUrl(StringUtil.getImgPath(url, paramVO.getWidth(), paramVO.getHeight()));//产品图片
				proList.add(productDetail);
			}
		}
		return proList;
	}

	/**
	 * 根据产品ID获取一张产品图片路径
	 * @param proId
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	private String getProUrl(Long proId) throws DaoException{
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT res.URL FROM t_test_product_to_resource proRes ");
			sb.append(" RIGHT JOIN t_test_resource res ON proRes.RESOURCE_ID = res.RESOURCE_ID");
			sb.append(" WHERE proRes.PRODUCT_ID =").append(proId);
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				return objects.get(0).toString(); 
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getProUrl()根据产品ID获取产品图片路径,出现异常！", e);
		}
	}

	/**
	 * 根据收货单ID获取供应商名和营业执照号
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TzAppRequestParamVO getBusInfo(TzAppRequestParamVO requestParamVO)
			throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			if(requestParamVO.getStatus()==0){//收货标识
				sb.append(" SELECT bus.name,bus.license_no,account.create_time,account.refuseReason,bus.id FROM tz_business_account account");
				sb.append(" LEFT JOIN business_unit bus ON account.out_business_id = bus.id");
			}else if(requestParamVO.getStatus()==1){//退货标识
				sb.append(" SELECT bus.name,bus.license_no,account.create_time,'' refuseReason,bus.id FROM tz_business_account_out account");
				sb.append(" LEFT JOIN business_unit bus ON account.in_business_id = bus.id");
			}
			sb.append(" WHERE account.id = ?1");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, requestParamVO.getId());
			List<Object[]> list = query.getResultList();
			if(list!=null&&list.size()>0){
				return setBusInfo(list.get(0));
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getBusInfo()根据收货单ID获取供应商名和营业执照号,出现异常！", e);
		}
	}

	/**
	 * 封装返回的供应商信息
	 * @param objects
	 * @return
	 * @throws ParseException
	 */
	private TzAppRequestParamVO setBusInfo(Object[] objects) throws ParseException {
		if(objects!=null&&objects.length>0){
			TzAppRequestParamVO busInfo = new TzAppRequestParamVO();
			busInfo.setOutBusName(objects[0]!=null?objects[0].toString():"");
			busInfo.setOutLicNo(objects[1]!=null?objects[1].toString():"");
			busInfo.setDealDate(objects[2]!=null?SDF.format(SDF.parse(objects[2].toString())):"");
			busInfo.setRefuseReason(objects[3]!=null?objects[3].toString():"");
			busInfo.setBusId(objects[4]!=null?objects[4].toString():"");
			return busInfo;
		}
		return null;
	}

	/**
	 * 根据台账信息ID加载产品详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TzAppReportAndProductDetailVO lookProductDetail(TzAppRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT * FROM (");
			sb.append(" SELECT pro.id proId,pro.barcode prob,pro.name proN,acInfo.product_batch acbatch,acInfo.production_date acprodate,acInfo.qs_no qs,pro.producer_id busId");
			if(paramVO.getStatus()==1){//退货单详情->查看产品详情
				sb.append(" FROM tz_business_account_info_out acInfo");
			}else{//收货单详情->查看产品详情
				sb.append(" FROM tz_business_account_info acInfo");
			}
			sb.append(" LEFT JOIN product pro ON acInfo.product_id = pro.id");
			sb.append(" WHERE acInfo.id = ").append(paramVO.getAcInfoId()).append(") test");
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> list = query.getResultList();
			return setReportAndProductDetail(paramVO,list);
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->lookProductDetail()根据产品ID加载产品详情,出现异常！", e);
		}
	}

	/**
	 * 封装返回产品详情
	 * @param paramVO 
	 * @param list
	 * @return
	 * @throws DaoException
	 * @throws ParseException
	 */
	private TzAppReportAndProductDetailVO setReportAndProductDetail(TzAppRequestParamVO paramVO, List<Object[]> list) throws DaoException, ParseException {
		if(list!=null&&list.size()>0){
			TzAppReportAndProductDetailVO vo = new TzAppReportAndProductDetailVO();
			Object[] object = list.get(0);
			vo.setProId(object[0]!=null?Long.parseLong(object[0].toString()):0);//产品ID
			vo.setBarcode(object[1]!=null?object[1].toString():"");//条形码
			vo.setName(object[2]!=null?object[2].toString():"");//产品名称
			vo.setBatch(object[3]!=null?object[3].toString():"");//批次
			if(object[4].toString().length()==8){
				vo.setProDate(object[4]!=null?simpleDateFormat.format(simpleDateFormat.parse(object[4].toString())):"");//产品生产日期
			}else{
				vo.setProDate(object[4]!=null?SDF.format(SDF.parse(object[4].toString())):"");//产品生产日期
			}
			vo.setQs(object[5]!=null?object[5].toString():"");//生产许可（QS）
			vo.setBusId(object[6]!=null?Long.parseLong(object[6].toString()):0);//生产企业ID
			/* 报告信息   */
			Object[] reportInfo = getReportINfo(vo.getProId());
			vo.setReportNum(reportInfo[0]!=null?reportInfo[0].toString():"");//报告编号
			vo.setReportType(reportInfo[1]!=null?reportInfo[1].toString():"");//报告类型
			vo.setReportPdf(reportInfo[2]!=null?reportInfo[2].toString():"");//报告pdf
			vo.setTestProResult(reportInfo[3]!=null?reportInfo[3].toString():"");//检测结果
			vo.setId(reportInfo[4]!=null?Long.parseLong(reportInfo[4].toString()):0);//报告ID
			
			vo.setFoodUrl(getFoodUrl(paramVO,vo.getProId()));//食品认证图片url
			vo.setBrand(getBrand(vo.getProId()));//商标
			vo.setBusName(getBusName(vo.getBusId()));//企业名称
			vo.setProFullUrl(StringUtil.isNotEmpty(getProUrl(vo.getProId()))?getProUrl(vo.getProId()):StringUtil.getImgPath("",0,0));//产品原图
			vo.setProUrl(StringUtil.getImgPath(getProUrl(vo.getProId()),paramVO.getWidth(),paramVO.getHeight()));//产品图片
			return vo;
		}
		return null;
	}

	/**
	 * 根据产品ID获取最新的报告
	 * @author ChenXiaolin 2015-12-18
	 * @param proId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object[] getReportINfo(Long proId) throws DaoException{
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT result.service_order reportnum,result.test_type reporttype,result.fullPdfPath ful,");
			sb.append(" IF(result.pass=1,'合格','不合格') pass,result.id reportId");
			sb.append(" FROM product_instance instance ");
			sb.append(" INNER JOIN test_result result ON result.sample_id = instance.id");
			sb.append(" WHERE result.pass = 1 and result.publish_flag IN(1,6) and result.del = 0 AND instance.product_id = ").append(proId);
			sb.append(" ORDER BY instance.production_date DESC LIMIT 1");
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				return objects.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getReportINfo() 根据产品ID获取最新的报告,出现异常！", e);
		}
		
	}

	/**
	 * 根据产品ID获取商标
	 * @param proId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getBrand(Long proId) throws DaoException{
		try {
			String sql = " SELECT name from business_brand WHERE id IN (SELECT business_brand_id FROM product WHERE id = ?1)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, proId);
			List<Object> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				return objects.get(0).toString();
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getBrand()根据产品ID获取商标,出现异常！", e);
		}
	}

	/**
	 * 根据企业ID获取企业名称
	 * @param busId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getBusName(Long busId) {
		String sql = " select name from business_unit where id = ?1";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, busId);
		List<Object> objects = query.getResultList();
		if(objects!=null&&objects.size()>0){
			return objects.get(0).toString();
		}
		return "";
	}

	//获取食品认证图片
	@SuppressWarnings("unchecked")
	private String getFoodUrl(TzAppRequestParamVO paramVO, Long proId) throws DaoException{
		try {
			StringBuffer sb = new StringBuffer();
			StringBuffer sbReturn = new StringBuffer();
			sb.append(" SELECT cer.imgurl,res.URL FROM business_certification_to_product buscerpro");
			sb.append(" RIGHT JOIN business_certification buscer ON buscerpro.business_cert_id = buscer.id");
			sb.append(" RIGHT JOIN t_test_resource res ON res.RESOURCE_ID = buscer.resource_id");
			sb.append(" RIGHT JOIN certification cer ON cer.id = buscer.cert_id ");
			sb.append(" WHERE buscerpro.product_id = ").append(proId);
			sb.append(" GROUP BY cer.id;");
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> list = query.getResultList();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					
					Object[] object = list.get(i);
					
					if(object!=null&&object.length>0){
						sbReturn.append(object[0]!=null?StringUtil.getImgPath(object[0].toString(), paramVO.getWidth(), paramVO.getHeight()):"").append(",")//小图标
						        .append(object[1]!=null?object[1].toString():"").append(";");//大图标--原图
					}else{
						return "";
					}
				}
				return sbReturn.toString();
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getFoodUrl()获取食品认证图片,出现异常！", e);
		}
	}

	/**
	 * 获取报告检测项目列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzAppTestProperty> getTestPropertyList(TzAppRequestParamVO busInfo) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT name,tech_indicator,assessment FROM test_property");
			sb.append(" WHERE test_result_id = ").append(busInfo.getReportId());
			Query query = entityManager.createNativeQuery(sb.toString());
			if(busInfo.getPage()>0&&busInfo.getPageSize()>0){
				query.setFirstResult((busInfo.getPage()-1)*busInfo.getPageSize());
				query.setMaxResults(busInfo.getPageSize());
			}
			List<Object[]> list = query.getResultList();
			return setTestProperty(list);
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getTestPropertyList()获取报告检测项目列表,出现异常！", e);
		}
	}

	/**
	 * 封装返回报告检测列表
	 * @param list
	 * @return
	 */
	private List<TzAppTestProperty> setTestProperty(List<Object[]> list) {
		if(list!=null&&list.size()>0){
			TzAppTestProperty testptoperty = null;
			List<TzAppTestProperty> testList = new ArrayList<TzAppTestProperty>();
			for(int i=0;i<list.size();i++){
				Object[] object = list.get(i);
				testptoperty = new TzAppTestProperty();
				testptoperty.setName(object[0]!=null?object[0].toString():"");
				testptoperty.setTechIndicator(object[1]!=null?object[1].toString():"");
				testptoperty.setPass(object[2]!=null?object[2].toString():"");
				testList.add(testptoperty);
			}
			return testList;
		}
		return null;
	}

	/**
	 * 供应商详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TzAppBusInfoVO lookBusInfo(TzAppRequestParamVO requestParam)
			throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT id,name,address,contact,telephone FROM business_unit WHERE id = ").append(Long.parseLong(requestParam.getBusId()));
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> list = query.getResultList();
			if(list!=null&&list.size()>0){
				return setBusInfoVO(list.get(0),requestParam);
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getTestPropertyList()获取报告检测项目列表,出现异常！", e);
		}
	}

	/**
	 * 封装返回的供应商详情信息
	 * @param objects
	 * @param requestParam 
	 * @return
	 * @throws DaoException 
	 */
	private TzAppBusInfoVO setBusInfoVO(Object[] objects, TzAppRequestParamVO requestParam) throws DaoException {
		TzAppBusInfoVO vo = new TzAppBusInfoVO();
		if(objects!=null&&objects.length>0){
			vo.setOutBusId(objects[0]!=null?Long.parseLong(objects[0].toString()):0l);
			vo.setBusName(objects[1]!=null?objects[1].toString():"");
			vo.setBusAddress(objects[2]!=null?objects[2].toString():"");
			vo.setContacts(objects[3]!=null?objects[3].toString():"");
			vo.setContactsPhone(objects[4]!=null?objects[4].toString():"");
		}
		if(!"".equals(vo.getBusName())){
			vo.setRegisteId(getRegisteId(vo));//根据企业名称获取企业注册ID
		}
		if(vo.getRegisteId()>0){
			String orgUrl = getOutBusResource(vo,"t_org_license_to_resource");
			vo.setOrgFullUrl(orgUrl);//组织机构代码原图图片路径
			vo.setOrgUrl(StringUtil.isNotEmpty(orgUrl)?StringUtil.getImgPath(orgUrl,requestParam.getWidth(),requestParam.getHeight()):"");//组织机构代码图片路径
			String licUrl = getOutBusResource(vo,"t_business_license_to_resource");
			vo.setLicNoFullUrl(licUrl);//营业执照原图图片路径
			vo.setLicNoUrl(StringUtil.isNotEmpty(licUrl)?StringUtil.getImgPath(licUrl,requestParam.getWidth(),requestParam.getHeight()):"");//营业执照图片路径
		}
		if(vo.getOutBusId()>0){
			vo.setQsFullUrl(getQsUrl(vo));//生产许可原图图片路径
			vo.setQsUrl(StringUtil.isNotEmpty(getQsUrl(vo))?StringUtil.getImgPath(getQsUrl(vo),requestParam.getWidth(),requestParam.getHeight()):"");//生产许可图片路径
			String url = getPublicUrl(vo);
			String defaultUrl = getDefaultUrl(requestParam);
			vo.setPublicizeFullUrl(StringUtil.isEmpty(url)?defaultUrl:url);//企业宣传照原图
			vo.setPublicizeUrl(StringUtil.isEmpty(url)?defaultUrl:StringUtil.getImgPath(url,requestParam.getWidth(),requestParam.getHeight()));//企业宣传照
		}
		return vo;
	}
	
	/**
	 * 企业默认的宣传照
	 * @param requestParam
	 * @return
	 */
	private String getDefaultUrl(TzAppRequestParamVO requestParam){
		String path = requestParam.getRequset().getContextPath();
		String basePath =  requestParam.getRequset().getScheme()+"://"
				+ requestParam.getRequset().getServerName()+":"
				+ requestParam.getRequset().getServerPort()+path+"/"
				+"resource/img/";
		return basePath+"enterprise_default_pic.png";
	}

	/**
	 * 企业宣传照
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getPublicUrl(TzAppBusInfoVO vo) throws DaoException{
		try {
			StringBuffer sb = new StringBuffer(); 
			sb.append(" SELECT pub_ptotos_url FROM t_bus_sales_info WHERE business_id = ?1");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, vo.getOutBusId());
			List<String> list = query.getResultList();
			if(list!=null&&list.size()>0){
				String url = list.get(0);
				if(!"".equals(url)&&url!=null){
					String[] urls = url.split("\\|");
					if(urls.length>0){
						return urls[0];
					}else{
						return "";
					}
				}else{
					return "";
				}
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getPublicUrl()获取企业宣传照,出现异常！",e);
		}
	}

	/**
	 * 生产许可图片路径
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getQsUrl(TzAppBusInfoVO vo) throws DaoException{
		try {
			StringBuffer sb = new StringBuffer(); 
			sb.append(" SELECT res.URL FROM product_to_businessunit protobus");
			sb.append(" RIGHT JOIN production_license_info prolic ON prolic.id = protobus.qs_id");
			sb.append(" RIGHT JOIN productionlicenseinfo_to_resource protolic ON protolic.qs_id = prolic.id");
			sb.append(" RIGHT JOIN t_test_resource res ON res.RESOURCE_ID = protolic.resource_id");
			sb.append(" WHERE protobus.business_id = ").append(vo.getOutBusId());
			Query query = entityManager.createNativeQuery(sb.toString());
			List<String> list = query.getResultList();
			if(list!=null&&list.size()>0){
				if(!"".equals(list.get(0))&&list.get(0)!=null){
					return list.get(0);
				}
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getQsUrl()获取生产许可图片路径,出现异常！",e);
		}
	}

	//获取组织机构代码图片路径、营业执照图片路径
	@SuppressWarnings("unchecked")
	private String getOutBusResource(TzAppBusInfoVO vo,String table) throws DaoException{
		try {
			StringBuffer sb = new StringBuffer(); 
			sb.append(" SELECT res.URL FROM ").append(table).append(" busRes");
			sb.append(" RIGHT JOIN t_test_resource res ON res.RESOURCE_ID = busRes.RESOURCE_ID");
			sb.append(" WHERE busRes.ENTERPRISE_REGISTE_ID = ?1");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, vo.getRegisteId());
			List<String> list = query.getResultList();
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getOutBusResource()获取组织机构代码图片路径、营业执照图片路径,出现异常！",e);
		}
	}

	/**
	 * 根据企业名称获取企业注册ID
	 * @param vo
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	private Long getRegisteId(TzAppBusInfoVO vo) throws DaoException{
		try {
			String sql = " SELECT id FROM enterprise_registe WHERE enterpriteName = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, vo.getBusName());
			List<Object> list = query.getResultList();
			if(list!=null&&list.size()>0){
				return Long.parseLong(list.get(0).toString());
			}
			return 0l;
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->getRegisteId()根据企业名称获取企业注册ID,出现异常！",e);
		}
	}

	/**
	 * 确认退货
	 */
	@Override
	public void confirmReturnOfGoods(Long id) throws DaoException {
		try {
			String sql = " UPDATE tz_business_account_out SET out_status = 1 WHERE id = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->confirmReturnOfGoods()确认退货,出现异常！",e);
		}
		
	}

	/**
	 * 加载未审核的报告列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzAppReportAndProductDetailVO> noCheck(TzAppSearchAndScanVO paramVo) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT tr.id,pro.barcode,pro.name,res.URL,tr.service_order,proinst.batch_serial_no ");
			sb.append(" FROM test_result tr");
			sb.append(" LEFT JOIN product_instance proinst ON tr.sample_id = proinst.id");
			sb.append(" LEFT JOIN product pro ON pro.id = proinst.product_id");
			sb.append(" LEFT JOIN t_test_product_to_resource proRes ON proRes.PRODUCT_ID = pro.id");
			sb.append(" LEFT JOIN t_test_resource res ON res.RESOURCE_ID = proRes.RESOURCE_ID");
			sb.append(" WHERE tr.publish_flag IN('4') AND tr.del = 0 AND EXISTS ( ");
			sb.append(" SELECT  1  FROM product_instance  pri ");
			sb.append(" INNER JOIN t_meta_from_to_business tb ON pri.product_id=tb.pro_id ");
			sb.append(" INNER JOIN business_unit bus ON bus.id = tb.from_bus_id ");
			sb.append(" INNER JOIN t_meta_enterprise_to_provider tc ON tc.`business_id`= tb.to_bus_id  AND tc.`provider_id`=tb.`from_bus_id`");
			sb.append(" WHERE tb.del = 0 AND tb.to_bus_id = ").append(paramVo.getCurBusId()).append(" AND tr.`sample_id` = pri.`id` AND bus.organization = tr.organization )");
			sb.append(" GROUP BY tr.id");
			Query query = entityManager.createNativeQuery(sb.toString());
			if(paramVo.getPage()>0&&paramVo.getPageSize()>0){
				query.setFirstResult((paramVo.getPage()-1)*paramVo.getPageSize());
				query.setMaxResults(paramVo.getPageSize());
			}
			List<Object[]> list = query.getResultList();
			return setLoadNoCheck(paramVo,list);//给加载未审核报告设置返回值
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->noCheck()加载未审核的报告列表,出现异常！",e);
		}
	}
	
	/**
	 * 给加载未审核报告设置返回值
	 * @param paramVo 
	 * @param list
	 * @return
	 */
	private List<TzAppReportAndProductDetailVO> setLoadNoCheck(TzAppSearchAndScanVO paramVo, List<Object[]> list) {
		if(list!=null&&list.size()>0){
			List<TzAppReportAndProductDetailVO> vos = new ArrayList<TzAppReportAndProductDetailVO>();
			TzAppReportAndProductDetailVO vo = null;
			for(int i=0;i<list.size();i++){
				Object[] object =  list.get(i);
				vo = new TzAppReportAndProductDetailVO();
				vo.setBarcode(object[1]!=null?object[1].toString():"");//条形码
				vo.setName(object[2]!=null?object[2].toString():"");//产品名称
				vo.setProFullUrl(object[3]!=null?object[3].toString():StringUtil.getImgPath("",0,0));//产品原图Url
				vo.setProUrl(StringUtil.getImgPath(object[3]!=null?object[3].toString():"",paramVo.getWidth(),paramVo.getHeight()));//产品图片
				vo.setId(object[0]!=null?Long.parseLong(object[0].toString()):0);//报告ID
				vo.setReportNum(object[4]!=null?object[4].toString():"");//报告编号
				vo.setBatch(object[5]!=null?object[5].toString():"");//批次
				vos.add(vo);
			}
			return vos;
		}
		return null;
	}

	/**
	 * 报告总数量和未审核的数量
	 * @param curOrg
	 * @return
	 */
	public Long getTotal(TzAppSearchAndScanVO paramVo) throws DaoException{
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT COUNT(*) FROM test_result tr");
			if(paramVo.getIsTotal()==0){//报告总数标识
				sb.append(" WHERE tr.publish_flag != '7' AND tr.del = 0 AND EXISTS ( ");
			}else if(paramVo.getIsTotal()==1){//待审核报告总数标识
				sb.append(" WHERE tr.publish_flag = '4' AND tr.del = 0 AND EXISTS ( ");
			}
			sb.append(" SELECT  1  FROM product_instance  pri ");
			sb.append(" INNER JOIN t_meta_from_to_business tb ON pri.product_id=tb.pro_id ");
			sb.append(" INNER JOIN business_unit bus ON bus.id = tb.from_bus_id ");
			sb.append(" INNER JOIN t_meta_enterprise_to_provider tc ON tc.`business_id`= tb.to_bus_id  AND tc.`provider_id`=tb.`from_bus_id`");
			sb.append(" WHERE tb.del = 0 AND tb.to_bus_id = ").append(paramVo.getCurBusId()).append(" AND tr.`sample_id` = pri.`id` AND bus.organization = tr.organization )");
			Query query = entityManager.createNativeQuery(sb.toString());
			Object total = query.getSingleResult();
			return Long.parseLong(total.toString());
		} catch (NumberFormatException e) {
			throw new DaoException("TzAppDaoImpl-->getTotal()报告总数量,出现异常！",e);
		}
	}

	/**
	 * 根据报告ID获取审核详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TzAppReportAndProductDetailVO reportlDetail(TzAppRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from (");
			sb.append(" SELECT result.id resId,pro.id proId,pro.barcode bar,pro.name proName,res.URL url,brand.`name` braName,");
			sb.append(" result.service_order sorder,result.test_type type,IF(result.pass=1,'合格','不合格') AS pass,");
			sb.append(" result.fullPdfPath fullurl,proinst.batch_serial_no batch,proinst.production_date prodate,bus.name busName,pro.producer_id busId");
			sb.append(" FROM test_result result");
			sb.append(" LEFT JOIN business_unit bus ON bus.organization = result.organization");
			sb.append(" LEFT JOIN product_instance proinst ON result.sample_id = proinst.id");
			sb.append(" LEFT JOIN product pro ON pro.id = proinst.product_id");
			sb.append(" LEFT JOIN business_brand brand ON pro.business_brand_id = brand.id");
			sb.append(" LEFT JOIN t_test_product_to_resource proRes ON proRes.PRODUCT_ID = pro.id");
			sb.append(" LEFT JOIN t_test_resource res ON res.RESOURCE_ID = proRes.RESOURCE_ID");
			sb.append(" WHERE result.id = ").append(paramVO.getId()).append(") test");
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> list = query.getResultList();
			if(list!=null&&list.size()>0){
				return setReportDetail(paramVO,list.get(0));//设置审核报告详情的值
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->reportlDetail()根据报告ID获取审核详情,出现异常！",e);
		}
	}

	/**
	 * 设置审核报告详情的值
	 * @param paramVO 
	 * @param objects
	 * @return
	 * @throws DaoException
	 * @throws ParseException
	 */
	private TzAppReportAndProductDetailVO setReportDetail(TzAppRequestParamVO paramVO, Object[] objects) throws DaoException, ParseException {
		if(objects!=null&&objects.length>0){
			TzAppReportAndProductDetailVO vo = new TzAppReportAndProductDetailVO();
			vo.setBarcode(objects[2]!=null?objects[2].toString():"");//条形码
			vo.setName(objects[3]!=null?objects[3].toString():"");//产品名称
			vo.setBrand(objects[5]!=null?objects[5].toString():"");//商标
			vo.setProFullUrl(objects[4]!=null?objects[4].toString():StringUtil.getImgPath("",0,0));//产品图片原图
			vo.setProUrl(StringUtil.getImgPath(objects[4]!=null?objects[4].toString():"", paramVO.getWidth(), paramVO.getHeight()));//产品图片url
			vo.setProId(objects[1]!=null?Long.parseLong(objects[1].toString()):0);//产品ID
			vo.setFoodUrl(getFoodUrl(paramVO,vo.getProId()));//获取食品认证
			vo.setBusId(objects[13]!=null?Long.parseLong(objects[13].toString()):0);//企业ID
			vo.setBusName(getProducerName(vo.getBusId()));//获取该产品的生产企业名称
			vo.setQs(getQsByProId(vo.getProId()));//获取生产许可QS
			vo.setId(objects[0]!=null?Long.parseLong(objects[0].toString()):0);//报告ID
			vo.setReportNum(objects[6]!=null?objects[6].toString():"");///报告编号
			vo.setTestProResult(objects[8]!=null?objects[8].toString():"");//检测结果
			vo.setReportType(objects[7]!=null?objects[7].toString():"");//检测类型
			vo.setReportPdf(objects[9]!=null?objects[9].toString():"");//报告原pdf
			vo.setBatch(objects[10]!=null?objects[10].toString():"");//批次
			vo.setProDate(objects[11]!=null?SDF.format(SDF.parse(objects[11].toString())):"");//生产日期
			return vo;
		}
		return null;
	}

	/**
	 * 获取生产企业名称
	 * @param busId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getProducerName(Long busId) {
		String sql = "SELECT name FROM business_unit WHERE id = ?1";
		Query query = entityManager.createNativeQuery(sql);	
		query.setParameter(1, busId);
		List<String> list = query.getResultList();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return "";
	}

	/**
	 * 根据企业ID获取生产许可证号（QS）
	 * @param busId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getQsByProId(Long proId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT pli.qs_no FROM product_to_businessunit ptb");
		sb.append(" LEFT JOIN production_license_info pli ON pli.id = ptb.qs_id");
		sb.append(" WHERE ptb.PRODUCT_ID = ").append(proId).append(" LIMIT 1");
		Query query = entityManager.createNativeQuery(sb.toString());
		List<String> list = query.getResultList();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return "";
	}

	/**
	 * 审核搜索
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzAppReportAndProductDetailVO> searchReport(TzAppSearchAndScanVO paramVo) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT tr.id,pro.barcode,pro.name,res.URL,tr.service_order,proinst.batch_serial_no ");
			sb.append(" FROM test_result tr");
			sb.append(" LEFT JOIN product_instance proinst ON tr.sample_id = proinst.id");
			sb.append(" LEFT JOIN product pro ON pro.id = proinst.product_id");
			sb.append(" LEFT JOIN t_test_product_to_resource proRes ON proRes.PRODUCT_ID = pro.id");
			sb.append(" LEFT JOIN t_test_resource res ON res.RESOURCE_ID = proRes.RESOURCE_ID");
			sb.append(" WHERE tr.del = 0 AND EXISTS ( ");
			sb.append(" SELECT  1  FROM product_instance  pri ");
			sb.append(" INNER JOIN t_meta_from_to_business tb ON pri.product_id=tb.pro_id ");
			sb.append(" INNER JOIN business_unit bus ON bus.id = tb.from_bus_id ");
			sb.append(" INNER JOIN t_meta_enterprise_to_provider tc ON tc.`business_id`= tb.to_bus_id  AND tc.`provider_id`=tb.`from_bus_id`");
			sb.append(" WHERE tb.del = 0 AND tb.to_bus_id = ").append(paramVo.getCurBusId()).append(" AND tr.`sample_id` = pri.`id` AND bus.organization = tr.organization ");
			if(StringUtil.isNotEmpty(paramVo.getsBusName())){
				sb.append(" and bus.name LIKE '%").append(paramVo.getsBusName()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVo.getsLicNo())){
				sb.append(" and bus.license_no LIKE '%").append(paramVo.getsLicNo()).append("%'");
			}
			sb.append(")");
			if(StringUtil.isNotEmpty(paramVo.getScanBarcode())||StringUtil.isNotEmpty(paramVo.getSearchBarcode())){
				if(paramVo.getIsScan()==0){//扫描
					sb.append(" and pro.barcode = '").append(paramVo.getScanBarcode()).append("'");
				}else if(paramVo.getIsScan()==1){//搜索
					sb.append(" and pro.barcode LIKE '%").append(paramVo.getSearchBarcode()).append("%'");
				}
			}
			if(StringUtil.isNotEmpty(paramVo.getsProName())){
				sb.append(" and pro.name LIKE '%").append(paramVo.getsProName()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVo.getsStatus())){
				if(paramVo.getsStatus()==0){//待审核
					sb.append(" and tr.publish_flag = 4");
				}
				if(paramVo.getsStatus()==1){//审核已通过
					sb.append(" and tr.publish_flag = 1");
				}
				if(paramVo.getsStatus()==2){//审核未通过
					sb.append(" and tr.publish_flag = 5");
				}
			}
			sb.append(" GROUP BY tr.id");
			Query query = entityManager.createNativeQuery(sb.toString());
			if(paramVo.getPage()>0&&paramVo.getPageSize()>0){
				query.setFirstResult((paramVo.getPage()-1)*paramVo.getPageSize());
				query.setMaxResults(paramVo.getPageSize());
			}
			List<Object[]> list = query.getResultList();
			return setLoadNoCheck(paramVo,list);//给加载未审核报告设置返回值
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->searchReport()审核搜索,出现异常！",e);
		}
	}

	/**
	 * 获取快捷原因列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> backResults(TzAppRequestParamVO busInfo)
			throws DaoException {
		try {
			String sql  = "select id,result from tz_back_result where type = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, busInfo.getType());
			if(busInfo.getPage()>0&&busInfo.getPageSize()>0){
				query.setFirstResult((busInfo.getPage()-1)*busInfo.getPageSize());
				query.setMaxResults(busInfo.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setBackResults(objects);//设置返回的快捷原因列表
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->backResults()获取快捷原因列表,出现异常！",e);
		}
	}

	/**
	 * 设置返回的快捷原因列表
	 * @param objects
	 * @return
	 */
	private List<Map<String, String>> setBackResults(List<Object[]> objects) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = null;
		if(objects!=null&&objects.size()>0){
			for(int i=0;i<objects.size();i++){
				Object[] object = objects.get(i);
				if(object!=null&&object.length>0){
					map = new HashMap<String, String>();
					map.put("id",object[0]!=null?object[0].toString():"无" );
					map.put("result",object[1]!=null?object[1].toString():"无" );
					list.add(map);
				}
			}
			return list;
		}
		return null;
	}

	@Override
	public Long getScanTotal(TzAppSearchAndScanVO paramVo) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT COUNT(*) FROM (");
			sb.append(" SELECT tr.id FROM test_result tr ");
			sb.append(" LEFT JOIN product_instance proinst ON tr.sample_id = proinst.id");
			sb.append(" LEFT JOIN product pro ON pro.id = proinst.product_id");
			sb.append(" LEFT JOIN t_test_product_to_resource proRes ON proRes.PRODUCT_ID = pro.id");
			sb.append(" LEFT JOIN t_test_resource res ON res.RESOURCE_ID = proRes.RESOURCE_ID");
			sb.append(" WHERE tr.del = 0 AND EXISTS ( ");
			sb.append(" SELECT  1  FROM product_instance  pri ");
			sb.append(" INNER JOIN t_meta_from_to_business tb ON pri.product_id=tb.pro_id ");
			sb.append(" INNER JOIN business_unit bus ON bus.id = tb.from_bus_id ");
			sb.append(" INNER JOIN t_meta_enterprise_to_provider tc ON tc.`business_id`= tb.to_bus_id  AND tc.`provider_id`=tb.`from_bus_id`");
			sb.append(" WHERE tb.del = 0 AND tb.to_bus_id = ").append(paramVo.getCurBusId()).append(" AND tr.`sample_id` = pri.`id` AND bus.organization = tr.organization ");
			if(StringUtil.isNotEmpty(paramVo.getsBusName())){
				sb.append(" and bus.name LIKE '%").append(paramVo.getsBusName()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVo.getsLicNo())){
				sb.append(" and bus.license_no LIKE '%").append(paramVo.getsLicNo()).append("%'");
			}
			sb.append(")");
			if(StringUtil.isNotEmpty(paramVo.getScanBarcode())||StringUtil.isNotEmpty(paramVo.getSearchBarcode())){
				if(paramVo.getIsScan()==0){//扫描
					sb.append(" and pro.barcode = '").append(paramVo.getScanBarcode()).append("'");
				}else if(paramVo.getIsScan()==1){//搜索
					sb.append(" and pro.barcode LIKE '%").append(paramVo.getSearchBarcode()).append("%'");
				}
			}
			if(StringUtil.isNotEmpty(paramVo.getsProName())){
				sb.append(" and pro.name LIKE '%").append(paramVo.getsProName()).append("%'");
			}
			sb.append(" and tr.publish_flag = 4");//待审核的
			sb.append(" GROUP BY tr.id) test");
			Query query = entityManager.createNativeQuery(sb.toString());
			Object object = query.getSingleResult();
			return Long.parseLong(object.toString());
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->searchReport()审核搜索,出现异常！",e);
		}
	}

	/**
	 * 获取收货或退货查询的总数
	 * @author ChenXiaolin 2015-11-21
	 */
	@Override
	public Long receiptOrRefuseTotal(TzAppSearchAndScanVO paramVo) throws DaoException {
		try {
			StringBuffer sbSql = new StringBuffer();
			if(paramVo.getIsReceipt()==0){//收货查询
				sbSql.append(" SELECT count(*) FROM (");
				sbSql.append(" SELECT ac.id id,ac.create_time time,IF(ac.return_status=1,2,ac.in_status ) inStatus,bus.name busName,ac.return_status reStatus,ac.refuseReason season,pro.`name` proName,pro.barcode barcode");
				sbSql.append(" FROM product pro");
				sbSql.append(" INNER JOIN tz_business_account_info acInfo ON pro.id = acInfo.product_id");
				sbSql.append(" INNER JOIN tz_business_account ac ON ac.id = acInfo.business_account_id");
				sbSql.append(" INNER JOIN business_unit bus ON bus.id = ac.out_business_id");
				sbSql.append(" WHERE ac.in_business_id = ").append(paramVo.getCurBusId());
			}else if(paramVo.getIsReceipt()==1){//退货查询
				sbSql.append(" SELECT count(*) FROM (");
				sbSql.append(" SELECT ac.id id,ac.create_time time,ac.out_status inStatus,bus.name busName,0,1,pro.`name` proName,pro.barcode barcode");
				sbSql.append(" FROM product pro");
				sbSql.append(" INNER JOIN tz_business_account_info_out acInfo ON pro.id = acInfo.product_id");
				sbSql.append(" INNER JOIN tz_business_account_out ac ON ac.id = acInfo.business_account_id");
				sbSql.append(" RIGHT JOIN business_unit bus ON bus.id = ac.in_business_id");
				sbSql.append(" WHERE ac.out_business_id = ").append(paramVo.getCurBusId());
			}
			//扫描或搜索条形码查询
			if(StringUtil.isNotEmpty(paramVo.getScanBarcode())||StringUtil.isNotEmpty(paramVo.getSearchBarcode())){
				if(paramVo.getIsScan()==0){//扫描
					sbSql.append(" and pro.barcode = '").append(paramVo.getScanBarcode()).append("'");
				}else if(paramVo.getIsScan()==1){//搜索
					sbSql.append(" and pro.barcode LIKE '%").append(paramVo.getSearchBarcode()).append("%'");
				}
			}
			//产品名称查询
			if(StringUtil.isNotEmpty(paramVo.getsProName())){
				sbSql.append(" and pro.name LIKE '%").append(paramVo.getsProName()).append("%'");
			}
			//供应商名称查询
			if(StringUtil.isNotEmpty(paramVo.getsBusName())){
				sbSql.append(" and bus.name LIKE '%").append(paramVo.getsBusName()).append("%'");
			}
			//营业执照号查询
			if(StringUtil.isNotEmpty(paramVo.getsLicNo())){
				sbSql.append(" and bus.license_no LIKE '%").append(paramVo.getsLicNo()).append("%'");
			}
			//进货 或交易时间查询处理
			if(StringUtil.isNotEmpty(paramVo.getsDealDate())){
				sbSql.append(" and ac.create_time >= '").append(paramVo.getsDealDate()).append(" 00:00:00").append("'");
				sbSql.append(" and ac.create_time <= '").append(paramVo.getsDealDate()).append(" 23:59:59").append("'");
			}
			//收货或处理状态查询处理
			if(StringUtil.isNotEmpty(paramVo.getsStatus())){
				if(paramVo.getIsReceipt()==1){//退货查询
					sbSql.append(" and ac.out_status = ").append(paramVo.getsStatus());//out_status:退货确认标识 0：未确认 1：已确认 
				}else if(paramVo.getIsReceipt()==0){//收货查询
					if(paramVo.getsStatus()==2){//拒收
						sbSql.append(" and ac.out_status = 0 and ac.return_status = 1");
					}else{//已收或待收
						sbSql.append(" and ac.out_status = 1");
						if(paramVo.getsStatus()==0){//待收货
							sbSql.append(" and (ac.in_status is null or ac.in_status = ").append(paramVo.getsStatus()).append(")");
						}else if(paramVo.getsStatus()==1){//已收货
							sbSql.append(" and ac.in_status = ").append(paramVo.getsStatus());
						}
					}
				}
			}
			sbSql.append(" GROUP BY ac.id) test");
			Query query = entityManager.createNativeQuery(sbSql.toString());
			Object total = query.getSingleResult();
			return Long.parseLong(total.toString());
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->receiptOrRefuseTotal()获取收货或退货查询的总数,出现异常！",e);
		}
	}

	/**
	 * 获取已收货或待收货的总数
	 * @author ChenXiaolin 2015-11-24
	 */
	@Override
	public Long loadTotal(TzAppSearchAndScanVO vo) throws DaoException {
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append(" SELECT count(*) FROM tz_business_account WHERE in_business_id = ?1 AND out_status = 1");
			if(vo.getConfirmTotal()==0){//已收货
				sbSql.append(" and in_status = 1");
			}else if(vo.getConfirmTotal()==1){//待收货
				sbSql.append(" and (in_status is null or in_status = 0)");
			}
			Query query = entityManager.createNativeQuery(sbSql.toString());
			query.setParameter(1, vo.getCurBusId());
			Object total = query.getSingleResult();
			return Long.parseLong(total.toString());
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->loadTotal()获取已收货或待收货的总数,出现异常！",e);
		}
	}
	
	/**
	 * update报告审核通过标识
	 * @author ChenXiaolin 2015-12-09
	 * @param charAt
	 * @param reportId
	 * @param returnReason
	 * @param curBusName
	 * @throws DaoException
	 */
	@Override
	public void updatePublishFlag(char publishFlag, Long reportId,String msg, String checkOrgName) throws DaoException {
		try {
			String sql = "";
			if(publishFlag == '5'){
				if(msg == null){
					msg = "";
				}
				sql = "UPDATE test_result SET publish_flag = ?1,back_time = now(),back_result= ?2,check_org_name=?4 WHERE id = ?3";
			}else if(publishFlag=='6'){
				msg = "";
				sql = "UPDATE test_result SET publish_flag = ?1,receiveDate = now(),back_result= ?2,check_org_name=?4 WHERE id = ?3";
			}else{//进口食品报告商超通过直接发布到portal
				msg = "";
				sql = "UPDATE test_result SET publish_flag = ?1,receiveDate = now(),pub_user_name=?5,publishDate = now(),back_result= ?2,check_org_name=?4 WHERE id = ?3";
			}
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, publishFlag);
			query.setParameter(2, msg);
			query.setParameter(3, reportId);
			query.setParameter(4, checkOrgName);
			if(publishFlag == '1'){
				query.setParameter(5, getUserName(checkOrgName));
			}
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("TzAppDaoImpl-->updatePublishFlag() 出现异常！" , e);
		}
		
	}

	/**
	 * 根据企业名称获取用户名
	 * @author ChenXiaolin 2015-12-09
	 * @param checkOrgName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getUserName(String checkOrgName) throws DaoException {
		try {
			String sql = "SELECT userName FROM enterprise_registe WHERE enterpriteName = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, checkOrgName);
			List<Object> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				return objects.get(0).toString();
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl-->getUserName()根据企业名称获取用户名， 出现异常！" , e);
		}
	}

}
