package com.gettec.fsnip.fsn.web.controller.rest.market;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.MkTempReportItem;
import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.service.market.MkTempReportService;
import com.gettec.fsnip.fsn.service.market.MkTestTemplateService;
import com.gettec.fsnip.fsn.service.test.TestPropertyService;
import com.gettec.fsnip.fsn.util.UtilImportOrExportExcel;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;
import com.gettec.fsnip.fsn.web.IWebUtils;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
@Controller
@RequestMapping("/tempReport")
public class MkTempReportRESTService {
	@Autowired private MkTempReportService  tempReportService;
	@Autowired private TestPropertyService testPropertyService;
	@Autowired private MkTestTemplateService templateService;
	
	
	/**
	 * 功能描述：暂存报告信息
	 * @author ZhangHui 2015/6/9
	 */
	@RequestMapping(method=RequestMethod.POST, value="/saveTempReport")
	public View saveTempReport(
			@RequestBody ReportOfMarketVO report_vo,
			HttpServletRequest req,
			HttpServletResponse resp, 
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long myRealOrgnizationId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			report_vo = tempReportService.save(report_vo, info.getRealUserName(), myRealOrgnizationId);
			
			model.addAttribute("data", report_vo);
		} catch(Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 当前登录用户获取自己的一条临时报告信息
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getTempReport")
	public View getTempReport(HttpServletRequest req,HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();	
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long myRealOrgnizationId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			ReportOfMarketVO report = tempReportService.getTempReport(info.getRealUserName(), myRealOrgnizationId);
			model.addAttribute("data", report);
		} catch(Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：清空临时报告信息
	 */
	@RequestMapping(method=RequestMethod.DELETE, value="/clearTempReport")
	public View clearTempReport(
			HttpServletRequest req, 
			HttpServletResponse resp, 
			Model model){
		
		ResultVO resultVO = new ResultVO();	
	 	try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long myRealOrgnizationId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			tempReportService.clearTemp(info.getRealUserName(), myRealOrgnizationId);
	 	} catch(Exception e) {
	 		e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	 }
	/**
	 * 背景:数据结构化编辑页面
	 * 功能描述：导出检测项目
	 * @author: wubiao 2015/10/16
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getItemList/{page}/{pageSize}/{barcode}/{orderNo}")
	public View getItemList(@PathVariable int page,int pageSize,@PathVariable("barcode") String barcode,@PathVariable("orderNo") String orderNo,
			HttpServletRequest request,
			HttpServletResponse response, 
			Model model) throws IOException{
		AuthenticateInfo info = SSOClientUtil.validUser(request, response);
			try {
				orderNo = URLDecoder.decode(orderNo, "UTF-8");
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				/*首先从test_property表中获取检测项目，如何获取不到检测项目，在去临时表中获取检测项目*/
				ReportOfMarketVO report_vo = templateService.findReportOfMarketByBarCode(barcode, info,currentUserOrganization,page,pageSize);
				List<TestProperty> testList = report_vo.getTestProperties();
				long count = 0l;
				//如果test_property这个表从获取不到数据，这去临时表中获取检测项目
				if(testList.size()==0){
				List<MkTempReportItem> itemList = tempReportService.getMkTempReportItemList(orderNo,info.getRealUserName(),page,pageSize); 
				count = tempReportService.getMkTempReportItemCount(orderNo,info.getRealUserName());
					for (MkTempReportItem e : itemList) {
						TestProperty vo = new TestProperty();
						vo.setId(e.getId());
						vo.setTemporaryFlag(true);
						vo.setName(e.getItemName());
						vo.setUnit(e.getItemUnit());
						vo.setTechIndicator(e.getSpecification());
						vo.setResult(e.getTestResult());
						vo.setAssessment(e.getConclusion());
						vo.setStandard(e.getStandard());
						testList.add(vo);
					}
				}else{
					count = report_vo.getCount();	
				}
				model.addAttribute("itemList", testList);
				model.addAttribute("counts", count);
			} catch (Exception e) {
		       model.addAttribute("status", IWebUtils.SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
		return JSON;
	}
	/**
	 * 功能描述：导出检测项目
	 * @author: wubiao 2015/10/16
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/exportExcel")
	public View exportExcel(@RequestParam String orderNo,@RequestParam String barcode,
			@RequestParam boolean isStructured,@RequestParam Long testResultId,
			HttpServletRequest request,
			HttpServletResponse response, 
			Model model) throws IOException{
		AuthenticateInfo info = SSOClientUtil.validUser(request, response);
		try {
			String filename = "TestProperty.xls";
			String path = request.getSession().getServletContext().getRealPath("/")+"/resource/excelFile/";
			//给数据封装成TreeMap集合;TreeMap分别为excel中（sheet：sheetMap，row：rowMap，col:colMap）
			TreeMap<String,TreeMap<Integer,Map<Integer,Object>>> sheetMap = new TreeMap<String,TreeMap<Integer,Map<Integer,Object>>>();
			//封装行
			TreeMap<Integer,Map<Integer,Object>> rowMap = new TreeMap<Integer,Map<Integer,Object>>();
			//封装列标题
			Map<Integer, Object> colMap = new LinkedHashMap<Integer, Object>();
			colMap.put(0, "检测项目");
			colMap.put(1, "计量单位");
			colMap.put(2, "技术指标");
			colMap.put(3, "检测结果");
			colMap.put(4, "单项评价");
			colMap.put(5, "检测依据");
			rowMap.put(0, colMap);
			int count = 0;
			if(isStructured){
				count = this.getTestPropertyExcel(rowMap,info,barcode,testResultId);
			} else {
				List<MkTempReportItem> itemList = tempReportService.getMkTempReportItemList(orderNo,info.getRealUserName(), 0, 0);
				if (itemList.size() > 0) {
					for (int i = 0; i < itemList.size(); i++) {
						MkTempReportItem item = itemList.get(i);
						Map<Integer, Object> colMaps = new LinkedHashMap<Integer, Object>();
						colMaps.put(0, item.getItemName());
						colMaps.put(1, item.getItemUnit());
						colMaps.put(2, item.getSpecification());
						colMaps.put(3, item.getTestResult());
						colMaps.put(4, item.getConclusion());
						colMaps.put(5, item.getStandard());
						rowMap.put(i + 1, colMaps);
						count++;
					}
				} else {
					count = this.getTestPropertyExcel(rowMap,info,barcode,testResultId);
				}
			}
			sheetMap.put("Sheet1", rowMap);
			String filePath = UtilImportOrExportExcel.exportFile(sheetMap,path,filename,response);
			model.addAttribute("filePath", filePath);
			if(count > 0){
				model.addAttribute("status", IWebUtils.SERVER_STATUS_SUCCESS);
			}else{
				model.addAttribute("status", IWebUtils.SERVER_STATUS_FAILED);
			}
		} catch (Exception e) {
			model.addAttribute("status", IWebUtils.SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		return JSON;
	}
    /**
     * 查询真实数据库表，而不是查出临时表数据
     * @param rowMap 组装map数据集合
     * @param info
     * @param barcode
     * @return
     */
	private int getTestPropertyExcel(TreeMap<Integer,Map<Integer,Object>> rowMap, AuthenticateInfo info, String barcode,Long testResultId) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		int count = 0 ;
		try {
			List<TestProperty> testList =  new ArrayList<TestProperty>();
			if (testResultId != null && !"".equals(testResultId + "")) {
			  testList = testPropertyService.findByReportId(testResultId);
			}
			if(testList.size()==0){
				ReportOfMarketVO report_vo = templateService.findReportOfMarketByBarCode(barcode, info,currentUserOrganization,0,0);
				testList = report_vo.getTestProperties(); 
			}
			for (int i = 0; i < testList.size(); i++) {
				TestProperty item = testList.get(i); 
				Map<Integer, Object> colMaps = new LinkedHashMap<Integer, Object>();
				colMaps.put(0, item.getName());
				colMaps.put(1, item.getUnit());
				colMaps.put(2, item.getTechIndicator());
				colMaps.put(3, item.getResult());
				colMaps.put(4, item.getAssessment());
				colMaps.put(5, item.getStandard());
				rowMap.put(i+1, colMaps);
				count++;
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return count;
	}

	@RequestMapping(method=RequestMethod.GET, value="/downLoadExcel")
	public void downLoadExcel(HttpServletRequest request,HttpServletResponse response, Model model){
		try{
			String path = request.getSession().getServletContext().getRealPath("/")+"/resource/excelFile/";
			InputStream is=new FileInputStream(path+"/TestProperty.xls");
			String namek="检测项目信息.xls";
			String name = new String(namek.getBytes("utf-8"),"ISO-8859-1");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+name);
			if(is!=null){
				FileCopyUtils.copy(is, response.getOutputStream());
			}
			response.flushBuffer();
		}catch(Exception e){
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	/**
	 * 背景:数据结构化编辑页面
	 * 功能描述：导出检测项目
	 * @author: wubiao 2015/10/16
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}/{flag}")
	public View delete(@PathVariable("id") long id,@PathVariable("flag") boolean flag,
			HttpServletRequest request,
			HttpServletResponse response, 
			Model model){
			try {
				boolean success = false;
				if(flag){
					success = tempReportService.deleteMkTempReportItem(id);
				}else{
					success = testPropertyService.deleteTestProperty(id);
				}
				model.addAttribute("status", success);
			} catch (Exception e) {
		       model.addAttribute("status", IWebUtils.SERVER_STATUS_FAILED);
				e.printStackTrace();
			}
		return JSON;
	}
}
