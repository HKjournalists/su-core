package com.gettec.fsnip.fsn.web.controller.rest.business;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gettec.fsnip.fsn.service.market.ResourceService;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessMarket;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.BusinessUnitInfoLog;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.business.MarketToBusiness;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.base.DistrictService;
import com.gettec.fsnip.fsn.service.base.OfficeService;
import com.gettec.fsnip.fsn.service.base.SysAreaService;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessMarketService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.CirculationPermitService;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.business.FieldValueService;
import com.gettec.fsnip.fsn.service.business.LicenseService;
import com.gettec.fsnip.fsn.service.business.LiquorSalesLicenseService;
import com.gettec.fsnip.fsn.service.business.MarketToBusinessPKService;
import com.gettec.fsnip.fsn.service.business.OrgInstitutionService;
import com.gettec.fsnip.fsn.service.business.ProducingDepartmentService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.business.TaxRegisterService;
import com.gettec.fsnip.fsn.service.market.FtpService;
import com.gettec.fsnip.fsn.service.product.BusinessCertificationService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.service.sales.BusinessSalesInfoService;
import com.gettec.fsnip.fsn.util.FsnUtil;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.vo.ProductCertificationVO;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.BusinessMarketVO;
import com.gettec.fsnip.fsn.vo.business.BusinessTreeNode;
import com.gettec.fsnip.fsn.vo.business.BusinessUnitLIMSVO;
import com.gettec.fsnip.fsn.vo.business.ExlVO;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.fsn.ws.SmsUtils;
import com.gettec.fsnip.fsn.ws.vo.Message;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.business.LightBusUnitVO;

 /**
 * BusinessUnit REST Service API
 * 
 * @author Ryan Wang
 */
@Controller
@RequestMapping("/business")
public class BusinessUnitRESTService extends BaseRESTService{
	@Autowired protected BusinessUnitService businessUnitService;
	@Autowired protected LicenseService licenseService;
	@Autowired protected OrgInstitutionService orgInstitutionService;
	@Autowired protected CirculationPermitService circulationPermitService;
	@Autowired protected ProductionLicenseService productionLicenseService;
	@Autowired protected EnterpriseRegisteService enterpriseService;
	@Autowired protected FieldValueService fieldValueService;
	@Autowired protected TaxRegisterService taxRegisterService;
	@Autowired protected LiquorSalesLicenseService liquorSalesLicenseService;
	@Autowired protected ProducingDepartmentService producingDepartmentService;
	@Autowired protected BusinessBrandService businessBrandService;
	@Autowired protected FtpService ftpService;
	@Autowired protected BusinessCertificationService businessCertificationService;
	@Autowired protected ProductTobusinessUnitService productTobusinessUnitService;
	@Autowired protected BusinessMarketService businessMarketService;
	@Autowired protected SysAreaService sysAreaService;
	@Autowired protected OfficeService officeService;
	@Autowired protected DistrictService districtService;
	@Autowired private JavaMailSender javaMailSender;
    @Autowired private VelocityEngine velocityEngine;
    @Autowired private MarketToBusinessPKService marketToBusinessPKService;
    @Autowired protected BusinessSalesInfoService businessSalesInfoService;
    @Autowired private ProductTobusinessUnitService productToBusinessUnitService;
    @Autowired private StatisticsClient staClient;
	 @Autowired protected ResourceService testResourceService;

	@RequestMapping(method = RequestMethod.GET, value = "business-unit/{id}")
	public BusinessUnit get(@PathVariable Long id) {
		try {
			BusinessUnit businessUnit = businessUnitService.findById(id);
			return businessUnit;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "business-unit")
	public RESTResult<BusinessUnit> create(@RequestParam("model") BusinessUnit businessUnit) {
		try {
			RESTResult<BusinessUnit> result = null;
			businessUnitService.create(businessUnit);
			result = new RESTResult<BusinessUnit>(RESTResult.SUCCESS, businessUnit);
			result.setSuccess(true);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/business-unit")
	public View update(@RequestBody BusinessUnit businessUnit,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		String errorMsg="";//定义错误消息 报错时 记录到日志表
		try {
			BusinessUnit orig_businessUnit = businessUnitService.findById(businessUnit.getId());
			/* 1. 向sso发送请求，更新该企业在sso的基本信息  */
			//JSONObject jsonResult = businessUnitService.updateBusInfoInSSO(businessUnit, orig_businessUnit);
			/* 2.更新 fsn端该企业的信息  */
			//if(jsonResult.getString("status").equals("true")){
				/* 2.1 保存营业执照信息 */
				licenseService.save(businessUnit.getLicense());
				/* 2.2 保存织机构代码信息  */
				orgInstitutionService.save(businessUnit.getOrgInstitution(), true);
				/* 2.3 保存食品流通许可证信息 */
				circulationPermitService.save(businessUnit.getDistribution());
				/* 2.4 保存多条生产许可证信息 */
				/*productionLicenseService.save(businessUnit.getProductionLicenses(), orig_businessUnit.getProductionLicenses());*/
				/* 2.5保存企业其他认证信息*/
				businessCertificationService.save(businessUnit);
				/* 保存销售系统中的 宣传片片和业业二码码*/
				/*businessSalesInfoService.save(businessUnit,info);*/
				/* 2.6 保存税务登记证信息 */
				taxRegisterService.save(businessUnit.getTaxRegister());
				if(orig_businessUnit.getType().equals("仁怀市白酒生产企业")){
					/* 2.6 保存税务登记证信息 */
					taxRegisterService.save(businessUnit.getTaxRegister());
					/* 2.67保存酒类销售信息 */
				    liquorSalesLicenseService.save(businessUnit.getLiquorSalesLicense());
				    /* 2.8保存生产车间布点信息 */
				    producingDepartmentService.save(businessUnit, businessUnit.getStep());
				    /* 2.9 保存认可的‘挂靠’酒厂基本情况登记表信息 */
				    /*producingDepartmentService.save(businessUnit.getSubDepartments(), businessUnit.getId(),false);*/
				    /* 3.0 保存企业扩展信息 */
					fieldValueService.save(businessUnit.getFieldValues(), businessUnit.getId());
					/* 3.1 保存企业品牌信息 */
				    //  businessBrandService.save(businessUnit.getBrands(), businessUnit.getId());
					/* 3.2 保存企业基本信息 */
					businessUnitService.updateBusinessUnit(businessUnit, businessUnit.getStep());
				}else{
					businessUnitService.updateBusinessUnit(businessUnit); // 保存企业基本信息
				}
				enterpriseService.update(businessUnit);  // 保存图片信息
			//}
			businessUnit=businessUnitService.updateBusinessUnitAll(businessUnit, info);
			model.addAttribute("data", businessUnit);
		} catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
			errorMsg=((Throwable) sex.getException()).getMessage();
		}catch(Exception e){
			e.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			errorMsg=errorMsg+"&&&&"+e.getMessage();
		}finally{
			
			/**
			 * 记录企业日志
			 * @author longxianzhen 2015/06/04
			 */
			BusinessUnitInfoLog logData=new BusinessUnitInfoLog(info.getUserName(), "进行更新企业操作",errorMsg, HttpUtils.getIpAddr(req),businessUnit);
			staClient.offer(logData);//记录企业日志异步的
			
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	 /**
	  * 保存企业基本信息
	  * @param businessUnit
	  * @param model
	  * @param req
	  * @param resp
      * @return
      */
	 @RequestMapping(method = RequestMethod.PUT, value = "/saveBusinessbasBasic")
	 public View updateBasic(@RequestBody BusinessUnit businessUnit,
						Model model,HttpServletRequest req,HttpServletResponse resp) {
		 ResultVO resultVO = new ResultVO();
		 AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		 String errorMsg="";//定义错误消息 报错时 记录到日志表
		 try {
			 businessUnitService.updateBusinessBasic(businessUnit,info); // 保存企业基本信息
			 model.addAttribute("data", businessUnit);
		 } catch(ServiceException sex){
			 resultVO.setStatus(SERVER_STATUS_FAILED);
			 resultVO.setSuccess(false);
			 resultVO.setErrorMessage(sex.getMessage());
			 ((Throwable) sex.getException()).printStackTrace();
			 errorMsg=((Throwable) sex.getException()).getMessage();
		 }catch(Exception e){
			 e.printStackTrace();
			 resultVO.setStatus(SERVER_STATUS_FAILED);
			 resultVO.setSuccess(false);
			 errorMsg=errorMsg+"&&&&"+e.getMessage();
		 }finally{

			 /**
			  * 记录企业日志
			  * @author longxianzhen 2015/06/04
			  */
			 BusinessUnitInfoLog logData=new BusinessUnitInfoLog(info.getUserName(), "进行更新企业操作",errorMsg, HttpUtils.getIpAddr(req),businessUnit);
			 staClient.offer(logData);//记录企业日志异步的

		 }
		 model.addAttribute("result", resultVO);
		 return JSON;
	 }

	 /**
	  * 保存企业证照信息
	  * @param businessUnit
	  * @param model
	  * @param req
	  * @param resp
      * @return
      */
	 @RequestMapping(method = RequestMethod.PUT, value = "/saveBusinessbasCert")
	 public View saveBusinessbasCert(@RequestBody BusinessUnit businessUnit,
							 Model model,HttpServletRequest req,HttpServletResponse resp) {
		 ResultVO resultVO = new ResultVO();
		 AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		 String errorMsg="";//定义错误消息 报错时 记录到日志表
		 try {
			 businessUnitService.updateBusinessCert(businessUnit,info); // 保存企业证照信息
			 testResourceService.saveBusinessCert(businessUnit);
			 model.addAttribute("data", businessUnit);
		 } catch(ServiceException sex){
			 resultVO.setStatus(SERVER_STATUS_FAILED);
			 resultVO.setSuccess(false);
			 resultVO.setErrorMessage(sex.getMessage());
			 ((Throwable) sex.getException()).printStackTrace();
			 errorMsg=((Throwable) sex.getException()).getMessage();
		 }catch(Exception e){
			 e.printStackTrace();
			 resultVO.setStatus(SERVER_STATUS_FAILED);
			 resultVO.setSuccess(false);
			 errorMsg=errorMsg+"&&&&"+e.getMessage();
		 }finally{

			 /**
			  * 记录企业日志
			  * @author longxianzhen 2015/06/04
			  */
			 BusinessUnitInfoLog logData=new BusinessUnitInfoLog(info.getUserName(), "进行更新企业操作",errorMsg, HttpUtils.getIpAddr(req),businessUnit);
			 staClient.offer(logData);//记录企业日志异步的

		 }
		 model.addAttribute("result", resultVO);
		 return JSON;
	 }
	
	@RequestMapping(method = RequestMethod.DELETE, value = "business-unit/{id}")
	public RESTResult<BusinessUnit> delte(@PathVariable("id") Long id) {
		try {
			RESTResult<BusinessUnit> result = null;
			BusinessUnit businessUnit = businessUnitService.findById(id);
			businessUnitService.delete(id);
			result = new RESTResult<BusinessUnit>(RESTResult.SUCCESS, businessUnit);
			return result;
		} catch (ServiceException sex) {
			return null;
		}
	}
	
	/**
	 * 按企业名称模糊查询企业信息
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "business-unitLIMS/{name}")
	public View getByName(@PathVariable("name") String name,Model model) {
		
		try {
			name=URLDecoder.decode(name, "UTF-8");
			List<BusinessUnit> businessUnits = businessUnitService.findByName_(name);
			if(businessUnits==null){
				model.addAttribute("status", SERVER_STATUS_FAILED);
				return JSON;
			}
			List<BusinessUnitLIMSVO> buLimss= new ArrayList<BusinessUnitLIMSVO>();
			for(BusinessUnit bu:businessUnits){
				BusinessUnitLIMSVO buLims=new BusinessUnitLIMSVO();
				buLims.setName(bu.getName()==null?"":bu.getName());
				buLims.setLicenseNo(bu.getLicense()==null?"":bu.getLicense().getLicenseNo());
				buLims.setAddress(bu.getAddress()==null?"":bu.getAddress());
				//buLims.setQsNo(bu.getProductionLicense()==null?"":bu.getProductionLicense().getQsNo());
				buLims.setContact(bu.getContact()==null?"":bu.getContact());
				buLimss.add(buLims);
			}
			model.addAttribute("businessUnits", buLimss);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
			model.addAttribute("size",buLimss.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "business-unitLIMS/{name}/{type}")
	public View getByName(@PathVariable("name") String name,@PathVariable("type") String type,Model model) {
		
		try {
			name=URLDecoder.decode(name, "UTF-8");
			List<BusinessUnit> businessUnits = businessUnitService.getListByName(name,type,1,10);
			if(businessUnits==null){
				model.addAttribute("status", SERVER_STATUS_FAILED);
				return JSON;
			}
			List<BusinessUnitLIMSVO> buLimss= new ArrayList<BusinessUnitLIMSVO>();
			for(BusinessUnit bu:businessUnits){
				String displayName=(bu.getLicense()==null?"无照":bu.getLicense().getLicenseNo())+"("+bu.getName()+")";
				BusinessUnitLIMSVO buLims=new BusinessUnitLIMSVO();
				buLims.setDisplayName(displayName);
				buLims.setName(bu.getName()==null?"":bu.getName());
				buLims.setLicenseNo(bu.getLicense()==null?"":bu.getLicense().getLicenseNo());
				buLims.setAddress(bu.getAddress()==null?"":bu.getAddress());
				buLims.setContact(bu.getContact()==null?"":bu.getContact());
				buLims.setAdministrativeLevel(bu.getAdministrativeLevel()==null?"":bu.getAdministrativeLevel());
				buLims.setDistributionNo(bu.getDistribution()==null?"":bu.getDistribution().getDistributionNo());
				buLims.setEmail(bu.getEmail()==null?"":bu.getEmail());
				buLims.setFax(bu.getFax()==null?"":bu.getFax());
				buLims.setTel(bu.getTelephone()==null?"":bu.getTelephone());
				buLims.setPersonInCharge(bu.getPersonInCharge()==null?"":bu.getPersonInCharge());
				buLims.setPostalCode(bu.getPostalCode()==null?"":bu.getPostalCode());
				buLims.setRegion(bu.getRegion()==null?"":bu.getRegion());
				buLims.setSampleLocal(bu.getSampleLocal()==null?"":bu.getSampleLocal());
				buLims.setNote(bu.getNote());
				buLimss.add(buLims);
			}
			model.addAttribute("businessUnits", buLimss);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return JSON;
	}
	

	/**
	 * 获得当前企业下的所有子企业集合有分页处理
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/subsidiary/{page}/{pageSize}/{configure}")
	public View getSubsidiaryListPage(@PathVariable(value="page")int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		    Long org=Long.parseLong(AccessUtils.getUserRealOrg().toString());
		    List<BusinessUnit> subsidiary = businessUnitService.getSubsidiaryListByOrgPage(page,pageSize,configure,org);
		    Long totalCount = businessUnitService.getCountByOrg(org,configure);
		    Map<String,Object> map = new HashMap<String,Object>();
		    map.put("subsidiarylist", subsidiary);
		    map.put("totalCount", totalCount);
		    model.addAttribute("data", map);
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
	 * 新增旗下企业
	 * @param businessUnit
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.POST, value = "/subsidiary/add")
	public View addSubsidiary(@RequestBody BusinessUnit businessUnit,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		    Long parentOrg=Long.parseLong(AccessUtils.getUserRealOrg().toString());
			businessUnitService.addSubsidiary(businessUnit,parentOrg);
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
	 * 编辑旗下企业
	 * @param businessUnit
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/subsidiary/edit")
	public View editSubsidiary(@RequestBody BusinessUnit businessUnit,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			businessUnitService.editSubsidiary(businessUnit);
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
	 * 删除旗下企业（假删除 只是把父组织机构id置空）
	 * @author longxianzhen 2015/06/18
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/subsidiaryBusiness/{id}")
	public View deleteSubsidiary(@PathVariable(value="id")Long id,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			BusinessUnit bu=businessUnitService.findById(id);
			bu.setParentOrganizationId(null);
			businessUnitService.update(bu);
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
	 * 验证企业名称或营业执照号唯一
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationNameOrLic/{val}/{type}")
	public View verificationNameOrLic(@PathVariable(value="val")String val,@PathVariable(value="type")String type,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		   // name=URLEncoder.encode(name, "UTF-8");
			boolean isExist =businessUnitService.verificationNameOrLic(val,type);
			model.addAttribute("isExist", isExist);
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
	 * 企业用户注册
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/enterpriseRegiste")
	public View enterpriseRegiste(@RequestBody EnterpriseRegiste enRegiste,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			businessUnitService.saveEnterpriseRegisteInfo(enRegiste);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		}catch(ServiceException sex){
			model.addAttribute("status", SERVER_STATUS_FAILED);
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			model.addAttribute("status", SERVER_STATUS_FAILED);
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 获得所有注册企业集合有分页处理
	 * @param name
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/enRegiste/{page}/{pageSize}/{configure}")
	public View getEnRegisteListPage(@PathVariable(value="page")int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		    List<EnterpriseRegiste> enRegiste = businessUnitService.getEnRegisteListByPage(page,pageSize,configure);
		    Long totalCount = businessUnitService.getAllCount(configure);
		    Map<String,Object> map = new HashMap<String,Object>();
		    map.put("enRegisteList", enRegiste);
		    map.put("totalCount", totalCount);
		    model.addAttribute("data", map);
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
	 * 根据Id获取注册企业
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getEnRegisteById/{id}")
	public View getEnRegisteById(@PathVariable Long id,Model model,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			EnterpriseRegiste enRegiste = enterpriseService.findById(id);
			if(enRegiste.getStatus().equals("审核通过")){
				boolean signFlag = businessUnitService.findSignFlagByName(enRegiste.getEnterpriteName());
				enRegiste.setSignFlag(signFlag);
			}
			model.addAttribute("enRegiste", enRegiste);
		} catch(ServiceException sex){
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
	 * 审核通过一个企业
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/spprove/{id}/{signFlag}")
	public View spprove(@PathVariable Long id,Model model, @PathVariable boolean signFlag,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		String errorMsg="";//定义错误消息 报错时 记录到日志表
		BusinessUnit business =null;
		try {
			/* 1. 审核 */
			EnterpriseRegiste enRegiste = businessUnitService.spprove(id, signFlag);
			if(enRegiste != null){
				/* 2.发送短信 */
				Message message = new Message();
				message.setMobile(enRegiste.getTelephone());
			    message.setContent(FsnUtil.getSendMessage(enRegiste.getUserName(), enRegiste.getPassword(), enRegiste.getEnterpriteName()));
				SmsUtils smsUtils =new SmsUtils(message);
				try {
					//异步短信发送
					smsUtils.SyncSendSms();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			business=businessUnitService.findByName(enRegiste.getEnterpriteName());
		} catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			if(sex.getException()!=null){
				((Throwable) sex.getException()).printStackTrace();
				errorMsg=((Throwable) sex.getException()).getMessage();
			}
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			e.printStackTrace();
			errorMsg=errorMsg+"&&&&"+e.getMessage();
		}finally{
			/**
			 * 记录企业日志
			 * @author longxianzhen 2015/06/04
			 */
			BusinessUnitInfoLog logData=new BusinessUnitInfoLog(info.getUserName(),"进行审核通过企业操作",errorMsg,HttpUtils.getIpAddr(req), business);
			staClient.offer(logData);//记录企业日志异步的
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/*
	 * 批量审核注册企业功能
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/batchEnterpriseSpprove")
	public void BatchEnterpriseSpprove(HttpServletRequest req) throws Exception {
		
		   try {
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("condition", " WHERE e.status='待审核' AND e.origin like '%jgxt%' ");
			   
			List<EnterpriseRegiste> enRegisteList = enterpriseService.getEnRegisteListByPage(1,4000,map);
			
			for (int i = 0; i < enRegisteList.size(); i++) {
				EnterpriseRegiste enRegiste = null;
				/* 1. 审核 */
				try {
					//System.out.println(enRegisteList.get(i).getEnterpriteName());
					 enRegiste = businessUnitService.spprove(enRegisteList.get(i).getId(), false);
				} catch (Exception e) {
					e.printStackTrace();
					 continue;
				}
				/*if(enRegiste != null){
					 2.发送短信 
					Message message = new Message();
					message.setMobile(enRegiste.getTelephone());
				    message.setContent(FsnUtil.getSendMessage(enRegiste.getUserName(), enRegiste.getPassword(), enRegiste.getEnterpriteName()));
					SmsUtils smsUtils =new SmsUtils(message);
					try {
						//异步短信发送
						smsUtils.SyncSendSms();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}*/
				Thread.sleep(1000);
				
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	 
	/**
	 * 更新企业签名状态
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateSignStatus/{busName}/{signFlag}/{passFlag}")
	public View updateSignStatus(@PathVariable String busName, @PathVariable boolean signFlag,
			@PathVariable boolean passFlag,
			Model model, HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			boolean status = businessUnitService.updateSignStatus(busName, signFlag,passFlag);
			resultVO.setSuccess(status);
		} catch(ServiceException sex){
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
	 * 根据组织机构ID获取企业
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getBusinessByOrg")
	public View getBusinessByOrg(Model model,
			@RequestParam(value="organization", required = false) Long organization,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			BusinessUnit businessUnit = null;
			if(organization != null){
				// 获取目标组织机构对应企业的信息（不包括qs号信息）
				info.setOrganization(organization);
				businessUnit = businessUnitService.findByInfo(info, true, false);
			}else{
				// 获取当前登录企业信息（不包括qs号信息）
				Long currentUserOrganization=Long.parseLong(AccessUtils.getUserRealOrg().toString());
				info.setOrganization(currentUserOrganization);
				businessUnit = businessUnitService.findByInfo(info, true, true);
			}
			
			String type = businessUnit.getType();
			String address = businessUnit.getAddress();
			/*如果是贵州流通企业，需要查询所选交易市场*/
			if(type!=null&&type.equals("流通企业")&&address!=null&&address.contains("贵州省")){
				BusinessMarketVO marketVO = businessMarketService.findVOByBusinessId(businessUnit.getId());
				if(marketVO != null) businessUnit.setMarketOrg(marketVO.getOrganization());
			}
			model.addAttribute("data", businessUnit);
		} catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据组织机构ID获取商超供应商企业
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getSCBusinessByOrg")
	public View getSCBusinessByOrg(Model model,
			@RequestParam(value="organization", required = false) Long organization,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			BusinessUnit businessUnit = null;
			if(organization != null){
				// 获取目标组织机构对应企业的信息（不包括qs号信息）
				info.setOrganization(organization);
				businessUnit = businessUnitService.findSCBusinessByInfo(info);
			}else{
				// 获取当前登录企业信息（不包括qs号信息）
				Long currentUserOrganization=Long.parseLong(AccessUtils.getUserRealOrg().toString());
				info.setOrganization(currentUserOrganization);
				businessUnit = businessUnitService.findSCBusinessByInfo(info);
			}
			model.addAttribute("data", businessUnit);
		} catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取当前企业基本信息
	 * @param organization 可选参数，如果不为空，则按照organization查询企业
	 * @param name 可选参数，如果不为空，则按照name查询企业
	 * @author TangXin
	 * 最后更新：ZhangHui 2015/5/14<br>
	 * 更新内容：查询方法采用原始sql查询，提交查询效率
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET,value="/getCurrentBusiness")
	public View getBusinessByOrgnazitionId(
			@RequestParam(value="organization", required=false) Long organization,
			@RequestParam(value="name", required=false) String name, 
			Model model, HttpServletRequest req,
			HttpServletResponse resp){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			LightBusUnitVO busUnitVO = null;
			if(name != null){
				busUnitVO = businessUnitService.findVOByName(name);
			}else{
				if(organization == null){
					organization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				}
				busUnitVO = businessUnitService.findBusVOByOrg(organization);
			}
			boolean proDateIsRequired=PropertiesUtil.getProDateIsRequired(organization);
			busUnitVO.setProDateIsRequired(proDateIsRequired);
			model.addAttribute("data", busUnitVO);
		}catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 批量导入旗下企业
	 * @param registerVo
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "batchAddSubsidiary")
	public View batchAddSubsidiary(@RequestBody ExlVO exlVO, HttpServletRequest request, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			Long currentUserOrganization=Long.parseLong(AccessUtils.getUserRealOrg().toString());
			model=businessUnitService.batchAddSubsidiary(exlVO,currentUserOrganization,model);
		} catch(ServiceException sex){
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
	 * 验证注册企业名称唯一
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/verificationEnName")
	public View verificationEnName(
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		@SuppressWarnings("unchecked")
		Map<String, String[]> params = req.getParameterMap();  
		String enName = params.get("param")[0];
		try {
		    //name=URLEncoder.encode(name, "UTF-8");
			boolean isExist =businessUnitService.verificationEnName(enName);
			if(!isExist){
				model.addAttribute("status", SERVER_STATUS_SUCCESS);
				model.addAttribute("type", "en");
			}else{
				model.addAttribute("status", SERVER_STATUS_FAILED);
				model.addAttribute("msg", "该企业已经存在");
				model.addAttribute("type", "en");
			}
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
	 * 验证注册企业用户名唯一
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/verificationEnUserName")
	public View verificationEnUserName(
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		@SuppressWarnings("unchecked")
		Map<String, String[]> params = req.getParameterMap();  
		String userName = params.get("param")[0];
		try {
		    //name=URLEncoder.encode(name, "UTF-8");
			boolean isExist =businessUnitService.verificationEnUserName(userName);
			if(isExist){
				model.addAttribute("status", SERVER_STATUS_FAILED);
				model.addAttribute("msg", "该用户已经存在");
				model.addAttribute("type", "userName");
			}else{
				model.addAttribute("status", SERVER_STATUS_SUCCESS);
				model.addAttribute("type", "userName");
			}
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
	 * 验证注册企业用户名唯一（portal用）
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationEnUserName")
	public View verificationEnUserName(@RequestParam(value="userName")String userName,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		    //name=URLEncoder.encode(name, "UTF-8");
			boolean isExist =businessUnitService.verificationEnUserName(userName);
			model.addAttribute("isExist", isExist);
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
	 * 验证注册企业生产许可证唯一（portal用）
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationProductNo")
	public View verificationProductNo(@RequestParam(value="productNo")String productNo,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			//name=URLEncoder.encode(name, "UTF-8");
			long num = enterpriseService.countByProductNo(productNo);
			if(num>0){
				model.addAttribute("isExist", true);
			}else{
				model.addAttribute("isExist", false);
			}
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
	 * 验证注册企业餐饮服务许可证唯一（portal用）
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationServiceNo")
	public View verificationServiceNo(@RequestParam(value="serviceNo")String serviceNo,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			//name=URLEncoder.encode(name, "UTF-8");
			long num = enterpriseService.countByServiceNo(serviceNo);
			if(num>0){
				model.addAttribute("isExist", true);
			}else{
				model.addAttribute("isExist", false);
			}
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
	 * 验证注册企业食品流通许可证唯一（portal用）
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationPassNo")
	public View verificationPassNo(@RequestParam(value="passNo")String passNo,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			//name=URLEncoder.encode(name, "UTF-8");
			long num = enterpriseService.countByPassNo(passNo);
			if(num>0){
				model.addAttribute("isExist", true);
			}else{
				model.addAttribute("isExist", false);
			}
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
	 * 验证注册企业名称唯一
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationEnName")
	public View verificationEnName(@RequestParam(value="enName")String enName,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
		    //name=URLEncoder.encode(name, "UTF-8");
			boolean isExist =businessUnitService.verificationEnName(enName);
			model.addAttribute("isExist", isExist);
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
	 * Portal接口：验证营业执照号是否已经被注册
	 * @author ZhangHui 2015/4/29
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationEnLicenseNo")
	public View verificationEnLicenseNo(@RequestParam(value="licenseNo")String licenseNo,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			long count = enterpriseService.countByLicenseNo(licenseNo);
			if(count > 0){
				model.addAttribute("isExist", true);
			}else{
				model.addAttribute("isExist", false);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 验证注册企业组织机构代码
	 * @param name
	 * @param model
	 * @param req
	 * @param resp
	 * @author TangXin
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/verificationEnOrgCode")
	public View verificationEnOrgCode(@RequestParam(value = "orgCode") String orgCode, 
			@RequestParam(value = "orgId",required=false) Long orgId, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			boolean isExist = businessUnitService.validateBusUnitOrgCode(orgCode,orgId);
			model.addAttribute("isExist", isExist);
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
	 * 企业注册审核不通过 
	 * @param id
	 * @param returnMes
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/noPassReturn/{id}")
	public View noPassReturn(@PathVariable Long id,@RequestParam("returnMes") String returnMes,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			//returnMes=URLEncoder.encode(returnMes, "UTF-8");
			businessUnitService.noPassReturn(returnMes,id);
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
	 * 背景：在报告录入页面
	 * 功能描述：根据生产企业营业执照号查询生产企业信息
	 * @author ZhangHui 2015/6/5
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getBusUnitOfReportVOByLicenseNo/{licenseNo}")
	public View getBusUnitByLicenseNo(
			@PathVariable String licenseNo,
			@RequestParam String barcode,
			Model model,
			HttpServletRequest req,
			HttpServletResponse resp) {
		
		ResultVO resultVO = new ResultVO();
		try {
		    BusinessUnitOfReportVO bus_vo = productToBusinessUnitService.getBusUnitOfReportVOLicenseNo(licenseNo, barcode);
		    model.addAttribute("data", bus_vo);
		} catch (Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, value = "/business-unit/validateUniqueBrands")
	public View validateUniqueBrands(@RequestBody BusinessUnit businessUnit,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			List<BusinessBrand> brands = businessUnit.getBrands();
			List<String> error_brands = new ArrayList<String>();
			for(BusinessBrand brand : brands){
				BusinessBrand orig_brand = businessBrandService.findByName(brand.getName());
				if(orig_brand != null && !orig_brand.getBusinessUnit().getId().equals(businessUnit.getId())){
					error_brands.add(brand.getName());
				}
			}
		    model.addAttribute("error_brands", error_brands);
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	//上传企业信息(pdf)
	@RequestMapping(method = RequestMethod.PUT, value = "/saveBusinessToPdf")
	public View saveBusinessToPdf(@RequestBody BusinessUnit businessUnit,Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			this.update(businessUnit,model,req,resp);
			BusinessUnit busUnit = businessUnitService.findByInfo(info, true, true);
			
			/**
			 * 获取企业的详细的生产许可证信息
			 * @author ZhangHui 2015/5/21
			 */
			List<ProductionLicenseInfo> ListproLicense = productionLicenseService.getListByBusId(busUnit.getId());
			
			Resource res=ftpService.mkUploadBusinessPdf(busUnit, ListproLicense);
			if(res!=null){
				busUnit.setWdaBackFlag(false);
				if(busUnit.getBusPdfAttachments()!=null){
					busUnit.getBusPdfAttachments().add(res);
					businessUnitService.update(busUnit);
				}else{
					Set<Resource> setRes=new HashSet<Resource>();
					setRes.add(res);
					busUnit.setBusPdfAttachments(setRes);
					businessUnitService.update(busUnit);
				}
				busUnit.setNowPdfUrl(res.getUrl());
				JSONObject json=JSONObject.fromObject(busUnit);
				String result = HttpUtils.send(HttpUtils.getWDAHostname()+"/wda-core/service/businessBasic/addBusiness", "POST", json);
				JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
				if(jsonResult.getJSONObject("result").getString("status").equals("false")){
					resultVO.setStatus(SERVER_STATUS_FAILED);
					resultVO.setSuccess(false);
					resultVO.setErrorMessage("WDA出错:"+jsonResult.getJSONObject("result").getString("errorMessage"));
				}
			}else{
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
				resultVO.setErrorMessage("生成pdf的时候出现异常！");
			}
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(e.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据企业id获取企业pdf列表。
	 * @param page
	 * @param pageSize
	 * @param id
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getBusUnitListPdf/{page}/{pageSize}/{id}")
	public View getBusUnitListPdf(@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize, 
			@PathVariable(value="id") Long id,Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long total=businessUnitService.countBusPdfByBusId(id);
			List<Resource> busUnitPdfs = businessUnitService.getBusinessPdfsByBusUnitIdWithPage(id, page, pageSize);
			model.addAttribute("listPdf", busUnitPdfs);
			model.addAttribute("total", total);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setMessage(sex.getMessage());
			((Throwable)sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 根据企业id只pdf id删除pdf记录
	 * @param resId
	 * @param busId
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.POST, value = "/removeBusUnitPdf/{resId}/{busId}")
	public View saveBusinessToPdf(@PathVariable Long resId,@PathVariable Long busId,Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			boolean status=businessUnitService.removeBusUnitPdf(resId,busId);
			resultVO.setSuccess(status);
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 	对外接口，监管系统根据企业id退回企业信息
	 * @param busId
	 * @param backMsg
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/wdaBackBusUnitInfoById")
	public View wdaBackBusUnitInfoById(@RequestParam("businessId") Long busId,@RequestParam("returnMes") String backMsg ,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			String msg=URLDecoder.decode(backMsg, "UTF-8");
			boolean status=businessUnitService.wdaBackBusUnitById(busId, msg);
			if(!status){
				resultVO.setErrorMessage("企业信息退回失败，该企业在企业门户上不存在。");
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
			}
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setMessage(sex.getMessage());
			((Throwable)sex.getException()).printStackTrace();
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/* 根据organizationId获取标准的认证信息
	 * */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getCertificationsByOrg")
	public View getListOfCertificationByOrg(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization=Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long busId = businessUnitService.getDAO().getIdByOrganization(currentUserOrganization);//通过用户组织机构查找企业id
			List<ProductCertificationVO> listOfCertification = businessCertificationService.getListOfCertificationVOByBusinessId(busId);
			model.addAttribute("data", listOfCertification);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable) sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/* 
	 * 根据认证id统计关联的产品数量
	 * */
	@RequestMapping(method = RequestMethod.GET, value = "/countProductByBusinessCertId/{certId}")
	public View countProductByBusinessCertId(@PathVariable Long certId, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			Long count=businessCertificationService.countProductByBusinessCertificationId(certId);
			model.addAttribute("count", count);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable) sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取所有的生产企业名称，提供自动加载使用。 
	 * @param req
	 * @param resp
	 * @param model
	 * @return JSON
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/searchAllBusUnitName")
	public View searchAllBusUnitName(@RequestParam(value="name", required=false) String name, @RequestParam(value="page", required=false) Integer page, 
			@RequestParam(value="pageSize", required=false) Integer pageSize,
			HttpServletRequest req,HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			page = (page == null ? 0 : page);
			pageSize = (pageSize == null ? 0 : pageSize);
			List<String> listOfBusUnitName = businessUnitService.getAllBusUnitName(name, page, pageSize);
			model.addAttribute("data", listOfBusUnitName);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;      
	}
	
	/**
	 * 从t_test_busunit表中查找所有QS_NO,
	 * 用于页面按QS号自动搜索数据的功能。
	 * @param firstpart qs 号的第一部分
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/searchBusQsNoAll/{firstpart}")
	public View searchBusQsNoAll(@PathVariable String firstpart,@RequestParam(value="formatId") Long formatId, HttpServletRequest req,
	        HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<String> listOfQsNo = businessUnitService.getListOfQsNo(firstpart,formatId);
			model.addAttribute("data", listOfQsNo);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;      
	}
	
	/**
	 * 获取所有的生产企业营业执照号，提供自动加载使用。 
	 * @param req
	 * @param resp
	 * @param model
	 * @return JSON
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/searchAllLicenseNo")
	public View searchAllLicenseNo(HttpServletRequest req,HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<String> listOfBusLicenseNo = businessUnitService.getAllBusUnitLicenseNoAndId();
			model.addAttribute("data", listOfBusLicenseNo);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;      
	}
	
	/**
	 * 获取所有的生产企业地址信息，提供自动加载使用。 
	 * @param req
	 * @param resp
	 * @param model
	 * @return JSON
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/searchAllBusUnitAddress")
	public View searchAllBusUnitAddress(HttpServletRequest req,HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<String> listOfAddress= businessUnitService.getAllAddressAndId();
			model.addAttribute("data", listOfAddress);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;      
	}
	
	/**
	 * 背景：流通企业报告录入页面，qs号下拉选择并自动加载生产企业信息
	 * 功能描述：按 qsno 查找一条生产企业信息
	 * @author ZhangHui 2015/6/4
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getBusUnitOfReportVOByQsno/{barcode}/{qsNo}")
	public View getBusUnitByQsNo(
			@PathVariable String barcode,
			@PathVariable String qsNo,
			HttpServletRequest req,
			HttpServletResponse resp,
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			BusinessUnitOfReportVO vo = productToBusinessUnitService.getBusUnitOfReportVOByQsno(barcode, qsNo); 
			model.addAttribute("data", vo);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON; 
	}
	
	/**
	 * 根据企业名称查找企业信息，并根据当前产品条码判断该企业有无绑定qs号
	 * @author TangXin
	 * 最后更新：ZhangHui 2015/5/14<br>
     * 更新内容：方法优化，提高执行效率
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/getBusUnitOfReportVOByBusname/{bus_name}")
	public View getBusUnitByName(
			@PathVariable String bus_name,
			@RequestParam String barcode,
			HttpServletRequest req,
			HttpServletResponse resp, 
			Model model){
		
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			
			BusinessUnitOfReportVO vo = productToBusinessUnitService.getBusUnitOfReportVOByBusname(bus_name, barcode);
			
			model.addAttribute("data", vo);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		} 
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 上传贵州流通企业到监管系统 
	 * @param businessUnit
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/uploadBusUnitToFdams")
	public View uploadBusUnitToFdms(@RequestBody BusinessUnit businessUnit, HttpServletRequest req,HttpServletResponse resp,Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			this.update(businessUnit, model, req, resp);
			Long marketOrg = businessUnit.getMarketOrg();
			businessUnit = businessUnitService.findByInfo(info, true, true);
			businessUnit.setMarketOrg(marketOrg);
			/*将一些fdams不需要的字段设置为null*/
			businessUnit.setProviders(null);
			businessUnit.setCustomers(null);
			JSONObject json=JSONObject.fromObject(businessUnit);
			String result = HttpUtils.send(HttpUtils.getFDAMSHostname() + "/fsn-fdams/service/circulate/cirBuExtend/addCirBuExtend", "POST", json);
			JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
			if(jsonResult.getJSONObject("result").getString("status").equals("false")){
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
			}
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON; 
	}
	
	/**
	 * 根据组织机构ID获取交易市场
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getMarketByOrg")
	public View getMarketByOrg(Model model,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);			
			BusinessMarket businessMarket = businessMarketService.findByOrg(info.getOrganization());
			BusinessUnit businessUnit = businessUnitService.findByInfo(info, true, true);
			businessMarket.setBusiness(businessUnit);
			model.addAttribute("data", businessMarket);
		} catch(ServiceException sex){
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
	 * 更新交易市场信息
	 * @param businessMarket
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/business-market")
	public View updateMarket(@RequestBody BusinessMarket businessMarket,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			BusinessUnit businessUnit = businessMarket.getBusiness();	
				/*1. 保存营业执照信息 */
				licenseService.save(businessUnit.getLicense());
				/*2。.保存织机构代码信息  */
				orgInstitutionService.save(businessUnit.getOrgInstitution(), true);
				/*3. 保存食品流通许可证信息 */
				circulationPermitService.save(businessUnit.getDistribution());
				/*4.更新交易市场*/
				businessMarketService.update(businessMarket);
				/*5.保存企业基本信息*/
				businessUnitService.updateBusinessUnit(businessUnit); // 保存企业基本信息
				/*6.保存图片信息*/
				enterpriseService.update(businessUnit);  // 保存图片信息
			//}
			model.addAttribute("data", businessUnit);
		} catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			resultVO.setErrorMessage(sex.getMessage());
			((Throwable) sex.getException()).printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 交易市场新增商户
	 * @param businessUnit
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/market/addBusiness")
	public View addBusiness(@RequestBody MarketToBusiness marBusiness,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);		
			boolean flag = marketToBusinessPKService.saveBusiness(marBusiness,info.getOrganization());			
			if(!flag){
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
			}
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 根据组织机构ID查询交易市场下所有商户
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/market/getBusiness/{configure}/{page}/{pageSize}")
	public View getBusinessByMarket(@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long total = marketToBusinessPKService.countByOrganization(info.getOrganization(),configure);
			List<MarketToBusiness> listOfBusinessUnit = marketToBusinessPKService.getByOrganization(info.getOrganization(), 
					 configure, page, pageSize);
			model.addAttribute("counts", total);
			model.addAttribute("data",listOfBusinessUnit);
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
	 * 根据企业名称查询企业信息
	 * @param businessUnitName
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getBusinessByName")
	public View getBusinessByName(@RequestParam(value = "businessUnitName") String businessUnitName,HttpServletRequest req,HttpServletResponse resp,
			Model model){
		ResultVO resultVO = new ResultVO();
		try{
			BusinessUnit businessUnit  = businessUnitService.findByName(businessUnitName);
			if(businessUnit==null){
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
			model.addAttribute("data", businessUnit);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON; 
	}
	
	/**
	 * 验证新增商户是否已存在
	 * @param businessUnit
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/market/verifyBusiness")
	public View verifyBusiness(@RequestBody MarketToBusiness marBusiness,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {		
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long count = 0;
			if(marBusiness.getId()!=null){
				count = marketToBusinessPKService.getCountBybusIdAndOrg(marBusiness.getId(),info.getOrganization());		
			}else{
				count = marketToBusinessPKService.getCountByLicenseAndOrg(marBusiness.getLicense(),info.getOrganization());	
			}
			model.addAttribute("count", count);
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 根据交易市场信息的上传状态查询交易市场信息
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/market/getBusMarket")
	public View getBusMarket(Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			List<BusinessMarket> listBusMarket = businessMarketService.getListByPublishFlag(true);
			List<BusinessMarketVO> listBusMarketVO = null;
			if(listBusMarket!=null && listBusMarket.size()>0){
				listBusMarketVO = new ArrayList<BusinessMarketVO>();
				for(BusinessMarket bm : listBusMarket){
					BusinessMarketVO bmVo = new BusinessMarketVO();
					bmVo.entityToVO(bm);
					listBusMarketVO.add(bmVo);
				}
			}
			model.addAttribute("data", listBusMarketVO);
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 上传交易市场到监管系统 
	 * @param businessUnit
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/uploadMarketToFdms")
	public View uploadMarketToFdms(@RequestBody BusinessMarket businessMarket, HttpServletRequest req,HttpServletResponse resp,Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/*1.更新交易市场信息*/
			if(!businessMarket.isPublishFlag()){
				businessMarket.setPublishFlag(true);//上传标志
			}
			this.updateMarket(businessMarket, model, req, resp);
			businessMarket = businessMarketService.findByOrg(info.getOrganization());
			BusinessUnit businessUnit = businessUnitService.findByInfo(info, true, true);
			businessMarket.setBusiness(businessUnit);
			/*2.将数据封装发送至Fdams*/
			JSONObject json=JSONObject.fromObject(businessMarket);
			String result = HttpUtils.send(HttpUtils.getFDAMSHostname() + "/fsn-fdams/service/circulate/foodmarket/addFoodMarket", "POST", json);
			JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
			if(jsonResult.getJSONObject("result").getString("status").equals("false")){
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
			}
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON; 
	}
	
	/**
	 * 向商户发送邮件提醒
	 * @param businessUnit
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value="/sendEmailToBusiness")
	public View sendEmailToBusiness(@RequestBody MarketToBusiness marBusiness,
			HttpServletRequest req,HttpServletResponse resp, Model model){	
		ResultVO resultVO = new ResultVO();
	    final Map<String,String> map = new HashMap<String,String>();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			BusinessMarket businessMarket = businessMarketService.findByOrg(info.getOrganization());	//交易市场
			MarketToBusiness orig_marBusiness = marketToBusinessPKService.findById(marBusiness.getId());			
			/*1.封装邮件参数*/
			map.put("email", orig_marBusiness.getEmail());
			map.put("businessName", orig_marBusiness.getName());
			map.put("marketName", businessMarket.getBusiness().getName());
			
			/*2.修改发送邮件记录*/		
			Date now = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String nowTime = df.format(now);
			int count = 0;
			/*2.1若记录的时间为空或者不是当天，则将记录时间更改为当前时间，count设置为1*/
			if(orig_marBusiness.getDate()==null||!(nowTime.equals(df.format(orig_marBusiness.getDate())))){
				orig_marBusiness.setDate(now);
				orig_marBusiness.setCount(1L);
			}else{
				/*2.2若记录的时间为当天，则count+1*/
				count = Integer.parseInt(String.valueOf(orig_marBusiness.getCount()))+1;
				orig_marBusiness.setCount( Long.parseLong(String.valueOf(count)));
			}	
			marketToBusinessPKService.update(orig_marBusiness);
			model.addAttribute("count", 5-orig_marBusiness.getCount());
		}catch(Exception e){
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();  
		}
		model.addAttribute("result", resultVO);
		
		/*3.发送邮件*/
		final MimeMessagePreparator preparator = new MimeMessagePreparator() {  
	           public void prepare(MimeMessage mimeMessage) throws Exception {  
	               MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true,"UTF-8");  
	               message.setSubject("食品安全与营养（贵州）信息科技有限公司企业注册确认");  
	               message.setTo(map.get("email"));  
	               message.setFrom("10000@fsnip.com");  
	               Map<String, Object> model = new HashMap<String, Object>();  
	               model.put("businessName",map.get("businessName"));
	               model.put("marketName",map.get("marketName"));
	               model.put("fsnUrl", "fsnip.com");
	               String text = VelocityEngineUtils.mergeTemplateIntoString(  
	               velocityEngine, "market-business.vm","UTF-8", model);  
	               message.setText(text, true);  
	           }  
	       };
	       try{
	        Thread thread = new Thread(){//异步发送邮件
	    		public void run(){
	    			javaMailSender.send(preparator);
	    		}
	    	};
	    	thread.start();
	       }catch(MailException e) {  
	           e.printStackTrace();  
	       }
		return JSON;	
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getCountEmail/{id}")
	public View getCountEmail(@PathVariable(value="id") Long id,HttpServletRequest req,HttpServletResponse resp,
			Model model){
		ResultVO resultVO = new ResultVO();
		try{		
			MarketToBusiness marBusiness = marketToBusinessPKService.findById(id);			
			int count = 0;
			/*1.判断日期是否为空，若为空返回0*/
			if(marBusiness.getDate()!=null){
				Date now = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String nowTime = df.format(now);
				String time = df.format(marBusiness.getDate());
				/*2.判断日期是否是当天，如果是，则返回count，如果否返回0*/
				if(nowTime.equals(time)){
					count =  Integer.parseInt(String.valueOf(marBusiness.getCount()));
				}	
			}
			model.addAttribute("count", count);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable)sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON; 
	}
	
	/**
	 * 交易市场编辑商户
	 * @param businessUnit
	 * @param model
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/market/editBusiness")
	public View editBusiness(@RequestBody MarketToBusiness marBusiness,
			Model model,HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);		
			MarketToBusiness business = marketToBusinessPKService.updateBusiness(marBusiness,info.getOrganization());			
			if(business==null){
				resultVO.setStatus(SERVER_STATUS_FAILED);
				resultVO.setSuccess(false);
			}
			model.addAttribute("data", business);
		}catch(Exception e){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
		}
		model.addAttribute("result", resultVO);
		return JSON;	
	}
	
	/**
	 * 根据企业ID获取企业所有信息
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getBusinessById/{id}")
	public View getBusinessByOrg(@PathVariable Long id,Model model,
			HttpServletRequest req,HttpServletResponse resp) {
		ResultVO resultVO = new ResultVO();
		try {
			BusinessUnit businessUnit = businessUnitService.findById(id);
			AuthenticateInfo info = new AuthenticateInfo();
			info.setOrganization(businessUnit.getOrganization());
			BusinessUnit orig_businessUnit = businessUnitService.findByInfo(info, true, true);
			String type = orig_businessUnit.getType();
			String address = orig_businessUnit.getAddress();
			/*如果是贵州流通企业，需要查询所选交易市场*/
			if(type!=null&&type.equals("流通企业")&&address!=null&&address.contains("贵州省")){
				BusinessMarketVO marketVO = businessMarketService.findVOByBusinessId(orig_businessUnit.getId());
				if(marketVO != null) orig_businessUnit.setMarketOrg(marketVO.getOrganization());
			}
			model.addAttribute("data", orig_businessUnit);
		} catch(ServiceException sex){
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
	 * 功能描述：获取当前企业的母企业、子企业、兄弟企业<br>
	 * 获取business TreeNode集合
	 * @author ZhangHui 2015/5/18
	 */
	@RequestMapping(value = "/tree/getRelativesOftree")
	@ResponseBody
	public List<BusinessTreeNode> getRelativesOftree(
			@RequestParam(value = "id", required = false) String id,
			HttpServletRequest req,
			HttpServletResponse resp) {
		List<BusinessTreeNode> list = null;
		try {
			if (id == null) {
				id = "0_null";
			} else {
				id = URLDecoder.decode(id, "UTF-8");
			}
			String[] array = id.split("_");
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			list = businessUnitService.getRelativesOfTreeNodes(Integer.parseInt(array[0]) + 1, array[1], info.getOrganization());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}