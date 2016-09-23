package com.gettec.fsnip.fsn.web.controller.rest.sales;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.File;
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
import com.gettec.fsnip.fsn.service.business.impl.BusinessUnitServiceImpl;
import com.gettec.fsnip.fsn.service.sales.ContractService;
import com.gettec.fsnip.fsn.service.sales.ElectronicDataService;
import com.gettec.fsnip.fsn.service.sales.SalesResourceService;
import com.gettec.fsnip.fsn.util.sales.AnnexDownLoad;
import com.gettec.fsnip.fsn.util.sales.BaseMailDefined;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.sales.ContractVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 电子资料
 * @author tangxin 2015/04/24
 *
 */
@Controller
@RequestMapping("/sales/electdate")
public class ElectronicDataRESTService {
	
	@Autowired private ContractService contractService;
	@Autowired private SalesResourceService salesResourceService;
	@Autowired private ElectronicDataService electronicDataService;
	@Autowired private BusinessUnitServiceImpl businessUnitServiceImpl;
	
	/**
	 * 创建电子合同信息
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/contract/create")
	public View createContract(@RequestBody ContractVO contractVO,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			contractVO = contractService.save(contractVO, info, true);
			model.addAttribute("data", contractVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 更新电子合同信息
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/contract/update")
	public View updateContract(@RequestBody ContractVO contractVO, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			contractVO = contractService.save(contractVO, info, false);
			model.addAttribute("data", contractVO);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 分页查询企业合同信息
	 * @author tangxin 2015-04-27
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/contract/getListContract/{configure}/{page}/{pageSize}")
	public View getListBranch(@PathVariable String configure, @PathVariable Integer page, @PathVariable Integer pageSize,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long totals = contractService.countByConfigure(info.getOrganization(), configure);
			List<ContractVO> listContractVO = contractService.getListByOrganizationWithPage(info.getOrganization(), configure, page, pageSize);
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
	 * 验证合同名称是否重复
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/contract/countName/{name}/{id}")
	public View countContractName(@PathVariable String name, @PathVariable Long id, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long count = contractService.countByName(name, info.getOrganization(), id);
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
	 * 删除电子合同信息(假删除)
	 * @author tangxin 2015-04-29
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/contract/delContractById/{contractId}")
	public View delBranchById(@PathVariable Long contractId, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = contractService.removeById(contractId, info);
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

	/**
	 * 根据企业获取所有的电子合同和电子资料
	 * @author HY 2015-04-30
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListContracts")
	public View getListContractsAndElectMater( HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			model = salesResourceService.getListContractsAndElectMater(info, model);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 打包下载用户勾选的电子合同和电子资料
	 * @param resId 用户勾选的内容
	 * @author HY 2015-04-30
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/downLoadElectData/{resIdStr}")
	public void downLoadElectData(@PathVariable String resIdStr,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		File file = null;
		try{
			if(resIdStr != null && !"".equals(resIdStr)){
				AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
				String enterName = businessUnitServiceImpl.findNameByOrganization(info.getOrganization());
				file = salesResourceService.downLoadElectData(resIdStr, enterName);
				resp.reset();
				resp = AnnexDownLoad.downloadZip(file, resp);
				resp.flushBuffer();
			}
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} finally{
            try {
               if(file != null) {
            	   AnnexDownLoad.delFolder(file.getParentFile().getPath());
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	/**
	 * 发送电子邮件
	 * @author tangxin 2015-05-10
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/sendEmail")
	public View sendEmail(@RequestBody BaseMailDefined baseMail, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = null;
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			resultVO = electronicDataService.sendMail(baseMail, info);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
