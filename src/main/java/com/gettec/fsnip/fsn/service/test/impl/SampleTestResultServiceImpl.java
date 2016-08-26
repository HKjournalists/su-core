package com.gettec.fsnip.fsn.service.test.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.test.SampleTestResultDAO;
import com.gettec.fsnip.fsn.model.test.SampleTestResult;
import com.gettec.fsnip.fsn.service.common.impl.AbstractBaseServiceImpl;
import com.gettec.fsnip.fsn.service.test.SampleTestResultService;

/**
 * TestResult service implementation
 * @author Ryan Wang
 */
@Service(value = "sampleTestResultService")
public class SampleTestResultServiceImpl extends AbstractBaseServiceImpl<SampleTestResult, SampleTestResultDAO> 
		implements SampleTestResultService {
	@Autowired 
	protected SampleTestResultDAO sampleTestResultDAO;

	@Override
	public SampleTestResultDAO getDAO() {
		return sampleTestResultDAO;
	}
	
}