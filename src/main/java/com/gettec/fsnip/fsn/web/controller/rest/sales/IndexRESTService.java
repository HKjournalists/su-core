package com.gettec.fsnip.fsn.web.controller.rest.sales;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.service.sales.BusinessSalesInfoService;
import com.gettec.fsnip.fsn.service.sales.SalesCaseService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.BusinessSalesVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 销售系统首页
 * @author tangxin 2015/04/24
 *
 */
@Controller
@RequestMapping("/sales/index")
public class IndexRESTService {

	@Autowired BusinessSalesInfoService salesInfoService;
	@Autowired SalesCaseService salesCaseService;

	/**
	 * 加载企业销售信息和企业信息
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/initbusinfo")
	public View loadBusSalesInfo(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			BusinessSalesVO businessSalesVO = salesInfoService.findByOrgId(info.getOrganization());
			model.addAttribute("data", businessSalesVO);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

}
