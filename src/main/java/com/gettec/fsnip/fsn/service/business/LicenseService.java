package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.LicenseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;


public interface LicenseService extends BaseService<LicenseInfo, LicenseDAO>{

	LicenseInfo save(LicenseInfo licenseInfo) throws ServiceException;

	LicenseInfo findByLic(String string);

	/**
	 * 功能描述：保存营业执照号
	 * @author ZhangHui 2015/6/5
	 * @throws ServiceException 
	 */
	public LicenseInfo save(String licenseNo) throws ServiceException;

}