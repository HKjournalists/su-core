package com.gettec.fsnip.fsn.service.product.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ImportedProductAgentsDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.ImportedProductAgents;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ImportedProductAgentsService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

/**
 * importedProductAgentsService service implementation
 * 
 * @author longxianzhen 2015/05/22
 */
@Service(value="importedProductAgentsService")
public class ImportedProductAgentsServiceImpl extends BaseServiceImpl<ImportedProductAgents, ImportedProductAgentsDAO> 
		implements ImportedProductAgentsService{
	

	@Autowired protected ImportedProductAgentsDAO importedProductAgentsDAO;

	
	@Override
	public ImportedProductAgentsDAO getDAO() {
		return importedProductAgentsDAO;
	}


	/**
	 * 保存进口食品国内代理商信息
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(ImportedProduct impProduct) throws ServiceException {
		try {
			String userName=AccessUtils.getUserName()!=null?AccessUtils.getUserName().toString():null;
			ImportedProductAgents impProductAgents=impProduct.getImportedProductAgents();
			impProductAgents.setImpProId(impProduct.getId());
			impProductAgents.setCreateDate(new Date());
			impProductAgents.setCreator(userName);
			importedProductAgentsDAO.persistent(impProductAgents);
		} catch (JPAException je) {
			throw new ServiceException("ImportedProductAgentsServiceImpl.save()-->"+je.getMessage(),je.getException());
		} catch (Exception e) {
			throw new ServiceException("ImportedProductAgentsServiceImpl.save()-->"+e.getMessage(),e);
		}
	}


	/**
	 * 根据进口食品id删除国内代理商
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteByImpProductId(Long impProId) throws ServiceException {
		try {
			importedProductAgentsDAO.deleteByImpProductId(impProId);
		} catch (DaoException e) {
			throw new ServiceException("ImportedProductAgentsServiceImpl.deleteByImpProductId()-->"+e.getMessage(),e.getException());
		}
		
	}


	/**
	 * 根据进口食品id查询国内代理商
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	public ImportedProductAgents findByImpProId(Long impProId)
			throws ServiceException {
		try {
			String condition=" WHERE e.impProId=?1 ";
			List<ImportedProductAgents> impProducts=getDAO().getListByCondition(condition, new Object[]{impProId});
			if(impProducts.size()>0){
				return impProducts.get(0);
			}
			return null;
		} catch (JPAException dex) {
			throw new ServiceException("ImportedProductAgentsServiceImpl.findByImpProId()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	
}
