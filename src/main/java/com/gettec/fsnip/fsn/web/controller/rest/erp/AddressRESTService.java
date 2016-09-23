package com.gettec.fsnip.fsn.web.controller.rest.erp;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

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

import com.gettec.fsnip.fsn.model.erp.base.AddressInfo;
import com.gettec.fsnip.fsn.model.erp.base.Area;
import com.gettec.fsnip.fsn.model.erp.base.City;
import com.gettec.fsnip.fsn.model.erp.base.Province;
import com.gettec.fsnip.fsn.service.erp.AddressService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/erp/address")
public class AddressRESTService {

	@Autowired
	private AddressService addressService;
	
	@RequestMapping(value="/findPro", method = RequestMethod.GET)
	public View findAllProvince(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		try {
			List<Province> pList = addressService.findAllProvince();
			model.addAttribute("result", pList);
			model.addAttribute("count", pList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(value="/findCity", method = RequestMethod.GET)
	public View findAllCity(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		try {
			List<City> cList = addressService.findAllCity();
			model.addAttribute("result", cList);
			model.addAttribute("count", cList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 根据省id获取省下面的市区名称
	 * @param proId
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(value="/findCityByProId/{proId}", method = RequestMethod.GET)
	public View findCityByProId(@PathVariable String proId ,HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			model.addAttribute("cities", addressService.getCityByProvId(proId));
			resultVO.setStatus("true");
		} catch (Exception e) {
			resultVO.setStatus("false");
			resultVO.setErrorMessage(e.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(value="/findArea", method = RequestMethod.GET)
	public View findAllArea(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		try {
			List<Area> aList = addressService.findAllArea();
			model.addAttribute("result", aList);
			model.addAttribute("count", aList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	 * 根据城市id获取城市下面的县级区域
	 * @param cityId
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(value="/findAreaByCityId/{cityId}", method = RequestMethod.GET)
	public View findAreaByCityId(@PathVariable String cityId ,HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			model.addAttribute("areas", addressService.getAreaByCityId(cityId));
			resultVO.setStatus("true");
		} catch (Exception e) {
			resultVO.setStatus("false");
			resultVO.setErrorMessage(e.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据企业地址字符串，获取地址区域代码，兼容老数据
	 * @param address
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(value="/getAreaCodeByAddress/{address}", method = RequestMethod.GET)
	public View getAreaCodeByAddress(@PathVariable String address ,HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			model.addAttribute("areaCode", addressService.getAreaCodeByAddress(address));
			resultVO.setStatus("true");
		} catch (Exception e) {
			resultVO.setStatus("false");
			resultVO.setErrorMessage(e.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(value={"/list"}, method = RequestMethod.GET)
	public View search(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam int page, @RequestParam int pageSize, Model model){
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			model.addAttribute("result", addressService.getPaging(page, pageSize, null, currentUserOrganization));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}
	
	@RequestMapping(value="/update", method = RequestMethod.POST)
	public View update(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody AddressInfo addressInfo, Model model) {
		
		AuthenticateInfo user = SSOClientUtil.validUser(req, resp);
		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		
		try {
			addressInfo.setOrganization(currentUserOrganization);
			addressService.update(addressInfo);
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
		
		try {
			model.addAttribute("result", addressService.getPaging(page, pageSize, keywords, currentUserOrganization));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSON;
	}
}
