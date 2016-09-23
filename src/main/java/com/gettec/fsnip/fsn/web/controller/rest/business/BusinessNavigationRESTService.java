package com.gettec.fsnip.fsn.web.controller.rest.business;


import java.util.List;


import com.gettec.fsnip.fsn.model.business.NavigationAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessNavigation;
import com.gettec.fsnip.fsn.service.business.BusinessNavigationService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

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
			int addressId = 0;
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long businessId = businessUnitService.findIdByOrg(currentUserOrganization);
			List<BusinessNavigation> navigationList = navigationService.getNavigationList(businessId);

			addressId = navigationList.size() + 1;

			navigation.setOrganization(currentUserOrganization);
			navigation.setAddressId(addressId);
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
	 * 拖动修改导航位置
	 */
	@RequestMapping(method = RequestMethod.PUT,value = "/updateAddress")
	public View updateAddress(@RequestBody NavigationAddress  navigationData,Model model){

		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long businessId = businessUnitService.findIdByOrg(currentUserOrganization);
			navigationService.updateNavigationList(navigationData);

//			List<BusinessNavigation> navigationList = navigationService.getNavigationList(businessId);
//			for (BusinessNavigation businessNavigation : navigationList) {
//				long oldNavigationId = businessNavigation.getId();
//				long newNavigationId = addresses.getNavigationId();
//				int oldAddressId = businessNavigation.getAddressId();
//				int newAddressId = addresses.getAddressId();
//				if (oldNavigationId == newNavigationId && oldAddressId != newAddressId) {
//					businessNavigation.setAddressId(newAddressId);
//				}
//				navigationService.update(businessNavigation);
			model.addAttribute("status",true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return JSON;
	}




	/**
	 * 删除快速导航
	 * @param id
	 * @param model
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
