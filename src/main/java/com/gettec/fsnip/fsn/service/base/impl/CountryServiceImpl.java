package com.gettec.fsnip.fsn.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.base.CountryDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Country;
import com.gettec.fsnip.fsn.service.base.CountryService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
/**
 * countryService service implementation
 * @author longxianzhen 2015/05/22
 */
@Service(value = "countryService")
public class CountryServiceImpl extends BaseServiceImpl<Country, CountryDAO> implements CountryService {

	@Autowired private CountryDAO countryDAO;
	


	@Override
	public CountryDAO getDAO() {
		return countryDAO;
	}

	/**
     * 根据条形码前三位获取对应的国家
	 * @author longxianzhen 2015/05/22
     */
	@Override
	public Country getCountryByBar3(int bar3) throws ServiceException {
		try {
			return countryDAO.getCountryByBar3(bar3);
		} catch (DaoException de) {
			throw new ServiceException("CountryServiceImpl-->getCountryByBar3()"+de.getMessage(),de.getException());
		}
	}

	
}
