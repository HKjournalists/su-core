package com.gettec.fsnip.fsn.dao.test.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.AbstractModelDAOImpl;
import com.gettec.fsnip.fsn.dao.test.SampleTestResultDAO;
import com.gettec.fsnip.fsn.model.test.SampleTestResult;

/**
 * TestResult customized operation implementation
 * 
 * @author Ryan Wang
 */
@Repository(value = "sampleTestResultDAO")
public class SampleTestResultDAOImpl extends AbstractModelDAOImpl<SampleTestResult>
		implements SampleTestResultDAO {

}