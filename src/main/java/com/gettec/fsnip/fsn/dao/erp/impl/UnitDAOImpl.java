package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.UnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.erp.base.Unit;

@Repository("unitDAO")
public class UnitDAOImpl extends BaseDAOImpl<Unit> implements UnitDAO {

	public long countUnit(String configure) {
		String jpql = "SELECT count(*) FROM T_META_UNIT e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<Unit> getPaging(int page, int size, String condition) {
		List<Unit> listOfEntity = null;
		try {
			StringBuffer buffer = new StringBuffer("SELECT e FROM " + entityClass.getName() + " e ");
			if(condition != null && condition.trim() != ""){
				buffer.append(condition);
			}
			Query query = entityManager.createQuery(buffer.toString());
			query.setFirstResult((page - 1)*size);
			query.setMaxResults(size);
			
			listOfEntity = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfEntity;
	}
	
	public Unit findByBusunitName(String unitName) throws DaoException {
		try {
			String condition = " WHERE e.name = ?1";
			List<Unit> result = this.getListByCondition(condition, new Object[]{unitName});
			if(result.size() > 0) {
				return result.get(0);
			}
			return null;
		} catch (JPAException jpae) {
			throw new DaoException("【DAO-error】按单位名称查找单位信息，出现异常", jpae.getException());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Unit> findAll(Long organization, String configure) {
		String jpql = "SELECT e FROM T_META_UNIT e";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		return query.getResultList();
	}

	/**
	 * 获取所有的产品单位名称
	 */
	@Override
	public List<String> getAllUnitName() throws DaoException {
		try{
			String sql="select name from T_META_UNIT";
			return this.getListBySQLWithoutType(String.class, sql, null);
		}catch(JPAException jpae){
			throw new DaoException("UnitDAOImpl.getAllUnitName()-->"+jpae.getMessage(),jpae.getException());
		}
	}
	
}
