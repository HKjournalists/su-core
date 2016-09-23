package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.MarketToBusinessPKDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.MarketToBusiness;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface MarketToBusinessPKService extends BaseService<MarketToBusiness, MarketToBusinessPKDAO> {

	public   boolean saveBusiness(MarketToBusiness marBusiness, Long organization)throws ServiceException;

	public long getCountByLicenseAndOrg(String license, Long organization)throws ServiceException;

	public List<MarketToBusiness> getByOrganization(Long organization,
			String configure, int page, int pageSize)throws ServiceException;

	public long countByOrganization(Long organization, String configure)throws ServiceException;

	public long getCountBybusIdAndOrg(Long businessId, Long organization)throws ServiceException;
	
	public void save(BusinessUnit businessUnit)throws ServiceException;

	public MarketToBusiness updateBusiness(MarketToBusiness marBusiness,
			Long organization)throws ServiceException;
}
