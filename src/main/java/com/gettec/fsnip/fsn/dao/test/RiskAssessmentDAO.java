package com.gettec.fsnip.fsn.dao.test;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.test.RiskAssessment;
/**
 * @author ZhaWanNeng
 */
 public interface RiskAssessmentDAO extends BaseDAO<RiskAssessment>{

	
	 public Long getRiskAssessmentId(Long propertyid) throws DaoException;
	
}