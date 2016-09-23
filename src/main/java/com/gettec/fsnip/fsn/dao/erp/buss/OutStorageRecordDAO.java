package com.gettec.fsnip.fsn.dao.erp.buss;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageRecord;

public interface OutStorageRecordDAO extends BaseDAO<OutStorageRecord> {

	public List<OutStorageRecord> getOutStorageRecordfilter_(int from,int size,String configure)throws DaoException;
	public long countOutStorageRecord(String configure)throws DaoException;
	String findNoMaxByNoStart(String noStart) throws DaoException;
}
