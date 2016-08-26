package com.gettec.fsnip.fsn.web.controller.rest.product;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.model.product.ProductionLicenseInfoApplicantClaim;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;
import com.gettec.fsnip.fsn.service.product.ProductLicenseInfoOfApplicantClaimService;
import com.gettec.fsnip.fsn.service.product.ProductLicenseOfApplicantClaimHandleService;
import com.gettec.fsnip.fsn.service.resource.ResourceOfProlicinfoApplicantClaimService;
import com.gettec.fsnip.fsn.vo.product.ProductionLicenseApplicantClaimHandleVO;
import com.gettec.fsnip.fsn.vo.receive.ResultVO;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * 生产许可证申请认领审核处理
 * @author Zhanghui 2015/6/1
 */
@Controller
@RequestMapping("/product/qsno/applicantClaimHandle")
public class ProductLicenseOfApplicantClaimHandleRESTService {
	@Autowired private ProductLicenseOfApplicantClaimHandleService productLicenseOfApplicantClaimHandleService;
	@Autowired private ProductLicenseInfoOfApplicantClaimService productLicenseInfoOfApplicantClaimService;
	@Autowired private ResourceOfProlicinfoApplicantClaimService resourceOfProlicinfoApplicantClaimService;
	
	/**
	 * 功能描述：获取企业认领申请待处理列表
	 * @author Zhanghui 2015/6/2
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@RequestMapping(method=RequestMethod.GET, value="/list/NotProcess/{page}/{pageSize}")
	public View getListOfNotProcess(
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			long total = productLicenseOfApplicantClaimHandleService.countByHandleResult(1); // 1代表 待处理记录
			List<ProductionLicenseApplicantClaimHandleVO> vos = productLicenseOfApplicantClaimHandleService.
					getListByByHandleResultOfPage(1, page, pageSize);
			
			Map map = new HashMap();
			map.put("list", vos);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：获取企业认领申请已退回列表
	 * @author Zhanghui 2015/6/18
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@RequestMapping(method=RequestMethod.GET, value="/list/back/{page}/{pageSize}")
	public View getListOfBack(
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			long total = productLicenseOfApplicantClaimHandleService.countByHandleResult(4); // 4代表审核退回
			List<ProductionLicenseApplicantClaimHandleVO> vos = productLicenseOfApplicantClaimHandleService.
					getListByByHandleResultOfPage(4, page, pageSize);
			
			Map map = new HashMap();
			map.put("list", vos);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：获取企业认领申请审核通过列表
	 * @author Zhanghui 2015/6/18
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@RequestMapping(method=RequestMethod.GET, value="/list/pass/{page}/{pageSize}")
	public View getListOfPass(
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			long total = productLicenseOfApplicantClaimHandleService.countByHandleResult(2); // 2代表 审核通过
			List<ProductionLicenseApplicantClaimHandleVO> vos = productLicenseOfApplicantClaimHandleService.
					getListByByHandleResultOfPage(2, page, pageSize);
			
			Map map = new HashMap();
			map.put("list", vos);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：执行处理操作
	 * @param applicantId    当前正在审核的申请记录id
	 * @param qsId           原生产许可证信息的qsId（审核通过后，此qs信息会被申请时qs信息覆盖）
	 * @param pass 
	 * 			true:  通过审核
	 * 			false: 未通过审核
	 * @param note 处理意见
	 * @author Zhanghui 2015/6/1
	 */
	@RequestMapping(method=RequestMethod.GET, value="/process/{applicantId}/{qs_id}/{pass}/{applicant_bus_name}")
	public View process(
			@PathVariable(value="applicantId") Long applicantId,
			@PathVariable(value="qs_id") Long qs_id,
			@PathVariable(value="pass") boolean pass,
			@PathVariable(value="applicant_bus_name") String applicant_bus_name,
			@RequestParam(required=false) String back_msg,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			if(back_msg != null){
				back_msg = URLDecoder.decode(back_msg, "UTF-8");
			}else{
				back_msg = "";
			}
			
			productLicenseOfApplicantClaimHandleService.executeProcess(applicantId, qs_id, pass, back_msg, applicant_bus_name, info);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：根据生产许可证认领申请的qsId，获取qs信息
	 * @param applicantId 当前正在审核的申请记录id
	 * @author Zhanghui 2015/6/2
	 */
	@RequestMapping(method=RequestMethod.GET, value="/findApplicantQsInfo/{applicantQsId}")
	public View findApplicantQsInfo(
			@PathVariable(value="applicantQsId") Long applicantQsId,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			ProductionLicenseInfoApplicantClaim prolic_applicant = productLicenseInfoOfApplicantClaimService.findById(applicantQsId);
			// 获取图片
			List<ResourceOfProlicinfoApplicantClaim> qsAttachments = resourceOfProlicinfoApplicantClaimService.findByApplicantQsId(applicantQsId);
			if(qsAttachments==null || qsAttachments.size()<1){
				throw new Exception("该认领申请的qs图片不存在！");
			}
			prolic_applicant.setQsAttachments(qsAttachments);
			
			model.addAttribute("data", prolic_applicant);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：获取当前登录企业 qs认领申请处理消息
	 * @author Zhanghui 2015/6/11
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.GET, value="/getmsgs/{page}/{pageSize}")
	public View getmsgs(
			@PathVariable(value="page") int page, 
			@PathVariable(value="pageSize") int pageSize,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			Long myOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			long total = productLicenseOfApplicantClaimHandleService.countWithoutOverdue(myOrganization);
			List<ProductionLicenseApplicantClaimHandleVO> vos = productLicenseOfApplicantClaimHandleService.
					getListWithoutOverdueByPage(myOrganization, page, pageSize);
			
			Map map = new HashMap();
			map.put("list", vos);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 功能描述：检查企业在此之前有无申请认领该qs号
	 * @author Zhanghui 2015/6/11
	 */
	@RequestMapping(method=RequestMethod.GET, value="/checkhasApplicant/{qs_id}/{applicant_bus_id}")
	public View checkHasApplicant(
			@PathVariable(value="qs_id") Long qs_id, 
			@PathVariable(value="applicant_bus_id") Long applicant_bus_id,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			if(qs_id==null || applicant_bus_id==null){
				throw new Exception("参数异常");
			}
			
			ProductionLicenseApplicantClaimHandleVO vo = productLicenseOfApplicantClaimHandleService.findOfNotOverdue(
					qs_id, applicant_bus_id);
			
			if(vo != null && vo.getHandle_result()!=4){
				model.addAttribute("data", vo);
			}else{
				model.addAttribute("data", null);
			}
		} catch (Exception e) {
			resultVO.setSuccess(false);
			resultVO.setMessage("失败！");
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
}
