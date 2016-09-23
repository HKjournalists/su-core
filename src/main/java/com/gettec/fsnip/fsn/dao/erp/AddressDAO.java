package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.AddressInfo;
import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.erp.base.City;
import com.gettec.fsnip.fsn.model.erp.base.Province;

public interface AddressDAO extends BaseDAO<AddressInfo> {

	public List<Province> findAllProvince();
	
	public List<City> findCityByProvinceId(String provinceId);
	
	public List<City> findAllCity();
	
	public List<Area> findAreaByCityId(String cityId);
	
	public List<Area> findAllArea();
	
	public List<City> getCityByProvId(String provId) throws DaoException;

	List<Area> getAreaByCityId(String cityId) throws DaoException;

	String getProCodeByProAddr(String province) throws DaoException;

	String getCityCodeByProAddr(String provinceCode, String city)
			throws DaoException;

	String getAreaCodeByProAddr(String cityCode, String area)
			throws DaoException;
	/**
	 * 根据省份的名称获取该省的简称
	 * @param province 省份的名称
	 * @return String
	 * @throws DaoException
	 * @author HuangYog
	 */
	String getProJianChengByPro(String province)throws DaoException;
}
