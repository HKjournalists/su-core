package com.gettec.fsnip.fsn.dao.erp;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToType;

public interface BusinessUnitToTypeDAO extends BaseDAO<BusinessUnitToType>{

	BusinessUnitToType findByBusIdAndOrgId(Long busunitId, Long organization,Long type);
	/*
	 *功能： 更新客户、供应商自定义类型
	 *author：cgw
	 *date：2014-11-5*/
	public void updateBusinessUnitTypeByBidAndOid(Long busunitId,Long typeId, Long organization,Long type);

	void removeByBussidAndType(Long bussid, Long organization, Long type) throws DaoException;
	
	BusinessUnitToType findByBusIdAndOrgId(Long busunitId, Long organization);
	
	void updateBusinessUnitType(Long busunitId, Long typeId, Long organization, Long type);
	
	void createBusinessUnitTypeBySql(Long busunitId, Long typeId, Long organization, Long type);
}
