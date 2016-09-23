package com.gettec.fsnip.fsn.service.erp.impl;

import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.LicenseDAO;
import com.gettec.fsnip.fsn.dao.erp.ProviderDAO;
import com.gettec.fsnip.fsn.dao.erp.ProviderTypeDAO;
import com.gettec.fsnip.fsn.dao.erp.ReceivingNoteDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.erp.base.ContactInfo;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfo;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.LicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.BusinessUnitToTypeService;
import com.gettec.fsnip.fsn.service.erp.ContactInfoService;
import com.gettec.fsnip.fsn.service.erp.CustomerAndProviderTypeService;
import com.gettec.fsnip.fsn.service.erp.CustomerService;
import com.gettec.fsnip.fsn.service.erp.CustomerToContactinfoService;
import com.gettec.fsnip.fsn.service.erp.ProviderService;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("providerService")
public class ProviderServiceImpl extends BaseServiceImpl<BusinessUnit, ProviderDAO> 
		implements ProviderService {
	@Autowired private ProviderDAO providerDAO;
	@Autowired private ProviderTypeDAO providerTypeDAO;
	@Autowired private LicenseDAO licenseDAO;
	@Autowired private ContactInfoService contactInfoService;
	@Autowired private CustomerToContactinfoService customerToContactinfoService;
	@Autowired private ReceivingNoteDAO receivingNoteDAO;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private BusinessUnitToTypeService businessUnitToTypeService;
	@Autowired private CustomerAndProviderTypeService customerAndProviderTypeService;
	@Autowired private LicenseService licenseService;
	@Autowired private CustomerService customerService;
	
	@Override
	public ProviderDAO getDAO() {
		return providerDAO;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO add(ResultVO resultVO,Long organization) {
		try {
			BusinessUnit provider = (BusinessUnit)resultVO.getObject();
			/* 1. 处理营业执照号 */
			LicenseInfo orig_licenInfo=licenseDAO.findById(provider.getLicense().getLicenseNo());
			if(orig_licenInfo == null){
				licenseDAO.persistent(provider.getLicense());
			}else{
				provider.setLicense(orig_licenInfo);
			}
			/* 2. 保存供应商 */
			BusinessUnit orig_currentBusiness = businessUnitService.findByOrganization(organization); // 登录企业
			BusinessUnit orig_provider = businessUnitService.findByName(provider.getName()); // 供应商
			if(orig_provider == null){
				/* 2.1 新增供应商  */
				providerDAO.persistent(provider);
				orig_currentBusiness.getProviders().add(provider);
			}else{
				/* 2.2 更新供应商 */
				for(BusinessUnit prov : orig_currentBusiness.getProviders()){
					if(prov.getName().equals(orig_provider.getName())){
						resultVO.setErrorMessage("同一企业供货商名称不能相同!");
						resultVO.setStatus(SERVER_STATUS_FAILED);
						return resultVO;
					}
				}
				if(orig_currentBusiness.getName().equals(orig_provider.getName())){
					orig_currentBusiness.setNote(provider.getNote());
					orig_currentBusiness.setLicense(provider.getLicense());
					orig_currentBusiness.getProviders().add(orig_currentBusiness);
				}else{
					orig_provider.setNote(provider.getNote());
					orig_provider.setLicense(provider.getLicense());
					orig_currentBusiness.getProviders().add(orig_provider);
				}
				provider.setId(orig_provider.getId());
			}
			providerDAO.merge(orig_currentBusiness);
			/* 3. 保存供应商与供应商类型的关联 */
			resultVO = businessUnitToTypeService.saveType(provider, organization);
			if(resultVO.getStatus().equals("false")){
				return resultVO;
			}
			/* 4. 保存联系人信息 */
			for(ContactInfo info : provider.getContacts()){
				info.setOrganization(organization);
			}
			//provider.getDiyType().setType(2);  // 2: 供应商类型
			List<ContactInfo> infos = contactInfoService.addListOfContactInfo(provider.getContacts());
			for(ContactInfo info : infos){
				provider.addRelationShipInfo(info);
			}
			/* 5. 保存供应商与联系人的关联表 */
			customerToContactinfoService.addRelationShips(provider.getInfos());
			resultVO.setMessage("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage("Error");
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		return resultVO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultVO updateProvider(ResultVO resultVO) {
		try {
			BusinessUnit provider = (BusinessUnit)resultVO.getObject();
			//provider.getDiyType().setType(2);
			BusinessUnit orig_customer = providerDAO.findById(provider.getId());
			/* 1. 处理营业执照号 */
			LicenseInfo orig_license = licenseDAO.findById(provider.getLicense().getLicenseNo());
			if(orig_license == null){
				licenseService.create(provider.getLicense());
			}else{
				//provider.setLicense(orig_license);
			}
			/* 保存供应商与供应商类型的关联 */
			resultVO = businessUnitToTypeService.saveType(provider, provider.getOrganization());
			if(resultVO.getStatus().equals("false")){
				return resultVO;
			}
			/* 2. 保存联系人 */
			contactInfoService.save(provider);
			/* 3. 更新供应商信息  */
			for(BusinessUnit pro : orig_customer.getCustomers()){
				if(pro.getName().equals(provider.getName()) && !pro.getId().equals(provider.getId())){
					resultVO.setErrorMessage("当前企业供货商名称不能相同!");
					resultVO.setStatus(SERVER_STATUS_FAILED);
					return resultVO;
				}
			}
			//orig_customer.setLicense(provider.getLicense());
			orig_customer.setName(provider.getName());
			orig_customer.setNote(provider.getNote());
			//resultVO.setObject(orig_customer);
			return resultVO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<BusinessUnit> getAllprovider(Long organization) {
		try {
			PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
			String configure = " WHERE e.organization = " + organization;
			result.setListOfModel(providerDAO.findAll(organization, configure));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 根据过滤条件查询当前登录企业的供应商列表信息
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 * @throws DaoException 
	 */
	public PagingSimpleModelVO<BusinessUnit> getProviderfilter(int page, int pageSize, 
			String condition, Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
			String configure = customerService.getConfigure(condition);
			/* 1. 获取供应商列表 */
			List<BusinessUnit> customers = providerDAO.getListByConditionAndOrgId(configure, organization, page, pageSize);
			result.setListOfModel(customers);
			/* 2. 获取供应商总数 */
			Long count = providerDAO.getCountByConditionAndOrgId(configure, organization);
			result.setCount(count);
			/* 3. 获取供应商自定义类型 */
			for(BusinessUnit busunit : result.getListOfModel()){
				busunit.setDiyType(customerAndProviderTypeService.findByBid(busunit.getId(), 2L, organization)); //2L:供应商
			}
			return result;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]CustomerServiceImpl.getCustomerfilter()-->", dex.getException());
		}
	}
	
	public long countProviderfilter(String configure) {
		String realConfigure = customerService.getConfigure(configure);
		return providerDAO.countProvider(realConfigure);
	}
	
	/**
	 * 供应商分页查询
	 * @param page
	 * @param size
	 * @param keywords
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PagingSimpleModelVO<BusinessUnit> getPaging(int page, int size,
			String keywords,Long organization) throws ServiceException {
		try {
	        Long  org = organization;
			PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
			/* 1. 获取供应商列表 */
			List<BusinessUnit> customers = providerDAO.getListByConditionAndOrgId(null, org, page, size);
			for (BusinessUnit businessUnit : customers) {
				organization = organization==null?businessUnit.getOrganization():organization;
				/* 1.1 获取供应商自定义类型 */
				businessUnit.setDiyType(customerAndProviderTypeService.findByBid(businessUnit.getId(), null, organization)); //供应商
				CustomerAndProviderType ptype = businessUnit.getDiyType();
				int type = (ptype != null ? ptype.getType() : 2);
				/* 1.2  获取供应商联系人（默认取第一条） */
				List<ContactInfo> listContacts = contactInfoService.getListByBusIdAndType(1, 1, businessUnit.getId(), type, organization);
				businessUnit.setContacts(listContacts);
				organization = organization==null?businessUnit.getOrganization():organization;
			}
			result.setListOfModel(customers);
			/* 2. 获取供应商总数 */
			Long count = providerDAO.getCountByConditionAndOrgId(null, org);
			result.setCount(count);
			return result;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]ProviderServiceImpl.getPaging()-->" + dex.getMessage(), dex.getException());
		}
	}
    
	/**
	 * 删除供应商
	 * @param provider
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean remove(BusinessUnit provider, Long organization) throws ServiceException {
		try {
			/* 1. 判断该供应商是否被使用 */
			String condition = " WHERE e.re_provide_num.id = ?1";
			Object[] params = new Object[]{provider.getId()};
			long count = receivingNoteDAO.count(condition, params);
			if(count > 0) {
				return false; // 供应商已经被使用
			}
			/* 2. 删除供应商与联系人的关系 */
			List<CustomerToContactinfo> orig_providToContacts = customerToContactinfoService.getListByIdAndType(provider.getId(), 2);
			for(CustomerToContactinfo providToContact : orig_providToContacts){
				customerToContactinfoService.delete(providToContact);
			}
			/* 3. 删除供应商与自定义类型的关系 */
			CustomerAndProviderType ctype = provider.getDiyType();
			long type =(long) (ctype != null ? ctype.getType() : 2);
			businessUnitToTypeService.getDAO().removeByBussidAndType(provider.getId(), organization, type);
			/* 4. 删除供应商 */
			BusinessUnit orig_busunit = businessUnitService.findByOrganization(organization);
			orig_busunit.removeProvider(businessUnitService.findById(provider.getId()));
			update(orig_busunit);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]ProviderServiceImpl.remove()-->", jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]ProviderServiceImpl.remove()-->", dex.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]ProviderServiceImpl.remove()-->", sex.getException());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<BusinessUnit> getProvider(Long organization) {
		BusinessUnit bus = null;
		Set<BusinessUnit> setB =new HashSet<BusinessUnit>();
		List<BusinessUnit> ListB = new ArrayList<BusinessUnit>();
		try {
			bus = providerDAO.findById(businessUnitService.findByOrganization(organization).getId());
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (JPAException e) {
			e.printStackTrace();
		}
		setB = bus.getProviders();
		Iterator<BusinessUnit> it = setB.iterator();
		while(it.hasNext()){
			ListB.add(it.next());
		}
		return ListB;
	}

	/**
	 * 判断供应商是否被使用
	 * @param businessUnit
	 * @param organization
	 * @return
	 *     true : 没有被使用
	 *     false: 被使用
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean judgeIsUsed(BusinessUnit businessUnit, Long organization) throws ServiceException {
		try {
			String condition = " WHERE e.re_provide_num.id = ?1";
			long count = receivingNoteDAO.count(condition, new Object[]{businessUnit.getId()});
			if(count > 1) {
				return false;
			}return true;
		} catch (JPAException jpae) {
			throw new ServiceException("ProviderServiceImpl.judgeIsUsed()--> ", jpae.getException());
		}
	}

	/**
	 * 根据组织机构、供应商id，查找数量
	 * @author ZhangHui 2015/4/13
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long count(Long organization, Long businessId) throws ServiceException {
		try {
			return getDAO().count(organization, businessId);
		} catch (DaoException e) {
			throw new ServiceException("ProviderServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 报告待处理供应商查询
	 * @author ZhangHui 2015/5/4
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PagingSimpleModelVO<BusinessUnit> getPagingOfOnHandProducer(int page, int size, Long organization,String configure) throws ServiceException {
		try {
			PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
			/* 1. 获取供应商列表 */
			Long toBusId = null;
			if(null!=organization){
				toBusId = businessUnitService.findIdByOrg(organization);
			}
			List<BusinessUnit> customers = getDAO().getListOfOnHandProducer(page, size, toBusId,configure);
			for (BusinessUnit businessUnit : customers) {
				/* 1.1 获取供应商联系人（默认取第一条） */
				List<ContactInfo> listContacts = contactInfoService.getListByBusIdAndType(1, 1, businessUnit.getId(), 2, organization);
				businessUnit.setContacts(listContacts);
			}
			result.setListOfModel(customers);
			/* 2. 获取供应商总数 */
			Long count = getDAO().getCountOfOnHandProducer(toBusId,configure);
			result.setCount(count);
			/* 3. 获取供应商自定义类型 */
			for(BusinessUnit busunit : result.getListOfModel()){
				busunit.setDiyType(customerAndProviderTypeService.findByBid(busunit.getId(), 2L, organization)); //2L:供应商
			}
			return result;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]ProviderServiceImpl.getPaging()-->" + dex.getMessage(), dex.getException());
		}
	}
}
