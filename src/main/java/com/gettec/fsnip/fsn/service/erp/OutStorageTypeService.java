package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.OutStorageTypeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

public interface OutStorageTypeService extends BaseService<OutStorageType, OutStorageTypeDAO> {

	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<OutStorageType> getOutStorageTypefilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countOutStorageTypefilter(String configure);
	
	/*功能：判断出库类型是否被使用
	 * author：cgw
	 * date：2014-10-31*/
	public boolean judgeIsUsed(Long id,Long organization) throws ServiceException;

	public boolean updateOutStorageType(SimpleModelVO vo, Long organization) throws ServiceException;

	public boolean add(SimpleModelVO vo, Long currentUserOrganization) throws ServiceException;

	void remove(Long id, Long organization) throws ServiceException;
}
