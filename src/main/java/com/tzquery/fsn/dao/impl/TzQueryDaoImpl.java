/**
 * 
 */
package com.tzquery.fsn.dao.impl;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.facility.FacilityInfo;
import com.gettec.fsnip.fsn.model.facility.FacilityMaintenanceRecord;
import com.gettec.fsnip.fsn.model.member.Member;
import com.gettec.fsnip.fsn.model.operate.OperateInfo;
import com.gettec.fsnip.fsn.model.procurement.ProcurementDispose;
import com.gettec.fsnip.fsn.model.procurement.ProcurementInfo;
import com.gettec.fsnip.fsn.model.procurement.ProcurementUsageRecord;
import com.tzquery.fsn.dao.TzQueryDao;
import com.tzquery.fsn.util.StringUtil;
import com.tzquery.fsn.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenXiaolin 2015-11-30
 */
@Repository("tzQueryDao")
public class TzQueryDaoImpl implements TzQueryDao{
	
	Logger logger = LoggerFactory.getLogger(TzQueryDaoImpl.class);
	
	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * 根据查询条件分页获取产品信息列表
	 * @author ChenXiaolin 2015-12-01
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public List<TzQueryResponseProInfoVO> productQuery(
			TzQueryRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT pro.id,pro.barcode,pro.name,pro.format,proLicInfo.qs_no,pro.category");
			sb.append(" FROM business_unit bus");
			sb.append(" INNER JOIN tz_stock stock ON bus.id = stock.business_id");
			sb.append(" INNER JOIN product pro ON pro.id = stock.product_id");
			sb.append(" LEFT JOIN product_to_businessunit protobus ON pro.id = protobus.PRODUCT_ID");
			sb.append(" LEFT JOIN production_license_info proLicInfo ON protobus.qs_id = proLicInfo.id");
			/* 产品名称 */
			if(StringUtil.isNotEmpty(paramVO.getProName())){
				sb.append(" AND pro.name LIKE '%").append(paramVO.getProName()).append("%'");
			}
			/* 产品条形码 */
			if(StringUtil.isNotEmpty(paramVO.getProBarcode())){
				sb.append(" AND pro.barcode LIKE '%").append(paramVO.getProBarcode()).append("%'");
			}
			/* 流通区域  */
			if(StringUtil.isNotEmpty(paramVO.getProvice())){
				String address = paramVO.getProvice();
				if(StringUtil.isNotEmpty(paramVO.getCity())){
					address = address+paramVO.getCity();
					if(StringUtil.isNotEmpty(paramVO.getArea())){
						address = address+paramVO.getArea();
					}
				}
				sb.append(" AND bus.address LIKE '%").append(address).append("%'");
			}
			/* 产品分类  */
			//监管内网和fsn的分类不统一，fsn上新版本才确定,所显示的分类到时也要改（目前显示的是二级或三级分类）
			
			sb.append(" GROUP BY pro.id");
			return excuteSql(paramVO,sb.toString().replaceFirst("AND", "WHERE"));
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->productQuery()根据查询条件获取产品信息列表，出现异常！", e);
		}
	}

	/**
	 * 执行Sql语句
	 * @author ChenXiaolin 2015-12-1
	 * @param paramVO 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<TzQueryResponseProInfoVO> excuteSql(TzQueryRequestParamVO paramVO, String sql) throws DaoException{
		try {
			Query query = entityManager.createNativeQuery(sql);
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setproQuery(objects);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->excuteSql()执行sql根据查询条件获取产品信息列表，出现异常！", e);
		}
	}

	/**
	 * 封装产品查询信息
	 * @author ChenXiaolin 2015-12-1
	 * @param objects
	 * @return
	 */
	private List<TzQueryResponseProInfoVO> setproQuery(List<Object[]> objects) {
		
		List<TzQueryResponseProInfoVO> list = new ArrayList<TzQueryResponseProInfoVO>();
		TzQueryResponseProInfoVO vo = null;
		if(objects!=null&&objects.size()>0){
			for(int i=0;i<objects.size();i++){
				Object[] object = objects.get(i);
				if(object!=null){
					vo = new TzQueryResponseProInfoVO();
					vo.setProId(object[0]!=null?object[0].toString():"");
					vo.setProBarcode(object[1]!=null?object[1].toString():"");
					vo.setProName(object[2]!=null?object[2].toString():"");
					vo.setProFormat(object[3]!=null?object[3].toString():"");
					vo.setProQs(object[4]!=null?object[4].toString():"");
					vo.setProCategory(getFirstCattegory(object[5]!=null?object[5].toString():""));
					vo.setCheckWay("");     	//待FSN新版本才有的字段
					vo.setSalesArea("");		//待FSN新版本才有的字段	
					vo.setWhetherEexport("");	//待FSN新版本才有的字段
					list.add(vo);
					
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取查询产品列表总记录数
	 * @author ChenXiaolin 2015-12-1
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getproductQueryTotal(TzQueryRequestParamVO paramVO)
			throws DaoException {
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT count(DISTINCT pro.id)");
			sb.append(" FROM business_unit bus");
			sb.append(" INNER JOIN tz_stock stock ON bus.id = stock.business_id");
			sb.append(" INNER JOIN product pro ON pro.id = stock.product_id");
			sb.append(" LEFT JOIN product_to_businessunit protobus ON pro.id = protobus.PRODUCT_ID");
			sb.append(" LEFT JOIN production_license_info proLicInfo ON protobus.qs_id = proLicInfo.id");
			/* 产品名称 */
			if(StringUtil.isNotEmpty(paramVO.getProName())){
				sb.append(" AND pro.name LIKE '%").append(paramVO.getProName()).append("%'");
			}
			/* 产品条形码 */
			if(StringUtil.isNotEmpty(paramVO.getProBarcode())){
				sb.append(" AND pro.barcode LIKE '%").append(paramVO.getProBarcode()).append("%'");
			}
			/* 流通区域  */
			if(StringUtil.isNotEmpty(paramVO.getProvice())){
				String address = paramVO.getProvice();
				if(StringUtil.isNotEmpty(paramVO.getCity())){
					address = address+paramVO.getCity();
					if(StringUtil.isNotEmpty(paramVO.getArea())){
						address = address+paramVO.getArea();
					}
				}
				sb.append(" AND bus.address LIKE '%").append(address).append("%'");
			}
			/* 产品分类  */
			
			/* 执行sql */
			Query query = entityManager.createNativeQuery(sb.toString().replaceFirst("AND", "WHERE"));
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getproductQueryTotal()获取查询产品列表总记录数，出现异常！", e);
		}
	}
	
	/**
	 * 根据产品ID获取产品详情
	 * @author ChenXiaolin 2015-12-2
	 * @param proId
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TzQueryResponseProInfoVO getProDetail(String proId)
			throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT * FROM (");
			sb.append(" SELECT pro.name proName,pro.category proCte,pro.format profor,brand.name brandName,pro.barcode proBarcode,prolicinfo.qs_no qs FROM product pro");
			sb.append("	LEFT JOIN product_to_businessunit protobus ON protobus.PRODUCT_ID = pro.id");
			sb.append(" LEFT JOIN production_license_info prolicinfo ON protobus.qs_id = prolicinfo.id");
			sb.append(" LEFT JOIN business_brand brand ON brand.id = pro.business_brand_id");
			sb.append(" WHERE pro.id = ").append(Long.parseLong(proId)).append(") test");
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> objects = query.getResultList();
			return setProDetail(objects,proId);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->lookReport()根据产品ID获取报告信息列表,出现异常！", e);
		}
	}

	/**
	 * 封装产品详情
	 * @author ChenXiaolin 2015-12-2
	 * @param objects
	 * @param proId 
	 * @return
	 */
	private TzQueryResponseProInfoVO setProDetail(List<Object[]> objects, String proId) {
		
		TzQueryResponseProInfoVO vo = null;
		if(objects!=null&&objects.size()>0){
			Object[] object = objects.get(0);
			if(object!=null){
				vo = new TzQueryResponseProInfoVO();
				vo.setProName(object[0]!=null?object[0].toString():"");							//产品名称
				vo.setProCategory(getFirstCattegory(object[1]!=null?object[1].toString():""));	//产品分类--一级分类
				vo.setProFormat(object[2]!=null?object[2].toString():"");						//规格型号
				vo.setProBrand(object[3]!=null?object[3].toString():"");						//注册商标
				vo.setProBarcode(object[4]!=null?object[4].toString():"");						//条形码
				vo.setProQs(object[5]!=null?object[5].toString():"");							//许可证编号
				vo.setIssueUnit("");		//发证单位--待FSN新版本才有的字段
				vo.setIssueDate("");		//发证时间--待FSN新版本才有的字段
				vo.setValidDate("");		//有效期至--待FSN新版本才有的字段
				vo.setProStatus("");		//生产状态--待FSN新版本才有的字段
				vo.setWhetherEexport("");	//是否出口--待FSN新版本才有的字段
				vo.setSalesArea("");		//销售区域--待FSN新版本才有的字段
				vo.setRelationType("");		//销售类型--待FSN新版本才有的字段
				vo.setCheckWay("");			//出厂检验方式待FSN新版本才有的字段
				vo.setProId(proId);         //产品ID
				//执行标准列表
				//???待FSN新版本才有的字段
			}
		}
		return vo;
	}

	/**
	 * 根据二级code获取一级分类名称
	 * @author ChenXiaolin 2015-12-02
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getFirstCattegory(String code) {
		if(StringUtil.isNotEmpty(code)&&code.length()>=2){
			String sql = " SELECT name FROM product_category WHERE code = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, code.substring(0, 2));
			List<Object> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				return objects.get(0).toString();
			}
		}
		return "";
	}
	
	/**
	 * 根据产品ID获取报告信息列表
	 * @author ChenXiaolin 2015-12-1
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzQueryResponseReportInfoVO> lookReport(
			TzQueryRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT pro.name,pro.barcode,proInst.batch_serial_no,result.service_order,result.test_type,");
			sb.append("result.test_orgnization,IF(result.pass=1,'合格','不合格') pass,result.fullPdfPath");
			sb.append(" FROM product_instance proInst");
			sb.append(" LEFT JOIN test_result result ON proInst.id = result.sample_id");
			sb.append(" LEFT JOIN product pro ON pro.id = proInst.product_id");
			sb.append(" WHERE result.del = 0 AND proInst.product_id = ").append(Long.parseLong(paramVO.getProId()));
			Query query = entityManager.createNativeQuery(sb.toString());
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setLookReport(objects);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->lookReport()根据产品ID获取报告信息列表,出现异常！", e);
		}
	}

	/**
	 * 封装报告列表信息
	 * @author ChenXiaolin 2015-12-01
	 * @param objects
	 * @return
	 */
	private List<TzQueryResponseReportInfoVO> setLookReport(List<Object[]> objects) {
		
		List<TzQueryResponseReportInfoVO> list = new ArrayList<TzQueryResponseReportInfoVO>();
		TzQueryResponseReportInfoVO vo = null; 
		
		if(objects!=null&&objects.size()>0){
			for(int i=0;i<objects.size();i++){
				Object[] object = objects.get(i);
				if(object!=null){
					vo = new  TzQueryResponseReportInfoVO();
					vo.setProName(object[0]!=null?object[0].toString():"");
					vo.setProBarcode(object[1]!=null?object[1].toString():"");
					vo.setProBatch(object[2]!=null?object[2].toString():"");
					vo.setReportNum(object[3]!=null?object[3].toString():"");
					vo.setTestType(object[4]!=null?object[4].toString():"");
					vo.setTestUnit(object[5]!=null?object[5].toString():"");
					vo.setTestResult(object[6]!=null?object[6].toString():"");
					vo.setReportPdf(object[7]!=null?object[7].toString():"");
					list.add(vo);
				}
			}
		}
		return list;
	}

	/**
	 * 获取报告信息列表总记录数
	 * @author ChenXiaolin 2015-12-2
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getLookReportTotal(TzQueryRequestParamVO paramVO)
			throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT count(*) FROM product_instance proInst");
			sb.append(" LEFT JOIN test_result result ON proInst.id = result.sample_id");
			sb.append(" LEFT JOIN product pro ON pro.id = proInst.product_id");
			sb.append(" WHERE result.del = 0 AND proInst.product_id = ").append(paramVO.getProId());
			Query query = entityManager.createNativeQuery(sb.toString());
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getLookReportTotal()获取报告信息列表总记录数 ,出现异常！", e);
		}
	}

	/**
	 * 根据产品ID分页获取销售企业列表
	 * @author ChenXiaolin 2015-12-02
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzQueryResponseBusVO> getSaleBusiness(
			TzQueryRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb_qyery = new StringBuffer(" and bus.other_address LIKE '");
			if (StringUtil.isNotEmpty(paramVO.getProvice())) {		//省
				sb_qyery.append(paramVO.getProvice()).append("-");
			}
			if(StringUtil.isNotEmpty(paramVO.getCity())){			//市
				sb_qyery.append(paramVO.getCity()).append("-");
			}
			if (StringUtil.isNotEmpty(paramVO.getArea())) {			//区
				sb_qyery.append(paramVO.getArea());
			}
			sb_qyery.append("%'");

			StringBuffer sb = new StringBuffer();
			sb.append(" select * from (");
			sb.append(" SELECT pro.name proName,pro.category cat,pro.barcode bar, bus.id busId,bus.other_address oadd,bus.name busName,");
			sb.append("bus.license_no lic,bus.type type,bus.address addr,IF(bus.id=pro.producer_id,'生产','经营') relation,pro.id");
			sb.append(" FROM tz_stock stock ");
			sb.append(" INNER JOIN business_unit bus ON bus.id = stock.business_id ");
			sb.append(" INNER JOIN product pro ON pro.id = stock.product_id");
			sb.append(" WHERE stock.product_id = ?1");
			sb.append(sb_qyery);
			sb.append(" GROUP BY bus.id ) test");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, Long.parseLong(paramVO.getProId()));
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setSaleBusiness(objects);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getSaleBusiness()根据产品ID分页获取销售企业列表 ,出现异常！", e);
		}
	}

	/**
	 * 封装企业相关信息
	 * @author ChenXiaolin 2015-12-02
	 * @param objects
	 * @return
	 */
	private List<TzQueryResponseBusVO> setSaleBusiness(List<Object[]> objects) {
		
		List<TzQueryResponseBusVO> list = new ArrayList<TzQueryResponseBusVO>();
		TzQueryResponseBusVO vo = null;
		if(objects!=null&&objects.size()>0){
			for(int i=0;i<objects.size();i++){
				Object[] object = objects.get(i);
				if(object!=null){
					vo = new TzQueryResponseBusVO();
					vo.setProName(object[0]!=null?object[0].toString():"");								//产品名称
					vo.setProFirstCategory(getFirstCattegory(object[1]!=null?object[1].toString():""));	//产品分类
					vo.setProBarcode(object[2]!=null?object[2].toString():"");							//条形码
					vo.setBusId(object[3]!=null?object[3].toString():"");								//企业ID
					//企业地址分割开--Start		
					String otherAddress = object[4]!=null?object[4].toString():"";				
					String proviceStr = "";
					String cityStr = "";
					String areaStr = "";
					if(!"".equals(otherAddress)){
						String[] address1 = otherAddress.split("\\--");
						if(address1.length>=1){
							String[] address2 = address1[0].split("\\-");
							if(address2.length>0){
								if(address2.length==1){
									proviceStr = address2[0];
								}
								if(address2.length==2){
									proviceStr = address2[0];
									cityStr = address2[1];
								}
								if(address2.length>=3){
									proviceStr = address2[0];
									cityStr = address2[1];
									areaStr = address2[2];
								}
							}
						}
					}
					vo.setProvice(proviceStr);	//流通区域--省
					vo.setCity(cityStr);		//流通区域--市
					vo.setArea(areaStr);		//流通区域--去
					//企业地址分割开--End
					vo.setBusName(object[5]!=null?object[5].toString():"");		//企业名称
					vo.setBusLic(object[6]!=null?object[6].toString():"");		//营业执照号
					vo.setBusType(object[7]!=null?object[7].toString():"");		//企业类型
					vo.setBusAddress(object[8]!=null?object[8].toString():"");	//企业地址
					vo.setProRelation(object[9]!=null?object[9].toString():"");	//产品关系
					vo.setProId(object[10]!=null?object[10].toString():"");		//产品ID
					list.add(vo);
				}
			}
		}
		return list;
	}

	/**
	 * 获取销售企业列表总记录
	 * @author ChenXiaolin 2015-12-02
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getSaleBusinessTotal(TzQueryRequestParamVO paramVO)
			throws DaoException {
		try {
			StringBuffer sb_qyery = new StringBuffer(" and bus.other_address LIKE '");
			if (StringUtil.isNotEmpty(paramVO.getProvice())) {        //省
				sb_qyery.append(paramVO.getProvice()).append("-");
			}
			if (StringUtil.isNotEmpty(paramVO.getCity())) {            //市
				sb_qyery.append(paramVO.getCity()).append("-");
			}
			if (StringUtil.isNotEmpty(paramVO.getArea())) {            //区
				sb_qyery.append(paramVO.getArea());
			}
			sb_qyery.append("%'");

			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT count(DISTINCT bus.id) FROM tz_stock stock");
			sb.append(" INNER JOIN business_unit bus ON bus.id = stock.business_id ");
			sb.append(" INNER JOIN product pro ON pro.id = stock.product_id");
			sb.append(" WHERE stock.product_id = ?1");
			sb.append(sb_qyery);
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, paramVO.getProId());
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getSaleBusinessTotal()获取销售企业列表总记录,出现异常！",e);
		}
	}

	/**
	 * 根据企业ID和产品ID获取销售企业中的交易明细
	 * @author ChenXiaolin 2015-12-03
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public List<TzQueryResponseTansDetailVO> getProQueryTransDetailList(
			TzQueryRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT busName,busLic,transType,proBatch,time,SUM(num) FROM (");
			sb.append(" SELECT bus.name busName,bus.license_no busLic,'供应商' transType,");
			sb.append("accountInfo.product_batch proBatch,account.create_time time,accountInfo.product_num num");
			sb.append(" FROM tz_business_account account");
			sb.append(" LEFT JOIN business_unit bus ON bus.id = account.out_business_id");
			sb.append(" INNER JOIN tz_business_account_info accountInfo ON account.id = accountInfo.business_account_id AND accountInfo.product_id = ?1");
			sb.append(" WHERE account.in_business_id = ?2");
			sb.append(" UNION");
			sb.append(" SELECT bus.name busName,bus.license_no busLic,'客户' transType,");
			sb.append("accountInfo.product_batch proBatch,account.create_time time,accountInfo.product_num num");
			sb.append(" FROM tz_business_account account");
			sb.append(" LEFT JOIN business_unit bus ON bus.id = account.in_business_id");
			sb.append(" INNER JOIN tz_business_account_info accountInfo ON account.id = accountInfo.business_account_id AND accountInfo.product_id = ?3");
			sb.append(" WHERE account.out_business_id  = ?4) test");
			/* 查询条件  */
			if(StringUtil.isNotEmpty(paramVO.getTransType())){
				if("全部".equals(paramVO.getTransType())){//供应商和客户
					sb.append(" and (transType = '供应商' or transType ='客户')");
				}else{//供应商或客户
					sb.append(" and transType = '").append(paramVO.getTransType()).append("'");
				}
			}
			if(StringUtil.isNotEmpty(paramVO.getProBatch())){
				sb.append(" and proBatch LIKE '%").append(paramVO.getProBatch()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransSDate())){
				sb.append(" and time >= '").append(paramVO.getTransSDate()).append(" 00:00:00").append("'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransEDate())){
				sb.append(" and time <= '").append(paramVO.getTransEDate()).append(" 23:59:59").append("'");
			}
			String sql = "";
			if(sb.toString().indexOf("test and ")>-1){
				sql = sb.toString().replace("test and", "test where");
			}else{
				sql = sb.toString();
			}
			sql=sql+" GROUP BY test.proBatch,test.time ORDER BY test.time desc";
			return excuteSqlToTransDetail(sql,paramVO);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getProQueryTransDetailList()根据企业ID和产品ID分页获取销售企业中的交易明细,出现异常！",e);
		}
	}

	/**
	 * 执行销售企业中的交易明细的sql
	 * @author ChenXiaolin 2015-12-03
	 * @param paramVO 
	 * @param sql 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<TzQueryResponseTansDetailVO> excuteSqlToTransDetail(String sql, TzQueryRequestParamVO paramVO)throws DaoException{
		try {
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, paramVO.getProId());
			query.setParameter(2, paramVO.getBusId());
			query.setParameter(3, paramVO.getProId());
			query.setParameter(4, paramVO.getBusId());
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setTransDetail(objects);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->excuteSqlToTransDetail()执行销售企业中的交易明细的sql,出现异常！",e);
		}
	}

	/**
	 * 封装返回交易明细的VO
	 * @author ChenXiaolin 2015-12-3
	 * @param objects
	 * @return
	 */
	private List<TzQueryResponseTansDetailVO> setTransDetail(
			List<Object[]> objects) {
		List<TzQueryResponseTansDetailVO> list = new ArrayList<TzQueryResponseTansDetailVO>();
		TzQueryResponseTansDetailVO vo = null;
		if(objects!=null&&objects.size()>0){
			for(int i=0;i<objects.size();i++){
				Object[] object =objects.get(i);
				if(object!=null&&object.length>0){
					vo = new TzQueryResponseTansDetailVO();
					vo.setTransTarget(object[0]!=null?object[0].toString():"");
					vo.setLicense(object[1]!=null?object[1].toString():"");
					vo.setTransType(object[2]!=null?object[2].toString():"");
					vo.setProBatch(object[3]!=null?object[3].toString():"");
					vo.setTransDate(object[4]!=null?StringUtil.datestrToDataStr(object[4].toString(),"yyyy-MM-dd HH:mm:ss"):"");
					vo.setTransAmount(object[5]!=null?Long.parseLong(object[5].toString()):0);
					list.add(vo);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取销售企业中交易明细的总记录数
	 * @author ChenXiaolin 2015-12-03
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getProQueryTransDetailTotal(TzQueryRequestParamVO paramVO)
			throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT count(*) FROM(");
			sb.append(" SELECT 1 FROM (");
			sb.append(" SELECT '供应商' transType,accountInfo.product_batch proBatch,account.create_time time FROM tz_business_account account");
			sb.append(" LEFT JOIN business_unit bus ON bus.id = account.out_business_id");
			sb.append(" INNER JOIN tz_business_account_info accountInfo ON account.id = accountInfo.business_account_id AND accountInfo.product_id = ?1");
			sb.append(" WHERE account.in_business_id = ?2");
			sb.append(" UNION");
			sb.append(" SELECT '客户' transType,accountInfo.product_batch proBatch,account.create_time time FROM tz_business_account account");
			sb.append(" LEFT JOIN business_unit bus ON bus.id = account.in_business_id");
			sb.append(" INNER JOIN tz_business_account_info accountInfo ON account.id = accountInfo.business_account_id AND accountInfo.product_id = ?3");
			sb.append(" WHERE account.out_business_id  = ?4) test");
			/* 查询条件  */
			if(StringUtil.isNotEmpty(paramVO.getTransType())){
				if("全部".equals(paramVO.getTransType())){//供应商和客户
					sb.append(" and (transType = '供应商' or transType ='客户')");
				}else{//供应商或客户
					sb.append(" and transType = '").append(paramVO.getTransType()).append("'");
				}
			}
			if(StringUtil.isNotEmpty(paramVO.getProBatch())){
				sb.append(" and proBatch LIKE '%").append(paramVO.getProBatch()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransSDate())){
				sb.append(" and time >= '").append(paramVO.getTransSDate()).append(" 00:00:00").append("'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransEDate())){
				sb.append(" and time <= '").append(paramVO.getTransEDate()).append(" 23:59:59").append("'");
			}
			String sql = "";
			if(sb.toString().indexOf("test and ")>-1){
				sql = sb.toString().replace("test and", "test where");
			}else{
				sql = sb.toString();
			}
			sql=sql+" GROUP BY test.proBatch,test.time) testTotal";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, paramVO.getProId());
			query.setParameter(2, paramVO.getBusId());
			query.setParameter(3, paramVO.getProId());
			query.setParameter(4, paramVO.getBusId());
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getProQueryTransDetailTotal()获取销售企业中交易明细的总记录数,出现异常！",e);
		}
	}

	/**
	 * 根据企业名称获取台账信息列表
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzQueryResponseBusVO> getAccountInfo(
			TzQueryRequestParamVO paramVO) throws DaoException {
		try {
			String changeSql = "SELECT DISTINCT outBus.id,outBus.name,outBus.license_no,outBus.type,outBus.address";
			StringBuffer sql = new StringBuffer();
			StringBuffer publicSql = new StringBuffer();
			publicSql.append(" FROM tz_business_account account");
			publicSql.append(" INNER JOIN business_unit inBus ON inBus.id = account.in_business_id");
			publicSql.append(" INNER JOIN business_unit outBus ON outBus.id = account.out_business_id");
			if(paramVO.getSalesType()==0){//采购
				sql.append(changeSql);
				sql.append(publicSql);
				sql.append(" WHERE inBus.id = ?1");
			}else if(paramVO.getSalesType()==1){//销售
				String temporarySql = changeSql.replace("outBus", "inBus");
				sql.append(temporarySql);
				sql.append(publicSql);
				sql.append(" WHERE outBus.id = ?1");
			}
			Query query = entityManager.createNativeQuery(sql.toString());
			query.setParameter(1, paramVO.getFirstBusId());
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setAccounInfo(objects,paramVO);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getAccountInfo()根据企业名称获取台账信息列表,出现异常！",e);
		}
	}

	/**
	 * 封装返回的台账信息列表
	 * @author ChenXiaolin 2015-12-04
	 * @param objects
	 * @param paramVO 
	 * @return
	 */
	private List<TzQueryResponseBusVO> setAccounInfo(List<Object[]> objects, TzQueryRequestParamVO paramVO) {
		
		List<TzQueryResponseBusVO> list = new ArrayList<TzQueryResponseBusVO>();
		TzQueryResponseBusVO vo = null;
		if(objects!=null&&objects.size()>0){
			for(int i = 0;i<objects.size();i++){
				Object[] object = objects.get(i);
				if(object!=null&&object.length>0){
					vo = new TzQueryResponseBusVO();
					vo.setBusId(object[0]!=null?object[0].toString():"");		//企业ID
					vo.setBusName(object[1]!=null?object[1].toString():"");		//企业名称
					vo.setBusLic(object[2]!=null?object[2].toString():"");		//企业营业执照号
					vo.setBusType(object[3]!=null?object[3].toString():"");		//企业类型
					vo.setBusAddress(object[4]!=null?object[4].toString():"");	//企业地址
					vo.setFirstBusName(paramVO.getBusName());					//一级企业名称:作为下一个接口的参数
					list.add(vo);
				}
			}
		}
		return list;
	}

	/**
	 * 获取台账信息列表总记录数
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getAccountInfoTotal(TzQueryRequestParamVO paramVO)
			throws DaoException {
		try {
			String changeSql = "SELECT count(DISTINCT outBus.id)";
			StringBuffer sql = new StringBuffer();
			StringBuffer publicSql = new StringBuffer();
			publicSql.append(" FROM tz_business_account account");
			publicSql.append(" INNER JOIN business_unit inBus ON inBus.id = account.in_business_id");
			publicSql.append(" INNER JOIN business_unit outBus ON outBus.id = account.out_business_id");
			if(paramVO.getSalesType()==0){//采购
				sql.append(changeSql);
				sql.append(publicSql);
				sql.append(" WHERE inBus.id = ?1");
			}else if(paramVO.getSalesType()==1){//销售
				String temporarySql = changeSql.replace("outBus", "inBus");
				sql.append(temporarySql);
				sql.append(publicSql);
				sql.append(" WHERE outBus.id = ?1");
			}
			Query query = entityManager.createNativeQuery(sql.toString());
			query.setParameter(1, paramVO.getFirstBusId());
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getAccountInfoTotal()获取台账信息列表总记录数,出现异常！",e);
		}
	}

	/**
	 * 获取企业查询中台账的交易明细列表
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzQueryResponseTansDetailVO> getBusQueryTransDetail(
			TzQueryRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT pro.barcode,pro.name,pro.format,acInfo.product_batch,acInfo.production_date,account.create_time,acInfo.product_num");
			sb.append(" FROM tz_business_account account");
			sb.append(" INNER JOIN tz_business_account_info acInfo ON account.id = acInfo.business_account_id");
			sb.append(" INNER JOIN product pro ON pro.id = acInfo.product_id");
			sb.append(" INNER JOIN business_unit inBus  ON inBus.id = account.in_business_id");
			sb.append(" INNER JOIN business_unit outBus ON outBus.id = account.out_business_id");
			if(paramVO.getSalesType()==0){//台账详细中的企业向一级企业供应产品明细
				sb.append(" WHERE account.out_business_id = ?1 AND inBus.id = ?2");
			}else if(paramVO.getSalesType()==1){//一级企业向台账详细中的企业销售产品明细
				sb.append(" WHERE account.in_business_id = ?1 AND outBus.id = ?2");
			}
			/* 查询条件  */
			if(StringUtil.isNotEmpty(paramVO.getProBarcode())){//产品条形码
				sb.append(" AND pro.barcode LIKE '%").append(paramVO.getProBarcode()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getProName())){//产品名称
				sb.append(" AND pro.name LIKE '%").append(paramVO.getProName()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransSDate())){
				sb.append(" AND account.create_time >= '").append(paramVO.getTransSDate()).append(" 00:00:00").append("'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransEDate())){
				sb.append(" AND account.create_time <= '").append(paramVO.getTransEDate()).append(" 23:59:59").append("'");
			}
			sb.append(" ORDER BY account.create_time DESC");
			/* 执行sql  */
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, paramVO.getBusId());
			query.setParameter(2, paramVO.getFirstBusId());
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setBusQueryDetail(objects);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getBusQueryTransDetail()获取企业查询中台账的交易明细列表,出现异常！",e);
		}
	}

	/**
	 * 封装企业查询中交易明细返回的Vo列表
	 * @author ChenXiaolin 2015-12-04
	 * @param objects
	 * @return
	 */
	private List<TzQueryResponseTansDetailVO> setBusQueryDetail(
			List<Object[]> objects) {
		
		List<TzQueryResponseTansDetailVO> list = new ArrayList<TzQueryResponseTansDetailVO>();
		TzQueryResponseTansDetailVO vo = null;
		if(objects!=null&&objects.size()>0){
			for(int i = 0;i<objects.size();i++){
				Object[] object = objects.get(i);
				if(object!=null&&object.length>0){
					vo = new TzQueryResponseTansDetailVO();
					vo.setProBarcode(object[0]!=null?object[0].toString():"");
					vo.setProName(object[1]!=null?object[1].toString():"");
					vo.setProFormat(object[2]!=null?object[2].toString():"");
					vo.setProBatch(object[3]!=null?object[3].toString():"");
					vo.setProDate(object[4]!=null?object[4].toString():"");
					vo.setTransDate(object[5]!=null?StringUtil.datestrToDataStr(object[5].toString(),"yyyy-MM-dd HH:mm:ss"):"");
					vo.setTransAmount(object[6]!=null?Long.parseLong(object[6].toString()):0);
					list.add(vo);
				}
			}
		}
 		return list;
	}

	/**
	 * 获取企业查询中台账的交易明细列表总记录数
	 * @author ChenXiaolin 2015-12-04
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getBusQueryAccountInfoTotal(TzQueryRequestParamVO paramVO)
			throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT count(*) FROM tz_business_account account");
			sb.append(" INNER JOIN tz_business_account_info acInfo ON account.id = acInfo.business_account_id");
			sb.append(" INNER JOIN product pro ON pro.id = acInfo.product_id");
			sb.append(" INNER JOIN business_unit inBus  ON inBus.id = account.in_business_id");
			sb.append(" INNER JOIN business_unit outBus ON outBus.id = account.out_business_id");
			if(paramVO.getSalesType()==0){//台账详细中的企业向一级企业供应产品明细
				sb.append(" WHERE account.out_business_id = ?1 AND inBus.id = ?2");
			}else if(paramVO.getSalesType()==1){//一级企业向台账详细中的企业销售产品明细
				sb.append(" WHERE account.in_business_id = ?1 AND outBus.id = ?2");
			}
			/* 查询条件  */
			if(StringUtil.isNotEmpty(paramVO.getProBarcode())){//产品条形码
				sb.append(" AND pro.barcode LIKE '%").append(paramVO.getProBarcode()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getProName())){//产品名称
				sb.append(" AND pro.name LIKE '%").append(paramVO.getProName()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransSDate())){
				sb.append(" AND account.create_time >= '").append(paramVO.getTransSDate()).append(" 00:00:00").append("'");
			}
			if(StringUtil.isNotEmpty(paramVO.getTransEDate())){
				sb.append(" AND account.create_time <= '").append(paramVO.getTransEDate()).append(" 23:59:59").append("'");
			}
			/* 执行sql  */
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, paramVO.getBusId());
			query.setParameter(2, paramVO.getFirstBusId());
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getBusQueryAccountInfoTotal()获取企业查询中台账的交易明细列表总记录数,出现异常！",e);
		}
	}
	
	 /**
	  * 根据企业名称获取企业ID
	  * @author ChenXiaolin 2015-12-05
	  * @param busName
	  * @return
	  * @throws DaoException
	  */
	@SuppressWarnings("unchecked")
	public String getBusIdByBusName(String busName) throws DaoException{
		try {
			String sql = "SELECT id from business_unit WHERE name = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, busName);
			List<Object> objects = query.getResultList();
			if(objects!=null&&objects.size()>0){
				return objects.get(0).toString();
			}
			return "";
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getBusIdByBusName()根据企业名称获取企业ID,出现异常！",e);
		}
	}

	/**
	 * 根据企业名称获取该企业销售的产品列表
	 * @author ChenXiaolin 2015-12-14
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TzQueryResponseProListVO> getBusQueryProList(
			TzQueryRequestParamVO paramVO) throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT * FROM ( ");
			sb.append(" SELECT pro.id id,pro.name proN,pro.barcode pb,pro.format f,pro.category cet");
			sb.append(" FROM tz_business_account account");
			sb.append(" INNER JOIN business_unit bus ON bus.id = account.out_business_id");
			sb.append(" INNER JOIN tz_business_account_info acInfo ON acInfo.business_account_id = account.id");
			sb.append(" INNER JOIN product pro ON pro.id = acInfo.product_id");
			sb.append(" WHERE bus.name = '").append(paramVO.getBusName()).append("'");
			
			if(StringUtil.isNotEmpty(paramVO.getLicenseNo())){//营业执照号
				sb.append(" AND bus.license_no = '").append(paramVO.getLicenseNo()).append("'");
			}
			/* 查询条件  */
			if(StringUtil.isNotEmpty(paramVO.getProName())){//产品名称
				sb.append(" AND pro.name LIKE '%").append(paramVO.getProName()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getFormat())){//规格型号
				sb.append(" AND pro.format LIKE '%").append(paramVO.getFormat()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getProBarcode())){//产品条形码
				sb.append(" AND pro.barcode LIKE '%").append(paramVO.getProBarcode()).append("%'");
			}
			/* 产品分类、生产状态、是否出口 */
			//FSN新系统上线后才有的字段
			sb.append(" GROUP BY pro.id ) test");
			/* 执行sql  */
			Query query = entityManager.createNativeQuery(sb.toString());
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			List<Object[]> objects = query.getResultList();
			return setBusQueryProList(objects);
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getBusQueryProList()根据企业名称获取该企业销售的产品列表,出现异常！",e);
		}
	}

	/**
	 * 封装根据企业名称获取该企业销售的产品列表信息
	 * @author ChenXiaolin 2015-12-14
	 * @param objects
	 * @return
	 */
	private List<TzQueryResponseProListVO> setBusQueryProList(
			List<Object[]> objects)throws DaoException {
		
		 List<TzQueryResponseProListVO> list = new ArrayList<TzQueryResponseProListVO>();
		 TzQueryResponseProListVO vo = null;
		 if(objects!=null&&objects.size()>0){
			 for(int i = 0;i<objects.size();i++){
				 Object[] object = objects.get(i);
				 if(object!=null&&object.length>0){
					 vo = new TzQueryResponseProListVO();
					 vo.setProId(object[0]!=null?object[0].toString():"");			//产品ID
					 vo.setProName(object[1]!=null?object[1].toString():"");		//产品名称
					 vo.setProBarcode(object[2]!=null?object[2].toString():"");		//条形码
					 vo.setProFormat(object[3]!=null?object[3].toString():"");		//规格型号
					 vo.setProCategory(getFirstCattegory(object[4]!=null?object[4].toString():""));	//产品分类--一级分类
					 vo.setProQs(getQsByProId(vo.getProId()));			//生产许可
					 vo.setStandardStr(getStandarByProId(vo.getProId()));	//执行标准
					 vo.setIssueUnit("");		//发证单位--待FSN新版本才有的字段
					 vo.setIssueDate("");		//发证时间--待FSN新版本才有的字段
					 vo.setValidDate("");		//有效期至--待FSN新版本才有的字段
					 vo.setProStatus("");		//生产状态--待FSN新版本才有的字段
					 vo.setWhetherEexport("");	//是否出口--待FSN新版本才有的字段
					 vo.setSalesArea("");		//销售区域--待FSN新版本才有的字段
					 vo.setRelationType("");	//销售类型--待FSN新版本才有的字段
					 vo.setCheckWay("");		//检验方式--待FSN新版本才有的字段
					 list.add(vo);
				 }
			 }
		 }
		return list;
	}

	/**
	 * 根据产品ID获取执行标准
	 * @author ChenXiaolin 2015-12-14
	 * @param proId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getStandarByProId(String proId)throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT cateInfo.name FROM product_to_regularity regluar");
			sb.append(" INNER JOIN product_category_info cateInfo ON cateInfo.id = regluar.regularity_id");
			sb.append(" WHERE regluar.product_id = ?1 LIMIT 0,1");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, Long.parseLong(proId));
			List<Object> list = query.getResultList();
			if(list!=null&&list.size()>0){
				return list.get(0).toString();
			}
			return "";
		} catch (NumberFormatException e) {
			throw new DaoException("TzQueryDaoImpl-->getStandarByProId()根据产品ID获取执行标准,出现异常！",e);
		}
	}

	/**
	 * 根据产品ID获取qs
	 * @author ChenXiaolin 2015-12-14
	 * @param proId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getQsByProId(String proId)throws DaoException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT prolic.qs_no FROM product_to_businessunit protobus");
			sb.append(" INNER JOIN production_license_info prolic ON protobus.qs_id = prolic.id");
			sb.append(" WHERE protobus.PRODUCT_ID = ?1 LIMIT 0,1");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter(1, Long.parseLong(proId));
			List<Object> list = query.getResultList();
			if(list!=null&&list.size()>0){
				return list.get(0).toString();
			}
			return "";
		} catch (NumberFormatException e) {
			throw new DaoException("TzQueryDaoImpl-->getQsByProId()根据产品ID获取qs,出现异常！",e);
		}
	}

	/**
	 * 根据企业名称获取该企业销售的产品列表总记录数
	 * @author ChenXiaolin 2015-12-14
	 * @param paramVO
	 * @return
	 * @throws DaoException
	 */
	@Override
	public Long getBusQueryProListTotal(TzQueryRequestParamVO paramVO)
			throws DaoException {
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT COUNT(DISTINCT pro.id) FROM tz_business_account account");
			sb.append(" INNER JOIN business_unit bus ON bus.id = account.out_business_id");
			sb.append(" INNER JOIN tz_business_account_info acInfo ON acInfo.business_account_id = account.id");
			sb.append(" INNER JOIN product pro ON pro.id = acInfo.product_id");
			sb.append(" WHERE bus.name = '").append(paramVO.getBusName()).append("'");
			
			if(StringUtil.isNotEmpty(paramVO.getLicenseNo())){//营业执照号
				sb.append(" AND bus.license_no = '").append(paramVO.getLicenseNo()).append("'");
			}
			/* 查询条件  */
			if(StringUtil.isNotEmpty(paramVO.getProName())){//产品名称
				sb.append(" AND pro.name LIKE '%").append(paramVO.getProName()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getFormat())){//规格型号
				sb.append(" AND pro.format LIKE '%").append(paramVO.getFormat()).append("%'");
			}
			if(StringUtil.isNotEmpty(paramVO.getProBarcode())){//产品条形码
				sb.append(" AND pro.barcode LIKE '%").append(paramVO.getProBarcode()).append("%'");
			}
			/* 产品分类、生产状态、是否出口 */
			//FSN新系统上线后才有的字段
			
			/* 执行sql  */
			Query query = entityManager.createNativeQuery(sb.toString());
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getBusQueryProListTotal()根据企业名称获取该企业销售的产品列表总记录数,出现异常！",e);
		}
	}

	/**
	 * 根据企业名称获取原材料信息列表
	 * @param paramVO
	 * @return
	 * @throws DaoException
     */
	@Override
	public List<ProcurementInfo> getRawMaterialInfoList(TzQueryRequestParamVO paramVO,int type) throws DaoException {
		try {
			List<ProcurementInfo> list = new ArrayList<ProcurementInfo>();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT p.* FROM procurement_info p ");
			sql.append("LEFT JOIN business_unit b ON b.organization=p.organization_id ");
			sql.append("WHERE b.`name`=?1 AND p.type=?2  ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND  p.name LIKE ?3  ");
			}
			sql.append(" ORDER BY p.procurement_date desc ");
			Query query = entityManager.createNativeQuery(sql.toString(),ProcurementInfo.class);
			query.setParameter(1, paramVO.getBusName());
			query.setParameter(2, type);
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(3, "%"+paramVO.getProName()+"%");
			}
			if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
				query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
				query.setMaxResults(paramVO.getPageSize());
			}
			list=query.getResultList();
			return list;
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getRawMaterialInfoList()根据企业名称获取原材料信息列表！",e);
		}
	}

	/**
	 * 根据企业名称获取原材料信息数量
	 * @param paramVO
	 * @return
	 * @throws DaoException
     */
	@Override
	public Long getRawMaterialInfoTotal(TzQueryRequestParamVO paramVO,int type) throws DaoException {
		try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT count(*) FROM procurement_info p ");
            sql.append("LEFT JOIN business_unit b ON b.organization=p.organization_id ");
            sql.append("WHERE b.`name`=?1 AND p.type=?2 ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND  p.name LIKE ?3  ");
			}
			Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1, paramVO.getBusName());
			query.setParameter(2, type);
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(3, "%"+paramVO.getProName()+"%");
			}
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			throw new DaoException("TzQueryDaoImpl-->getRawMaterialInfoTotal()根据企业名称获取原材料信息数量！",e);
		}
	}

    /**
     * 根据企业名称获取人员信息列表
     * @param paramVO
     * @return
     * @throws DaoException
     */
    @Override
    public List<Member> getMemberInfoList(TzQueryRequestParamVO paramVO) throws DaoException {
        try {
            List<Member> list = new ArrayList<Member>();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT m.* FROM member m LEFT JOIN business_unit b ON m.orgId=b.id ");
            sql.append("WHERE b.`name`=?1  ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND ( m.name LIKE ?2 OR m.position LIKE ?3 OR m.identificationNo LIKE ?4 )");
			}
			sql.append(" ORDER BY m.id desc  ");
            Query query = entityManager.createNativeQuery(sql.toString(),Member.class);
            query.setParameter(1, paramVO.getBusName());
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(2, "%"+paramVO.getProName()+"%");
				query.setParameter(3, "%"+paramVO.getProName()+"%");
				query.setParameter(4, "%"+paramVO.getProName()+"%");
			}
            if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
                query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
                query.setMaxResults(paramVO.getPageSize());
            }
            list=query.getResultList();
            return list;
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getMemberInfoList()根据企业名称获取人员信息列表！",e);
        }
    }

    /**
     * 根据企业名称获取人员信息数量
     * @param paramVO
     * @return
     * @throws DaoException
     */
    @Override
    public Long getMemberInfoTotal(TzQueryRequestParamVO paramVO) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT count(*) FROM member m LEFT JOIN business_unit b ON m.orgId=b.id ");
            sql.append("WHERE b.`name`=?1 ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND ( m.name LIKE ?2 OR m.position LIKE ?3 OR m.identificationNo LIKE ?4 )");
			}
            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1, paramVO.getBusName());
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(2, "%"+paramVO.getProName()+"%");
				query.setParameter(3, "%"+paramVO.getProName()+"%");
				query.setParameter(4, "%"+paramVO.getProName()+"%");
			}
            return Long.parseLong(query.getSingleResult().toString());
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getMemberInfoTotal()根据企业名称获取人员信息数量！",e);
        }
    }

    /**
     * 根据企业名称获取设备信息列表
     * @param paramVO
     * @return
     * @throws DaoException
     */
    @Override
    public List<FacilityInfo> getFacilityInfoList(TzQueryRequestParamVO paramVO) throws DaoException {
        try {
            List<FacilityInfo> list = new ArrayList<FacilityInfo>();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT f.* FROM facility_info f LEFT JOIN business_unit b ON f.business_id=b.id ");
            sql.append("WHERE b.`name`=?1 ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND f.facility_name LIKE ?2 ");
			}
			sql.append(" ORDER BY f.buying_time desc ");
            Query query = entityManager.createNativeQuery(sql.toString(),FacilityInfo.class);
            query.setParameter(1, paramVO.getBusName());
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(2, "%"+paramVO.getProName()+"%");
			}
            if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
                query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
                query.setMaxResults(paramVO.getPageSize());
            }
            list=query.getResultList();
            return list;
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getFacilityInfoList()根据企业名称获取设备信息列表！",e);
        }
    }

    /**
     * 根据企业名称获取设备信息数量
     * @param paramVO
     * @return
     * @throws DaoException
     */
    @Override
    public Long getFacilityInfoTotal(TzQueryRequestParamVO paramVO) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT count(*) FROM facility_info f LEFT JOIN business_unit b ON f.business_id=b.id ");
            sql.append("WHERE b.`name`=?1 ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND f.facility_name LIKE ?2 ");
			}
            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1, paramVO.getBusName());
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(2, "%"+paramVO.getProName()+"%");
			}
            return Long.parseLong(query.getSingleResult().toString());
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getFacilityInfoTotal()根据企业名称获取设备信息数量！",e);
        }
    }

    /**
     * 根据企业名称获取规模信息列表
     * @param paramVO
     * @return
     * @throws DaoException
     */
    @Override
    public OperateInfo getOperateInfo(TzQueryRequestParamVO paramVO) throws DaoException {
        try {
            List<OperateInfo> list = new ArrayList<OperateInfo>();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM operate_info o LEFT JOIN business_unit b ON o.business_id=b.id ");
            sql.append("WHERE b.`name`=?1 ");
            Query query = entityManager.createNativeQuery(sql.toString(),OperateInfo.class);
            query.setParameter(1, paramVO.getBusName());
            list=query.getResultList();
            if(list!=null&&list.size()>0){
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getOperateInfo()根据企业名称获取规模信息列表！",e);
        }
    }

    /**
     * 根据id获取原材料使用记录信息列表
     * @param paramVO
     * @param rId
     * @return
     * @throws DaoException
     */
    @Override
    public List<ProcurementUsageRecord> getProcurementUsageRecordList(TzQueryRequestParamVO paramVO, Long rId) throws DaoException {
        try {
            List<ProcurementUsageRecord> list = new ArrayList<ProcurementUsageRecord>();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM procurement_usage_record WHERE procurement_id=?1 ORDER BY use_date desc");
            Query query = entityManager.createNativeQuery(sql.toString(),ProcurementUsageRecord.class);
            query.setParameter(1, rId);
            if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
                query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
                query.setMaxResults(paramVO.getPageSize());
            }
            list=query.getResultList();
            return list;
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getProcurementUsageRecordList()根据id获取原材料使用记录信息列表！",e);
        }
    }

    /**
     * 根据id获取原材料使用记录信息数量
     * @param paramVO
     * @param rId
     * @return
     * @throws DaoException
     */
    @Override
    public Long getProcurementUsageRecordTotal(TzQueryRequestParamVO paramVO, Long rId) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT count(*) FROM procurement_usage_record WHERE procurement_id=?1 ");
            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1, rId);
            return Long.parseLong(query.getSingleResult().toString());
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getProcurementUsageRecordTotal()根据id获取原材料使用记录信息数量！",e);
        }
    }

    /**
     * 根据id获取原材料后续处理信息列表
     * @param paramVO
     * @return
     * @throws DaoException
     */
    @Override
    public List<ProcurementDispose> getProcurementDisposeList(TzQueryRequestParamVO paramVO, int type) throws DaoException {
        try {
            List<ProcurementDispose> list = new ArrayList<ProcurementDispose>();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT p.* FROM procurement_dispose p LEFT JOIN business_unit b ON b.organization=p.organization_id  WHERE b.name=?1 AND p.type=?2 ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND  p.procurement_name LIKE ?3  ");
			}
			sql.append(" ORDER BY dispose_date desc ");
            Query query = entityManager.createNativeQuery(sql.toString(),ProcurementDispose.class);
            query.setParameter(1, paramVO.getBusName());
			query.setParameter(2, type);
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(3, "%"+paramVO.getProName()+"%");
			}
            if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
                query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
                query.setMaxResults(paramVO.getPageSize());
            }
            list=query.getResultList();
            return list;
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getProcurementDisposeList()根据id获取原材料后续处理信息列表！",e);
        }
    }

    /**
     * 根据id获取原材料后续处理信息数量
     * @param paramVO
     * @return
     * @throws DaoException
     */
    @Override
    public Long getProcurementDisposeTotal(TzQueryRequestParamVO paramVO, int type) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer();
			sql.append("SELECT count(*) FROM procurement_dispose p LEFT JOIN business_unit b ON b.organization=p.organization_id  WHERE b.name=?1 AND p.type=?2 ");
			if(StringUtils.isNotBlank(paramVO.getProName())){
				sql.append(" AND  p.procurement_name LIKE ?3  ");
			}
            Query query = entityManager.createNativeQuery(sql.toString());
			query.setParameter(1, paramVO.getBusName());
			query.setParameter(2, type);
			if(StringUtils.isNotBlank(paramVO.getProName())){
				query.setParameter(3, "%"+paramVO.getProName()+"%");
			}
            return Long.parseLong(query.getSingleResult().toString());
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getProcurementDisposeTotal()根据id获取原材料后续处理信息数量！",e);
        }
    }

    /**
     * 根据设备id获取设备养护记录信息列表
     * @param paramVO
     * @param fId
     * @return
     * @throws DaoException
     */
    @Override
    public List<FacilityMaintenanceRecord> getFacilityMaintenanceRecordList(TzQueryRequestParamVO paramVO, Long fId) throws DaoException {
        try {
            List<FacilityMaintenanceRecord> list = new ArrayList<FacilityMaintenanceRecord>();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM facility_maintenance_record WHERE facility_id=?1 ORDER BY maintenance_time desc");
            Query query = entityManager.createNativeQuery(sql.toString(),FacilityMaintenanceRecord.class);
            query.setParameter(1, fId);
            if(paramVO.getPage()>0&&paramVO.getPageSize()>0){
                query.setFirstResult((paramVO.getPage()-1)*paramVO.getPageSize());
                query.setMaxResults(paramVO.getPageSize());
            }
            list=query.getResultList();
            return list;
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getFacilityMaintenanceRecordList()根据设备id获取设备养护记录信息列表！",e);
        }
    }

    /**
     * 根据设备id获取设备养护记录信息数量
     * @param paramVO
     * @param fId
     * @return
     * @throws DaoException
     */
    @Override
    public Long getFacilityMaintenanceRecordTotal(TzQueryRequestParamVO paramVO, Long fId) throws DaoException {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT count(*) FROM facility_maintenance_record WHERE facility_id=?1 ");
            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter(1, fId);
            return Long.parseLong(query.getSingleResult().toString());
        } catch (Exception e) {
            throw new DaoException("TzQueryDaoImpl-->getFacilityMaintenanceRecordTotal()根据设备id获取设备养护记录信息数量！",e);
        }
    }

}
