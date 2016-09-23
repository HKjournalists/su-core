package com.gettec.fsnip.fsn.dao.product;



import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
/**
 * ImportedProduct DAO
 * 
 * @author  longxianzhen 2015/05/22
 */
 public interface ImportedProductDAO extends BaseDAO<ImportedProduct>{

	 /**
	  * 根据产品id删除进口产品信息（假删除）
	  * @author longxianzhen 2015/05/27
	  */
	void deleteByProId(Long proId)throws DaoException;

	

	
}