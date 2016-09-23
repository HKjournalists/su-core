package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.ProviderTypeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

public interface ProviderTypeService extends BaseService<CustomerAndProviderType, ProviderTypeDAO> {

	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<CustomerAndProviderType> getProviderTypefilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countProviderTypefilter(String configure);
	
	public PagingSimpleModelVO<CustomerAndProviderType> getPaging(int page, int size, String keywords, Long organization) throws ServiceException;
	
	public PagingSimpleModelVO<CustomerAndProviderType> getAll(Long organization) throws ServiceException;
	
	/*功能：判断供货商类型是否被使用
	 * author：cgw
	 * date：2014-10-31*/
	public boolean judgeIsUsed(Long id,Long organization) throws ServiceException;	

	public boolean updateProviderType(SimpleModelVO vo, Long organization) throws ServiceException;

	void remove(Long id, Long organization) throws ServiceException;
}
