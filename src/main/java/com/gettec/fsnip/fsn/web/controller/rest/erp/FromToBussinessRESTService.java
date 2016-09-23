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
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.erp.FromToBussinessService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.product.erp.ProductLeadVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 销往企业关系
 * @author ZhangHui 2015/4/8
 */
@Controller
@RequestMapping("/erp/fromToBus")
public class FromToBussinessRESTService {
	
	@Autowired private FromToBussinessService fromToBusService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductService productService;
	
	/**
	 * 引进产品时，保存销往企业
	 * @author ZhangHui 2015/4/8
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.PUT, value="")
	public View findAllProvince(@RequestBody ProductLeadVO productVO,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			String customerIds = productVO.getSelectedCustomerIds();
			if(customerIds != null){
				Long proId = productService.getIdByBarcode(productVO.getBarcode());
				Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				fromToBusService.save(resultVO, proId, fromBusId, customerIds.split(","));
				
				/*Long[] proId = productService.getIdByBarcode(productVO.getBarcode(),currentUserOrganization);
				//Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				if(proId!=null){ 
					fromToBusService.save(resultVO, proId[0], proId[1], customerIds.split(","));
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取销往企业Ids
	 * @author ZhangHui 2015/4/8
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/{proId}")
	public View getCustomerIds(@PathVariable Long proId, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 查询销往企业 */
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			String selectedCustomerIds = fromToBusService.findIdstrs(proId, fromBusId, false);
			/* 返回 */
			model.addAttribute("data", selectedCustomerIds);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取销往企业Names
	 * @author ZhangHui 2015/4/14
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getCustomerNames/{barcode}")
	public View getCustomerStrs(@PathVariable String barcode, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 查询销往企业 */
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			Long proId = productService.getIdByBarcode(barcode);
			String selectedCustomerNames = fromToBusService.findNamestrs(proId, fromBusId, false);
			/**
			 * 修改人 :wubiao   日期 :2015.917
			 * 优化代码，去掉fromBusId查询数据库
			 * 而是通过Long[] proId数组查询 返回fromBusId值
			 */
//			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
		/*	Long proId[] = productService.getIdByBarcode(barcode,currentUserOrganization);
			String selectedCustomerNames = fromToBusService.findNamestrs(proId[0], proId[1], false);*/
			/* 返回 */
			model.addAttribute("data", selectedCustomerNames);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 检查该供应商有无给当前客户提供产品
	 * @author ZhangHui 2015/4/13
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getCountOfProduct/{fromBusId}")
	public View getCountOfProduct(
			@PathVariable Long fromBusId, 
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			long count = fromToBusService.counts(fromBusId, toBusId, false);
			/* 返回 */
			model.addAttribute("count", count);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
