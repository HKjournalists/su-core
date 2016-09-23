package com.gettec.fsnip.fsn.dao.business;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.Field;

public interface FieldDAO extends BaseDAO<Field>{

	Field findByName(String name) throws DaoException;
	
}