package com.gettec.fsnip.fsn.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.business.BusinessNavigationDao;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.model.business.BusinessNavigation;
import com.gettec.fsnip.fsn.service.business.BusinessNavigationService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

@Service(value = "businessNavigationService")
public class BusinessNavigationServiceImpl extends BaseServiceImpl<BusinessNavigation, BusinessNavigationDao> 
implements BusinessNavigationService{

	@Autowired
	private BusinessNavigationDao businessNavigationDao;
	
	@Override
	public BusinessNavigationDao getDAO() {
		return businessNavigationDao;
	}

	@Override
	public List<BusinessNavigation> getNavigationList(Long businessID) {
		
		return businessNavigationDao.getNavigationList(businessID);
	}
	
	
	
	
	
	
	
	
	

}
