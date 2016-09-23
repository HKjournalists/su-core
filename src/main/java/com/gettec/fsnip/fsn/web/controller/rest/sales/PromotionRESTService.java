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

import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.service.sales.PromotionCaseService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.PromotionCaseVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 促销活动 Controller
 * @author tangxin 2015/04/24
 *
 */
@Controller
@RequestMapping("/sales/promotion")
public class PromotionRESTService {

	@Autowired private PromotionCaseService promotionCaseService;
	@Autowired private SalesResourceService salesResourceService;
	
	/**
	 * 创建促销活动信息
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public View createContract(@RequestBody PromotionCaseVO promotionVO,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = promotionCaseService.save(promotionVO, info, true);
			model.addAttribute("data", null);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 更新促销活动信息
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/update")
	public View updateContract(@RequestBody PromotionCaseVO promotionVO, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = promotionCaseService.save(promotionVO, info, false);
			model.addAttribute("data", null);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 分页查询促销活动信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListPromotion/{configure}/{page}/{pageSize}")
	public View getListBranch(@PathVariable String configure, @PathVariable Integer page, @PathVariable Integer pageSize,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long totals = promotionCaseService.countByConfigure(info.getOrganization(), configure);
			List<PromotionCaseVO> listContractVO = promotionCaseService.getListByOrganizationWithPage(info.getOrganization(), configure, page, pageSize);
			model.addAttribute("data", listContractVO);
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
	 * 验证促销活动名称是否重复
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/countName/{name}/{id}")
	public View countContractName(@PathVariable String name, @PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long count = promotionCaseService.countByName(name, info.getOrganization(), id);
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
	 * 根据id获取促销活动信息
	 * @author tangxin 2015-05-03
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findByid/{id}")
	public View findByid(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			PromotionCaseVO promotionVO = new PromotionCaseVO(promotionCaseService.findById(id));
			if(promotionVO != null) {
				promotionVO.setResource(salesResourceService.getListResourceByGUID(promotionVO.getGuid()));
			}
			model.addAttribute("data", promotionVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 删除促销活动信息(假删除)
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delById/{id}")
	public View delBranchById(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = promotionCaseService.removeById(id, info);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 通过全局guid 获取资源列表
	 * @author tangxin 2015-04-30
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListResourceByGuid/{guid}")
	public View getListResourceByGuid(@PathVariable String guid, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			List<SalesResource> listRes = salesResourceService.getListResourceByGUID(guid);
			model.addAttribute("data", listRes);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
}
