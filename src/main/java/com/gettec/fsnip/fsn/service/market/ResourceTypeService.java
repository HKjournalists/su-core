package com.gettec.fsnip.fsn.service.market;

import java.util.List;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.ResourceType;


public interface ResourceTypeService {
	
	ResourceType findByName(String name) throws ServiceException;

	List<ResourceType> getResourceType() throws ServiceException;

	ResourceType findById(long id) throws ServiceException;
	
}
