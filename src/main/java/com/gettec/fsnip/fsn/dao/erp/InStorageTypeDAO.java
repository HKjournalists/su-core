package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageType;

public interface InStorageTypeDAO extends BaseDAO<InStorageType> {

	public List<InStorageType> getInStorageTypefilter_(int from,int size,String configure);
	public long countInStorageType(String configure);
}
