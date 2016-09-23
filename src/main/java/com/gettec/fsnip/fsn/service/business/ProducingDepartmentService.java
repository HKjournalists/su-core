package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.ProducingDepartmentDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.ProducingDepartment;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProducingDepartmentService  extends BaseService<ProducingDepartment, ProducingDepartmentDAO>{

	void save(BusinessUnit businessUnit, String step) throws ServiceException;

}