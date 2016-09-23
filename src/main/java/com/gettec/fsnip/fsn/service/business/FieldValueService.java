package com.gettec.fsnip.fsn.service.business;

import java.util.List;

import com.gettec.fsnip.fsn.dao.business.FieldValueDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.FieldValue;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface FieldValueService extends BaseService<FieldValue, FieldValueDAO>{
	
	void save(List<FieldValue> fieldValues, long busunitId) throws ServiceException;

	List<FieldValue> getListByBusunitId(long id) throws ServiceException;
	
}