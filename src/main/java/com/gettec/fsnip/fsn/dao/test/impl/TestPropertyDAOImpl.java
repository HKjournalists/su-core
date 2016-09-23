package com.gettec.fsnip.fsn.dao.test.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.TestPropertyDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.test.TestProperty;

/**
 * TestProperty customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="testPropertyDAO")
public class TestPropertyDAOImpl extends BaseDAOImpl<TestProperty>
		implements TestPropertyDAO {
		
	/**
	 * 按报告Id查找检测项目集合
	 * 
	 * @throws DaoException
	 */
	@Override
	public List<TestProperty> getListByReportIdWithPage(Long reportId,
			int page, int pageSize) throws DaoException {
		try {
			String condition = " WHERE e.testResultId = ?1 ORDER BY ID ASC";
			return this.getListByPage(page, pageSize, condition,
					new Object[] { reportId });
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按报告Id查找检测项目集合，出现异常",
					jpae.getException());
		}
	}
	
	/**
	 * 按报告Id查找检测项目的条数
	 * 
	 * @throws DaoException
	 */
	@Override
	public Long countByReportId(Long reportId) throws DaoException {
		try {
			String condition = " WHERE e.testResultId = ?1";
			return this.count(condition, new Object[] { reportId });
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按报告Id查找检测项目的条数，出现异常",
					jpae.getException());
		}
	}

	/**
	 * 按报告Id查找检测项目列表
	 * @throws DaoException 
	 */
	@Override
	public List<TestProperty> findByReportId(Long reportId) throws DaoException{
		try {
			String condition = " WHERE e.testResultId = ?1 ORDER BY ID ASC";
			return this.getListByCondition(condition,
					new Object[] { reportId });
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按报告Id查找检测项目集合，出现异常",
					jpae.getException());
		}
	}
	
	/**
	 * 根据报告id查找检测项目（无需返回id）
	 * @author ZhangHui 2015/5/8
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TestProperty> findByReportIdWithoutId(Long reportId) throws DaoException{
		try {
			if(reportId == null){
				return null;
			}
			
			String sql = "SELECT `name`,unit,tech_indicator,result,assessment,standard FROM test_property WHERE test_result_id = ?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, reportId);
			/* 数据封装 */
			List<Object[]> result = query.getResultList();
			List<TestProperty> tpys = new ArrayList<TestProperty>();
			if(result != null){
				for(Object[] obj : result){
					TestProperty tp = new TestProperty(obj[0]==null?"":obj[0].toString(), obj[1]==null?"":obj[1].toString(),
							obj[2]==null?"":obj[2].toString(), obj[3]==null?"":obj[3].toString(), obj[4]==null?"":obj[4].toString(),
							obj[5]==null?"":obj[5].toString());
					tpys.add(tp);
				}
			}
			return tpys;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException("ReportDAOImpl.getListOfReport() 出现异常！", e);
		}
	}
	
	/**
	 * 按检测项目不同的列名查找不同列的集合信息
	 * @throws DaoException
	 */
	@Override
	public List<String> getListOfColumeValue(int columnName,String keyword,int page,int pageSize)
			throws DaoException {
		try {
			String sql = null ,condition=" LIMIT 0,20 ";
			final int ITEM_NAME = 1, ITEM_UNIT = 2, SPECIFICATION = 3, TEST_RESULT = 4, STANDARD = 5;
			if(keyword==null){
				keyword = "";
			}
			switch (columnName) {
			case ITEM_NAME:
				sql = "SELECT DISTINCT name FROM test_property WHERE name IS NOT NULL AND name like '%"+keyword+"%'";
				break;
			case ITEM_UNIT:
				sql = "SELECT DISTINCT unit FROM test_property WHERE unit IS NOT NULL AND unit like '%"+keyword+"%'";
				break;
			case SPECIFICATION:
				sql = "SELECT DISTINCT tech_indicator FROM test_property WHERE tech_indicator IS NOT NULL AND tech_indicator like '%"+keyword+"%'";
				break;
			case TEST_RESULT:
				sql = "SELECT DISTINCT result FROM test_property WHERE result IS NOT NULL AND result like '%"+keyword+"%'";
				break;
			case STANDARD:
				sql = "SELECT DISTINCT standard FROM test_property WHERE standard IS NOT NULL AND standard like '%"+keyword+"%'";
				break;
			}
			if (sql == null) {
				return null;
			}
			if(page>0){
				condition = " LIMIT " + (page - 1) * pageSize + "," + pageSize;	
			}
			sql+=condition;
			return this.getListBySQLWithoutType(String.class, sql, null);
		} catch (Exception e) {
			throw new DaoException("【DAO-error】按检测项目不同的列名查找不同列的集合信息，出现异常", e);
		}
	}

	/**
	 * 根据报告ID删除检测项目
	 * @param reportid
	 * @throws DaoException
	 * @author LongXianZhen
	 */
	@Override
	public void deleByTestResultId(Long reportid) throws DaoException {
		try {
			String sql="DELETE FROM test_property WHERE test_result_id=?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, reportid);
			query.executeUpdate();
		} catch (Exception e) {
	        throw new DaoException("TestPropertyDAOImpl.findByTestResult()-->",e);
	    }
	}

}