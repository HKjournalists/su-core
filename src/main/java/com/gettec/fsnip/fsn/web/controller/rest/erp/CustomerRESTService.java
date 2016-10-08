package com.gettec.fsnip.fsn.web.controller.rest.erp;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gettec.fsnip.fsn.vo.business.AccountBusinessVO;
import com.lhfs.fsn.vo.business.LightBusUnitVO;
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
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.PurchaseorderInfo;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.erp.AddressService;
import com.gettec.fsnip.fsn.service.erp.ContactInfoService;
import com.gettec.fsnip.fsn.service.erp.CustomerService;
import com.gettec.fsnip.fsn.service.erp.FromToBussinessService;
import com.gettec.fsnip.fsn.service.erp.OrderTypeService;
import com.gettec.fsnip.fsn.service.erp.OutGoodsInfoService;
import com.gettec.fsnip.fsn.service.erp.OutOfBillService;
import com.gettec.fsnip.fsn.service.erp.ProviderService;
import com.gettec.fsnip.fsn.service.erp.PurchaseorderInfoService;
import com.gettec.fsnip.fsn.service.erp.ReceivingNoteService;
import com.gettec.fsnip.fsn.service.erp.StorageInfoService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.CustomerVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/customer/{type}")
public class CustomerRESTService {
	@Autowired private CustomerService customerService;
	@Autowired private ProviderService providerService;
	@Autowired private OrderTypeService orderTypeService;
	@Autowired private ContactInfoService contactInfoService;
	@Autowired private ReceivingNoteService receivingNoteService;
	@Autowired private OutOfBillService outOfBillService;
	@Autowired private OutGoodsInfoService outGoodsInfoService;
	@Autowired private AddressService addressService;
	@Autowired private StorageInfoService storageInfoService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private PurchaseorderInfoService purchaseorderInfoService;
	@Autowired private FromToBussinessService fromToBusService;

	private SimpleModelTypeEnums getType(int type) {
		return SimpleModelTypeEnums.getModelTypeByType(type);
	}

	/**
	 * 新增客户/供应商/单别/收货单
	 * @param req
	 * @param resp
	 * @param type
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "" }, method = RequestMethod.POST)
	public View save(HttpServletRequest req, HttpServletResponse resp,@PathVariable int type, @RequestBody CustomerVO vo, Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		resultVO.setObject(vo.getBusinessUnit());
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE: // 新增客户
				resultVO = customerService.add(resultVO,currentUserOrganization);
				/**
				 * 自动为该客户新增一条供应商信息
				 * @author ZhangHui 2015/4/13
				 */
				if("true".equals(resultVO.getStatus())) {
					BusinessUnit customer = (BusinessUnit)resultVO.getObject();
					Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
					customerService.addProviderRelation(customer,fromBusId);
				}
				break;
			case PROVIDER_TYPE: // 新增供应商
				resultVO = providerService.add(resultVO,currentUserOrganization);
				break;
			case ORDER_TYPE: // 新增单别
				vo.getOrdertype().setOrganization(currentUserOrganization);
				boolean success = orderTypeService.add(vo.getOrdertype(), currentUserOrganization); 
				if(success) {
					resultVO.setMessage("添加成功");
				} else {
					resultVO.setErrorMessage("相同的所属模块和所属单据，单别名称不能相同");
					resultVO.setStatus(SERVER_STATUS_FAILED);
				}
				break;
			case RECEIVE_TYPE:  // 新增收货单
				vo.getReceivingNote().setOrganization(currentUserOrganization);
				vo.getReceivingNote().setUserName(info.getUserName());
				receivingNoteService.add(vo.getReceivingNote(),
						currentUserOrganization);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage("添加失败");
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 更新客户/供应商/收货单
	 * @param req
	 * @param resp
	 * @param type
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "" }, method = RequestMethod.PUT)
	public View update(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, @RequestBody CustomerVO vo, Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		Object obj = null;
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:  // 更新客户
				BusinessUnit businessUnit=businessUnitService.findById(vo.getBusinessUnit().getId());
				if(businessUnit!=null){
					if(vo.getBusinessUnit().getTelephone()!=null&&!vo.getBusinessUnit().getTelephone().equals("")){
						businessUnit.setTelephone(vo.getBusinessUnit().getTelephone());
					}
					if(vo.getBusinessUnit().getAddress()!=null&&!vo.getBusinessUnit().getAddress().equals("")){
						businessUnit.setAddress(vo.getBusinessUnit().getAddress());
					}
				}
				businessUnitService.update(businessUnit);
				vo.getBusinessUnit().setOrganization(currentUserOrganization);
				vo.getBusinessUnit().getDiyType().setOrganization(currentUserOrganization);
				resultVO.setObject(vo.getBusinessUnit());
				obj = customerService.updateCustomer(resultVO);
				break;
			case PROVIDER_TYPE:  // 更新供应商
				vo.getBusinessUnit().setOrganization(currentUserOrganization);
				vo.getBusinessUnit().getDiyType().setOrganization(currentUserOrganization);
				resultVO.setObject(vo.getBusinessUnit());
				obj = providerService.updateProvider(resultVO);
				break;
			case RECEIVE_TYPE:   // 更新收货单
				vo.getReceivingNote().setOrganization(currentUserOrganization);
				vo.getReceivingNote().setUserName(info.getUserName());
				resultVO.setObject(vo.getReceivingNote());
				obj = receivingNoteService.updateReceivNote(vo.getReceivingNote());
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("model", obj);
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(value = { "/judgeIsUsed" }, method = RequestMethod.POST)
	public View judgeIsUsed(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, @RequestBody CustomerVO vo, Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		boolean flag = true;
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:
				flag = customerService.judgeIsUsed(vo.getBusinessUnit(), currentUserOrganization);
				break;
			case PROVIDER_TYPE:
				flag = providerService.judgeIsUsed(vo.getBusinessUnit(), currentUserOrganization);
				break;
			case RECEIVE_TYPE:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("flag", flag);
		return JSON;
	}

	/**
	 * 删除客户/供应商/单别/收货单
	 * @param req
	 * @param resp
	 * @param type
	 * @param vo
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = { "", "" }, method = RequestMethod.DELETE)
	public View delete(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, @RequestBody CustomerVO vo, Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		ResultVO resultVO = new ResultVO();
		boolean success = false;
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:  // 删除客户
				BusinessUnit customer = vo.getBusinessUnit();
				success = customerService.remove(customer,currentUserOrganization);
				if(success) {
					resultVO.setMessage("删除成功");
					/* 删除该客户对应销往企业 */
					Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
					fromToBusService.updateDelFlag(null, fromBusId, customer.getId(), true);
				} else {
					resultVO.setErrorMessage("删除失败");
					resultVO.setStatus(SERVER_STATUS_FAILED);
				}
				break; 
			case PROVIDER_TYPE: // 删除供应商
				success = providerService.remove(vo.getBusinessUnit(),currentUserOrganization);
				if(success) {
					resultVO.setMessage("删除成功");
				} else {
					resultVO.setErrorMessage("删除失败");
					resultVO.setStatus(SERVER_STATUS_FAILED);
				}
				break;
			case ORDER_TYPE:  // 删除单别
				success = orderTypeService.remove(vo.getOrdertype(), currentUserOrganization); //状态判断码
				if(success) {
					resultVO.setMessage("删除成功");
				} else {
					resultVO.setErrorMessage("该单别已经被使用过，不能删除!");
					resultVO.setStatus(SERVER_STATUS_FAILED);
				}
				break;
			case RECEIVE_TYPE: // 删除收货单
				receivingNoteService.remove(vo.getReceivingNote());
				break;
			}
		} catch (Exception e) {
			resultVO.setErrorMessage("删除失败");
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 客户分页查询/供应商/单别/收货单(搜索框)
	 */
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, @RequestParam int page,
			@RequestParam int pageSize, Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE: // 客户分页查询
				model.addAttribute("result", customerService.getPaging(page,
						pageSize, null,currentUserOrganization));
				break;
			case PROVIDER_TYPE: // 供应商分页查询
				model.addAttribute("result", providerService.getPaging(page,
						pageSize, null,currentUserOrganization));
				break;
			case ORDER_TYPE:    // 单别分页查询
				model.addAttribute("result", orderTypeService.getPaging(page,
						pageSize, null, currentUserOrganization));
				break;
			case RECEIVE_TYPE:  // 收货单分页查询
				model.addAttribute("result", receivingNoteService.getPagingByUserName(
						page, pageSize, null, info.getUserName(), currentUserOrganization));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	// 获取商品cgw
	@RequestMapping(value = { "/findProduct" }, method = RequestMethod.GET)
	public View findProduct(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "num") String num,Model model) {
		try {
			model.addAttribute("result", outGoodsInfoService.getAllProductByNum(num));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	// 获取提供商cgw
	@RequestMapping(value = { "/findProvider" }, method = RequestMethod.GET)
	public View findProvider(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", providerService.getProvider(currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	// 获取单别cgw
	@RequestMapping(value = { "/findOrderType" }, method = RequestMethod.GET)
	public View findOrderType(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "module") String module,
			@RequestParam(value = "order") String order, Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result",
					orderTypeService.getOrdrTypeByModuleAndOrder(module, order, currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	// 获取仓库信息cgw
	@RequestMapping(value = { "/findStorage" }, method = RequestMethod.GET)
	public View findStorage(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", storageInfoService.getAllStorageInfo(currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	// 获取出货单信息
	@RequestMapping(value = { "/findOutOrderInfo" }, method = RequestMethod.GET)
	public View findOutOrderInfo(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "num") String num, Model model) {
		try {
			model.addAttribute("result",outOfBillService.getOutOrderInfoByOutOrder(num));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	// 获取出货单号cgw
	@RequestMapping(value = { "/findOutGood" }, method = RequestMethod.GET)
	public View findOutGood(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "num") Long num, Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result",outOfBillService.getOurOfBillByProviderNum(num,currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
		
	// 获取供货商联系人cgw
	@RequestMapping(value = { "/findProviderContact" }, method = RequestMethod.GET)
	public View findProviderContact(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "num") String num, Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		Long no = Long.parseLong(num);
		try {
			model.addAttribute("result",contactInfoService.getContactsByTypeAndNo(2,no,currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value = { "/lists" }, method = RequestMethod.GET)
	public View getAll(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, Model model) {

		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:
				model.addAttribute("result",customerService.getCustomer(currentUserOrganization));
				break;
			default: break;

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}

	@RequestMapping(value = { "/findPro" }, method = RequestMethod.POST)
	public View findAllProvince(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		try {
			model.addAttribute("result", addressService.findAllProvince());
			model.addAttribute("count", addressService.findAllProvince().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	@RequestMapping(value = { "/findCity" }, method = RequestMethod.POST)
	public View findAllCity(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		try {
			model.addAttribute("result", addressService.findAllCity());
			model.addAttribute("count", addressService.findAllCity().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	@RequestMapping(value = { "/findArea" }, method = RequestMethod.POST)
	public View findAllArea(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		try {
			model.addAttribute("result", addressService.findAllArea());
			model.addAttribute("count", addressService.findAllArea().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value = { "/list/suresureReceivingnote" }, method = RequestMethod.GET)
	public View searchSuresureReceivingnote(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type, @RequestParam int page,
			@RequestParam int pageSize, Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();

		try {
			switch (getType(type)) {
			case RECEIVE_TYPE:
				model.addAttribute("result", receivingNoteService.getPagingSureReceivingnote(
						page, pageSize, null, currentUserOrganization));
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}
	
	@RequestMapping(value = { "/list/suresureReceivingnote/search" }, method = RequestMethod.GET)
	public View searchSuresureReceivingnoteKeywords(HttpServletRequest req, HttpServletResponse resp,@RequestParam String keywords,
			@PathVariable int type, @RequestParam int page,
			@RequestParam int pageSize, Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();
		
		try {
			switch (getType(type)) {
			case RECEIVE_TYPE:
				model.addAttribute("result", receivingNoteService.getPagingSureReceivingnote(
						page, pageSize, keywords, currentUserOrganization));
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}
	
	/**
	 * 收货单确认
	 * @param req
	 * @param resp
	 * @param ReceivingNote
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/checkReceivingnote"}, method = RequestMethod.POST)
	public View checkReceivingnote(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody ReceivingNote ReceivingNote, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();
		
		Object obj = null;
		try {
			obj = receivingNoteService.checkReceivingnote(ReceivingNote.getRe_num(),userName, currentUserOrganization);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("result", obj);
		return JSON;
	}
	
	/**
	 * 根据过滤条件查询，客户/供应商列表信息
	 * @param req
	 * @param resp
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = { "/reglist/{page}/{pageSize}/{configure}" })
	public View getReqList(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			@PathVariable int type, Model model) {
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String userName = info.getUserName();
			switch (getType(type)) {
			case CUSTOMER_TYPE: // 客户
				model.addAttribute("result", customerService.getCustomerfilter(page, pageSize, configure, currentUserOrganization));
				break;
			case PROVIDER_TYPE: // 供应商
				model.addAttribute("result", providerService.getProviderfilter(page, pageSize, configure, currentUserOrganization));
				break;
			case ORDER_TYPE:    // 单别
				model.addAttribute("result", orderTypeService.getOrderTypefilter(page, pageSize, configure, currentUserOrganization));
				break;
			case RECEIVE_TYPE:  // 收货单
				model.addAttribute("result", receivingNoteService.getReceivingNotefilter(page, pageSize, configure, currentUserOrganization, userName));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/*
	 * 根据客户\供应商获取联系人信息
	 * author：cgw
	 * date：2014-11-3*/
	@RequestMapping(method = RequestMethod.GET, value = { "/findContact/{page}/{pageSize}/{busnessItemID}" })
	public View findContact(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "busnessItemID") Long busnessItemID,
			@PathVariable int type, Model model) {
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		//2是客户3是供应商
		try {
			if(type==2){
				model.addAttribute("result", contactInfoService.getListContacts(page, pageSize, busnessItemID, 1, currentUserOrganization));
			}else if(type==3){
				model.addAttribute("result", contactInfoService.getListContacts(page, pageSize, busnessItemID, 2, currentUserOrganization));
			}	
		} catch (Exception e) {
			
		}
		return JSON;
	}
	// 获取所有企业
	@RequestMapping(value = { "/getListBusinessUnit" }, method = RequestMethod.GET)
	public View getListBusinessUnit(
			@RequestParam("page") Integer page , @RequestParam("keyword") String keyword, 
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		try {
			Integer pageSize = 20;
			String busType = ""; //企业类型
			model.addAttribute("result",businessUnitService.getAllBusUnitName(page,pageSize,keyword,busType));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 根据企业名称查找企业营业执照
	 * @param req
	 * @param resp
	 * @param businessUnitName
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/getLicense" }, method = RequestMethod.POST)
	public View getLicense(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "businessUnitName") String businessUnitName, Model model) {
		try {
			model.addAttribute("result",businessUnitService.findLicenseByName(businessUnitName));
			LightBusUnitVO buVo = businessUnitService.findVOByName(businessUnitName);
			model.addAttribute("business",buVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 根据收获单号，获取商品列表
	 * @param req
	 * @param resp
	 * @param no
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = { "/getGoodsByReceivNo/{no}" }, method = RequestMethod.GET)
	public View getGoodsByReceivNo(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable(value = "no") String no,@RequestParam(value = "page") int page,
			@RequestParam(value = "pageSize") int pageSize, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			List<PurchaseorderInfo> listOfGoods = purchaseorderInfoService.getListByNoPage(no, page, pageSize);
			long total = purchaseorderInfoService.countByNo(no);
			Map map = new HashMap();
			map.put("listOfGoods", listOfGoods);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 查找客户是否存在
	 * @author ZhangHui 2015/04/13
	 */
	@RequestMapping(value = { "/getCountOfCustomerOrProvider/{busId}" }, method = RequestMethod.GET)
	public View countOfCustomer(
			@PathVariable int type,
			@PathVariable long busId,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long count = 0L;
			switch (getType(type)) {
			case CUSTOMER_TYPE:
				count = providerService.count(currentUserOrganization,busId);
				break;
			case PROVIDER_TYPE:
				count = customerService.count(currentUserOrganization, busId);
				break;
			}
			model.addAttribute("count", count);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 报告待处理供应商查询
	 * @author ZhangHui 2015/5/4
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = { "/list/onHandProducer/{configure}" }, method = RequestMethod.GET)
	public View queryOnHandProducer(
			@RequestParam(value="flag",required=false)boolean flag,
			@PathVariable int type, 
			@RequestParam int page,
			@RequestParam int pageSize,
			@PathVariable String configure,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = null;
		if(!flag){
		   currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
	   }
		try {
			switch (getType(type)) {
			case PROVIDER_TYPE:
				model.addAttribute("result", providerService.getPagingOfOnHandProducer(page,
						pageSize, currentUserOrganization,configure));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 查询入住客户
	 * @author wubiao 2015/90/21
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = { "/searchCustomer/{page}/{pageSize}/{keyword}/{rond}" }, method = RequestMethod.GET)
	public View searchCustomer(@PathVariable int type,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "keyword") String keyword,
			@PathVariable(value = "rond") String rond,
			HttpServletRequest req,HttpServletResponse resp,Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			PagingSimpleModelVO<BusinessUnit>  result = customerService.searchCustomerList(page, pageSize, keyword, currentUserOrganization);
			model.addAttribute("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	/**
	 * 查询入住客户
	 * @author wubiao 2015/90/21
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = { "/sourceOrsoldCustomer/{_type}/{page}/{pageSize}/{configure}/{rond}" }, method = RequestMethod.GET)
	public View sourceOrsoldCustomer(@PathVariable(value = "_type") int _type,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			@PathVariable(value = "rond") String rond,
			HttpServletRequest req,HttpServletResponse resp,Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			PagingSimpleModelVO<BusinessUnit>  result = customerService.sourceOrsoldCustomer(_type,page, pageSize, configure, currentUserOrganization);
			model.addAttribute("result", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}

	/**
	 * 根据过滤条件查询，客户/供应商列表信息
	 * @param req
	 * @param resp
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = { "/allReglist/{page}/{pageSize}/{configure}" })
	public View getAllReqList(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			@PathVariable int type, Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		if(info==null){
			return JSON;
		}
		try {
			PagingSimpleModelVO<BusinessUnit> busList= providerService.getProviderfilter(page, pageSize, configure, null) ;
			model.addAttribute("result", busList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 客户分页查询/供应商/单别/收货单(搜索框)
	 */
	@RequestMapping(value = { "/allList" }, method = RequestMethod.GET)
	public View searchAll(HttpServletRequest req, HttpServletResponse resp,
			@PathVariable int type,
			@RequestParam int page,
			@RequestParam int pageSize, Model model) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		if(info==null){
			return JSON;
		}
		try {
			PagingSimpleModelVO<BusinessUnit> busList= providerService.getPaging(page,pageSize, null,null) ;
		  model.addAttribute("result", busList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	
	/*@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public View searchKeywords(HttpServletRequest req, HttpServletResponse resp,@RequestParam String keywords,
			@PathVariable int type, @RequestParam int page,
			@RequestParam int pageSize, Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();

		try {
			switch (getType(type)) {
			case CUSTOMER_TYPE:
				model.addAttribute("result", customerService.getPaging(page,
						pageSize, keywords, currentUserOrganization));
				break;
			case PROVIDER_TYPE:
				model.addAttribute("result", providerService.getPaging(page,
						pageSize, keywords, currentUserOrganization));
				break;
			case ORDER_TYPE:
				model.addAttribute("result", orderTypeService.getPaging(page,
						pageSize, keywords, currentUserOrganization));// 单别cgw
				break;
			case RECEIVE_TYPE:
				model.addAttribute("result", receivingNoteService.getPagingByUserName(
						page, pageSize, keywords,userName, currentUserOrganization));// 收货单管理cgw
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}*/
}
