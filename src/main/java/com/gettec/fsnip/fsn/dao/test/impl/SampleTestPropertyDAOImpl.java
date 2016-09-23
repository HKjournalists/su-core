package com.gettec.fsnip.fsn.dao.test.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.test.SampleTestPropertyDAO;
import com.gettec.fsnip.fsn.model.test.SampleTestProperty;

/**
 * TestProperty customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value="sampleTestPropertyDAO")
public class SampleTestPropertyDAOImpl extends BaseDAOImpl<SampleTestProperty>
		implements SampleTestPropertyDAO {

}