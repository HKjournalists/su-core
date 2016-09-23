package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.AddressDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.AddressInfo;
import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.erp.base.City;
import com.gettec.fsnip.fsn.model.erp.base.Province;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface AddressService extends BaseService<AddressInfo, AddressDAO> {
	public List<Province> findAllProvince();
	
	public List<City> findCityByProvinceId(String provinceId);
	
	public List<City> findAllCity();
	
	public List<Area> findAreaByCityId(String cityId);
	
	public List<Area> findAllArea();
	
	public List<City> getCityByProvId(String provId) throws ServiceException;

	List<Area> getAreaByCityId(String cityId) throws ServiceException;

	String getAreaCodeByAddress(String address) throws ServiceException;
	
	String getProJianChengByPro(String address) throws ServiceException;
}
