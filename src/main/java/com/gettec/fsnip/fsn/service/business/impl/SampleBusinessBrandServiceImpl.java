package com.gettec.fsnip.fsn.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.SampleBusinessBrandDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.gettec.fsnip.fsn.service.business.SampleBusinessBrandService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;

/**
 * BusinessBrand service implementation
 * 
 * @author Ryan Wang
 */
@Transactional
@Service(value="sampleBusinessBrandService")
public class SampleBusinessBrandServiceImpl extends BaseServiceImpl<SampleBusinessBrand, SampleBusinessBrandDAO> 
		implements SampleBusinessBrandService{

	@Autowired 
	private SampleBusinessBrandDAO sampleBusinessBrandDAO;
	
	
	@Override
	public SampleBusinessBrandDAO getDAO() {
		return sampleBusinessBrandDAO;
	}
	
	@Override
	public SampleBusinessBrand checkBusinessBrand(
			SampleBusinessBrand sampleBusinessBrand) throws ServiceException {
		return sampleBusinessBrandDAO.checkSampleBusinessBrand(sampleBusinessBrand);
	}


}