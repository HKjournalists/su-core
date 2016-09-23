package com.gettec.fsnip.fsn.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.SampleProductDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.SampleProduct;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.SampleProductService;


/**
 * Product service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="sampleProductService")
public class SampleProductServiceImpl extends BaseServiceImpl<SampleProduct, SampleProductDAO> 
		implements SampleProductService{
	
	@Autowired 
	private SampleProductDAO sampleProductDAO;

	
	public SampleProductDAO getDAO() {
		return sampleProductDAO;
	}


	@Override
	@Transactional
	public SampleProduct checkSampleProduct(SampleProduct sampleProduct)
			throws ServiceException {
		
		return sampleProductDAO.checkSampleProduct(sampleProduct);
	}
	
}