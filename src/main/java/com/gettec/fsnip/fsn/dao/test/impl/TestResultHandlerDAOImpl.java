package com.gettec.fsnip.fsn.dao.test.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.TestResultHandlerDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.test.TestResultHandler;
import com.gettec.fsnip.fsn.vo.report.ToBeStructuredReportVO;

/**
 * testResultHandlerDAO customized operation implementation
 * 
 * @author LongXianZhen
 * 2015-04-28
 */
@Repository(value="testResultHandlerDAO")
public class TestResultHandlerDAOImpl extends BaseDAOImpl<TestResultHandler>
		implements TestResultHandlerDAO {
	
	/**
	 * 根据报告id，查找结构化报告数量
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public long countByCanEdit(Long test_result_id) throws DaoException {
		try {
			String sql = "SELECT COUNT(*) FROM test_result_handler trh INNER JOIN test_result tr ON trh.test_result_id = tr.id WHERE trh.test_result_id = ?1 AND tr.del=0 AND trh.status IN (1,4,12,100)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, test_result_id);
			return Long.parseLong(query.getSingleResult().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("TestResultHandlerDAOImpl.count() 出现异常！", e);
		}
	}

	/**
	 * 根据报告id和结构化状态查找报告
	 * @author ZhangHui 2015/5/6
	 */
	@Override
	public TestResultHandler findByTestResultIdCanEdit(Long test_result_id) throws DaoException {
		try {
			String condition = " WHERE e.testResult.id = ?1 AND e.testResult.del = 0 AND e.status IN (1,4,12,100)";
			List<TestResultHandler> result = this.getListByCondition(condition, new Object[]{test_result_id});
			if(result.size() > 0){
				return result.get(0);
			}
			return null;
		} catch (JPAException e) {
			e.printStackTrace();
			throw new DaoException("TestResultHandlerDAOImpl.findByTestResultAndStatus() 出现异常！", e);
		}
	}
	
	/**
	 * 根据用户名查询已退回的结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countOfBack(String handler,String configure) {
		try {
			if(handler==null || "".equals(handler)){
				return 0;
			}
			String condition = " WHERE e.handler = ?1 AND e.testResult.del = 0 AND  (e.status = 4 or e.testResult.publishFlag = '2') " + this.getSqlConfigure(configure,false);
			return this.count(condition, new Object[]{handler});
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 根据用户名查询已退回的结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long count(String handler, int status,String configure) {
		try {
			if(handler==null || "".equals(handler)){
				return 0;
			}
			String condition = " WHERE e.handler = ?1 AND e.status = ?2 AND e.testResult.del = 0 " + this.getSqlConfigure(configure,false);
			return this.count(condition, new Object[]{handler, status});
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 根据用户名查询结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ToBeStructuredReportVO> getStructuredsByPage(String handler,
			int status, int page, int pageSize,String configure) throws DaoException {
		try {
			String sql ="SELECT e.id,e.service_order,e.proName,e.test_type," +
					"e.last_modify_time,e.last_modify_user,e.batch_serial_no,e.buName," +
					"e.production_date,e.publish_flag,e.fullPdfPath,e.barcode,e.status FROM  " +
					"(SELECT report.id,report.service_order,pro.name AS proName,pro.barcode,report.test_type," +
					"report.last_modify_time as last_modify_time,report.last_modify_user,sample.batch_serial_no," +
					"producer.`name` AS buName,sample.production_date,report.publish_flag,report.fullPdfPath,trh.status " +
					"FROM test_result_handler trh " +
					"LEFT JOIN test_result report " +
							"ON trh.test_result_id = report.id " +
					"LEFT JOIN product_instance sample " +
							"ON report.sample_id = sample.id " +
					"LEFT JOIN business_unit producer " +
							"ON sample.producer_id = producer.id " +
					"LEFT JOIN product pro " +
							"ON sample.product_id = pro.id " +
					"WHERE trh.handler=?1 AND trh.status = ?2 AND report.del = 0   "+ this.getSqlConfigure(configure,true) +" ) e " +
					" ORDER BY e.id ";
			
 			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, handler);
			query.setParameter(2, status);
			 if(page > 0 && pageSize > 0){
					query.setFirstResult((page-1)*pageSize);
					query.setMaxResults(pageSize);
			 }
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ToBeStructuredReportVO> vos = new ArrayList<ToBeStructuredReportVO>();
			if(result != null){
				for(Object[] obj : result){
					ToBeStructuredReportVO vo = new ToBeStructuredReportVO(((BigInteger)obj[0]).longValue(), 
							obj[1]==null?"":obj[1].toString(), obj[2]==null?"":obj[2].toString(),
							obj[3]==null?"":obj[3].toString(), obj[4]==null?null:(Date)obj[4], 
							obj[5]==null?"":obj[5].toString(), obj[6]==null?"":obj[6].toString(),
							obj[7]==null?"":obj[7].toString(), obj[8]==null?null:(Date)obj[8],
							obj[9]==null?"":obj[9].toString(), obj[10]==null?"":obj[10].toString(),
					        obj[11]==null?"":obj[11].toString(),((Integer)obj[12]).intValue());
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			throw new DaoException("TestResultHandlerDAOImpl.getStructuredsByPage() 出现异常！", e);
		}
	}
	
	/**
	 * 根据用户名查询已退回的结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ToBeStructuredReportVO> getBackStructuredsByPage(String handler,
			int page, int pageSize, String configure) throws DaoException {
		try {
			String sql ="SELECT e.id,e.service_order,e.proName,e.test_type," +
					"e.last_modify_time,e.last_modify_user,e.batch_serial_no,e.buName," +
					"e.production_date,e.publish_flag,e.fullPdfPath,e.barcode,e.status FROM  " +
					"(SELECT report.id,report.service_order,pro.name AS proName,pro.barcode,report.test_type," +
					"report.last_modify_time as last_modify_time,report.last_modify_user,sample.batch_serial_no," +
					"producer.`name` AS buName,sample.production_date,report.publish_flag,report.fullPdfPath,trh.status " +
					"FROM test_result_handler trh " +
					"LEFT JOIN test_result report " +
							"ON trh.test_result_id = report.id " +
					"LEFT JOIN product_instance sample " +
							"ON report.sample_id = sample.id " +
					"LEFT JOIN business_unit producer " +
							"ON sample.producer_id = producer.id " +
					"LEFT JOIN product pro " +
							"ON sample.product_id = pro.id " +
					"WHERE trh.handler=?1 AND report.del = 0  AND (trh.status = 4 OR report.publish_flag = '2')  "+this.getSqlConfigure(configure,true)+" ) e "+ 
					" ORDER BY e.id ";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, handler);
			if(page > 0 && pageSize > 0){
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
		    }
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ToBeStructuredReportVO> vos = new ArrayList<ToBeStructuredReportVO>();
			if(result != null){
				for(Object[] obj : result){
					ToBeStructuredReportVO vo = new ToBeStructuredReportVO(((BigInteger)obj[0]).longValue(), 
							obj[1]==null?"":obj[1].toString(), obj[2]==null?"":obj[2].toString(),
							obj[3]==null?"":obj[3].toString(), obj[4]==null?null:(Date)obj[4], 
							obj[5]==null?"":obj[5].toString(), obj[6]==null?"":obj[6].toString(),
							obj[7]==null?"":obj[7].toString(), obj[8]==null?null:(Date)obj[8],
							obj[9]==null?"":obj[9].toString(), obj[10]==null?"":obj[10].toString(),
							obj[11]==null?"":obj[11].toString(),((Integer)obj[12]).intValue());
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			throw new DaoException("TestResultHandlerDAOImpl.getStructuredsByPage() 出现异常！", e);
		}
	}
	
	/**
	 * 根据用户名查询已结构化报告数量
	 * @author ZhangHui 2015/5/7
	 */
	@Override
	public long countOfStructured(String handler,String configure) {
		try {
			if(handler==null || "".equals(handler)){
				return 0;
			}
			String condition = " WHERE e.handler = ?1 AND e.testResult.del = 0 AND e.status IN (2,8)" + this.getSqlConfigure(configure,false);
			return this.count(condition, new Object[]{handler});
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据用户名查询已退回的结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ToBeStructuredReportVO> getHasStructuredsByPage(String handler,
			int page, int pageSize, String configure) throws DaoException {
		try {
			String sql ="SELECT e.id,e.service_order,e.proName,e.test_type," +
					"e.last_modify_time,e.last_modify_user,e.batch_serial_no,e.buName," +
					"e.production_date,e.publish_flag,e.fullPdfPath,e.barcode,e.status FROM  " +
					"(SELECT report.id,report.service_order,pro.name AS proName,pro.barcode,report.test_type," +
					"report.last_modify_time as last_modify_time,report.last_modify_user,sample.batch_serial_no," +
					"producer.`name` AS buName,sample.production_date,report.publish_flag,report.fullPdfPath,trh.status " +
					"FROM test_result_handler trh " +
					"LEFT JOIN test_result report " +
							"ON trh.test_result_id = report.id AND report.del = 0 " +
					"LEFT JOIN product_instance sample " +
							"ON report.sample_id = sample.id " +
					"INNER JOIN business_unit producer " +
							"ON sample.producer_id = producer.id " +
					"LEFT JOIN product pro " +
							"ON sample.product_id = pro.id " +
					"WHERE trh.handler=?1 AND trh.status IN (2,8)  "+this.getSqlConfigure(configure,true)+" ) e " +
					" ORDER BY e.status asc,e.last_modify_time asc";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, handler);
			if(page > 0 && pageSize > 0){
				query.setFirstResult((page-1)*pageSize);
				query.setMaxResults(pageSize);
			}
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<ToBeStructuredReportVO> vos = new ArrayList<ToBeStructuredReportVO>();
			if(result != null){
				for(Object[] obj : result){
					ToBeStructuredReportVO vo = new ToBeStructuredReportVO(((BigInteger)obj[0]).longValue(), 
							obj[1]==null?"":obj[1].toString(), obj[2]==null?"":obj[2].toString(),
							obj[3]==null?"":obj[3].toString(), obj[4]==null?null:(Date)obj[4], 
							obj[5]==null?"":obj[5].toString(), obj[6]==null?"":obj[6].toString(),
							obj[7]==null?"":obj[7].toString(), obj[8]==null?null:(Date)obj[8],
							obj[9]==null?"":obj[9].toString(), obj[10]==null?"":obj[10].toString(),
					        obj[11]==null?"":obj[11].toString(),((Integer)obj[12]).intValue());
					vos.add(vo);
				}
			}
			return vos;
		} catch (Exception e) {
			throw new DaoException("TestResultHandlerDAOImpl.getHasStructuredsByPage() 出现异常！", e);
		}
	}

	private String getSqlConfigure(String configure,boolean flag) {
		String sqlWhere = "";
		if(configure!=null&&!"".equals(configure)&&!"0".equals(configure)){
			String[] conds = configure.split("@@");
			for (int i = 0; i < conds.length; i++) {
				String[] fields = conds[i].split("@");
				//数据库字段名称
				String tableName=""; 
				    if(flag){
						if("proName".equals(fields[0])){
							tableName = "pro.name";
						 }else if("barcode".equals(fields[0])){
							 tableName = "pro.barcode"; 
						 }else if("serviceOrder".equals(fields[0])){
							 tableName="report.service_order";
						 }else if("status".equals(fields[0])){
							 tableName="trh.status";
						 }else if("producerName".equals(fields[0])){
							 tableName = "producer.name";
						 }
				    }else{
				    	if("proName".equals(fields[0])){
							tableName = "e.testResult.sample.product.name";
						 }else if("barcode".equals(fields[0])){
							 tableName = "e.testResult.sample.product.barcode"; 
						 }else if("serviceOrder".equals(fields[0])){
							 tableName="e.testResult.serviceOrder";
						 }else if("status".equals(fields[0])){
							 tableName="e.status";
						 }else if("producerName".equals(fields[0])){
							 tableName = "e.testResult.sample.producer.name";
						 }
				    }
					if("eq".equals(fields[1])){
						sqlWhere +=" and " + tableName + " = '" + fields[2] + "'";
					}else if("neq".equals(fields[1])){
						sqlWhere +=" and " + tableName + " != '" + fields[2] + "'";
					}else if("contains".equals(fields[1])){
						sqlWhere +=" and " + tableName + " like '%" + fields[2] + "%'";
					}
			}
		}
		return sqlWhere;
	}

	/**
	 * 根据报告id判断该报告是否为完整的报告
	 * @author ZhangHui 2015/5/8
	 */
	@Override
	public boolean isCanViewAllInfo(long test_result_id) {
		try {
			String sql = "SELECT COUNT(*) FROM test_result tr " +
							   " INNER JOIN test_result_handler trh ON trh.test_result_id = tr.id AND tr.id = ?1 AND trh.`status` IN (1,2,4) " +
							   " WHERE tr.del = 0";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, test_result_id);
			long count = Long.parseLong(query.getSingleResult().toString());
			if(count > 0){
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 根据产品id查找最近一次已结构化报告id
	 * @author ZhangHui 2015/5/8
	 */
	@SuppressWarnings("unchecked")
	@Override
	public long getTestResultIdOfHasStructed(long productId) {
		try {
			String sql = "SELECT tr.id FROM test_result tr " +
						 "INNER JOIN test_result_handler trh ON trh.test_result_id = tr.id AND trh.`status` IN (2,8) " +
						 "INNER JOIN product_instance pin ON pin.id = tr.sample_id AND pin.product_id = ?1 " +
						 "WHERE tr.del = 0 " +
						 "ORDER BY tr.last_modify_time DESC LIMIT 0,1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, productId);
			
			List<Object> result = query.getResultList();
			
			if(result!=null && result.size()>0){
				Object obj = result.get(0);
				return ((BigInteger)obj).longValue();
			}
			
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 根据报告id和状态查找结构化报告数量
	 * @author ZhangHui 2015/5/8
	 */
	@Override
	public long count(long myReportId, int status) {
		try {
			String condition = " WHERE e.testResult.id = ?1 AND e.status = ?2 AND e.testResult.del = 0 ";
			return this.count(condition, new Object[]{myReportId, status});
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public TestResultHandler getMinTestResultHandler(String user_handler) {
		String userName  = this.getSQLMin(user_handler);
		TestResultHandler handler = null;
			try {
				if(userName==null){
					String sql = "SELECT id,HANDLER,MIN(total) FROM (SELECT  trh.id,trh.HANDLER,COUNT(trh.HANDLER) AS total FROM  test_result_handler trh ";
					sql+="  INNER JOIN test_result tr ON  trh.test_result_id = tr.id " ;       
					sql+="  WHERE trh.STATUS=1 AND  tr.del = 0 AND trh.HANDLER IN("+user_handler+") GROUP BY trh.HANDLER ORDER BY total) a";
					Query query = entityManager.createNativeQuery(sql);
					List<Object[]> result = query.getResultList();
					if(result.size() > 0){
						handler = new TestResultHandler();
						Object[] obj = result.get(0);
						handler.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
						handler.setHandler(obj[1]==null?"":obj[1].toString());
					}
				}else{
					handler = new TestResultHandler(); 
					handler.setHandler(userName);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		return handler;
	}

	private String getSQLMin(String user_handler) {
		String[] strSql = user_handler.split(",");
		String userName = null;
		for (int i = 0; i < strSql.length; i++) {
			String sql ="SELECT COUNT(*) FROM test_result_handler trh INNER JOIN test_result tr ON trh.test_result_id = tr.id  WHERE  trh.STATUS = 1  AND  tr.del = 0  AND trh.HANDLER="+strSql[i];
			Query query = entityManager.createNativeQuery(sql);
			long count = Long.parseLong(query.getSingleResult().toString());
			if(count == 0){
				userName = strSql[i].replaceAll("'", "");
				break;
			}
		}
			return userName;
	}
}