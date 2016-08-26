package com.gettec.fsnip.fsn.dao.product;



import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ImportedProductAgents;
/**
 * ImportedProductAgents DAO
 * 
 * @author  longxianzhen 2015/05/22
 */
 public interface ImportedProductAgentsDAO extends BaseDAO<ImportedProductAgents>{

	 /**
	  * 根据进口食品id删除国内代理商
	  * @author longxianzhen 2015/05/27
	  */
	void deleteByImpProductId(Long impProId)throws DaoException;

	

	
}