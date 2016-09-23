package com.gettec.fsnip.fsn.dao.business;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.business.FieldValue;

public interface FieldValueDAO extends BaseDAO<FieldValue>{

	List<FieldValue> getListByBusunitIdAndFieldId(long id, long busunitId) throws DaoException;

	List<FieldValue> getListByBusunitId(long busunitId) throws DaoException;

	FieldValue findByBusunitIdAndFieldIdAndValue(long fieldId, long busunitId, String fieldValue) throws DaoException;

}