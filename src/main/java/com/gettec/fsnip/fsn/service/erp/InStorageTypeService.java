package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.InStorageTypeDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

public interface InStorageTypeService extends BaseService<InStorageType, InStorageTypeDAO> {

	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<InStorageType> getInStorageTypefilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countInStorageTypefilter(String configure);
	
	public boolean judgeIsUsed(Long id,Long organization) throws ServiceException;

	public boolean updateInStorageType(SimpleModelVO vo, Long organization) throws ServiceException;

	public boolean add(SimpleModelVO vo, Long currentUserOrganization) throws ServiceException;

	void remove(Long id, Long organization) throws ServiceException;
}
