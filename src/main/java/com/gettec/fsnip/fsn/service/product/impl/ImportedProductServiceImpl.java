package com.gettec.fsnip.fsn.service.product.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ImportedProductDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.ImportedProductAgents;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ImportedProductAgentsService;
import com.gettec.fsnip.fsn.service.product.ImportedProductService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

/**
 * importedProductService service implementation
 * 
 * @author longxianzhen 2015/05/22
 */
@Service(value="importedProductService")
public class ImportedProductServiceImpl extends BaseServiceImpl<ImportedProduct, ImportedProductDAO> 
		implements ImportedProductService{
	

	@Autowired protected ImportedProductDAO importedProductDAO;
	@Autowired protected ImportedProductAgentsService importedProductAgentsService;
	@Autowired private ResourceService testResourceService;
	
	@Override
	public ImportedProductDAO getDAO() {
		return importedProductDAO;
	}


	/**
	 * 保存进口食品信息
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Product product) throws ServiceException {
		try {
			String userName=AccessUtils.getUserName()!=null?AccessUtils.getUserName().toString():null;
			ImportedProduct impProduct=product.getImportedProduct();
			impProduct.setProductId(product.getId());
			/* 2.当id为null时为新增 否则为更新 */
			if(impProduct.getId()==null||impProduct.getId().equals("")){  
				/**
				 * 新增
				 */
				impProduct.setCreateDate(new Date());
				impProduct.setCreator(userName);
				create(impProduct);
				//删除之前的代理商信息保存新的代理商
				importedProductAgentsService.deleteByImpProductId(impProduct.getId());
				importedProductAgentsService.save(impProduct);
			}else{  
				/**
				 * 更新
				 */
				ImportedProduct oldImpProduct=importedProductDAO.findById(impProduct.getId());
				oldImpProduct.setProductId(product.getId());
				oldImpProduct.setCountry(impProduct.getCountry());
				oldImpProduct.setLastModifyDate(new Date());
				oldImpProduct.setLastModifyUser(userName);
				
				/**
				 * 处理产品图片
				 * @author ZhangHui 2015/6/3
				 */
				Set<Resource> removes = testResourceService.getListOfRemoves(oldImpProduct.getLabelAttachments(), impProduct.getLabelAttachments());
				oldImpProduct.removeResources(removes);
				
				Set<Resource> adds = testResourceService.getListOfAdds(impProduct.getLabelAttachments());
				oldImpProduct.addResources(adds);
				
				// 执行更新操作
				update(oldImpProduct);
				
				//删除之前的代理商信息保存新的代理商
				importedProductAgentsService.deleteByImpProductId(impProduct.getId());
				importedProductAgentsService.save(impProduct);
			}
		} catch (JPAException e) {
			throw new ServiceException("ImportedProductServiceImpl.save()-->"+e.getMessage(),e.getException());
		} catch (Exception e) {
			throw new ServiceException("ImportedProductServiceImpl.save()-->"+e.getMessage(),e);
		}
		
	}

	/**
	 * 根据产品id查找进口产品信息
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	public ImportedProduct findByProductId(Long proIdd) throws ServiceException {
		try {
			String condition= "  WHERE e.productId=?1 AND e.del=?2 ";
			List<ImportedProduct> impProducts=getDAO().getListByCondition(condition, new Object[]{proIdd,false});
			ImportedProduct impProduct=null;
			if(impProducts!=null&&impProducts.size()>0){
				impProduct=impProducts.get(0);
				ImportedProductAgents importedProductAgents=importedProductAgentsService.findByImpProId(impProduct.getId());
				impProduct.setImportedProductAgents(importedProductAgents);
			}
			return impProduct;
		} catch (JPAException dex) {
			throw new ServiceException("ImportedProductServiceImpl.getproductList()-->" + dex.getMessage(), dex.getException());
		} 
	}

	/**
	 * 根据产品id删除进口产品信息（假删除）
	 * @author longxianzhen 2015/05/27
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteByProId(Long proId) throws ServiceException {
		try {
			importedProductDAO.deleteByProId(proId);
		} catch (DaoException dex) {
			throw new ServiceException("ImportedProductServiceImpl.deleteByProId()-->" + dex.getMessage(), dex.getException());
		}
	}
}
