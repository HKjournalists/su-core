package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.OutStorageTypeDAO;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageType;

@Repository("outStorageTypeDAO")
public class OutStorageTypeDAOImpl extends BaseDAOImpl<OutStorageType> 
		implements OutStorageTypeDAO{

	@SuppressWarnings("unchecked")
	public List<OutStorageType> getOutStorageTypefilter_(int from,int size,String configure) {
		String jpql="select e from T_META_OUT_STORAGE_TYPE e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
//		query.setFirstResult(from);  
//		query.setMaxResults(size); 
//		System.out.println(query);
		List<OutStorageType> result = query.getResultList();
		return result;
	}
	
	public long countOutStorageType(String configure) {
		String jpql = "SELECT count(*) FROM T_META_OUT_STORAGE_TYPE e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
}
