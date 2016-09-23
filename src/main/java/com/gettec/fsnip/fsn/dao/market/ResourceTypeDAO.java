package com.gettec.fsnip.fsn.dao.market;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.market.ResourceType;


public interface ResourceTypeDAO  extends BaseDAO<ResourceType> {
	
	public ResourceType findByName(String name) throws DaoException;

}
