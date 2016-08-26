package com.gettec.fsnip.fsn.web.controller.rest.product;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 查询产品信息
 * @author Zhanghui 2015/4/10
 */
@Controller
@RequestMapping("/product/query")
public class ProductQueryRESTService {
	@Autowired private ProductService productService;
	@Autowired private InitializeProductService initializeProductService;
	
	/**
	 * 按barcode查找该产品id
	 * @param barcode
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getProductId/{barcode}")
	public View validateBarcodeUnique(@PathVariable String barcode, HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long productId = productService.getIdByBarcode(barcode);
			model.addAttribute("data", productId);
			model.addAttribute("products", productId);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * 按barcode查找该产品id
	 * @param barcode
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getCheckProductId/{barcode}")
	public View validateBarcode(@PathVariable String barcode, HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long productId = productService.getIdByBarcode(barcode);
			
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long count = initializeProductService.count(productId, currentUserOrganization, false);
			model.addAttribute("counts", count);
			model.addAttribute("data", productId);
			model.addAttribute("products", productId);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
