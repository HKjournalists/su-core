package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductRecommendUrlDAO;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;

@Repository(value="recommendUrlDAO")
public class ProductRecommendUrlDAOImpl extends BaseDAOImpl<ProductRecommendUrl>
implements ProductRecommendUrlDAO{
	
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	public List<ProductRecommendUrl> getProductRecommendUrl(Long proId,String type) {
		List<ProductRecommendUrl>  urlList = new ArrayList<ProductRecommendUrl>();
		String jpql = "SELECT  *  FROM  product_recommend_url where  pro_id = "+proId ;
		if(type!=null&&!"".equals(type)){
			jpql+= " and (status = 0 or identify= '"+type+"')";
		}else{
		   jpql +="  and status = 0 ";
		}		
		urlList = (List<ProductRecommendUrl>) entityManager.createNativeQuery(jpql,ProductRecommendUrl.class).getResultList();
		return urlList;
	}
	
	public List<ProductRecommendUrl> getProductRecommendUrl(Long proId) {
		String jpql = "SELECT e FROM " + entityClass.getName() + " e where e.proId=:proId";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("proId",proId);
		return query.getResultList();
	}
}
