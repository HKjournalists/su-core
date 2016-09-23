package com.gettec.fsnip.fsn.dao.product.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.MapProductAddrDAO;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.model.product.MapProductAddr;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
@Repository
public class MapProductAddrDAOImpl extends BaseDAOImpl<MapProductAddr> implements MapProductAddrDAO {

	@Override
	public List<MapProduct> getAllMapProducts() {
		String jpql="SELECT id,product_name from t_map_product";
		Query query = entityManager.createNativeQuery(jpql);
		List<Object[]> objList=query.getResultList();
		List<MapProduct>  productList=new ArrayList<MapProduct>();
		for(Object obj[]:objList){
			
			MapProduct mapProduct = new MapProduct();
			mapProduct.setId(obj[0]==null?null: Long.parseLong(obj[0].toString()));
			mapProduct.setProductName(obj[1]==null?"":obj[1].toString());
			
			productList.add(mapProduct);
		}
		return productList;
	}
}
