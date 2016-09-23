package com.gettec.fsnip.fsn.service.test;

import com.gettec.fsnip.fsn.dao.test.RiskAssessmentDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.test.RiskAssessment;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface RiskAssessmentService extends BaseService<RiskAssessment, RiskAssessmentDAO>{
	
	public Boolean  calculate(Product product,String userName ) throws ServiceException;

	public Long getRiskAssessmentId(Long propertyid) throws ServiceException;
	
	public boolean deletRiskAssess(Long reportId) throws ServiceException;
	
}