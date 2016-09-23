package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;

public interface ProviderTypeDAO extends BaseDAO<CustomerAndProviderType> {

	public List<CustomerAndProviderType> getProviderTypefilter_(int from,int size,String configure);
	public long countProviderType(String configure);
	
	public CustomerAndProviderType findByUniqueNameAndOrganization(String name, Long organization);
}
