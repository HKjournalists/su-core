package com.gettec.fsnip.fsn.service.erp.buss;

import com.gettec.fsnip.fsn.dao.erp.buss.StorageChangeLogDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.buss.StorageChangeLog;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface StorageChangeLogService extends BaseService<StorageChangeLog, StorageChangeLogDAO> {

	void log(MerchandiseInstance instance, String storage1, String storage2, int count,
			String type, String no, String userName, Long organization) throws ServiceException;
	PagingSimpleModelVO<StorageChangeLog> getStorageLogs(int page,
			int pageSize, String keywords, Long organization) throws ServiceException;
	
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<StorageChangeLog> getStorageChangeLogfilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countStorageChangeLogfilter(String configure);
}
