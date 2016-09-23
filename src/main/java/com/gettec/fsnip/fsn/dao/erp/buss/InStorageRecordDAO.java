package com.gettec.fsnip.fsn.dao.erp.buss;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageRecord;

public interface InStorageRecordDAO extends BaseDAO<InStorageRecord>{

	public List<InStorageRecord> getInStorageRecordfilter_(int from,int size,String configure) throws DaoException;
	public long countInStorageRecord(String configure) throws DaoException;
	public String findNoMaxByNoStart(String noStart) throws DaoException;
}
