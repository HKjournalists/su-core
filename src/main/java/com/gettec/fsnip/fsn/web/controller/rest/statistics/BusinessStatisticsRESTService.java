package com.gettec.fsnip.fsn.web.controller.rest.statistics;



import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.statistics.BusinessStatisticsService;
import com.gettec.fsnip.fsn.service.statistics.ProductVisitStatisticsService;
import com.gettec.fsnip.fsn.vo.BusinessStaVO;
import com.gettec.fsnip.fsn.vo.ProductStaVO;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.lhfs.fsn.util.StringUtil;



 /**
 * 
 * 
 * @author lxz
 */
@Controller
@RequestMapping("/businessSta")
public class BusinessStatisticsRESTService extends BaseRESTService{

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(BusinessStatisticsRESTService.class);
	
	/**
	 * 根据条件统计企业发布报告数
	 * @author LongXianZhen
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/businessStas/{page}/{pageSize}")
	public View getBusinessStaListByCondition(@PathVariable(value="page")int page, @RequestParam("businessName") String businessName,
			@RequestParam("businessType") String businessType,@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@PathVariable(value="pageSize") int pageSize,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		    List<BusinessStaVO> businessSta = businessStatisticsService.getbusinessStaListByConfigure(page,pageSize,businessName,businessType,startDate,endDate);
		    Long totalCount =businessStatisticsService.getBusinessStaCountByConfigure(businessName,businessType,startDate,endDate);
		    Map<String,Object> map = new HashMap<String,Object>();
		    map.put("businessStaList", businessSta);
		    map.put("totalCount", totalCount);
		    model.addAttribute("data", map);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 根据条件统计企业下产品发布报告数
	 * @author LongXianZhen
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/productStas/{page}/{pageSize}")
	public View getProductStaListByCondition(@PathVariable(value="page")int page, @RequestParam("businessId") Long businessId,
			@RequestParam("productName") String productName,@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@PathVariable(value="pageSize") int pageSize,@RequestParam("barcode") String barcode,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
//		try {
//			BusinessUnit bu= businessUnitService.findById(businessId);
//		    List<ProductStaVO> productStas = businessStatisticsService.getProductStaListByConfigure(page,pageSize,businessId,productName,barcode,startDate,endDate);
//		    Long totalCount =businessStatisticsService.getProductStaCountByConfigure(businessId,productName,barcode);
//		    Map<String,Object> map = new HashMap<String,Object>();
//		    map.put("productStaList", productStas);
//		    map.put("totalCount", totalCount);
//		    map.put("buName", bu.getName());
//		    model.addAttribute("data", map);
//		}catch(ServiceException sex){
//			resultVO.setStatus(SERVER_STATUS_FAILED);
//			resultVO.setSuccess(false);
//			resultVO.setErrorMessage(sex.getMessage());
//			((Throwable) sex.getException()).printStackTrace();
//		}catch(Exception e){
//			resultVO.setStatus(SERVER_STATUS_FAILED);
//			resultVO.setSuccess(false);
//			e.printStackTrace();
//		}
		/**
		 * 代码优化
		 * authorL:wubiao
		 * 2015.12.17  16:00
		 * 
		 */
	    try {
			List<ProductStaVO> _productStas = businessStatisticsService.getProductStaListByConfigureData(businessId,productName,barcode,startDate,endDate,page,pageSize);
			Long _totalCount =businessStatisticsService.getProductStaCountByConfigureData(businessId,productName,barcode);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("productStaList", _productStas);
			map.put("totalCount", _totalCount);
			map.put("buName", _productStas.size()==0?"":_productStas.get(0).getBusinessName());
			model.addAttribute("data", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/downBusinessExcel/{businessName}/{businessType}/{startDate}/{endDate}")
	public void  downBusinessExcel(@PathVariable("businessName") String businessName,
			@PathVariable("businessType") String businessType,@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate,
			Model model,HttpServletRequest req,HttpServletResponse response) {
		ResultVO resultVO = new ResultVO();
		try {
			if(businessName.equals("null")){
				businessName="";
			}
			if(businessType.equals("null")){
				businessType="";
			}
			if(startDate.equals("null")){
				startDate="";
			}
			if(endDate.equals("null")){
				endDate="";
			}
			 businessName = StringUtil.isBlank(businessName)? "" : StringUtil.getUTF8Code(businessName);
			 businessType = StringUtil.isBlank(businessType)? "" : StringUtil.getUTF8Code(businessType);
			 List<BusinessStaVO> businessSta = businessStatisticsService.getbusinessStaListByConfigure(0,0,businessName,businessType,startDate,endDate);
			 HSSFWorkbook wb= businessStatisticsService.downBusinessExcel(businessSta,businessName,businessType,startDate,endDate);	 
			 response.setContentType("applicationnd.ms-excel");
			 String nameK="企业发布报告统计.xls";
			 String name = new String(nameK.getBytes("utf-8"),"ISO-8859-1");
			 response.setHeader("Content-Disposition", "attachment; filename="+name);
			 OutputStream output = response.getOutputStream();  
		     BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);  
		     bufferedOutPut.flush();  
	         wb.write(bufferedOutPut);  
	         bufferedOutPut.close();  
			 response.flushBuffer();
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			e.printStackTrace();
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/downProductExcel/{productName}/{barcode}/{startDate}/{endDate}/{editBuId}")
	public void  downProductExcel(@PathVariable("productName") String productName,
			@PathVariable("barcode") String barcode,@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate,@PathVariable("editBuId") String buId,
			Model model,HttpServletRequest req,HttpServletResponse response) {
		ResultVO resultVO = new ResultVO();
		try {
			if(productName.equals("null")){
				productName="";
			}
			if(barcode.equals("null")){
				barcode="";
			}
			if(startDate.equals("null")){
				startDate="";
			}
			if(endDate.equals("null")){
				endDate="";
			}
			Long businessId=Long.parseLong(buId.toString());
			productName = StringUtil.isBlank(productName)? "" : StringUtil.getUTF8Code(productName);
			 List<ProductStaVO> productStas = businessStatisticsService.getProductStaListByConfigure(0,0,businessId,productName,barcode,startDate,endDate);
			 HSSFWorkbook wb= businessStatisticsService.downProductExcel(productStas,productName,barcode,startDate,endDate,businessId);	 
			 response.setContentType("applicationnd.ms-excel");
			 String nameK="企业发布报告详细统计.xls";
			 String name = new String(nameK.getBytes("utf-8"),"ISO-8859-1");
			 response.setHeader("Content-Disposition", "attachment; filename="+name);
			 OutputStream output = response.getOutputStream();  
		     BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);  
		     bufferedOutPut.flush();  
	         wb.write(bufferedOutPut);  
	         bufferedOutPut.close();  
			 response.flushBuffer();
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			e.printStackTrace();
		}
	}
	
	@Autowired
	protected ProductVisitStatisticsService productVisitStatisticsService;
	
	@Autowired
	protected BusinessUnitService businessUnitService;
	
	@Autowired
	protected BusinessStatisticsService businessStatisticsService;
}
