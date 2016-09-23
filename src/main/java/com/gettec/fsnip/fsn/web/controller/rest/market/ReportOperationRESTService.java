package com.gettec.fsnip.fsn.web.controller.rest.market;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PATH;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.util.MKReportNOUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.sso.client.util.ReportUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.service.testReport.TestReportService;

@Controller
@RequestMapping("/archive")
public class ReportOperationRESTService {
	@Autowired private TestReportService testReportService;
	
	private static final Logger LOG = Logger.getLogger(ReportOperationRESTService.class);
	private static final String INVALID_REPORT = "无效报告";
	private static final String NO_RESOURCE_REPORT = "无报告资源";
	private static final String FAILE_UPLOADE = "pdf未上传";
	
	/**
	 * Sign Report
	 * @param todo
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = {"/sign/{id}"})
	public void sign(@PathVariable("id") Long id, HttpServletRequest req,HttpServletResponse resp, Model model){	
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			if(info != null){
				TestResult orig_report = testReportService.findById(id);
				String reportNoEng =  MKReportNOUtils.convertCharacter(orig_report.getServiceOrder(), orig_report.getUploadPath());
				/* 1. 确保该报告的pdf只有一个 */
				String service = "/service/report/sign";
				String response = INVALID_REPORT;
				Resource res;
				if (orig_report.getRepAttachments() != null && orig_report.getRepAttachments().size() == 1){
					res = orig_report.getRepAttachments().iterator().next();
					/* 2. 签名 */
					UploadUtil uploadUtil = new UploadUtil();
					String path=null;
					if(UploadUtil.IsOss()){
						try{
							URL url=new URL(res.getUrl());
							path=url.getPath().substring(1);
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						path = "/http" + res.getUrl().replace(PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PATH), "");
					}
					InputStream in = uploadUtil.downloadFileStream(path);
					if(null != in){
						StringBuffer callbackURL = new StringBuffer();
						callbackURL.append(req.getScheme())
								.append("://")
								.append(req.getServerName())
								.append(":")
								.append(req.getServerPort())
								.append(req.getContextPath())
								.append("/service/archive/download/" + id + "/" + URLEncoder.encode(reportNoEng, "UTF-8") + "/" +  URLEncoder.encode(reportNoEng, "UTF-8") + "/" + res.getId());
						
						response = ReportUtils.signReport(service, info.getUserId().toString(), URLEncoder.encode(reportNoEng, "UTF-8"), URLEncoder.encode(reportNoEng, "UTF-8"), in, callbackURL.toString(),true,false,req.getHeader("User-Agent"));
					}
				}else{
					response = NO_RESOURCE_REPORT;
					LOG.error("Invalid pdf reports:" + id);
				}
				byte[] buffer = null;
				if(response != null && (NO_RESOURCE_REPORT.equals(response) || INVALID_REPORT.equals(response))){
					buffer = response.getBytes("GBK");
				}else if(response != null){
					buffer = response.getBytes("UTF-8");
				}
				resp.getOutputStream().write(response != null ? buffer : "User Managerment Error: Return null".getBytes("GBK"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 2. DownLoad
	 * @param todo
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = {"/download/{id}/{reportNo}/{reportNOEng}/{resId}"})
	public void download(@PathVariable("id") Long id,
			@PathVariable("reportNo") String reportNo,
			@PathVariable("reportNOEng") String reportNOEng,
			@PathVariable("resId") Long resId,
			HttpServletRequest req,HttpServletResponse resp, Model model){	
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			if(info != null){
				String service = "/service/report/" + reportNo;
				byte[] pdfBytes = ReportUtils.downloadReport(service);
				if(pdfBytes.length==0 || pdfBytes==null){
					resp.getOutputStream().write(FAILE_UPLOADE.getBytes("GBK"));
					resp.flushBuffer();
					return;
				}
				ByteArrayInputStream input = new ByteArrayInputStream(pdfBytes);
				testReportService.saveAfterSign(id, input);
			}
			resp.sendRedirect("/fsn-core/views/market/publish_testreport.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}