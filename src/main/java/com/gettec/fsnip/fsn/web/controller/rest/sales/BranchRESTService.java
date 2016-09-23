package com.gettec.fsnip.fsn.web.controller.rest.sales;

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
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.service.sales.RecommendBuyService;
import com.gettec.fsnip.fsn.service.sales.SalesBranchService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;
import com.gettec.fsnip.fsn.vo.sales.SalesBranchVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 销售网点 and 推荐购买方式 Controller
 * @author tangxin 2015/04/24
 *
 */
@Controller
@RequestMapping("/sales/branch")
public class BranchRESTService {

	@Autowired private SalesBranchService salesBranchService;
	@Autowired private RecommendBuyService recommendBuyService;
	
	/**
	 * 创建销售网点信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public View createBranch(@RequestBody SalesBranchVO salesBranchVO,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			salesBranchVO = salesBranchService.save(salesBranchVO, info, true);
			model.addAttribute("data", salesBranchVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 更新销售网点信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/update")
	public View updateBranch(@RequestBody SalesBranchVO salesBranchVO, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			salesBranchVO = salesBranchService.save(salesBranchVO, info, false);
			model.addAttribute("data", salesBranchVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 分页查询销售网点信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListBranch/{configure}/{page}/{pageSize}")
	public View getListBranch(@PathVariable String configure, @PathVariable Integer page, @PathVariable Integer pageSize,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long totals = salesBranchService.countByConfigure(info.getOrganization(), configure);
			List<SalesBranchVO> listBranch = salesBranchService.getListByOrganizationWithPage(info.getOrganization(), configure, page, pageSize);
			model.addAttribute("data", listBranch);
			model.addAttribute("totals", totals);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据id获取销售网点信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findByid/{branchId}")
	public View findByid(@PathVariable Long branchId, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			SalesBranchVO branchVO = new SalesBranchVO(salesBranchService.findById(branchId));
			model.addAttribute("data", branchVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据地址类型（01省、02市） 和关键字 获取销售网点二级地址的列表
	 * @author tangxin 2015-04-28
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListAddrByType/{keyword}/{type}")
	public View getListAddrByType(@PathVariable String keyword, @PathVariable String type, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<String> listAddr = salesBranchService.getListAddrByType(info.getOrganization(), keyword, type);
			model.addAttribute("data", listAddr);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取当前登陆企业 指定省份下的销售网点信息(销售网点模块，预览地图时加载)
	 * @author tangxin 2015/6/15
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListByProvince/{province}")
	public View getListAddrByProvince(@PathVariable String province, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<SalesBranchVO> listBranchVO = salesBranchService.getListAddrByProvince(info.getOrganization(), province);
			model.addAttribute("data", listBranchVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	
	/**
	 * 根据销售网点名称统计数量，验证名称时候重复用
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/countBranchByName/{name}/{id}")
	public View countBranchByName(@PathVariable String name, @PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		 	long count = salesBranchService.countByName(name,info.getOrganization(),id);
			model.addAttribute("count", count);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 删除销售网点信息(假删除)
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delBranchById/{branchId}")
	public View delBranchById(@PathVariable Long branchId, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = salesBranchService.removeBranchById(branchId, info);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 创建推荐购买方式信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/buyWay/create")
	public View createBuyWay(@RequestBody RecommendBuyVO recommendBuyVO,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			recommendBuyVO = recommendBuyService.save(recommendBuyVO, info, true);
			model.addAttribute("data", recommendBuyVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 更新推荐购买方式信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/buyWay/update")
	public View updateBuyWay(@RequestBody RecommendBuyVO recommendBuyVO, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			recommendBuyVO = recommendBuyService.save(recommendBuyVO, info, false);
			model.addAttribute("data", recommendBuyVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据推荐购买方式名称统计 数量 验证名称是否重复使用
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/countRecommendByName/{name}/{id}")
	public View countRecommendByName(@PathVariable String name, @PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long count = recommendBuyService.countByName(name, info.getOrganization(), id);
			model.addAttribute("count", count);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 分页查询推荐购买方式信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListBuyWay/{configure}/{page}/{pageSize}")
	public View getListBuyWay(@PathVariable String configure, @PathVariable Integer page, @PathVariable Integer pageSize,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long totals = recommendBuyService.countByConfigure(info.getOrganization(), configure);
			List<RecommendBuyVO> listBuyWay = recommendBuyService.getListByOrganizationWithPage(info.getOrganization(), configure, page, pageSize);
			model.addAttribute("data", listBuyWay);
			model.addAttribute("totals", totals);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 删除推荐购买方式信息(假删除)
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delBuyWayById/{buywayId}")
	public View delBuyWayById(@PathVariable Long buywayId, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = recommendBuyService.removeRecommendBuyById(buywayId, info);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
}
