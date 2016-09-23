package com.gettec.fsnip.fsn.web.controller.rest.thirdreport;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.thirdReport.Thirdreport;
import com.gettec.fsnip.fsn.service.thirdreport.ThirdReportService;
import com.gettec.fsnip.fsn.vo.thirdreport.ThirdreportVo;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@RequestMapping(value = "/thirdReport")
@Controller
public class ThirdReportRESTService {

	@Autowired private ThirdReportService thirdReportService;
	
	@RequestMapping(method = RequestMethod.GET,value = "/searchReportNo")
	public View searchReportNo(HttpServletRequest req,HttpServletResponse resp, Model model){
		
//		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		long currrntUserId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		List<ThirdreportVo> reportNoList = thirdReportService.getReportNo(currrntUserId);
	
		model.addAttribute("status", 1);
		model.addAttribute("data", reportNoList);
		
		
		return JSON;
	}
	
	
	//获取随机抽样的报告数量,
	@RequestMapping(method = RequestMethod.GET,value = "/getReportCount")
	public View getReportCount(HttpServletRequest req,HttpServletResponse resp, Model model){
		String testType = "第三方检测";
		long currrntUserId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		List<Long> count = thirdReportService.getReportCounts(currrntUserId,testType);
		
		model.addAttribute("reportCounts", count);
		
		return JSON;
	}
	
	
	//获取抽检列表，获取第三方检测报告的列表
	@RequestMapping(method = RequestMethod.GET,value = "/getReportDetail")
	public View getReportDetail(HttpServletRequest req,HttpServletResponse resp, Model model){
		String testType = "第三方检测";
		long currrntUserId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		List<TestResult> testResults = thirdReportService.getReportDetail(currrntUserId,testType);
		long reportCount = thirdReportService.getReportCount(currrntUserId, testType);
		
		model.addAttribute("reportCount",reportCount);
		for (TestResult testResult : testResults) {
			model.addAttribute("createTime", testResult.getCreate_time());
			model.addAttribute("status", testResult.getStatus());
		}
		
		return JSON;
	}
	
	
	@RequestMapping(method = RequestMethod.POST,value = "/save")
	public View save(@RequestBody Thirdreport thirdreport,HttpServletRequest req,
			HttpServletResponse resp, Model model) throws Exception{
		
		try {
			thirdReportService.save(thirdreport);
			model.addAttribute("status", true);
		} catch (Exception e) {
			model.addAttribute("status", false);
			e.printStackTrace();
		}
		return JSON;
		
	}
	

	
//	public void fileUpload(InputStream in, String filename, String upload_path)  
//	        throws FileNotFoundException, IOException {  
//	    File uploadFolder = new File(upload_path);  
//	    if (!uploadFolder.exists()) {  
//	        uploadFolder.mkdir();  
//	    }  
//	    File uploadFile = new File(uploadFolder + "/" + filename);  
//	    OutputStream out = new FileOutputStream(uploadFile);  
//	    byte[] buffer = new byte[1024 * 1024];  
//	    int length;  
//	    while ((length = in.read(buffer)) > 0) {  
//	        out.write(buffer, 0, length);  
//	    }  
//	    in.close();  
//	    out.close();  
//	}  
	
	
	//获取报告所执行的检验标准
	@RequestMapping(method = RequestMethod.GET, value = "/getStandards")
	public View getStandards(HttpServletRequest req,
			HttpServletResponse resp, Model model){
		long currrntUserId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		List<String > standards = thirdReportService.getStandards(currrntUserId);
		
		model.addAttribute("standards", standards);
		
		return JSON;
	}
	
	
	
	
	
	
	
}
