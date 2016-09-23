package com.gettec.fsnip.fsn.web.controller.rest.business;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.service.business.BrandCategoryService;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.transfer.BusinessBrandTransfer;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.BrandCategoryTreeNode;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

 /**
 * BusinessBrand REST Service API
 * 
 * @author Ryan Wang
 */
@Controller
@RequestMapping("/business")
public class BusinessBrandRESTService extends BaseRESTService{
	@Autowired protected BusinessBrandService businessBrandService;
	@Autowired protected BusinessUnitService businessUnitService;
	@Autowired protected BrandCategoryService brandcategoryService;
	
	/* 按商标ID查找一条商标信息
	 * */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/business-brand/{id}")
	public View get(@PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			BusinessBrand businessBrand =BusinessBrandTransfer.transfer(businessBrandService.findById(id));
			model.addAttribute("data", businessBrand);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 新增商标
	 * @param businessBrand
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/business-brand")
	public View createBusinessBrand(@RequestBody BusinessBrand businessBrand,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 验证当前品牌是否已经存在 */
			if(businessBrandService.validateBrandName(businessBrand)){
				businessBrandService.createBusinessBrand(businessBrand, info);
				businessBrand.setBusinessUnit(null);
				model.addAttribute("data", businessBrand);
			}else{
				resultVO.setErrorMessage("当前品牌名称已经存在，不允许出现相同的品牌！");
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
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
	
	/* 更新商标
	 * */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.PUT, value = "/business-brand")
	public View updateBusinessBrand(@RequestBody BusinessBrand businessBrand,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			//验证当前品牌是否已经存在，返回true表示不存在，false表示已经存在
			if(businessBrandService.validateBrandName(businessBrand)){
				businessBrandService.updateBusinessBrand(businessBrand);
				model.addAttribute("data", businessBrand);
			}else{
				resultVO.setErrorMessage("当前品牌名称已经存在，不允许出现相同的品牌！");
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
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
	 * 删除商标
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/business-brand/{id}")
	public RESTResult<BusinessBrand> delte(@PathVariable("id") Long id) {
		try {
			RESTResult<BusinessBrand> result = null;
			BusinessBrand businessBrand = businessBrandService.findById(id);
			businessBrandService.delete(id);
			result = new RESTResult<BusinessBrand>(RESTResult.SUCCESS, businessBrand);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	/* 查找当前登录企业的某一分页的商标信息
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/business-brand/getMyBrands/{configure}/{page}/{pageSize}")
	public View getListOfMyBrandWithPage(@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long total = businessBrandService.countByOrgnizationId(info.getOrganization(), configure);
			List<BusinessBrand> listOfBrand = businessBrandService.getListOfBrandByOrgnizationIdWithPage(
					info.getOrganization(), configure, page, pageSize);
			Map map = new HashMap();
			map.put("listOfBrand", listOfBrand);
			map.put("counts", total);
			model.addAttribute("data", map);
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
	 * 查找当前登录企业的所有商标信息
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/business-brand/getMyBrandsAll")
	public View getListOfMyBrand(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<BusinessBrand> listOfBrand = businessBrandService.getListOfBrandByOrganization(info.getOrganization());
			model.addAttribute("data", listOfBrand);
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/business-brand/getAllBranByParenOgranizationId/{configure}/{page}/{pageSize}")
	public View getAllBranByParenOgranizationId(@PathVariable(value="page") int page, @PathVariable(value="pageSize") int pageSize, 
			@PathVariable(value="configure") String configure,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO=new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long total = businessBrandService.countByOrgnizationId(info.getOrganization(), configure);
			List<BusinessBrand> listBrand = businessBrandService.getListOfBrandByOrgnizationIdWithPage(
					info.getOrganization(), configure, page, pageSize);
			model.addAttribute("counts", total);
			model.addAttribute("listBrand",listBrand);
		}catch(ServiceException sep){
			resultVO.setErrorMessage(sep.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}catch(Exception e){
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		return JSON;
	}
	
	/**
	 * 验证当前品牌是否已经存在,true表示不存在，false表示存在
	 * @param brandName
	 * @param req
	 * @param resp
	 * @param model
	 * @return JSON
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/validateBrand/{brandName}")
	public View validateBrand(@PathVariable(value="brandName") String brandName,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO=new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			boolean status = businessBrandService.validateBrandName(brandName, info.getOrganization());
			model.addAttribute("status", status);
		}catch(ServiceException sexe){
			resultVO.setErrorMessage(sexe.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}catch(Exception e){
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取所有商标名称
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/business-brand/getAllName")
	public View getAllBrandName(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO=new ResultVO();
		try{
			List<String> listBrandName = businessBrandService.getAllBrandName();
			model.addAttribute("data", listBrandName);
		}catch(ServiceException sexe){
			resultVO.setErrorMessage(sexe.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sexe.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取ProductTreeNode集合
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "tree")
	@ResponseBody
	public List<BrandCategoryTreeNode> tree(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = false) String id) {
		List<BrandCategoryTreeNode> list = null;
		try {
			if (id == null) {
				id = "0_null";
			} else {
				id = URLDecoder.decode(id, "UTF-8");
			}
			String[] array = id.split("_");
			list = brandcategoryService.getTreeNodes(Integer.parseInt(array[0]) + 1, array[1]);
		} catch (ServiceException sexe) {
			((Throwable)sexe.getException()).printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
