package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteToPurchaseorderInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfoPK;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ReceivingNoteToPurchaseorderInfoService 
		extends BaseService<ReceivingNoteToContactinfo, ReceivingNoteToPurchaseorderInfoDAO>{
	List<ReceivingNoteToContactinfo> getListByNo(String no) throws ServiceException;

	ReceivingNoteToContactinfo findByPk(ReceivingNoteToContactinfoPK pk) throws ServiceException;
}
