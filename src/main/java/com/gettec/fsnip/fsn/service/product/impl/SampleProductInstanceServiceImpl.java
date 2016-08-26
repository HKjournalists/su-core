package com.gettec.fsnip.fsn.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.product.SampleProductInstanceDAO;
import com.gettec.fsnip.fsn.model.product.SampleProductInstance;
import com.gettec.fsnip.fsn.service.common.impl.AbstractBaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.SampleProductInstanceService;

/**
 * ProductInstance service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="sampleProductInstanceService")
public class SampleProductInstanceServiceImpl extends AbstractBaseServiceImpl<SampleProductInstance, SampleProductInstanceDAO> 
		implements SampleProductInstanceService{
	
	@Autowired 
	protected SampleProductInstanceDAO sampleProductInstanceDAO;

	@Override
	public SampleProductInstanceDAO getDAO() {
		return sampleProductInstanceDAO;
	}
	

}
