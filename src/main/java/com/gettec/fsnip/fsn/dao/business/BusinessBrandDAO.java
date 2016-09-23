package com.gettec.fsnip.fsn.dao.business;

import java.util.List;
import java.util.Map;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;

public interface BusinessBrandDAO extends BaseDAO<BusinessBrand>{

	public BusinessBrand findByName(String name);
	
	public long countByOrgnizationId(Long organizationId) throws DaoException;

	public List<BusinessBrand> getListByOrganization(Long organization) throws DaoException;
	
	public long countBrandByCondition(Map<String,Object> condition) throws DaoException;
	
	public List<BusinessBrand> getListOfBrandByConditionWithPage(
			Map<String,Object> condition, int page, int pageSize) throws DaoException;
	
	public List<BusinessBrand> getListByBusunitId(Long busunitId) throws DaoException;

	List<String> getAllBrandName() throws DaoException;

	public long countbyNameAndCategoryId(Long brandId, String name, Long id) throws DaoException;

	public long countByNameAndCategoryName(String name,
			String parentCategoryName, Long brandId) throws DaoException;

	public long countByNameAndOrgId(String brandName, Long organization) throws DaoException;

	public BusinessBrand findByNameAndCategoryId(String brandName,
			Long categoryId) throws DaoException;

	public BusinessBrand findByNameAndBussunitId(String brandName, Long bussunitId) throws DaoException;
	
	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public BusinessBrand checkBusinessBrand(BusinessBrand businessBrand) throws ServiceException;
}
