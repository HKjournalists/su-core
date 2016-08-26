package com.gettec.fsnip.fsn.service.base;

import com.gettec.fsnip.fsn.dao.base.CountryDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Country;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface CountryService extends BaseService<Country, CountryDAO>{

	/**
     * 根据条形码前三位获取对应的国家
	 * @author longxianzhen 2015/05/22
     */
	Country getCountryByBar3(int bar3)throws ServiceException;

}
