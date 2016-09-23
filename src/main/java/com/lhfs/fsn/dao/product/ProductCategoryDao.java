package com.lhfs.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
/**
 * 
 * @author Kxo
 *
 */
public interface ProductCategoryDao extends BaseDAO<ProductCategory>{

	List<ProductCategory> findByCode(String productCategory);

}
