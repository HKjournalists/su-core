package com.gettec.fsnip.fsn.service.erp.buss;

import com.gettec.fsnip.fsn.dao.erp.buss.InStorageRecordDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageRecord;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.BusinessOrderVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface InStorageRecordService extends BaseService<InStorageRecord, InStorageRecordDAO> {
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<InStorageRecord> getInStorageRecordfilter(int page, int pageSize,
			String configure, Long organization) throws ServiceException;
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countInStorageRecordfilter(String configure) throws ServiceException;

	public Object addBusinessOrder(BusinessOrderVO vo,
			Long currentUserOrganization, String userName) throws ServiceException;
}
