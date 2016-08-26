package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.MerchandiseInfoDAO;
import com.gettec.fsnip.fsn.model.product.Product;

@Repository("merchandiseInfoDAO")
public class MerchandiseInfoDAOImpl extends BaseDAOImpl<Product> implements MerchandiseInfoDAO {

	@SuppressWarnings("unchecked")
	public List<Product> getMerchandiseInfofilter_(int from,int size,String configure) {
		String jpql="select e from product e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
//		query.setFirstResult(from);  
//		query.setMaxResults(size); 
//		System.out.println(query);
		List<Product> result = query.getResultList();
		return result;
	}
	
	public long countMerchandiseInfo(String configure) {
		String jpql = "SELECT count(*) FROM product e ";
		if(configure != null){
			jpql = jpql+configure;
		}
		Query query = entityManager.createQuery(jpql);
		Object rtn = query.getSingleResult();
    	return rtn == null ? 0 : Long.parseLong(rtn.toString());
	}
	
	public boolean updateMerchandiseInfo(String configure) {
		boolean result = false;
		try {
			String jpql = "UPDATE product set " + configure;
			Query query = entityManager.createQuery(jpql);
			query.executeUpdate();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
}
