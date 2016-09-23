package com.gettec.fsnip.fsn.web.controller.rest.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;

 /**
 * TestProperty REST Service API
 * 
 * @author Ryan Wang
 */
@Controller
@RequestMapping("/test")
public class TestPropertyRESTService extends BaseRESTService{
	private final static Logger logger = Logger.getLogger(TestPropertyRESTService.class);
	@Autowired protected TestPropertyService testPropertyService;
	
	@RequestMapping(method = RequestMethod.GET, value = "test-property/{id}")
	public TestProperty get(@PathVariable Long id) {
		try {
			TestProperty testProperty = testPropertyService.findById(id);
			return testProperty;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "test-property")
	public RESTResult<TestProperty> create(@RequestParam("model") TestProperty testProperty) {
		try {
			RESTResult<TestProperty> result = null;
			testPropertyService.create(testProperty);
			result = new RESTResult<TestProperty>(RESTResult.SUCCESS, testProperty);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "test-property")
	public RESTResult<TestProperty> update(@RequestParam("model") TestProperty testProperty) {
		try {
			RESTResult<TestProperty> result = null;
			testPropertyService.update(testProperty);
			result = new RESTResult<TestProperty>(RESTResult.SUCCESS, testProperty);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "test-property/{id}")
	public RESTResult<TestProperty> delte(@PathVariable("id") Long id) {
		try {
			RESTResult<TestProperty> result = null;
			TestProperty testProperty = testPropertyService.findById(id);
			testPropertyService.delete(id);
			result = new RESTResult<TestProperty>(RESTResult.SUCCESS, testProperty);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
}
