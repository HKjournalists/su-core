package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.base.Unit;

public interface UnitDAO extends BaseDAO<Unit> {

	public long countUnit(String configure);
	
	public List<Unit> getPaging(int page, int size, String condition);
	
	public Unit findByBusunitName(String unitName) throws DaoException;
	
	List<Unit> findAll(Long organization, String configure);
	
	List<String> getAllUnitName() throws DaoException;
}
