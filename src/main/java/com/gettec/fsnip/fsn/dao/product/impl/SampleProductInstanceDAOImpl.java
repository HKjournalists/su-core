package com.gettec.fsnip.fsn.dao.product.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.SampleProductInstanceDAO;
import com.gettec.fsnip.fsn.model.product.SampleProductInstance;

/**
 * ProductInstance customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value = "sampleProductInstanceDAO")
public class SampleProductInstanceDAOImpl extends
		BaseDAOImpl<SampleProductInstance> implements SampleProductInstanceDAO {
	
}
