package com.gettec.fsnip.fsn.dao.product;


import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;

public interface ProductNutriDAO  extends BaseDAO<ProductNutrition>{

	List<ProductNutrition> getListOfNutrisByProductId(Long productId) throws DaoException;
	
	/**
	 * 
	 * @param productId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws DaoException
	 */
	List<ProductNutrition> getListOfProductNutritionByProductIdWithPage(
            Long productId, int page, int pageSize) throws DaoException;
    /**
     * 按产品id获取营养报告总条数
     * @param productId
     * @return Long
     * @throws DaoException
     */
    long countByproductId(Long productId) throws DaoException;

    /**
     * 按点击的列号获取营养报告中相对应的数据
     * @param columeId
     * @return List
     * @throws ServiceException
     */
    List<String> getListOfColumeValue(int columeId) throws DaoException ;
}
