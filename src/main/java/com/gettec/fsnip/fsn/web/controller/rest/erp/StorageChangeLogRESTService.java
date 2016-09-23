package com.gettec.fsnip.fsn.web.controller.rest.erp;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.erp.buss.StorageChangeLog;
import com.gettec.fsnip.fsn.service.erp.buss.StorageChangeLogService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/storagelog")
public class StorageChangeLogRESTService {

	@Autowired
	private StorageChangeLogService storageChangeLogService;
	
	private static final Logger LOG = Logger.getLogger(StorageChangeLogRESTService.class);
	
	@RequestMapping(value={"/list"}, method=RequestMethod.GET)
	public View getLog(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			PagingSimpleModelVO<StorageChangeLog> result = storageChangeLogService.getStorageLogs(page, pageSize,null,currentUserOrganization);
			model.addAttribute("result", result);
		} catch (Exception e) {
			LOG.error("getLog()");
		}
		return JSON;
	}
	
	@RequestMapping(value={"/search"}, method=RequestMethod.GET)
	public View searchKeywords(HttpServletRequest req, HttpServletResponse resp,@RequestParam String keywords,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			PagingSimpleModelVO<StorageChangeLog> result = storageChangeLogService.getStorageLogs(page, pageSize,keywords, currentUserOrganization);
			model.addAttribute("result", result);
		} catch (Exception e) {
			LOG.error("getLog()");
		}
		return JSON;
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/reglist/{page}/{pageSize}/{configure}"})
	public View getReqList(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable(value="page") int page, @PathVariable(value="pageSize") int pageSize, 
			@PathVariable(value="configure") String configure, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			model.addAttribute("result", storageChangeLogService.getStorageChangeLogfilter(page, pageSize, configure, currentUserOrganization));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
}
