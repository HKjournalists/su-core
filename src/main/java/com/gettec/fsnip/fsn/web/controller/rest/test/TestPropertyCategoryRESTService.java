package com.gettec.fsnip.fsn.web.controller.rest.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.test.TestPropertyCategory;
import com.gettec.fsnip.fsn.service.test.TestPropertyCategoryService;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;

 /**
 * TestPropertyCategory REST Service API
 * 
 * @author Ryan Wang
 */
@Controller
@RequestMapping("/test")
public class TestPropertyCategoryRESTService extends BaseRESTService{
	private final static Logger logger = Logger.getLogger(TestPropertyCategoryRESTService.class);
	@Autowired protected TestPropertyCategoryService testPropertyCategoryService;
	
	@RequestMapping(method = RequestMethod.GET, value = "test-property-category/{id}")
	public TestPropertyCategory get(@PathVariable Long id) {
		try {
			TestPropertyCategory testPropertyCategory = testPropertyCategoryService.findById(id);
			return testPropertyCategory;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "test-property-category")
	public RESTResult<TestPropertyCategory> create(@RequestParam("model") TestPropertyCategory testPropertyCategory) {
		try {
			RESTResult<TestPropertyCategory> result = null;
			testPropertyCategoryService.create(testPropertyCategory);
			result = new RESTResult<TestPropertyCategory>(RESTResult.SUCCESS, testPropertyCategory);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "test-property-category")
	public RESTResult<TestPropertyCategory> update(@RequestParam("model") TestPropertyCategory testPropertyCategory) {
		try {
			RESTResult<TestPropertyCategory> result = null;
			testPropertyCategoryService.update(testPropertyCategory);
			result = new RESTResult<TestPropertyCategory>(RESTResult.SUCCESS, testPropertyCategory);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "test-property-category/{id}")
	public RESTResult<TestPropertyCategory> delte(@PathVariable("id") Long id) {
		try {
			RESTResult<TestPropertyCategory> result = null;
			TestPropertyCategory testPropertyCategory = testPropertyCategoryService.findById(id);
			testPropertyCategoryService.delete(id);
			result = new RESTResult<TestPropertyCategory>(RESTResult.SUCCESS, testPropertyCategory);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
}
