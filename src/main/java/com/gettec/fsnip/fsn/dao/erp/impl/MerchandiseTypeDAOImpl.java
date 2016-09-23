package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.MerchandiseTypeDAO;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseType;

@Repository("merchandiseTypeDAO")
public class MerchandiseTypeDAOImpl extends BaseDAOImpl<MerchandiseType> 
			implements MerchandiseTypeDAO{

	@SuppressWarnings("unchecked")
	public List<MerchandiseType> getMerchandiseTypefilter_(int from,int size,String configure) {
		String jpql="select e from T_META_MERCHANDISE_TYPE e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
//		query.setFirstResult(from);  
//		query.setMaxResults(size); 
//		System.out.println(query);
		List<MerchandiseType> result = query.getResultList();
		return result;
	}
	
	public long countMerchandiseType(String configure) {
		String jpql = "SELECT count(*) FROM T_META_MERCHANDISE_TYPE e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
}
