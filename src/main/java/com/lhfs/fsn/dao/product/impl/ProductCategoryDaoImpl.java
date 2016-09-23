package com.lhfs.fsn.dao.product.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.lhfs.fsn.dao.product.ProductCategoryDao;
/**
 * 
 * @author Kxo
 *
 */
@Repository
public class ProductCategoryDaoImpl extends BaseDAOImpl<ProductCategory> implements ProductCategoryDao{

	@Override
	public List<ProductCategory> findByCode(String productCategory) {
		List<ProductCategory> result = entityManager.createQuery("  from ProductCategory pc where pc.code = ? "
				,ProductCategory.class).setParameter(1, productCategory)
				.getResultList();
		return result;
	}

}
