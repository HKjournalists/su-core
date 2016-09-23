package com.gettec.fsnip.fsn.service.business.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.LiutongToProduceDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;
import com.gettec.fsnip.fsn.model.business.LiutongToProduce;
import com.gettec.fsnip.fsn.model.business.LiutongToProduceLicense;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.business.LicenceFormatService;
import com.gettec.fsnip.fsn.service.business.LiutongFieldValueService;
import com.gettec.fsnip.fsn.service.business.LiutongToProduceLicenseService;
import com.gettec.fsnip.fsn.service.business.LiutongToProduceService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.util.FilterUtils;

@Service(value = "liutongToProduceService")
public class LiutongToProduceServiceImpl extends BaseServiceImpl<LiutongToProduce, LiutongToProduceDAO>
		implements LiutongToProduceService {

	@Autowired private LiutongToProduceDAO liutongToProduceDAO;
	@Autowired private LiutongFieldValueService liutongFieldValueService;
	@Autowired private EnterpriseRegisteService enterpriseService;
	@Autowired private LiutongToProduceLicenseService liutongToProduceLicenseService;
	@Autowired private ProductionLicenseService productionLicenseService;
	@Autowired private LicenceFormatService licenceFormatService;

	@Override
	public List<LiutongToProduce> getProducerByOrganizationWithPage(Long orgId,
			String configure, int page, int pageSize) throws ServiceException {
		try{
			Map<String, Object> map = getConfigure(orgId, configure);
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			if(condition != null){
				condition += " order by id desc ";
			}else{
				condition = " order by id desc ";
			}
			return getDAO().getListByPage(page, pageSize, condition, params);
		}catch(JPAException jpae){
			throw new ServiceException("LiutongToProduceServiceImpl.getProducerByOrganizationWithPage()"+jpae.getMessage(), jpae);
		}
	}

	@Override
	public long countByOrgIdAndCondition(Long orgId, String configure)
			throws ServiceException {
		try{
			Map<String, Object> map = getConfigure(orgId, configure);
			String condition = (String) map.get("condition");
			Object[] params = (Object[]) map.get("params");
			return getDAO().count(condition, params);
		}catch(JPAException jpae){
			throw new ServiceException("LiutongToProduceServiceImpl.countByOrgIdAndCondition()"+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 根据流通企业组织机构和生产企业id，获取一条流通企业-生产企业 关系。
	 * @param organization 流通企业组织机构
	 * @param producerId 生产企业id
	 * @return LiutongToProduce
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public LiutongToProduce findByOrganizationAnProducerId(Long organization,
			Long producerId) throws ServiceException {
		try{
			String condition = " where e.organization = ?1 and e.producerId = ?2";
			List<LiutongToProduce> listModel = getDAO().getListByCondition(condition, new Object[]{organization,producerId});
			if(listModel == null || listModel.size()<1) return null;
			return listModel.get(0);
		}catch(JPAException jpae){
			throw new ServiceException("LiutongToProduceServiceImpl.findByOrganizationAnProducerId()"+jpae.getMessage(),jpae);
		}
	}

	/**
	 * 新增流通企业-生产企业 关系。
	 * @param business 生产企业信息
	 * @param organization 流通企业组织机构
	 * @return LiutongToProduce
	 * @throws ServiceException
	 * @author TangXin
	 */
	/*@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LiutongToProduce save(BusinessUnit business, Long organization)
			throws ServiceException {
		try{
			if(business==null || organization==null || business.getId()==null || 
					business.getQsNoAndFormatVo()==null) return null;
			 1.保存 [流通企业-生产企业-qs号] 关系 
			LiutongToProduceLicense orig_liu2prolice = liutongToProduceLicenseService
					.findByOrgIdAndProducerIdAndQs(organization, business.getId(), business.getQsNoAndFormatVo().getQsNo());
			if(orig_liu2prolice == null){
				orig_liu2prolice = saveLiutongToProduceLicense(business, organization);
			}
			 2.保存[生产企业-营业执照号] 关系、[生产企业-组织机构] 关系 
			int fieldFlag = saveLiutongFieldValue(business);
			 3.保存 [流通企业-生产企业] 关系  
			LiutongToProduce orig_liu2pro = findByOrganizationAnProducerId(organization, business.getId());
			if(orig_liu2pro == null){
				orig_liu2pro = saveLiutongToProduce(business, organization);
			}
			 4. 更新生产企业信息完整度状态值 
			if(orig_liu2prolice.isFullFlag()&&(fieldFlag==1||fieldFlag==2)){
				orig_liu2pro.setFullFlag(true);
			}else{
				orig_liu2pro.setFullFlag(false);
			}
			if(orig_liu2prolice.getPassFlag()=="审核通过"&&fieldFlag==1){
				orig_liu2pro.setMsg("审核通过");
			}
			update(orig_liu2pro);
			return orig_liu2pro;
		}catch(ServiceException sex){
			throw new ServiceException("liutongToProduceServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}*/

	/**
	 * 保存[生产企业-营业执照号] 关系、[生产企业-组织机构] 关系 
	 * @param business
	 * @return char 0:fullFlag=false,passFlag=false 1:fullFlag=true,passFlag=true 2:fullFlag=true,passFlag=false
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	private int saveLiutongFieldValue(BusinessUnit business) throws ServiceException {
		try {
			/* 1.根据生产企业名称，获取一条注册企业信息 */
			EnterpriseRegiste orig_enterprise = enterpriseService.findbyEnterpriteName(business.getName());
			/* 2.保存 [生产企业-营业执照号] 关系 */
			LiutongFieldValue orig_liu2liceNo = liutongFieldValueService.findByProducerIdAndValueAndDisplay(business.getId(), 
					business.getLicense()!=null?business.getLicense().getLicenseNo():"", "营业执照号");
			if(orig_liu2liceNo == null){
				/* 封装对象 */
				LiutongFieldValue liu2liceNo = new LiutongFieldValue();
				liu2liceNo.setDisplay("营业执照号");
				liu2liceNo.setPassFlag("等待审核");
				liu2liceNo.setProducerId(business.getId());
				liu2liceNo.setValue(business.getLicense().getLicenseNo());
				/* 从注册信息表中获取营业执照图片 */
				if(orig_enterprise != null){
					Set<Resource> lice_res = copyResource(orig_enterprise.getLicAttachments());
					if(lice_res != null && lice_res.size() > 0){
						liu2liceNo.setAttachments(lice_res);
						liu2liceNo.setFullFlag(true);  // 营业执照信息完整度设为true
						liu2liceNo.setPassFlag("审核通过");
					}
				}
				/* 保存 */
				liutongFieldValueService.create(liu2liceNo);
				orig_liu2liceNo = liu2liceNo;
			}
			/* 2.保存 [生产企业-组织机构] 关系 */
			LiutongFieldValue orig_liu2orgCode = liutongFieldValueService.findByProducerIdAndValueAndDisplay(business.getId(), 
					business.getOrgInstitution()!=null?business.getOrgInstitution().getOrgCode():"", "组织机构代码");
			if(orig_liu2orgCode == null && orig_enterprise != null){
				/* 封装对象 */
				LiutongFieldValue liu2orgCode = new LiutongFieldValue();
				orig_liu2orgCode = liu2orgCode;
				liu2orgCode.setValue(orig_enterprise.getOrganizationNo());
				liu2orgCode.setProducerId(business.getId());
				liu2orgCode.setDisplay("组织机构代码");
				/* 从注册信息表中获取组织机构图片 */
				Set<Resource> org_res = copyResource(orig_enterprise.getOrgAttachments());
				if(org_res != null && org_res.size() > 0){
					liu2orgCode.setAttachments(org_res);
					liu2orgCode.setFullFlag(true);  // 组织机构信息完整度设为true
					liu2orgCode.setPassFlag("审核通过");
				}
				/* 保存 */
				liutongFieldValueService.create(liu2orgCode);
				orig_liu2orgCode = liu2orgCode;
			}
			if(orig_liu2liceNo.getPassFlag().equals("审核通过")&&orig_liu2orgCode.getPassFlag().equals("审核通过")){
				return 1;
			}else{
				if(orig_liu2liceNo.isFullFlag()&&orig_liu2orgCode.isFullFlag()){
					return 2;
				}else{
					return 0;
				}
			}
		} catch (ServiceException sex) {
			throw new ServiceException("LiutongToProduceServiceImpl.saveLiutongFieldValue()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 保存 [流通企业-生产企业] 关系
	 * @param business
	 * @param organization
	 * @return LiutongToProduce
	 * @throws ServiceException 
	 * @author ZhangHui
	 */
	private LiutongToProduce saveLiutongToProduce(BusinessUnit business, Long organization) throws ServiceException {
		try {
			LiutongToProduce liu2pro = new LiutongToProduce();
			liu2pro.setProducerId(business.getId());
			liu2pro.setProducerName(business.getName());
			liu2pro.setMsg("等待审核");
			liu2pro.setOrganization(organization);
			create(liu2pro);
			return liu2pro;
		} catch (ServiceException sex) {
			throw new ServiceException("LiutongToProduceServiceImpl.saveLiutongToProduce()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 保存流通企业-生产企业-qs号 关系
	 * @param orig_liu2prolice
	 * @param business
	 * @param organization
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	/*private LiutongToProduceLicense saveLiutongToProduceLicense(BusinessUnit business,
			Long organization) throws ServiceException {
		try {
			if(business==null || organization==null || business.getId()==null){ return null; }
			LiutongToProduceLicense liu2pro = new LiutongToProduceLicense();
			liu2pro.setOrganization(organization);
			liu2pro.setProducerId(business.getId());
			 1.处理qs号  
			String qsNo = business.getQsNoAndFormatVo().getQsNo();
			ProductionLicenseInfo orig_qsInfo = productionLicenseService.getDAO().findById(qsNo);
			if(orig_qsInfo == null){
				ProductionLicenseInfo qsInfo = new ProductionLicenseInfo();
				Long qsFormatId = business.getQsNoAndFormatVo().getLicenceFormat().getId();
				LicenceFormat licenceFormat = licenceFormatService.findbyId(Long.valueOf(qsFormatId));
				qsInfo.setQsNo(qsNo);
				qsInfo.setQsnoFormat(licenceFormat!=null?licenceFormat:null);
				orig_qsInfo = qsInfo;
			}else{
				 qs号图片存在，则qs号信息完整度为true 
				if(orig_qsInfo.getQsAttachments()!=null && orig_qsInfo.getQsAttachments().size()>0) {
					liu2pro.setFullFlag(true);
				}
			}
			if(liutongToProduceLicenseService.ValidateByProducerIdAndQs(business.getId(), qsNo)){
				liu2pro.setPassFlag("审核通过");
				liu2pro.setFullFlag(true);
			}else{
				liu2pro.setPassFlag("等待审核");
			}
			liu2pro.setQsInstance(orig_qsInfo);
			 2. 保存[通企业-生产企业-qs号 关系] 
			liutongToProduceLicenseService.create(liu2pro);
			return liu2pro;
		} catch (JPAException jape) {
			throw new ServiceException("[JPAException]LiutongToProduceServiceImpl.saveLiutongToProduceLicense()-->" + jape.getMessage(), jape.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]LiutongToProduceServiceImpl.saveLiutongToProduceLicense()-->" + sex.getMessage(), sex.getException());
		}
	}*/

	/**
	 * 保存[流通企业-生产企业]关系
	 * @param liutongToProduce
	 * @param liutongOrgId
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(LiutongToProduce liutongToProduce, long liutongOrgId) throws ServiceException {
		try{
			if(liutongToProduce == null) return;
			LiutongToProduce orig_producer = this.findByOrganizationAnProducerId(liutongOrgId, liutongToProduce.getProducerId());
			if(orig_producer == null){
				liutongToProduce.setMsg("等待审核");
				liutongToProduce.setFullFlag(true);
				liutongToProduce.setOrganization(liutongOrgId);
				create(liutongToProduce);
			}else{
				orig_producer.setMsg(liutongToProduce.getMsg());
				orig_producer.setFullFlag(true);
				update(orig_producer);
			}
		}catch(ServiceException sex){
			throw new ServiceException("LiutongToProduceServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 
	 * @param reportId
	 * @param organization
	 * @return LiutongToProduce
	 * @throws ServiceException
	 * @author TangXin
	 */
	/*@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LiutongToProduce validateProducerByReportId(long reportId, long organization)
			throws ServiceException {
		try{
			TestResult report = testReportService.findById(reportId);
			 1. 获取该报告的qs号 
			ProductToBusinessUnit pro2bus = productToBusinessUnitService.findByProductIdAndBusunitId(
					report.getSample().getProduct().getId(), report.getSample().getProducer().getId());
			BusinessUnit busUnit = report.getSample().getProducer();
			QsNoAndFormatVo qsVO = new QsNoAndFormatVo();
			qsVO.setQsNo(pro2bus!=null?pro2bus.getProductionLicense().getQsNo():"");
			busUnit.setQsNoAndFormatVo(qsVO);
			 2. 保存流通企业-生产企业 关系（兼容快捷版以前的老数据） 
			return save(busUnit, organization);
		}catch(Exception e){
			throw new ServiceException("liutongToProduceServiceImpl.validateProducerByReportId()"+e.getMessage(),e);
		}
	}*/
	
	/**
	 * 审核通过生产企业信息
	 * @param produce
	 * @return void
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void approved(LiutongToProduce produce) throws ServiceException {
		try{
			LiutongToProduce orig_produce = findByOrganizationAnProducerId(produce.getOrganization(), produce.getProducerId());
			orig_produce.setMsg(produce.getMsg());
			update(orig_produce);
		}catch(Exception e){
			throw new ServiceException("liutongToProduceServiceImpl.saveReturn()"+e.getMessage(),e);
		}
	}
	
	private Set<Resource> copyResource(Set<Resource> targetRes){
		if(targetRes == null || targetRes.size()<1) return null;
		Set<Resource> new_Res = new HashSet<Resource>();
		for(Resource res:targetRes){
			if(res.getId()==null) continue;
			//MkTestResource rs = mkTestResourceService.findById(res.getId());
			new_Res.add(res);
		}
		return new_Res;
	}
	
	/**
     * 按以下四个字段信息拼接where字符串
     * @param organizationId 当前登录用户的组织机构ID
     * @param configure 页面过滤条件
     * @return
     */
    private Map<String, Object> getConfigure(Long organizationId, String condition){
        String new_configure = " WHERE e.organization = ?1";
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
     * @param isSon 
     * @return
     * @throws ServiceException
     */
    private String splitJointConfigure(String field,String mark,String value) throws ServiceException{
        try {
            value = URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
        }
        if(field.equals("producerName")){
            return FilterUtils.getConditionStr("producerName",mark,value);
        }
        if(field.equals("fullFlag")){
        	value=(value.equals("是")?"1":value);
        	value=(value.equals("否")?"0":value);
        	String condition = " fullFlag = '"+value+"'";
        	if(mark.equals("neq")||mark.equals("doesnotcontain")){
        		condition = " fullFlag != '"+value+"'";
        	}
            return condition;
        }
        return null;
    }
	
	@Override
	public LiutongToProduceDAO getDAO() {
		return liutongToProduceDAO;
	}

}
