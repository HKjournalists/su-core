package com.gettec.fsnip.fsn.dao.erp.buss.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseStorageInfoDAO;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfo;

@Repository("merchandiseStorageInfoDAO")
public class MerchandiseStorageInfoDAOImpl extends BaseDAOImpl<MerchandiseStorageInfo> 
		implements MerchandiseStorageInfoDAO{

	@SuppressWarnings("unchecked")
	public List<MerchandiseStorageInfo> getMerchandiseStorageInfofilter_(int from,
			int size, String configure) {
		String jpql="select e from T_BUSS_MERCHANDISE_STORAGE_INFO e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
//		query.setFirstResult(from);  
//		query.setMaxResults(size); 
//		System.out.println(query);
		List<MerchandiseStorageInfo> result = query.getResultList();
		return result;
	}

	public long countMerchandiseStorageInfo(String configure) {
		String jpql = "SELECT count(*) FROM T_BUSS_MERCHANDISE_STORAGE_INFO e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
}
