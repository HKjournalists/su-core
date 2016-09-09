package com.lhfs.fsn.dao.testReport.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.lhfs.fsn.dao.testReport.TestReportDao;
import com.lhfs.fsn.util.DateUtil;
import com.lhfs.fsn.vo.ProductInfoVO;
import com.lhfs.fsn.vo.ProductJGVO;


@Repository
public class TestReportDaoImpl extends BaseDAOImpl<TestResult> implements TestReportDao{

	@PersistenceContext(type=PersistenceContextType.TRANSACTION) private EntityManager entityManager;

	/**
	 * 按查询条件获取有pdf的报告总条数
	 * @throws DaoException 
	 */
	@Override
	public long countByConditionIsCanPublish(Map<String, Object> map) throws DaoException{
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			condition += " AND e.repAttachments IS NOT EMPTY";
			return this.count(condition, params);
		} catch (Exception e) {
			throw new DaoException("【DAO-error】按查询条件获取有pdf的报告总条数，出现异常", e);
		}
	}

	/**
	 * 按查询条件获取有pdf的报告集合
	 * @throws DaoException 
	 */
	@Override
	public List<TestResult> getListByIsHavePdfWithPage(int page, int pageSize, Map<String, Object> map) throws DaoException{
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			condition += " AND e.repAttachments IS NOT EMPTY order by e.lastModifyTime desc";
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按查询条件获取有pdf的报告集合，出现异常", jpae.getException());
		}
	}

	@SuppressWarnings({ "unchecked", "finally" })
	public List<TestResult> getResultListByIdAndType(long product_id, String test_report_type) {

		List<TestResult> result = new ArrayList<TestResult>();
		try {
			//entityManager.joinTransaction(); // 强制将该动作添加到事务
			result = entityManager.createNativeQuery(
					"select tr.* from test_result as tr, product_instance as pi where "+
							" tr.del = 0 and tr.sample_id = pi.id and "+
							" tr.publish_flag=:publishFlag and"+
							" pi.product_id = :pid and "+
							" tr.test_type = :type "+
							" order by pi.production_date desc",
							TestResult.class)
							.setParameter("pid", product_id)
							.setParameter("type", test_report_type)
							.setParameter("publishFlag",'1')
							.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(result != null && result.size()>0) {
				return result;
			}else{
				return null;
			}
		}
	}

	public String getSerial(long sampleId) {
		List<?> result = entityManager
				.createNativeQuery(
						"select serial from product_instance where id = :id")
						.setParameter("id", sampleId)
						.getResultList();
		if(result != null){
			return (String) result.get(0);
		}
		return null;
	}

	public String getTradeMark(long brandId) {
		List<?> result = entityManager
				.createNativeQuery(
						"select trademark from business_brand where id = :id")
						.setParameter("id", brandId)
						.getResultList();
		if(result != null){
			return (String) result.get(0);
		}
		return null;
	}

	public String getEnterprise(long brandId) {
		List<?> result = entityManager
				.createNativeQuery(
						"select name from business_unit where id = (select business_unit_id from business_brand where id = :id)")
						.setParameter("id", brandId)
						.getResultList();
		if(result != null){
			return (String) result.get(0);
		}
		return null;
	}



	public String getFormat(long sampleId) {
		List<?> result = entityManager
				.createNativeQuery(
						"select product.format from product where product.id in (select product_instance.product_id from product_instance where product_instance.id = :id)")
						.setParameter("id", sampleId)
						.getResultList();
		if(result != null && result.size()>0){
			return (String) result.get(0);
		}
		return null;
	}


	public String getTestee(long testeeId) {
		List<?> result = entityManager
				.createNativeQuery(
						"select name from business_unit where id = :id")
						.setParameter("id", testeeId)
						.getResultList();
		if(result != null){
			return  (String) result.get(0);
		}
		return null;
	}


	public String getBatchSN(long sampleId) {
		List<?> result = entityManager
				.createNativeQuery(
						"select batch_serial_no from product_instance where id = :id")
						.setParameter("id", sampleId)
						.getResultList();
		if(result != null){
			return (String) result.get(0);
		}
		return null;
	}


	public String getStatus(long sampleId) {
		List<?> result = entityManager
				.createNativeQuery(
						"select product.status from product where product.id in (select product_instance.product_id from product_instance where product_instance.id = :id)")
						.setParameter("id", sampleId)
						.getResultList();
		if(result != null && result.size()>0){
			return (String) result.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<TestProperty> getPropertyListByID(long id) {

		List<TestProperty> result = entityManager
				.createNativeQuery(
						"select * from test_property where test_result_id = :id",
						TestProperty.class)
						.setParameter("id", id)
						.getResultList();
		if(result != null && result.size() > 0){
			return result;
		}
		return null;
	}

	public TestRptJson getTestRptJson(long product_id, String test_report_type,
			int sn) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BusinessUnit getBusinessUnit(Long ProductID) {
		List<BusinessUnit> result = entityManager
				.createNativeQuery(
						"select * from business_unit where business_unit.id in "+
								" (select product.business_brand_id from product where product.id = ?1) "
								,BusinessUnit.class)
								.setParameter(1, ProductID.toString())
								.getResultList();
		if(result != null && result.size() > 0){
			return (BusinessUnit) result.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BusinessBrand getBrand(String barcode) {
		List<BusinessBrand> result = entityManager
				.createNativeQuery(
						"select * from business_brand where business_brand.id in (select product.business_brand_id from product where product.barcode = ?1)",
						BusinessBrand.class)
						.setParameter(1, barcode)
						.getResultList();
		if(result != null && result.size() > 0){
			return (BusinessBrand) result.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BusinessBrand getBrandBySampleID(Long id) {
		List<BusinessBrand> result = entityManager
				.createNativeQuery(
						"select * from business_brand where business_brand.id in (select product.business_brand_id from product where product.id in(select product_instance.product_id from product_instance where product_instance.id = ?1) )",
						BusinessBrand.class)
						.setParameter(1, id.toString())
						.getResultList();
		if(result != null && result.size() > 0){
			return (BusinessBrand) result.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TestResult getRptByIdAndTypeAndDate(long id,
			String test_report_type, String date) {
		if("企业自检".equals(test_report_type)){
			List<TestResult> result = entityManager
					.createNativeQuery(
							" select * from test_result where "+
									" test_result.del = 0 and test_result.sample_id in (select product_instance.id from product_instance where ( "+
									" product_instance.production_date between ?1 and ?2 and product_instance.product_id = ?3 "+
									" ) order by product_instance.batch_serial_no desc ) "+
									" and test_result.test_type like ?4 and test_result.publish_flag=:publishFlag ",
									TestResult.class)
									.setParameter(1, date + " 00:00:00")
									.setParameter(2, date + " 23:59:59")
									.setParameter(3, id)
									.setParameter(4, test_report_type)
									.setParameter("publishFlag", '1')
									.getResultList();
			if(result != null && result.size()>0){				
				return result.get(0);
			}
			else {
				ProductInstance proInstance=null;
				List<ProductInstance> proInstances=entityManager
						.createNativeQuery("select * from product_instance where "+
								" product_instance.production_date <= ?1 and product_instance.product_id = ?2 "+
								" and product_instance.id in(select test_result.sample_id from test_result where test_result.del = 0 and test_result.publish_flag=:publishFlag and test_result.test_type like ?3) "+
								" order by product_instance.production_date desc  ",ProductInstance.class)
								.setParameter(1, date + " 23:59:59")
								.setParameter(2, id)
								.setParameter("publishFlag", '1')
								.setParameter(3, test_report_type)
								.getResultList();
				if(proInstances != null && proInstances.size()>0){
					proInstance= proInstances.get(0);
				}else{
					List<ProductInstance> proInstances1=entityManager
							.createNativeQuery("select * from product_instance where "+
									" product_instance.production_date >= ?1 and product_instance.product_id = ?2 "+
									" and product_instance.id in(select test_result.sample_id from test_result where test_result.del = 0 and test_result.publish_flag=:publishFlag and test_result.test_type like ?3) "+
									" order by product_instance.production_date asc  ",ProductInstance.class)
									.setParameter(1, date + " 00:00:00")
									.setParameter(2, id)
									.setParameter("publishFlag", '1')
									.setParameter(3, test_report_type)
									.getResultList();
					if(proInstances1 != null && proInstances1.size()>0){
						proInstance= proInstances1.get(0);
					}else{
						return null;
					}
				}


				List<TestResult> result1 = entityManager
						.createNativeQuery(
								" select * from test_result where "+
										" test_result.del = 0 and test_result.sample_id = ?1 ",
										TestResult.class)
										.setParameter(1, proInstance.getId())
										.getResultList();
				if(result1 != null && result1.size()>0){				
					return result1.get(0);
				}else{
					return null;
				}
			}
		}
		else {
			List<TestResult> result = entityManager
					.createNativeQuery(
							" select test_result.* from test_result, product_instance where "+
									" test_result.del = 0 and test_result.sample_id = product_instance.id and "+
									" test_result.publish_flag=:publishFlag and "+
									" product_instance.product_id = ?1 and "+
									" test_result.test_type like ?2 "+
									" order by product_instance.production_date between ?3 and ?4 desc, product_instance.production_date desc",
									TestResult.class)
									.setParameter(1, id)
									.setParameter(2, test_report_type)
									.setParameter(3, date + " 00:00:00")
									.setParameter(4, date + " 23:59:59")
									.setParameter("publishFlag", '1')
									.getResultList();
			if(result != null && result.size()>0)
				return result.get(0);
			else 
				return null;
		}
	}

	@Override
	public List<BigInteger> getIDsByProductInstanceID(Long id) {
		@SuppressWarnings("unchecked")
		List<BigInteger> result = entityManager
		.createNativeQuery(
				"select id from test_result where del = 0 and sample_id like ?1")
				.setParameter(1, id.toString())
				.getResultList();
		if(result != null && result.size() > 0){
			return result;
		}
		return null;
	}
	/**
	 * 根据样品编号查询检测报告是否存在
	 * @param sampleNO 样品编号
	 * @return boolean （true：检测报告存在；false检测报告不存在）
	 * @author zhaWanNeng
	 * 最近更新时间：2015/3/13
	 */
	@Override
	public boolean findBySampleNO(String sampleNO) {
		String jpql = "SELECT count(id) FROM test_result where del = 0 and sample_no = ?1" ;
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, sampleNO);
		Object rtn = query.getSingleResult();
		if(rtn != null && Long.parseLong(rtn.toString()) > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean verifyExist(String barcode, String batchSeriaNo,
			String serviceOrder) {
		@SuppressWarnings("unchecked")
		List<TestResult> result = entityManager
		.createNativeQuery(
				" select * from test_result where "+
						" test_result.del = 0 and test_result.sample_id in (select product_instance.id from product_instance where ( "+
						" product_instance.batch_serial_no = ?1 and product_instance.product_id = (select product.id from product where product.barcode = ?2))) "+
						" and test_result.service_order = ?3",
						TestResult.class)
						.setParameter(1, batchSeriaNo)
						.setParameter(2, barcode)
						.setParameter(3, serviceOrder)
						.getResultList();
		if(result != null && result.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * 按userOrgName、pubFlag、backFlag查询报告集合(并分页)
	 * @throws DaoException 
	 */
	@Override
	public List<TestResult> getListByConditionWithPage(char pubFlag, 
			int page, int pageSize, Map<String, Object> map) throws DaoException{
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			if(pubFlag == '2'){   // 已退回
				condition += " order by e.backTime desc";
			}else if(pubFlag == '0' || pubFlag == '1'){ // 已发布
				condition += " order by e.receiveDate desc";
			}else{   // 未发布 
				condition += " order by e.lastModifyTime desc";
			}
			return this.getListByPage(page, pageSize, condition, params);
		} catch (JPAException jpae) {
			throw new DaoException("TestReportDaoImpl.getListByConditionWithPage()-->", jpae.getException());
		}
	}

	/**
	 * 功能描述：报告唯一性检查（报告编号、产品barcode、批次）
	 * @return  true  代表 当前报告是唯一的
	 * 			false 代表 已经存在类似的报告
	 * @throws DaoException
	 * @author ZhangHui 2015/6/5
	 */
	@Override
	public boolean checkUniquenessOfReport(Long reportId, String serviceOrder, String barcode, String batchNo) 
			throws DaoException{

		try{
			String condition = "";
			if(reportId !=null ) {
				condition = " and id != " + reportId;
			}

			String sql = "SELECT count(*) FROM test_result WHERE del = 0 and service_order= ?1  " + condition + " and sample_id IN " +
					"(SELECT id FROM product_instance WHERE batch_serial_no= ?2 AND product_id = " +
					"(SELECT id FROM product where barcode= ?3))";

			Object result = entityManager.createNativeQuery(sql)
					.setParameter(1, serviceOrder)
					.setParameter(2, batchNo)
					.setParameter(3, barcode)
					.getSingleResult();

			if(result != null && !result.toString().equals("0")){
				return true;
			}

			return false;
		}catch(Exception e){
			throw new DaoException("TestReportDaoImpl.checkUniquenessOfReport()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 功能描述：查找当前登录用户通过解析pdf增加的报告数量。
	 * @throws ServiceException 
	 * @author ZhangHui 2015/6/18
	 */
	@Override
	public long countOfPasePdf(String userName, Map<String, Object> map) throws DaoException {
		try {
			String condition = (String)map.get("condition");
			Object[] params = (Object[])map.get("params");
			if(condition == null) condition = "";
			condition = condition + " AND e.lastModifyUserName = '"+userName+"' AND e.dbflag = 'parse_pdf'";
			return this.count(condition, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("【DAO-error】查找当前登录用户通过解析pdf增加的报告数量，出现异常", e);
		}
	}

	/**
	 * 功能描述：查找用户通过解析pdf生成的报告
	 * @author ZhangHui 2015/6/18
	 * @throws ServiceException 
	 */
	@Override
	public List<TestResult> getListOfPasePdfByPage(String userName, int page, int pageSize, Map<String, Object> map) 
			throws DaoException {
		try {
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			if(condition == null) condition = "";
			condition = condition + " AND e.lastModifyUserName = '"+userName+"' AND e.dbflag = 'parse_pdf' order by e.lastModifyTime desc";
			return this.getListByPage(page, pageSize, condition, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("【DAO-error】查找当前登录用户通过解析pdf增加的报告列表，出现异常", e);
		}
	}

	/**
	 * 根据产品id和检测类型统计改产品的报告数量
	 */
	@Override
	public long countByProductIdAndTestType(Long productId, String testType)
			throws DaoException {
		try{
			String sql="select count(report.id) from test_result report"+
					" LEFT JOIN product_instance sample ON sample.id=report.sample_id"+
					" where sample.product_id= ?1 and report.del = 0 and report.test_type= ?2 and report.publish_flag=1";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, productId);
			query.setParameter(2, testType);
			return Long.parseLong(query.getSingleResult().toString());
		}catch(Exception e){
			throw new DaoException("【dao-Exception】根据产品id和检测类型统计改产品的报告数量时，出现异常！",e);
		}
	}
	/**
	 * 根据产品id、生产日期和检测类型统计改产品的报告数量
	 * @throws DaoException 
	 */
	@Override
	public long countByProductIdAndTestTypeWithProductdate(Long productId, String testType,String productDate) throws DaoException {
		try{
			String sql="select count(report.id) from test_result report"+
					" LEFT JOIN product_instance sample ON sample.id=report.sample_id"+
					" where sample.product_id= ?1 and report.del = 0 and report.test_type= ?2 and report.publish_flag=1 and sample.production_date=?3";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, productId);
			query.setParameter(2, testType);
			query.setParameter(3, productDate);
			return Long.parseLong(query.getSingleResult().toString());
		}catch(Exception e){
			throw new DaoException("【dao-Exception】根据产品id、生产日期和检测类型统计改产品的报告数量时，出现异常！",e);
		}
	}
	/**
	 * 根据报告id查找businessId
	 */
	@Override
	public Long getBusIdBytestReportId(Long id) throws DaoException {
		try{
			String sql=" select DISTINCT pro_inst.producer_id from test_result sample " +
					" LEFT JOIN product_instance pro_inst on pro_inst.id=sample.sample_id "+ 
					" where pro_inst.producer_id=?1 ";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, id );
			return Long.parseLong(query.getSingleResult().toString());
		}catch(Exception e){
			throw new DaoException("【dao-Exception】根据报告id查找businessId时，出现异常！",e);
		}
	}

	/**
	 * 根据产品id查找距离指定生产日期最近的报告
	 */
	@Override
	public TestResult findByProductIdAndproductionDate(Long proId, String date,String type) throws DaoException {
		try{
			String sql="SELECT rep.* FROM `product_instance` samp LEFT JOIN test_result rep " +
					"ON samp.id=rep.sample_id " +
					"WHERE samp.production_date is not null " +
					"AND samp.product_id= ?1 " +
					"AND rep.publish_flag='1' " +
					"AND rep.test_type = ?2 " + 
					"AND samp.production_date = ?3 ";
			//"ORDER BY abs(UNIX_TIMESTAMP( production_date) - UNIX_TIMESTAMP(?3)) ASC LIMIT 0,1";
			List<TestResult> result = this.getListBySQL(TestResult.class, sql, new Object[]{proId , type , date});
			if(result != null && result.size() > 0){
				List<TestProperty> items = this.getItemsByReportId(result.get(0).getId());
				result.get(0).setTestProperties(items);
				return result.get(0);
			}
			return null;
		}catch(JPAException jpae){
			throw new DaoException("TestReportDaoImpl.findByProductIdAndproductionDate() "+jpae.getMessage(),jpae);
		}
	}

	//cxl
	@Override
	public List<TestResult> getReportBySampleId(Object sampleId)throws DaoException {
		try{
			String condition = " where e.sample.id = ?1 and e.del = 0 ";
			//String condition = " where e.sample.id = ?1 e.publishFlag = '1' "; //只查询报告已发布的
			List<TestResult> result = this.getListByCondition(condition, new Object[]{Long.valueOf(sampleId+"")});
			return result.size()>0?result : null;
		}catch(JPAException jpae){
			throw new DaoException("TestReportDaoImpl.getReportBySampleId() "+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 根据产品id查找距离指定生产日期最近的报告
	 */
	@Override
	public List<TestProperty> getItemsByReportId(Long reportId) throws DaoException {
		try{
			String sql="SELECT item.* FROM `test_property` item WHERE item.test_result_id = ?1";
			List<TestProperty> result = this.getListBySQL(TestProperty.class, sql, new Object[]{reportId});
			return result;
		}catch(JPAException jpae){
			throw new DaoException("TestReportDaoImpl.getItemsByReportId() "+jpae.getMessage(),jpae);
		}
	}

	//根据产品id 查找该产品的报告总数
	@Override
	public Long getReportCountForProductId(Long id) throws DaoException {
		try{
			String sql="select count(report.id) from test_result report "+
					" LEFT JOIN product_instance sample ON sample.id=report.sample_id "+
					" where sample.product_id= ?1 and report.del = 0 and report.publish_flag=1";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			return Long.parseLong(query.getSingleResult().toString());
		}catch(Exception e){
			throw new DaoException("【dao-Exception】根据产品id和检测类型统计改产品的报告数量时，出现异常！",e);
		}
	}

	/**
	 * 根据条形码barcode获取所有自检报告的pdfUrl
	 * @param barcode 条形码
	 * @author 郝圆彬
	 */
	@Override
	public List<String> getSelfReportPdfUrlsByBarcode(String barcode,String batch)
			throws DaoException {
		try{
			List<String> result = null;

			if(StringUtils.isNotBlank(barcode)) {
				String sql = "SELECT t.fullPdfPath FROM test_result t "
						+"LEFT JOIN product_instance p ON p.id=t.sample_id "
						+"LEFT JOIN product s ON s.id=p.product_id "
						+"WHERE t.del = 0 AND s.barcode = ?1  AND t.test_type='企业自检' ";
				if(StringUtils.isNotBlank(batch)){
					sql += " AND p.batch_serial_no = ?2";
				}
				result = this.getListBySQLWithoutType(String.class, sql,StringUtils.isNotBlank(batch)? new Object[]{barcode,batch}:new Object[]{barcode});
			}
			return result;
		}catch(JPAException jpae){
			throw new DaoException("TestReportDaoImpl-->getReportBySampleId() "+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 更改报告的发布状态
	 * publishFlag 5：商超退回    6：商超通过   1：进口食品商超审核通过直接发布到大众门户
	 * @author ZhangHui 2015/4/9
	 */
	@Override
	public void updatePublishFlag(char publishFlag, long reportId, String msg,String checkOrgName) throws DaoException {
		try {
			String userName=AccessUtils.getUserName()!=null?AccessUtils.getUserName().toString():null;
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
				query.setParameter(5, userName);
			}
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("FromToBussinessDAOImpl.updatePublishFlag() 出现异常！" , e);
		}
	}

	/**
	 * 根据报告编号和来源标识，获取报告数量
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	public long countByServiceorderAndEdition(String serviceOrder,
			String edition) throws DaoException {
		try {
			String condition = " WHERE e.serviceOrder = ?1 AND e.edition = ?2 AND e.del = 0";
			return this.count(condition, new Object[]{serviceOrder, edition});
		} catch (JPAException e) {
			throw new DaoException("TestReportDaoImpl.countByServiceorderAndEdition()-->" + e.getException() , e.getException());
		}
	}

	/**
	 * 功能描述：关联报告的产品实例
	 * @author ZhangHui 2015/6/7
	 * @throws DaoException 
	 */
	@Override
	public void updateRecordOfSample(Long id, Long sample_id) throws DaoException {
		try {
			if(id==null || sample_id==null){
				throw new Exception("参数为空");
			}

			String sql = "UPDATE test_result SET sample_id = ?1 WHERE id = ?2";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, sample_id);
			query.setParameter(2, id);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("TestReportDaoImpl.updateRecordOfSample()-->" + e.getMessage() , e);
		}
	}

	/**
	 * 功能描述：关联报告的被检单位/人
	 * @author ZhangHui 2015/6/7
	 * @throws DaoException 
	 */
	@Override
	public void updateRecordOfTestee(Long id, Long testee_id) throws DaoException {
		try {
			if(id==null || testee_id==null){
				throw new Exception("参数为空");
			}

			String sql = "UPDATE test_result SET testee_id = ?1 WHERE id = ?2";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, testee_id);
			query.setParameter(2, id);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("TestReportDaoImpl.updateRecordOfTestee()-->" + e.getMessage() , e);
		}
	}

	/**
	 * 大众门户按日期查找报告最近的报告
	 *@author LongXianZhen 2015/06/02
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TestResult getRptListByIdAndTypeAndDate(long proId,
			String testType, String date,int sn,boolean portalFlag) throws DaoException {
		TestResult tr= getResultByIdAndType(proId,testType,sn,date,portalFlag);
		/*List<TestResult> result = entityManager
					.createNativeQuery(
							" select * from test_result where "+
							" test_result.del = 0 and test_result.sample_id in (select product_instance.id from product_instance where ( "+
							" product_instance.production_date between ?1 and ?2 and product_instance.product_id = ?3 "+
							" ) order by product_instance.batch_serial_no desc ) "+
							" and test_result.test_type like ?4 and test_result.publish_flag=:publishFlag ",
							TestResult.class)
							.setParameter(1, date + " 00:00:00")
							.setParameter(2, date + " 23:59:59")
							.setParameter(3, proId)
							.setParameter(4, testType)
							.setParameter("publishFlag", '1')
							.getResultList();*/
		if(tr != null ){	//查到当天有报告则返回报告集合			
			return tr;
		}else {//当天没有报告则当前前后分别查询出最近的一份报告
			List<ProductInstance> pInstances=new ArrayList<ProductInstance>();
			List<ProductInstance> proInstances=entityManager    //当前日期之前最近的报告
					.createNativeQuery("select * from product_instance where "+
							" product_instance.production_date <= ?1 and product_instance.product_id = ?2 "+
							" and product_instance.id in(select test_result.sample_id from test_result where test_result.del = 0 and test_result.publish_flag=:publishFlag and test_result.test_type like ?3) "+
							" order by product_instance.production_date desc  LIMIT 0,1 ",ProductInstance.class)
							.setParameter(1, date + " 23:59:59")
							.setParameter(2, proId)
							.setParameter("publishFlag", '1')
							.setParameter(3, testType)
							.getResultList();
			List<ProductInstance> proInstances1=entityManager   //当前日期之后最近的报告
					.createNativeQuery("select * from product_instance where "+
							" product_instance.production_date >= ?1 and product_instance.product_id = ?2 "+
							" and product_instance.id in(select test_result.sample_id from test_result where test_result.del = 0 and test_result.publish_flag=:publishFlag and test_result.test_type like ?3) "+
							" order by product_instance.production_date asc  LIMIT 0,1",ProductInstance.class)
							.setParameter(1, date + " 00:00:00")
							.setParameter(2, proId)
							.setParameter("publishFlag", '1')
							.setParameter(3, testType)
							.getResultList();
			if(proInstances.size()>0&&proInstances1.size()==0){
				pInstances.addAll(proInstances);
			}else if(proInstances.size()==0&&proInstances1.size()>0){
				pInstances.addAll(proInstances1);
			}else if(proInstances.size()>0&&proInstances1.size()>0){
				boolean falg=DateUtil.compareDate(proInstances.get(0).getProductionDate(),date,proInstances1.get(0).getProductionDate());
				if(falg){
					pInstances.addAll(proInstances);
				}else{
					pInstances.addAll(proInstances1);
				}
			}else{
				return null;
			}
			String date1=DateUtil.dateFormatYYYYMMDD(pInstances.get(0).getProductionDate());
			TestResult trt= getResultByIdAndType(proId,testType,sn,date1,portalFlag);
			if(trt!=null){
				return trt;
			}else{
				return null;
			}
			/*	List<TestResult> result1 =  entityManager
						.createNativeQuery(
								" select * from test_result where "+
								" test_result.del = 0 and test_result.sample_id in (select product_instance.id from product_instance where ( "+
								" product_instance.production_date between ?1 and ?2 and product_instance.product_id = ?3 "+
								" ) order by product_instance.batch_serial_no desc ) "+
								" and test_result.test_type like ?4 and test_result.publish_flag=:publishFlag ",
								TestResult.class)
								.setParameter(1, date1 + " 00:00:00")
								.setParameter(2, date1 + " 23:59:59")
								.setParameter(3, proId)
								.setParameter(4, testType)
								.setParameter("publishFlag", '1')
								.getResultList();
				if(result1 != null && result1.size()>0){				
					return result1;
				}else{
					return null;
				}*/
		}
	}

	/**
	 * 功能描述：根据报告id,删除/恢复报告
	 * @param del 
	 * 			0 代表恢复报告
	 * 			1 代表删除报告
	 * @author ZhangHui 2015/6/17
	 * @throws DaoException 
	 */
	@Override
	public void updateByDel(Long test_result_id, int del) throws DaoException {
		try {
			if(test_result_id == null){
				throw new Exception("参数为空");
			}

			String sql = "UPDATE test_result SET del = ?1 WHERE id = ?2";

			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, del);
			query.setParameter(2, test_result_id);

			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("TestReportDaoImpl.updateByDel()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 查找供应商的商超已审核通过的报告
	 * @author longxianzhen 20150807
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TestResult> getReportsOfDealerAllPassWithPage(String config,
			int page, int pageSize) throws DaoException {
		try {
			String sql = " SELECT tr.id,tr.service_order,tr.publish_flag,tr.last_modify_time," +
					"ip.batch_serial_no,ip.production_date,p.`name` ,tr.last_modify_user," +
					"tr.back_result,tr.back_time,p.barcode FROM test_result tr "+
					"LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
					"LEFT JOIN product p ON p.id=ip.product_id ";
			sql+=config;
			Query query = entityManager.createNativeQuery(sql);
			query.setFirstResult((page-1)*pageSize);
			query.setMaxResults(pageSize);
			List<Object[]> objs=query.getResultList();
			List<TestResult> trs= new ArrayList<TestResult>();
			if(objs!=null&&objs.size()>0){
				for(Object[] obj:objs){
					TestResult tr=new TestResult();
					tr.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
					tr.setServiceOrder(obj[1] != null ? obj[1].toString() : "");
					tr.setPublishFlag((Character)obj[2]);
					tr.setLastModifyTime(obj[3]==null?null:(Date)obj[3]);
					ProductInstance pi=new ProductInstance();
					pi.setBatchSerialNo(obj[4] != null ? obj[4].toString() : "");
					pi.setProductionDate(obj[5]==null?null:(Date)obj[5]);
					Product pro =new Product();
					pro.setName(obj[6] != null ? obj[6].toString() : "");
					pro.setPackageFlag('0');
					pro.setBarcode(obj[10] != null ? obj[10].toString() : "");
					pi.setProduct(pro);
					pi.getProduct().setPackageFlag('0');
					tr.setSample(pi);
					tr.setLastModifyUserName(obj[7] != null ? obj[7].toString() : "");
					tr.setBackResult(obj[8] != null ? obj[8].toString() : "");
					tr.setBackTime(obj[9]==null?null:(Date)obj[9]);
					trs.add(tr);
				}
			}
			return trs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("TestReportDaoImpl.getReportsOfDealerAllPassWithPage()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 查找供应商的商超已审核通过的报告数量
	 * @author longxianzhen 20150807
	 */
	@Override
	public long countOfDealerAllPass(String config) throws DaoException {
		try{
			String sql = " SELECT count(*) FROM test_result tr " +
					"LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
					"LEFT JOIN product p ON p.id=ip.product_id ";
			sql+=config;
			Query query=entityManager.createNativeQuery(sql);
			return Long.parseLong(query.getSingleResult().toString());
		}catch(Exception e){
			throw new DaoException("供应商已发布报告数量时，出现异常！",e);
		}
	}

	/**
	 * 根据产品id及报告检测类型查找该产品最新的报告
	 * @author longxianzhen 2015/08/07
	 * @throws DaoException 
	 */
	@Override
	public TestResult getResultByIdAndType(Long id, String testType ,int sn,String date,boolean portalFlag) throws DaoException {
		try {
			Query query;
			if(date!=null&&(!"".equals(date))){
				String sql = " SELECT tr.id,tr.`comment`,tr.pass,tr.test_date," +
						"ip.batch_serial_no,ip.production_date,tr.sample_quantity," +
						"tr.sampling_location,tr.sampling_date,tr.test_type,tr.standard," +
						"tr.result,tr.fullPdfPath,tr.interceptionPdfPath,tr.publish_flag," +
						"tr.service_order,tr.sample_no,tr.test_orgnization,tr.organization_name,tr.edition,ip.expiration_date FROM test_result tr " +
						"LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
						"LEFT JOIN product p ON p.id=ip.product_id WHERE ";
				if(!portalFlag){     
					sql +="DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND ";
				}
				sql +=" p.id=?1 AND " +
						//						 "LEFT JOIN t_meta_initialize_product tmip  ON tmip.product_id=p.id "+
						//						 "LEFT JOIN business_unit bu ON bu.organization=tmip.organization "+
						"tr.test_type=?2 AND tr.publish_flag='1'  AND ip.production_date between ?4 and ?5  ORDER BY ip.production_date DESC LIMIT ?3,1";
				query = entityManager.createNativeQuery(sql);
				query.setParameter(1, id);
				query.setParameter(2, testType);
				query.setParameter(3, sn-1);
				query.setParameter(4, date + " 00:00:00");
				query.setParameter(5, date + " 23:59:59");
			}else{
				String sql = " SELECT tr.id,tr.`comment`,tr.pass,tr.test_date," +
						"ip.batch_serial_no,ip.production_date,tr.sample_quantity," +
						"tr.sampling_location,tr.sampling_date,tr.test_type,tr.standard," +
						"tr.result,tr.fullPdfPath,tr.interceptionPdfPath,tr.publish_flag," +
						"tr.service_order,tr.sample_no,tr.test_orgnization,tr.organization_name,tr.edition,ip.expiration_date FROM test_result tr " +
						"LEFT JOIN test_result_handler trh on trh.test_result_id=tr.id AND trh.status=8 "+
						"LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
						"LEFT JOIN product p ON p.id=ip.product_id WHERE  ";
				if(!portalFlag){     
					sql +="DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND ";
				}
				sql +=" p.id=?1 AND " +
						"tr.test_type=?2 AND tr.publish_flag='1' ORDER BY trh.status desc,ip.production_date DESC LIMIT ?3,1";
				query = entityManager.createNativeQuery(sql);
				query.setParameter(1, id);
				query.setParameter(2, testType);
				query.setParameter(3, sn-1);
				//				if(objs==null||objs.size()<=0){
				//					sql = " SELECT tr.id,tr.`commeb nt`,tr.pass,tr.test_date," +
				//							 "ip.batch_serial_no,ip.production_date,tr.sample_quantity," +
				//							 "tr.sampling_location,tr.sampling_date,tr.test_type,tr.standard," +
				//							 "tr.result,tr.fullPdfPath,tr.interceptionPdfPath,tr.publish_flag," +
				//							 "tr.service_order,tr.sample_no,tr.test_orgnization,tr.organization_name FROM test_result tr " +
				//							 "LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
				//							 "LEFT JOIN product p ON p.id=ip.product_id WHERE p.id=?1 AND " +
				//							 "tr.test_type=?2 AND tr.publish_flag='1' ORDER BY ip.production_date DESC LIMIT ?3,1";
				//					query = entityManager.createNativeQuery(sql);
				//					query.setParameter(1, id);
				//					query.setParameter(2, testType);
				//					query.setParameter(3, sn-1);
				//					objs=query.getResultList();
				//				}
			}
			List<Object[]> objs=query.getResultList();
			TestResult tr=null; 
			if(objs!=null&&objs.size()>0){
				Object[] obj=objs.get(0);
				tr=new TestResult();
				tr.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
				tr.setComment(obj[1] != null ? obj[1].toString() : "");
				tr.setPass(obj[2] != null ? (Boolean)obj[2] : false);
				tr.setTestDate(obj[3] != null ? (Date)obj[3] : null);
				ProductInstance pi=new ProductInstance();
				pi.setBatchSerialNo(obj[4] != null ? obj[4].toString() : "");
				pi.setProductionDate(obj[5]==null?null:(Date)obj[5]);
				//过期日期
				pi.setExpirationDate(obj[20]==null?null:(Date)obj[20]);
				tr.setSample(pi);
				tr.setSampleQuantity(obj[6] != null ? obj[6].toString() : "");
				tr.setSamplingLocation(obj[7] != null ? obj[7].toString() : "");
				tr.setSamplingDate(obj[8] != null ? (Date)obj[8] : null);
				tr.setTestType(obj[9] != null ? obj[9].toString() : "");
				tr.setStandard(obj[10] != null ? obj[10].toString() : "");
				tr.setResult(obj[11] != null ? obj[11].toString() : "");
				tr.setFullPdfPath(obj[12] != null ? obj[12].toString() : "");
				tr.setInterceptionPdfPath(obj[13] != null ? obj[13].toString() : "");
				tr.setPublishFlag((Character)obj[14]);
				tr.setServiceOrder(obj[15] != null ? obj[15].toString() : "");
				tr.setSampleNO(obj[16] != null ? obj[16].toString() : "");
				tr.setTestOrgnization(obj[17] != null ? obj[17].toString() : "");
				tr.setOrganizationName(obj[18] != null ? obj[18].toString() : "");
				tr.setEdition(obj[19] != null ? obj[19].toString() : "");
			}
			return tr;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("TestReportDaoImpl.getResultByIdAndType()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 根据产品id及报告检测类型查找该产品报告数量
	 * @author longxianzhen 2015/08/07
	 */
	@Override
	public Integer countByIdAndType(Long id, String testType,String date,boolean portalFlag) throws DaoException {
		try{
			String condition=" ";
			if(date!=null&&(!"".equals(date))){
				condition=" AND ip.production_date between ?3 and ?4 ";
			}

			String sql = " SELECT count(*) FROM test_result tr " +
					"LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
					"LEFT JOIN product p ON p.id=ip.product_id WHERE ";
			if(!portalFlag){
				sql+="DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND ";
			}
			sql+="  p.id=?1 AND " +
					"tr.test_type=?2 AND tr.publish_flag='1' "+condition;
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			query.setParameter(2, testType);
			if(date!=null&&(!"".equals(date))){
				query.setParameter(3, date + " 00:00:00");
				query.setParameter(4, date + " 23:59:59");
			}
			Object result=query.getSingleResult();
			return result!=null?Integer.parseInt(result.toString()):0;
		}catch(Exception e){
			e.printStackTrace();
			throw new DaoException("供应商已发布报告数量时，出现异常！",e);
		}
	}

	/**
	 * 根据barcode验证该产品是否有已退回的报告没有处理
	 * @param barcode
	 * @return
	 */
	@Override
	public boolean verifyBackReportByBarcode(String barcode)
			throws DaoException {
		try {
			Long myOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String sql=" SELECT count(*) FROM test_result tr "+
					"LEFT JOIN product_instance ip ON ip.id=tr.sample_id "+
					"LEFT JOIN product p ON p.id=ip.product_id  "+
					"LEFT JOIN test_result_handler trh ON trh.test_result_id=tr.id  "+
					"WHERE DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND tr.del = 0  AND (tr.publish_flag IN ('5', '7') OR (tr.publish_flag=6 AND trh.status=1 AND tr.back_result is not null AND tr.back_result != '') )AND tr.organization = ?1 AND p.barcode=?2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, myOrganization);
			query.setParameter(2, barcode);
			Object result=query.getSingleResult();
			Integer count=result!=null?Integer.parseInt(result.toString()):0;
			return count==0?false:true;
		} catch (Exception e) {
			throw new DaoException("TestReportDaoImpl.verifyBackReportByBarcode()-->",e);
		}
	}

	/**
	 * 获取企业下的检测报告
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TestResult getTestResult(String barcode, long buId,
			long product_id, String type, String date) {
		String sql = " SELECT tr.id,tr.`comment`,tr.pass,tr.test_date," +
				"ip.batch_serial_no,ip.production_date,tr.sample_quantity," +
				"tr.sampling_location,tr.sampling_date,tr.test_type,tr.standard," +
				"tr.result,tr.fullPdfPath,tr.interceptionPdfPath,tr.publish_flag," +
				"tr.service_order,tr.sample_no,tr.test_orgnization,tr.organization_name,ip.expiration_date " +
				//					 "FROM  business_unit bu "+
				//					 "LEFT JOIN t_meta_initialize_product tmip   ON bu.organization=tmip.organization "+
				//					 "LEFT JOIN product p ON p.id=tmip.product_id  "+
				//					 "LEFT JOIN product_instance ip ON p.id=ip.product_id "+
				//					 "LEFT JOIN test_result tr ON  ip.id=tr.sample_id "+
				"FROM product p "+
				"LEFT JOIN  product_instance ip ON p.id=ip.product_id "+
				"LEFT JOIN business_unit bu ON ip.producer_id=bu.id "+
				"LEFT JOIN enterprise_registe er ON er.enterpritename = bu.name "+
				"LEFT JOIN test_result tr ON tr.sample_id=ip.id "+
				"WHERE DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND  bu.organization=tr.organization AND   bu.id=?1  AND  p.id=?2   AND  p.barcode=?3 AND "+
				"tr.test_type=?4 AND tr.publish_flag='1'  " ;
		if (date != null && !"".equals(date)) {
			sql += " AND ip.production_date between ?5 and ?6  ";
		}
		sql +=" ORDER BY tr.last_modify_time DESC ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, buId);
		query.setParameter(2, product_id);
		query.setParameter(3, barcode);
		query.setParameter(4, type);
		if (date != null && !"".equals(date)) {
			query.setParameter(5, date + " 00:00:00");
			query.setParameter(6, date + " 23:59:59");
		}
		List<Object[]> objs=query.getResultList();
		TestResult tr=null; 
		if(objs!=null&&objs.size()>0){
			Object[] obj=objs.get(0);
			tr=new TestResult();
			tr.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
			tr.setComment(obj[1] != null ? obj[1].toString() : "");
			tr.setPass(obj[2] != null ? (Boolean)obj[2] : false);
			tr.setTestDate(obj[3] != null ? (Date)obj[3] : null);
			ProductInstance pi=new ProductInstance();
			pi.setBatchSerialNo(obj[4] != null ? obj[4].toString() : "");
			pi.setProductionDate(obj[5]==null?null:(Date)obj[5]);
			pi.setExpirationDate(obj[19]==null?null:(Date)obj[19]);
			tr.setSample(pi);
			tr.setSampleQuantity(obj[6] != null ? obj[6].toString() : "");
			tr.setSamplingLocation(obj[7] != null ? obj[7].toString() : "");
			tr.setSamplingDate(obj[8] != null ? (Date)obj[8] : null);
			tr.setTestType(obj[9] != null ? obj[9].toString() : "");
			tr.setStandard(obj[10] != null ? obj[10].toString() : "");
			tr.setResult(obj[11] != null ? obj[11].toString() : "");
			tr.setFullPdfPath(obj[12] != null ? obj[12].toString() : "");
			tr.setInterceptionPdfPath(obj[13] != null ? obj[13].toString() : "");
			tr.setPublishFlag((Character)obj[14]);
			tr.setServiceOrder(obj[15] != null ? obj[15].toString() : "");
			tr.setSampleNO(obj[16] != null ? obj[16].toString() : "");
			tr.setTestOrgnization(obj[17] != null ? obj[17].toString() : "");
			tr.setOrganizationName(obj[18] != null ? obj[18].toString() : "");
		}
		return tr;
	}
	/**
	 * 获取企业下的检测报告数量
	 */
	@Override
	public Integer countByIdAndTypeOrg(long product_id, String type,
			String pDate, Long organization) {
		Integer count = 0;	
		try {
			String condition=" ";
			if(pDate!=null&&(!"".equals(pDate))){
				condition=" AND ip.production_date between ?4 and ?5 ";
			}
			String sql = " SELECT count(*) FROM test_result tr " +
					"LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
					"LEFT JOIN product p ON p.id=ip.product_id WHERE DATEDIFF(ip.expiration_date , SYSDATE()) > 0 AND  p.id=?1 AND " +
					"tr.test_type=?2 AND tr.organization=?3 AND tr.publish_flag='1' "+condition;
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, product_id);
			query.setParameter(2, type);
			query.setParameter(3, organization);
			if(pDate!=null&&(!"".equals(pDate))){
				query.setParameter(4, pDate + " 00:00:00");
				query.setParameter(5, pDate + " 23:59:59");
			}
			Object result=query.getSingleResult();
			count = result!=null?Integer.parseInt(result.toString()):0;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public TestResult findByIdPublishFlag(Long id) {
		String sql = "SELECT id,publish_flag FROM test_result WHERE id = ?1";
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1, id);
		List<Object[]> objs=query.getResultList();
		TestResult tr=null; 
		if(objs!=null&&objs.size()>0){
			Object[] obj=objs.get(0);
			tr = new TestResult();
			tr.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
			tr.setPublishFlag((Character)obj[1]);
		}
		return tr;
	}

	@Override
	public List<TestResult> getByLicenseNoTistResult(String licenseNo,
			String type, Integer page, Integer pageSize) {

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductInfoVO> getByLicenseNoProduct(String licenseNo,
			Integer page, Integer pageSize) {
		String sql = "SELECT p.id,p.name,p.barcode  FROM product  p ";
		sql += " LEFT JOIN business_unit bu ON bu.id=p.producer_id ";
		sql += " WHERE bu.license_no = ?1";
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1, licenseNo);
		// 判断是否分页查询
		if (page > 0) {
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
		}
		List<Object[]> objs = query.getResultList();
		List<ProductInfoVO> proList = new ArrayList<ProductInfoVO>();
		for (Object[] obj : objs) {
			ProductInfoVO vo = new ProductInfoVO();
			vo.setProductId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
			vo.setProductName(obj[1]==null?null:obj[1].toString());
			vo.setBarcode(obj[2]==null?null:obj[2].toString());
			proList.add(vo);
		}
		return proList;
	}

	@Override
	public Long getByLicenseNoProductCount(String licenseNo) {
		String sql = "SELECT COUNT(p.id)  FROM product  p ";
		sql += " LEFT JOIN business_unit bu ON bu.id=p.producer_id ";
		sql += " WHERE bu.license_no = ?1";
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1, licenseNo);
		Object result=query.getSingleResult();
		Long total=result!=null?Long.parseLong(result.toString()):0;
		return total ;
	}
	
	/**
	 * 查生产企业产品
	 * @param businessUnit
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<ProductJGVO> getProductByProducerBusinessUnit(BusinessUnit businessUnit, int page, int pageSize){
		String sql="select  p.id,p.name,p.format,p.status,p.product_type," +
				"(SELECT pci.name FROM product_category_info pci WHERE pci.id = p.category_id) categoryName ";
		sql +=" FROM product p where p.producer_id=?1";
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1, businessUnit.getId());
		query.setFirstResult((page-1)*pageSize);
		query.setMaxResults(pageSize);
		query.getResultList();
		List<Object[]> objs = query.getResultList();
		List<ProductJGVO> proList = new ArrayList<ProductJGVO>();
		for (Object[] obj : objs) {
			ProductJGVO e = new ProductJGVO();
			e.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
			e.setProName(obj[1]==null?null:obj[1].toString());
			e.setFormat(obj[2]==null?null:obj[2].toString());
			e.setStatus(obj[3]==null?null:obj[3].toString());
			e.setProductType(obj[4]==null?null:Integer.parseInt(obj[4].toString())==2?"是":"否");
			e.setCategoryName(obj[5]==null?null:obj[5].toString());
			proList.add(e);
		}
		return proList;
	}
	
	/**
	 * 查流通企业产品
	 * @param businessUnit
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<ProductJGVO> getProductByCirculationBusinessUnit(BusinessUnit businessUnit, int page, int pageSize) {
		String sql ="SELECT DISTINCT p.id,p.name,p.format,p.status,p.product_type,(SELECT pci.name FROM product_category_info pci WHERE pci.id = p.category_id) categoryName ";
		sql +=" FROM product p ";		
		sql +=" INNER JOIN  t_meta_from_to_business tb ON p.id = tb.pro_id ";		
		sql +=" INNER JOIN t_meta_enterprise_to_provider tp ON tp.provider_id = tb.from_bus_id AND tp.business_id = ?1";		
//		sql +=this.getConfig(licenseNo, qsNo,businessName, buslicenseNo);
		sql +=" WHERE  tb.del = false AND tb.to_bus_id = ?2";
//		sql +=this.getConfig(licenseNo, qsNo,businessName, buslicenseNo);
//		if (page > 0) {
//			int start  = 0;
//			int end  = pageSize;
//			start = (page - 1) * pageSize;
//			sql += " LIMIT  "+start+","+end+" ";
//		}
		Query query=entityManager.createNativeQuery(sql);
		      query.setParameter(1, businessUnit.getId());
		      query.setParameter(2, businessUnit.getId());
		      query.setFirstResult((page-1)*pageSize);
			  query.setMaxResults(pageSize);
		List<Object[]> objs = query.getResultList();
		List<ProductJGVO> proList = new ArrayList<ProductJGVO>();
		for (Object[] obj : objs) {
			ProductJGVO e = new ProductJGVO();
			e.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
			e.setProName(obj[1]==null?null:obj[1].toString());
			e.setFormat(obj[2]==null?null:obj[2].toString());
			e.setStatus(obj[3]==null?null:obj[3].toString());
			e.setProductType(obj[4]==null?null:Integer.parseInt(obj[4].toString())==2?"是":"否");
			e.setCategoryName(obj[5]==null?null:obj[5].toString());
			proList.add(e);
		}
		return proList;
	}

	private String getConfig(String licenseNo, String qsNo,String businessName, String buslicenseNo) {
		String sql = "(SELECT MAX(bu.id) FROM business_unit bu ";
		sql +=" LEFT JOIN product_to_businessunit ptb  ON bu.id = ptb.business_id ";
		sql +=" LEFT JOIN production_license_info pli ON pli.id=ptb.qs_id ";
		sql +=" WHERE bu.organization IS NOT NULL ";
		boolean flag = false;
		if(licenseNo!=null&&!"".equals(licenseNo)){
			sql+=" AND bu.license_no='"+licenseNo+"'";
			flag = true;
		}
		if(qsNo!=null&&!"".equals(qsNo)&&flag){
			sql+=" OR pli.qs_no='"+qsNo+"'";
		}else if(qsNo!=null&&!"".equals(qsNo)){
			sql+=" AND pli.qs_no='"+qsNo+"'";
			flag = true;
		}
		if(businessName!=null&&!"".equals(businessName)&&flag){
			sql+=" OR bu.name='"+businessName+"'";
		}else if(businessName!=null&&!"".equals(businessName)){
			sql+=" AND bu.name='"+businessName+"'";
			flag = true;
		}
		if(buslicenseNo!=null&&!"".equals(buslicenseNo)){
			sql+="  ";
		}
		sql+=")";
		return sql;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> getByIdRegularity(long id) {
		String sql ="SELECT  DISTINCT ptr.regularity_id,pci.name  FROM product_to_regularity ptr ";
		sql+=" LEFT JOIN product_category_info pci ON ptr.regularity_id=pci.id ";
		sql+=" WHERE  ptr.product_id=?1 "; 
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1, id);
		List<Object[]> objs = query.getResultList();
		Map<String, String> map = new HashedMap();
		for (Object[] obj : objs) {
			map.put(obj[0]==null?"-1":obj[0].toString(), obj[1]==null?null:obj[1].toString());
		}
		return map;
	}
	@Override
	public Long getByProductCount(BusinessUnit businessUnit,String type) {
		//		String sql  = "SELECT COUNT(*) FROM (SELECT DISTINCT p.id ";
		//		sql+=" FROM product p " ;
		//		sql+=" INNER JOIN tz_stock ts ON    ts.product_id = p.id ";
		//		sql+=" INNER JOIN business_unit bu ON bu.id = ts.business_id ";
		//		sql+=" LEFT JOIN product_to_businessunit ptb  ON bu.id = ptb.business_id ";
		//		sql+=" INNER JOIN production_license_info pli ON pli.id=ptb.qs_id ";
		//		sql+=" WHERE 1 = 1 ";
		//		sql +=this.getConfig(licenseNo, qsNo,businessName, buslicenseNo);
		//		sql  += " UNION SELECT DISTINCT p.id ";
		//		sql+=" FROM product p " ;
		//		sql+=" INNER JOIN business_unit bu ON bu.id = p.producer_id ";
		//		sql+=" LEFT JOIN product_to_businessunit ptb  ON bu.id = ptb.business_id ";
		//		sql+=" INNER JOIN production_license_info pli ON pli.id=ptb.qs_id ";
		//		sql+=" WHERE 1 = 1 ";
		//		sql +=this.getConfig(licenseNo, qsNo,businessName, buslicenseNo);
		//		sql +=" ) e ";'
		String sql = "";
		if(type.equals("生产企业")){
			sql="select  COUNT(p.id) ";
			sql +=" FROM product p where p.producer_id=?1";
		}else{
			sql ="SELECT COUNT(DISTINCT p.id) ";
			sql +=" FROM product p ";		
			sql +=" INNER JOIN  t_meta_from_to_business tb ON p.id = tb.pro_id ";		
			sql +=" INNER JOIN t_meta_enterprise_to_provider tp ON tp.provider_id = tb.from_bus_id AND tp.business_id = "+businessUnit.getId();		
	//		sql +=this.getConfig(licenseNo, qsNo,businessName, buslicenseNo);
			sql +=" WHERE  tb.del = false AND tb.to_bus_id = ?1";
		}
//		sql +=this.getConfig(licenseNo, qsNo,businessName, buslicenseNo);
		Query query=entityManager.createNativeQuery(sql);
		      query.setParameter(1, businessUnit.getId());
		Object result=query.getSingleResult();
		Long total=result!=null?Long.parseLong(result.toString()):0;
		return total;
	}
	
	public List<TestRptJson> getThirdList(long productId,int year,String type){
		String sql="select tr.id,tr.test_date,tr.pass from test_result tr left join product_instance pi on pi.id=tr.sample_id"
				+ " where tr.test_date between ?1 and ?2 and pi.product_id=?3 and tr.test_type=?4 and tr.publish_flag='1' order by test_date asc";
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1,year+"-01-01 00:00:00");
		query.setParameter(2,year+"-12-31 23:59:00");
		query.setParameter(3,productId);
		query.setParameter(4,type);
		List<Object[]> objs=query.getResultList();
		List<TestRptJson> testResultList=new ArrayList<TestRptJson>();
		for(Object obj[]:objs){
			TestRptJson tr=new TestRptJson();
			tr.setId(Long.valueOf(obj[0].toString()));
			tr.setTestDate(obj[1]==null?"":obj[1].toString());
			tr.setResult(obj[2]==null?"1":obj[2].toString());
			testResultList.add(tr);
		}
		return testResultList;
	}
}