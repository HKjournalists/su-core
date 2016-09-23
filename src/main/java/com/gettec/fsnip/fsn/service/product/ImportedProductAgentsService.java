package com.gettec.fsnip.fsn.service.product;

import com.gettec.fsnip.fsn.dao.product.ImportedProductAgentsDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.ImportedProductAgents;
import com.gettec.fsnip.fsn.service.common.BaseService;
/**
 * 进口食品国内代理商 servise
 * @author jazhen  2015/05/27
 *
 */
public interface ImportedProductAgentsService extends
		BaseService<ImportedProductAgents, ImportedProductAgentsDAO> {

	/**
	 * 保存进口食品国内代理商信息
	 * @author longxianzhen 2015/05/27
	 */
	void save(ImportedProduct impProduct)throws ServiceException;

	/**
	 * 根据进口食品id删除国内代理商
	 * @author longxianzhen 2015/05/27
	 */
	void deleteByImpProductId(Long impProId)throws ServiceException;

	/**
	 * 根据进口食品id查询国内代理商
	 * @author longxianzhen 2015/05/27
	 */
	ImportedProductAgents findByImpProId(Long impProId)throws ServiceException;

	

}
