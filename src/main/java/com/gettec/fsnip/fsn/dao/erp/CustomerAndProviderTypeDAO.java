package com.gettec.fsnip.fsn.dao.erp;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;

public interface CustomerAndProviderTypeDAO extends BaseDAO<CustomerAndProviderType> {
	/*
	 * 根据企业id获取对应的类型
	 * author:cgw
	 * date:2014-10-23*/
	public CustomerAndProviderType findByBid(Long bid,Long type,Long organization);
}
