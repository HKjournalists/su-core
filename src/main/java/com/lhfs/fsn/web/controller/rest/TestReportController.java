package com.lhfs.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_REPORT_CENSOR;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_REPORT_SAMPLE;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_REPORT_SELF;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_REPORT_THIRD;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.test.ReportFlowLog;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestReports;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.lhfs.fsn.cache.EhCacheFactory;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.vo.BusinessTestResultVO;
import com.lhfs.fsn.vo.ProductInfoVO;
import com.lhfs.fsn.vo.ProductJGVO;
import com.lhfs.fsn.vo.ResultVO;
import com.lhfs.fsn.vo.TestBusinessUnitVo;
import com.lhfs.fsn.web.controller.RESTResult;

import net.sf.ehcache.Element;

@Controller
@Scope("prototype")
@RequestMapping("/portal/test_report")
public class TestReportController {
	
	@SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(TestReportController.class);
	
	@Autowired private TestReportService testReportService;
	@Autowired private TestResultService testResultService;
	@Autowired private StatisticsClient staClient;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private EnterpriseRegisteService enterpriseRegisteService;
	@Autowired private TestPropertyService testPropertyService;
	
	/**
	 * 给大众门户提供接口：跟据产品id获取自检、送检、抽检报告
	 * @param id 产品ID
	 * @param type 报告类型：{"self","censor","sample"}
	 * @param sn 序号
	 * @param date 日期：20130801
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="")
	public RESTResult<TestRptJson> getRpt(@RequestParam long id, @RequestParam String type, 
			@RequestParam int sn, @RequestParam String date){	
		try {
			Element result = null;
			if(("self").equals(type)){
				type = "企业自检";
				result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SELF + "_" + id + sn + date.toString());
			}else if(("censor").equals(type)){
				type = "企业送检";
				result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_CENSOR + "_" + id + sn + date.toString());
			}else if(("sample").equals(type)){
				type = "政府抽检";
				result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SAMPLE + "_" + id + sn + date.toString());
			}else if("third".equals(type)){
				type = "第三方检测";
				result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_THIRD + "_" + id + sn + date.toString());
			}
			TestRptJson testRptJson = null;
			//是否为portal调用;是为ture；否则：false
			boolean portalFlag = true;
			if(result == null){
				if("".equals(date)||date==null){
					testRptJson = testReportService.getReportJson(id, type, sn, date, false, null,portalFlag);
				}else{
					testRptJson = testReportService.getReportJsonByDate(id, type, sn, date,portalFlag);
				}
				if(("企业自检").equals(type)){
					EhCacheFactory.put(CACHE_REPORT_SELF + "_" + id  + sn + date.toString(), testRptJson);
				}else if(("企业送检").equals(type)){
					EhCacheFactory.put(CACHE_REPORT_CENSOR + "_" + id + sn + date.toString(), testRptJson);
				}else if(("政府抽检").equals(type)){
					EhCacheFactory.put(CACHE_REPORT_SAMPLE + "_" + id + sn + date.toString(), testRptJson);
				}
			}else{
				testRptJson = (TestRptJson) result.getObjectValue();
			}
			if (testRptJson == null)
				return new RESTResult<TestRptJson>(0, "非法操作或者数据缺失！");
			return new RESTResult<TestRptJson>(1, testRptJson);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getTestResultById")
	public View getTestResultById(Model model,@RequestParam long id){
		try {
			TestResult testResult=testReportService.findById(id);
			TestRptJson trj=testReportService.getReportJson(testResult);
			model.addAttribute("testResult",trj);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getThirdCountByProductId")
	public View getThirdCountByProductId(Model model,@RequestParam long productId){
		model.addAttribute("count",testReportService.getThirdCountByProductId(productId));
		model.addAttribute("testPropertyList",this.testPropertyService.getTestPropertyListByProductId(productId));
		return JSON;
	}
	
	
	/**
	 * 给大众门户提供接口：跟据产品id获取自检、送检、抽检报告
	 * @param id 产品ID
	 * @param type 报告类型：{"self","censor","sample"}
	 * @param sn 序号
	 * @param date 日期：20130801
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="/getThirdList")
	public View getThirdList(Model model,@RequestParam long productId){	
		String type="第三方检测";
		List<TestRptJson> testResultList=testReportService.getThirdList(productId, Calendar.getInstance().get(Calendar.YEAR),type);
		model.addAttribute("testResultList", testResultList);
		return JSON;
	}
	
	/**
	 * 给手机app提供接口：跟据产品id获取自检、送检、抽检报告
	 * @param id
	 * @param date
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="/{id}/{date}")
	public RESTResult<TestReports> appNeedsReport(@PathVariable long id, @PathVariable String date){
		try {
			TestRptJson self = testReportService.getReportJson(id, "企业自检", date);
			TestRptJson censor = testReportService.getReportJson(id, "企业送检", date);
			TestRptJson sample = testReportService.getReportJson(id, "政府抽检", date);
			TestReports rpts = new TestReports(self, censor, sample);
			return new RESTResult<TestReports>(1, rpts);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*********提供给fdams监管系统接口***************
	 * 根据产品产品Id(pid)获取报告相关信息
	 * Author：hy
	 */
	 @RequestMapping(method=RequestMethod.GET,value="/getReportBySampleId/{id}")
     public View getReportBySampleId(@PathVariable(value="id")Long id,@RequestParam(value="batch")String batch,
    		 HttpServletRequest req, HttpServletResponse resp, Model model){
         ResultVO resultVO = new ResultVO();
         try {
        	 model = testReportService.getReportBySampleId(id,batch,model);
          } catch (Exception e) {
              resultVO.setErrorMessage(e.getMessage());
              resultVO.setStatus(SERVER_STATUS_FAILED);
          }
          return JSON;
     }
	 
	 /*******提供给南方平台的接口*****************************
	  * 根据条形码barcode获取自检报告pdfUrl
	  * @param barcode
	  * @param req
	  * @param resp
	  * @param model
	  * @return
	  * @author 郝圆彬
	  */
	 @RequestMapping(method = RequestMethod.GET, value="/getSelfReportPdfUrlsByBarcode/{barcode}")
	 public View getSelfReportPdfUrlsByBarcode(@PathVariable(value="barcode")String barcode,@RequestParam String batch,
			 HttpServletRequest req, HttpServletResponse resp, Model model){
		 ResultVO resultVO = new ResultVO();
		 try {
        	 model.addAttribute("data", testReportService.getSelfReportPdfUrlsByBarcode(barcode,batch));
          } catch (Exception e) {
              resultVO.setErrorMessage(e.getMessage());
              resultVO.setStatus(SERVER_STATUS_FAILED);
          }
		  model.addAttribute("result", resultVO);
          return JSON;
	 }
	 /**
		 * LIMS主动退回报告接口
		 * @author LongXianZhen 2015/4/9
		 */
	 @RequestMapping(method = RequestMethod.GET, value="/backLims")
	 public View backLims(@RequestParam(value="sampleId")Long sampleId,@RequestParam(value="sampleNo") String sampleNo,
			 @RequestParam(value="serviceOrder") String serviceOrder,@RequestParam(value="organizationID") Long organizationID,
			 HttpServletRequest req, HttpServletResponse resp, Model model){
		 ResultVO resultVO = new ResultVO();
		 try {
			 TestResult tr=testResultService.getLimsReportByConditions(sampleId,sampleNo,serviceOrder,organizationID);
			 if(tr!=null){
				 testResultService.deleteById(tr.getId());
				 resultVO.setStatus(SERVER_STATUS_SUCCESS);
				 resultVO.setMessage("退回成功");
				//记录报告日志
				ReportFlowLog rfl=new ReportFlowLog(tr.getId(),"LIMS人员",tr.getSample().getProduct().getBarcode()
						,tr.getSample().getBatchSerialNo(),tr.getServiceOrder(),"完成 LIMS主动退回报告操作");
				staClient.offer(rfl);//记录报告日志为异步处理
			 }else{
				 resultVO.setStatus(SERVER_STATUS_FAILED);
				 resultVO.setMessage("没有找到该报告");
			 }
          }catch (ServiceException e) {
        	  ((Throwable) e.getException()).printStackTrace();
              resultVO.setMessage("退回失败-->"+((Throwable) e.getException()).getMessage());
              resultVO.setStatus(SERVER_STATUS_FAILED);
          } catch (Exception e) {
        	  e.printStackTrace();
              resultVO.setMessage(e.getMessage());
              resultVO.setStatus(SERVER_STATUS_FAILED);
          }
		  model.addAttribute("result", resultVO);
          return JSON;
	 }
	 
	 //========================================================================================================================
	 
	 
	/**
	  * 根据条形码barcode获取所有生产企业
	  * @author wubiao
	  * @date   2015.11.14
	  * @param barcode
	  * @param req
	  * @param resp
	  * @param model
	  * @return
	  * 
	  */
	 @RequestMapping(method = RequestMethod.GET, value="/getByBarcode")
	 public View getByBarcode(@RequestParam(value="barcode")String barcode,@RequestParam(value="page",required=false) Integer page,@RequestParam(value="pageSize",required=false) Integer pageSize,
			 HttpServletRequest req, HttpServletResponse resp, Model model){
		  List<TestBusinessUnitVo> busList =  testResultService.getBusinessUnitVOList(barcode,page,pageSize);
		if (busList.size() > 0) {
			 model.addAttribute("status", true);
			model.addAttribute("result", busList);
		} else {
			 model.addAttribute("status", false);
			model.addAttribute("result", "非法操作或者数据缺失！");
		}
          return JSON;
	 }
	 /**
	  * 根据条形码barcode获取ID下生产企业基本信息
	  * @author wubiao
	  * @param barcode 条形码
	  * @param id  生产企业ID
	  * @param req
	  * @param resp
	  * @param model
	  * @param date 日期：2015.11.13
	  * @return
	  */
	 @RequestMapping(method = RequestMethod.GET, value="/getByBase")
	 public View getByBase(@RequestParam(value="barcode")String barcode,@RequestParam(value="buId") long buId,@RequestParam(value="date",required=false) String date,
			 HttpServletRequest req, HttpServletResponse resp, Model model){
		 TestBusinessUnitVo busList =  testResultService.getBusinessUnitVO(barcode,buId,date);
		 if(busList == null){
			 model.addAttribute("status", false);
			 model.addAttribute("message", "非法操作或者数据缺失！");
		 }else{
			 model.addAttribute("status", true);
		     model.addAttribute("result", busList);
		 }
		 return JSON;
	 }
 
	   
		/**
		 * 给大众门户提供接口：跟据产品id获取自检、送检、抽检报告
		 * @author wubiao
		 * @param barcode 条形码
		 * @param buId  生产企业 ID
		 * @param product_id 产品ID
		 * @param type 报告类型：{"self","censor","sample"}
		 * @param date 日期：2015.11.13
		 * @return
		 */
		@RequestMapping(method = RequestMethod.GET, value="/getTestResult")
		public View getTestResult(@RequestParam(value="barcode") String barcode,
				@RequestParam(value="buId") long buId,@RequestParam("product_id") long product_id,
				@RequestParam(value="type",required=false) String type,@RequestParam(value="date",required=false) String date,
				HttpServletRequest req, HttpServletResponse resp, Model model){	
			try {
				Element result = null;
				if(("self").equals(type)){
					type = "企业自检";
					result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SELF + "_" + barcode + buId + product_id + date);
				}else if(("censor").equals(type)){
					type = "企业送检";
					result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_CENSOR + "_" + barcode + buId + product_id + date);
				}else if(("sample").equals(type)){
					type = "政府抽检";
					result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SAMPLE + "_" + barcode + buId + product_id + date);
				}
				TestRptJson testRptJson = null;
				
				if(result == null){
						testRptJson = testReportService.getTestResult(barcode,buId,product_id,type,date);
					if(("企业自检").equals(type)){
						EhCacheFactory.put(CACHE_REPORT_SELF + "_" + barcode + buId + product_id + date, testRptJson);
					}else if(("企业送检").equals(type)){
						EhCacheFactory.put(CACHE_REPORT_CENSOR + "_" + barcode + buId + product_id + date , testRptJson);
					}else if(("政府抽检").equals(type)){
						EhCacheFactory.put(CACHE_REPORT_SAMPLE + "_" + barcode + buId + product_id + date, testRptJson);
					}
				}else{
					testRptJson = (TestRptJson) result.getObjectValue();
				}
				if (testRptJson == null){
					model.addAttribute("status", false);
					model.addAttribute("message", "非法操作或者数据缺失！");
				}else{
					model.addAttribute("status", true);
					model.addAttribute("result", testRptJson);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			 return JSON;
		}
		@RequestMapping(method = RequestMethod.GET, value="/getByLicenseNo")
		public View getByLicenseNoTestResult(@RequestParam(value="licenseNo") String licenseNo,
				@RequestParam(value="page",required=true, defaultValue="1") Integer page,@RequestParam(value="pageSize",required=true, defaultValue="10") Integer pageSize,
				@RequestParam(value="type",required=false) String type,HttpServletRequest req, HttpServletResponse resp, Model model){	
			try {
				if("".equals(type)){type = null;}
				List<BusinessTestResultVO> trList = testReportService.getByLicenseNoTistResult(licenseNo,type,page,pageSize);
					model.addAttribute("trList", trList);
					model.addAttribute("status", true);
			} catch (Exception e) {
				model.addAttribute("status", false);
				e.printStackTrace();
			}
			return JSON;
		}
		@RequestMapping(method = RequestMethod.GET, value="/getByLicenseNoProDuct")
		public View getByLicenseNoProduct(@RequestParam(value="licenseNo") String licenseNo,
				@RequestParam(value="page",required=true, defaultValue="1") Integer page,@RequestParam(value="pageSize",required=true, defaultValue="10") Integer pageSize,
				HttpServletRequest req, HttpServletResponse resp, Model model){	
			try {
				
				List<ProductInfoVO> trList = testReportService.getByLicenseNoProduct(licenseNo,page,pageSize);
				Long total = testReportService.getByLicenseNoProductCount(licenseNo);
				model.addAttribute("trList", trList);
				model.addAttribute("total", total);
				model.addAttribute("status", true);
			} catch (Exception e) {
				model.addAttribute("status", false);
				e.printStackTrace();
			}
			return JSON;
		}
		
		@RequestMapping(method = RequestMethod.GET, value="/getFindProduct")
		public View getFindProduct(@RequestParam(value="page",required = true ,defaultValue="1")String page,
				@RequestParam(value="pageSize",required = true ,defaultValue="10") String pageSize, 
				@RequestParam(value = "licenseNo", required = false) String licenseNo,
				@RequestParam(value = "qsNo", required = false) String qsNo,
				@RequestParam(value = "businessName", required = false) String businessName,
				@RequestParam(value = "buslicenseNo", required = false) String buslicenseNo,
				HttpServletRequest req, HttpServletResponse resp, Model model){	
			try {
				List<ProductJGVO> trList = null;
				Long total = 0L;
				BusinessUnit businessUnit=this.businessUnitService.getBusinessUnitByCondition(businessName, qsNo, buslicenseNo);
				if(businessUnit == null){
					trList = new ArrayList<ProductJGVO>();
				}else{
					int start = 1;
					int end = 10;
					//为了防止page乱填,因此做了如下处理(比如:填入的不是数字)
					if(page!=null&&!"".equals(page)){
						try {
							start = Integer.parseInt(page);
						} catch (Exception e) {
							start = 1;
						}
					}
					//为了防止pageSize乱填,因此做了如下处理(比如:填入的不是数字)
					if(pageSize!=null&&!"".equals(pageSize)){
					 try {
							end = Integer.parseInt(pageSize);
						} catch (Exception e) {
							end = 10;
						}
					}
					EnterpriseRegiste enterpriseReg=enterpriseRegisteService.findbyEnterpriteName(businessUnit.getName());
					trList = testReportService.getFindProduct(businessUnit,enterpriseReg.getEnterpriteType(),start,end);
					total = testReportService.getByProductCount(businessUnit,enterpriseReg.getEnterpriteType());
				}
				model.addAttribute("trList", trList);
				model.addAttribute("total", total);
				model.addAttribute("status", true);
			} catch (Exception e) {
				model.addAttribute("status", false);
				e.printStackTrace();
			}
			return JSON;
		}
}
