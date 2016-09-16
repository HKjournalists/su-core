package com.gettec.fsnip.fsn.web.controller.rest.test;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.service.test.impl.RiskAssessmentServiceImpl;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.TestResultSearchCriteria;
import com.gettec.fsnip.fsn.vo.report.ReportBackVO;
import com.gettec.fsnip.fsn.vo.report.StructuredReportOfTestlabVO;
import com.gettec.fsnip.fsn.web.controller.JSONPropertyEditor;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.service.testReport.TestReportService;

import net.sf.json.JSONObject;

 /**
 * TestResult REST Service API
 * @author Ryan Wang
 */
@Controller
@RequestMapping("/test")
public class TestResultRESTService extends BaseRESTService{
	@Autowired private TestReportService testReportService;
	@Autowired private TestResultService testResultService;
	@Autowired private TestPropertyService testPropertyService;
	@Autowired private MkCategoryService mkCategoryService; // 一级，二级产品分类
	@Autowired private RiskAssessmentServiceImpl riskAssessmentService;
	@Autowired private UpdataReportService updataReportService;
	@Autowired private StatisticsClient staClient;
	
	@RequestMapping(method = RequestMethod.GET, value = "test-result/{id}")
	public TestResult get(@PathVariable Long id) {
		try {
			TestResult testResult = testResultService.findByTestResultId(id);
			if(testResult.getSample()!=null && testResult.getSample().getProduct()!=null && 
			        testResult.getSample().getProduct().getCategory()!=null && 
			        testResult.getSample().getProduct().getCategory().getCategory() !=null) {
			    
			    String fristCategoryCode = testResult.getSample().getProduct().getCategory().getCategory().getCode();
			    //保存一级菜单
			    fristCategoryCode = fristCategoryCode.substring(0,2);
			    ProductCategory fristCategory = mkCategoryService.findCategoryByCode(fristCategoryCode);
			    testResult.getSample().getProduct().getCategory().getCategory().setFristCategory(fristCategory);
			}
			List<TestProperty> testPropertyList = testPropertyService.findByReportId(testResult.getId());
			if(testPropertyList!=null){
				testResult.setTestProperties(testPropertyList);			
			}
			return testResult;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "test-result")
	public RESTResult<TestResult> update(@RequestParam("model") TestResult testResult) {
		try {
			RESTResult<TestResult> result = null;
			testResultService.update(testResult);
			result = new RESTResult<TestResult>(RESTResult.SUCCESS, testResult);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST, value = "test-results2")
	public Map search(@RequestParam("criteria") TestResultSearchCriteria criteria) {
		try {
			List<TestResult> testResults = testResultService.findTestResults(criteria);
			long totalCount=testResultService.getCount(criteria);
			Map map=new HashedMap();
			map.put("testResultList", testResults);
			map.put("totalCount", totalCount);
			return map;
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST, value = "test-resultsByThird")
	public Map testResultsByThird(@RequestParam("criteria") TestResultSearchCriteria criteria) {
		try {
			List<TestResult> testResults = testResultService.findTestResultsByThird(criteria);
			long totalCount=testResultService.getThirdCount(criteria);
			Map map=new HashedMap();
			map.put("testResultList", testResults);
			map.put("totalCount", totalCount);
			return map;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取需审核的结构化报告集合
	 * @author ZhangHui 2015/5/6
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST, value = "/test-results/getStructureds")
	public Map searchStructureds(
			@RequestParam("criteria") TestResultSearchCriteria criteria) {
		try {
			List<StructuredReportOfTestlabVO> testResults = testResultService.findTestResultsOfStructureds(criteria);
			long totalCount = testResultService.getCountOfStructureds(criteria);
			Map map = new HashedMap();
			map.put("testResultList", testResults);
			map.put("totalCount", totalCount);
			return map;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * testlab审核报告通过,发布至portal
	 * @author ZhangHui 2015/5/7
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/publish/{id}")
	public View publish(
			@PathVariable("id") Long id, 
			HttpServletRequest req, 
	        HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			testResultService.publishStructured(id);
			
			TestResult testResult = testResultService.findById(id);
			/* 更新报告申请表中的相关产品记录的状态  改为“已处理” */
			updataReportService.changeApplyReportStatus(testResult.getSample().getProduct().getBarcode(), testResult.getTestType(), 2);
			/* 向protal推送相关的报告更新消息 */
			updataReportService.sendMessageToPortal(testResult.getSample().getProduct().getBarcode(), testResult.getTestType());
			/* 计算产品的风险指数 */
			Product product = testResult.getSample().getProduct();
			String userName=AccessUtils.getUserName().toString();
			riskAssessmentService.calculate(product,userName);
			//记录报告日志
			ReportFlowLog rfl=new ReportFlowLog(testResult.getId(),info.getUserName(),testResult.getSample().getProduct().getBarcode()
					,testResult.getSample().getBatchSerialNo(),testResult.getServiceOrder(),"完成 testlab审核报告通过,发布至portal操作");
			staClient.offer(rfl);//记录报告日志为异步处理
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * teslab审核退回结构化报告
	 * @author ZhangHui 2015/5/6
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/sendBack/structured/{test_result_id}/{isSendBackToGYS}")
	public View sendBackStructured(
				@PathVariable("test_result_id") Long test_result_id,
				@PathVariable("isSendBackToGYS") boolean isSendBackToGYS, 
				@RequestBody ReportBackVO reportBackVO,
				HttpServletRequest req, 
		        HttpServletResponse resp,
				Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			reportBackVO.setReturnMes(URLDecoder.decode(reportBackVO.getReturnMes(), "UTF-8"));
			testResultService.sendBackStructured(test_result_id, reportBackVO, isSendBackToGYS);
			
			//记录报告日志
			TestResult testResult=testReportService.findById(test_result_id);
			ReportFlowLog rfl=new ReportFlowLog(testResult.getId(),info.getUserName(),testResult.getSample().getProduct().getBarcode()
					,testResult.getSample().getBatchSerialNo(),testResult.getServiceOrder(),"完成 teslab审核退回结构化报告操作 退回原因："+reportBackVO.getReturnMes());
			staClient.offer(rfl);//记录报告日志为异步处理
		} catch(Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * testlab审核报告从portal撤回
	 * 最后更新：zhanghui 2015/5/6
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/backout/{test_result_id}")
	public View goBack(
			@PathVariable("test_result_id") Long test_result_id, 
			HttpServletRequest req, 
	        HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			// 报告退回
			testResultService.goBackFromPortal(test_result_id);
			// 计算风险指数
			TestResult testResult = testReportService.findById(test_result_id);
			Product product = testResult.getSample().getProduct();
			String userName=AccessUtils.getUserName().toString();
			riskAssessmentService.calculate(product,userName);
			
			//记录报告日志
			ReportFlowLog rfl=new ReportFlowLog(testResult.getId(),info.getUserName(),testResult.getSample().getProduct().getBarcode()
					,testResult.getSample().getBatchSerialNo(),testResult.getServiceOrder(),"完成 testlab审核报告从portal撤回操作");
			staClient.offer(rfl);//记录报告日志为异步处理
		} catch(Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	@RequestMapping(method = RequestMethod.DELETE, value = "test-result/{id}")
	public RESTResult<TestResult> delte(@PathVariable("id") Long id) {
		try {
			RESTResult<TestResult> result = null;
			TestResult testResult = testResultService.findById(id);
			testResultService.delete(id);
			result = new RESTResult<TestResult>(RESTResult.SUCCESS, testResult);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/{id}")
	public View removeTest(@PathVariable Long id,@RequestParam("returnMes") String returnMes,
			HttpServletRequest req, 
	        HttpServletResponse resp,
			@RequestParam("returnURL") String httpURL,@RequestParam("from") String from,Model model){
		//RESTResult<TestResult> result = null;
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			String userName=AccessUtils.getUserName().toString();
			TestResult testResult = testResultService.findById(id);
			//result = new RESTResult<TestResult>(RESTResult.SUCCESS, null);
			String reportNO=testResult.getServiceOrder();
			String sampleNO=testResult.getSampleNO();
			Long sampleId=testResult.getLimsSampleId();
			String backURL=testResult.getBackLimsURL();
			try {
				returnMes=URLEncoder.encode(returnMes, "UTF-8");
				reportNO=URLEncoder.encode(reportNO,"UTF-8");
				sampleNO=URLEncoder.encode(sampleNO,"UTF-8");
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
			String url = "";
			String param="";
			if(backURL!=null&&!backURL.equals("")&&sampleId!=null){
				url=backURL;
				param="returnMes="+ returnMes+"&reportNO="+reportNO+"&userName="+userName+"&sampleNO="+sampleNO+"&sampleId="+sampleId; 
			}else{
				url=httpURL;
				param="returnMes="+ returnMes+"&reportNO="+reportNO+"&userName="+userName+"&sampleNO="+sampleNO; 
			}
            String fsnResult = HttpUtils.send(url, "GET",param);
            JSONObject returnResult=JSONObject.fromObject(fsnResult);
	        String resuFlag=returnResult.getString("status");
			if(resuFlag.equals("true")){
				try{			
					if(testResult!=null){
						//记录报告日志
						ReportFlowLog rfl=new ReportFlowLog(testResult.getId(),info.getUserName(),testResult.getSample().getProduct().getBarcode()
								,testResult.getSample().getBatchSerialNo(),testResult.getServiceOrder(),"完成 testlab退回LIMS报告操作 退回原因："+returnMes);
						staClient.offer(rfl);//记录报告日志为异步处理
						
						testResultService.deleteById(testResult.getId());
					}
				}catch(ServiceException e){
					resultVO.setErrorMessage(e.getMessage());
					resultVO.setStatus(SERVER_STATUS_FAILED);
					resultVO.setSuccess(false);
					e.printStackTrace();
					
				}
			}else{
				resultVO.setMessage("lims返回失败");
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
			}
		} catch (Exception e) {
			resultVO.setErrorMessage("退回失败"+e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * testlab重新计算风险指数
	 * @param id 需要重新计算风险指数的报告id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "calculate/{id}")
	public RESTResult<TestResult> calculate(@PathVariable("id") Long id) {
		RESTResult<TestResult> result = new RESTResult<TestResult>();
		try {
			String userName=AccessUtils.getUserName().toString();
			TestResult testResult = testResultService.findById(id);
			Product product = testResult.getSample().getProduct();
			Boolean calculate = riskAssessmentService.calculate(product,userName);
			if(calculate){
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
			return result;
		} catch (ServiceException sex) {
			result.setSuccess(false); 
			return result;
		}
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
		binder.registerCustomEditor(TestResult.class, new JSONPropertyEditor(TestResult.class));
		binder.registerCustomEditor(TestResultSearchCriteria.class, new JSONPropertyEditor(TestResultSearchCriteria.class));
	}
}