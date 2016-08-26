package com.gettec.fsnip.fsn.service.erp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.erp.AddressDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.AddressInfo;
import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.erp.base.City;
import com.gettec.fsnip.fsn.model.erp.base.Province;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.AddressService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("addressService")
public class AddressServiceImpl extends BaseServiceImpl<AddressInfo, AddressDAO> 
		implements AddressService {
	@Autowired private AddressDAO addressDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Province> findAllProvince() {
		return getDAO().findAllProvince();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<City> findCityByProvinceId(String provinceId) {
		return getDAO().findCityByProvinceId(provinceId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<City> findAllCity() {
		return getDAO().findAllCity();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Area> findAreaByCityId(String cityId) {
		return getDAO().findAreaByCityId(cityId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Area> findAllArea() {
		return getDAO().findAllArea();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<AddressInfo> getPaging(int page, int size,
			String keywords, Long organization) {
		try {
			PagingSimpleModelVO<AddressInfo> result = new PagingSimpleModelVO<AddressInfo>();
			String condition = " where e.organization=" + organization;
			if(keywords != null && keywords.trim()!="") {
				condition = condition + " and (e.province like '%" + keywords + "%' or e.city like '%" + keywords + "%' or e.area like '%" + keywords + "%' or e.other like '%" + keywords + "%') ";
			}
			Long count = getDAO().count(condition);
			result.setCount(count);
			result.setListOfModel(getDAO().getPaging(page, size, condition));
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 根据省id获取省下面的市区名称
	 * @param provId
	 * @return List<City>
	 * @author TangXin
	 */
	@Override
	public List<City> getCityByProvId(String provId) throws ServiceException{
		try{
			return getDAO().getCityByProvId(provId);
		}catch(DaoException e){
			throw new ServiceException("AddressServiceImpl.getCityByProvId()->"+e.getMessage(),e);
		}
	}

	/**
	 * 根据城市id获取城市下面的县级区域
	 * @param cityId
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public List<Area> getAreaByCityId(String cityId) throws ServiceException{
		try{
			return getDAO().getAreaByCityId(cityId);
		}catch(DaoException e){
			throw new ServiceException("AddressServiceImpl.getAreaByCityId()->"+e.getMessage(),e);
		}
	}
	
	/**
	 * 根据企业地址字符串，获取地址区域代码，兼容老数据
	 * @param address
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public String getAreaCodeByAddress(String address) throws ServiceException{
		try{
			/*解析地址，获得省市县数组*/
			String[] addrs = address.split("-");
			if(addrs.length!=3) return null;
			/*获取省级代码*/
			String provinceCode = getDAO().getProCodeByProAddr(addrs[0]);
			if(provinceCode==null) return null;
			/*获得市级代码*/
			String cityCode = getDAO().getCityCodeByProAddr(provinceCode, addrs[1]);
			if(cityCode==null) return null;
			/*获得县级代码*/
			String areaCode = getDAO().getAreaCodeByProAddr(cityCode, addrs[2]);
			return areaCode;
		}catch(DaoException e){
			throw new ServiceException("AddressServiceImpl.getAreaCodeByAddress()->"+e.getMessage(),e);
		}
	}
	
	@Override
	public AddressDAO getDAO() {
		return addressDAO;
	}
	/**
	 * 根据省份的名称获取该省的简称
     * @param province 省份的名称
     * @return String
     * @author HuangYog
	 */
    @Override
    public String getProJianChengByPro(String province) throws ServiceException {
        try {
            return addressDAO.getProJianChengByPro(province);
        } catch (DaoException e) {
            throw new ServiceException("AddressServiceImpl.getProJianChengByPro()->"+e.getMessage(),e);
        }
    }
	
}
