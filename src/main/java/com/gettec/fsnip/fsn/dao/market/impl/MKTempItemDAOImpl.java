package com.gettec.fsnip.fsn.dao.market.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.market.MkTempItemDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.market.MkTempReportItem;

@Repository(value="tempItemDAO")
public class MKTempItemDAOImpl extends BaseDAOImpl<MkTempReportItem> 
					implements MkTempItemDAO{
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<MkTempReportItem> getMkTempReportItemList(String orderNo,String userName,int page,int pageSize) {
		
		String jpql = "SELECT e FROM " + entityClass.getName() + " e WHERE e.tempReport.reportNo=?1 and e.tempReport.createUserRealName=?2";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, orderNo);
		query.setParameter(2, userName);
		if (page > 0 && pageSize > 0) {
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
		}
		return query.getResultList();
	}
    
	public long getCount(String orderNo, String realUserName) {
		String condition = " WHERE e.tempReport.reportNo=?1 and e.tempReport.createUserRealName=?2";
		long count = 0l ;
		  try {
			count = this.count(condition, new Object[]{orderNo, realUserName});
		} catch (JPAException e) {
			e.printStackTrace();
		}
		return count;
	}

	public boolean deleteById(long id) {
		String  sql = "delete from  t_test_temp_item where id = ?1 ";
		Query query=entityManager.createNativeQuery(sql);
		query.setParameter(1, id);
		if(query.executeUpdate()>0){
			return true; 
		}
		return false;
	}
}
