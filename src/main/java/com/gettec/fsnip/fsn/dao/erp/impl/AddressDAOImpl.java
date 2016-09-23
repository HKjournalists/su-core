package com.gettec.fsnip.fsn.dao.erp.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.erp.AddressDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.AddressInfo;
import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.erp.base.City;
import com.gettec.fsnip.fsn.model.erp.base.Province;

@Repository("addressDAO")
public class AddressDAOImpl extends BaseDAOImpl<AddressInfo> implements AddressDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<Province> findAllProvince() {
		String sql = "SELECT id,provinceId,province,short_prov FROM HAT_PROVINCE";
		List<Object[]> object = entityManager.createNativeQuery(sql)
				.getResultList();
		List<Province> pros = new ArrayList<Province>();
		if(object != null) {
			for (Object[] obj2 : object) {
				Province province = new Province();
				province.setId(Integer.parseInt(obj2[0].toString()));
				province.setProvinceId(obj2[1].toString());
				province.setProvince(obj2[2].toString());
				province.setShortProv(obj2[3].toString());
				pros.add(province);
			}
		}
		return pros;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<City> findCityByProvinceId(String provinceId) {
		String sql = "SELECT id,cityId,city FROM HAT_CITY WHERE FATHER = :provinceId";
		List<Object[]> object = entityManager.createNativeQuery(sql)
				.setParameter("provinceId", provinceId)
				.getResultList();
		List<City> cities = new ArrayList<City>();
		if(object != null) {
			for (Object[] obj2 : object) {
				City city = new City();
				city.setId(Integer.parseInt(obj2[0].toString()));
				city.setCityId(obj2[1].toString());
				city.setCity(obj2[2].toString());
				cities.add(city);
			}
		}
		return cities;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findAreaByCityId(String cityId) {
		String sql = "SELECT id,areaId,area FROM HAT_AREA WHERE FATHER = :cityId";
		List<Object[]> object = entityManager.createNativeQuery(sql)
				.setParameter("cityId", cityId)
				.getResultList();
		List<Area> areas = new ArrayList<Area>();
		if(object != null) {
			for (Object[] obj2 : object) {
				Area area = new Area();
				area.setId(Integer.parseInt(obj2[0].toString()));
				area.setAreaId(obj2[1].toString());
				area.setArea(obj2[2].toString());
				areas.add(area);
			}
		}
		return areas;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findAllArea() {
		String sql = "SELECT id,areaId,area FROM HAT_AREA";
		List<Object[]> object = entityManager.createNativeQuery(sql)
				.getResultList();
		List<Area> areas = new ArrayList<Area>();
		if(object != null) {
			for (Object[] obj2 : object) {
				Area area = new Area();
				area.setId(Integer.parseInt(obj2[0].toString()));
				area.setAreaId(obj2[1].toString());
				area.setArea(obj2[2].toString());
				area.setCityId(obj2[3].toString());
				areas.add(area);
			}
		}
		return areas;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<City> findAllCity() {
		String sql = "SELECT id,cityId,city FROM HAT_CITY";
		List<Object[]> object = entityManager.createNativeQuery(sql)
				.getResultList();
		List<City> cities = new ArrayList<City>();
		if(object != null) {
			for (Object[] obj2 : object) {
				City city = new City();
				city.setId(Integer.parseInt(obj2[0].toString()));
				city.setCityId(obj2[1].toString());
				city.setCity(obj2[2].toString());
				city.setProvinceId(obj2[3].toString());
				cities.add(city);
			}
		}
		return cities;
	}

	/**
	 * 根据省id获取省下面的市区名称
	 * @param provId
	 * @return List<City>
	 * @throws DaoException
	 * @author TangXin
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<City> getCityByProvId(String provId) throws DaoException{
		try{
			String sql="select id,cityID,city from hat_city where provinceId = ?1";
			List<Object[]> Objects = entityManager.createNativeQuery(sql)
										.setParameter(1, provId)
										.getResultList();
			List<City> cities = new ArrayList<City>();
			if(Objects!=null&&Objects.size()>0){
				for(Object[] objs:Objects){
					City city = new City();
					city.setId(Integer.parseInt(objs[0].toString()));
					city.setCityId(objs[1].toString());
					city.setCity(objs[2].toString());
					city.setProvinceId(provId);
					cities.add(city);
				}
			}
			return cities;
		}catch(Exception e){
			throw new DaoException("AddressDAOImpl.getCityByProvId()->"+e.getMessage(),e);
		}
	}
	
	/**
	 * 根据城市id获取城市下面的县级区域
	 * @param cityId
	 * @return List<Area>
	 * @throws DaoException
	 * @author TangXin
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getAreaByCityId(String cityId) throws DaoException{
		try{
			String sql="select id,areaID,area from hat_area where cityId = ?1";
			List<Object[]> Objects = entityManager.createNativeQuery(sql)
										.setParameter(1, cityId)
										.getResultList();
			List<Area> areas = new ArrayList<Area>();
			if(Objects!=null&&Objects.size()>0){
				for(Object[] objs:Objects){
					Area area = new Area();
					area.setId(Integer.parseInt(objs[0].toString()));
					area.setAreaId(objs[1].toString());
					area.setArea(objs[2].toString());
					area.setCityId(cityId);
					areas.add(area);
				}
			}
			return areas;
		}catch(Exception e){
			throw new DaoException("AddressDAOImpl.getAreaByCityId()->"+e.getMessage(),e);
		}
	}
	
	/**
	 * 根据省级名称city，获取省级代码
	 * @param province
	 * @return
	 * @throws DaoException
	 * @author TangXin
	 */
	@Override
	public String getProCodeByProAddr(String province) throws DaoException{
		try{
			String sql="SELECT provinceID FROM hat_province WHERE province LIKE ?1";
			List<Object> proAddrs = this.getListBySQLWithoutType(null, sql, new Object[]{"%"+province+"%"});
			if(proAddrs!=null&&proAddrs.size()==1){
				return proAddrs.get(0).toString();
			}
			return null;
		}catch(Exception e){
			throw new DaoException("AddressDAOImpl.getProCodeByProAddr()->"+e.getMessage(),e);
		}
	}
	
	/**
	 * 根据市级名称city 和省级代码 provinceCode ，获取市级代码
	 * @param provinceCode
	 * @param city
	 * @return
	 * @throws DaoException
	 * @author TangXin
	 */
	@Override
	public String getCityCodeByProAddr(String provinceCode, String city) throws DaoException{
		try{
			String sql="SELECT cityID FROM hat_city WHERE provinceId=?1 AND city LIKE ?2";
			List<Object> proAddrs = this.getListBySQLWithoutType(null, sql, new Object[]{provinceCode,"%"+city+"%"});
			if(proAddrs!=null&&proAddrs.size()==1){
				return proAddrs.get(0).toString();
			}
			return null;
		}catch(Exception e){
			throw new DaoException("AddressDAOImpl.getCityCodeByProAddr()->"+e.getMessage(),e);
		}
	}
	
	/**
	 * 根据县区名称area 和市级代码 cityCode ，获取县区代码
	 * @param cityCode
	 * @param area
	 * @return
	 * @throws DaoException
	 * @author TangXin
	 */
	@Override
	public String getAreaCodeByProAddr(String cityCode, String area) throws DaoException{
		try{
			String sql="SELECT areaID FROM hat_area WHERE cityId= ?1 AND area LIKE ?2";
			List<Object> proAddrs = this.getListBySQLWithoutType(null, sql, new Object[]{cityCode,"%"+area+"%"});
			if(proAddrs!=null&&proAddrs.size()==1){
				return proAddrs.get(0).toString();
			}
			return null;
		}catch(Exception e){
			throw new DaoException("AddressDAOImpl.getCityCodeByProAddr()->"+e.getMessage(),e);
		}
	}

	/**
     * 根据省份的名称获取该省的简称
     * @param province 省份的名称
     * @return String
     * @author HuangYog
     */
    @Override
    public String getProJianChengByPro(String province) throws DaoException {
        try{
            String sql="SELECT short_prov FROM hat_province WHERE province LIKE ?1";
            List<Object> proJianCheng = this.getListBySQLWithoutType(null, sql, new Object[]{"%"+province+"%"});
            if(proJianCheng!=null&&proJianCheng.size()==1){
                return proJianCheng.get(0).toString();
            }
            return null;
        }catch(Exception e){
            throw new DaoException("AddressDAOImpl.getProJianChengByPro()->"+e.getMessage(),e);
        }
    }
    
}
