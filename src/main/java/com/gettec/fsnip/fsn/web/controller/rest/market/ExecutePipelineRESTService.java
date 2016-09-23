package com.gettec.fsnip.fsn.web.controller.rest.market;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.util.MKReportNOUtils;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.service.testReport.TestReportService;

@Controller
@RequestMapping("/pipeline")
public class ExecutePipelineRESTService {
	@Autowired private TestReportService testReportService;

	/**
	 * 向fsn发布报告
	 * @param id
	 * @param ftpPath
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = {"/getTestResult/{id}"})
	public View getTestResultJson(@PathVariable("id") Long id,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			TestResult tr = testReportService.findById(id);
			if(MKReportNOUtils.hasFullSize(tr.getSample().getProduct().getBarcode())){  // 如果barcode包含中文,则不允许发布
				model.addAttribute("error", "产品条形码不能为中文！发布失败！");
				model.addAttribute("status", SERVER_STATUS_FAILED);
				return JSON;
			}
			/* 判断在页面还未刷新前是否有用户提前发布了该条消息 */
			if(tr.getPublishFlag()=='0' || tr.getPublishFlag()=='1'){
			    model.addAttribute("error", "该条记录已被其他用户发布请您刷新，发布失败！");
			    model.addAttribute("status", SERVER_STATUS_FAILED);
			    return JSON;
			}
			/* 判定此报告的pdf有无签名 */
			Set<Resource> repAttachments = tr.getRepAttachments();
			String pdfUrl = "";
			if(repAttachments.size() == 0){
				model.addAttribute("error", "缺失报告资源！发布失败！");
				model.addAttribute("status", SERVER_STATUS_FAILED);
				return JSON;
			}else if(repAttachments.size() == 1){
				for(Resource rs : repAttachments){
					if(!rs.getUrl().contains(".pdf")){
						model.addAttribute("error", "发布失败！报告资源必须是pdf！");
						model.addAttribute("status", SERVER_STATUS_FAILED);
						return JSON;
					}else{
						pdfUrl = rs.getUrl();
					}
				}
			}else if(repAttachments.size() > 1){
				model.addAttribute("error", "发布失败！请合并多张报告资源为一份pdf后再尝试发布！");
				model.addAttribute("status", SERVER_STATUS_FAILED);
				return JSON;
			}
			/* 如果没有产品图片，会用一张临时图片代替  */
			String tempProductUrl = tr.getSample().getProduct().getImgUrl();
			if(tr.getSample().getProduct().getProAttachments().size()<1){
				tempProductUrl = "http://qa.fsnrec.com/portal/img/product/temp/temp.jpg";
			}
			testReportService.publishToTestLab(tr.getId(), tempProductUrl, info);
		}catch(Exception e){
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
