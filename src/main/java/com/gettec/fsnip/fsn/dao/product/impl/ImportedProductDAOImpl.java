package com.gettec.fsnip.fsn.dao.product.impl;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ImportedProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;

/**
 * importedProductDAO customized operation implementation
 * 
 * @author longxianzhen 2015/05/22
 */
@Repository(value="importedProductDAO")
public class ImportedProductDAOImpl extends BaseDAOImpl<ImportedProduct>
		implements ImportedProductDAO {
	@PersistenceContext
	private EntityManager entityManager;

	
	/**
	  * 根据产品id删除进口产品信息（假删除）
	  * @author longxianzhen 2015/05/27
	  */
	@Override
	public void deleteByProId(Long proId) throws DaoException {
		try{
			String sql = " UPDATE imported_product e SET e.del=1 WHERE e.product_id = ?1 ";
			Query query=entityManager.createNativeQuery(sql);
			query.setParameter(1, proId);
			query.executeUpdate();
		}catch(Exception jpae){
			throw new DaoException("ImportedProductDAOImpl.deleteByProId()->"+jpae.getMessage(),jpae);
		}
	}

	
	
}