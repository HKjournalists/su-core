package com.gettec.fsnip.fsn.web.controller.rest.member;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_MEMBER_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_MEMBER_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.sql.Update;
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
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.member.Member;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.LicenceFormatService;
import com.gettec.fsnip.fsn.service.erp.FromToBussinessService;
import com.gettec.fsnip.fsn.service.erp.UnitService;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.member.MemberService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.member.MemberManageViewVO;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

 /**
 * Member REST Service API
 * 
 * @author HCJ
 */
@Controller
@RequestMapping("/member")
public class MemberRESTService extends BaseRESTService{
	@Autowired private MemberService memberService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private UnitService unitService;
	@Autowired private MkCategoryService mkCategoryService;
	@Autowired private LicenceFormatService licenceFormatService;
	@Autowired private FromToBussinessService fromToBusService;
	@Autowired private UpdataReportService updataReportService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired private StatisticsClient staClient;
	private final static Logger logger = Logger.getLogger(MemberRESTService.class);
	
	/**
	 * 根据Id查找人员信息
	 * @param id
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public View get(@PathVariable Long id,@RequestParam(required=false,defaultValue="FSN",value="identify") String identify, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			Member member = memberService.findByMemberId(id,identify);
			model.addAttribute("data", member);
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
	 * 背景：人员新增/编辑页面
	 * 功能描述：新增人员
	 * @param enterpriseName 当前正在执行人员新增操作的企业名称
	 * @author ZhangHui 2015/6/3
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{current_business_name}/{isNew}")
	public View createMember(
			@RequestBody Member member,
			@PathVariable String current_business_name,
			@PathVariable boolean isNew,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		
		ResultVO resultVO = new ResultVO();
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		String errorMsg="";//定义错误消息 报错时 记录到日志表
		try{
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			// 第一步：上传人员图片
			if(member==null || member.getIdentificationNo()==null || "".equals(member.getIdentificationNo().replace(" ", ""))){
				throw new Exception("参数为空");
			}
			
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_MEMBER_PATH) + "/" + member.getIdentificationNo();
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_MEMBER_PATH) + "/" + member.getIdentificationNo();
			//保存证件照
			for (Resource resource : member.getHdAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = member.getIdentificationNo() + "-" + randomStr + "." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setUrl(url);
						resource.setName(name);
					}else{
						throw new Exception("证件照图片上传失败");
					}
				}
			}
			//保存健康证
			for (Resource resource : member.getHthAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = member.getIdentificationNo() + "-" + randomStr + "." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setUrl(url);
						resource.setName(name);
					}else{
						throw new Exception("健康证图片上传失败");
					}
				}
				resource.setOrigin(member.getHealthNo());
			}
			//保存从业资格证
			for (Resource resource : member.getQcAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = member.getIdentificationNo() + "-" + randomStr + "." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setUrl(url);
						resource.setName(name);
					}else{
						throw new Exception("从业资格证图片上传失败");
					}
				}
				resource.setOrigin(member.getQualificationNo());
			}
			int i = 1;
			//保存荣誉证书
			for (Resource resource : member.getHnAttachments()) {
				if(i == 1){
					resource.setOrigin(member.getHonorNo1());
				}else if(i== 2){
					resource.setOrigin(member.getHonorNo2());
				}else if(i== 3){
					resource.setOrigin(member.getHonorNo3());
				}
				i++;
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = member.getIdentificationNo() + "-" + randomStr + "." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setUrl(url);
						resource.setName(name);
						
					}else{
						throw new Exception("荣誉证书图片上传失败");
					}
				}
			}
			
			uploadUtil.close();
			Long orgId = businessUnitService.findIdByOrg(currentUserOrganization);
			member.setOrgId(orgId);
			// 第三步：保存人员信息
			memberService.saveMember(member, current_business_name, currentUserOrganization, isNew);
			
			model.addAttribute("data", member);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			errorMsg=errorMsg+"&&&&"+e.getMessage();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
	public ResultVO delte(@PathVariable("id") Long id,Model model,HttpServletRequest req, 
			HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			memberService.delete(id);
			//resultVO.setStatus(SERVER_STATUS_SUCCESS);
			//return resultVO;
		} catch (ServiceException sex) {
			((Throwable) sex.getException()).printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			//return null;
		}
		model.addAttribute("result", resultVO);
		return resultVO;
	}

	/**
	 * 查找当前登录企业的所有人员信息
	 * @author HCJ 2016-5-19
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping(method = RequestMethod.GET, value = "/getMembers/{configure}/{page}/{pageSize}/{key}")
	public View getListOfMyMember(@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize,
								  @PathVariable(value="key") String key,
								  @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			key = key.substring(key.indexOf("=")+1);
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			Long orgId = businessUnitService.findIdByOrg(currentUserOrganization);
			//long total = memberService.count(orgId, configure);
			long total =memberService.countByHotWord(orgId,key);
			//List<MemberManageViewVO> listOfMember = memberService.getLightMemberVOsByPage(page, pageSize, orgId);
			List<Member>  listOfMember=  memberService.getListByHotWordWithPage(orgId,page,pageSize,key);
			Map map = new HashMap();
			map.put("listOfMember", listOfMember);
			map.put("counts",total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/getAllMember/{key}")
	public View getAllMember(@PathVariable(value="key") String key,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
//			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			key = key.substring(key.indexOf("=")+1);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			Long orgId = businessUnitService.findIdByOrg(currentUserOrganization);
			List<Member> listOfMember = memberService.getLightMemberVOsByPage(orgId,key);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("listOfMember", listOfMember);
			map.put("key", key);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	 @SuppressWarnings("unused")
	 @RequestMapping(method = RequestMethod.GET, value = "/queryMemberByJg")
	 public View queryMemberByJg(@RequestParam(value="name") String name,
								 @RequestParam(value="credentialsType") String credentialsType,
								 @RequestParam(value="identificationNo") String identificationNo,
								 HttpServletRequest req, HttpServletResponse resp, Model model) {
		 ResultVO resultVO = new ResultVO();
		 try{

			 model.addAttribute("data", "");
		 } catch (Exception e) {
			 resultVO.setErrorMessage(e.getMessage());
			 resultVO.setStatus(SERVER_STATUS_FAILED);
		 }
		 model.addAttribute("result", resultVO);
		 return JSON;
	 }

}