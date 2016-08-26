package com.gettec.fsnip.fsn.web.controller.rest.market;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_REPORT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_REPORT_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.MkTempReport;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.test.ImportedProductTestResult;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.market.FtpService;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.MkTempReportService;
import com.gettec.fsnip.fsn.service.market.MkTestTemplateService;
import com.gettec.fsnip.fsn.service.market.ResourceTypeService;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.product.ImportedProductService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.service.test.ImportedProductTestResultService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultHandlerService;
import com.gettec.fsnip.fsn.util.MKReportNOUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.ReadPdfUtil;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.report.ReportBackVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.service.testReport.TestReportService;

@Controller
@RequestMapping("/testReport")
public class MkTestReportRESTService {
	@Autowired private TestReportService testReportService;
	@Autowired private MkTempReportService  tempReportService;
	@Autowired private MkCategoryService categoryService;
	@Autowired private ProductCategoryInfoService productCategoryInfoService;
	@Autowired private TestPropertyService testPropertyService;
	@Autowired private MkTestTemplateService templateService;
	@Autowired private FtpService ftpService;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	@Autowired private ProductService productService;
	@Autowired private ResourceTypeService resourceTypeService;
	@Autowired private UpdataReportService updataReportService;
	@Autowired private TestResultHandlerService testResultHandlerService;
	@Autowired private ImportedProductTestResultService importedProductTestResultService;
	@Autowired private StatisticsClient staClient;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ImportedProductService importedProductService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static Logger log = Logger.getLogger(MkTestReportRESTService.class.getName());
	
	/**
	 * 按报告id查找一条报告信息
	 * @param view 
	 * 			true 代表当前为预览报告操作
	 * @author ZhangHui 2015/6/4
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public View getTestReport(
			@PathVariable Long id,
			@RequestParam(value="view", required=false) String view,
			HttpServletRequest req,
			HttpServletResponse resp, 
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			if(id == null){
				throw new Exception("参数为空");
			}
			
			/* 1.获取报告基本信息 */
			TestResult orig_report = testReportService.findById(id);
			ReportOfMarketVO report_vo = new ReportOfMarketVO(orig_report);
//			MkTempReport report  = tempReportService.getTempReportOrderNo(report_vo.getServiceOrder());
//			if(report!=null){
//				report_vo.setTestee(report.getTesteeName());
//				report_vo.setTestOrgnization(report.getTestOrgnizName());
//				report_vo.setTestPlace(report.getTestPlace());
//				report_vo.setSamplingLocation(report.getSamplePlace());
//				report_vo.setSampleQuantity(report.getSampleCount());
//				report_vo.setStandard(report.getJudgeStandard());
//				report_vo.setResult(report.getTestResultDescribe());
//				report_vo.setComment(report.getRemark());
//				SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd" );
//				report_vo.setTestDateStr(sdf.format(report.getTestDate()));
//				
//			}
			/* 2.如果是进口产品，则获取进口食品报告信息 */
			ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			if(product_vo == null){
				throw new Exception("报告的产品信息为空");
			}
			
			/* 查找该产品进口食品信息 */
			ImportedProduct impPro=importedProductService.findByProductId(product_vo.getId());
			product_vo.setImportedProduct(impPro);
			//if(report_vo.getProduct_vo().getProductType() == 2){ // 产品类型为2 代表是进口产品
			/**
			 * 由于之前录过很多进口产品，它们的产品类型为1，暂时所有报告都去查询进口食品报告信息
			 * @author longxianzhen 2015/06/09
			 */
			ImportedProductTestResult impTest=importedProductTestResultService.findByReportId(report_vo.getId());
			if(impTest!=null){
				report_vo.setImpProTestResult(impTest);
			}
			//}
			
			/* 3.获取检测项目列表信息 */
			if(view==null || !"true".equals(view)){
				report_vo.setTestProperties(testPropertyService.findByReportId(report_vo.getId()));
			}
			
			/* 3.获取qs许可证号 */
			/*List<BusinessUnitOfReportVO> pro2Buss = productTobusinessUnitService.getListByBarcode(
					orig_report.getSample().getProduct().getBarcode());
			
			if(pro2Buss!=null && pro2Buss.size() > 0){
				// 该产品对应的已经绑定qs号的生产企业信息（前台用户从可选生产企业中选择一条后，加载生产企业详细信息使用）
				report_vo.setPro2Bus(pro2Buss);
				
				// 返回前台所有该产品绑定的qs号，用于用户下拉选择
				List<Map<Object,String>> listOfBusunitName = templateService.getBusNamesFromPro2Bus(pro2Buss);
				report_vo.setListOfBusunitName(listOfBusunitName);
			}
			
			BusinessUnit producerOfReport = orig_report.getSample().getProducer();
			BusinessUnitOfReportVO bus_vo = templateService.getPro2BusFromList(producerOfReport.getName(), pro2Buss);
			if(bus_vo != null){
				report_vo.setBus_vo(bus_vo);
			}*/
			
			/* 4. 判断产品是否是否已经被生产企业认领 */
			Product product = orig_report.getSample().getProduct();
			if(product.getOrganization()!=null && !product.getOrganization().equals(0L)){
				if(product.getProducer()!=null && product.getOrganization().equals(product.getProducer().getOrganization())){
					product_vo.setCan_edit_pro(false);
				}
			}
			
			model.addAttribute("data", report_vo);
		} catch(Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 按当前用户的组织机构或用户姓名，获取报告列表。
	 * @param page
	 * @param pageSize
	 * @param pubFlag
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping( method = RequestMethod.GET , value = "/byuser/{page}/{pageSize}/{pubFlag}/{isPubUser}")
	public View getTestReportListByUser(@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="pubFlag") char pubFlag, 
			@PathVariable(value="isPubUser") boolean isPubUser, HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 1 获取当前登录用户的组织机构id */
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total;
			List<TestResult> listOfReport;
			if(isPubUser){
				/* 2.1 当前登录用户是发布人员时 */
				total = testReportService.countByOrgIdAndUserRealName(currentUserOrganization, null, pubFlag, null);
				listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
						null, page, pageSize, pubFlag, null);
			}else{
				/* 2.2 当前登录用户是报告录入人员时 */
				String userName = info.getUserName();
				total = testReportService.countByOrgIdAndUserRealName(currentUserOrganization, userName, pubFlag, null);
				listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
						userName, page, pageSize, pubFlag, null);
			}
			Map map = new HashMap();
			map.put("listOfReport", listOfReport);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 按当前用户的组织机构或用户姓名，获取报告列表（只有商超供应商使用）。
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping( method = RequestMethod.GET , value = "/byuserFromSC/{page}/{pageSize}/{pubFlag}/{configure}")
	public View getTestReportListByUserFromSC(@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="pubFlag") char pubFlag, 
			@PathVariable(value="configure") String configure,
			HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 1 获取当前登录用户的组织机构id */
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total;
			List<TestResult> listOfReport;

			/* 2 当前登录用户是报告录入人员时 */
			String userName = info.getUserName();
			total = testReportService.countByOrgIdAndUserRealName(currentUserOrganization, userName, pubFlag, configure);
			listOfReport = testReportService.getReportsByOrgIdAndUserNameFromSCWithPage(currentUserOrganization,
					userName, page, pageSize, pubFlag, configure);	
			Map map = new HashMap();
			map.put("listOfReport", listOfReport);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 按当前用户的组织机构名称和其他过滤条件，获取报告列表。
	 * @param page
	 * @param pageSize
	 * @param pubFlag
	 * @param configure
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping( method = RequestMethod.GET , value = "/byuser/{page}/{pageSize}/{pubFlag}/{configure}/{isPubUser}")
	public View getTestReportListByConfigure(@PathVariable(value="page")int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="pubFlag") char pubFlag,
			@PathVariable(value="configure") String configure, @PathVariable(value="isPubUser") boolean isPubUser,
			HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			/* 1 获取当前登录用户的组织机构id */
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total;
			List<TestResult> listOfReport;
			configure = configure.replaceAll("\\\\0gan0\\\\", "/"); // 搜索条件可以包括特殊字符斜杠'/'
			configure = configure.replaceAll("%","\\\\%");
			if(isPubUser){
				/* 2.1 当前登录用户是发布人员时 */
				total = testReportService.countByOrgIdAndUserRealName(currentUserOrganization, null, pubFlag, configure);
				listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
						null, page, pageSize, pubFlag, configure);
			}else{
				/* 2.2 当前登录用户是报告录入人员时 */
				total = testReportService.countByOrgIdAndUserRealName(currentUserOrganization, info.getUserName(), pubFlag, configure);
				listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
						info.getUserName(), page, pageSize, pubFlag, configure);
			}
			Map map = new HashMap();
			map.put("listOfReport", listOfReport);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：获取所有退回给当前登陆供应商的报告
	 * @author ZhangHui 2015/6/9
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping( method = RequestMethod.GET , value = "/byuser/getBackToGYS/{page}/{pageSize}/{configure}")
	public View getBackToGYS(
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize,
			@PathVariable(value="configure") String configure,
			HttpServletRequest req,
			HttpServletResponse resp, 
			Model model) {
		
		ResultVO resultVO = new ResultVO();
		try {
			Long myOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			if(configure!=null && !"null".equals(configure)){
				// 搜索条件可以包括特殊字符斜杠'/'
				configure = configure.replaceAll("\\\\0gan0\\\\", "/").replaceAll("%","\\\\%");
			}
			
			long total = testReportService.countOfBackToGYS(myOrganization, configure);
			List<TestResult> listOfReport = testReportService.getReportsOfBackToGYSByPage(myOrganization, page, pageSize, configure);
				
			Map map = new HashMap();
			map.put("listOfReport", listOfReport);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 修改报告的消息提示信息
	 * @param tipText
	 * @param repId
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET,value="/editTips/{tipTextValue}/{reportId}")
	public View editRemark(@PathVariable("tipTextValue") String tipTextValue,
			@PathVariable("reportId") Long reportId,HttpServletRequest req, 
			HttpServletResponse resp,Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			testReportService.editTips(reportId, tipTextValue);
		}catch(ServiceException sex){
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	 
	   /**
	    * 功能描述：按报告id删除一条报告
	    * @author ZhangHui 2015/6/17
	    */
	   @RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	   public View removeServiceOrder(
			   @PathVariable Long id,
			   HttpServletRequest req, 
			   HttpServletResponse resp, 
			   Model model){
		   
		   	ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				TestResult report=testReportService.findById(id);
                testReportService.deleteTestReport(resultVO, id);
				
                //记录报告日志
				ReportFlowLog rfl=new ReportFlowLog(report.getId(),info.getUserName(),report.getSample().getProduct().getBarcode()
						,report.getSample().getBatchSerialNo(),report.getServiceOrder(),"完成 删除报告操作 ");
				staClient.offer(rfl);//记录报告日志为异步处理
			}catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
	   }
	
	   /**
		 * 从  t_test_category 中获取所有产品类别信息，
		 * 用于页面产品类别下拉框中数据展示
		 * @param req
		 * @param resp
		 * @param level 层级
		 * @param parentCode 父级code
		 * @param model
		 * @return
		 */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.GET, value="/searchCategory/{level}")
		public View searchCategory(HttpServletRequest req, HttpServletResponse resp,@PathVariable Long level ,
				@RequestParam("parentCode") String parentCode,Model model){
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				List<ProductCategory> listOfCategory = categoryService.getListOfCategory(level, parentCode);
				model.addAttribute("data", listOfCategory);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;  
		}
	   
		/**
		 * 按当前用户的组织机构名称或用户真实姓名，获取所有同机构的退回报告列表
		 * @param page
		 * @param pageSize
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(method=RequestMethod.GET,value="/getAllBackRep/{page}/{pageSize}/{isPubUser}")
		public View getAllBackRep(@PathVariable(value="page") int page, @PathVariable(value="pageSize") int pageSize,
				@PathVariable(value="isPubUser") boolean isPubUser, HttpServletRequest req, 
				HttpServletResponse resp, Model model){
			ResultVO resultVO = new ResultVO();
			try{

				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				/* 1 获取当前登录用户的组织机构id */
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long total;
				List<TestResult> listOfReport;
				if(isPubUser){
					/* 2.1 当前登录用户是发布人员时 */
					total = testReportService.countByOrgNameAndUserRealNameAndBackFlag(currentUserOrganization,
							null, '2', null);
					listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
							null, page, pageSize, '2', null);
				}else{
					/* 2.2 当前登录用户是报告录入人员时 */
					total = testReportService.countByOrgNameAndUserRealNameAndBackFlag(currentUserOrganization,
							info.getUserName(), '2', null);
					listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
							info.getUserName(), page, pageSize, '2', null);
				}
				Map map = new HashMap();
				map.put("listOfReport", listOfReport);
				map.put("counts", total);
				model.addAttribute("data", map);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}	
	
		/**
		 * 按当前用户的组织机构名称和其他过滤条件，获取所有同机构的退回报告列表
		 * @param page
		 * @param pageSize
		 * @param configure
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping( method = RequestMethod.GET , value = "/getAllBackRep/{page}/{pageSize}/{configure}/{isPubUser}")
		public View getBackTestReportListByConfigure(@PathVariable(value="page")int page, 
				@PathVariable(value="pageSize")int pageSize,@PathVariable(value="configure")String configure,
				@PathVariable(value="isPubUser") boolean isPubUser, HttpServletRequest req,
				HttpServletResponse resp, Model model) {
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				configure=configure.replaceAll("\\\\0gan0\\\\", "/"); // 搜索条件可以包括特殊字符斜杠'/'
				configure = configure.replaceAll("%","\\\\%");
				/* 1 获取当前登录用户的组织机构id */
				Long currentUserOrganization=Long.parseLong(AccessUtils.getUserRealOrg().toString());
				Long total;
				List<TestResult> listOfReport;
				if(isPubUser){
					/* 2.1 当前登录用户是发布人员时 */
					total = testReportService.countByOrgNameAndUserRealNameAndBackFlag(currentUserOrganization,
							null, '2', configure);
					listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
							null, page, pageSize, '2', configure);
				}else{
					/* 2.2 当前登录用户是报告录入人员时 */
					total = testReportService.countByOrgNameAndUserRealNameAndBackFlag(currentUserOrganization,
							info.getUserName(), '2', configure);
					listOfReport = testReportService.getReportsByOrgIdAndUserRealNameWithPage(currentUserOrganization,
							info.getUserName(), page, pageSize, '2', configure);
				}
				Map map = new HashMap();
				map.put("listOfReport", listOfReport);
				map.put("counts", total);
				model.addAttribute("data", map);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 获取可以发布的报告列表（此报告必须有pdf）
		 * @param page
		 * @param pageSize
		 * @param pubFlag
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		@RequestMapping( method = RequestMethod.GET , value = "/canPublished/{page}/{pageSize}/{publishFlag}")
		public View getReportList_canPub(@PathVariable(value="page") int page, 
				@PathVariable(value="pageSize") int pageSize, @PathVariable(value="publishFlag") char publishFlag, 
				HttpServletRequest req, HttpServletResponse resp, Model model) {
			ResultVO resultVO = new ResultVO();
			try {
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				long total = testReportService.countByIsCanPublish(currentUserOrganization, publishFlag, null);
				List<TestResult> listOfReport = testReportService.getListByIsCanPublish(currentUserOrganization,
						page, pageSize, publishFlag, null);
				Map map = new HashMap();
				map.put("listOfReport", listOfReport);
				map.put("counts", total);
				model.addAttribute("data", map);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 按过滤条件获取可以发布的报告列表（此报告必须有pdf）
		 * @param page
		 * @param pageSize
		 * @param pubFlag
		 * @param configure
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
		@RequestMapping( method = RequestMethod.GET , value = "/canPublished/{page}/{pageSize}/{configure}/{publishFlag}")
		public View getTestReportListByConfigure_(@PathVariable(value="page") int page, 
				@PathVariable(value="pageSize") int pageSize, @PathVariable(value="publishFlag") char publishFlag, 
				@PathVariable(value="configure") String configure, 
				HttpServletRequest req, HttpServletResponse resp, Model model) {
			ResultVO resultVO = new ResultVO();
			try {
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				configure=configure.replaceAll("\\\\0gan0\\\\", "/"); // 搜索条件可以包括特殊字符斜杠'/'
				configure = configure.replaceAll("%","\\\\%");
				Long currentUserOrganization=Long.parseLong(AccessUtils.getUserRealOrg().toString());
				long total = testReportService.countByIsCanPublish(currentUserOrganization, publishFlag, configure);
				List<TestResult> listOfReport = testReportService.getListByIsCanPublish(currentUserOrganization,
						page, pageSize, publishFlag, configure);
				Map map = new HashMap();
				map.put("listOfReport", listOfReport);
				map.put("counts", total);
				model.addAttribute("data", map);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 根据报告id获取当前报告的检测项目列表
		 * @author TangXin
		 */
		@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
		@RequestMapping( method = RequestMethod.GET , value = "/testItems/{page}/{pageSize}/{id}")
		public View getTestItemsByRepID(@PathVariable(value="page")int page, 
				@PathVariable(value="pageSize")int pageSize,@PathVariable(value="id")Long id, HttpServletRequest req,HttpServletResponse resp, Model model) {
			ResultVO resultVO = new ResultVO();
			try {
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				long total = testPropertyService.getCountByReportId(id);
				List<TestProperty> listOfItem = testPropertyService.getListByReportIdWithPage(id, page, pageSize);
				Map map = new HashMap();
				map.put("listOfItem", listOfItem);
				map.put("counts", total);
				model.addAttribute("data", map);
			}catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 实现当报告为未结构化报告时，编辑时，自动加载同产品的最近一次已结构化报告的检测项目
		 * @param myReportId 本次编辑状态的未结构化报告id
		 * @param productId 产品id
		 * @author zhangHui 2015/5/8
		 */
		@SuppressWarnings("unused")
		@RequestMapping( method = RequestMethod.GET , value = "/testItems/{myReportId}/{productId}")
		public View getTestItemsByRepID(
				@PathVariable(value="myReportId") long myReportId, 
				@PathVariable(value="productId") long productId,
				HttpServletRequest req,
				HttpServletResponse resp, 
				Model model) {
			
			ResultVO resultVO = new ResultVO();
			try {
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				// 1. 判断本报告是否为未结构化报告
				long count = testResultHandlerService.count(myReportId, 1);
				
				// 2. 为本未结构化报告加载同产品的以结构化报告的检测项目
				List<TestProperty> listOfItem = null;
				if(count > 0){
					long hasStruReportId = testResultHandlerService.getTestResultIdOfHasStructed(productId);
					listOfItem = testPropertyService.findByReportIdWithoutId(hasStruReportId);
				}
				model.addAttribute("data", listOfItem);
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 背景：生产企业 报告录入界面，产品条形码下拉选择
		 * 功能描述：查找当前登录的生产企业已经绑定过qs号的所有产品条形码
		 * @author ZhangHui 2015/6/5
		 */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.GET, value="/searchBarCodeAll")
		public View searchTestProduct(HttpServletRequest req,HttpServletResponse resp, Model model){
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				List<ProductOfMarketVO> vos = productToBusinessUnitService.getListBarcodeByOrganization(currentUserOrganization);
				model.addAttribute("data", vos);
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 功能描述：按barcode从t_test_template获取最近一条报告的所有信息。
		 * 用于页面按产品条形码自动加载数据的功能。
		 * @author ZhangHui
		 * 最新更新：ZhangHui 2015/5/14<br>
		 * 更新内容：提高查询效率，减少返回值
		 */
		@RequestMapping(method=RequestMethod.GET, value="/getReportByBarcode/{barcode}")
		public View searchTestProduct(@PathVariable String barcode,HttpServletRequest req,HttpServletResponse resp, 
				Model model){
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				ReportOfMarketVO report_vo = templateService.findReportByBarCode(barcode, info,currentUserOrganization);
				boolean isExist = testReportService.verifyBackReportByBarcode(barcode);
				model.addAttribute("isExist", isExist);
				model.addAttribute("data", report_vo);
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 验证报告编号是否已经存在，报告编号必须唯一的验证。
		 */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.PUT, value="/validatReportNo")
		public View validateRepNo(
				@RequestParam String report_no,
				@RequestParam String barcode,
				@RequestParam String batch_no,
				@RequestParam(required=false) Long edit_report_id,
				HttpServletRequest req, 
				HttpServletResponse resp, 
				Model model){
			
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				report_no = report_no.replaceAll("\\\\0gan0\\\\", "/");
				
				boolean isExist= testReportService.checkUniquenessOfReport(edit_report_id, report_no, barcode, batch_no);
				
				model.addAttribute("data", !isExist);
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 保存报告之前验证产品类别是否为系统中的类别选项，
		 * 如果不是，则应提示用户重新选择。（不允许用户自定义）
		 * @param category
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.GET, value="/validatCategory/{categoryName}")
		public View validatCategory(@PathVariable String categoryName,HttpServletRequest req, HttpServletResponse resp, Model model){
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				ProductCategory category = categoryService.getCategoryByName(categoryName);
				model.addAttribute("data", category);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
	    * 报告录入界面，自动搜索检测项目每列的值
	    * @param colName
	    * @param req
	    * @param resp
	    * @param model
	    * @return
	    */
	   @SuppressWarnings("unused")
	   @RequestMapping(method=RequestMethod.GET,value="/autoItems/{colName}")
	   public View autoTtestItems(@PathVariable(value="colName")int colName,@RequestParam int page,
			   @RequestParam int pageSize,@RequestParam String keyword,
			   HttpServletRequest req, HttpServletResponse resp, Model model){
		   ResultVO resultVO = new ResultVO();
		   try {
			   AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			   List<String> listOfItem = testReportService.autoTestItems(colName,keyword,page,pageSize);
			   model.addAttribute("data", listOfItem);
		    } catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			return JSON;
	   }
	
	   /**
	    * easy报告退回功能
	    * @param id
	    * @param req
	    * @param resp
	    * @param model
	    * @return
	    */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.POST, value="/goBack/{backResult}/{reportId}")
		public View boBack(@PathVariable(value="reportId") Long reportId,
				@PathVariable(value="backResult") String backResult,
				@RequestBody ReportBackVO reportBackVO,
				HttpServletRequest req, HttpServletResponse resp, Model model){
			ResultVO resultVO = new ResultVO();
			try{
				reportBackVO.setReturnMes(URLDecoder.decode(reportBackVO.getReturnMes(), "UTF-8"));
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				testReportService.goBack(reportId, reportBackVO);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
				((Throwable)sex.getException()).printStackTrace();
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
		 * 发布报告时，需要校验当前报告是否有产品图片，
		 * 如果没有，则提示用户“会用一张临时图片代替”
		 * @param repId
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.GET, value="/validatHaveProJpg/{repId}")
		public View validateRepNo(@PathVariable Long repId,HttpServletRequest req, HttpServletResponse resp, Model model){
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				boolean isHave= testReportService.validatHaveProJpg(repId);
				model.addAttribute("data", isHave);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 发布之前，需对当前报告的pdf是否签过名的验证。
		 * 如果没有签名，则不允许发布。
		 * @param reportId
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.GET, value="/getSignFlag/{reportId}")
		public View getSignFlag(@PathVariable Long reportId, HttpServletRequest req, HttpServletResponse resp, Model model){
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				boolean isSign= testReportService.getSignFlag(reportId);
				model.addAttribute("data", isSign);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 功能描述：查找当前用户通过上传pdf功能增加的pdf列表
		 * @author ZhangHui 2015/6/18
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping( method = RequestMethod.GET , value = "/getListByUploadPdf/{configure}/{page}/{pageSize}")
		public View getTestReportListByUploadPdf(
				@PathVariable(value="configure") String configure, 
				@PathVariable(value="page") int page, 
				@PathVariable(value="pageSize") int pageSize, 
				HttpServletRequest req, 
				HttpServletResponse resp,
				Model model) {
			
			ResultVO resultVO = new ResultVO();
			try {
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				
				long total = testReportService.countOfPasePdf(info.getUserName(), configure);
				List<TestResult> listOfReport = testReportService.getListOfPasePdfByPage(info.getUserName(), page, pageSize, configure);
				
				Map map = new HashMap();
				map.put("listOfReport", listOfReport);
				map.put("counts", total);
				model.addAttribute("data", map);
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;
		}
		
		/**
		 * 查找三级分类或者执行标准
		 * @param req
		 * @param resp
		 * @param model
		 * @param upId 二级分类ID
		 * @param categoryFlag  true:查找二级分类下的三级分类，false：查找二级分类下的执行标准
		 * @return
	     * @author 郝圆彬
		 */
		@SuppressWarnings("unused")
		@RequestMapping(method=RequestMethod.GET, value="/searchLastCategory/{upId}")
		public View searchLastCategory(HttpServletRequest req, HttpServletResponse resp,
				@RequestParam("categoryFlag") boolean categoryFlag,@PathVariable Long upId,Model model){
			ResultVO resultVO = new ResultVO();
			try{
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				List<ProductCategoryInfo> listOfCategory = productCategoryInfoService.getListOfUpId(upId, categoryFlag);
				model.addAttribute("data", listOfCategory);
			} catch (ServiceException sex) {
				resultVO.setErrorMessage(sex.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				((Throwable)sex.getException()).printStackTrace();
			} catch (Exception e) {
				resultVO.setErrorMessage(e.getMessage());
				resultVO.setStatus(SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
			model.addAttribute("result", resultVO);
			return JSON;  
		}
	
	/**
	 * 功能描述：通过解析pdf增加报告(目前只支持蒙牛提供的两个pdf模板)
	 * @author ZhangHui 2015/6/18
	 */
	@RequestMapping( method = RequestMethod.POST , value = "/analysisPDF")
	public View analysisPDF(
			@RequestBody Resource pdfResource, 
			HttpServletRequest req, 
			HttpServletResponse resp, 
			Model model) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setEnterpriseName("mn");
		
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long myOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			info.setOrganization(myOrganization);
			
			/**
			 * 1. 解析pdf
			 */
			ReportOfMarketVO report_from_parse = (ReportOfMarketVO) ReadPdfUtil.readFileOfPDF(pdfResource.getFile(), productService, testReportService).get("report"); 
			if(report_from_parse == null){
				resultVO.setErrorMessage("pdf解析失败!");
				resultVO.setStatus(SERVER_STATUS_FAILED);
				model.addAttribute("result", resultVO);
				return JSON;
			}
			
			ProductOfMarketVO product_from_parse = report_from_parse.getProduct_vo();
			BusinessUnitOfReportVO bus_from_parse = report_from_parse.getBus_vo();
			
			/**
			 * 2. 按产品别名和规格匹配产品 
			 */
			List<Product> products = productService.getListByOtherNameAndPDFformat(
					product_from_parse.getName(), product_from_parse.getFormat(), false);
			if(products.size() < 1){
				products = productService.getListByOtherNameAndPDFformat(
						product_from_parse.getName()+"/", product_from_parse.getFormat(), true);
				if(products.size() < 1){
					products = productService.getListByOtherNameAndPDFformat(
							"/" + product_from_parse.getName(), product_from_parse.getFormat(), true);
				}
			}
			
			/**
			 * 3. 新建报告
			 */
			int create_report_count = 0;
			for(Product product : products){
				product.setLocal(true); // 此产品属于当前登录企业的产品，无需执行引进操作 ZhangHui 2015/5/14
				
				// 3.1 封装report_vo
				ReportOfMarketVO report_vo = new ReportOfMarketVO();
				report_vo.setDbflag("parse_pdf");
				report_vo.setPublishFlag('3');
				report_vo.setNew_flag(true);
				report_vo.setAutoReportFlag(false);
				report_vo.setTestType("企业自检");
				report_vo.setDbflag("parse_pdf");
				report_vo.setTip("来源：pdf自动解析！");
				report_vo.setPass(true);
				
				report_vo.setTestDate(report_from_parse.getTestDate());         // 报告检测日期
				report_vo.setServiceOrder(report_from_parse.getServiceOrder()); // 报告编号
				
				/* 3.2  封装product_vo */
				ProductOfMarketVO product_vo = new ProductOfMarketVO();
				product_vo.setBarcode(product.getBarcode());
				product_vo.setCan_edit_pro(false);
				
				product_vo.setProductionDate(product_from_parse.getProductionDate()); // 产品生产日期
				product_vo.setBatchSerialNo(product_from_parse.getBatchSerialNo());   // 产品批次
				
				report_vo.setProduct_vo(product_vo);
				
				// 3.3 封装bus_vo
				BusinessUnitOfReportVO bus_vo = new BusinessUnitOfReportVO();
				bus_vo.setCan_edit_bus(false);
				bus_vo.setCan_edit_qs(false);
				
				bus_vo.setName(bus_from_parse.getName()); // 生产企业名称
				
				report_vo.setBus_vo(bus_vo);
				
				/* 4. 检测项目 */
				List<TestProperty> testProperties = new ArrayList<TestProperty>();
				for(TestProperty property : report_from_parse.getTestProperties()){
					TestProperty item = new TestProperty();
					item.setName(property.getName());                   // 检测名称
					item.setTechIndicator(property.getTechIndicator()); // 检测依据
					item.setResult(property.getResult());               // 检测结果
					item.setAssessment(property.getAssessment());       // 单项评价
					testProperties.add(item);
				}
				report_vo.setTestProperties(testProperties); // 检测项目
				
				// 5. 报告与pdf关联
				Set<Resource> repattachments = new HashSet<Resource>();
				Resource resource = new Resource();
				resource.setFile(pdfResource.getFile());
				resource.setFileName(pdfResource.getFileName());
				resource.setName(pdfResource.getName());
				resource.setType(resourceTypeService.findById(3L));
				repattachments.add(resource);
				report_vo.setRepAttachments(repattachments);
				
				/* 6. 保存报告 */
				try {
					save(report_vo, info,false);
					create_report_count++;
				} catch (Exception e) {
					log.info("保存报告发生异常: " + e.getMessage());
				}
			}
			
			log.info("方法执行成功并退出！");
			
			model.addAttribute("data", create_report_count);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
			log.error(e.getMessage());
		}
		resultVO.setObject(null);
		model.addAttribute("result", resultVO);
		return JSON;
	}

	
	/**
	 * 功能描述：新增/编辑 报告
	 * @param current_business_name 当前正在录入报告的企业名称
	 * @param applyUpdat 是否是用户需要跟新处理portal 跟新报告请求
	 * @author ZhangHui 2015/6/5
	 */
	@RequestMapping(method=RequestMethod.POST, value="/{applyUpdat}")
	public View createTestReport(
			@RequestBody ReportOfMarketVO report_vo,
	        @PathVariable("applyUpdat") Boolean applyUpdat,
	        @RequestParam(value="save",required=false) Boolean save,
	        HttpServletRequest req,
	        HttpServletResponse resp, 
	        Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long myOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			info.setOrganization(myOrganization);
			
			
			if(save==null){
				save=true;
			}
			// 1. 保存报告
			report_vo = save(report_vo, info,save);
			
			// 2. template 更新
			templateService.saveTemplate(report_vo, info, myOrganization);
			
			// 3. portal报告申请更新
			if(applyUpdat) {
				ProductOfMarketVO product_vo = report_vo.getProduct_vo();
			    updataReportService.changeApplyReportStatus(product_vo.getBarcode(), report_vo.getTestType(), 1);
			}
			
			model.addAttribute("data", report_vo);
		}catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		resultVO.setObject(null);
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 *	保存报告 
	 */
	private ReportOfMarketVO save(ReportOfMarketVO report_vo, AuthenticateInfo info,boolean structed) throws Exception {
		boolean isStructed = false;
		
		ProductOfMarketVO product_vo = report_vo.getProduct_vo();
		//保质期
		String endDate = product_vo.getExpire_Date();
	    boolean timeFlag = false;
	    
	    //如果接收的保质期没有单位，那么就会导致无法识别是年还是月，天；因此做以下处理
	    if(endDate!=null&&!"".equals(endDate)){
	    	if(endDate.indexOf("年")==-1&&endDate.indexOf("月")==-1&&endDate.indexOf("天")==-1){
	    		timeFlag = true;
	    	}
	    }
		if(endDate==null||"".equals(endDate)||timeFlag){
			endDate = product_vo.getExpirationDate()+"天";
		}
		int year = 0;
		int month = 0;
		int dayofyear = 0;
		if(endDate!=null&&!"".equals(endDate)&&!"无".equals(endDate)){
			//以下替换是为了避免输入的保质期为汉字年月 比如：八个月
			endDate = endDate.replaceAll("个", "").replaceAll("一", "1").replaceAll("二", "2").replaceAll("三", "3")
					.replaceAll("四", "4").replaceAll("五", "5").replaceAll("六", "6").replaceAll("七", "7")
					.replaceAll("八", "8").replaceAll("九", "9").replaceAll("零", "0");
			String  strYear = "0";
			String  strMonth = "0";
			String  strDayofyear = "0";
			//如果保质期是年
			if(endDate.indexOf("年")!=-1){
				strYear = endDate.substring(0, endDate.lastIndexOf("年"));
			}
			//如果保质期有年和月
			if(endDate.indexOf("年")!=-1&&endDate.indexOf("月")!=-1){
				strMonth = endDate.substring(endDate.indexOf("年")+1, endDate.lastIndexOf("月"));
				if(strMonth.length()>2){
					strMonth = strMonth.replaceAll("十", "");
				}else if(strMonth.length() == 2){
					strMonth = strMonth.replaceAll("十", "1");	
				}else{
					strMonth = strMonth.replaceAll("十", "10");	
				}
			//如果保质期只有月
			}else if(endDate.indexOf("月")!=-1){
				strMonth = endDate.substring(0, endDate.lastIndexOf("月"));
				if(strMonth.length()>2){
					strMonth = strMonth.replaceAll("十", "");
				}else if(strMonth.length() == 2){
					strMonth = strMonth.replaceAll("十", "1");	
				}else{
					strMonth = strMonth.replaceAll("十", "10");	
				}
			}
			//如果保质期有月和天
			if(endDate.indexOf("月")!=-1&&endDate.indexOf("天")!=-1){
				strDayofyear = endDate.substring(endDate.indexOf("月")+1, endDate.lastIndexOf("天"));	
			//如果保质期有年和天，没有月
			}else if(endDate.indexOf("年")!=-1&&endDate.indexOf("月")==-1&&endDate.indexOf("天")!=-1){
				strDayofyear = endDate.substring(endDate.indexOf("年")+1, endDate.lastIndexOf("天"));	
			//如果保质期只有天
			}else if(endDate.indexOf("年")==-1&&endDate.indexOf("天")!=-1){
				strDayofyear = endDate.substring(0, endDate.lastIndexOf("天"));
			}
			year = Integer.parseInt(strYear);
			if(year>100){
				year = 100;
			}
			month = Integer.parseInt(strMonth);
			if(month>1200){
				month = 1200;
			}
			dayofyear = Integer.parseInt(strDayofyear);
			if(dayofyear>36500){
				dayofyear = 36500;
			}
		}
		
		//生产日期
		Date dt =  product_vo.getProductionDate();
		//保质期日的计算    
		    Calendar rightNow = Calendar.getInstance();
		    rightNow.setTime(dt);
		    rightNow.add(Calendar.YEAR,year);//日期年相加
		    rightNow.add(Calendar.MONTH,month);//日期月相加
		    rightNow.add(Calendar.DAY_OF_YEAR,dayofyear);//日期天相加
		   //计算出的过期日期
		    Date dt1=rightNow.getTime();
		    product_vo.setExpireDate(dt1);

//		  //保质期日的计算
//		  Date expirationDate =  product_vo.getExpireDate();
//		  int dayNun = Integer.parseInt(product_vo.getExpirationDate());
//		  long   nDay=(expirationDate.getTime()/(24*60*60*1000)+1+dayNun)*(24*60*60*1000);  
//		  expirationDate.setTime(nDay);
//			//结束日期（即：过期日期）
//		  product_vo.setExpireDate(expirationDate);
		// 判断报告必填项
		if(product_vo.getBatchSerialNo()==null || "".equals(product_vo.getBatchSerialNo())){
			  throw new Exception("报告批次和生产日期不可为空");
		 }
		/**
		 * 先判断当前登录企业生产日期是否必填，必填 才对生产日期进行判断
		 * @author longxianzhen 2015/07/17
		 */
		Long myOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		boolean proDateIsRequired=PropertiesUtil.getProDateIsRequired(myOrganization);
		if(proDateIsRequired){
			if(product_vo.getProductionDate()==null){
			  throw new Exception("报告生产日期不可为空");
			}
		}
		
		
		if(report_vo.isNew_flag()){
			/* 判断一： 新增时，判断该报告是否已经存在  */
			boolean isExist = testReportService.checkUniquenessOfReport(null, report_vo.getServiceOrder(),
					product_vo.getBarcode(), product_vo.getBatchSerialNo());
			
			if(isExist){
				/** 如果是进口食品更改提示验证条件 
				 *  @author longxianzhen 2015/06/09 
				 * */
				if(report_vo.getImpProTestResult()!=null){
					throw new Exception("该报告已存在！（重复条件：卫生证书编号、产品条形码、生产日期）");
				}else{
					throw new Exception("该报告已存在！（重复条件：报告编号、产品条形码、批次）");
				}
			}
		}else{
			/* 判断二： 编辑时，判断该报告是否已经发布  */
//			TestResult orig_report = testReportService.findById(report_vo.getId());
			/**
			 * 代码优化 ：只需要查询相关的值，不需要查询整个对象
			 * 修改人：wubiao  2016.2.18 10:25
			 */
			TestResult orig_report = testReportService.findByIdPublishFlag(report_vo.getId());
			
			long count_handler = testResultHandlerService.countByCanEdit(report_vo.getId());
			
			/**
			 * isStructed 表示此报告是否为结构化报告
			 * 		true  代表是
			 * 		false 代表不是
			 */
			if(count_handler > 0&&structed){
				isStructed = true;
			}
			
			
			if(testResultHandlerService.isCanViewAllInfo(report_vo.getId())==true&&!isStructed && (orig_report.getPublishFlag()=='0' || orig_report.getPublishFlag()=='1')){
				throw new Exception("此报告已经发布，不可以更新！");
			}
		}
		
		
		// 1. 上传产品图片
		UploadUtil uploadUtil = new UploadUtil();
		String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + product_vo.getBarcode();
		String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + product_vo.getBarcode();
		
		for (Resource resource : product_vo.getProAttachments()) {
			if (resource.getFile() != null) {
				String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
				String name = product_vo.getBarcode() + "-" + randomStr + "." + resource.getType().getRtDesc();
				boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
				if(isSuccess){
					String url;
					if(UploadUtil.IsOss()){
						url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
					}else{
						url = webUrl + "/" + name;
					}
					resource.setName(name);
					resource.setUrl(url);
				}else{
					throw new Exception("产品图片上传失败");
				}
			}
		}
		
		// 2. 上传pdf
		String reportNOEng = MKReportNOUtils.convertCharacter(report_vo.getServiceOrder(), null);
		report_vo.setReportNOEng(reportNOEng);
		
		ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_REPORT_PATH) + "/" + reportNOEng;
		webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_REPORT_PATH) + "/" + reportNOEng;
		
		Map<String, String> map = null;
		if(report_vo.isAutoReportFlag()){
			// 自动生成pdf
			ByteArrayInputStream input = ftpService.mkUploadReportPdf(report_vo);
			
			if (input != null) {
				String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
				String name = reportNOEng + "-" + randomStr + "-report.pdf";
				map = uploadUtil.uploadReportPdf(input, null, ftpPath, name, reportNOEng, report_vo.getTestType());
				
				if(map != null){
					Resource resource = new Resource();
					resource.setName(name);
					resource.setFileName(name);
					resource.setUrl(map.get("fullPdfPath"));
					resource.setType(resourceTypeService.findById(3L));
					resource.setUploadDate(new Date());
					
					report_vo.getRepAttachments().clear();
					report_vo.getRepAttachments().add(resource);
				}else{
					throw new Exception("自动生成pdf成功，但上传失败");
				}
			}else{
				throw new Exception("自动生成pdf失败");
			}
		}else{
			// 上传pdf
			for (Resource resource : report_vo.getRepAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = reportNOEng + "-" + randomStr + "-report.pdf";
					
					ByteArrayInputStream in_fullPdf = new ByteArrayInputStream(resource.getFile());
					ByteArrayInputStream in_interceptionPdf = new ByteArrayInputStream(resource.getFile());
					map = uploadUtil.uploadReportPdf(in_fullPdf, in_interceptionPdf, ftpPath, name, reportNOEng, report_vo.getTestType());
					if(map != null){
						resource.setName(name);
						resource.setUrl(map.get("fullPdfPath"));
					}else{
						throw new Exception("报告pdf上传失败");
					}
				}
			}
		}
		report_vo.setMap(map);
		savePhoto(report_vo.getReportImgList(), report_vo.getServiceOrder());
		savePhoto(report_vo.getCheckImgList(), report_vo.getServiceOrder());
		savePhoto(report_vo.getBuyImgList(), report_vo.getServiceOrder());
		// 3. 保存报告
		Long current_business_id = businessUnitService.findIdByOrg(info.getOrganization());
		testReportService.saveReport(report_vo, current_business_id, info, isStructed);
		
		// 6. 记录报告日志(异步处理)
		if(report_vo.isNew_flag()){
			ReportFlowLog rfl = new ReportFlowLog(report_vo.getId(), info.getUserName(), product_vo.getBarcode()
					,product_vo.getBatchSerialNo(), report_vo.getServiceOrder(), "完成 新增报告操作 ");
			staClient.offer(rfl);
		}else{
			ReportFlowLog rfl = new ReportFlowLog(report_vo.getId(), info.getUserName(), product_vo.getBarcode()
					, product_vo.getBatchSerialNo(), report_vo.getServiceOrder(), "完成 更新报告操作 ");
			staClient.offer(rfl);
		}
		
		// 7. 返回
		report_vo.setTestProperties(testPropertyService.findByReportId(report_vo.getId()));
		
		return report_vo;
	}
	
	
	private Set<Resource> savePhoto(Set<Resource> resList,String orderNo) throws Exception {
		
		UploadUtil uploadUtil = new UploadUtil();
		String filePath=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		for (Resource resource : resList) {
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + filePath;
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + filePath;
			if (resource.getFile() != null) {
				String name =uploadUtil.createFileNameByDate(resource.getFileName());
				boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
				if(isSuccess){
					String url;
					if(UploadUtil.IsOss()){
						url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
					}else{
						url = webUrl + "/" + name;
					}
					resource.setUrl(url);
					resource.setName(name);
				}else{
					throw new Exception("产品图片上传失败");
				}
			}else{
				resource.setId(null);
			}
		}
		return resList;
	}
	
	/**
	 * 根据barcode验证该产品是否有已退回的报告没有处理
	 * @param barcode
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/verifyBackReport/{barcode}")
	public View verifyBackReportByBarcode(@PathVariable String barcode, HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 验证是否存在已退回的报告 */
			boolean isExist = testReportService.verifyBackReportByBarcode(barcode);
			model.addAttribute("isExist", isExist);
		} catch (ServiceException e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}