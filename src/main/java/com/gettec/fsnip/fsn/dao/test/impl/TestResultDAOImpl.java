package com.gettec.fsnip.fsn.dao.test.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.TestResultDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.transfer.TestResultTransfer;
import com.gettec.fsnip.fsn.util.DateUtil;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.RiskResultVO;
import com.gettec.fsnip.fsn.vo.product.EnterpriseVo;
import com.gettec.fsnip.fsn.vo.product.ProductReportVo;
import com.gettec.fsnip.fsn.vo.report.ReportVO;
import com.gettec.fsnip.fsn.vo.report.StructuredReportOfTestlabVO;

/**
 * TestResult customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value = "testResultDAO")
public class TestResultDAOImpl extends BaseDAOImpl<TestResult>
		implements TestResultDAO {
	@PersistenceContext private EntityManager entityManager;

	/**
	 * 获取报告过滤条件方法
	 * @author longxianzhen 20150815
	 */
	private String getConfigure( String configure,String new_configure) {	
		
		if(configure != null && !"null".equals(configure)){
			String filter[] = configure.split("@@");
			for(int i=0;i<filter.length;i++){
				String filters[] = filter[i].split("@");
				String config = splitSCJointConfigure(filters[0],filters[1],filters[2]);
				if(config==null){
					continue;
				}
				new_configure = new_configure + " AND " + config;
			}
		}
	    return new_configure;
	}
	
	/**
	 * 过滤条件拼接方法
	 * @author longxianzhen 20150815
	 */
	private String splitSCJointConfigure(String field,String mark,String value) {
		try {
			if(field.equals("id")){
				return FilterUtils.getConditionStr(" tr.id ",mark,value);
			}
			if(field.equals("serviceOrder")){
				return FilterUtils.getConditionStr(" tr.service_order ",mark,value);
			}
			if(field.equals("sample_product_name")){
				return FilterUtils.getConditionStr(" p.name ",mark,value,true);
			}
			if(field.equals("sample_producer_name")){
				return FilterUtils.getConditionStr(" bu.name ",mark,value,true);
			}
			if(field.equals("testType")){
				return FilterUtils.getConditionStr(" tr.test_type ",mark,value,true);
			}
			if(field.equals("sample_product_barcode")){
				return FilterUtils.getConditionStr(" p.barcode ",mark,value,true);
			}
			if(field.equals("sample_product_format")){
				return FilterUtils.getConditionStr(" p.format ",mark,value,true);
			}
			if(field.equals("testDate")){
				return FilterUtils.getConditionStr(" tr.test_date ",mark,value,true);
			}
			if(field.equals("sample_batchSerialNo")){
				return FilterUtils.getConditionStr(" ip.batch_serial_no ",mark,value,true);
			}
			if(field.equals("sample_productionDate")){
				return FilterUtils.getConditionStr(" ip.production_date ",mark,value,true);
			}
			if(field.equals("organizationName")){
				return FilterUtils.getConditionStr(" tr.organization_name ",mark,value,true);
			}
			if(field.equals("pubUserName")){
				return FilterUtils.getConditionStr(" tr.pub_user_name ",mark,value,true);
			}
			if(field.equals("publishFlag")){
				if(value.contains("未")){
					value="0";
				}else if(value.contains("已")){
					value="1";
				}
				return FilterUtils.getConditionStr(" tr.publish_flag ",mark,value);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TestResult> findTestResults(int page, int pageSize, String configure) throws DaoException{
		try {
		
			String new_configure = getConfigure(configure,"1=1");
			String sql="SELECT tr.id,tr.service_order,tr.publish_flag,ip.batch_serial_no,p.`name` AS pName,p.barcode,"
					+ "bu.name AS bName,tr.test_type,tr.pub_user_name,tr.organization_name,tr.receiveDate,tr.publishDate,"
					+ "p.risk_succeed,p.riskIndex,p.risk_failure,tr.edition,tr.organization FROM "
					+ "( SELECT * FROM test_result tr where tr.test_type ='第三方检测'  ) tr "
					+" LEFT JOIN product_instance ip ON ip.id=tr.sample_id "
					+" LEFT JOIN product p ON p.id=ip.product_id "
					+" LEFT JOIN business_unit bu ON bu.id=ip.producer_id  "
					+" where "+new_configure
					+" order by tr.receiveDate ASC ";
			Query query = entityManager.createNativeQuery(sql);
			if(page > 0 && pageSize > 0){
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> objs=query.getResultList();
			List<TestResult> trs= new ArrayList<TestResult>();
			if(objs!=null&&objs.size()>0){
				for(Object[] obj:objs){
					TestResult tr=new TestResult();
					tr.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
					tr.setServiceOrder(obj[1] != null ? obj[1].toString() : "");
					tr.setPublishFlag((Character)obj[2]);
					ProductInstance pi=new ProductInstance();
					pi.setBatchSerialNo(obj[3] != null ? obj[3].toString() : "");
					Product pro =new Product();
					pro.setName(obj[4] != null ? obj[4].toString() : "");
					pro.setPackageFlag('0');
					pro.setRisk_succeed(obj[12]==null?false:(Boolean)obj[12]);
					pro.setBarcode(obj[5] != null ? obj[5].toString() : "");
					pro.setRiskIndex(obj[13]==null?null:(Double)obj[13]);
					pro.setRiskFailure(obj[14] != null ? obj[14].toString() : "");
					pi.setProduct(pro);
					pi.getProduct().setPackageFlag('0');
					pi.setProducer(new BusinessUnit(obj[6] != null ? obj[6].toString() : ""));
					tr.setSample(pi);
					tr.setTestType(obj[7] != null ? obj[7].toString() : "");
					tr.setPubUserName(obj[8] != null ? obj[8].toString() : "");
					tr.setOrganizationName(obj[9] != null ? obj[9].toString() : "");
					tr.setReceiveDate(obj[10]==null?null:(Date)obj[10]);
					tr.setPublishDate(obj[11]==null?null:(Date)obj[11]);
					tr.setEdition(obj[15] != null ? obj[15].toString() : "");
					tr.setOrganization(obj[16] != null ? Long.parseLong(obj[16].toString()) : -1L);
					trs.add(tr);
				}
			}
			return trs;
		} catch (Exception jpae) {
			throw new DaoException("TestResultDAOImpl.findTestResults() 出现异常！", jpae);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TestResult> findTestResults(boolean publishFlag, int page, int pageSize, String configure) throws DaoException{
		try {
		
			String new_configure=" WHERE tr.publish_flag='" + (publishFlag?'1':'0') + "'";
			new_configure = getConfigure(configure,new_configure);
			
			String sql = "SELECT trInfo.id,trInfo.service_order,trInfo.publish_flag," +
					 "trInfo.batch_serial_no,trInfo.pName,trInfo.barcode,trInfo.bName,trInfo.test_type," +
					 "trInfo.pub_user_name,trInfo.organization_name,trInfo.receiveDate,trInfo.publishDate," +
					 "trInfo.risk_succeed,trInfo.riskIndex,trInfo.risk_failure,trInfo.edition,trInfo.organization FROM "+
					"( SELECT tr.id,tr.service_order,tr.publish_flag," +
					 "ip.batch_serial_no,p.`name` AS pName,p.barcode,bu.name AS bName,tr.test_type," +
					 "tr.pub_user_name,tr.organization_name,tr.receiveDate,tr.publishDate," +
					 "p.risk_succeed,p.riskIndex,p.risk_failure,tr.edition,tr.organization FROM test_result tr "+
					 "LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
					 "LEFT JOIN product p ON p.id=ip.product_id " +
					 "LEFT JOIN business_unit bu ON bu.id=ip.producer_id "+new_configure +" ) trInfo ";
			if(publishFlag){
				sql+=" order by trInfo.publishDate ASC " ;
			}else{
				sql+=" order by trInfo.receiveDate ASC " ;
			}
			Query query = entityManager.createNativeQuery(sql);
			if(page > 0 && pageSize > 0){
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			List<Object[]> objs=query.getResultList();
			List<TestResult> trs= new ArrayList<TestResult>();
			if(objs!=null&&objs.size()>0){
				for(Object[] obj:objs){
					TestResult tr=new TestResult();
					tr.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
					tr.setServiceOrder(obj[1] != null ? obj[1].toString() : "");
					tr.setPublishFlag((Character)obj[2]);
					ProductInstance pi=new ProductInstance();
					pi.setBatchSerialNo(obj[3] != null ? obj[3].toString() : "");
					Product pro =new Product();
					pro.setName(obj[4] != null ? obj[4].toString() : "");
					pro.setPackageFlag('0');
					pro.setRisk_succeed(obj[12]==null?false:(Boolean)obj[12]);
					pro.setBarcode(obj[5] != null ? obj[5].toString() : "");
					pro.setRiskIndex(obj[13]==null?null:(Double)obj[13]);
					pro.setRiskFailure(obj[14] != null ? obj[14].toString() : "");
					pi.setProduct(pro);
					pi.getProduct().setPackageFlag('0');
					pi.setProducer(new BusinessUnit(obj[6] != null ? obj[6].toString() : ""));
					tr.setSample(pi);
					tr.setTestType(obj[7] != null ? obj[7].toString() : "");
					tr.setPubUserName(obj[8] != null ? obj[8].toString() : "");
					tr.setOrganizationName(obj[9] != null ? obj[9].toString() : "");
					tr.setReceiveDate(obj[10]==null?null:(Date)obj[10]);
					tr.setPublishDate(obj[11]==null?null:(Date)obj[11]);
					tr.setEdition(obj[15] != null ? obj[15].toString() : "");
					tr.setOrganization(obj[16] != null ? Long.parseLong(obj[16].toString()) : -1L);
					trs.add(tr);
				}
			}
			return trs;
		} catch (Exception jpae) {
			throw new DaoException("TestResultDAOImpl.findTestResults() 出现异常！", jpae);
		}
	}
	
	
	/**
	 * 获取报告过滤条件方法
	 * @author longxianzhen 20150815
	 */
	private String getConfigureOfStructureds( String configure,String new_configure) {	
		
		if(configure != null && !"null".equals(configure)){
			String filter[] = configure.split("@@");
			for(int i=0;i<filter.length;i++){
				String filters[] = filter[i].split("@");
				String config = splitJointConfigureOfStructureds(filters[0],filters[1],filters[2]);
				if(config==null){
					continue;
				}
				new_configure = new_configure + " AND " + config;
			}
		}
	    return new_configure;
	}
	
	/**
	 * 过滤条件拼接方法
	 * @author longxianzhen 20150815
	 */
	private String splitJointConfigureOfStructureds(String field,String mark,String value) {
		try {
			if(field.equals("serviceOrder")){
				return FilterUtils.getConditionStr(" e.service_order ",mark,value);
			}
			if(field.equals("sampleName")){
				return FilterUtils.getConditionStr(" pro.name ",mark,value,true);
			}
			if(field.equals("barcode")){
				return FilterUtils.getConditionStr(" pro.barcode ",mark,value,true);
			}
			if(field.equals("batchSerialNo")){
				return FilterUtils.getConditionStr(" pri.batch_serial_no ",mark,value,true);
			}
			if(field.equals("producerName")){
				return FilterUtils.getConditionStr(" bus.name ",mark,value,true);
			}
			if(field.equals("testType")){
				return FilterUtils.getConditionStr(" e.test_type ",mark,value,true);
			}
			if(field.equals("operator")){
				return FilterUtils.getConditionStr(" trh.handler ",mark,value,true);
			}
			if(field.equals("status")){
				if(value.contains("未")){
					return	" e.publish_flag = '6' AND trh.`status` = 1 AND (e.back_result is null OR e.back_result='') ";
				}else if(value.contains("已")){
					return	" trh.`status` = 2 ";
				}else if(value.contains("重")){
					return" e.publish_flag = '6' AND trh.`status` = 1 AND e.back_result is not null AND e.back_result != '' ";
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取需审核的结构化报告集合
	 * @author ZhangHui 2015/5/6
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StructuredReportOfTestlabVO> findTestResultsOfStructureds(int page, int pageSize,String configure) throws DaoException{
		try {
			 
			 String new_configure=" WHERE e.del = 0 ";
			 if(configure==null||!configure.contains("status")){
				 new_configure+=" AND ((e.publish_flag = '6' AND trh.`status` = 1) OR trh.`status` = 2) ";
			 }
			 
			 new_configure = getConfigureOfStructureds(configure,new_configure);
			
			 String sql = "SELECT re.id,re.service_order,re.proName,re.batch_serial_no,re.buName," +
			 						"re.test_type,re.`handler`,re.receiveDate,re.publish_flag,re.`status`,re.organization,re.barcode,re.back_count,re.suppliers_back_count " +
			 			  "FROM " +
			 			  "	( SELECT e.id,e.service_order,pro.`name` AS proName,pri.batch_serial_no,bus.`name` AS buName," +
			 						"e.test_type,trh.`handler`,e.receiveDate,e.publish_flag,trh.`status`,e.organization,pro.barcode,e.back_count,e.suppliers_back_count " +
			 			  "FROM test_result e " +
					 	  "INNER JOIN test_result_handler trh ON trh.test_result_id = e.id " +
					 	  "LEFT JOIN product_instance pri ON pri.id = e.sample_id " +
					 	  "LEFT JOIN product pro ON pro.id = pri.product_id " +
					 	  "LEFT JOIN business_unit bus ON bus.id = pri.producer_id " +
					 	  new_configure+" ) re order by re.receiveDate asc ";
			 Query query = entityManager.createNativeQuery(sql);
			 if(page > 0 && pageSize > 0){
					query.setFirstResult((page-1)*pageSize);
					query.setMaxResults(pageSize);
			 }
			 /* 数据封装 */
			 List<Object[]> result = query.getResultList();
			 List<StructuredReportOfTestlabVO> vos = new ArrayList<StructuredReportOfTestlabVO>();
			 if(result != null){
					for(Object[] obj : result){
						StructuredReportOfTestlabVO vo = new StructuredReportOfTestlabVO(((BigInteger)obj[0]).longValue(), 
										obj[1]==null?"":obj[1].toString(), obj[2]==null?"":obj[2].toString(),
										obj[3]==null?"":obj[3].toString(), obj[4]==null?"":obj[4].toString(), 
										obj[5]==null?"":obj[5].toString(), obj[6]==null?"":obj[6].toString(),
										obj[7]==null?null:(Date)obj[7],obj[8]==null?"":obj[8].toString(),
										Integer.parseInt(String.valueOf(obj[9])),((BigInteger)obj[10]).longValue());
						vo.setBarcode(obj[11]==null?"":obj[11].toString());
						vo.setBackCount(obj[12]==null?0:Integer.parseInt(obj[12].toString()));
						vo.setSuppliersBackCount(obj[13]==null?0:Integer.parseInt(obj[13].toString()));
						vos.add(vo);
					}
			 }
			 
	         return vos;
		} catch (Exception e) {
			throw new DaoException("TestResultDAOImpl.findTestResultsOfStructureds() 出现异常！", e);
		}
	}

	/**
	  * 根据条件查询某个产品最后发布报告的时间
	  * @param proId 产品ID
	  * @param organizationId 企业组织机构ID
	  * @param startDate 报告发布起始时间
	  * @param endDate 报告发布结束时间
	  * @return Date
	  * @throws DaoException
	  * @author LongXianZhen
	  */
	@SuppressWarnings("unchecked")
	@Override
	public Date getLastPubDateByProIdBu(Long proId, Long organizationId, String startDate, String endDate)
			throws DaoException {
		try {
			boolean dateFlag=!startDate.equals("")&&!endDate.equals("");
			String dateSql=" ";
			//判断查询条件是否填写报告发布时间范围
			if(dateFlag){
				endDate=DateUtil.addDays(endDate, 1);
				dateSql=" (e.publishDate BETWEEN '"+startDate+"' AND '"+endDate+"') AND ";
			}
			String jpql= "SELECT e.publishDate FROM " + entityClass.getName()+" e WHERE "+dateSql+
					"  e.publishFlag='1' AND e.sample.product.id=?1 AND e.organization=?2 AND publishDate is not null order by publishDate desc";
			Query query = entityManager.createQuery(jpql).setParameter(1, proId).setParameter(2, organizationId);
			List<Date> lastDates=query.getResultList();
			if(lastDates!=null&&lastDates.size()>0){
				return lastDates.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new DaoException("dao层查询按条件查询企业统计个数出错", e);
		}
	}

	/**
	 * 根据企业组织机构ID查询该企业已发布报告的产品数量
	 * @param organizationId 企业组织机构ID
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getPublishProCountByOrganizationId(Long organizationId) throws DaoException {
		try {				
			String jpql = "SELECT distinct e.sample.product.id FROM " + entityClass.getName()+" e WHERE e.publishFlag='1' AND e.organization=?1 ";				
			Query query = entityManager.createQuery(jpql).setParameter(1, organizationId);
			List<Long> proIds=query.getResultList();
			if(proIds!=null){
				return (long) proIds.size();
			}else{
				return 0L;
			}
		} catch (Exception e) {
			throw new DaoException("TestResultDAOImpl.getPublishProCountByOrganizationId()-->根据企业组织机构ID查询该企业已发布报告的产品数量出错", e);
		}
	}

	/**
	 * 根据企业组织机构ID查询该企业已发布报告数量
	 * @param organizationId 企业组织机构ID
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getRepoCountByOrganizationId(Long organizationId) throws DaoException {
		try {				
			String jpql = "SELECT distinct id FROM " + entityClass.getName()+" e WHERE e.publishFlag='1' AND e.organization=?1 ";				
			Query query = entityManager.createQuery(jpql).setParameter(1, organizationId);
			List<Long> reprIds=query.getResultList();
			if(reprIds!=null){
				return (long) reprIds.size();
			}else{
				return 0L;
			}
		} catch (Exception e) {
			throw new DaoException("TestResultDAOImpl.getRepoCountByOrganizationId()-->根据企业组织机构ID查询该企业已发布报告数量出错", e);
		}
	}

	@Override
	public long getTestResultCount(boolean publishFlag, String configure) throws DaoException{
		try {
			String new_configure=" WHERE tr.publish_flag='" + (publishFlag?'1':'0') + "'";
			new_configure = getConfigure(configure,new_configure);
			
			String sql = "SELECT count(*) FROM test_result tr "+
					 "LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
					 "LEFT JOIN product p ON p.id=ip.product_id " +
					 "LEFT JOIN business_unit bu ON bu.id=ip.producer_id "+new_configure ;
			Query query=entityManager.createNativeQuery(sql);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception jpae) {
			throw new DaoException("TestResultDAOImpl.getTestResultCount() 出现异常！", jpae);
		}
	 }
	
	public long getTestResultThirdCount(String configure) throws DaoException{
			try {
				String new_configure = getConfigure(configure,"");
				
				String sql = "SELECT count(*) FROM test_result tr "+
						 "LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
						 "LEFT JOIN product p ON p.id=ip.product_id " +
						 "LEFT JOIN business_unit bu ON bu.id=ip.producer_id where tr.test_type ='第三方检测' "+new_configure ;
				Query query=entityManager.createNativeQuery(sql);
				return Long.parseLong(query.getSingleResult().toString());
			} catch (Exception jpae) {
				throw new DaoException("TestResultDAOImpl.getTestResultCount() 出现异常！", jpae);
			}
		 }
	
	/**
	 * 获取需审核的结构化报告数量
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public long getCountOfStructureds(String configure) {
		try {
			 String new_configure=" WHERE e.del = 0 ";
			 if(configure==null||!configure.contains("status")){
				 new_configure+=" AND ((e.publish_flag = '6' AND trh.`status` = 1) OR trh.`status` = 2) ";
			 }
			 
			 new_configure = getConfigureOfStructureds(configure,new_configure);
			String sql = "SELECT count(*) FROM test_result e " +
					 	  "INNER JOIN test_result_handler trh ON trh.test_result_id = e.id " +
					 	  "LEFT JOIN product_instance pri ON pri.id = e.sample_id " +
					 	  "LEFT JOIN product pro ON pro.id = pri.product_id " +
					 	  "LEFT JOIN business_unit bus ON bus.id = pri.producer_id " +new_configure;
						 
			Query query = entityManager.createNativeQuery(sql);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	 }

	
	/**
	 * 根据条件查询某个产品已发布或未发布报告的数量
	 * @param organizationId 企业组织机构ID
	 * @param proId 产品ID
	 * @param startDate 报告发布起始时间
	 * @param endDate 报告发布结束时间
	 * @param publishFlag 报告发布标志为'1'查询已发布的报告数量，为'0'查询未发布的报告数量
	 * @return Long
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getReportQuantityByProBu(Long buOrgId, Long proId,String startDate,String endDate,String publishFlag)
			throws DaoException {
		try {				
			boolean dateFlag=!startDate.equals("")&&!endDate.equals("");
			String dateSql=" ";
			//判断查询条件是否填写报告发布时间范围，且时间范围只对已发布的报告进行处理
			if(dateFlag&&publishFlag.equals("1")){
				endDate=DateUtil.addDays(endDate, 1); //报告发布结束时间加一天
				dateSql=" (e.publishDate BETWEEN '"+startDate+"' AND '"+endDate+"') AND ";
			}
			String jpql = "SELECT distinct id FROM " + entityClass.getName()+" e WHERE "+dateSql+ " e.publishFlag='"+publishFlag+"' AND e.organization=?1 AND e.sample.product.id=?2 ";				
			Query query = entityManager.createQuery(jpql).setParameter(1, buOrgId).setParameter(2,proId);
			List<Long> reprIds=query.getResultList();
			if(reprIds!=null){
				return (long) reprIds.size();
			}else{
				return 0L;
			}
		} catch (Exception e) {
			throw new DaoException("TestResultDAOImpl.getReportQuantityByProBu()-->根据条件查询某个产品已发布或未发布报告的数量出错", e);
		}
	}
	
	/**
	 * dao层根据过滤条件查询产品报告集合
	 */
	@Override
	public List<TestResult> getProReportListByPage(int page, int pageSize, String configure) throws DaoException {
		try {
			 String condition = "";
			 if(configure == null || "".endsWith(configure)){
            	condition = " WHERE ";
			 }else{
            	condition = configure + " AND ";
			 }
			 condition += "publishFlag='0' or publishFlag='1'";
			 List<TestResult> testResultList=this.getListByPage(page, pageSize, condition, new Object[]{});
			 TestResultTransfer.transfer(testResultList);
			 return testResultList;
		} catch (Exception e) {
			throw new DaoException("dao层根据过滤条件查询产品报告集合出错", e);
		}
	}
	
	/**
	 * dao层根据过滤条件查询产品报告数量
	 */
	@Override
	public Long getCountProReport(String configure) throws DaoException {
		try {
            String condition = "";
            if(configure == null || "".endsWith(configure)){
            	condition = " WHERE ";
            }else{
            	condition = configure + " AND ";
            }
            condition += "publishFlag='0' or publishFlag='1'";
            return this.count(condition, new Object[]{});
		} catch (Exception e) {
			throw new DaoException("dao层根据过滤条件查询产品报告数量出错", e);
		}
	}
	//将obj 数组专为List<TestResult>
	private  List<TestResult> deliddingTestResultByObjArray(List<Object[]> objects) throws ParseException{
	    List<TestResult> list =null;
        if(objects!=null && objects.size()>0){
            TestResult report = null;
            list =new ArrayList<TestResult>();
            for(Object[] objs:objects){
                report=new TestResult();
                BusinessBrand brand = new BusinessBrand();
                brand.setName(objs[6]!=null?objs[6].toString():null);
                
                Product product=new Product();
                product.setBarcode(objs[1]!=null?objs[1].toString():null);
                product.setName(objs[2]!=null?objs[2].toString():null);
                product.setFormat(objs[4]!=null?objs[4].toString():null);
                product.setBusinessBrand(brand);
                
                BusinessUnit businessUnit = new BusinessUnit();
                businessUnit.setName(objs[5]!=null?objs[5].toString():null);
                
                ProductInstance instance = new ProductInstance();
                instance.setProducer(businessUnit);
                instance.setProduct(product);
                instance.setProductionDate(objs[8]!=null? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(objs[8].toString()):null);
                instance.setBatchSerialNo(objs[7]!=null?objs[7].toString():null);
                
                report.setServiceOrder(objs[0]!=null?objs[0].toString():null);
                report.setSample(instance);
                report.setTestType(objs[3]!=null?objs[3].toString():null);
                report.setTestDate(objs[9]!=null?new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(objs[9].toString()):null);
                report.setPublishFlag(objs[10]!=null?(objs[10].toString()).charAt(0):null);
                list.add(report);
            }
        }
        return list;
	}

   // 导出EXECL
    @SuppressWarnings("unchecked")
	@Override
    public List<TestResult> getProEXECLReportList(Map<String, Object> configSQLString) throws DaoException {
        try {
            String condition = (String) configSQLString.get("condition");
            Object[] params = (Object[]) configSQLString.get("params");  
                String sql = "SELECT e.service_order,e.barcode,e.proname,e.test_type,e.format,e.busname,e.brandname, " + 
                		    "e.batch_serial_no,e.production_date,e.test_date,e.publish_flag FROM  " + 
                		    "(SELECT rep.service_order ,pt.barcode,pt.`name` AS proname,rep.test_type, pt.format, " + 
                		    "bu.`name` AS busname,bb.`name` AS brandname, p.batch_serial_no, p.production_date, " + 
                		    "rep.test_date,rep.publish_flag  FROM test_result rep  " + 
                		    "LEFT JOIN product_instance p  ON p.id = rep.sample_id  " + 
                		    "LEFT JOIN product pt ON pt.id = p.product_id  " + 
                		    "LEFT JOIN business_brand bb ON bb.id = pt.business_brand_id  " + 
                		    "LEFT JOIN business_unit  bu ON bu.id = p.producer_id " +
                		    "WHERE rep.del = 0) e ";
                
                if (condition != null) {
                    sql = sql + " " + condition;
                }
                Query query = entityManager.createNativeQuery(sql);
                // 增加参数
                if (params != null && params.length != 0) {
                    Object[] val = params;
                    for (int i = 0; i < val.length; i++) {
                        if(val[i] == null){
                            break;
                        }
                        query.setParameter(i + 1, val[i]);
                    }
                }
                List<Object[]> objects=query.getResultList();
                return deliddingTestResultByObjArray(objects);
        } catch (Exception e) {
            throw new DaoException("dao层根据过滤条件查询产品报告集合出错", e);
        }
    }
    /**
     * 按产品实例id查询检测报告
     * @param id 实例id
     * @param isInspect 是否是企业送检
	 * @author ZhaWanNeng
     */
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<RiskResultVO> findTRByProductInstance(Long id,Boolean isInspect)throws DaoException {
		try {
			String sql="";
			if(isInspect){
				sql = "select e.id,e.test_date,e.standard from test_result e where e.del = 0 and e.sample_id=?1 and e.test_type='企业送检' and e.publish_flag='1' order by e.test_date desc ";
			}else {
				sql = "select e.id,e.test_date,e.standard from test_result e where e.del = 0 and e.sample_id=?1 and e.publish_flag='1' and ( e.test_type='企业自检' OR e.test_type='政府抽检' ) order by e.test_date desc ";
			}
             Query query = entityManager.createNativeQuery(sql);
             query.setParameter(1, id);
             List<RiskResultVO> riskResultVOs=new ArrayList<RiskResultVO>();
             List<Object[]> list = query.getResultList();
             for (Object[] objects : list) {
            	 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	 RiskResultVO riskresult = new RiskResultVO();
            	 riskresult.setId(objects[0]!=null? Long.valueOf(objects[0].toString()):null);
            	 Date date =null;
            	 if(objects[1]!=null){
            		 date = format.parse(objects[1].toString());
            	 }else {
            		 date = format.parse("1970-01-01 00:00:00");
				 }
            	 riskresult.setTestDate(date);
            	 riskresult.setStandard(objects[2] != null?objects[2].toString():"");
            	 riskResultVOs.add(riskresult);
 		    }
		    return riskResultVOs;
		} catch (Exception e) {
	        throw new DaoException("dao层按条件查询检测报告出错", e);
	    }
	}

    /**
     * 根据产品和报告类型查找最新一条报告 
     * @author HuangYog
     */
    @SuppressWarnings("unchecked")
    @Override
    public ReportVO getNewestReportForPIdAndReportType(Long organization,
            Long proId, String reportType,Integer status) throws DaoException {
		String condition = "";
		if(status == 0){
			/* 在未处理中的报告不包括testlab退回的 */
			condition = " AND e.publish_flag != '2' ";
		}
        String sql = "SELECT e.id,e.test_date,e.test_type, e.publish_flag FROM test_result e "+
				"RIGHT JOIN product_instance simple ON e.sample_id = simple.id "+
                "WHERE simple.product_id = ?1 AND e.test_type = ?2 "+condition+" AND e.publish_flag != ?3 AND e.del = 0 "+
        		"ORDER BY e.test_date DESC  LIMIT 0,1";
        try {
            List<Object[]> result = entityManager.createNativeQuery(sql)
                    .setParameter(1, proId)
                    .setParameter(2, reportType)
                    .setParameter(3, "1")
                    .getResultList();
            Object[] obj = result != null && result.size() > 0 ? result.get(0): null;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(obj!=null && obj.length>0){
                Long id = (obj[0].toString() != null&&!"".equals(obj[0].toString())) ? Long.parseLong(obj[0].toString()):-1;
                String date = null;
                if(obj[1]!=null){
                    Date d = sdf.parse(obj[1].toString());
                    date = sdf.format(d);
                }
                String type = obj[2].toString() !=null ? obj[2].toString() : "";
                String flag = obj[3].toString() !=null ? obj[3].toString() : "";
                return new ReportVO(id,date,type,flag);
            }
            return null;
        } catch (Exception e) {
            throw new DaoException("TestResultDAOImpl.getNewestReportForPIdAndReportType()-->"+"根据条件查询产品报告出错", e);
        }
    }
    /**
	 * 报告总数
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public Long testResultCount() throws DaoException {
		try {
			    String sql = "SELECT count(t.id) FROM test_result t where t.del = 0";
	            Query query = entityManager.createNativeQuery(sql);
	            List<Object> list = query.getResultList();
			    return Long.valueOf(list.get(0).toString());
		} catch (Exception e) {
			throw new DaoException("TestResultDAOImpl.testResultCount() ", e);
		}
	}
	/**
	 * 根据组织机构id获取产品总数
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public Integer productCountByOrg(Integer organization) throws DaoException {
		try {
			    String sql = "SELECT  COUNT(pro.id)  FROM product pro WHERE  pro.organization = ?1 ";
	            Query query = entityManager.createNativeQuery(sql);
	            query.setParameter(1, organization);
	            List<Object> list = query.getResultList();
			    return Integer.valueOf(list.get(0).toString());
		} catch (Exception e) {
			throw new DaoException("TestResultDAOImpl.testResultCount() ", e);
		}
	}
	/**
	 * 根据组织机构id获取产品的风险排行的数量
	 * @param code 一级分类的code
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@SuppressWarnings("unchecked")
	public List<ProductReportVo> getProductReportVo(Integer organization,int proSize) throws DaoException {
		try {
			String sql = "SELECT  t.cont,product.id,product.name,product.imgUrl FROM product LEFT JOIN " +
					"(SELECT pro.id 'productid',COUNT(tres.id) 'cont' FROM product pro " +
					" RIGHT JOIN product_instance pi ON pi.product_id = pro.id" +
					" RIGHT JOIN test_result tres ON tres.sample_id = pi.id AND tres.del = 0" +
					" WHERE tres.publish_flag = 1  and pro.organization = "+organization+" " +
					" GROUP BY pro.id ) t ON product.id = t.productid" +
					" WHERE product.organization = "+organization+" ORDER BY t.cont desc,product.id DESC";
			Query query = entityManager.createNativeQuery(sql);
			query.setMaxResults(proSize);
			
			List<Object[]> result = query.getResultList();
			List<ProductReportVo> productList = new ArrayList<ProductReportVo>();
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				int countProReport =Integer.valueOf( obj[0] !=null ? obj[0].toString() : "0");
				Long id = Long.valueOf(obj[1].toString());
				String name = obj[2] !=null ? obj[2].toString() : "";
				String imgUrl = obj[3] !=null ? obj[3].toString() : "";
				ProductReportVo productReportVo = new ProductReportVo(id, name, imgUrl, countProReport);
				productList.add(productReportVo);
			}
			return productList;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.riskBillboard() ", e);
		}
	}
	
	/**
     * 企业专栏
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
     */
	@SuppressWarnings("unchecked")
	public List<EnterpriseVo> getEnterprise(int proSize,String busIds) throws DaoException {
		try {
			String[] busIdsArray=busIds.split(",");
			List<Long> buIdsList=new ArrayList<Long>();
			for(String bid:busIdsArray){
				buIdsList.add(Long.parseLong(bid));
			}
			String condition = "";
	        if(busIds!=null){
				condition = "  bus.id in (" + (busIds.equals("") ? "'"+"'" :busIds )+") ";
			}
			
			String sql="SELECT COUNT(res.id),bus.name,bus.id,bus.organization FROM business_unit bus " + 
			 		"RIGHT JOIN product pro ON pro.organization = bus.organization " + 
			 		"LEFT JOIN product_instance pin ON  pin.product_id = pro.id " + 
			 		"LEFT JOIN test_result res ON res.sample_id = pin.id AND res.del = 0 AND res.publish_flag = '1' and res.test_type != '' and res.test_type is not null " + 
			 		"WHERE " +condition+ "  GROUP BY bus.id ";
			Query query = entityManager.createNativeQuery(sql);
			
			List<Object[]> result = query.getResultList();
			List<EnterpriseVo> enterprises = new ArrayList<EnterpriseVo>();
			for(int i=0;i<result.size();i++ ){
				enterprises.add(null);
			}
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				EnterpriseVo enterprise = new EnterpriseVo();
				int countOfReport =Integer.valueOf( obj[0] !=null ? obj[0].toString() : "0");
				String name = obj[1] !=null ? obj[1].toString() : "";
				Long id = Long.valueOf(obj[2].toString());
				Integer organization = Integer.valueOf(obj[3] !=null ? obj[3].toString() : "0");
				List<ProductReportVo> productList = getProductReportVo(organization, proSize);
				int size =productCountByOrg(organization);
				enterprise.setId(id);
				enterprise.setName(name);
				enterprise.setCountOfReport(countOfReport);
				enterprise.setCountOfProduct(size);
				enterprise.setProductList(productList);
				int index=buIdsList.indexOf(id);
				enterprises.set(index, enterprise);
			}
			return enterprises;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.riskBillboard() ", e);
		}
	}
	
	/**
     * 企业专栏生产企业总数
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/13
     */
	@SuppressWarnings("unchecked")
	public int countProductEnterprise() throws DaoException {
		try {
			String sql="SELECT bus.id FROM business_unit bus " + 
			 		"RIGHT JOIN product pro ON pro.organization = bus.organization " + 
			 		"LEFT JOIN product_instance pin ON  pin.product_id = pro.id " + 
			 		"LEFT JOIN test_result res ON res.sample_id = pin.id AND res.del = 0 AND res.publish_flag = '1' " + 
			 		"WHERE  (bus.type='生产企业' or bus.type='仁怀市白酒生产企业' ) GROUP BY bus.id   ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object> result = query.getResultList();
			if(result.size() <= 0){
				return 0;
			}
			return result.size();
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.countProductEnterprise() ", e);
		}
	}
	
	/**
     * 茶叶专栏
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/13
     */
	@SuppressWarnings("unchecked")
	public List<EnterpriseVo> getBusinessTeaUnit(int pageSize,int page,int proSize,String keyword) throws DaoException {
		try {
			String condition =""; 
			if(keyword!=null&&!"".equals(keyword)){
				condition=condition+" AND bus.name like '%"+keyword+"%' ";
			}
			String sql = "SELECT DISTINCT COUNT(tres.id),tr.URL,bu.name,bu.id,bu.organization,bus.about,bus.website FROM business_teaunit  bu " +
					     " LEFT JOIN business_unit  bus ON bus.id = bu.businessUnitid "+
						 " LEFT JOIN enterprise_registe  er ON er.enterpriteName = bus.name " +
					     " LEFT JOIN t_business_logo_to_resource  tb ON tb.ENTERPRISE_REGISTE_ID = er.id " +
					     " LEFT JOIN t_test_resource  tr ON tb.RESOURCE_ID = tr.RESOURCE_ID " +
					     " LEFT JOIN test_result tres ON tres.organization = bu.organization AND tres.del = 0 " +
					     " WHERE tres.publish_flag = 1 "+condition+" GROUP BY bu.organization ORDER BY COUNT(tres.id) DESC ";
			Query query = entityManager.createNativeQuery(sql);
			query.setFirstResult((page - 1)*pageSize);
			query.setMaxResults(pageSize);
			
			List<Object[]> result = query.getResultList();
			List<EnterpriseVo> enterprises = new ArrayList<EnterpriseVo>();
			if(result.size() <= 0){
				return null;
			}
			for (Object[] obj : result) {
				int countOfReport =Integer.valueOf( obj[0] !=null ? obj[0].toString() : "0");
				String logo = obj[1] !=null ? obj[1].toString() : "";
				String name = obj[2] !=null ? obj[2].toString() : "";
				Long id = Long.valueOf(obj[3].toString());
				Integer organization = Integer.valueOf(obj[4] !=null ? obj[4].toString() : "0");
				String about = obj[5] !=null ? obj[5].toString() : "";
				String website = obj[6] !=null ? obj[6].toString() : "";
				List<ProductReportVo> productList = getProductReportVo(organization, proSize);
				int countOfProduct =productCountByOrg(organization);
				EnterpriseVo enterprise = new EnterpriseVo(id, name, logo, about, website ,countOfReport, countOfProduct, productList);
				enterprises.add(enterprise);
			}
			return enterprises;
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.getBusinessTeaUnit() ", e);
		}
	}
	/**
     * 茶叶专栏企业总数
     * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/13
     */
	@SuppressWarnings("unchecked")
	public int countBusinessTeaUnit(String keyword) throws DaoException {
		try {
			String condition =""; 
			if(keyword!=null&&!"".equals(keyword)){
				condition=condition+" AND bu.name like '%"+keyword+"%' ";
			}
			String sql = "SELECT DISTINCT bu.id FROM business_teaunit  bu " +
					     " LEFT JOIN enterprise_registe  er ON er.enterpriteName = bu.name " +
					     " LEFT JOIN t_business_logo_to_resource  tb ON tb.ENTERPRISE_REGISTE_ID = er.id " +
					     " LEFT JOIN t_test_resource  tr ON tb.RESOURCE_ID = tr.RESOURCE_ID " +
					     " LEFT JOIN test_result tres ON tres.organization = bu.organization AND tres.del = 0 " +
					     " WHERE tres.publish_flag = 1 "+condition+" GROUP BY bu.organization ORDER BY COUNT(tres.id) DESC ";
			Query query = entityManager.createNativeQuery(sql);
			List<Object> result = query.getResultList();
			if(result.size() <= 0){
				return 0;
			}
			return result.size();
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.countProductEnterprise() ", e);
		}
	}

	/**
     * 审核通过报告
	 * @author ZhangHui 2015/5/6
     */
	@Override
	public void publishTestResult(Long result_id) throws DaoException {
		try {
			String sql = "UPDATE test_result SET publish_flag = ?1,publishDate = now() WHERE id = ?2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, '1');
			query.setParameter(2, result_id);
			query.executeUpdate();
		} catch (Exception e) {
			throw new DaoException("ProductDAOImpl.publishTestResult() ", e);
		}
	}
	
	/**
     * 审核通过结构化报告
	 * @author ZhangHui 2015/5/6
     */
	@Override
	public void publishTestResultOfStructured(Long result_id) throws DaoException {
		try {
			String sql = "UPDATE test_result_handler SET `status` = ?1 WHERE test_result_id = ?2 AND `status` = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, 8);
			query.setParameter(2, result_id);
			query.setParameter(3, 2);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.publishTestResultOfStructured() 出现异常", e);
		}
	}

	/**
	 * 功能描述：teslab将已结构化报告退回至结构化人员
	 * @author ZhangHui 2015/6/29
     */
	@Override
	public void sendBackToJGH(Long test_result_id, String returnMes) throws DaoException {
		try {
			String sql = "UPDATE test_result SET publish_flag = '2',back_result = ?1,back_time = now() WHERE id = ?2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, returnMes);
			query.setParameter(2, test_result_id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.sendBackTestResult() ", e);
		}
	}

	/**
	 * 功能描述：teslab将已结构化报告退回至结构化人员
	 * @author ZhangHui 2015/6/29
     */
	@Override
	public void sendBackOfStructuredToJGH(Long test_result_id) throws DaoException {
		try {
			String sql = "UPDATE test_result_handler SET `status` = 4 WHERE test_result_id = ?1 AND `status` = 2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, test_result_id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.sendBackTestResultOfStructured() 出现异常", e);
		}
	}
	
	/**
	 * 功能描述：teslab将已结构化报告退回至供应商
	 * @author ZhangHui 2015/6/29
     */
	@Override
	public void sendBackToGYS(Long test_result_id, String returnMes) throws DaoException {
		try {
			String sql = "UPDATE test_result SET publish_flag = '7',back_result = ?1,back_time = now() WHERE id = ?2";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, returnMes);
			query.setParameter(2, test_result_id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.sendBackTestResult() ", e);
		}
	}

	/**
	 * 功能描述：teslab将已结构化报告退回至供应商
	 * @author ZhangHui 2015/6/29
     */
	@Override
	public void sendBackOfStructuredToGYS(Long test_result_id,String sql) throws DaoException {
		try {
//			String sql = "UPDATE test_result_handler SET `status` = 12 WHERE test_result_id = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, test_result_id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.sendBackTestResultOfStructured() 出现异常", e);
		}
	}

	/**
	 * 从portal撤回不是来自经销商的报告
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public int goBackTestResult(Long test_result_id) throws DaoException {
		try {
			String sql = "UPDATE test_result SET publish_flag = ?1 WHERE id = ?2 " +
						 "AND NOT EXISTS (SELECT id FROM test_result_handler WHERE test_result_id = ?3)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, '0');
			query.setParameter(2, test_result_id);
			query.setParameter(3, test_result_id);
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 从portal撤回结构化报告
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public void goBackTestResultOfStructured(Long test_result_id) throws DaoException {
		try {
			String sql = "UPDATE test_result_handler SET `status` = ?1 WHERE test_result_id = ?2 AND `status` = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, 2);
			query.setParameter(2, test_result_id);
			query.setParameter(3, 8);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ProductDAOImpl.goBackTestResultOfStructured() 出现异常", e);
		}
	}

	/**
	 * 从portal撤回来自经销商的报告_0
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public int goBackTestResultOfDealer_0(Long test_result_id){
		try {
			String sql = "UPDATE test_result SET publish_flag = ?1 WHERE id = ?2 " +
						 "AND EXISTS (SELECT id FROM test_result_handler WHERE test_result_id = ?3 AND `status` = ?4)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, '6');
			query.setParameter(2, test_result_id);
			query.setParameter(3, test_result_id);
			query.setParameter(4, 1);
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 从portal撤回来自经销商的报告_0
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public int goBackTestResultOfDealer_1(Long test_result_id){
		try {
			String sql = "UPDATE test_result SET publish_flag = ?1 WHERE id = ?2 " +
						 "AND EXISTS (SELECT id FROM test_result_handler WHERE test_result_id = ?3 AND `status` IN (2,8,4))";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, '2');
			query.setParameter(2, test_result_id);
			query.setParameter(3, test_result_id);
			return query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 根据报告id获取检测报告 （没有级联）
	 * @param id
	 * @author lxz 2015/5/6
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TestResult findByTestResultId(Long id) throws DaoException {
		try {
			String sql = "SELECT trInfo.id,trInfo.service_order,trInfo.publish_flag," +
					 "trInfo.batch_serial_no,trInfo.pId,trInfo.bName,trInfo.test_type," +
					 " trInfo.sampling_location,trInfo.sampling_date,trInfo.standard,"+
					 "trInfo.approve_by,trInfo.audit_by,trInfo.key_tester,trInfo.test_date," +
					 "trInfo.result,trInfo.comment,trInfo.fullPdfPath,trInfo.interceptionPdfPath," +
					 "trInfo.back_result,trInfo.tName,trInfo.edition,trInfo.organization FROM "+
					"( SELECT tr.id,tr.service_order,tr.publish_flag," +
					 "ip.batch_serial_no,p.id AS pId,bu.name AS bName,tr.test_type," +
					 " tr.sampling_location,tr.sampling_date,tr.standard,tr.approve_by, "+
					 " tr.audit_by,tr.key_tester,tr.test_date,tr.result,tr.comment," +
					 "tr.fullPdfPath,tr.interceptionPdfPath,tr.back_result,buu.name AS tName," +
					 "tr.edition,tr.organization FROM test_result tr "+
					 "LEFT JOIN product_instance ip ON ip.id=tr.sample_id " +
					 "LEFT JOIN product p ON p.id=ip.product_id " +
					 "LEFT JOIN business_unit bu ON bu.id=ip.producer_id " +
					 "LEFT JOIN business_unit buu ON buu.id=tr.testee_id WHERE tr.id=?1 ) trInfo ";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, id);
			List<Object[]> objs=query.getResultList();
			TestResult tr=new TestResult();
			if(objs!=null&&objs.size()>0){
				Object[] obj=objs.get(0);
				tr.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : -1L);
				tr.setServiceOrder(obj[1] != null ? obj[1].toString() : "");
				tr.setPublishFlag((Character)obj[2]);
				ProductInstance pi=new ProductInstance();
				pi.setBatchSerialNo(obj[3] != null ? obj[3].toString() : "");
				Product pro =new Product();
				pro.setId(obj[4] != null ? Long.parseLong(obj[4].toString()) : -1L);
				pi.setProduct(pro);
				pi.setProducer(new BusinessUnit(obj[5] != null ? obj[5].toString() : ""));
				tr.setSample(pi);
				tr.setTestType(obj[6] != null ? obj[6].toString() : "");
				tr.setSamplingLocation(obj[7] != null ? obj[7].toString() : "");
				tr.setSamplingDate(obj[8]==null?null:(Date)obj[8]);
				tr.setStandard(obj[9] != null ? obj[9].toString() : "");
				tr.setApproveBy(obj[10] != null ? obj[10].toString() : "");
				tr.setAuditBy(obj[11] != null ? obj[11].toString() : "");
				tr.setKeyTester(obj[12] != null ? obj[12].toString() : "");
				tr.setTestDate(obj[13]==null?null:(Date)obj[13]);
				tr.setResult(obj[14] != null ? obj[14].toString() : "");
				tr.setComment(obj[15] != null ? obj[15].toString() : "");
				tr.setFullPdfPath(obj[16] != null ? obj[16].toString() : "");
				tr.setInterceptionPdfPath(obj[17] != null ? obj[17].toString() : "");
				tr.setBackResult(obj[18] != null ? obj[18].toString() : "");
				tr.setTestee(new BusinessUnit(obj[19] != null ? obj[19].toString() : ""));
				tr.setEdition(obj[20] != null ? obj[20].toString() : "");
				tr.setOrganization(obj[21] != null ? Long.parseLong(obj[21].toString()) : -1L);
			}
			return tr;
		} catch (Exception jpae) {
			throw new DaoException("TestResultDAOImpl.findByTestResultId() 出现异常！", jpae);
		}
	}
}