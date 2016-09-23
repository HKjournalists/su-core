package com.gettec.fsnip.fsn.service.business;

import java.util.List;
import com.gettec.fsnip.fsn.dao.business.BusinessBrandDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

public interface BusinessBrandService extends BaseService<BusinessBrand, BusinessBrandDAO>{
	public BusinessBrand findById(Object id);
	public BusinessBrand findByName(String name);
	public long countByOrgnizationId(Long organizationId, String configure) throws ServiceException;

	public List<BusinessBrand> getListOfBrandByOrgnizationIdWithPage(
			Long organization, String configure, int page, int pageSize) throws ServiceException;

	public void createBusinessBrand(BusinessBrand businessBrand, AuthenticateInfo info) throws ServiceException;

	public List<BusinessBrand> getListOfBrandByOrganization(Long organization) throws ServiceException;

	public void updateBusinessBrand(BusinessBrand orig_brand) throws ServiceException;
	
	public void save(List<BusinessBrand> brands, Long busunitId) throws ServiceException;
	
	public List<String> getAllBrandName() throws ServiceException;

	boolean validateBrandName(BusinessBrand businessBrand)
			throws ServiceException;

	public boolean validateBrandName(String brandName,
			Long organization) throws ServiceException;

	public BusinessBrand findByNameAndCategoryId(String name, Long id) throws ServiceException;

	public BusinessBrand findByNameAndBussunitId(String name, Long bussunitId) throws ServiceException;

	/**
	 * 功能描述：保存产品的品牌信息
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	public BusinessBrand saveBrand(BusinessBrand businessBrand, BusinessUnit orig_producer, Long organization) 
			throws ServiceException;

	void saveBrand(ProductInstance sample, Long organization)
			throws ServiceException;

	/**
	 * 功能描述：保存商标信息<br>
	 * 作用于保存泊银等其他外部系统的样品数据。
	 * @author ZhangHui 2015/4/24
	 */
	void saveBusinessBrand(ProductInstance sample, Long organization)
			throws ServiceException;
	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public BusinessBrand checkBusinessBrand(BusinessBrand businessBrand) throws ServiceException;


}
