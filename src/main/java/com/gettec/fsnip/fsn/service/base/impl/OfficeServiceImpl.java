package com.gettec.fsnip.fsn.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.base.OfficeDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Office;
import com.gettec.fsnip.fsn.service.base.OfficeService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

@Service(value = "officeService")
public class OfficeServiceImpl extends BaseServiceImpl<Office, OfficeDAO> implements OfficeService {

	@Autowired private OfficeDAO officeDAO;
	
	/**
	 * 
	 */
	@Override
	public List<Office> getListByParnetId(Long parentId)
			throws ServiceException {
		try{
			List<Office> listOffice = officeDAO.getListByParentId(parentId);
			if(listOffice == null) return null;
			for(Office office : listOffice){
				if(office.getParentIds().length()<4) office.setHasChildren(true);
				else office.setHasChildren(false);
			}
			return listOffice;
		}catch(DaoException daoe){
			throw new ServiceException("OfficeServiceImpl.getListByParnetId() " + daoe.getMessage(),daoe);
		}
	}

	@Override
	public OfficeDAO getDAO() {
		return officeDAO;
	}

	
}
