package com.gettec.fsnip.fsn.web.controller.rest.erp;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseInstanceService;
import com.gettec.fsnip.fsn.service.erp.buss.MerchandiseStorageInfoService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.sso.client.util.AccessUtils;

@Controller
@RequestMapping("/erp/merchandiseStorage")
public class MerchandiseStorageInfoRESTService {
	@Autowired private MerchandiseStorageInfoService merchandiseStorageInfoService;
	@Autowired private MerchandiseInstanceService merchandiseInstanceService;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private ProductService productService;
	
	@RequestMapping(value={"/list"}, method=RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			model.addAttribute("result", merchandiseStorageInfoService.getPaging(page, pageSize, null, currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
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
			model.addAttribute("result", merchandiseStorageInfoService.getMerchandiseStorageInfofilter(page, pageSize, configure, currentUserOrganization));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	//获取库存中商品的信息
	@RequestMapping(value={"/listall"}, method=RequestMethod.GET)
	public View searchAll(HttpServletRequest req, HttpServletResponse resp, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			model.addAttribute("result", merchandiseStorageInfoService.getProductByStorageInfo(currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON;
	}
	
	/**
	 * 根据商品ID和仓库ID获取库存中的批次
	 * @param req
	 * @param resp
	 * @param productId
	 * @param storageId
	 * @param model
	 * @return 商品实例集合
	 * Author 郝圆彬
	 * 2014-10-27
	 * 修改
	 */
	@RequestMapping(value={"/getBatchNum"}, method=RequestMethod.GET)
	public View getBatchNum(HttpServletRequest req, HttpServletResponse resp,Long productId,String storageId, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			model.addAttribute("result", merchandiseStorageInfoService.getBatchNumByStorageInfo(productId,storageId,currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON;
	}
	/**
	 * 商品实例ID或者产品ID获取库存中的仓库
	 * @param req
	 * @param resp
	 * @param productId
	 * @param storageId
	 * @param model
	 * @return 商品实例集合
	 * Author 郝圆彬
	 * 2014-10-27
	 * 新增
	 */
	@RequestMapping(value={"/getStorage"}, method=RequestMethod.GET)
	public View getStorage(HttpServletRequest req, HttpServletResponse resp,Long productId,Long instanceId, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			model.addAttribute("result", merchandiseStorageInfoService.getStorageByProductIdOrInstanceId(productId,instanceId,currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON;
	}
	/**
	 * 根据商品实例ID和仓库ID获取库存量
	 * @param req
	 * @param resp
	 * @param productId
	 * @param storageId
	 * @param model
	 * @return 商品实例集合
	 * Author 郝圆彬
	 * 2014-10-28
	 * 新增
	 */
	@RequestMapping(value={"/getStorageNum"}, method=RequestMethod.GET)
	public View getStorageNum(HttpServletRequest req, HttpServletResponse resp,String storageId,Long instanceId, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			model.addAttribute("result", merchandiseStorageInfoService.getNumByStorage(storageId,instanceId,currentUserOrganization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON;
	}
	/**
	 * 根据商品批次和仓库ID获取库存量
	 * @return 商品实例集合
	 * Author cgw
	 * 2014-11-7
	 * 新增
	 */
	@RequestMapping(value={"/getStorageNumByBatchAndStorage"}, method=RequestMethod.GET)
	public View getStorageNumByBatchAndStorage(HttpServletRequest req, HttpServletResponse resp,String storageId,String batch,String barcode, Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			Product pro = productService.getDAO().findByBarcode(barcode);
			InitializeProduct initProduct = initializeProductService.findByProIdAndOrgId(pro.getId(), currentUserOrganization);
			MerchandiseInstance instance = merchandiseInstanceService.getInstanceByNoAndBatchNumber(initProduct.getId(), batch);
			model.addAttribute("result", merchandiseStorageInfoService.getNumByStorage(storageId,instance.getInstanceID(),currentUserOrganization));
		} catch (ServiceException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON;
	}
	/**
	 * 商品barcode获取库存中的仓库
	 * @return 商品实例集合
	 * Author cgw
	 * 2014-10-27
	 * 新增
	 */
	@RequestMapping(value={"/getStorageBybarcode"}, method=RequestMethod.GET)
	public View getStorageBybarcode(HttpServletRequest req, HttpServletResponse resp,String barcode,Model model){
		
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		try {
			Product product = productService.getDAO().findByBarcode(barcode);
			InitializeProduct initProduct = initializeProductService.findByProIdAndOrgId(product.getId(), currentUserOrganization);
			model.addAttribute("result", merchandiseStorageInfoService.getStorageByProductIdOrInstanceId(initProduct.getId(),null,currentUserOrganization));
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON;
	}
}
