package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.Field;

public interface FieldService{

	Field findByFieldId(Long id) throws ServiceException;

	Field findByFieldName(String fieldName) throws ServiceException;
	
}