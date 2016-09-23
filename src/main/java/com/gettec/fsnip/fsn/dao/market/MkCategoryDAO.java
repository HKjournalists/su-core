package com.gettec.fsnip.fsn.dao.market;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;

public interface MkCategoryDAO extends BaseDAO<ProductCategory>{

	/**
	 * 查找一级分类、二级分类
	 * @author ZhangHui 2015/4/27
	 */
	List<ProductCategoryVO> getListOfCategory(int level,String parentCode) throws DaoException;
	
	ProductCategory findByCode(String code) throws DaoException;
	ProductCategory getCategoryByName(String categoryName) throws DaoException;
	
	/**
	 * 加载产品分类的第一大类
	 * @return List<ProductCategory>
	 * @throws DaoException
	 * 
	 * @author HuangYog
	 */
    List<ProductCategory> loadProduCtcategory()throws DaoException;
    
	List<ProductCategory> getListOfCategory(Long level, String parentCode)
			throws DaoException;
}
