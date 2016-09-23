package com.gettec.fsnip.fsn.web.controller.rest.erp;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

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
import com.gettec.fsnip.fsn.model.erp.base.OutOfBill;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.erp.ContactInfoService;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.OutGoodsInfoService;
import com.gettec.fsnip.fsn.service.erp.OutOfBillService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.service.erp.buss.OutStorageRecordService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/outOfGoods")
public class OutOfBillRESTService {
	
	@Autowired
	private OutOfBillService outOfBillService;
	@Autowired
	private OutGoodsInfoService outGoodsInfoService;
	@Autowired
	private ContactInfoService contactInfoService;
	@Autowired
	private OutStorageRecordService outStorageRecordService;
	@Autowired
	private ProductInstanceService productInstanceService;
	@Autowired
	private ProductService productService;
	@Autowired
	private MerchandiseStorageInfoService merchandiseStorageInfoService;
	@Autowired
	private InitializeProductService initializeProductService;
	@RequestMapping(value={"/list"}, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();
		
		try {
			model.addAttribute("result", outOfBillService.getPagingByUserName(page, pageSize, null,userName, currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 新增出货单
	 * @param req
	 * @param resp
	 * @param outOfBill
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/add"}, method = RequestMethod.POST)
	public View save(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody OutOfBill outOfBill ,Model model){
		ResultVO resultVO=new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			outOfBill.setOrganization(currentUserOrganization);
			outOfBill.setUserName(info.getUserName());
			outOfBill.setOrgname(info.getUserOrgName());
			outOfBillService.add(outOfBill, currentUserOrganization);
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
	 * 更新出货单
	 * @param req
	 * @param resp
	 * @param outOfBill
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/update"}, method = RequestMethod.POST)
	public View update(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody OutOfBill outOfBill ,Model model){
		ResultVO resultVO=new ResultVO();
		try {
			AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			outOfBill.setUserName(user.getUserName());
			outOfBill.setOrganization(currentUserOrganization);
			outOfBillService.updateOutOfBill(outOfBill);
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(value={"/delete"}, method = RequestMethod.POST)
	public View delete(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody OutOfBill outOfBill ,Model model){
		Object obj = null;
		try {
			obj = outOfBillService.remove(outOfBill);
		} catch (Exception e) {
			
		}
		model.addAttribute("result", obj);
		return JSON;
	}
	
	@RequestMapping(value={"/findContact"}, method = RequestMethod.GET)
	public View findcontact(HttpServletRequest req, HttpServletResponse resp,@RequestParam(value="num") String num,Model model) {
		Long no = Long.parseLong(num);
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", contactInfoService.getContactsByTypeAndNo(1, no,currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	//查询商品批次cgw
	@RequestMapping(value={"/findBatches"}, method = RequestMethod.GET)
	public View findBatches(HttpServletRequest req, HttpServletResponse resp, Long id,String storage,Model model) {
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", merchandiseStorageInfoService.getBatchNumByStorageInfo(id, storage, currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/*
	 * describe:查询仓库
	 * author:cgw
	 * */
	@RequestMapping(value={"/getStorages"}, method = RequestMethod.GET)
	public View getStorages(HttpServletRequest req, HttpServletResponse resp,String barcode,Long productInstanceId,Model model) {
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		Product p =null;
		try {
			if(barcode.equals("null")){
				model.addAttribute("result", merchandiseStorageInfoService.getStorageByProductIdOrInstanceId(null, Long.valueOf(productInstanceId), currentUserOrganization));
			}else{
				p = productService.getDAO().findByBarcode(barcode);
				model.addAttribute("result", merchandiseStorageInfoService.getStorageByProductIdOrInstanceId(p.getId(), null, currentUserOrganization));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 出货单确认发货
	 * @param req
	 * @param resp
	 * @param outOfBill
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/checkOne"}, method = RequestMethod.POST)
	public View checkOne(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody OutOfBill outOfBill, Model model){
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		Object obj = null;
		try {
			obj = outOfBillService.checkOne(outOfBill.getOutOfBillNo(), info.getUserName(), currentUserOrganization);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("result", obj);
		return JSON;
	}
	
	@RequestMapping(value={"/checkTwo"}, method = RequestMethod.POST)
	public View checkTwo(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody OutOfBill outOfBill, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();
		
		Object obj = null;
		try {
			obj = outOfBillService.checkTwo(outOfBill.getOutOfBillNo(),userName, currentUserOrganization);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("result", obj);
		return JSON;
	}
	
	@RequestMapping(value={"/list/sureoutofgood"}, method = RequestMethod.GET)
	public View searchSureOutofgood(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();
		
		try {
			model.addAttribute("result", outOfBillService.getPagingSureOutofgood(page, pageSize, null,userName, currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value={"/search"}, method = RequestMethod.GET)
	public View searchKeywords(HttpServletRequest req, HttpServletResponse resp,@RequestParam String keywords,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();
		try {
			model.addAttribute("result", outOfBillService.getPagingByUserName(page, pageSize, keywords,userName, currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value={"/list/sureoutofgood/search"}, method = RequestMethod.GET)
	public View searchSureOutofgoodKeywords(HttpServletRequest req, HttpServletResponse resp,@RequestParam String keywords,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		String userName = user.getUserName();
		
		try {
			model.addAttribute("result", outOfBillService.getPagingSureOutofgood(page, pageSize, keywords,userName, currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 根据过滤条件查询出货单列表信息(出货单管理模块)
	 * @param req
	 * @param resp
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = { "/reglist/{page}/{pageSize}/{configure}" })
	public View getReqList(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			Model model) {
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String userName = info.getUserName();
			model.addAttribute("result", outOfBillService.getOutOfBillfilter(page, pageSize, configure, currentUserOrganization, userName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 根据过滤条件查询出货单列表信息(出货单确认模块)
	 * @param req
	 * @param resp
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = { "/reglistcheck/{page}/{pageSize}/{configure}" })
	public View getReqListCheck(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			Model model) {
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String userName = info.getUserName();
			model.addAttribute("result", outOfBillService.getOutOfBillCheckfilter(page, pageSize, configure, 
					currentUserOrganization, userName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	@RequestMapping(value={"/JudgeStorage"}, method=RequestMethod.GET)
	public View JudgeOutNum(HttpServletRequest req, HttpServletResponse resp, 
			@RequestParam(value = "storage") String storage, @RequestParam(value = "batch") String batch,
			@RequestParam(value = "value") String value,@RequestParam(value = "productId") Long productId, Model model){
		boolean flag = false;
		try {
			flag = outStorageRecordService.JudgeOutNum(productId, value, batch, storage);
		} catch (Exception e) {
			
		}
		model.addAttribute("flag", flag);
		return JSON;
	}
	//根据出货单号查询相应商品信息
	@RequestMapping(value={"/getProductById"}, method=RequestMethod.GET)
	public View getProductById(HttpServletRequest req, HttpServletResponse resp, 
			@RequestParam(value = "id") String id, Model model){
		long pid = Integer.parseInt(id);
		try {
			model.addAttribute("result", outGoodsInfoService.findById(pid));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	/*
	 * 功能：判断商品是否有质检报告
	 * author：cgw
	 * 增加
	 * date：2014-10-30*/
	@RequestMapping(value={"/isHasReporter"}, method=RequestMethod.POST)
	public View isHasReporter(HttpServletRequest req, HttpServletResponse resp, 
			@RequestParam(value = "id") Long id,@RequestParam(value = "batch") String batch, Model model){
		boolean flag = false;
		List<ProductInstance> proIns = null;
		try {
			proIns = productInstanceService.getProductInstancesByBatchAndProductId(batch, initializeProductService.findById(id).getProduct().getId());
			if(proIns.size()==0){
				flag = false;
			}else{
				flag = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		model.addAttribute("flag", flag);
		return JSON;
	}
	
	/*
	 * 根据出货单号获取商品信息
	 * author：cgw
	 * date：2014-11-3*/
	@RequestMapping(method = RequestMethod.GET, value = { "/findOutGoods/{page}/{pageSize}/{outGoodItemNum}" })
	public View findOutGoods(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "outGoodItemNum") String outGoodItemNum,Model model) {
		try {
			model.addAttribute("result",outGoodsInfoService.getListByNoPage(page, pageSize, outGoodItemNum));
		} catch (ServiceException sex) {
			sex.printStackTrace();
		}
		return JSON;
	}
}
