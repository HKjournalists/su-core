package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.exception.ServiceException;

import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.product.ProductSales;



/**
 * ProductPoll service
 * 
 * @author 
 */
public interface ProductSalesService {

	List<ProductSales> getListByProIdPage(int page, int pageSize,
			String configure, Long productId,Long org)throws ServiceException;

	Long getCountByproId(Long productId,Long org,String configure)throws ServiceException;

	List<Area> getAreaByLevel(int level)throws ServiceException;

	List<Area> getMunicipalityByparentId(int parentId)throws ServiceException;

	void addProductSales(ProductSales productSales, Long org)throws ServiceException;

	ProductSales getById(Long id)throws ServiceException;

	void editProductSales(ProductSales productSales)throws ServiceException;

	void deleteById(Long id)throws ServiceException;

	

}