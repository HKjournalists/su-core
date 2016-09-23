package com.gettec.fsnip.fsn.web.controller.rest.business;


import java.util.List;

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
import com.gettec.fsnip.fsn.model.business.BusinessNavigation;
import com.gettec.fsnip.fsn.service.business.BusinessNavigationService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

@Controller
@RequestMapping("/business")
public class BusinessNavigationRESTService extends BaseRESTService{

	@Autowired
	private BusinessNavigationService navigationService;
	@Autowired 
	private BusinessUnitService businessUnitService;

	/**
	 * 获取用户已添加的快速导航
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/getNavigationList")
	public View getNavigationList(Model model){
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long businessId = businessUnitService.findIdByOrg(currentUserOrganization);
			List<BusinessNavigation> navigationList = navigationService.getNavigationList(businessId);
			model.addAttribute("navigationList", navigationList);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} 
		
		return JSON;
	}
	

	/**
	 * 添加快速导航
	 * @param navigation
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/addNavigation")
	public View addNavigation(@RequestBody BusinessNavigation navigation,Model model){
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			navigation.setOrganization(currentUserOrganization);
			if (navigation != null) {
				BusinessNavigation newNavigation = navigationService.create(navigation);
				model.addAttribute("newNavigation", newNavigation);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		return JSON;
	}


	/**
	 * 删除快速导航
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE,value = "/deleteNavigation/{id}")
	public View deleteNavigation(@PathVariable("id") long id,Model model){
		try {
			navigationService.delete(id);
			model.addAttribute("data", true);
		} catch (ServiceException e) {
			model.addAttribute("data", false);
			e.printStackTrace();
		}
		return JSON;
	}
	
	
}
