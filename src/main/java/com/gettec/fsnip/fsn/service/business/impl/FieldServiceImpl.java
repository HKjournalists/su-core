package com.gettec.fsnip.fsn.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.business.FieldDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.Field;
import com.gettec.fsnip.fsn.service.business.FieldService;

/**
 * @author Hui Zhang
 */
@Service(value="fieldService")
public class FieldServiceImpl implements FieldService{
	@Autowired private FieldDAO fieldDAO;

	@Override
	public Field findByFieldId(Long id) throws ServiceException {
		try {
			return fieldDAO.findById(id);
		} catch (JPAException e) {
			throw new ServiceException("【service-error】按id查找一条field信息，出现异常", e);
		}
	}

	@Override
	public Field findByFieldName(String fieldName) throws ServiceException{
		try{
			return fieldDAO.findByName(fieldName);
		}catch(DaoException e){
			throw new ServiceException("【service-error】按fieldName查找一条field信息，出现异常", e.getException());
		}
	}
	
}