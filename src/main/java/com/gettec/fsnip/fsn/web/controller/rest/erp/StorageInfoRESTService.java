package com.gettec.fsnip.fsn.web.controller.rest.erp;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.service.erp.StorageInfoService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/storage")
public class StorageInfoRESTService {

	@Autowired
	private StorageInfoService storageInfoService;
	
	/**
	 * 新增仓库信息
	 * @param req
	 * @param resp
	 * @param info
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"","/"}, method = RequestMethod.POST)
	public View add(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody StorageInfo info, Model model){
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		try {
			info.setOrganization(currentUserOrganization);
			boolean result = storageInfoService.add(info, currentUserOrganization);
			if(result) {
				resultVO.setMessage("添加成功!");
			} else {
				resultVO.setErrorMessage("仓库名称已经存在！");
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
		} catch (ServiceException e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 更新仓库信息
	 * @param req
	 * @param resp
	 * @param storageInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"","/"}, method = RequestMethod.PUT)
	public View update(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody StorageInfo storageInfo, Model model){
		AuthenticateInfo info = SSOClientUtil.validUser(req,resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		try {
			info.setOrganization(currentUserOrganization);
			boolean success = storageInfoService.update(storageInfo, currentUserOrganization);
			if(!success) {
				resultVO.setErrorMessage("该名称已存在，更新失败！");
				resultVO.setShow(true);
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
		} catch (ServiceException sex) {
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setErrorMessage(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 删除仓库信息
	 * @param req
	 * @param resp
	 * @param storageInfo
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value={"","/"}, method = RequestMethod.DELETE)
	public View delete(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody StorageInfo storageInfo, Model model){
		AuthenticateInfo info = SSOClientUtil.validUser(req,resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		try {
			storageInfoService.remove(storageInfo, currentUserOrganization);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage("该仓库已被使用，无法删除！");
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 查找仓库信息
	 * @param req
	 * @param resp
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/list"}, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", storageInfoService.getPaging(page, pageSize, null, currentUserOrganization));
		} catch (ServiceException sex) {
			((Throwable)sex.getException()).printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value={"/lists"}, method=RequestMethod.GET)
	public View getAll(HttpServletRequest req, HttpServletResponse resp,
			 Model model){
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", storageInfoService.getAll(currentUserOrganization).getListOfModel());
			model.addAttribute("count", storageInfoService.getAll(currentUserOrganization).getListOfModel().size());
		} catch (ServiceException sex) {
			((Throwable)sex.getException()).printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value={"/search"}, method = RequestMethod.GET)
	public View searchKeywords(HttpServletRequest req, HttpServletResponse resp,@RequestParam String keywords,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", storageInfoService.getPaging(page, pageSize, keywords, currentUserOrganization));
		} catch (ServiceException sex) {
			((Throwable)sex.getException()).printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(method=RequestMethod.GET,value={"/reglist/{page}/{pageSize}/{configure}"})
	public View getReqList(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize, 
			@PathVariable(value="configure") String configure, 
			Model model){
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", storageInfoService.getStorageInfofilter(page, pageSize, configure, currentUserOrganization));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	/**
	 * 判断仓库是否被使用
	 * @param req
	 * @param resp
	 * @param info
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/judgeIsUsed"}, method = RequestMethod.POST)
	public View judgeIsUsed(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody StorageInfo storageInfo, Model model){
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		boolean flag = true;
		try {
			flag = storageInfoService.judgeIsUsed(storageInfo, currentUserOrganization);
		} catch (ServiceException sex) {
			flag = false;
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("flag",flag);
		return JSON;
	}
}
