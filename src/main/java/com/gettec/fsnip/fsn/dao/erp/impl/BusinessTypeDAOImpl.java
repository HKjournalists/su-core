package com.gettec.fsnip.fsn.dao.erp.impl;


import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.BusinessTypeDAO;
import com.gettec.fsnip.fsn.model.erp.base.BusinessType;

@Repository("businessTypeDAO")
public class BusinessTypeDAOImpl extends BaseDAOImpl<BusinessType> 
		implements BusinessTypeDAO{

	@SuppressWarnings("unchecked")
	public List<BusinessType> getBusinessTypefilter_(int from,int size,String configure){
		String jpql="select e from T_META_BUSINESS_TYPE e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		List<BusinessType> result = query.getResultList();
		return result;
	}
	
	public long countBusinessType(String configure) {
		String jpql = "SELECT count(*) FROM T_META_BUSINESS_TYPE e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
}
