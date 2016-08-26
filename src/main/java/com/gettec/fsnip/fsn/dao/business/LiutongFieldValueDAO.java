package com.gettec.fsnip.fsn.dao.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;

public interface LiutongFieldValueDAO extends BaseDAO<LiutongFieldValue> {
	
	List<Long> getResourceIds(Long producerId, String value, String disaply) throws DaoException;
}
