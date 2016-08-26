package com.gettec.fsnip.fsn.service.test.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.test.SampleTestPropertyDAO;
import com.gettec.fsnip.fsn.model.test.SampleTestProperty;
import com.gettec.fsnip.fsn.service.common.impl.AbstractBaseServiceImpl;
import com.gettec.fsnip.fsn.service.test.SampleTestPropertyService;

/**
 * TestProperty service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="sampleTestPropertyService")
public class SampleTestPropertyServiceImpl extends AbstractBaseServiceImpl<SampleTestProperty, SampleTestPropertyDAO> 
		implements SampleTestPropertyService{
	@Autowired 
	protected SampleTestPropertyDAO sampleTestPropertyDAO;

	@Override
	public SampleTestPropertyDAO getDAO() {
		return sampleTestPropertyDAO;
	}
	

}