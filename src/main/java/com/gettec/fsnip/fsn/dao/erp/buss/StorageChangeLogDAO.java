package com.gettec.fsnip.fsn.dao.erp.buss;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.erp.buss.StorageChangeLog;

public interface StorageChangeLogDAO extends BaseDAO<StorageChangeLog> {

	public List<StorageChangeLog> getStorageChangeLogfilter_(int from,int size,String configure);
	public long countStorageChangeLog(String configure);
}
