package com.gettec.fsnip.fsn.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.base.DistrictDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.District;
import com.gettec.fsnip.fsn.service.base.DistrictService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

@Service(value = "districtService")
public class DistrictServiceImpl extends BaseServiceImpl<District, DistrictDAO> implements DistrictService {

	@Autowired private DistrictDAO districtDAO;
	
	@Override
	public List<District> getListByDescription(String description)
			throws ServiceException {
		try{
			return districtDAO.getListByDescription(description);
		}catch(DaoException daoe){
			throw new ServiceException("DistrictServiceImpl.getListByDescription() " + daoe.getMessage(),daoe);
		}
	}

	@Override
	public DistrictDAO getDAO() {
		return districtDAO;
	}

}
