package com.gettec.fsnip.fsn.dao.erp;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;

public interface ReceivingNoteDAO extends BaseDAO<ReceivingNote> {
	boolean checkReceivingnote(String re_num);
	
	String findNoMaxByNoStart(String noStart) throws DaoException;

	void removeByNo(String no) throws DaoException;

	long countByStorageUsed(String name, Long organization)throws DaoException;
}
