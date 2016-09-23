package com.gettec.fsnip.fsn.service.market;

import java.util.List;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;


public interface MkCategoryService {

	/**
	 * 获取一级分类、二级分类
	 * @author ZhangHui 2015/4/27
	 */
	List<ProductCategoryVO> getListOfCategoryVO(int level,String parentCode) throws ServiceException;

	ProductCategory findCategoryByCode(String category) throws ServiceException;
	
	ProductCategory getCategoryByName(String categoryName) throws ServiceException;

	/**
	 * 加载产品分类的第一大类
	 * @return List<ProductCategory>
	 * @throws ServiceException
	 * 
	 * @author HuangYog
	 */
    List<ProductCategory> loadProduCtcategory() throws ServiceException;

	ProductCategory findById(Long id) throws ServiceException;

	List<ProductCategory> getListOfCategory(Long level, String parentCode)
			throws ServiceException;

}
