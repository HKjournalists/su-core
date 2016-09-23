package com.gettec.fsnip.fsn.service.business.impl;

import com.gettec.fsnip.fsn.dao.business.BusinessUnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.District;
import com.gettec.fsnip.fsn.model.base.Office;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.model.business.*;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.sales.BusinessSalesInfo;
import com.gettec.fsnip.fsn.service.base.DistrictService;
import com.gettec.fsnip.fsn.service.base.OfficeService;
import com.gettec.fsnip.fsn.service.base.SysAreaService;
import com.gettec.fsnip.fsn.service.business.*;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.BusinessCertificationService;
import com.gettec.fsnip.fsn.service.sales.BusinessSalesInfoService;
import com.gettec.fsnip.fsn.transfer.BusinessBrandTransfer;
import com.gettec.fsnip.fsn.transfer.BusinessUnitTransfer;
import com.gettec.fsnip.fsn.util.AnalysisExlUtil;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.vo.business.*;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.common.OrganizationVO;
import com.gettec.fsnip.fsn.vo.common.UserVO;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.BusinessUnitVO;
import com.lhfs.fsn.vo.business.BussinessUnitVOToPortal;
import com.lhfs.fsn.vo.business.LightBusUnitVO;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * BusinessUnit service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="businessUnitService")
public class BusinessUnitServiceImpl extends BaseServiceImpl<BusinessUnit, BusinessUnitDAO> 
		implements BusinessUnitService{
	@Autowired private BusinessUnitDAO businessUnitDAO;
	@Autowired private BusinessBrandService businessBrandService;
	@Autowired private ResourceService testResourceService;
	@Autowired private EnterpriseRegisteService enterpriseService;
	@Autowired private LicenseService licenseService;
	@Autowired private OrgInstitutionService orgInstitutionService;
	@Autowired private CirculationPermitService circulationPermitService;
	@Autowired private FieldValueService fieldValueService;
	@Autowired private ProducingDepartmentService producingDepartmentService;
	@Autowired private TaxRegisterService taxRegisterService;
	@Autowired private LiquorSalesLicenseService liquorSalesLicenseService;
	@Autowired private BusinessCertificationService businessCertificationService;
	@Autowired private SysAreaService sysAreaService;
	@Autowired private OfficeService officeService;
	@Autowired private DistrictService districtService;
	@Autowired private BusinessMarketService businessMarketService;
	@Autowired private MarketToBusinessPKService MarketToBusinessPKService;
	@Autowired private BusinessSalesInfoService businessSalesInfoService;
	@Autowired private ResourceService resourceService;

	@Override
	public BusinessUnitDAO getDAO() {
		return businessUnitDAO;
	}
	
	/**
	 * 验证企业名称唯一性
	 * @param name
	 * @return
	 */
	public boolean checkUniqueName(String name) throws ServiceException {
		try {
			String condition = " WHERE e.name = ?1";
			return getDAO().count(condition, new Object[]{name}) < 1;
		} catch (JPAException jpae) {
			throw new ServiceException("BusinessUnitServiceImpl.checkUniqueName()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 根据企业名称查找企业营业执照
	 * @param name
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String findLicenseByName(String name) throws ServiceException {
		try {
			return getDAO().findLicenseByName(name);
		} catch (DaoException dex) {
			throw new ServiceException("BusinessUnitServiceImpl.findByName()-->" + dex.getMessage(), dex.getException());
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public int getProductCount() {
		return getDAO().getProductCount();
	}

	/**
	 * 按企业名称模糊查询企业信息
	 * @param name
	 * @throws ServiceException 
	 */
	@Override
	public List<BusinessUnit> findByName_(String name) throws ServiceException {
		try {
			List<BusinessUnit> businessUnitList= getDAO().findByName_(name);
			BusinessUnitTransfer.transfer(businessUnitList);
			return businessUnitList;
		} catch (DaoException dex) {
			throw new ServiceException("BusinessUnitServiceImpl.findByName_()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 获取所有qs号（并去重），用于录入页面按qs证号搜索功能。
	 * @param firstpart qs号的第一部分
	 * @param formatId qs号规则id
	 * @throws ServiceException 
	 * @author HuangYog 2015/01/28 修改--加参数 firstpart
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<String> getListOfQsNo(String firstpart,Long formatId) throws ServiceException {
		try {
			return getDAO().getListOfQsNo(firstpart, formatId);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：报告录入页面，保存被检单位
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/7
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessUnit saveTestee(String testee) throws ServiceException {
		try {
			if(testee==null || testee==null || "".equals(testee)){
				return null;
			}
			
			BusinessUnit bus = new BusinessUnit(testee);
			
//			BusinessUnit orig_testee = getDAO().findByName(testee);
			/**
			 * 代码优化，这里不需要查询整个对象，只要根据名称查询相关的值即可
			 * 修改人：wubiao  2016.2.18 10:20
			 */
			BusinessUnit orig_testee = getDAO().findByNameOrganization(testee);
			
			if(orig_testee == null){
				create(bus);
			}else{
				bus.setId(orig_testee.getId());
			}
			return bus;
		} catch (Exception e) {
			throw new ServiceException("BusinessUnitServiceImpl.saveTestee()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 按名称获取生产商信息
	 * @param name
	 * @throws ServiceException
	 */
	@Override
	public List<BusinessUnit> getListByName(String name, String type, int page,
			int pageSize) throws ServiceException {
		try {
			return getDAO().getListByName(page, pageSize, name,type);
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】按名称获取生产商信息,出现异常。", dex);
		}
	}

	
	/**
	 * service获得当前企业下的所有子企业集合有分页处理
	 */
	@Override
	public List<BusinessUnit> getSubsidiaryListByOrgPage(int page,
			int pageSize, String configure, Long org) throws ServiceException {
		List<BusinessUnit> subsidiary=null;
		try {
			subsidiary = getDAO().getSubsidiaryListByOrgPage(getConfigure(org, configure),page,pageSize);
			BusinessUnitTransfer.transfer(subsidiary);
		} catch (DaoException dex) {
             throw new ServiceException(dex.getMessage(),dex.getException());
		}
		return subsidiary;
	}

	/**
	 * service获得当前企业下的所有子企业个数
	 */
	@Override
	public Long getCountByOrg(Long organization, String configure) throws ServiceException {
		
		try {
			Long count=getDAO().getCountByOrg(getConfigure(organization, configure));
			return count;
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * service层新增旗下企业方法
	 * @author longxianzhen 2015/06/18
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void addSubsidiary(BusinessUnit businessUnit, Long org)
			throws ServiceException {
		try {
			BusinessUnit bus=getDAO().findByName(businessUnit.getName());
			/* 如果该旗下企业不存在 或者 存在但不是注册的企业则到sso新增该组织机构 */
			if(bus==null||(bus!=null&&(bus.getOrganization()==null||bus.getOrganization()==0))){
				BusinessUnit parentBu=getDAO().findByOrgnizationId(org);
				BatchAddSubsidiatyVO batchAddSuVO=new BatchAddSubsidiatyVO();
				List<OrganizationVO> orgs=new ArrayList<OrganizationVO>();//封装sso需要的数据			
					OrganizationVO orgVO=new OrganizationVO();
					orgVO.setOrganizationName(businessUnit.getName());
					orgVO.setOrganizationAddress(businessUnit.getAddress());
					orgVO.setParentId(org.toString());
					orgVO.setComments("");
					orgs.add(orgVO);		
				batchAddSuVO.setOrganizations(orgs);
				JSONObject json=JSONObject.fromObject(batchAddSuVO);
				//往sso发送请求
				String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/organization/fsnOrgainzationReg", SSOClientUtil.POST, json);
				JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
				List<JSONObject> successList = jsonResult.getJSONArray("success");
				if(successList.size()==1){
					for(JSONObject orgJSON : successList){
						if(bus==null){//sso新增成功后 如果该旗下企业不存在 则新增
							businessUnit.setParentOrganizationId(org);
							businessUnit.setOrganization(Long.parseLong(orgJSON.getString("id")));
							businessUnit.setType(parentBu.getType());
							businessUnit.setEnterpriteDate(new Date());
							if(parentBu.getType().contains("生产企业")){
								businessUnit.setSignFlag(parentBu.isSignFlag());
							}
							/*处理营业执照信息*/
							LicenseInfo orig_license = licenseService.save(businessUnit.getLicense());
							businessUnit.setLicense(orig_license);
							create(businessUnit);
						}else{//如果存在则更新
							bus.setParentOrganizationId(org);
							bus.setOrganization(Long.parseLong(orgJSON.getString("id")));
							bus.setType(parentBu.getType());
							bus.setEnterpriteDate(new Date());
							bus.setAddress(businessUnit.getAddress());
							bus.setOtherAddress(businessUnit.getOtherAddress());
							if(parentBu.getType().contains("生产企业")){
								bus.setSignFlag(parentBu.isSignFlag());
							}
							/*处理营业执照信息*/
							LicenseInfo orig_license = licenseService.save(businessUnit.getLicense());
							bus.setLicense(orig_license);
							update(bus);
						}
					}
				}
			}else{//如果该旗下企业已经注册过则把设置该旗下企业的父组织机构ID
				bus.setParentOrganizationId(org);
				update(bus);
			}
			
		}catch (Exception e) {
			throw new ServiceException("service层新增旗下企业方法系统异常",e);
		}
		
	}

	/**
	 * service层验证企业名称唯一性
	 */
	@Override
	public boolean verificationNameOrLic(String val,String type) throws ServiceException {
		try {
			return getDAO().verificationNameOrLic(val,type);			
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
		
	}

	/**
	 * service层修改旗下企业方法 （前台已屏蔽 不让编辑旗下企业）
	 * @author longxianzhen 2015/06/18
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editSubsidiary(BusinessUnit businessUnit)
			throws ServiceException {
		StringBuffer sb=new StringBuffer();
		try {
			BusinessUnit originalBusinessUnit = findById(businessUnit.getId());
			BusinessUnit buByName=getDAO().findByName(businessUnit.getName());
			/* 根据企业名称查询看修改的企业名称是否存在 */
			if(buByName==null){//不存在则走之前的更新流程
				/* 处理营业执照  */
				LicenseInfo orig_license = licenseService.save(businessUnit.getLicense());
				businessUnit.setLicense(orig_license);
				originalBusinessUnit.setName(businessUnit.getName());
				if(businessUnit.getAddress()!=null&&!businessUnit.getAddress().equals(originalBusinessUnit.getAddress())){
					originalBusinessUnit.setAddress(businessUnit.getAddress());
					originalBusinessUnit.setOtherAddress(businessUnit.getOtherAddress());
				}
				originalBusinessUnit.setLicense(businessUnit.getLicense());	
				//封装sso需要的数据
				sb.append("parentId=").append(originalBusinessUnit.getParentOrganizationId());
				sb.append("&organizationName=").append(URLEncoder.encode(businessUnit.getName().toString(), "utf-8"));
				sb.append("&organizationAddress=").append(URLEncoder.encode(businessUnit.getAddress().toString(), "utf-8"));
				sb.append("&comments=").append("");
				sb.append("&organizationId=").append(originalBusinessUnit.getOrganization());
				sb.append("&isFsnRequest=").append(true);
				/* 请求SSO同步修改旗下企业信息 */
				String result = HttpUtils.send(HttpUtils.getSSOHostname()+"/service/organization", "PUT", sb.toString());
				if(result != null) {
					JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
					if(jsonResult.getString("status").equals("true")){		
						update(originalBusinessUnit);
					}
				} else {
					throw new ServiceException("在请求SSO同步修改旗下企业信息时，返回状态为 null ！",new Exception());
				}
			
			/* 修改的企业名称存在 且为未注册的企业 则走新增旗下企业的流程 并取消之前旗下企业的关系*/
			}else if(buByName!=null&&(buByName.getOrganization()==null||buByName.getOrganization()==0)){
				BusinessUnit parentBu=BusinessUnitTransfer.transfer(getDAO().findByOrgnizationId(originalBusinessUnit.getParentOrganizationId()));
				BatchAddSubsidiatyVO batchAddSuVO=new BatchAddSubsidiatyVO();
				List<OrganizationVO> orgs=new ArrayList<OrganizationVO>();//封装sso需要的数据			
					OrganizationVO orgVO=new OrganizationVO();
					orgVO.setOrganizationName(businessUnit.getName());
					orgVO.setOrganizationAddress(businessUnit.getAddress());
					orgVO.setParentId(originalBusinessUnit.getParentOrganizationId().toString());
					orgVO.setComments("");
					orgs.add(orgVO);		
				batchAddSuVO.setOrganizations(orgs);
				JSONObject json=JSONObject.fromObject(batchAddSuVO);
				//往sso发送请求
				String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/organization/fsnOrgainzationReg", SSOClientUtil.POST, json);
				JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
				List<JSONObject> successList = jsonResult.getJSONArray("success");
				if(successList.size()==1){
					for(JSONObject orgJSON : successList){
						buByName.setParentOrganizationId(originalBusinessUnit.getParentOrganizationId());
						buByName.setOrganization(Long.parseLong(orgJSON.getString("id")));
						buByName.setType(parentBu.getType());
						buByName.setEnterpriteDate(new Date());
						buByName.setAddress(businessUnit.getAddress());
						buByName.setOtherAddress(businessUnit.getOtherAddress());
						if(parentBu.getType().contains("生产企业")){
							buByName.setSignFlag(parentBu.isSignFlag());
						}
						/*处理营业执照信息*/
						LicenseInfo orig_license = licenseService.save(businessUnit.getLicense());
						buByName.setLicense(orig_license);
						update(buByName);
					}
				}
				originalBusinessUnit.setParentOrganizationId(null);
				update(originalBusinessUnit);
			}else{//修改的企业名称存在 且为已注册的企业 则新增修改后旗下企业的关系 取消之前旗下企业的关系
				buByName.setParentOrganizationId(originalBusinessUnit.getParentOrganizationId());
				update(buByName);
				originalBusinessUnit.setParentOrganizationId(null);
				update(originalBusinessUnit);
			}
			
			
		}catch (UnsupportedEncodingException uex) {
			throw new ServiceException("service层新增旗下企业方法URL编码出错",uex);
		}catch (Exception e) {
			throw new ServiceException("service层新增旗下企业方法系统异常",e);
		}
		
	}
	
	/**
	 * service层保存企业注册信息
	 * @param enRegiste
	 * @return void
	 * @throw ServiceException
	 * @author LongXianZhen
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveEnterpriseRegisteInfo(EnterpriseRegiste enRegiste)
			throws ServiceException {	
		 try {			 
			 Set<Resource> licAttachments = enRegiste.getLicAttachments();
			 Set<Resource> disAttachments = enRegiste.getDisAttachments();
			 Set<Resource> qsAttachments = enRegiste.getQsAttachments();
			 Set<Resource> liquorAttachments = enRegiste.getLiquorAttachments();
			 for(Resource resource : qsAttachments){
				 resource.setId(null);
			 }
			 for(Resource resource : disAttachments){
				 resource.setId(null);
			 }
			 for(Resource resource : licAttachments){
				 resource.setId(null);
			 }
			 for(Resource resource : liquorAttachments){
				 resource.setId(null);
			 }
			 enRegiste.setId(null);
			 enRegiste.setStatus("待审核");
			 String addr = enRegiste.getEnterptiteAddress();
			 enRegiste.setEnterptiteAddress(addr.replaceAll("-", ""));
			 enRegiste.setOtherAddress(addr);
			 enRegiste.setEnterpriteDate(new Date());
			 enRegiste.setLicAttachments(licAttachments);
//			 enRegiste.setOrgAttachments(orgAttachments);
			 enRegiste.setDisAttachments(disAttachments);
			 enRegiste.setQsAttachments(qsAttachments);
			 enRegiste.setLiquorAttachments(liquorAttachments);
			 enterpriseService.create(enRegiste);
			 enRegiste.setLicAttachments(licAttachments);
			 enRegiste.setDisAttachments(disAttachments);
			 enRegiste.setQsAttachments(qsAttachments);
			 enRegiste.setLiquorAttachments(liquorAttachments);
//			 enRegiste.setOrgAttachments(orgAttachments);
			 if("流通企业.其他流通企业".equals(enRegiste.getEnterpriteType())){
				 enRegiste.setEnterpriteType("流通企业");
			 }
			 testResourceService.saveLicAndOrgResource(enRegiste);
		} catch (ServiceException sex) {
			throw new ServiceException("BusinessUnitServiceImpl.saveEnterpriseRegisteInfo()-->" + sex.getMessage(),sex.getException());
		}
	}
	/**
	 * 获得所有注册企业集合有分页处理
	 */
	@Override
	public List<EnterpriseRegiste> getEnRegisteListByPage(int page,
			int pageSize, String configure) throws ServiceException {
		List<EnterpriseRegiste> enRegiste=null;
		try {
			enRegiste = enterpriseService.getEnRegisteListByPage(page,pageSize,getConfigureEn(configure));			
		} catch (ServiceException sex) {
             throw new ServiceException("BusinessUnitServiceImpl.getEnRegisteListByPage()-->" + sex.getMessage(),sex.getException());
		}
		return enRegiste;
	}

	/**
	 * service获得所有注册企业个数
	 */
	@Override
	public Long getAllCount(String configure)throws ServiceException {
		Long count=0L;
		try {
			count = enterpriseService.getAllCount(getConfigureEn(configure));
		} catch (ServiceException sex) {
			throw new ServiceException("BusinessUnitServiceImpl.getAllCount()-->" + sex.getMessage(), sex.getException());
		}
		return count;
	}
	
	/**
	 * 审核通过一个注册企业
	 * @param id 企业id
	 * @return boolean
	 *         true: 审核通过
	 *         false: 未通过
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public EnterpriseRegiste spprove(Long id, boolean signFlag) throws ServiceException {
		try {
			EnterpriseRegiste orig_enRegiste = enterpriseService.findById(id);
			if(orig_enRegiste==null){return null;}
			
			/* 1. 调用sso审核企业和用户的接口 */
			JSONObject json = getSpproveSSOData(orig_enRegiste);
			String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/organization/fsnOrgainzationReg", SSOClientUtil.POST, json);
			JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
			if(!jsonResult.getString("status").equals("true")){
				if(jsonResult.getString("error").equals("SYS_ERROR_CODE_USER_NAME_EXISTING")){
					throw new ServiceException("用户名已存在，审核失败！", null);
				}
				throw new ServiceException("审核失败！", null);
			}
			/* 2. 审核成功 */
			orig_enRegiste.setStatus("审核通过");
			Long orgId = Long.parseLong(jsonResult.getString("userOrganizationId"));				
			/* 3. 处理营业执照信息 */
			LicenseInfo orig_liceInfo = licenseService.save(orig_enRegiste.getLicenseNo());
			/* 4. 处理组织机构代码 */
			OrganizingInstitution orig_orgIns = orgInstitutionService.save(orig_enRegiste.getOrganizationNo());
			/* 5. 处理企业BusinessUnit信息 */
			BusinessUnit orig_business = getDAO().findByName(orig_enRegiste.getEnterpriteName());
			if(orig_business==null){
				/* 5.1 新增 */
				BusinessUnit business = getBusinessByEnregiste(orig_enRegiste, orgId);
				business.setLicense(orig_liceInfo);
				business.setOrgInstitution(orig_orgIns);
				business.setSignFlag(signFlag);
				create(business);
				orig_business = business;
			}else{
				/* 5.2 编辑 */
				setBusinessValue(orig_business, orig_enRegiste, orgId);
				orig_business.setLicense(orig_liceInfo);
				orig_business.setOrgInstitution(orig_orgIns);
				orig_business.setSignFlag(signFlag);
				update(orig_business);
			}			
			/* 6. 更新注册表 */
			enterpriseService.update(orig_enRegiste);
			/* 7. 针对仁怀企业，需要更新fdams的相关信息 */
			/*if(orig_enRegiste.getEnterpriteType().equals("仁怀市白酒生产企业")){
				JSONObject wdaJson = getSpproveFDAMSData(orig_business, orig_enRegiste);
				String wdaResult = HttpUtils.send(HttpUtils.getWDAHostname()+"/wda-core/service/businessBasic/addBusinessBasic", "POST", wdaJson);
				JSONObject wdaJsonResult = (JSONObject)JSONSerializer.toJSON(wdaResult);
				if(!wdaJsonResult.getJSONObject("result").getBoolean("success")){
					throw new ServiceException("审核失败！", null);
				}
			}*/
			/*8.如果是交易市场类型，则向交易市场插入数据*/
			if(orig_business.getType().equals("交易市场")){
				BusinessMarket market = new BusinessMarket();
				market.setBusiness(orig_business);
				market.setPublishFlag(false);
				businessMarketService.create(market);
			}
			return orig_enRegiste;
		}catch (DaoException dex) {
			throw new ServiceException("BusinessUnitServiceImpl.spprove()-->" + dex.getMessage(), dex.getException());
		}catch (Exception e) {
			throw new ServiceException("系统异常！"+e.getMessage(), e);
		}
	}

	/**
	 * 更新BusinessUnit之前，根据EnterpriseRegiste赋值操作
	 * @param orig_business
	 * @param orig_enRegiste
	 * @param orgId
	 */
	private void setBusinessValue(BusinessUnit orig_business, EnterpriseRegiste orig_enRegiste, Long orgId) {
		orig_business.setAddress(orig_enRegiste.getEnterptiteAddress());
		orig_business.setOtherAddress(orig_enRegiste.getOtherAddress());
		orig_business.setPersonInCharge(orig_enRegiste.getLegalPerson());
		orig_business.setOrganization(orgId);
		//orig_business.setParentOrganizationId(0L);
		orig_business.setType(orig_enRegiste.getEnterpriteType());
		orig_business.setEnterpriteDate(orig_enRegiste.getEnterpriteDate());
		orig_business.setTelephone(orig_enRegiste.getTelephone());
		orig_business.setEmail(orig_enRegiste.getEmail());
	}

	/**
	 * 根据EnterpriseRegiste，获取一条BusinessUnit信息
	 * @param orig_enRegiste
	 * @param orgId
	 * @return
	 */
	private BusinessUnit getBusinessByEnregiste(EnterpriseRegiste orig_enRegiste, Long orgId) {
		BusinessUnit business = new BusinessUnit();
		business.setName(orig_enRegiste.getEnterpriteName());
		business.setAddress(orig_enRegiste.getEnterptiteAddress());
		business.setOtherAddress(orig_enRegiste.getOtherAddress());
		business.setPersonInCharge(orig_enRegiste.getLegalPerson());
		business.setEmail(orig_enRegiste.getEmail());
		business.setOrganization(orgId);
		business.setParentOrganizationId(0L);
		business.setType(orig_enRegiste.getEnterpriteType());
		business.setEnterpriteDate(orig_enRegiste.getEnterpriteDate());
		business.setTelephone(orig_enRegiste.getTelephone());
		return business;
	}

	/**
	 * 封装sso需要的数据
	 * @param enRegiste
	 * @return
	 */
	private JSONObject getSpproveSSOData(EnterpriseRegiste enRegiste) {
		EnterpriseRegisteSSO enRegisteSSO = new EnterpriseRegisteSSO();
		/* 1. List<OrganizationVO> */
		List<OrganizationVO> orgs = new ArrayList<OrganizationVO>();
		OrganizationVO org = new OrganizationVO();
		org.setOrganizationName(enRegiste.getEnterpriteName());
		org.setOrganizationAddress(enRegiste.getEnterptiteAddress());
		org.setComments("");
		if("流通企业".equals(enRegiste.getEnterpriteType())){
			org.setType("流通企业.其他流通企业");
		}else{
			org.setType(enRegiste.getEnterpriteType());
		}
		orgs.add(org);
		/* 2. UserVO */
		UserVO user = new UserVO();
		user.setUserName(enRegiste.getUserName());
		user.setPassword(enRegiste.getPassword());
		user.setEmail(enRegiste.getEmail());
		user.setTelephone(enRegiste.getTelephone());
		enRegisteSSO.setOrganizations(orgs);
		enRegisteSSO.setUser(user);
		/* 3. 返回 */
		JSONObject json=JSONObject.fromObject(enRegisteSSO);
		return json;
	}

	/**
	 * 按组织机构Id查找一条生产商信息
	 * @param info  用户登录信息
	 * @param isLoadImg<br>
	 *              true:需要加载图片信息, false:无需加载图片信息
	 * @param isCompatibilityNotRegiste<br>
	 *              true:兼容未注册的企业, false:忽略未注册的企业
	 * @author ZhangHui 2015/4/9
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BusinessUnit findByInfo(AuthenticateInfo info, boolean isLoadImg, boolean isCompatibilityNotRegiste)
			throws ServiceException {
		try {
			/* 1. 企业基本信息  */
			BusinessUnit businessUnit = BusinessUnitTransfer.transfer(getDAO().findByOrgnizationId(info.getOrganization()));
			if(businessUnit.getType()!=null&&businessUnit.getType().equals("仁怀市白酒生产企业")){
				/* 2. 企业品牌信息  */
				List<BusinessBrand> brands = BusinessBrandTransfer.transfer(businessBrandService.getDAO().getListByBusunitId(businessUnit.getId()));
				businessUnit.setBrands(brands);
				/* 3. 企业其他扩展信息  */
				List<FieldValue> fieldValues = fieldValueService.getListByBusunitId(businessUnit.getId());
				businessUnit.setFieldValues(fieldValues);
				/* 4. 企业挂靠酒厂信息 */
				List<ProducingDepartment> subDepartments = producingDepartmentService.getDAO().getListByBusunitIdAndDepartFlag(businessUnit.getId(), false);		
				businessUnit.setSubDepartments(subDepartments);
				/* 5. 企业生产车间信息 */
				List<ProducingDepartment> proDepartments = producingDepartmentService.getDAO().getListByBusunitIdAndDepartFlag(businessUnit.getId(), true);
				businessUnit.setProDepartments(proDepartments);
			}
			if(isLoadImg){
				EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(businessUnit.getName());
				if(orig_enterprise != null){
					/* 4. 各类证照图片  */
					businessUnit.setOrgAttachments(orig_enterprise.getOrgAttachments());
					businessUnit.setLicAttachments(orig_enterprise.getLicAttachments());
					businessUnit.setDisAttachments(orig_enterprise.getDisAttachments());
					businessUnit.setQsAttachments(orig_enterprise.getQsAttachments());
					businessUnit.setLogoAttachments(orig_enterprise.getLogoAttachments());
					businessUnit.setTaxRegAttachments(orig_enterprise.getTaxRegAttachments());
					if(businessUnit.getTaxRegister() != null) {
						businessUnit.getTaxRegister().setTaxAttachments(orig_enterprise.getTaxRegAttachments());
					}
					businessUnit.setLiquorAttachments(orig_enterprise.getLiquorAttachments());
				}else if(isCompatibilityNotRegiste){
					/* 兼容没有通过注册方式的企业 */
					enterpriseService.save(businessUnit, info);
				}
				/* 企业宣传销售信息 */
				BusinessSalesInfo orig_busSalesInfo = businessSalesInfoService.findByBusId(businessUnit.getId());
				if(orig_busSalesInfo != null) {
					businessUnit.setPropagandaAttachments(setListResourceEx(orig_busSalesInfo.getPubPtotosName(),orig_busSalesInfo.getPubPtotosUrl()));
					businessUnit.setQrAttachments(setListResource(orig_busSalesInfo.getQrcodeImgName(), orig_busSalesInfo.getQrcodeImgUrl()));
				}
				/* 查找认证信息图片列表  */
				List<BusinessCertification> listOfCertification = businessCertificationService.getListOfCertificationByBusinessId(businessUnit.getId());
				businessUnit.setListOfCertification(listOfCertification);
			}
			return businessUnit;
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】按组织机构Id查找一条生产商信息,出现异常！", dex.getException());
		}
	}

	/**
	 * 按组织机构Id查找一条生产商信息
	 * @param info  用户登录信息
	 * @param isLoadImg<br>
	 *              true:需要加载图片信息, false:无需加载图片信息
	 * @param isCompatibilityNotRegiste<br>
	 *              true:兼容未注册的企业, false:忽略未注册的企业
	 * @author ZhangHui 2015/4/9
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BusinessUnit findByInfo2(AuthenticateInfo info,BusinessUnit businessUnit1, boolean isLoadImg, boolean isCompatibilityNotRegiste)
			throws ServiceException {
//		try {
			/* 1. 企业基本信息  */
//			BusinessUnit businessUnit = BusinessUnitTransfer.transfer(getDAO().findByOrgnizationId(info.getOrganization()));
			BusinessUnit businessUnit = null;
			try {
				businessUnit = BusinessUnitTransfer.transfer(getDAO().findById(businessUnit1.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (businessUnit == null) {
				businessUnit = businessUnit1;
			}
			if(businessUnit.getType()!=null&&businessUnit.getType().equals("仁怀市白酒生产企业")){
				/* 2. 企业品牌信息  */
				List<BusinessBrand> brands = null;
				try {
					brands = BusinessBrandTransfer.transfer(businessBrandService.getDAO().getListByBusunitId(businessUnit.getId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				businessUnit.setBrands(brands);
				/* 3. 企业其他扩展信息  */
				List<FieldValue> fieldValues = null;
				try {
					fieldValues = fieldValueService.getListByBusunitId(businessUnit.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				businessUnit.setFieldValues(fieldValues);
				/* 4. 企业挂靠酒厂信息 */
				List<ProducingDepartment> subDepartments = null;
				try {
					subDepartments = producingDepartmentService.getDAO().getListByBusunitIdAndDepartFlag(businessUnit.getId(), false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				businessUnit.setSubDepartments(subDepartments);
				/* 5. 企业生产车间信息 */
				List<ProducingDepartment> proDepartments = null;
				try {
					proDepartments = producingDepartmentService.getDAO().getListByBusunitIdAndDepartFlag(businessUnit.getId(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				businessUnit.setProDepartments(proDepartments);
			}
			if(isLoadImg){
				EnterpriseRegiste orig_enterprise = null;
				try {
					orig_enterprise = enterpriseService.findbyEnterpriteName(businessUnit.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(orig_enterprise != null){
					/* 4. 各类证照图片  */
					businessUnit.setOrgAttachments(orig_enterprise.getOrgAttachments());
					businessUnit.setLicAttachments(orig_enterprise.getLicAttachments());
					businessUnit.setDisAttachments(orig_enterprise.getDisAttachments());
					businessUnit.setQsAttachments(orig_enterprise.getQsAttachments());
					businessUnit.setLogoAttachments(orig_enterprise.getLogoAttachments());
					businessUnit.setTaxRegAttachments(orig_enterprise.getTaxRegAttachments());
					try {
						if(businessUnit.getTaxRegister() != null) {
							try {
								businessUnit.getTaxRegister().setTaxAttachments(orig_enterprise.getTaxRegAttachments());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							businessUnit.setTaxRegister(null);
						}
					} catch (Exception e) {
						e.printStackTrace();
						businessUnit.setTaxRegister(null);
					}
					businessUnit.setLiquorAttachments(orig_enterprise.getLiquorAttachments());
				}else if(isCompatibilityNotRegiste){
					/* 兼容没有通过注册方式的企业 */
					enterpriseService.save(businessUnit, info);
				}
				/* 企业宣传销售信息 */
				BusinessSalesInfo orig_busSalesInfo = null;
				try {
					orig_busSalesInfo = businessSalesInfoService.findByBusId(businessUnit.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(orig_busSalesInfo != null) {
					businessUnit.setPropagandaAttachments(setListResourceEx(orig_busSalesInfo.getPubPtotosName(),orig_busSalesInfo.getPubPtotosUrl()));
					businessUnit.setQrAttachments(setListResource(orig_busSalesInfo.getQrcodeImgName(), orig_busSalesInfo.getQrcodeImgUrl()));
				}
				/* 查找认证信息图片列表  */
				List<BusinessCertification> listOfCertification = null;
				try {
					listOfCertification = businessCertificationService.getListOfCertificationByBusinessId(businessUnit.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				businessUnit.setListOfCertification(listOfCertification);
			}
			return businessUnit;
//		} catch (DaoException dex) {
//			throw new ServiceException("【service-error】按组织机构Id查找一条生产商信息,出现异常！", dex.getException());
//		}
	}

	private List<Resource> setListResource(String name,String url){
		List<Resource> result =null;
		if(url != null && url != null){
			result = new ArrayList<Resource>();
			Resource res = new Resource();
			res.setId(new Long(-1));
			res.setFileName(name);
			res.setName(name);
			res.setUrl(url);
			result.add(res);
		}
		return result;
	}
	
	
	private List<Resource> setListResourceEx(String name,String url){
		
		List<Resource> result =null;
		if(name !=null && url != null)
		{
			String [] names = name.split("\\|");
			String [] urls = url.split("\\|");
			result = new ArrayList<Resource>();
			for(int i=0;i<names.length;i++)
			{
				Resource res = new Resource();
				res.setId(new Long(-1));
				res.setFileName(names[i]);
				res.setName(names[i]);
				res.setUrl(urls[i]);
				result.add(res);
			}
		}
		return result;
	}
	
	/**
	 * 更新该企业在FSN bus_unit中的信息
	 * @param businessUnit  企业信息
	 * @return void
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBusinessUnit(BusinessUnit businessUnit)
			throws ServiceException {
		try {
			BusinessUnit orig_businessUnit = findById(businessUnit.getId());
			setBusinessUnitValue(orig_businessUnit, businessUnit);
			if(businessUnit.getTaxRegister()!=null){
				orig_businessUnit.setTaxRegister(taxRegisterService.findById(businessUnit.getTaxRegister().getId()));
			}
			if(businessUnit.getLicense()!=null){
				orig_businessUnit.setLicense(licenseService.getDAO().findById(businessUnit.getLicense().getLicenseNo()));				
			}
			if(businessUnit.getOrgInstitution()!=null){
				orig_businessUnit.setOrgInstitution(orgInstitutionService.findByOrgCode(businessUnit.getOrgInstitution().getOrgCode()));
			}
			if(businessUnit.getDistribution()!=null){				
				orig_businessUnit.setDistribution(circulationPermitService.findByDistributionNo(businessUnit.getDistribution().getDistributionNo()));
			}
			
			/*保存贵州省流通企业的扩展信息*/
			String type = orig_businessUnit.getType();
			String addr = orig_businessUnit.getAddress();
			if(type!= null && addr !=null && ((type.contains("流通企业")&& addr.contains("贵州省"))||type.contains("交易市场"))){
				orig_businessUnit.setNote(businessUnit.getNote());
				if(businessUnit.getSysArea() != null){
					SysArea orig_area = sysAreaService.findById(businessUnit.getSysArea().getId());
					orig_businessUnit.setSysArea(orig_area);
				}
				if(businessUnit.getOffice() != null){
					Office orig_office = officeService.findById(businessUnit.getOffice().getId());
					orig_businessUnit.setOffice(orig_office);
				}
				MarketToBusinessPKService.save(orig_businessUnit);
			}
			/*普通流通有主体性质--所以和贵州流通都需要*/
			if(businessUnit.getDistrict() != null && businessUnit.getDistrict().size()>0){
				Set<District> new_origDist = this.getNewDistrict(orig_businessUnit.getDistrict(), businessUnit.getDistrict());
				orig_businessUnit.setDistrict(new_origDist);
			}
			update(orig_businessUnit);
			//同布更新企业注册时所填写的基本信息
			EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(businessUnit.getName());
			if(orig_enterprise!=null){
				orig_enterprise.setEnterptiteAddress(businessUnit.getAddress());
				orig_enterprise.setLegalPerson(businessUnit.getPersonInCharge());
				orig_enterprise.setEmail(businessUnit.getEmail());
				orig_enterprise.setTelephone(businessUnit.getTelephone());
				enterpriseService.update(orig_enterprise);
			}
		} catch (JPAException jpae) {
			throw new ServiceException("【JpaExcetion-error】更新该企业在FSN的信息，出现异常。", jpae.getException());
		} catch (Exception e) {
			throw new ServiceException("【service-error】更新该企业在FSN的信息，出现异常。", e);
		}
	}

	/**
	 * 保存生产企业信息前赋值操作
	 * @param orig_businessUnit
	 * @param businessUnit
	 * @author ZhangHui
	 */
	private void setBusinessUnitValue(BusinessUnit orig_businessUnit,
			BusinessUnit businessUnit) {
		orig_businessUnit.setAddress(businessUnit.getAddress());
		orig_businessUnit.setOtherAddress(businessUnit.getOtherAddress());
		orig_businessUnit.setPersonInCharge(businessUnit.getPersonInCharge());
		orig_businessUnit.setContact(businessUnit.getContact());
		orig_businessUnit.setTelephone(businessUnit.getTelephone());
		orig_businessUnit.setPostalCode(businessUnit.getPostalCode());
		orig_businessUnit.setEmail(businessUnit.getEmail());
		orig_businessUnit.setFax(businessUnit.getFax());
		orig_businessUnit.setAbout(businessUnit.getAbout());
		orig_businessUnit.setWebsite(businessUnit.getWebsite());
		orig_businessUnit.setMarketOrg(businessUnit.getMarketOrg());
	}

	private Set<District> getNewDistrict(Set<District> orig_district, Set<District> now_district) throws Exception{
		try{
			List<Long> new_ids = new ArrayList<Long>(); 
			List<Long> orig_ids = new ArrayList<Long>(); 
			Set<District> new_orig_dis = new HashSet<District>();
			if(now_district != null ){ 
				for(District dis : now_district){
					if(dis.getId() != null) new_ids.add(dis.getId());
				}
			}
			if(orig_district != null){
				for(District ordis : orig_district){
					if(new_ids.contains(ordis.getId())) new_orig_dis.add(ordis);
				}
			}
			for(District dis1 : new_orig_dis){
				if(dis1.getId() != null) orig_ids.add(dis1.getId());
			}
			for(District dist : now_district){
				if(dist.getId() != null && !orig_ids.contains(dist.getId())) {
					District orig_distr = districtService.findById(dist.getId());
					new_orig_dis.add(orig_distr);
					orig_ids.add(dist.getId());
				}
			}
			return new_orig_dis;
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 更新仁怀市白酒生产企业在FSN bus_unit中的信息
	 * @param businessUnit  企业信息
	 * @param step          步骤
	 * @return void
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBusinessUnit(BusinessUnit businessUnit, String step) throws ServiceException {
		try {
			if(step==null && businessUnit!=null){
				updateBusinessUnit(businessUnit);
				return;
			}
			if(step.equals("step1")||step.equals("step3")){return;}
			if(step.equals("step2")){
				BusinessUnit orig_businessUnit = findById(businessUnit.getId());
				if(businessUnit.getLicense()!=null){
					orig_businessUnit.setLicense(licenseService.getDAO().findById(businessUnit.getLicense().getLicenseNo()));
				}
				if(businessUnit.getOrgInstitution()!=null){
					orig_businessUnit.setOrgInstitution(orgInstitutionService.findByOrgCode(businessUnit.getOrgInstitution().getOrgCode()));
				}
				if(businessUnit.getDistribution()!=null){				
					orig_businessUnit.setDistribution(circulationPermitService.findByDistributionNo(businessUnit.getDistribution().getDistributionNo()));
				}
				if(businessUnit.getTaxRegister()!=null){
					orig_businessUnit.setTaxRegister(taxRegisterService.findById(businessUnit.getTaxRegister().getId()));
				}
				if(businessUnit.getLiquorSalesLicense()!=null){
					orig_businessUnit.setLiquorSalesLicense(liquorSalesLicenseService.findById(businessUnit.getLiquorSalesLicense().getId()));
				}
				update(orig_businessUnit);
			}
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]BusinessUnitServiceImpl.updateBusinessUnit()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]BusinessUnitServiceImpl.updateBusinessUnit()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 向sso发送请求，更新该企业在sso的基本信息
	 * @param businessUnit       更改后的企业信息
	 * @param orig_businessUnit  更改前的企业信息（必须是从数据库中查出来的对象）
	 * @return
	 * @throws ServiceException 
	 */
	public JSONObject updateBusInfoInSSO(BusinessUnit businessUnit, BusinessUnit orig_businessUnit) throws ServiceException {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("parentId=").append(orig_businessUnit.getParentOrganizationId());
			//sb.append("&organizationName=").append(URLEncoder.encode(businessUnit.getName().toString(), "utf-8"));  //企业名称一旦注册不允许修改
			sb.append("&organizationAddress=").append(URLEncoder.encode(businessUnit.getAddress().toString(), "utf-8"));
			sb.append("&comments=").append("");
			sb.append("&organizationId=").append(orig_businessUnit.getOrganization());
			sb.append("&isFsnRequest=").append(true);
			String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/organization", SSOClientUtil.PUT, sb.toString());
			JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
			return jsonResult;
		} catch (Exception e) {
			throw new ServiceException("【service-error】执行updateBusInfoInSSO方法时， 出现异常。", e);
		}
	}

	/**
	 * 根据当企业Id获取该生产企业的信息，name，addre，licenseNo，businessType字段
	 */
	@Override
	public BusinessUnit findByIdOfLigth(Long busId)
			throws ServiceException {
		try{
			return getDAO().getBusinessByOrganizationIdOfLigth(busId);
		}catch(DaoException dep){
			throw new ServiceException("BusinessUnitServiceImpl.findByIdOfLigth() " +dep.getMessage(),dep.getException());
		}
	}

	/**
	 * 批量导入旗下企业
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Model batchAddSubsidiary(ExlVO exlVO, Long currentUserOrganization,Model model)
			throws ServiceException {
		try {
			BusinessUnit parentBu=getDAO().findByOrgnizationId(currentUserOrganization);
			List<Resource> attachments =exlVO.getAryExlAttachments();		
			for(Resource re:attachments){
				List<BusinessUnit> bus=AnalysisExlUtil.getBuListByExlByte(re.getFile());
				BatchAddSubsidiatyVO batchAddSuVO=new BatchAddSubsidiatyVO();
				List<OrganizationVO> orgs=new ArrayList<OrganizationVO>();
				List<JSONObject> existsBusJsonList=new ArrayList<JSONObject>();
				String strJson="";
				for(BusinessUnit bu:bus){
					BusinessUnit b = getDAO().findByName(bu.getName());
					OrganizationVO org=new OrganizationVO();
					org.setOrganizationName(bu.getName());
					org.setOrganizationAddress(bu.getAddress());
					org.setParentId(currentUserOrganization.toString());
					org.setLicenseNo(bu.getLicense().getLicenseNo());
					org.setComments("");
					if(b==null){
						orgs.add(org);
					}else{
						strJson+="{'organizationName':'"+bu.getName()+"','organizationAddress':'"+bu.getAddress()+
								"','licenseNo':'"+bu.getLicense().getLicenseNo()+"','msg':'组织机构名称重复'},";
					}
				}
				if(strJson.length()>0){
					strJson=strJson.substring(0, strJson.length()-1);
					strJson="{'success':[],'faild':["+strJson+"]}";
					strJson.replaceAll("'", "\"");
					JSONObject jsn=(JSONObject)JSONSerializer.toJSON(strJson);
					existsBusJsonList.addAll(jsn.getJSONArray("faild"));
				}
				if(orgs.size()>0){
					batchAddSuVO.setOrganizations(orgs);
					JSONObject json=JSONObject.fromObject(batchAddSuVO);
					String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/organization/fsnOrgainzationReg", SSOClientUtil.POST, json);
					JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
					List<JSONObject> successList = jsonResult.getJSONArray("success");
					List<JSONObject> faildList = jsonResult.getJSONArray("faild");
					for(JSONObject org : successList){			
						BusinessUnit bu=new BusinessUnit();
						bu.setLicense(new LicenseInfo());
						bu.setName(org.getString("organizationName"));
						bu.setAddress(org.getString("organizationAddress"));
						bu.getLicense().setLicenseNo(org.getString("licenseNo"));
						bu.setOrganization( Long.parseLong(org.getString("id")));
						bu.setType(parentBu.getType());
						bu.setParentOrganizationId(currentUserOrganization);				
						create(bu);
					}
					faildList.addAll(existsBusJsonList);
					model.addAttribute("successSize", successList.size());
					model.addAttribute("faildList", faildList);
					model.addAttribute("faildSize", faildList.size());
				}else{
					model.addAttribute("successSize", 0);
					model.addAttribute("faildList", existsBusJsonList);
					model.addAttribute("faildSize", existsBusJsonList.size());
				}
			}
				
		}catch (Exception e) {
			throw new ServiceException("系统异常",e);
			
		}
		return model;
	}

	/**
	 * service验证注册企业名称唯一性
	 */
	@Override
	public boolean verificationEnName(String name) throws ServiceException {
		boolean isExist=false;
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("orgName=").append(URLEncoder.encode(name, "utf-8"));
			sb.append("&parentId=").append(0L);
			isExist=enterpriseService.verificationEnName(name);
			if(isExist){
				return isExist;
			}
			String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/organization/checkOrgNameUnique", SSOClientUtil.GET, sb.toString());			
			JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result); 
            if(jsonResult.get("status").equals("false")){
            	isExist=true;
            }
		} catch (ServiceException sex) {
			throw new ServiceException("BusinessUnitServiceImpl.verificationEnName()-->" + sex.getMessage(), sex.getException());
		}catch (Exception e) {
			throw new ServiceException("系统异常", e);
		}
		return isExist;
	}

	/**
	 * service验证注册企业用户名唯一性
	 */
	@Override
	public boolean verificationEnUserName(String userName)
			throws ServiceException {
		boolean isExist=false;
		StringBuffer sb = new StringBuffer();
		try {			
			sb.append("name=").append(URLEncoder.encode(userName, "utf-8"));
			isExist= enterpriseService.verificationEnUserName(userName);
			if(isExist){
				return isExist;
			}
			//String casServiceURL = SSOClientUtil.getServiceURLOfCurrentCAS();
			String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/portal/user/checkUserNameUnique", SSOClientUtil.GET, sb.toString());			
			JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result); 
            if(jsonResult.get("status").equals("false")){
            	isExist=true;
            }	
		} catch (ServiceException sex) {
			throw new ServiceException("BusinessUnitServiceImpl.verificationEnUserName()-->" + sex.getMessage(), sex.getException());
		}catch (Exception e) {
			throw new ServiceException("系统异常", e);
		}
		return isExist;
	}

	/**
	 * 注册企业审核不通过
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void noPassReturn(String returnMes, Long id) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		try {
			EnterpriseRegiste enRegiste=enterpriseService.findById(id);
			if(enRegiste!=null){
				sb.append("userName=").append(URLEncoder.encode(enRegiste.getUserName(), "utf-8"));
				sb.append("&organizationName=").append(URLEncoder.encode(enRegiste.getEnterpriteName(), "utf-8"));
				sb.append("&reason=").append(URLEncoder.encode(returnMes, "utf-8"));
				sb.append("&email=").append(URLEncoder.encode(enRegiste.getEmail(), "utf-8"));
				String result = SSOClientUtil.send(HttpUtils.getSSOHostname()+"/service/organization/fsnRejectRegist", SSOClientUtil.POST, sb.toString());
				JSONObject jsonResult = (JSONObject)JSONSerializer.toJSON(result);
				if(jsonResult.getString("status").equals("true")){
					enterpriseService.delete(enRegiste);
				}			
			}	
		}catch (ServiceException sex) {
			throw new ServiceException("BusinessUnitServiceImpl.noPassReturn()-->" + sex.getMessage(), sex.getException());
		}catch (Exception e) {
			throw new ServiceException("系统异常",e);
		}		
	}

	/**
	 * 获取所有的生产企业名称，提供自动加载使用。 
	 */
	 @Override
	public List<String> getAllBusUnitName(String name, int page, int pageSize) throws ServiceException {
		try{
			return getDAO().getAllBusUnitName(name, page, pageSize);
		}catch(DaoException daoe){
			throw new ServiceException("【service-error:】加载所有生产企业名称时出现异常！",daoe.getException());
		}
	}

	 /**
	  * 获取所有的生产企业的营业执照号，提供加载使用。
	  */
	@Override
	public List<String> getAllBusUnitLicenseNoAndId() throws ServiceException {
		try{
			return getDAO().getAllLicenseNoAndId();
		}catch(DaoException daoe){
			throw new ServiceException("【service-error】:获取所有的生产企业的营业执照号时出现异常！",daoe.getException());
		}
	}

	/**
	 * 获取所有生产企业的地址，提供加载使用。
	 */
	@Override
	public List<String> getAllAddressAndId() throws ServiceException {
		try{
			return getDAO().getAllBusUnitAddressAndId();
		}catch(DaoException daoe){
			throw new ServiceException("【service-error】获取所有生产企业的地址时出现异常！",daoe.getException());
		}
	}

	private Map<String, Object> getConfigureEn(String configure) throws ServiceException{
		 if(configure==null||configure.equals("null")){
			 return null;
		 }
	        Object[] params = null;
	        String new_configure = " WHERE ";
	        params = new Object[1];
	        String filter[] = configure.split("@@");
	        for(int i=0;i<filter.length;i++){
	            String filters[] = filter[i].split("@");
	            if(filters.length > 3){
	                try {
	                    String config = splitJointConfigureEn(filters[0],filters[1],filters[2]);
	                    if(config==null){
	                        continue;
	                    }
	                    if(i==0){
	                        new_configure = new_configure + config;
	                    }else{
	                        new_configure = new_configure +" AND " + config;
	                    }
	                } catch (Exception e) {
	                e.printStackTrace();
	                }
	            }
	        }
	        
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("condition", new_configure);
	        map.put("params", params);
	        return map;
	    }
	 
	 /**
	     * 分割页面的过滤信息
	     * @param field
	     * @param mark
	     * @param value
	     * @throws ServiceException
	     */
	    private String splitJointConfigureEn(String field, String mark, String value) throws ServiceException{
	        try {
	            value = URLDecoder.decode(value, "utf-8");
	        } catch (UnsupportedEncodingException e) {
	            throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
	        }
	        if(field.equals("id")){
	            return FilterUtils.getConditionStr("id",mark,value);
	        }
	        if(field.equals("userName")){
	            return FilterUtils.getConditionStr("userName",mark,value);
	        }
	        if(field.equals("materialNo")){
	            return FilterUtils.getConditionStr("materialNo",mark,value);
	        }
	        if(field.equals("enterpriteName")){
	            return FilterUtils.getConditionStr("enterpriteName",mark,value);
	        }
	        if(field.equals("enterptiteAddress")){
	            return FilterUtils.getConditionStr("enterptiteAddress",mark,value);
	        }
	        if(field.equals("legalPerson")){
	            return FilterUtils.getConditionStr("legalPerson",mark,value);
	        }
	        if(field.equals("status")){
	            return FilterUtils.getConditionStr("status",mark,value);
	        }
	        if(field.equals("name")){
	        	return FilterUtils.getConditionStr("name",mark,value);
	        }
	        if(field.equals("email")){
	        	return FilterUtils.getConditionStr("email",mark,value);
	        }
	        if(field.equals("personInCharge")){
	        	return FilterUtils.getConditionStr("personInCharge",mark,value);
	        }
	        if(field.equals("telephone")){
	        	return FilterUtils.getConditionStr("telephone",mark,value);
	        }
	        if(field.equals("license")){
	        	return FilterUtils.getConditionStr("license_no",mark,value);
	        }
	        if(field.equals("enterpriteType")){
	        	return FilterUtils.getConditionStr("enterpriteType",mark,value);
	        }
	        if(field.equals("enterpriteDate")){
	        	return FilterUtils.getConditionStr("enterpriteDate",mark,value);
	        }
	        return null;
	    }
	
	/**
     * 按以下四个字段信息拼接where字符串
     * @param organizationId 当前登录用户的组织机构ID
     * @param condition 页面过滤条件
     * @return
     */
    private Map<String, Object> getConfigure(Long organizationId, String condition){
        String new_configure = " WHERE e.parentOrganizationId = ?1";
        if(condition != null&&!condition.equals("null")){
            String filter[] = condition.split("@@");
            for(int i=0;i<filter.length;i++){
                String filters[] = filter[i].split("@");
                try {
                    String config = splitJointConfigure(filters[0],filters[1],filters[2]);
                    if(config==null){
                        continue;
                    }
                    if(i==0){
                        new_configure = new_configure + " AND " + config;
                    }else{
                        new_configure = new_configure +" AND " + config;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("condition", new_configure);
        map.put("params", new Object[]{organizationId});
        return map;
    }
    
    /**
     * 分割页面的过滤信息
     * @param field
     * @param mark
     * @param value
     * @throws ServiceException
     */
    private String splitJointConfigure(String field,String mark,String value) throws ServiceException{
        try {
            value = URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
        }
        if(field.equals("id")){
            return FilterUtils.getConditionStr("id",mark,value);
        }
        if(field.equals("name")){
            return FilterUtils.getConditionStr("name",mark,value);
        }
        if(field.equals("address")){
            return FilterUtils.getConditionStr("address",mark,value);
        }
        if(field.equals("license_licenseNo")){
            return FilterUtils.getConditionStr("license.licenseNo",mark,value);
        }
        return null;
    }

    /**
     * 删除企业信息生产的pdf
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean removeBusUnitPdf(Long resId,Long busId) throws ServiceException {
		try{
			Set<Resource> removes = new HashSet<Resource>();
			Resource remove = testResourceService.findById(resId);
			removes.add(remove);
			BusinessUnit orgBus = findById(busId);
			if(removes!=null && orgBus!=null){
				orgBus.removeResources(removes);
				testResourceService.delete(remove);
				update(orgBus);
			}
			return true;
		}catch(Exception e){
			throw new ServiceException("service-exception: 删除企业信息生产的pdf时出现异常！",e);
		}
	}

	@Override
	public Long countBusPdfByBusId(Long busId) throws ServiceException {
		try{
			return testResourceService.countBusPdfByBusId(busId);
		}catch(ServiceException se){
			throw new ServiceException("BusinessUnitServiceImpl.countBusPdfByBusId() "+se.getMessage(),se.getException());
		}
	}
	
	/**
	 * 根据企业id获取所有企业信息生产的pdf
	 */
	@Override
	public List<Resource> getBusinessPdfsByBusUnitIdWithPage(Long busId,int page,int pageSize)
			throws ServiceException {
		try{
			return testResourceService.getListBusPdfWithPage(busId, page, pageSize);
		}catch(ServiceException jpae){
			throw new ServiceException("service-exception:根据企业id获取所有企业信息生产的pdf出现异常！",jpae.getException());
		}
	}
	
	/**
	 * 监管系统根据企业id退回企业上传的pdf
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean wdaBackBusUnitById(Long busId,String backMsg)
			throws ServiceException {
		BusinessUnit orig_bus = findById(busId);
		if(orig_bus!=null){
			orig_bus.setWdaBackFlag(true);
			orig_bus.setWdaBackMsg(backMsg);
			update(orig_bus);
			return true;
		}
		return false;
	}

	/**
	 * 统计某个认证关联的产品数
	 */
	@Override
	public Long countProductByBusinessCertificationId(Long busCertId)
			throws ServiceException {
		try{
			return businessCertificationService.countProductByBusinessCertificationId(busCertId);
		}catch(ServiceException e){
			throw new ServiceException("BusinessUnitServiceImpl.countProductByBusinessCertificationId() "+e.getMessage(),e.getException());
		}
	}

	/**
	 * 根据组织机构id获取企业
	 */
	@Override
	public BusinessUnit findByOrganization(Long organization)
			throws ServiceException {
		try{
			return getDAO().findByOrgnizationId(organization);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.findByOrganization() " +daoe.getMessage(),daoe.getException());
		}
	}
	
	/**
     * 验证企业组织机构代码是否重复
     * @param orgCode
     * @return boolean：ture表示重复
     */
	@Override
	public boolean validateBusUnitOrgCode(String orgCode, Long orgId)
			throws ServiceException {
		try{
			return getDAO().validateBusUnitOrgCode(orgCode,orgId);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.validateBusUnitOrgCode() " +daoe.getMessage(),daoe.getException());
		}
	}

	/**
   	 * 根据 企业名称 查找一条企业信息
   	 */
	@Override
	public BusinessUnit findByName(String name) throws ServiceException {
		try {
			return getDAO().findByName(name);
		} catch (DaoException dex) {
			throw new ServiceException("BusinessUnitServiceImpl.findByName()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 功能描述：根据组织机构id获取轻量级企业信息
	 * @author ZhangHui 2015/5/1
	 * 最近更新：ZhangHui 2015/5/14<br>
	 * 更新内容：返回类型变为LightBusUnitVO
	 */
	@Override
	public LightBusUnitVO findBusVOByOrg(Long organization) throws ServiceException {
		try{
			return getDAO().findBusVOByOrg(organization);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.findByOrganization() " +daoe.getMessage(),daoe.getException());
		}
	}
	
	/**
	 * 功能描述：根据企业名称获取轻量级企业信息
	 * @author ZhangHui 2015/5/14
	 */
	@Override
	public LightBusUnitVO findVOByName(String name) throws ServiceException {
		try {
			return getDAO().findVOByName(name);
		} catch (DaoException dex) {
			throw new ServiceException("BusinessUnitServiceImpl.findByName()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 更新企业签名状态
	 * @param busName
	 * @param signFlag
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateSignStatus(String busName, boolean signFlag,boolean passFlag) 
			throws ServiceException{
		try{
			/*BusinessUnit orig_bus = this.findByName(busName);
			if(orig_bus != null){
				orig_bus.setSignFlag(signFlag);
				update(orig_bus);
				return true;
			}
			return false;*/
			return getDAO().updateSignStatus(busName,signFlag,passFlag);
		}catch(Exception e){
			throw new ServiceException("BusinessUnitServiceImpl.updateBusUnitSignStatus() " + e.getMessage(),e);
		}
	}
	
	/**
	 * 获取企业的签名状态
	 * @param busName
	 * @return
	 * @throws ServiceException
	 */
	public boolean findSignFlagByName(String busName) throws ServiceException{
		try{
			return getDAO().findSignFlagByName(busName);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl.findSignFlagByName() " + daoe.getMessage(), daoe);
		}
	}

	/**
	 * 获取当前交易市场下商户总数量
	 */
	public long countMarketByOrganization(Long organization, String configure) throws ServiceException {
		try{
			return getDAO().countMarketByOrganization(getConfigureEn(configure),organization);
		}catch(DaoException daoe){
			throw new ServiceException("BusinessUnitServiceImpl-->countMarketByOrganization()"+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 保存企业信息,如果企业地址不规范时，需要更新企业地址信息
	 * @param busUnit
	 * @return
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessUnit save(BusinessUnit busUnit)throws ServiceException{
		try{
			if(busUnit==null) return null;
			BusinessUnit orig_bus = getDAO().findByName(busUnit.getName());
			if(orig_bus==null){
				create(busUnit);
				orig_bus = busUnit;
			}else {
				String mainAddr = orig_bus.getAddress();
				String otherAddress = orig_bus.getOtherAddress();
				String streetAddr = null;
				if(otherAddress!=null&&otherAddress.split("--").length>1){
					streetAddr = otherAddress.split("--")[1];
				}
				if(mainAddr==null||mainAddr.equals("")||streetAddr==null||streetAddr.equals("")){
					orig_bus.setAddress(busUnit.getAddress());
					orig_bus.setOtherAddress(busUnit.getOtherAddress());
					update(orig_bus);
				}
			}
			return orig_bus;
		}catch(Exception e){
			throw new ServiceException("BusinessUnitServiceImpl-->saveQRCodeProductBusUnit()"+e.getMessage(),e);
		}
	}
	
	/** 
	 * 保存生产企业信息 或者被检人信息 
	 * @param obj 
	 * @author 未知<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/3/16
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void create(JSONObject obj) {
		try {
			String bussName = obj.getString("name");
			BusinessUnit businessUnit = new BusinessUnit();
			if(bussName.equals("") || bussName==null){
				return ;
			}
			businessUnit.setName(bussName);
			businessUnit.setAddress(obj.getString("address"));
			businessUnit.setContact(obj.getString("contact"));
			businessUnit.setEmail(obj.getString("email"));
			businessUnit.setRegion(obj.getString("region"));
			businessUnit.setSampleLocal(obj.getString("sampleLocal"));
			businessUnit.setAdministrativeLevel(obj.getString("administrativeLevel"));
			businessUnit.setPostalCode(obj.getString("postalCode"));
			businessUnit.setPersonInCharge("personInCharge");
			businessUnit.setTelephone(obj.getString("contact_phone"));
			businessUnit.setFax(obj.getString("fax"));
			String license = obj.getString("license_no");
			if(StringUtils.isNotEmpty(license)){
				businessUnit.setLicense(null);
			}else {
				LicenseInfo lic=licenseService.findByLic(license);
				if(lic==null){
					businessUnit.setLicense(new LicenseInfo());
					businessUnit.getLicense().setLicenseNo(license);
				}else {
					businessUnit.setLicense(lic);
				}
			}
			String distribution = obj.getString("distribution_no");
			if(StringUtils.isNotEmpty(distribution)){
				businessUnit.setDistribution(null);
			}else {
				CirculationPermitInfo cir=circulationPermitService.findByDistributionNo(distribution);
				if(cir==null){
					businessUnit.setDistribution(new CirculationPermitInfo());
					businessUnit.getDistribution().setDistributionNo(distribution);
				}else {
					businessUnit.setDistribution(cir);
				}
			}
			create(businessUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存被检测人或生产企业
	 * @param bu
	 * @return Map
	 * @author LongXianZhen
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public Map<String, Object> saveBusinessUnit(BusinessUnitVO bu) {
		Map<String, Object> map=new HashMap<String, Object>();
		String bussName = bu.getName().trim();
		BusinessUnit businessUnit = new BusinessUnit();
		if(bussName.equals("") || bussName==null){
			map.put("msg", "生产企业名称为空");
			map.put("status", "false");
			return map;
		}
		BusinessUnit bus=null;
		try {
			bus=findByName(bussName);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		if(bus!=null){
			map.put("business", bus);
			map.put("status", "true");
			return map;
		}
		businessUnit.setName(bussName);
		businessUnit.setAddress(bu.getAddress()==null||bu.getAddress().equals("")?null:bu.getAddress());
		businessUnit.setContact(bu.getContact()==null||bu.getContact().equals("")?null:bu.getContact());
		businessUnit.setEmail(bu.getEmail()==null||bu.getEmail().equals("")?null:bu.getEmail());
		businessUnit.setRegion(bu.getRegion()==null||bu.getRegion().equals("")?null:bu.getRegion());
		businessUnit.setSampleLocal(bu.getSampleLocal()==null||bu.getSampleLocal().equals("")?null:bu.getSampleLocal());
		businessUnit.setAdministrativeLevel(bu.getAdministrativeLevel()==null||bu.getAdministrativeLevel()
				.equals("")?null:bu.getAdministrativeLevel());
		businessUnit.setPostalCode(bu.getPostalCode()==null||bu.getPostalCode().equals("")?null:bu.getPostalCode());
		businessUnit.setPersonInCharge(bu.getPersonInCharge()==null||bu.getPersonInCharge().equals("")?null:bu.getPersonInCharge());
		businessUnit.setTelephone(bu.getContact_phone()==null||bu.getContact_phone().equals("")?null:bu.getContact_phone());
		businessUnit.setFax(bu.getFax()==null||bu.getFax().equals("")?null:bu.getFax());
		//处理营业执照
		if("".equals(bu.getLicense_no())||bu.getLicense_no()==null){
			businessUnit.setLicense(null);
		}else{
			LicenseInfo lic=licenseService.findByLic(bu.getLicense_no().trim());
			if(lic==null){
				businessUnit.setLicense(new LicenseInfo());
				businessUnit.getLicense().setLicenseNo(bu.getLicense_no());
			}else{
				businessUnit.setLicense(lic);
			}
			
		}
		//处理流通许可证
		if("".equals(bu.getDistribution_no())||bu.getDistribution_no()==null){
			businessUnit.setDistribution(null);
		}else{
			CirculationPermitInfo cir=null;
			try {
				cir = circulationPermitService.findByDistributionNo(bu.getDistribution_no());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			if(cir==null){
				businessUnit.setDistribution(new CirculationPermitInfo());
				businessUnit.getDistribution().setDistributionNo(bu.getDistribution_no());
			}else{
				businessUnit.setDistribution(cir);
			}
			
		}
		try {
			businessUnitDAO.persistent(businessUnit);
		} catch (JPAException e) {
			e.printStackTrace();
			map.put("msg", "保存企业失败");
			map.put("status", "false");
			return map;
		}
		map.put("business", businessUnit);
		map.put("status", "true");
		return map;
	}

	/**
	 * 根据当前登录组织机构ID获取交易市场的名称
	 * @author ZhaWanNeng
	 */
	@Override
	public String getMarketNameByOrganization(Long organization)
			throws ServiceException {
		try{
			String orig_marketName = businessUnitDAO.getMarketNameByOrganization(organization);
			if(StringUtils.isNotEmpty(orig_marketName)){
				orig_marketName  = orig_marketName+"-";
			}
			return orig_marketName;
		}catch(Exception e){
			throw new ServiceException("BusinessUnitServiceImpl-->getMarketNameByOrganization()"+e.getMessage(),e);
		}
	}
	/**
	 * 企业总数
	 * @return Long
	 * @author ZhaWanNeng
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public Long unitCount() throws ServiceException {
		try {
			return getDAO().unitCount();
		} catch (DaoException dex) {
		   throw new ServiceException("BusinessUnitServiceImpl-->unitCount()"+dex.getMessage(), dex.getException());
		}
	}
	
	
	/**
	 * 根据组织机构ID获取企业id
	 * @author ZhangHui 2015/4/8
	 */
	@Override
	public Long findIdByOrg(Long organization) throws ServiceException {
		try{
			return getDAO().getIdByOrganization(organization);
		}catch(Exception e){
			throw new ServiceException("BusinessUnitServiceImpl-->findIdByOrg()" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：根据企业id查找企业组织机构
	 * @throws ServiceException
	 * @author ZhangHui 2015/7/1
	 */
	@Override
	public Long findOrgById(Long organization) throws ServiceException {
		try{
			return getDAO().findOrgById(organization);
		}catch(Exception e){
			throw new ServiceException("BusinessUnitServiceImpl-->findOrgById()" + e.getMessage(), e);
		}
	}
	
	/**
	 * 根据企业名称获取企业id
	 * @author ZhangHui 2015/6/2
	 */
	@Override
	public Long findIdByName(String name) throws ServiceException {
		try{
			return getDAO().getIdByName(name);
		}catch(Exception e){
			throw new ServiceException("BusinessUnitServiceImpl-->findIdByName()" + e.getMessage(), e);
		}
	}

	/**
	 * 客户管理新增客户中的按关键字并分页自动加载客户名称
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	@Override
	public Object getAllBusUnitName(Integer page,Integer pageSize, String keyword,String busType)
			throws ServiceException {
		try{
			return getDAO().getAllBusUnitName(page,pageSize,keyword,busType);
		}catch(DaoException daoe){
			throw new ServiceException("【service-error:】客户管理新增客户中的按关键字并分页自动加载客户名称！",daoe.getException());
		}
	}
	
	/**
	 * 功能描述：保存产品信息<br>
	 * 作用于保存泊银等其他外部系统的样品数据。
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveBYProducer(ProductInstance sample, Long organization) {
		try {
			if(sample == null){ return false; }
			BusinessUnit producer = sample.getProducer();
			if(producer == null){ return false; }
			String bus_guid = producer.getGuid();
			if(bus_guid==null || "".equals(bus_guid)){ return false; }
			
			/* 1. 保存营业执照信息 */
			LicenseInfo orig_license = null;
			if(producer.getLicense()!=null){
				orig_license = licenseService.save(producer.getLicense().getLicenseNo());
				producer.setLicense(orig_license);
			}
			
			/* 2. 保存生产企业信息 */
			BusinessUnit orig_busUnit = getDAO().findByGuid(bus_guid);
			if(orig_busUnit == null){
				/* 2.1 新建企业 */
				create(producer);
			}else{
				/**
				 * ????????????是否允许修改企业信息
				 */
				/*orig_busUnit.setLicense(producer.getLicense());
				orig_busUnit.setAddress(producer.getAddress());
				update(orig_busUnit);*/
				
				producer = orig_busUnit;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 功能描述：获取当前企业的母企业、子企业、兄弟企业<br>
	 * 获取business TreeNode集合
	 * @author ZhangHui 2015/5/18
	 */
	@Override
	public List<BusinessTreeNode> getRelativesOfTreeNodes(int level, String keyword, Long organization) throws ServiceException {
		try {
			return buildProductTree(getDAO().getRelativesOfTreeNodes(level, keyword, organization), level);
		} catch (DaoException e) {
			throw new ServiceException("BusinessUnitServiceImpl.getRelativesOfTreeNodes()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * build BrandCategoryDetail to BrandCategoryTreeNode
	 * @param details
	 * @param level
	 * */
	@SuppressWarnings("deprecation")
	private static List<BusinessTreeNode> buildProductTree(
			List<BusinessTreeDetail> details, int level) {
		if (details.size()<1) {
			return new ArrayList<BusinessTreeNode>(0);
		}

		List<BusinessTreeNode> nodes = new ArrayList<BusinessTreeNode>(
				details.size());
		for (BusinessTreeDetail detail : details) {
			BusinessTreeNode node = new BusinessTreeNode();
			try {
				node.setId(URLEncoder.encode(level + "_" + detail.getName(),
						"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				node.setId(URLEncoder.encode(level + "_" + detail.getName()));
			}

			int index = detail.getName().lastIndexOf('.');
			String realName = detail.getName();
			if (index > 0) {
				realName = detail.getName().substring(index + 1);
			}
			node.setName(realName);
			node.setType("folder");
			node.setHasChildren(detail.getChildrenNum() > 0 ? true : false);
			if (detail.getBusinessId() != -1L) {
				node.setLeafId(detail.getBusinessId());
			}
			nodes.add(node);
		}
		nodes.get(0).setExpanded(false);
		return nodes;
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessUnit updateBusinessUnitAll(BusinessUnit businessUnit, AuthenticateInfo info) throws ServiceException {
	
		BusinessUnit orig_businessUnit = findById(businessUnit.getId());
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
			/* 2.4 保存多条生产许可证信息 
			/* 2.5保存企业其他认证信息*/
			businessCertificationService.save(businessUnit);
			/* 保存销售系统中的 宣传片片和业业二码码*/
			businessSalesInfoService.save(businessUnit,info);
			/* 2.6 保存税务登记证信息 */
			taxRegisterService.save(businessUnit.getTaxRegister());
			if(orig_businessUnit.getType().equals("仁怀市白酒生产企业")){
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
				updateBusinessUnit(businessUnit, businessUnit.getStep());
			}else{
				updateBusinessUnit(businessUnit); // 保存企业基本信息
			}
			enterpriseService.update(businessUnit);  // 保存图片信息
			
			return businessUnit;
	}
	
	/**
	 * 根据企业组织机构id获取企业名称
	 * @param organization
	 * @author tangxin 2015-05-18
	 */
	@Override
	public String findNameByOrganization(Long organization) throws ServiceException{
		try {
			return getDAO().findNameByOrganization(organization);
		} catch (DaoException dao) {
			throw new ServiceException(dao.getMessage(), dao.getException());
		}
	}
	
	/**
	 * 功能描述：根据企业id查找企业名称
	 * @throws ServiceException
	 * @author ZhangHui 2015/7/3
	 */
	@Override
	public String findNameById(Long organization) throws ServiceException{
		try {
			return getDAO().findNameById(organization);
		} catch (DaoException dao) {
			throw new ServiceException(dao.getMessage(), dao.getException());
		}
	}

	/***************************************台帐查看企业List*******************************************************/
	/**
	 * 政府专员查看企业信息，根据企业地址过滤企业
	 * @author HY
	 */
	@Override
	public List<AccountBusinessVO> getAccountEnRegisteList(int page, int pageSize, String province, String city, String area,String nameOrLicNo,String btype) throws ServiceException {
		try {
			return businessUnitDAO.getAccountEnRegisteList(page,pageSize,province,city,area,nameOrLicNo,btype);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(),e.getException());
		}
	}

	/**
	 * 政府专员查看企业信息，根据企业地址过滤企业 加载出的总数
	 * @author HY
	 */
	@Override
	public Long getAccountEnRegisteListTotal(String province, String city, String area,String nameOrLicNo,String btype) throws ServiceException {
		try {
			return businessUnitDAO.getAccountEnRegisteListTotal(province,city,area,nameOrLicNo,btype);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(),e.getException());
		}
	}

	/**
	 * 根据企业id查看企业详情
	 * @author HY
	 */
	@Override
	public AccountBusinessVO getAccountBusinessById(Long busId) throws ServiceException {
		try {
			AccountBusinessVO vo = businessUnitDAO.getAccountBusinessById(busId);
			if(vo != null){
				String date = vo.getRegDate()!=null&&"".equals(vo.getRegDate())?vo.getRegDate().split(".")[0]:vo.getRegDate();
				vo.setRegDate(date);
				EnterpriseRegiste er = enterpriseService.getEnteryByLicNoAndOrgCodeAndBName(vo.getLicNo(),vo.getOrgCode(),vo.getName());
				if(er instanceof EnterpriseRegiste){
					/* 获取企业的组织机构证 */
					String orgImage = er.getOrgAttachments() != null && er.getOrgAttachments().size() > 0 ? er.getOrgAttachments().iterator().next().getUrl() : "";
					/* 获取企业的营业执照证 */
					String licImage = er.getLicAttachments() != null && er.getLicAttachments().size() > 0 ? er.getLicAttachments().iterator().next().getUrl() : "";
					vo.setOrgImage(orgImage);
					vo.setLicImage(licImage);
				}
				/* 当联系人为空的时候就等于负责人 */
				if(vo.getLinkMan()==null || "".equals(vo.getLinkMan())){
					vo.setLinkMan(vo.getPersonInCharge());
				}
			}
			return vo;
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(),e.getException());
		}
	}
	/***********************************************************************************************************/

	/**
	 * 背景：报告录入页面
	 * 功能描述：保存生产企业信息
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/5
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessUnitOfReportVO saveProducer(BusinessUnitOfReportVO bus_vo) throws ServiceException {
		try {
			if(bus_vo==null || bus_vo.getName()==null || "".equals(bus_vo.getName()) ){
				//||bus_vo.getLicenseno()==null || "".equals(bus_vo.getLicenseno())
				throw new Exception("参数为空");
			}
			
			/**
			 * 代码优化，这里不需要查询整个对象，只要根据名称查询相关的值即可
			 * 修改人：wubiao  2016.2.18 10:20
			 */
//			BusinessUnit orig_bus = getDAO().findByName(bus_vo.getName());
			BusinessUnit orig_bus = getDAO().findByNameOrganization(bus_vo.getName());
			
			/**
			 * 当企业已经在系统中存在，下面两种情况不允许更新企业信息
			 * 		1  前台封装的数据显示该企业已经入驻到平台
			 *      2 前台封装的数据显示该企业未入驻到平台，当实时查询得知，该企业已经入驻到平台（要考虑到并发操作）
			 */
			if(orig_bus != null){
				bus_vo.setId(orig_bus.getId());
				if(!bus_vo.isCan_edit_bus() || (orig_bus.getOrganization()!=null && !orig_bus.getOrganization().equals(0L))){
					return bus_vo;
				}else{
					// 更新企业信息
					BusinessUnit buss = new BusinessUnit();
					buss.setId(bus_vo.getId());
					buss.setName(bus_vo.getName());
					buss.setAddress(bus_vo.getAddress());
					
					/* 保存营业执照信息 */
					LicenseInfo orig_license = licenseService.save(bus_vo.getLicenseno());
					
					getDAO().updateRecord(buss, orig_license==null?"":orig_license.getLicenseNo());
				}
			}else{
				// 新增一条企业信息
				BusinessUnit buss = new BusinessUnit();
				buss.setName(bus_vo.getName());
				buss.setAddress(bus_vo.getAddress());
				
				/* 保存营业执照信息 */
				String licenseno = bus_vo.getLicenseno();
				if(licenseno != null){
					licenseno = licenseno.replace(" ", "");
				}
				
				licenseService.save(licenseno);
				
				getDAO().createNewRecord(buss.getName(), buss.getAddress(), licenseno);
				
				// 获取企业id
				Long id = findIdByName(bus_vo.getName());
				if(id == null){
					throw new Exception("企业新增失败");
				}
				bus_vo.setId(id);
			}
			
			return bus_vo;
		} catch (Exception e) {
			throw new ServiceException("BusinessUnitServiceImpl.saveProducer()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 背景：流通企业产品新增/编辑页面
	 * 功能描述：保存产品所属的企业名称
	 * @param business_name  企业名称
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	@Override
	public BusinessUnit saveProducerName(String business_name) throws ServiceException {
		try {
			if(business_name==null || "".equals(business_name.replace(" ", ""))){
				throw new Exception("参数为空");
			}
			
			business_name = business_name.replace(" ", "");
			
			BusinessUnit orig_producer = findByName(business_name);
			
			if(orig_producer == null){
				BusinessUnit producer = new BusinessUnit();
				producer.setName(business_name);
				create(producer);
				orig_producer = producer;
			}
			
			return orig_producer;
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]BusinessUnitServiceImpl.saveProducer()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]BusinessUnitServiceImpl.saveProducer()-->" + e.getMessage(), e);
		}
	}

	/**
	 * 根据产品id查询该产品的所有生产企业
	 * @author longxianzhen 2015-08-06
	 */
	@Override
	public List<BussinessUnitVOToPortal> getBuVOToPortalByProId(Long proId)
			throws ServiceException {
		try {
			List<BussinessUnitVOToPortal> bus=businessUnitDAO.getBuVOToPortalByProId(proId);
			/*for(BussinessUnitVOToPortal bu:bus){
				Map<String,String> map=resourceService.getBusinessUnitCertById(bu.getId());
				if(map!=null){
					String licUrl=map.get("licUrl");
					if(licUrl!=null){
						bu.setLicImg(licUrl);
					}
					String disUrl=map.get("disUrl");
					if(disUrl!=null){
						bu.setDisImg(disUrl);
					}
				}
				List<Resource> qsResource = resourceService.getQsResourceByQsId(bu.getQsId());
				if(qsResource!=null&&qsResource.size()>0){
					bu.setQsImg(qsResource.get(0).getUrl());
				}
			}*/
			return bus;
		} catch (DaoException e) {
			throw new ServiceException("[ServiceException]BusinessUnitServiceImpl.getBuVOToPortalByProId()-->" + e.getMessage(), e.getException());
		}
	}
	
	public BusinessUnit findUnitNameSanZhengInfo(long orgId)throws ServiceException
	{
		BusinessUnit bsine = null;
		try {
			bsine= businessUnitDAO.finUnitSanZenInfo(orgId);
		} catch (DaoException e) {
			return null;
		}
		return bsine;
	}

	/**
	 * 根据用户信息查找商超或供应商的企业信息
	 * @author longxiaznhen 2015/08/07
	 */
	@Override
	public BusinessUnit findSCBusinessByInfo(AuthenticateInfo info)
			throws ServiceException {
		try {
			/* 1. 企业基本信息  */
			BusinessUnit businessUnit = getDAO().findSCByOrgnizationId(info.getOrganization());
			if(businessUnit==null){
				return null;
			}
			LicenseInfo lic=licenseService.findByLic(businessUnit.getLicense().getLicenseNo());
			businessUnit.setLicense(lic);
			OrganizingInstitution org = orgInstitutionService.findByOrgCode(businessUnit.getOrgInstitution().getOrgCode());
			businessUnit.setOrgInstitution(org);
			CirculationPermitInfo cir = circulationPermitService.findByDistributionNo(businessUnit.getDistribution().getDistributionNo());
			businessUnit.setDistribution(cir);
			EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(businessUnit.getName());
			if(orig_enterprise != null){
				/* 4. 各类证照图片  */
				businessUnit.setOrgAttachments(orig_enterprise.getOrgAttachments());
				businessUnit.setLicAttachments(orig_enterprise.getLicAttachments());
				businessUnit.setDisAttachments(orig_enterprise.getDisAttachments());
				businessUnit.setLogoAttachments(orig_enterprise.getLogoAttachments());
				businessUnit.setLiquorAttachments(orig_enterprise.getLiquorAttachments());
				businessUnit.setLiquorCode(orig_enterprise.getServiceNo());//餐饮许可证号
			}
			
			return businessUnit;
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】按组织机构Id查找一条生产商信息,出现异常！", dex.getException());
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BusinessUnit getBusinessUnitByCondition(String businessName,String qsNo,String licenseNo){
		return businessUnitDAO.getBusinessUnitByCondition(businessName,qsNo,licenseNo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBusinessUnit(String strImg,Long id, String url) {
		
		businessUnitDAO.updateBusinessUnit(strImg,id, url);
	}

	/**
	 * 更加产品ID查询所有的企业信息
	 *
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<Resource> getByIdResource(Long id,String strImg) {
		return businessUnitDAO.getByIdResourceList(id,strImg);
	}

	@Override
	public List<BussinessUnitVOToPortal> getBuVOToPortalByBarcode(String barcode) {
		
		return businessUnitDAO.getBuVOToPortalByBarcode(barcode);
	}
	/**
	 * 保存企业基本信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBusinessBasic(BusinessUnit businessUnit, AuthenticateInfo info)
			throws ServiceException {
		try {
			BusinessUnit orig_businessUnit = findById(businessUnit.getId());
			orig_businessUnit.setAddress(businessUnit.getAddress());
			orig_businessUnit.setOtherAddress(businessUnit.getOtherAddress());
			orig_businessUnit.setPersonInCharge(businessUnit.getPersonInCharge());
			orig_businessUnit.setContact(businessUnit.getContact());
			orig_businessUnit.setTelephone(businessUnit.getTelephone());
			orig_businessUnit.setPostalCode(businessUnit.getPostalCode());
			orig_businessUnit.setEmail(businessUnit.getEmail());
			orig_businessUnit.setFax(businessUnit.getFax());
			orig_businessUnit.setAbout(businessUnit.getAbout());
			orig_businessUnit.setWebsite(businessUnit.getWebsite());
			
			update(orig_businessUnit);
			
			/* 5.企业Logo图片 */
			testResourceService.saveLogoResource(businessUnit.getLogoAttachments(), businessUnit.getName());
			
			/* 保存销售系统中的 宣传片片和业业二码码*/
			businessSalesInfoService.save(businessUnit,info);
			
			//同布更新企业注册时所填写的基本信息
			EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(businessUnit.getName());
			if(orig_enterprise!=null){
				orig_enterprise.setEnterptiteAddress(businessUnit.getAddress());
				orig_enterprise.setLegalPerson(businessUnit.getPersonInCharge());
				orig_enterprise.setEmail(businessUnit.getEmail());
				orig_enterprise.setTelephone(businessUnit.getTelephone());
				enterpriseService.update(orig_enterprise);
			}
			
		} catch (Exception e) {
			throw new ServiceException("【service-error】更新该企业在FSN的信息，出现异常。", e);
		}
		
	}
	/**
	 * 保存企业证照信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBusinessCert(BusinessUnit businessUnit,
			AuthenticateInfo info) throws ServiceException {
		try {	
			/* 2.1 保存营业执照信息 */
			licenseService.save(businessUnit.getLicense());
			/* 2.2 保存织机构代码信息  */
			orgInstitutionService.save(businessUnit.getOrgInstitution(), true);
			/* 2.6 保存税务登记证信息 */
			taxRegisterService.save(businessUnit.getTaxRegister());
			
			/* 2.5保存企业其他认证信息*/
			businessCertificationService.save(businessUnit);
			
			
			BusinessUnit orig_businessUnit = findById(businessUnit.getId());
			
			setBusinessUnitValue(orig_businessUnit, businessUnit);
			if(businessUnit.getTaxRegister()!=null){
				orig_businessUnit.setTaxRegister(taxRegisterService.findById(businessUnit.getTaxRegister().getId()));
			}
			if(businessUnit.getLicense()!=null){
				orig_businessUnit.setLicense(licenseService.getDAO().findById(businessUnit.getLicense().getLicenseNo()));				
			}
			if(businessUnit.getOrgInstitution()!=null){
				orig_businessUnit.setOrgInstitution(orgInstitutionService.findByOrgCode(businessUnit.getOrgInstitution().getOrgCode()));
			}
			
			update(orig_businessUnit);

		} catch (Exception e) {
			throw new ServiceException("【service-error】更新该企业在FSN的信息，出现异常。", e);
		}
	}
}