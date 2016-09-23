package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.CustomerAndProviderTypeDAO;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface CustomerAndProviderTypeService extends BaseService<CustomerAndProviderType, CustomerAndProviderTypeDAO>{
	/*
	 * 根据企业id获取对应的类型
	 * author:cgw
	 * date:2014-10-23*/
	public CustomerAndProviderType findByBid(Long bid,Long type,Long organization);
}
