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
import com.gettec.fsnip.fsn.enums.SimpleModelTypeEnums;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.service.erp.BusinessTypeService;
import com.gettec.fsnip.fsn.service.erp.CustomerTypeService;
import com.gettec.fsnip.fsn.service.erp.InStorageTypeService;
import com.gettec.fsnip.fsn.service.erp.MerchandiseCategoryService;
import com.gettec.fsnip.fsn.service.erp.MerchandiseTypeService;
import com.gettec.fsnip.fsn.service.erp.OutStorageTypeService;
import com.gettec.fsnip.fsn.service.erp.ProviderTypeService;
import com.gettec.fsnip.fsn.service.erp.UnitService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/model/{type}")
public class SimpleModelRestService {
	@Autowired private BusinessTypeService businessTypeService;
	@Autowired private MerchandiseTypeService merchandiseTypeService;
	@Autowired private CustomerTypeService customerTypeService;
	@Autowired private UnitService unitService;
	@Autowired private ProviderTypeService providerTypeService;
	@Autowired private InStorageTypeService inStorageTypeService;
	@Autowired private OutStorageTypeService outStorageTypeService;
	@Autowired private MerchandiseCategoryService merchandiseCategoryService;

	private SimpleModelTypeEnums getType(int type) {
		return SimpleModelTypeEnums.getModelTypeByType(type);
	}

	/**
	 * 新增基础模块
	 * @param request
	 * @param response
	 * @param vo
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.POST)
	public View saveSimpleModel(HttpServletRequest request,
			HttpServletResponse response, @RequestBody SimpleModelVO vo,
			@PathVariable int type, Model model) {
		ResultVO resultVO = new ResultVO();
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		boolean success = true;
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE: // 客户类型
				success = customerTypeService.add(vo, currentUserOrganization, 1);
				break;
			case PROVIDER_TYPE: // 供应商类型
				success = customerTypeService.add(vo, currentUserOrganization, 2);
				break;
			case MERCHANDISE_TYPE: // 商品类型
				success = merchandiseTypeService.add(vo, currentUserOrganization);
				break;
			case IN_STORAGE_TYPE: // 入库类型
				success = inStorageTypeService.add(vo, currentUserOrganization);
				break;
			case OUT_STORAGE_TYPE: // 出库类型
				success = outStorageTypeService.add(vo, currentUserOrganization);
				break;
			default:
				break;
			}
			if(!success) {
				resultVO.setErrorMessage("该名称已存在，请重新输入！");
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setShow(true);
			}
		} catch (ServiceException e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 更新基础模块
	 * @param request
	 * @param response
	 * @param vo
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.PUT)
	public View updateSimpleModel(HttpServletRequest request,
			HttpServletResponse response, @RequestBody SimpleModelVO vo,
			@PathVariable int type, Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		boolean success = true;
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:  // 客户类型
				success = customerTypeService.updateCustomerType(vo, currentUserOrganization);
				break;
			case PROVIDER_TYPE:  // 供应商类型
				success = providerTypeService.updateProviderType(vo, currentUserOrganization);
				break;
			case MERCHANDISE_TYPE:  // 商品类型
				success = merchandiseTypeService.updateMerchandiseType(vo, currentUserOrganization);
				break;
			case IN_STORAGE_TYPE:  // 入库类型
				success = inStorageTypeService.updateInStorageType(vo, currentUserOrganization);
				break;
			case OUT_STORAGE_TYPE:  // 出库类型
				success = outStorageTypeService.updateOutStorageType(vo, currentUserOrganization);
				break;
			default:
				break;
			}
			if(!success) {
				resultVO.setErrorMessage("该名称已存在，更新失败！");
				resultVO.setShow(true);
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 删除基本模块
	 * @param request
	 * @param response
	 * @param vo
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.DELETE)
	public View deleteSimpleModel(HttpServletRequest request,
			HttpServletResponse response, @RequestBody SimpleModelVO vo,
			@PathVariable int type, Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:  // 客户类型
				customerTypeService.remove(vo.getId(), currentUserOrganization);
				break;
			case PROVIDER_TYPE:  // 供应商类型
				providerTypeService.remove(vo.getId(), currentUserOrganization);
				break;
			case MERCHANDISE_TYPE: // 商品类型
				merchandiseTypeService.remove(vo.getId(), currentUserOrganization);
				break;
			case IN_STORAGE_TYPE: // 入库类型
				inStorageTypeService.remove(vo.getId(), currentUserOrganization);
				break;
			case OUT_STORAGE_TYPE: // 出库类型
				outStorageTypeService.remove(vo.getId(), currentUserOrganization);
				break;
			default:
				break;
			}
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, @RequestParam int page,
			@RequestParam int pageSize, Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			switch (getType(type)) {
			case BUSINESS_TYPE:
				model.addAttribute("result", businessTypeService.getPaging(
						page, pageSize, null, currentUserOrganization));
				break;
			case CUSTOMER_TYPE:
				model.addAttribute("result", customerTypeService.getPaging(
						page, pageSize, null, currentUserOrganization));
				break;
			case IN_STORAGE_TYPE:
				model.addAttribute("result", inStorageTypeService.getPaging(
						page, pageSize, null, currentUserOrganization));
				break;
			case OUT_STORAGE_TYPE:
				model.addAttribute("result", outStorageTypeService.getPaging(
						page, pageSize, null, currentUserOrganization));
				break;
			case MERCHANDISE_TYPE:
				model.addAttribute("result", merchandiseTypeService.getPaging(
						page, pageSize, null, currentUserOrganization));
				break;
			case PROVIDER_TYPE:
				model.addAttribute("result", providerTypeService.getPaging(
						page, pageSize, null, currentUserOrganization));
				break;
			case MERCHANDISE_CATEGORY:
				model.addAttribute("result", merchandiseCategoryService
						.getPaging(page, pageSize, null, currentUserOrganization));
				break;
			default:
				model.addAttribute("result", unitService.getPaging(page,
						pageSize, null, currentUserOrganization));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	@RequestMapping(value = { "/lists" }, method = RequestMethod.GET)
	public View getAll(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());

		try {
			switch (getType(type)) {
			case BUSINESS_TYPE:
				model.addAttribute("result",
						businessTypeService.getAll(currentUserOrganization));
				break;
			case CUSTOMER_TYPE:
				model.addAttribute("result",
						customerTypeService.getAll(currentUserOrganization));
				break;
			case IN_STORAGE_TYPE:
				model.addAttribute("result",
						inStorageTypeService.getAll(currentUserOrganization));
				break;
			case OUT_STORAGE_TYPE:
				model.addAttribute("result",
						outStorageTypeService.getAll(currentUserOrganization));
				break;
			case MERCHANDISE_TYPE:
				model.addAttribute("result",
						merchandiseTypeService.getAll(currentUserOrganization));
				break;
			case PROVIDER_TYPE:
				model.addAttribute("result",
						providerTypeService.getAll(currentUserOrganization));
				break;
			case MERCHANDISE_CATEGORY:
				model.addAttribute("result",
						merchandiseCategoryService.getAll(currentUserOrganization));
				break;
			default:
				model.addAttribute("result", unitService.getAll(currentUserOrganization));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return JSON;
	}

	// begin filter cgw
	@RequestMapping(method = RequestMethod.GET, value = { "/reglist/{page}/{pageSize}/{configure}" })
	public View getReqList(HttpServletRequest req,
			HttpServletResponse response,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			@PathVariable int type, Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, response);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());

		try {
			switch (getType(type)) {
			case BUSINESS_TYPE:
				model.addAttribute("result", businessTypeService
						.getBusinessTypefilter(page, pageSize, configure,
								currentUserOrganization));
				break;
			case CUSTOMER_TYPE:
				model.addAttribute("result", customerTypeService
						.getCustomerTypefilter(page, pageSize, configure,
								currentUserOrganization));
				break;
			case IN_STORAGE_TYPE:
				model.addAttribute("result", inStorageTypeService
						.getInStorageTypefilter(page, pageSize, configure,
								currentUserOrganization));
				break;
			case OUT_STORAGE_TYPE:
				model.addAttribute("result", outStorageTypeService
						.getOutStorageTypefilter(page, pageSize, configure,
								currentUserOrganization));
				break;
			case MERCHANDISE_TYPE:
				model.addAttribute("result", merchandiseTypeService
						.getMerchandiseTypefilter(page, pageSize, configure,
								currentUserOrganization));
				break;
			case PROVIDER_TYPE:
				model.addAttribute("result", providerTypeService
						.getProviderTypefilter(page, pageSize, configure,
								currentUserOrganization));
				break;
			case MERCHANDISE_CATEGORY:
				model.addAttribute("result", merchandiseCategoryService
						.getMerchandiseCategoryfilter(page, pageSize,
								configure, currentUserOrganization));
				break;
			default:
//				model.addAttribute("result", unitService.getUnitfilter(page,
//						pageSize, configure, organization));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	// end filter cgw

	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public View searchKeywords(HttpServletRequest req,
			HttpServletResponse resp, @RequestParam String keywords,
			@PathVariable int type, @RequestParam int page,
			@RequestParam int pageSize, Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());

		try {
			switch (getType(type)) {
			case BUSINESS_TYPE:
				model.addAttribute("result", businessTypeService.getPaging(
						page, pageSize, keywords, currentUserOrganization));
				break;
			case CUSTOMER_TYPE:
				model.addAttribute("result", customerTypeService.getPaging(
						page, pageSize, keywords, currentUserOrganization));
				break;
			case IN_STORAGE_TYPE:
				model.addAttribute("result", inStorageTypeService.getPaging(
						page, pageSize, keywords, currentUserOrganization));
				break;
			case OUT_STORAGE_TYPE:
				model.addAttribute("result", outStorageTypeService.getPaging(
						page, pageSize, keywords, currentUserOrganization));
				break;
			case MERCHANDISE_TYPE:
				model.addAttribute("result", merchandiseTypeService.getPaging(
						page, pageSize, keywords, currentUserOrganization));
				break;
			case PROVIDER_TYPE:
				model.addAttribute("result", providerTypeService.getPaging(
						page, pageSize, keywords, currentUserOrganization));
				break;
			case MERCHANDISE_CATEGORY:
				model.addAttribute("result", merchandiseCategoryService
						.getPaging(page, pageSize, keywords, currentUserOrganization));
				break;
			default:
//				model.addAttribute("result", unitService.getPaging(page,
//						pageSize, keywords, organization));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 页面删除之前，先判断是否被使用
	 * @param request
	 * @param response
	 * @param vo
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/judgeIsUsed" }, method = RequestMethod.POST)
	public View judgeIsUsed(HttpServletRequest request,
			HttpServletResponse response, @RequestBody SimpleModelVO vo,
			@PathVariable int type, Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(request, response);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		boolean flag = true;
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:  // 客户类型
				flag = customerTypeService.judgeIsUsed(vo.getId(), currentUserOrganization);
				break;
			case PROVIDER_TYPE:	 // 供应商类型
				flag = providerTypeService.judgeIsUsed(vo.getId(), currentUserOrganization);
				break;
			case MERCHANDISE_TYPE: // 商品类型
				flag = merchandiseTypeService.judgeIsUsed(vo.getId(), currentUserOrganization);
				break;
			case IN_STORAGE_TYPE: // 入库类型
				flag = inStorageTypeService.judgeIsUsed(vo.getId(), currentUserOrganization);
				break;
			case OUT_STORAGE_TYPE:  // 出库类型
				flag = outStorageTypeService.judgeIsUsed(vo.getId(), currentUserOrganization);
				break;
			default:
				break;
			}
		} catch (ServiceException sex) {
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("flag", flag);
		return JSON;
	}
}
