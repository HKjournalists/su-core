package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.LiquorSalesLicenseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LiquorSalesLicenseInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;



public interface LiquorSalesLicenseService extends BaseService<LiquorSalesLicenseInfo, LiquorSalesLicenseDAO>{

	void save(LiquorSalesLicenseInfo liquorSalesLicense) throws ServiceException;

}