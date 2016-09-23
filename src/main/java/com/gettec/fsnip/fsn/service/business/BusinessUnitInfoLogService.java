package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitInfoLogDAO;
import com.gettec.fsnip.fsn.model.business.BusinessUnitInfoLog;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * BusinessUnitInfoLogService customized operation implementation
 * 
 * @author LongXianZhen 2015/06/03
 */
public interface BusinessUnitInfoLogService extends BaseService<BusinessUnitInfoLog, BusinessUnitInfoLogDAO>{

	void saveBusinessUnitInfoLog(BusinessUnitInfoLog data);

	
	
}