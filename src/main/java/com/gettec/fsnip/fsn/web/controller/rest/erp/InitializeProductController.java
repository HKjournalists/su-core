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
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductLog;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.erp.FromToBussinessService;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/initializeProduct")
public class InitializeProductController {
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private FromToBussinessService fromToBusService;
	@Autowired private ProductService productService;
	@Autowired private StatisticsClient staClient;
	/**
	 * 初始化产品信息
	 * @param req
	 * @param resp
	 * @param initializeProduct
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"","/"}, method = RequestMethod.PUT)
	public View saveInitializeProduct(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody InitializeProduct initializeProduct, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			initializeProductService.save(initializeProduct, info.getOrganization());
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 加载已经初始化的产品信息
	 * @param req
	 * @param resp
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/listall/InitializeProduct"}, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam int page, @RequestParam int pageSize,Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		
		try {
			model.addAttribute("result", initializeProductService.getAllInitializeProduct(page,pageSize,organization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	/**
	 * 加载可以出库的产品信息
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/outlistall"}, method = RequestMethod.GET)
	public View searchout(HttpServletRequest req, HttpServletResponse resp, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		
		try {
			model.addAttribute("result", initializeProductService.getAllOutInitializeProduct(organization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	//商品入库加载已经初始化的商品
	@RequestMapping(value={"/listall"}, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		
		try {
			model.addAttribute("result", initializeProductService.getInitializeProduct(organization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 根据组织机构ID查询所有引进产品
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getInitProductByOrgAndLocal/{local}/{configure}/{page}/{pageSize}")
	public View getInitProductByOrgAndLocal(@PathVariable boolean local, @PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = initializeProductService.countByOrganizationAndLocal(currentUserOrganization, local, configure);
			List<InitializeProduct> listOfProduct = initializeProductService.getByOrganizationAndLocal(currentUserOrganization, 
					local, configure, page, pageSize);
			/* 获取销往企业 */
			for(InitializeProduct product : listOfProduct){
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				String selectedCustomerNames = fromToBusService.findNamestrs(product.getProduct().getId(), fromBusId, false);
				product.setSelectedCustomerNames(selectedCustomerNames);
			}
			model.addAttribute("counts", total);
			model.addAttribute("data",listOfProduct);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
			return JSON;
	}
	
   /**
    * 删除产品
    * @author ZhangHui 2015/4/14
    */
   @RequestMapping(method=RequestMethod.DELETE, value="/{proId}")
   public View delete(@PathVariable Long proId,
			HttpServletRequest req, HttpServletResponse resp, Model model){
	   ResultVO resultVO = new ResultVO();
	   AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
	   String errorMsg="";//定义错误消息 报错时 记录到日志表
	   Product product=null;
		try{
			product=productService.findById(proId);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			/* 删除产品 */
			initializeProductService.updateDelFlag(proId, currentUserOrganization, true);
			/* 删除该产品的销往企业 */
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			fromToBusService.updateDelFlag(proId, fromBusId, null, true);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable) sex.getException()).printStackTrace();
			errorMsg=((Throwable) sex.getException()).getMessage();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
			errorMsg=errorMsg+"&&&&"+e.getMessage();
		}finally{
			/**
			 * 记录产品日志
			 * @author longxianzhen 2015/06/03
			 */
			ProductLog logData=new ProductLog(info.getUserName(), "商超进行产品删除操作", errorMsg ,HttpUtils.getIpAddr(req),product);
			staClient.offer(logData);//记录产品日志异步的
		}
		model.addAttribute("result", resultVO);
		return JSON;
   }
   
   /**
    * 查找该产品是否为当前登录企业的引进产品
    * @author ZhangHui 2015/4/14
    */
   @SuppressWarnings("unused")
   @RequestMapping(method=RequestMethod.GET, value="/countInitialProduct/{proId}")
   public View count(@PathVariable Long proId,
			HttpServletRequest req, HttpServletResponse resp, Model model){
	   ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long count = initializeProductService.count(proId, currentUserOrganization, false);
			model.addAttribute("counts", count);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
   }
   
   /**
	 * 引进产品
	 * @author ZhangHui 2015/4/14
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/leadProduct/{barcode}")
	public View leadProduct(@PathVariable(value="barcode") String barcode,HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			initializeProductService.leadProduct(barcode, currentUserOrganization);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
