package com.gettec.fsnip.fsn.service.business.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.MarketToBusinessPKDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.business.MarketToBusiness;
import com.gettec.fsnip.fsn.service.business.MarketToBusinessPKService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.util.FilterUtils;

@Service(value="marketToBusinessPKService")
public class MarketToBusinessPKServiceImpl extends BaseServiceImpl<MarketToBusiness, MarketToBusinessPKDAO> 
implements  MarketToBusinessPKService{

	@Autowired private MarketToBusinessPKDAO marketToBusinessPKDAO;
	
	@Override
	public MarketToBusinessPKDAO getDAO() {
		
		return marketToBusinessPKDAO;
	}

	/**
	 * 新增商户
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveBusiness(MarketToBusiness marBusiness, Long organization)
			throws ServiceException {
		try{
			/*1.根据营业执照号验证交易市场是否已经新增过*/
			Long count = marketToBusinessPKDAO.getCountByLicenseAndOrg(marBusiness.getLicense(),organization); 
			if(count>0){
				return false;
			}
			/*2.新增商户到当前交易市场*/
			MarketToBusiness business= new MarketToBusiness();
			business.setName(marBusiness.getName());
			business.setLicense(marBusiness.getLicense());
			business.setPersonInCharge(marBusiness.getPersonInCharge());
			business.setTelephone(marBusiness.getTelephone());
			business.setEmail(marBusiness.getEmail());
			business.setOrganization(organization);
			create(business);
			return true;
		}catch(Exception daoe){
			daoe.printStackTrace();
			throw new ServiceException("MarketToBusinessPKServiceImpl-->saveBusiness："+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 根据营业执照号验证当前交易市场是否已经新增过
	 */
	@Override
	public long getCountByLicenseAndOrg(String license, Long organization)
			throws ServiceException {
		try{
			return marketToBusinessPKDAO.getCountByLicenseAndOrg(license,organization);
		} catch (Exception e) {
			throw new ServiceException("MarketToBusinessPKServiceImpl-->getCountByLicenseAndOrg"+e.getMessage(),e);
		}
	}
	
	/**
	 * 分页加载当前交易市场下所有商户
	 */
	@Override
	public List<MarketToBusiness> getByOrganization(Long organization,
			String configure, int page, int pageSize) throws ServiceException {
		try{
			return marketToBusinessPKDAO.getByOrganization(getConfigure(organization,configure),page,pageSize);
		} catch (Exception e) {
			throw new ServiceException("MarketToBusinessPKServiceImpl-->getByOrganization"+e.getMessage(),e);
		}
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
        if(field.equals("id")){
            return FilterUtils.getConditionStr("id",mark,value);
        }
        if(field.equals("name")){
            return FilterUtils.getConditionStr("name",mark,value);
        }
        if(field.equals("license")){
            return FilterUtils.getConditionStr("license",mark,value);
        }
        if(field.equals("personInCharge")){
            return FilterUtils.getConditionStr("personInCharge",mark,value);
        }
        if(field.equals("telephone")){
        	return FilterUtils.getConditionStr("telephone",mark,value);
        }
        if(field.equals("email")){
        	return FilterUtils.getConditionStr("email",mark,value);
        }
        return null;
    }
    
    /**
     * 根据组织结构统计当前交易市场下商户数量
     */
	@Override
	public long countByOrganization(Long organization, String configure)
			throws ServiceException {
		try{
			return marketToBusinessPKDAO.countByOrganization(getConfigure(organization,configure));
		} catch (Exception e) {
			throw new ServiceException("MarketToBusinessPKServiceImpl-->countByOrganization"+e.getMessage(),e);
		}
	}
	
	/**
	 * 根据企业ID验证当前交易市场和商户是否已建立关系（交易市场查看商户详细信息验证）
	 */
	@Override
	public long getCountBybusIdAndOrg(Long businessId, Long organization)
			throws ServiceException {
		try{
			return marketToBusinessPKDAO.getCountBybusIdAndOrg(businessId,organization);
		} catch (Exception e) {
			throw new ServiceException("MarketToBusinessPKServiceImpl-->getCountBybusIdAndOrg"+e.getMessage(),e);
		}
	}

	/**
	 * 经营主体选择交易市场时，保存关系
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(BusinessUnit businessUnit) throws ServiceException {
		try{
			if(businessUnit == null) return;
			String condition = "";
			/*1.判断当前经营主体是否选择了交易市场，如果没选，需要将之前选择的删除*/
			if(businessUnit.getMarketOrg()==null){
				MarketToBusiness orig_market = marketToBusinessPKDAO.findByBusinessId(businessUnit.getId());
				if(orig_market!=null) marketToBusinessPKDAO.remove(orig_market);
				return;
			}
			LicenseInfo lice = businessUnit.getLicense();
			if(lice==null || lice.getLicenseNo().equals("")) return;
			condition = " where e.business.id = ?1";
			List<MarketToBusiness> orig_listM2B = marketToBusinessPKDAO.getListByCondition(condition,
					new Object[]{businessUnit.getId()});
			/*2.如果交易市场自己添加的经营主体，需要更新信息*/
			if(orig_listM2B!=null && orig_listM2B.size()>0) {
				MarketToBusiness orig_M2B = orig_listM2B.get(0);
				orig_M2B.setBusiness(businessUnit);
				orig_M2B.setEmail(businessUnit.getEmail());
				orig_M2B.setLicense(lice.getLicenseNo());
				orig_M2B.setName(businessUnit.getName());
				orig_M2B.setPersonInCharge(businessUnit.getPersonInCharge());
				orig_M2B.setTelephone(businessUnit.getTelephone());
				orig_M2B.setOrganization(businessUnit.getMarketOrg());
				marketToBusinessPKDAO.merge(orig_M2B);
			}else{
				MarketToBusiness new_M2B = new MarketToBusiness();
				new_M2B.setBusiness(businessUnit);
				new_M2B.setEmail(businessUnit.getEmail());
				new_M2B.setLicense(lice.getLicenseNo());
				new_M2B.setName(businessUnit.getName());
				new_M2B.setPersonInCharge(businessUnit.getPersonInCharge());
				new_M2B.setTelephone(businessUnit.getTelephone());
				new_M2B.setOrganization(businessUnit.getMarketOrg());
				marketToBusinessPKDAO.persistent(new_M2B);
			}
		}catch(Exception daoe){
			throw new ServiceException("MarketToBusinessPKServiceImpl-->save()"+daoe.getCause(),daoe);
		}
		
	}

	/**
	 * 更新交易市场下商户信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public MarketToBusiness updateBusiness(MarketToBusiness marBusiness,
			Long organization) throws ServiceException {
		try{
			MarketToBusiness orig_business = marketToBusinessPKDAO.findById(marBusiness.getId());
			orig_business.setName(marBusiness.getName());
			orig_business.setLicense(marBusiness.getLicense());
			orig_business.setPersonInCharge(marBusiness.getPersonInCharge());
			orig_business.setTelephone(marBusiness.getTelephone());
			orig_business.setEmail(marBusiness.getEmail());
			update(orig_business);
			return orig_business;
		}catch(Exception daoe){
			throw new ServiceException("MarketToBusinessPKServiceImpl-->updateBusiness()"+daoe.getCause(),daoe);
		}
	}
}
