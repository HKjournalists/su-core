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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.statistics.ProductVisitStatistics;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.statistics.ProductVisitStatisticsService;
import com.gettec.fsnip.fsn.service.test.TestResultService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;

/**
 * 
 * @author lxz
 */
@Controller
@RequestMapping("/proViSta")
public class ProductVisitRESTService extends BaseRESTService{
	@Autowired private ProductVisitStatisticsService productVisitStatisticsService;
	@Autowired private TestResultService testResultService;
	
	/**
	 * 获取该产品访问量列表 有分页处理
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param productId
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/proViStas/{page}/{pageSize}/{configure}")
	public View getAllProViStaList(@PathVariable(value="page")int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		    List<ProductVisitStatistics> productSales = productVisitStatisticsService.getAllProViStaListByPage(page,pageSize,configure);
		    Long totalCount = productVisitStatisticsService.getCount(configure);
		    Map<String,Object> map = new HashMap<String,Object>();
		    map.put("proViStaList", productSales);
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
	 * 根据过滤条件查询产品报告
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/proReport/{page}/{pageSize}/{configure}")
	public View getProReportListByConfigure(@PathVariable(value="page")int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		    List<TestResult> tes = testResultService.getProReportListByConfigure(page,pageSize,configure);
		    Long totalCount = testResultService.getCountProReport(configure);
		    Map<String,Object> map = new HashMap<String,Object>();
		    map.put("proReportList", tes);
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
	 * 根据过滤条件查询产品报告并下载成excel
	 * @param configure
	 * @param model
	 * @param req
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/downExcel/{configure}")
	public void  downProductExcel( @PathVariable(value="configure") String configure,
			Model model,HttpServletRequest req,HttpServletResponse response) {
		ResultVO resultVO = new ResultVO();
		try {
			List<TestResult> tes = testResultService.getProReportListByConfigure(0,0,configure);
			 HSSFWorkbook wb= productVisitStatisticsService.downExcel(tes,configure);	 
			 response.setContentType("applicationnd.ms-excel");
			 String nameK="产品报告查询.xls";
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
}
