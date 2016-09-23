package com.gettec.fsnip.fsn.web.controller.rest.report;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.erp.FromToBussinessService;
import com.gettec.fsnip.fsn.service.report.ReportService;
import com.gettec.fsnip.fsn.service.test.TestResultHandlerService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.report.ReviewReportOfSuperVO;
import com.gettec.fsnip.fsn.vo.report.ToBeStructuredReportVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/report")
public class ReportRESTService {
	@Autowired private ReportService reportService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private FromToBussinessService fromToBusService;
	@Autowired private TestResultHandlerService testResultHandlerService;
	
	/**
	 * 跟据产品id获取自检、送检、抽检报告
	 * @param id 产品ID
	 * @param type 报告类型：{"self","censor","sample"}
	 * @param date 日期：20130801
	 * @author Zhanghui 2015/4/9
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getReports/{isSearchAll}/{configure}/{page}/{pageSize}/{productId}/{fromBusStrId}/{testType}")
	public View getReports(
			@RequestParam(value="org",required=false) Long org,
			@PathVariable boolean isSearchAll,
			@PathVariable Long productId,
			@PathVariable String fromBusStrId,
			@PathVariable String testType,
			@PathVariable String configure,
			@PathVariable int page,
			@PathVariable int pageSize,
	        HttpServletRequest req,
	        HttpServletResponse resp, 
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = org==null?Long.parseLong(AccessUtils.getUserRealOrg().toString()):org;
			/* 验证产品id是否存在  */
			if(productId == null){
				if(productId == null){
					throw new Exception("产品不存在！");
				}
			}
			if(("self").equals(testType)){
				testType = "企业自检";
			}else if(("censor").equals(testType)){
				testType = "企业送检";
			}else if(("sample").equals(testType)){
				testType = "政府抽检";
			}else{
				throw new Exception("testType不合法");
			}
			
			/* 获取当前商超的企业id */
			Long toBusId = null;
			Long fromBusId = null;
			
			if(isSearchAll){
				if(org == null || org!=0){
					toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				}
				/**
				 * 此查询操作是来源于点击#query input条形码搜索后，查找相关检测报告
				 */
				fromBusId = fromToBusService.findFromBusId(productId, toBusId);
				
				// 判断是否只有一家供应商给当前商超供货
				/*long count = fromToBusService.count(productId, toBusId);
				
				if(count == 1){
					fromBusId = fromToBusService.findFromBusId(productId, toBusId);
				}else if(count > 1){
					List<String> dealers = fromToBusService.getListOfFromBusName(productId, toBusId);
					
					String names = "";
					for(String name : dealers){
						names += "【" + name + "】;";
					}
					model.addAttribute("dealer_names", names);
					throw new Exception("有多家供应商给当前商超供货！");
				}*/
			}else{
				if(fromBusStrId == null){
					throw new Exception("参数为空");
				}
				
				/**
				 * 此查询操作是来源于点击某个经销商的某个商品后，需要查找相关检测报告
				 */
				fromBusId = Long.parseLong(fromBusStrId);
			}
			
			Long from_bus_org = null; 
			
			// 获取经销商组织机构
			if(fromBusId != null){
				from_bus_org = businessUnitService.findOrgById(fromBusId);
			}
			
			/* 获取报告总数 */
			long total = reportService.countAllOfReports(productId, testType);
			model.addAttribute("counts", total);
			
			/* 获取报告 */
			List<ReviewReportOfSuperVO> reports = reportService.getListOfAllReportByPage(productId, from_bus_org, testType, page, pageSize);
			if(org != null || "0".equals(String.valueOf(org))){
				toBusId = null;
			}
			/* 获取待处理报告总数 */
			long totalOfOnHandle = reportService.countOfAllOnHandleReports(productId, testType, fromBusId, toBusId);
			
			model.addAttribute("data", reports);
			model.addAttribute("totalOfOnHandle", totalOfOnHandle);
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据用户名查询待结构化的报告
	 * @author LongXianZhen 2015/4/28
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getToBeStructured/{page}/{pageSize}/{configure}")
	public View getToBeStructuredByUser(
			@PathVariable int page,
			@PathVariable int pageSize,
			@PathVariable String configure,
	        HttpServletRequest req,
	        HttpServletResponse resp, 
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 获取报告总数 */
			Long total = testResultHandlerService.count(info.getUserName(), 1,configure);
			model.addAttribute("counts", total);
			/* 获取报告 */
			List<ToBeStructuredReportVO> reports = testResultHandlerService.getStructuredsByPage(info.getUserName(), 1, page, pageSize,configure);
			model.addAttribute("data", reports);
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据用户名查询已退回的结构化报告
	 * @author ZhangHui 2015/5/7
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getBackStructureds/{page}/{pageSize}/{configure}")
	public View getBackStructuredsByUser(
			@PathVariable int page,
			@PathVariable int pageSize,
			@PathVariable String configure,
	        HttpServletRequest req,
	        HttpServletResponse resp, 
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 获取报告总数 */
			Long total = testResultHandlerService.countOfBack(info.getUserName(),configure);
			model.addAttribute("counts", total);
			/* 获取报告 */
			List<ToBeStructuredReportVO> reports = testResultHandlerService.getBackStructuredsByPage(info.getUserName(), page, pageSize,configure);
			model.addAttribute("data", reports);
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 根据用户名查询已结构化的报告
	 * @author ZhangHui 2015/5/7
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getHasStructureds/{page}/{pageSize}/{configure}")
	public View getStructuredsByUser(
			@PathVariable int page,
			@PathVariable int pageSize,
			@PathVariable String configure,
	        HttpServletRequest req,
	        HttpServletResponse resp,
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 获取报告总数 */
			Long total = testResultHandlerService.countOfStructured(info.getUserName(),configure);
			model.addAttribute("counts", total);
			/* 获取报告 */
			List<ToBeStructuredReportVO> reports = testResultHandlerService.getHasStructuredsByPage(info.getUserName(), page, pageSize,configure);
			model.addAttribute("data", reports);
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取当前登录商超的， 总产品数、待处理报告数
	 * @author Zhanghui 2015/5/1
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getCountsOfReports")
	public View getCountsOfReports(
			@RequestParam(value="flag",required = false) boolean flag,
	        HttpServletRequest req,
	        HttpServletResponse resp, 
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			/* 获取当前登录的商超企业id */
    		Long toBusId = null;
    		if(!flag){
    			toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
		    }
    		
			// 总产品数
			long totalOfProduct = fromToBusService.counts(null, toBusId, false);
			
			// 待处理报告数
			Long totalOfOnHandReports = reportService.countOfOnHandleReports(null, null, toBusId, null);
			
			// 获取报告总数 
			Long totalOfReports = reportService.countOfReportsOfMyDealer(null, toBusId);
			
			model.addAttribute("totalOfProduct", totalOfProduct);
			model.addAttribute("totalOfOnHandReports", totalOfOnHandReports);
			model.addAttribute("totalOfReports", totalOfReports);
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：获取已知供应商给当前商超， 总报告数
	 * @author Zhanghui 2015/6/30
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getKnownDealerCountsOfReports/{fromBusId}")
	public View getKnownDealerCountsOfReports(
			@RequestParam(value="flag",required=false) boolean flag,
			@PathVariable Long fromBusId,
	        HttpServletRequest req,
	        HttpServletResponse resp,
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long toBusId = null;
			if(!flag){
			/* 获取当前登录的商超企业id */
    		  toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			}
    		
			// 获取报告总数 
			Long totalOfReports = reportService.countOfReportsOfMyDealer(fromBusId, toBusId);
			
			model.addAttribute("totalOfReports", totalOfReports);
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 处理之前待结构化的老数据
	 * @author LongXiZhen 2015/5/8
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/dealWithTheOldData")
	public View dealWithTheOldData(
	        HttpServletRequest req,
	        HttpServletResponse resp, 
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			testResultHandlerService.dealWithTheOldData();
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * 根据产品id获取当前登录商超的产品
	 * @author xuetaoyang 2016/8/19
	 */
	//@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getProductById")
	public View getProductById(
			//@RequestParam(value="flag",required = false) boolean flag,
			@RequestParam Long proId,
	        HttpServletRequest req,
	        HttpServletResponse resp, 
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			//AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			/* 获取当前登录的商超企业id */
    		Long toBusId = null;
    	//	if(!flag){
    			toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
		//    }
    		
			// 总产品数
			Product product = fromToBusService.getProbyId(null, toBusId, proId, false);
			
			if(product==null){
				model.addAttribute("product", null);
			}else{
				String name=product.getName();
				String format=product.getFormat();
				model.addAttribute("product", name);
				model.addAttribute("format", format);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		//model.addAttribute("result", resultVO);
		return JSON;
	}
}
