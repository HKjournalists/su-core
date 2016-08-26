package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnitToLims;

public interface BusinessUnitToLimsService {
	
	BusinessUnitToLims save(BusinessUnitToLims busUnitLims) throws ServiceException;
}
