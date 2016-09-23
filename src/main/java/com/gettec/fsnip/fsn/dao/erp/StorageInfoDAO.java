package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;

public interface StorageInfoDAO extends BaseDAO<StorageInfo> {

	public List<StorageInfo> getStorageInfofilter_(int from,int size,String configure);
	public long countStorageInfo(String configure);
	
	public StorageInfo findByName(String name,Long organization);
	
	public List<StorageInfo> getStorageByProductIdOrInstanceId(Long productId,Long instanceId,Long organization);
	
	public List<StorageInfo> getStoragefilter(String filter,String name,String fieldName);
	public String findLastNoByOrg(Long organization) throws DaoException;
}
