package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageType;

public interface OutStorageTypeDAO extends BaseDAO<OutStorageType> {

	public List<OutStorageType> getOutStorageTypefilter_(int from,int size,String configure);
	public long countOutStorageType(String configure);
}
