package com.gettec.fsnip.fsn.dao.base;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.base.Country;

public interface CountryDAO extends BaseDAO<Country>{

	/**
     * 根据条形码前三位获取对应的国家
	 * @author longxianzhen 2015/05/22
     */
	Country getCountryByBar3(int bar3)throws DaoException;

}
