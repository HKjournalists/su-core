package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;


public interface CustomerTypeDAO extends BaseDAO<CustomerAndProviderType> {
	public List<CustomerAndProviderType> getCustomerTypefilter_(int from,int size,String configure);
	public long countCtype(String configure);
	
	public CustomerAndProviderType findByUniqueNameAndOrganization(String name, Long organization);
	
	CustomerAndProviderType findByCustomerAndOrganization(Long organization, Integer type, String name) throws DaoException;
}
