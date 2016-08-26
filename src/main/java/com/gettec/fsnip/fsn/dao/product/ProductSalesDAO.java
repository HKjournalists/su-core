package com.gettec.fsnip.fsn.dao.product;



import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;

import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.product.ProductSales;
/**
 * ProductPoll DAO
 * 
 * @author 
 */
 public interface ProductSalesDAO extends BaseDAO<ProductSales>{

	List<ProductSales> getListByProIdPage(int page, int pageSize,
			Map<String, Object> map)throws DaoException;

	Long getCountByproId(Map<String, Object> map)throws DaoException;

	List<Area> getAreaByLevel(int level)throws DaoException;

	List<Area> getMunicipalityByparentId(int parentId)throws DaoException;

	
}