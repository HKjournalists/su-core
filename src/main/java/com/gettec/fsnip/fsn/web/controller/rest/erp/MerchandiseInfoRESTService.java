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
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.erp.MerchandiseInfoService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/merchandiseInfo")
public class MerchandiseInfoRESTService {

	@Autowired private MerchandiseInfoService merchandiseInfoService;
	@Autowired private ProductService productService;
	
	@RequestMapping(value={"","/"}, method = RequestMethod.PUT)
	public View update(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody Product merchandiseInfo, Model model) {
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		//Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			merchandiseInfo.setOrganization(organization);
			merchandiseInfoService.update(merchandiseInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("result", merchandiseInfo);
		return JSON;
	}
	
	/**
	 * 查找未初始化的企业本地产品或引进产品信息
	 * @param req
	 * @param resp
	 * @param keyWord
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/list/{keyWord}"}, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp, @PathVariable(value = "keyWord") String keyWord,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<Product> initializeProducts = null;
			Long count = null;
			if(keyWord.equals("local")){
				/* 1.1 企业本地产品 */
				initializeProducts = productService.getAllLocalProduct(page, pageSize, info.getOrganization());
				count = productService.getCountOfAllLocalProduct(info.getOrganization());
			}else if(keyWord.equals("notlocal")){
				/* 1.2 企业引进产品 */
				initializeProducts = productService.getAllNotLocalProduct(page, pageSize, info.getOrganization());
				count = productService.getCountOfAllNotLocalProduct(info.getOrganization());
			}
			PagingSimpleModelVO<Product> modelVO = new PagingSimpleModelVO<Product>();
			modelVO.setListOfModel(initializeProducts);
			modelVO.setCount(count);
			model.addAttribute("data", modelVO);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(value={"/listall"}, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		
		try {
			model.addAttribute("result", merchandiseInfoService.getAll(organization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value={"/search"}, method = RequestMethod.GET)
	public View searchKeywords(HttpServletRequest req, HttpServletResponse resp,@RequestParam String keywords,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		
		try {
			model.addAttribute("result", merchandiseInfoService.getPaging(page, pageSize, keywords, organization));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/reglist/{page}/{pageSize}/{configure}" })
	public View getReqList(HttpServletRequest req,
			HttpServletResponse resp,
			@PathVariable(value = "page") int page,
			@PathVariable(value = "pageSize") int pageSize,
			@PathVariable(value = "configure") String configure,
			Model model) {

		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();

		try {
			model.addAttribute("result", merchandiseInfoService.getMerchandiseInfofilter(page, pageSize, configure, organization));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	/*
	 * 功能：判断商品是否被使用
	 * author：cgw
	 * date：2014-11-1*/
	@RequestMapping(value={"/judgeIsUsed"}, method = RequestMethod.POST)
	public View judgeIsUsed(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody Product merchandiseInfo, Model model) {
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		boolean flag = true;
		try {
			flag = merchandiseInfoService.judgeIsUsed(merchandiseInfo, organization);
		} catch (Exception e) {
		}
		model.addAttribute("result", flag);
		return JSON;
	}
	/**
	 * 获取引进的商品
	 * @param req
	 * @param resp
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 * @author hao
	 * 2014-11-01
	 */
	@RequestMapping(value={"/introduceProducts"}, method = RequestMethod.GET)
	public View searchIntroduceProducts(HttpServletRequest req, HttpServletResponse resp, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long organization = user.getOrganization();
		
		try {
//			model.addAttribute("result", merchandiseInfoService.getIntroduceProducts( null,organization));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}
}
