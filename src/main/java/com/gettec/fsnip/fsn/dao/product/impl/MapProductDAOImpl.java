package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.MapProductDAO;
import com.gettec.fsnip.fsn.model.product.MapProduct;
@Repository
public class MapProductDAOImpl extends BaseDAOImpl<MapProduct> implements MapProductDAO {
	public MapProduct findByProductId(long productId){
		String sql="select e from "+this.entityClass.getName()+" e where e.productId=?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1,productId);
		List<MapProduct> mapProductList=query.getResultList();
		query.setMaxResults(1);
		if(mapProductList.size()>0){
			return mapProductList.get(0);
		}else{
			return null;
		}
	}
}
