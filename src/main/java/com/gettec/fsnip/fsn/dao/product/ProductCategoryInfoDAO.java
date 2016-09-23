package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.vo.product.ProductCategoryVO;

public interface ProductCategoryInfoDAO extends BaseDAO<ProductCategoryInfo>{
	
	/**
	 * 获取三级分类/执行标准
	 * @author ZhangHui 2015/4/27
	 */
	public List<ProductCategoryVO> getListByParentcode(String parentcode, boolean iscategory)throws DaoException;

	public ProductCategoryInfo findByNameAndCategoryIdAndFlag(String name, Long categoryId,boolean categoryFlag) throws DaoException;

	List<ProductCategoryInfo> getListOfUpId(Long upId, boolean categoryFlag)
			throws DaoException;

	/**
	 * 根据产品id获取产品执行标准
	 * @author longxianzhen 2015-08-06
	 */
	public List<ProductCategoryInfo> getRegularityByProId(Long proId)throws DaoException;

	ProductCategoryInfo findByCategoryId(Long categoryId) throws DaoException;
}