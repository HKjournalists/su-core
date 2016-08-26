package com.gettec.fsnip.fsn.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitToLimsDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnitToLims;
import com.gettec.fsnip.fsn.service.business.BusinessUnitToLimsService;

@Service(value="businessUnitToLimsService")
public class BusinessUnitToLimsServiceImpl implements BusinessUnitToLimsService{

	@Autowired private BusinessUnitToLimsDAO businessUnitToLimsDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessUnitToLims save(BusinessUnitToLims busUnitLims) throws ServiceException {
		try{
			if(busUnitLims==null){return null;}
			if(busUnitLims.getId()!=null){
				businessUnitToLimsDao.merge(busUnitLims);
			}else{
				businessUnitToLimsDao.persistent(busUnitLims);
			}
			return busUnitLims;
		}catch(JPAException jpae){
			throw new ServiceException("BusinessUnitToLimsServiceImpl.save() "+jpae.getMessage(),jpae.getException());
		}
	}

}
