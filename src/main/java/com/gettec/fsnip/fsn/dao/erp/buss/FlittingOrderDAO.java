package com.gettec.fsnip.fsn.dao.erp.buss;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.buss.FlittingOrder;

public interface FlittingOrderDAO extends BaseDAO<FlittingOrder> {
	public String findNoMaxByNoStart(String noStart) throws DaoException;
}
