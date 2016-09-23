package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.ProductNutriDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProductNutriService extends BaseService<ProductNutrition, ProductNutriDAO>{

	List<ProductNutrition> getListOfNutrisByProductId(Long productId) throws ServiceException;

	void save(Product product) throws ServiceException;
	
	/**
	 * 根据点击的列号得到这一列对应的数据
	 * @param colName
	 * @return List
	 * @throws ServiceException
	 */
	List<String> autoNutritionItems(int colNameId) throws ServiceException, DaoException;

}
