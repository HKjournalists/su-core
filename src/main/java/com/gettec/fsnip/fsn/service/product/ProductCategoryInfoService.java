package com.gettec.fsnip.fsn.service.product;

import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.dao.product.ProductCategoryInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;

public interface ProductCategoryInfoService extends BaseService<ProductCategoryInfo, ProductCategoryInfoDAO>{
	Set<ProductCategoryInfo> getRegularityByString(String regularityStr,ProductCategory twoCategory)
			throws ServiceException;

	ProductCategoryInfo saveCategoryInfo(ProductCategoryInfo categoryInfo)throws ServiceException;
	
	ProductCategoryInfo findByNameAndCategoryIdAndFlag(String name,
			Long categoryId, boolean categoryFlag) throws ServiceException;
	ProductCategoryInfo findByCategoryId(Long categoryId) throws ServiceException;
	
	/**
	 * 获取三级分类
	 * @author ZhangHui 2015/4/27
	 */
	List<ProductCategoryVO> getListVOByParentcode(String parentcode,
			boolean iscategory) throws ServiceException;

	List<ProductCategoryInfo> getListOfUpId(Long upId, boolean categoryFlag)
			throws ServiceException;

	/**
	 * 根据产品id获取产品执行标准
	 * @author longxianzhen 2015-08-06
	 */
	List<ProductCategoryInfo> getRegularityByProId(Long proId)throws ServiceException;

}