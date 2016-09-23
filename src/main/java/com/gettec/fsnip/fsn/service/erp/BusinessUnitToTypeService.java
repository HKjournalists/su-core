package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.BusinessUnitToTypeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;

public interface BusinessUnitToTypeService extends BaseService<BusinessUnitToType, BusinessUnitToTypeDAO> {
	/*
	 * 添加企业与企业类型的关联信息
	 * author：cgw
	 * 2014-10-23
	 * */
	public void save(BusinessUnitToType buToType) throws ServiceException;
	public BusinessUnitToType findByBusIdAndOrgIdAndType(Long busunitId, Long organization,Long type);
	
	ResultVO saveType(BusinessUnit customer, Long organization) throws ServiceException;
}
