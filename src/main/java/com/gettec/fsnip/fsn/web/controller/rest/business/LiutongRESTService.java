package com.gettec.fsnip.fsn.web.controller.rest.business;

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

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;
import com.gettec.fsnip.fsn.model.business.LiutongToProduce;
import com.gettec.fsnip.fsn.model.business.LiutongToProduceLicense;
import com.gettec.fsnip.fsn.service.business.LiutongFieldValueService;
import com.gettec.fsnip.fsn.service.business.LiutongToProduceLicenseService;
import com.gettec.fsnip.fsn.service.business.LiutongToProduceService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.LiutongVO;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

@Controller
@RequestMapping("/liutong")
public class LiutongRESTService extends BaseRESTService{

	@Autowired LiutongFieldValueService liutongFieldValueService;
	@Autowired LiutongToProduceService liutongToProduceService;
	@Autowired LiutongToProduceLicenseService liutongToProduceLicenseService;
	
	/**
	 * 获得当前流通企业下的生产企业列表
	 * @param name
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListProducer/{configure}/{page}/{pageSize}")
	public View getListProducer(@PathVariable(value="page")int page,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long counts = liutongToProduceService.countByOrgIdAndCondition(info.getOrganization(), configure);
		    List<LiutongToProduce> listProducer= liutongToProduceService
		    		.getProducerByOrganizationWithPage(info.getOrganization(), configure, page, pageSize);
		    model.addAttribute("counts", counts);
		    model.addAttribute("data", listProducer);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 根据生产企业id获取生产企业信息
	 * @param name
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getProducerById/{id}")
	public View getProducerById(@PathVariable(value="id") long id, @RequestParam(value="organization",required=false) Long organization,
			Model model, HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			if(organization == null) organization = info.getOrganization();
			List<LiutongFieldValue> listFieldValue = liutongFieldValueService.getByProducerId(id);
			List<LiutongToProduceLicense> listQs = liutongToProduceLicenseService.getByOrganizationAndProducerId(organization, id);
		    model.addAttribute("listFieldValue", listFieldValue);
		    model.addAttribute("listQs", listQs);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 流通企业完善生产企业信息完整度
	 * @param liutongVo
	 * @param model
	 * @param req
	 * @param resp
	 * @return View
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveModel")
	public View create(@RequestBody LiutongVO liutongVo, Model model, 
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		/*try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			 1.保存[生产企业-营业执照号]、[生产企业-组织机构]关系 
			liutongFieldValueService.save(liutongVo.getFieldValues());
			 2.保存[流通企业-生产企业-qs号]关系  
			liutongToProduceLicenseService.save(liutongVo.getListLtQs(), info.getOrganization());
			 3.保存[流通企业-生产企业]关系 
			liutongToProduceService.save(liutongVo.getLiutongToProduce(), info.getOrganization());
		    model.addAttribute("data", liutongVo);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}*/
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * testlab发布报告时，审核通过企业信息
	 * @param organization
	 * @param produceId
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/approved")
	public View approved(@RequestBody LiutongVO liutongVO, @RequestParam(value="orgId",required=false) Long orgId, 
			@RequestParam(value="produceId",required=false) Long produceId,
			@RequestParam(value="passFlag",required=false) boolean passFlag, Model model, 
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		/*try {
			if(liutongVO!=null){
				如果审核通过，改变生产企业的审核状态
				if(passFlag){
					for(LiutongFieldValue fv :liutongVO.getFieldValues()){
						fv.setMsg("");
						fv.setPassFlag("审核通过");
					}
					for(LiutongToProduceLicense qs : liutongVO.getListLtQs()){
						qs.setMsg("");
						qs.setPassFlag("审核通过");
					}
					liutongVO.getLiutongToProduce().setMsg("审核通过");
				}else{
					liutongVO.getLiutongToProduce().setMsg("审核退回");
				}
				 1. 更新营业执照信息、组织机构信息，并将审核标记改为通过 
				liutongFieldValueService.approved(liutongVO.getFieldValues());
				 2. 将生产许可证的审核标记改为通过 
				liutongToProduceLicenseService.approved(liutongVO.getListLtQs());
				 3. 将生产企业的审核标记改为通过 
				liutongToProduceService.approved(liutongVO.getLiutongToProduce());
			}else{
				liutongFieldValueService.approvedByProducerId(produceId, passFlag);
				liutongToProduceLicenseService.approvedByOrgIdAndProduceId(orgId, produceId, passFlag);
				LiutongToProduce produce = new LiutongToProduce();
				produce.setOrganization(orgId);
				produce.setProducerId(produceId); 
				produce.setMsg(passFlag?"审核通过":"审核退回");
				liutongToProduceService.approved(produce);
			}
			
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}*/
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 流通企业发布报告时，验证报告的生产企业信息是否完整
	 * @param id
	 * @param model
	 * @param req
	 * @param resp
	 * @return View
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/validateProducerByReportId/{id}")
	public View validateProducerByReportId(@PathVariable(value="id") long id, Model model, 
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		/*try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			LiutongToProduce producer = liutongToProduceService.validateProducerByReportId(id, info.getOrganization());
		    model.addAttribute("producer", producer);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}*/
		model.addAttribute("result", resultVO);
		return JSON;	
	}
}
