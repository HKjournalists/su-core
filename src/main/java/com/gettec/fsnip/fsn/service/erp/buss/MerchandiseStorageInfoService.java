package com.gettec.fsnip.fsn.service.erp.buss;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseStorageInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.model.erp.buss.MerchandiseStorageInfo;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface MerchandiseStorageInfoService extends BaseService<MerchandiseStorageInfo, MerchandiseStorageInfoDAO>{

	boolean addStorage(MerchandiseInstance instance, int count, 
			StorageInfo target, String no, boolean log,String type, String userName, Long organization) throws ServiceException;
	boolean reduceStorage(MerchandiseInstance instance, int count, StorageInfo target, 
			String no, boolean log,String type, String userName,Long organization) throws ServiceException;
	boolean flittingStorage(MerchandiseInstance instance, int count, StorageInfo from, 
			StorageInfo to,  String no,String type, String userName,Long organization) throws ServiceException;
	
	PagingSimpleModelVO<MerchandiseStorageInfo> getPaging(int page, int pageSize, String configure, Long organization);
	
	/**
	 * 过滤
	 * @author Liang Zhou
	 * 2014-10-29
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException
	 */
	public PagingSimpleModelVO<MerchandiseStorageInfo> getMerchandiseStorageInfofilter(int page, int pageSize,String configure, Long organization)throws ServiceException;
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countMerchandiseStorageInfofilter(String configure);
	public List<Product> getProductByStorageInfo(Long organization) throws ServiceException;
	public List<MerchandiseInstance> getBatchNumByStorageInfo(
			Long productId, String storageId, Long organization);
	public List<StorageInfo> getStorageByProductIdOrInstanceId(Long productId,Long instanceId,
			Long organization);
	public MerchandiseStorageInfo getNumByStorage(String storageId, Long instanceId,
			Long organization) throws ServiceException;
	public void changeReserves(MerchandiseInstance merchandiseInstance, int count,
			StorageInfo firstStorage,String type) throws ServiceException;
	}
