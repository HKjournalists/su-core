package com.gettec.fsnip.fsn.test.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.test.common.MockJunit;
import com.lhfs.fsn.service.testReport.TestReportService;

public class TestReportServiceTest extends MockJunit{
	@Autowired private TestReportService testReportService;

	@Test(expected = Exception.class)
	public void test() throws ServiceException{
		boolean b = testReportService.validatHaveProJpg(62577L);
		System.out.println(b);
	}
}
