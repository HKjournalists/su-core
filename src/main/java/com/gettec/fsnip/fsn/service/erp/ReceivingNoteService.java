package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface ReceivingNoteService extends BaseService<ReceivingNote, ReceivingNoteDAO> {

	public boolean checkReceivingnote(String re_num, String userName,
			Long organization);

	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	public PagingSimpleModelVO<ReceivingNote> getReceivingNotefilter(int page, int pageSize,
			String configure, Long organization, String userName) throws ServiceException;
	
	public Object getReceivingNoteCheckfilter(int page, int pageSize, String configure, 
			Long organization, String userName) throws ServiceException;

	public PagingSimpleModelVO<ReceivingNote> getPagingByUserName(int page, int size,
			String keywords, String userName, Long organization) throws ServiceException;

	ReceivingNote add(ReceivingNote receivingNote, Long organization) throws ServiceException;

	public boolean remove(ReceivingNote receivingNote);

	ReceivingNote updateReceivNote(ReceivingNote receivNote) throws ServiceException;

	PagingSimpleModelVO<ReceivingNote> getPagingSureReceivingnote(int page,
			int size, String keywords, Long organization) throws ServiceException;
}
