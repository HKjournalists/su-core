package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.StorageInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface StorageInfoService extends BaseService<StorageInfo, StorageInfoDAO> {

	public PagingSimpleModelVO<StorageInfo> getAllStorageInfo(Long organization);
	
	public StorageInfo findByName(String name,Long organization);
	
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<StorageInfo> getStorageInfofilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countStorageInfofilter(String configure);
	public List<StorageInfo> getStorageByProductIdOrInstanceId(Long productId,
			Long instanceId, Long organization);
	
	public boolean add(StorageInfo e, Long organization) throws ServiceException;
	
	public boolean update(StorageInfo e, Long organization) throws ServiceException;
	
	public void remove(StorageInfo e, Long organization) throws ServiceException;
	
	/*功能：判断仓库是否被使用
	 * author：cgw
	 * date：2014-10-31*/
	public boolean judgeIsUsed(StorageInfo storage,Long organization) throws ServiceException;

	public StorageInfo findById(String no) throws ServiceException;	
}
