package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.OrderType;


public interface OrderTypeDAO extends BaseDAO<OrderType> {
	public List<OrderType> getOrdrTypeByModuleAndOrder(String module,String order, String configure);

	public long count(String belongModule, String belongOrder,
			Long organization, String noStart) throws DaoException;
}
