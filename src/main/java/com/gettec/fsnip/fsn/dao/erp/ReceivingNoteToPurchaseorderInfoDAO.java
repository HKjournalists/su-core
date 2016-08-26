package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNoteToContactinfo;

public interface ReceivingNoteToPurchaseorderInfoDAO extends BaseDAO<ReceivingNoteToContactinfo> {
	List<ReceivingNoteToContactinfo> getListByNo(String no) throws DaoException;
}
