package com.gettec.fsnip.fsn.service.erp.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.gettec.fsnip.fsn.dao.erp.CustomerAndProviderTypeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.CustomerAndProviderTypeService;

@Service("customerAndProviderTypeService")
public class CustomerAndProviderTypeServiceImpl extends BaseServiceImpl<CustomerAndProviderType, CustomerAndProviderTypeDAO>
		implements CustomerAndProviderTypeService{
	@Autowired private CustomerAndProviderTypeDAO customerAndProviderTypeDAO;

	@Override
	public CustomerAndProviderTypeDAO getDAO() {
		return customerAndProviderTypeDAO;
	}

	/**
	 * 获取客户自定义类型
	 * @param bid   客户企业id
	 * @param type  客户类型
	 * @param organization
	 * @return
	 * @throws DaoException 
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public CustomerAndProviderType findByBid(Long bid, Long type, Long organization) {
		return customerAndProviderTypeDAO.findByBid(bid,type,organization);
	}

}
