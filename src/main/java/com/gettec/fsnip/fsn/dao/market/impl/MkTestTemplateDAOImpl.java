package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.MkTestTemplateDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.MkTestTemplate;
import com.gettec.fsnip.fsn.model.test.TestResult;


@Repository(value="templateDAO")
public class MkTestTemplateDAOImpl extends BaseDAOImpl<MkTestTemplate> 
						implements MkTestTemplateDAO{
	/**
	 * 从template中查找所有barcode集合
	 * @throws DaoException 
	 */
	@Override
	public List<String> getListOfBarCode(Long orignizatonId) throws DaoException{
		try {
			String sql = "SELECT DISTINCT BAR_CODE FROM T_TEST_TEMPLATE";
			return this.getListBySQL(String.class, sql, null);
		} catch (Exception e) {
			throw new DaoException("【DAO-error】从template中查找所有barcode集合，出现异常", e);
		}
	}
	
	/**
	 * 按barcod从template查找一条信息
	 * @throws DaoException 
	 */
	@Override
	public MkTestTemplate findByBarCode(String barCode, Long organization, String userName) throws DaoException {
		try {
			String condition = " WHERE e.barCode = ?1 AND e.orignizatonId = ?2 AND e.userName = ?3";
			List<MkTestTemplate> resultList = this.getListByCondition(condition, new Object[]{barCode, organization, userName});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按barcod从template查找一条信息，出现异常", jpae.getException());
		}
	}
	
	/**
	 * 按barcod从template查找一条信息
	 * @throws DaoException 
	 */
	@Override
	public List<MkTestTemplate> findByBarCode(String barCode, Long organization) throws DaoException {
		try {
			String condition = " WHERE e.barCode = ?1 AND e.orignizatonId = ?2";
			return this.getListByCondition(condition, new Object[]{barCode, organization});
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按barcod从template查找一条信息，出现异常", jpae.getException());
		}
	}
	
	/**
	 * 按barcod从template查找一条信息
	 * @throws DaoException 
	 */
	@Override
	public MkTestTemplate findByBarCode(String barCode) throws DaoException {
		try {
			String condition = " WHERE e.barCode = ?1";
			List<MkTestTemplate> resultList =  this.getListByCondition(condition, new Object[]{barCode});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按barcod从template查找一条信息，出现异常", jpae.getException());
		}
	}
	
	/**
	 * 按reportId从template查找一条信息
	 * @throws DaoException 
	 */
	@Override
	public MkTestTemplate findByReportId(Long reportId) throws DaoException {
		try {
			String condition = " WHERE e.report.id = ?1";
			List<MkTestTemplate> resultList = this.getListByCondition(condition, new Object[]{reportId});
			if(resultList.size() > 0){
				return resultList.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按reportId从template查找一条信息，出现异常", jpae.getException());
		}
	}
	
	/**
	 * 根据 barcode 和 organization 和 userName 查找一条template模板信息id
	 * @author tangxin 2015/6/8
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long findIdByBarCodeAndOrganizationAndUserName(String barcode, Long organization, String userName) throws DaoException {
		try {
			if(barcode == null || "".equals(barcode) || organization == null 
					|| userName == null || "".equals(userName)) {
				throw new DaoException("参数为空",new Exception("参数为空"));
			}
			String sql = "SELECT id FROM T_TEST_TEMPLATE WHERE BAR_CODE = ?1 AND ORG_ID = ?2 AND USER_NAME = ?3";
			
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, barcode);
			query.setParameter(2, organization);
			query.setParameter(3, userName);
			
			List<Object> result = query.getResultList();
			if(result!=null && result.size()>0){
				return Long.parseLong(result.get(0).toString());
			}
			
			return null;
		} catch (Exception e) {
			throw new DaoException("【DAO-error】根据 barcode 和 organization 和 userName 查找一条template模板信息id 出现异常！" + e.getMessage(), e);
		}
	}
	
	/**
	 * 根据 id 更新 template 中的 barcode 和 reportid 字段 
	 * @author tangxin 2015/6/8
	 * @throws DaoException
	 */
	@Override
	public boolean updateById(Long id, String barcode, Long reportId) throws DaoException {
		try {
			if(id == null || barcode == null || "".equals(barcode) || reportId == null) {
				throw new DaoException("参数为空",new Exception("参数为空"));
			}
			String sql = "UPDATE T_TEST_TEMPLATE SET BAR_CODE = ?1, REPORT_ID = ?2 WHERE id = ?3";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, barcode);
			query.setParameter(2, reportId);
			query.setParameter(3, id);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			throw new DaoException("【DAO-error】根据 id 更新 template 中的 barcode 和 reportid 字段 时出现异常！" + e.getMessage(), e);
		}
	}
	
	/**
	 * 创建 template 信息 
	 * @author tangxin 2015/6/8
	 * @throws DaoException
	 */
	@Override
	public boolean createBySql(String barcode, Long reportId, Long organization, String userName) throws DaoException {
		try {
			if(reportId == null || barcode == null || "".equals(barcode) 
					|| organization == null || userName == null || "".equals(userName)) {
				throw new DaoException("参数为空",new Exception("参数为空"));
			}
			String sql = "INSERT INTO T_TEST_TEMPLATE(BAR_CODE, REPORT_ID, ORG_ID, USER_NAME) VALUES(?1,?2,?3,?4)";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, barcode);
			query.setParameter(2, reportId);
			query.setParameter(3, organization);
			query.setParameter(4, userName);
			query.executeUpdate();
			return true;
		} catch (Exception e) {
			throw new DaoException("【DAO-error】创建 template 信息  时出现异常！" + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MkTestTemplate findByTemplateBarCode(String barcode,
			Long organization, String userName) {
		String sql = this.getSql();
		       sql+="WHERE e.BAR_CODE = ?1 AND e.ORG_ID = ?2 AND e.USER_NAME = ?3  ";
		       Query query = entityManager.createNativeQuery(sql);
		       query.setParameter(1, barcode);
			   query.setParameter(2, organization);
			   query.setParameter(3, userName);
			   List<Object[]> objs = query.getResultList();
			   MkTestTemplate template = new MkTestTemplate();
			   for (Object[] obj : objs) {
				   TestResult testResult = new TestResult();
				   template.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
				   testResult.setId(obj[1]==null?null:Long.parseLong(obj[1].toString()));
				   template.setReport(testResult);
				   template.setBarCode(obj[2]==null?"":obj[2].toString());
				   template.setOrignizatonId(obj[3]==null?null:Long.parseLong(obj[3].toString()));
				   template.setUserName(obj[4]==null?"":obj[4].toString());
				   break;
			   }
			   
		return template;
	}
	@SuppressWarnings("unchecked")
	@Override
	public MkTestTemplate findByTemplateBarCode(String barcode,Long organization) {
		String sql = this.getSql();
		sql+="WHERE e.BAR_CODE = ?1 AND e.ORG_ID = ?2  ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, barcode);
		query.setParameter(2, organization);
		List<Object[]> objs = query.getResultList();
		MkTestTemplate template = new MkTestTemplate();
		for (Object[] obj : objs) {
			 TestResult testResult = new TestResult();
			   template.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
			   testResult.setId(obj[1]==null?null:Long.parseLong(obj[1].toString()));
			   template.setReport(testResult);
			template.setBarCode(obj[2]==null?"":obj[2].toString());
			template.setOrignizatonId(obj[3]==null?null:Long.parseLong(obj[3].toString()));
			template.setUserName(obj[4]==null?"":obj[4].toString());
			break;
		}
		
		return template;
	}
	@SuppressWarnings("unchecked")
	@Override
	public MkTestTemplate findByTemplateBarCode(String barcode) {
		String sql = this.getSql();
		sql+="WHERE e.BAR_CODE = ?1 ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(1, barcode);
		List<Object[]> objs = query.getResultList();
		MkTestTemplate template = new MkTestTemplate();
		for (Object[] obj : objs) {
			 TestResult testResult = new TestResult();
			   template.setId(obj[0]==null?null:Long.parseLong(obj[0].toString()));
			   testResult.setId(obj[1]==null?null:Long.parseLong(obj[1].toString()));
			   template.setReport(testResult);
			template.setBarCode(obj[2]==null?"":obj[2].toString());
			template.setOrignizatonId(obj[3]==null?null:Long.parseLong(obj[3].toString()));
			template.setUserName(obj[4]==null?"":obj[4].toString());
			break;
		}
		
		return template;
	}

	private String getSql() {
		String sql ="select e.id,e.REPORT_ID,e.BAR_CODE,e.ORG_ID,e.USER_NAME from T_TEST_TEMPLATE e  ";
		return sql;
	}
}
