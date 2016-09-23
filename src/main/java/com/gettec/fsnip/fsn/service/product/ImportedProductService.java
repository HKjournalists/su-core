package com.gettec.fsnip.fsn.service.product;

import com.gettec.fsnip.fsn.dao.product.ImportedProductDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ImportedProductService extends
		BaseService<ImportedProduct, ImportedProductDAO> {

	/**
	 * 保存进口产品信息
	 * @author longxianzhen 2015/05/27
	 */
	void save(Product product)throws ServiceException;

	/**
	 * 根据产品id查找进口产品信息
	 * @author longxianzhen 2015/05/27
	 */
	ImportedProduct findByProductId(Long proIdd)throws ServiceException;

	/**
	 * 根据产品id删除进口产品信息（假删除）
	 * @author longxianzhen 2015/05/27
	 */
	void deleteByProId(Long proId)throws ServiceException;

	

}
