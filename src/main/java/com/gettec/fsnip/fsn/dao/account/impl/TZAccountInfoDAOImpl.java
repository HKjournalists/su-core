package com.gettec.fsnip.fsn.dao.account.impl;

import com.gettec.fsnip.fsn.dao.account.TZAccountInfoDAO;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.account.TZAccountInfo;
import com.gettec.fsnip.fsn.util.sales.SalesUtil;
import com.gettec.fsnip.fsn.vo.account.ProductionDateVO;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * desc:
 */
@Repository(value = "tZAccountInfoDAO")
public class TZAccountInfoDAOImpl extends BaseDAOImpl<TZAccountInfo> implements TZAccountInfoDAO {

	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 查看进货时加载产品详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> getPurchaseList(Long id, int type,Long curBusId,int page, int pageSize)
			throws DaoException {
		String sql = " SELECT pro.id,pro.name,pro.barcode,pro.format,tzInfo.qs_no,tzInfo.product_batch,"+
				" tzInfo.production_date,tzInfo.over_date,tzInfo.product_num,tzInfo.product_price,"+
				" pro.expiration_date,tzInfo.report_id FROM tz_business_account_info tzInfo LEFT JOIN product pro ON"+
				" pro.id = tzInfo.product_id WHERE tzInfo.business_account_id = ?1";
		try {
			Query query = entityManager.createNativeQuery(sql).setParameter(1, id);
			if(page>0) {
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return encapsulationResultToReturnProductVO(result,type,0,curBusId);//进货 0:在这表示无意义为了凑出这个方法的参数个数，批发调用
		}catch (Exception e){
			e.printStackTrace();
			throw new DaoException("TZAccountInfoDAOImpl.getPurchaseList()根据台账ID加载进货的产品,出现异常！",e);
		}
	}

	/**
	 * 封装查看进货、批发时加载产品详情
	 * @throws Exception
	 */
	private List<ReturnProductVO> encapsulationResultToReturnProductVO( List<Object[]> result,int type,int busType,Long curBusId) throws Exception {
		List<ReturnProductVO> lists = null;
		if(result!=null&&result.size()>0) {
			lists = new ArrayList<ReturnProductVO>();
			for(int i = 0; i < result.size(); i++) {
				ReturnProductVO vo = new ReturnProductVO();
				Object[] objs = result.get(i);
				Long proId = objs[0] != null ? Long.valueOf(objs[0].toString()) : -1;//产品ID
				String qs = objs[4] != null ? objs[4].toString() : "";//qs号
				vo.setUuid(SalesUtil.createGUID());
				vo.setProductId(proId);
				vo.setName(objs[1] != null ? objs[1].toString() : "");
				vo.setBarcode(objs[2] != null ? objs[2].toString() : "");
				vo.setFormat(objs[3] != null ? objs[3].toString() : "");
				vo.setQsNumber(qs);
				vo.setBatch(objs[5] != null ? objs[5].toString() : "");
				vo.setProductionDate(objs[6] != null ? objs[6].toString() : "");
				vo.setOverDate(objs[7] != null ? objs[7].toString() : "");
				vo.setReturnCount(objs[8] != null ? Long.valueOf(objs[8].toString()) : 0);
				vo.setPrice(new BigDecimal(objs[9] != null && !"".equals(objs[9].toString()) ? objs[9].toString() : 0 + ""));
				String day = objs[10] != null ? objs[10].toString() : "";
				vo.setExpday(Integer.parseInt(day));
				vo.setReportId(objs[11] != null ? Long.parseLong(objs[11].toString()) : 0L);
				List<ProductionDateVO> birthDateList = null;
				if(busType==1){//1：代表生产企业
					vo.setBusType(1);
					birthDateList = getBirthDateByProductId(vo.getProductId(),day,1);//生产企业调用
				}else{
    				 /* 根据当前登录企业ID、产品ID、qs号获取库存中库存数量 */
					Long total = getProductCount(curBusId,proId,qs);
					vo.setCount(total);
					birthDateList = getBirthDateByProductId(vo.getProductId(),day,0);//非生产企业调用
				}

				vo.setBirthDateList(birthDateList);
				lists.add(vo);
			}
		}
		return lists;
	}

	/**
	 *  //根据当前登录企业ID、产品ID、qs号获取库存中库存数量
	 * @param curBusId
	 * @param proId
	 * @param qs
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Long getProductCount(Long curBusId, Long proId, String qs) throws Exception {
		try {
			String sql = " SELECT product_num FROM tz_stock WHERE business_id = ?1 AND product_id = ?2 AND qs_no = ?3 ";
			Query query = entityManager.createNativeQuery(sql).setParameter(1,curBusId)
					.setParameter(2, proId).setParameter(3,qs);
			List<BigInteger> list = query.getResultList();
			if(list!=null&&list.size()>0){
				Long lon = list.get(0).longValue();
				return lon;
			}
			return 0l;
		} catch (Exception e) {
			throw new Exception("TZAccountInfoDAOImpl.getProductCount()根据当前登录企业ID、产品ID、qs号获取库存中库存数量,出现异常！",e);
		}
	}

	/**
	 * 封装查看收货时加载产品详情
	 */
	private List<ReturnProductVO> receiptProductVO( List<Object[]> result) throws ParseException {
		List<ReturnProductVO> lists = null;
		if(result!=null&&result.size()>0) {
			lists = new ArrayList<ReturnProductVO>();
			for(int i = 0; i < result.size(); i++) {
				Object[] objs = result.get(i);
				ReturnProductVO vo = new ReturnProductVO();
				vo.setUuid(SalesUtil.createGUID());
				vo.setProductId(Long.valueOf(objs[0].toString()));//产品ID
				vo.setName(objs[1] != null ? objs[1].toString() : "");//产品名称
				vo.setBarcode(objs[2] != null ? objs[2].toString() : "");//条形码
				vo.setFormat(objs[3] != null ? objs[3].toString() : "");//规格
				vo.setQsNumber(objs[4] != null ? objs[4].toString() : "");//QS
				vo.setCountTotal(objs[5] != null && !"".equals(objs[5]) ? Long.valueOf(objs[5].toString()) : 0l);//数量
				vo.setPrice(objs[6] != null && !"".equals(objs[6]) ? new BigDecimal(objs[6].toString()) : new BigDecimal( 0 + ""));//价格
				vo.setProductionDate(objs[7] != null && !"".equals(objs[7]) ? objs[7].toString() : "");//生产日期
				vo.setBatch(objs[8] != null ? objs[8].toString() :"");//批次
				vo.setOverDate(objs[9] != null ? objs[9].toString() :"");//过期日期
				vo.setId(objs[10] != null ? Long.valueOf(objs[10].toString()) : -1);
				vo.setReturnCount(objs[11] != null ? Long.valueOf(objs[11].toString()) :vo.getCountTotal());
				vo.setReportId(objs[12] != null ? Long.valueOf(objs[12].toString()) :0L);
				lists.add(vo);
			}
		}
		return lists;
	}

	/**
	 * 根据产品id加载实例的生产日期--查看收货、批发时
	 * not in(4,5,7) 表示商超审核通过后就可以做台账
	 */
	@SuppressWarnings("unchecked")
	private List<ProductionDateVO> getBirthDateByProductId(Long productId,String day,int type) {
		StringBuilder sb = new StringBuilder();
		String sqlStr1 = " SELECT DISTINCT pin.id, pin.production_date,pin.batch_serial_no,p.expiration_date FROM product p " +
				" RIGHT JOIN  product_instance pin on p.id = pin.product_id  " +
				" RIGHT JOIN test_result test on pin.id = test.sample_id and test.del = 0 " +
				"WHERE pin.product_id = ?1";
		sb.append(sqlStr1);
		if(type==1){
			sb.append(" AND test.publish_flag = 1");
		}{
			sb.append(" AND test.publish_flag not in(4,5,7)");
		}
		sb.append(" GROUP BY  pin.production_date,pin.batch_serial_no ORDER BY pin.production_date DESC ");
		Query query = entityManager.createNativeQuery(sb.toString()).setParameter(1,productId);
		List<Object[]> result = query.getResultList();
		List<ProductionDateVO> lists = null;
		if(result!=null&&result.size()>0){
			lists = new ArrayList<ProductionDateVO>();
			for(int i=0;i<result.size();i++){
				Object[] objs=result.get(i);
				ProductionDateVO vo = new ProductionDateVO();
				vo.setInstanceId(objs[0] != null ? objs[0].toString() : "");
				vo.setBatch(objs[2] != null ? objs[2].toString(): "");
				try {
					vo.setBirthDate(objs[1] != null ? SDF.format(SDF.parse(objs[1].toString())): "");
					if(day!=null&&!"".equals(day)){
						calculationOverDate(vo, Integer.parseInt(day));
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				lists.add(vo);
			}
		}
		return lists;
	}

	//根据生产日期和保质期天数计算过期日期
	private void calculationOverDate(ProductionDateVO vo, int expDay) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(vo.getBirthDate());
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.DATE, expDay);
		vo.setOverDate(sdf.format(cl.getTime()));


	}

	/**
	 * 获取查看进货时产品详情总数
	 */
	@Override
	public Long getPurchaseTotal(long id) throws DaoException {
		try {
			String sql = " select count(*) from tz_business_account_info tzInfo LEFT JOIN product pro on pro.id = tzInfo.product_id " +
					" WHERE tzInfo.business_account_id = ?1  ";
			Query query = entityManager.createNativeQuery(sql).setParameter(1,id);
			return Long.valueOf(query.getSingleResult().toString());
		} catch (Exception jpae) {
			throw new DaoException("TZAccountInfoDAOImpl.getPurchaseTotal()  按照台账Id获取商品总数时, 出现异常！", jpae);
		}
	}

	@Override
	public TZAccountInfo findByAccountId(Long id) throws DaoException {
		try {
			String condition = " WHERE business_account_id = ?1";
			List<TZAccountInfo> list = this.getListByCondition(condition,new Object[]{id});
			if(list.size()>0){
				return list.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountInfoDAOImpl.TZAccountInfo()  加载查看已经进货的产品的供应商信息, 出现异常！", jpae.getException());
		}
	}

	@Override
	public List<TZAccountInfo> getListByAccountId(Long id) throws DaoException {
		try {
			String condition = " WHERE business_account_id = ?1";
			return this.getListByCondition(condition,new Object[]{id});
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountInfoDAOImpl.getListByAccountId() 根据台账ID获取台账信息列表时, 出现异常！", jpae.getException());
		}
	}

	/**
	 * 根据台账ID加载收货的产品详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> getProList(long tzId,int page,int pageSize) throws DaoException {
		String sql = " SELECT * FROM (SELECT p.id pid,p.name pname,p.barcode pbarcode,p.format pformat,iout.qs_no qs,iout.product_num pronum,iout.product_price price," +
				" iout.production_date prodate,iout.product_batch batch,iout.over_date overdate,iout.id infoid,iout.real_num realnum,iout.report_id" +
				" FROM tz_business_account_info iout LEFT JOIN product p ON p.id = iout.product_id" +
				" WHERE iout.business_account_id = ?1 ) test ";
		try {
			Query query = entityManager.createNativeQuery(sql).setParameter(1, tzId);
			if(page >0 && pageSize > 0){
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return receiptProductVO(result);
		}catch (Exception e){
			throw new DaoException("TZAccountInfoDAOImpl.getProList()根据台账ID加载收货的产品详情,出现异常！",e);
		}
	}

	/**
	 * 根据台账ID加载收货的产品详情总数
	 */
	@Override
	public Long getTZReceiptTotal(long tzId) throws DaoException {
		String sql = " select count(*) FROM tz_business_account_info iout LEFT JOIN product p ON p.id = iout.product_id  " +
				" WHERE iout.business_account_id = ?1 ";
		try {
			Query query = entityManager.createNativeQuery(sql).setParameter(1, tzId);
			return  Long.valueOf(query.getSingleResult().toString());
		}catch (Exception e){
			throw new DaoException("TZAccountInfoDAOImpl.getTZReceiptTotal()根据台账ID加载收货的产品总数,出现异常！",e);
		}
	}

	/**
	 * 首页点击查看批发台账后进入新增页面然后加载产品详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReturnProductVO> loadingDetailGoods(Long tzId,int busType,int outStatus,Long curBusId, int page,
													int pageSize) throws DaoException {

		String sql = " select pro.id,pro.name,pro.barcode,pro.format,tzInfo.qs_no,tzInfo.product_batch,tzInfo.production_date," +
				" tzInfo.over_date,tzInfo.product_num,tzInfo.product_price,pro.expiration_date,tzInfo.report_id from tz_business_account_info tzInfo " +
				"LEFT JOIN product pro on pro.id = tzInfo.product_id " +
				" WHERE tzInfo.business_account_id = ?1";
		try {
			Query query = entityManager.createNativeQuery(sql).setParameter(1, tzId);
			if(page>0) {
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> result = query.getResultList();
			return encapsulationResultToReturnProductVO(result,outStatus,busType,curBusId);//批发
		}catch (Exception e){
			throw new DaoException("TZAccountInfoDAOImpl.getPurchaseList()根据台账ID加载进货的产品,出现异常！",e);
		}

	}

	/**
	 * 首页点击查看批发台账后进入新增页面然后加载产品详情总数
	 */
	@Override
	public long getloadingDetailGoodsTotal(Long tzId) throws DaoException {
		try {
			String sql = " SELECT count(*) FROM ( " +
					" select tzInfo.product_id,tzInfo.qs_no,tzInfo.product_batch " +
					" from tz_business_account_info tzInfo LEFT JOIN product pro on pro.id = tzInfo.product_id  " +
					" WHERE tzInfo.business_account_id = ?1 ) temp ";
			Query query = entityManager.createNativeQuery(sql).setParameter(1,tzId);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception jpae) {
			throw new DaoException("TZAccountInfoDAOImpl.getPurchaseTotal()  按照台账Id获取商品总数时, 出现异常！", jpae);
		}
	}

	@Override
	public List<TZAccountInfo> getTZInfolist(Long tzId) throws DaoException {
		try {
			String condition = " WHERE business_account_id = ?1";
			return this.getListByCondition(condition,new Object[]{tzId});
		} catch (JPAException jpae) {
			throw new DaoException("TZAccountInfoDAOImpl.getTZInfolist() 根据台账ID获取台账信息列表时, 出现异常！", jpae.getException());
		}
	}

	@Override
	public void deleteInfoByaccountId(long tZId) throws DaoException {
		String sql = "DELETE FROM tz_business_account_info WHERE business_account_id = ?1 ";
		Query query = entityManager.createNativeQuery(sql).setParameter(1, tZId);
		query.executeUpdate();

	}

	/**
	 * 根据报告i获取报告的pdf地址
	 * @param reportId
	 * @return
	 * @throws DaoException
	 */
	@Override
	public String getReportUrlByReportId(Long reportId) throws DaoException {
		String sql = " SELECT res.URL FROM  t_test_resource res " +
				"LEFT JOIN  t_test_report_to_resource rop ON rop.RESOURCE_ID = res.RESOURCE_ID " +
				"WHERE rop.REPORT_ID = ?1 LIMIT 0,1";
		try {
			Query query = entityManager.createNativeQuery(sql).setParameter(1, reportId);
			try {
				Object singleResult = query.getSingleResult();
				return singleResult!=null?singleResult.toString():null;
			} catch (NoResultException nor) {
				return null;
			}}catch (Exception e){
			throw new DaoException("TZAccountInfoDAOImpl.getReportUrlByReportId()根据报告i获取报告的pdf地址,出现异常！",e);
		}
	}


}