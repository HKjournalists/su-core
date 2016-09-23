package com.gettec.fsnip.fsn.dao.erp;


import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.base.BusinessType;

public interface BusinessTypeDAO extends BaseDAO<BusinessType> {
	public List<BusinessType> getBusinessTypefilter_(int from,int size,String configure);
	public long countBusinessType(String configure);
}
