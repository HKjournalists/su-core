package com.gettec.fsnip.fsn.dao.test.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.TestPropertyCategoryDAO;
import com.gettec.fsnip.fsn.model.test.TestPropertyCategory;

/**
 * TestPropertyCategory customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="testPropertyCategoryDAO")
public class TestPropertyCategoryDAOImpl extends BaseDAOImpl<TestPropertyCategory>
		implements TestPropertyCategoryDAO {
		
}