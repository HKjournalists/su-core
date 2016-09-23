package com.gettec.fsnip.fsn.dao.erp.buss.impl;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.buss.StorageChangeLogDAO;
import com.gettec.fsnip.fsn.model.erp.buss.StorageChangeLog;

@Repository("storageChangeLogDAO")
public class StorageChangeLogDAOImpl extends BaseDAOImpl<StorageChangeLog> 
		implements StorageChangeLogDAO{

	@SuppressWarnings("unchecked")
	public List<StorageChangeLog> getStorageChangeLogfilter_(int from,int size,String configure) {
		String jpql="select e from T_BUSS_STORAGE_LOG e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
//		query.setFirstResult(from);  
//		query.setMaxResults(size); 
//		System.out.println(query);
		List<StorageChangeLog> result = query.getResultList();
		return result;
	}
	
	public long countStorageChangeLog(String configure) {
		String jpql = "SELECT count(*) FROM T_BUSS_STORAGE_LOG e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
}
