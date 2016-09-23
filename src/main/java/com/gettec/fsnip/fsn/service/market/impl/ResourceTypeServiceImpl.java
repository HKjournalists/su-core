package com.gettec.fsnip.fsn.service.market.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.market.ResourceTypeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.ResourceType;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;

@Service(value="resourceTypeService")
public class ResourceTypeServiceImpl implements ResourceTypeService{
	@Autowired 
	ResourceTypeDAO resourceTypeDAO;

	/**
	 * 按类别名称查找一条资源类别信息
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ResourceType findByName(String name) throws ServiceException{
		try {
			return resourceTypeDAO.findByName(name);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<ResourceType> getResourceType() throws ServiceException {
		try {
			return resourceTypeDAO.findAll();
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 按id查找一条资源类型
	 * @throws ServiceException 
	 */
	@Override
	public ResourceType findById(long id) throws ServiceException{
		try {
			return resourceTypeDAO.findById(id);
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		}
	}
}