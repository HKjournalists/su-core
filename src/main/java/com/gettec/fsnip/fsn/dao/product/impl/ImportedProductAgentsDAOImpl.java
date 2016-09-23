package com.gettec.fsnip.fsn.dao.product.impl;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ImportedProductAgentsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ImportedProductAgents;

/**
 * importedProductAgentsDAO customized operation implementation
 * 
 * @author longxianzhen 2015/05/22
 */
@Repository(value="importedProductAgentsDAO")
public class ImportedProductAgentsDAOImpl extends BaseDAOImpl<ImportedProductAgents>
		implements ImportedProductAgentsDAO {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 根据进口食品id删除国内代理商
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	public void deleteByImpProductId(Long impProId) throws DaoException {
		try {
			String sql="DELETE FROM imported_product_domestic_agents  WHERE imp_pro_id=?1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter(1, impProId);
			query.executeUpdate();
		} catch (Exception e) {
	        throw new DaoException("ImportedProductAgentsDAOImpl.deleteByImpProductId()-->"+e.getMessage(),e);
	    }
	}

	
	
}