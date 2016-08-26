package com.gettec.fsnip.fsn.service.test.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.test.TestPropertyCategoryDAO;
import com.gettec.fsnip.fsn.model.test.TestPropertyCategory;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.test.TestPropertyCategoryService;

/**
 * TestPropertyCategory service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="testPropertyCategoryService")
public class TestPropertyCategoryServiceImpl extends BaseServiceImpl<TestPropertyCategory, TestPropertyCategoryDAO> 
		implements TestPropertyCategoryService{
	@Autowired protected TestPropertyCategoryDAO testPropertyCategoryDAO;
	
	@Override
	public TestPropertyCategoryDAO getDAO() {
		return testPropertyCategoryDAO;
	}
}