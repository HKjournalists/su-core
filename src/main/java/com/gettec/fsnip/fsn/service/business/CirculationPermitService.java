package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.CirculationPermitDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.CirculationPermitInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface CirculationPermitService extends BaseService<CirculationPermitInfo, CirculationPermitDAO>{

	CirculationPermitInfo findByDistributionNo(String distributionNo) throws ServiceException;

	void save(CirculationPermitInfo distribution, boolean isNew) throws ServiceException;

	void save(CirculationPermitInfo circulationPermitInfo) throws ServiceException;
	
}