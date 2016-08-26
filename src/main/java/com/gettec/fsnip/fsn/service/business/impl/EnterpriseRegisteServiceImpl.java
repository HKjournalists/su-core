package com.gettec.fsnip.fsn.service.business.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.EnterpriseRegisteDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.EnterpriseRegiste;
import com.gettec.fsnip.fsn.model.business.ProducingDepartment;
import com.gettec.fsnip.fsn.service.business.EnterpriseRegisteService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.business.BussinessUnitVO;

@Service(value="enterpriseService")
public class EnterpriseRegisteServiceImpl extends BaseServiceImpl<EnterpriseRegiste, EnterpriseRegisteDAO>
		implements EnterpriseRegisteService{
	@Autowired private EnterpriseRegisteDAO enterpriseRegisteDAO;
	@Autowired protected ResourceService testResourceService;
	
	@Override
	public EnterpriseRegisteDAO getDAO() {
		return enterpriseRegisteDAO;
	}

	/**
	 * 新增一条注册信息（兼容以前的老企业数据）
	 * @param businessUnit
	 * @return void
	 * @throws ServiceException
	 * @author ZhangHui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(BusinessUnit businessUnit, AuthenticateInfo info) throws ServiceException {
		try {
			EnterpriseRegiste enterprise = new EnterpriseRegiste();
			enterprise.setUserName(info.getUserName());
			enterprise.setEmail(businessUnit.getEmail());
			enterprise.setEnterpriteName(businessUnit.getName());
			enterprise.setEnterptiteAddress(businessUnit.getAddress());
			enterprise.setStatus("审核通过");
			enterprise.setEnterpriteDate(new Date());
			enterprise.setEnterpriteType(businessUnit.getType());
			create(enterprise);
		} catch (ServiceException sex) {
			throw new ServiceException("EnterpriseRegisteServiceImpl.save()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 更新
	 * @param businessUnit
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(BusinessUnit businessUnit) throws ServiceException {
		try {
			/* 1.组织机构代码证件 */
			testResourceService.saveOrgnizationResource(businessUnit.getOrgAttachments(), businessUnit);
			/* 2.营业执照图片 */
			testResourceService.saveLicenseResource(businessUnit.getLicAttachments(), businessUnit.getName());
			/* 3.流通许可证图片 */
			testResourceService.saveDisResource(businessUnit.getDisAttachments(), businessUnit.getName());
			/* 4.生产许可证图片 */
			//testResourceService.saveQsResource(businessUnit.getQsAttachments(), businessUnit.getName());
			/* 5.企业Logo图片 */
			testResourceService.saveLogoResource(businessUnit.getLogoAttachments(), businessUnit.getName());
			/* 6.税务登记证图片 */
			testResourceService.saveTaxRegResource(businessUnit.getTaxRegAttachments(), businessUnit.getName());
			/* 7.酒类销售许可证图片*/
			testResourceService.saveLiquorResource(businessUnit.getLiquorAttachments(), businessUnit.getName());
			/* 8.企业生产车间图片*/
			for(ProducingDepartment proDep:businessUnit.getProDepartments()){
				testResourceService.saveProDepResource(proDep.getDepAttachments(),proDep.getId());
			}
		}catch (ServiceException sex) {
			throw sex;
		}
	}

	/**
	 * 根据企业名称查找logo
	 * @throws ServiceException 
	 */
	@Override
	public String findLogoByName(String name) throws ServiceException {
		try {
			return getDAO().findLogoByEnterpriteName(name);
		}catch (DaoException dex) {
			throw new ServiceException("根据企业名称查找logo, 出现异常", dex);
		}
	}

	/**
	 * 根据企业id加载企业信息  包括：企业名称、企业法人、企业证照、主营商品
	 * @param id
	 * @return BussinessUnitVO
	 * @throws ServiceException
	 */
	@Override
	public BussinessUnitVO loadBusinessUnit(String name)throws ServiceException {
		BussinessUnitVO bussinessUnitVO = new BussinessUnitVO();
		try {
			EnterpriseRegiste enterpriseRegiste = getDAO().findbyEnterpriteName(name);
			if(enterpriseRegiste != null){
				bussinessUnitVO.setId(enterpriseRegiste.getId());
				bussinessUnitVO.setAddress(enterpriseRegiste.getEnterptiteAddress());
				bussinessUnitVO.setLicImageUrl(enterpriseRegiste.getLicAttachments());
				bussinessUnitVO.setLicenseNo(enterpriseRegiste.getLicenseNo());
				bussinessUnitVO.setName(enterpriseRegiste.getEnterpriteName());
				bussinessUnitVO.setOrgImageUrl(enterpriseRegiste.getOrgAttachments());
				bussinessUnitVO.setPersonInCharge(enterpriseRegiste.getLegalPerson());
				bussinessUnitVO.setType(enterpriseRegiste.getEnterpriteType());
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return bussinessUnitVO;
	}

	/**
	 * 获取企业类型（行业分类）
     * @return List<String>
	 */
    @Override
    public List<String> getBuseinssUnitType() throws ServiceException {
        try {
            return getDAO().getBuseinssUnitType();
        } catch(Exception e){
            throw new ServiceException("BusinessUnitServiceImpl.getBuseinssUnitType() "+e.getMessage(),e);
        }
    }

    /**
     * 根据企业类型查找相关的所有企业
     * @param type  企业类型
     * @return List<EnterpriseRegiste>
     */
    @Override
    public List<String> getBuseinssUnitByType(int page,int pageSize,String type) throws ServiceException {
        try {
            return getDAO().getBuseinssUnitByType(page,pageSize,type);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据企业类型查找相关的所有企业 的名称 总数
     * @param type 企业类型
     * @return Object
     * @throws ServiceException
     */
    @Override
    public Object getBuseinssUnitCountByType(String type) throws ServiceException {
        try {
            return getDAO().getBuseinssUnitCountByType(type);
        } catch (DaoException e) {
            throw new ServiceException("BusinessUnitServiceImpl.getBuseinssUnitCountByType() "+e.getMessage(),e);
        }
    }
	
	/**
	 * 获得所有注册企业集合有分页处理
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @return List<EnterpriseRegiste>
	 * @throws ServiceException
	 */
	@Override
	public List<EnterpriseRegiste> getEnRegisteListByPage(int page,
			int pageSize, Map<String, Object> configure) throws ServiceException {
		try {
			return getDAO().getEnRegisteListByPage(page, pageSize, configure);
		} catch (DaoException dex) {
			throw new ServiceException("EnterpriseRegisteServiceImpl.getEnRegisteListByPage()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 获得所有注册企业集合总数
	 * @param configure
	 * @return List<EnterpriseRegiste>
	 * @throws ServiceException
	 */
	@Override
	public Long getAllCount(Map<String, Object> configure) throws ServiceException {
		try {
			return getDAO().getAllCount(configure);
		} catch (DaoException dex) {
			throw new ServiceException("EnterpriseRegisteServiceImpl.getAllCount()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据企业名称，查找注册记录
	 * @param name
	 * @return EnterpriseRegiste
	 * @throws ServiceException
	 */
	@Override
	public EnterpriseRegiste findbyEnterpriteName(String name) throws ServiceException {
		try {
			return getDAO().findbyEnterpriteName(name);
		} catch (DaoException dex) {
			throw new ServiceException("EnterpriseRegisteServiceImpl.findbyEnterpriteName()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 验证注册企业名称唯一性
	 * @param name
	 * @return EnterpriseRegiste
	 * @throws ServiceException
	 */
	@Override
	public boolean verificationEnName(String name) throws ServiceException {
		try {
			return getDAO().verificationEnName(name);
		} catch (DaoException dex) {
			throw new ServiceException("EnterpriseRegisteServiceImpl.verificationEnName()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 验证注册企业用户名唯一性
	 * @param name
	 * @return EnterpriseRegiste
	 * @throws ServiceException
	 */
	@Override
	public boolean verificationEnUserName(String userName) throws ServiceException {
		try {
			return getDAO().verificationEnUserName(userName);
		} catch (DaoException dex) {
			throw new ServiceException("EnterpriseRegisteServiceImpl.verificationEnUserName()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 查询相关企业类型下面的 与模糊名称匹配的企业名称
	 * @param page
	 * @param pageSize
	 * @param type 企业类型
	 * @param name 用户输入企业名称的关键字
	 * @return List<String> 企业名称
	 */
	@Override
	public List<String> getBuseinssUnitNameByTypeAndName(int page,
			int pageSize, String type, String name) throws ServiceException {
		
		try {
            return getDAO().getBuseinssUnitByTypeAndName(page,pageSize,type,name);
        } catch (DaoException e) {
            throw new ServiceException("BusinessUnitServiceImpl.getBuseinssUnitCountByType() "+e.getMessage(),e);
        }
	}
	
	/**
	 * 查询相关企业类型下面的 与模糊名称匹配的企业名称 的总数
	 * @param type 企业类型
	 * @param name 用户输入企业名称的关键字
	 * @return Object 总数
	 */
	@Override
	public Object getBuseinssUnitNameCountByType(String type, String name)
			throws ServiceException {
		try {
            return getDAO().getBuseinssUnitCountByTypeAndName(type,name);
        } catch (DaoException e) {
            throw new ServiceException("BusinessUnitServiceImpl.getBuseinssUnitCountByType() "+e.getMessage(),e);
        }
	}
	
	/**
     * 根据营业执照号，获取已注册企业数量
     * @param licenseNo 营业执照号
     * @return 已经使用该营业执照号注册的已注册企业数量<br>
     * @author ZhangHui 2015/4/29
     */
	@Override
	public long countByLicenseNo(String licenseNo) throws ServiceException {
		try{
			return getDAO().countByLicenseNo(licenseNo);
		}catch(DaoException e){
			throw new ServiceException("BusinessUnitServiceImpl.countByLicenseNo()-->" + e.getMessage(), e.getException());
		}
	}
	/**
	 * 根据餐饮服务许可证号，获取已注册企业数量
	 * @param serviceNo 餐饮服务许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	@Override
	public long countByServiceNo(String serviceNo) throws ServiceException {
		try{
			return getDAO().countByServiceNo(serviceNo);
		}catch(DaoException e){
			throw new ServiceException("BusinessUnitServiceImpl.countByServiceNo()-->" + e.getMessage(), e.getException());
		}
	}
	/**
	 * 根据食品流通许可证号，获取已注册企业数量
	 * @param passNo 食品流通许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	@Override
	public long countByPassNo(String passNo) throws ServiceException {
		try{
			return getDAO().countByPassNo(passNo);
		}catch(DaoException e){
			throw new ServiceException("BusinessUnitServiceImpl.countByPassNo()-->" + e.getMessage(), e.getException());
		}
	}
	/**
	 * 根据s生产许可证号，获取已注册企业数量
	 * @param productNo 生产许可证号
	 * @return 已经使用该营业执照号注册的已注册企业数量<br>
	 * @author HCJ 2015/5/17
	 */
	@Override
	public long countByProductNo(String productNo)  throws ServiceException {
		try{
			return getDAO().countByProductNo(productNo);
		}catch(DaoException e){
			throw new ServiceException("BusinessUnitServiceImpl.countByProductNo()-->" + e.getMessage(), e.getException());
		}
	}

	@Override
	public EnterpriseRegiste getEnteryByLicNoAndOrgCodeAndBName(String licNo, String orgCode, String bName) throws ServiceException {
		try {
			return getDAO().getEnteryByLicNoAndOrgCodeAndBName(licNo,orgCode,bName);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(),e.getException());
		}
	}
}