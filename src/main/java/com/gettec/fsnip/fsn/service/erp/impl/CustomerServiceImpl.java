package com.gettec.fsnip.fsn.service.erp.impl;

import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.business.LicenseDAO;
import com.gettec.fsnip.fsn.dao.erp.CustomerDAO;
import com.gettec.fsnip.fsn.dao.erp.OutOfBillDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.LicenseInfo;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToType;
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
import com.gettec.fsnip.fsn.service.erp.CustomerTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("customerService")
public class CustomerServiceImpl extends BaseServiceImpl<BusinessUnit, CustomerDAO> implements CustomerService {
	@Autowired private CustomerDAO customerDAO;
	// @Autowired private CustomerTypeDAO customerTypeDAO;
	@Autowired private LicenseDAO licenseDAO;
	@Autowired private ContactInfoService contactInfoService;
	@Autowired private CustomerToContactinfoService customerToContactinfoService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private OutOfBillDAO outOfBillDAO;
	@Autowired private BusinessUnitToTypeService businessUnitToTypeService;
	@Autowired private CustomerAndProviderTypeService customerAndProviderTypeService;
	@Autowired private LicenseService licenseService;
    @Autowired private CustomerTypeService customerTypeService;
    
    private final String PRODUCT_FROM_CUSTOMER = "产品来源客户";
    
	/**
	 * 添加客户
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO add(ResultVO resultVO, Long organization) {
		try{
			BusinessUnit customer = (BusinessUnit)resultVO.getObject();
			/* 1. 处理营业执照号 */
			LicenseInfo orig_licenInfo=licenseDAO.findById(customer.getLicense().getLicenseNo());
			if(orig_licenInfo == null){
				licenseDAO.persistent(customer.getLicense());
			}else{
				customer.setLicense(orig_licenInfo);
			}
			/* 2. 保存客户 */
			BusinessUnit orig_currentBusiness = businessUnitService.findByOrganization(organization); // 登录企业
			BusinessUnit orig_customer = businessUnitService.findByName(customer.getName()); // 客户
			if(orig_customer == null){
				/* 2.1 新增客户  */
				customerDAO.persistent(customer);
				orig_currentBusiness.getCustomers().add(customer);
			}else{
				/* 2.2 更新客户 */
				customer.setOrganization(orig_customer.getOrganization());
				for(BusinessUnit cust : orig_currentBusiness.getCustomers()){
					if(cust.getName().equals(orig_customer.getName())){
						resultVO.setErrorMessage("同一企业客户名称不能相同!");
						resultVO.setStatus(SERVER_STATUS_FAILED);
						return resultVO;
					}
				}
				if(orig_currentBusiness.getName().equals(orig_customer.getName())){
					orig_currentBusiness.setNote(customer.getNote());
					orig_currentBusiness.setLicense(customer.getLicense());
					orig_currentBusiness.getCustomers().add(orig_currentBusiness);
				}else{
					orig_customer.setNote(customer.getNote());
					orig_customer.setLicense(customer.getLicense());
					orig_currentBusiness.getCustomers().add(orig_customer);
				}
				customer.setId(orig_customer.getId());
			}
			customerDAO.merge(orig_currentBusiness);
			/* 3.保存客户与客户类型的关联(注意:经销商模块客户类型不是必填项) */
			resultVO = businessUnitToTypeService.saveType(customer, organization);
			if(resultVO.getStatus().equals("false")){
				return resultVO;
			}
			/* 4. 保存联系人信息 */
			for(ContactInfo info : customer.getContacts()){
				info.setOrganization(organization);
			}
			//customer.getDiyType().setType(1);  // 1: 客户类型
			List<ContactInfo> infos = contactInfoService.addListOfContactInfo(customer.getContacts());
			for(ContactInfo info : infos){
				customer.addRelationShipInfo(info);
			}
			/* 5. 保存客户与联系人的关联表 */
			customerToContactinfoService.addRelationShips(customer.getInfos());
			resultVO.setMessage("添加成功");
		}catch(Exception e){
			resultVO.setErrorMessage("error");
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		return resultVO;
	}
	
	/**
	 * 更新客户
	 * @param customer
	 * @param organization
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultVO updateCustomer(ResultVO resultVO) {
		try {
			BusinessUnit customer = (BusinessUnit)resultVO.getObject();
			/*if(customer.getDiyType() != null) {
				customer.getDiyType().setType(1);
			}*/
			BusinessUnit orig_customer = customerDAO.findById(customer.getId());
			/* 1. 处理营业执照号 */
			LicenseInfo orig_license = licenseDAO.findById(customer.getLicense().getLicenseNo());
			if(orig_license == null){
				licenseService.create(customer.getLicense());
			}else{
				//customer.setLicense(orig_license);
			}
			/* 保存客户类型 */
			resultVO = businessUnitToTypeService.saveType(customer, customer.getOrganization());
			if(resultVO.getStatus().equals("false")){
				return resultVO;
			}
			/* 2. 保存联系人 */
			contactInfoService.save(customer);
			/* 3. 更新客户信息  */
			for(BusinessUnit custo : orig_customer.getCustomers()){
				if(custo.getName().equals(customer.getName()) && !custo.getId().equals(customer.getId())){
					resultVO.setErrorMessage("当前企业客户名称不能相同!");
					resultVO.setStatus(SERVER_STATUS_FAILED);
					return resultVO;
				}
			}
			//orig_customer.setLicense(customer.getLicense());
			orig_customer.setName(customer.getName());
			orig_customer.setNote(customer.getNote());
			//resultVO.setObject(orig_customer);
			return resultVO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<BusinessUnit> getAllByOrganization(Long organization) {
		try {
			String configure = " WHERE e.organization = " + organization;
			return customerDAO.findAll(organization, configure);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<BusinessUnit> getCustomer(Long organization) {
		try {
			return getDAO().getListByCustomerType(organization, 1, 0, 0);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 客户分页查询
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
			String keywords, Long organization) throws ServiceException{
		try {
			PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
			/* 1. 获取客户列表 */
			List<BusinessUnit> customers = customerDAO.getListByConditionAndOrgId(null, organization, page, size);
			for (BusinessUnit businessUnit : customers) {
				/* 1.1 获取客户自定义类型 */
//				businessUnit.setDiyType(customerAndProviderTypeService.findByBid(businessUnit.getId(), null, organization)); //客户
				/* 1.2 获取客户联系人（默认取第一条） */
				CustomerAndProviderType ctype = businessUnit.getDiyType();
				int type = (ctype != null ? ctype.getType() : 1);
				List<ContactInfo> listContacts = contactInfoService.getListByBusIdAndType(1, 1, businessUnit.getId(), type, organization);
				businessUnit.setContacts(listContacts);
			}
			result.setListOfModel(customers);
			/* 2. 获取客户总数 */
			Long count = customerDAO.getCountByConditionAndOrgId(null, organization);
			result.setCount(count);
			return result;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]CustomerServiceImpl.getPaging()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据过滤条件查询客户列表信息
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public PagingSimpleModelVO<BusinessUnit> getCustomerfilter(int page, int pageSize, String condition, 
			Long organization) throws ServiceException {
		try {
			PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
			String configure = getConfigure(condition);
			/* 1. 获取客户列表 */
			List<BusinessUnit> customers = customerDAO.getListByConditionAndOrgId(configure, organization, page, pageSize);
			result.setListOfModel(customers);
			/* 2. 获取客户总数 */
			Long count = customerDAO.getCountByConditionAndOrgId(configure, organization);
			result.setCount(count);
			/* 3. 获取客户自定义类型 */
			for(BusinessUnit busunit : result.getListOfModel()){
				busunit.setDiyType(customerAndProviderTypeService.findByBid(busunit.getId(), 1L, organization)); //1L:客户
			}
			return result;
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]CustomerServiceImpl.getCustomerfilter()-->", dex.getException());
		}
	}
	
	public long countCustomerfilter(String configure) throws ServiceException {
		try {
			String realConfigure = getConfigure(configure);
			configure=configure.replaceAll("e#Provider#e.", " ");
			return customerDAO.count(realConfigure);
		} catch (JPAException jpae) {
			throw new ServiceException("CustomerServiceImpl.countCustomerfilter()-->", jpae.getException());
		}
	}
	
	/**
	 * 按过滤条件拼接where字符串
	 * @param configure 页面过滤条件
	 * @return
	 */
	@Override
	public String getConfigure(String configure) {
		if(configure == null){
			return null;
		}
	    String new_configure = "";
	    String filter[] = configure.split("@@");
	    for(int i=0;i<filter.length;i++){
	    	String filters[] = filter[i].split("@");
	    	try {
	    		if(i==0){
	    			new_configure += " WHERE " + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}else{
	    			new_configure += " AND " + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    return new_configure;
	}
	
	private String splitJointConfigure(String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("e#Provider#e.name",mark,value,true);
		}
		if(field.equals("diyType_name")){
			return FilterUtils.getConditionStr("diyType.name",mark,value,true);
		}
		if(field.equals("note")){
			return FilterUtils.getConditionStr("e#Provider#e.note",mark,value,true);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}

	/**
	 * 删除客户
	 * @param customer
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean remove(BusinessUnit customer,Long organization) throws ServiceException {
		try {
			/* 1. 判断该客户是否被使用 */
			String condition = " WHERE e.customer.id = ?1";
			long count = outOfBillDAO.count(condition, new Object[]{customer.getId()});
			if(count > 0) {
				return false; // 客户已经被使用
			}
			/* 2. 删除客户与联系人的关系 */
			customerToContactinfoService.deleteByCIdAndOrgId(customer.getId(),organization);
			/*List<CustomerToContactinfo> orig_customToContacts = customerToContactinfoService.getListByIdAndType(customer.getId(), 1);
			for(CustomerToContactinfo customToContact : orig_customToContacts){
				customerToContactinfoService.delete(customToContact);
			}*/
			
			/* 3. 删除客户与自定义类型的关系 */
			CustomerAndProviderType ctype = customer.getDiyType();
			long type =(long) (ctype != null ? ctype.getType() : 1);
			businessUnitToTypeService.getDAO().removeByBussidAndType(customer.getId(), organization, type);
			/* 4. 删除客户 */
			BusinessUnit orig_busunit = businessUnitService.findByOrganization(organization);
			orig_busunit.removeCustomer(businessUnitService.findById(customer.getId()));
			update(orig_busunit);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]CustomerServiceImpl.remove()-->", jpae.getException());
		} catch (DaoException dex) {
			throw new ServiceException("[DaoException]CustomerServiceImpl.remove()-->", dex.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]CustomerServiceImpl.remove()-->", sex.getException());
		}
	}

	/**
	 * 判断客户是否被使用
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
			String condition = " WHERE e.customer.id = ?1";
			long countCustomerType = outOfBillDAO.count(condition, new Object[]{businessUnit.getId()});
			if(countCustomerType > 1) {
				return false;
			} else {
				return true;
			}
		} catch (JPAException jpae) {
			throw new ServiceException("CustomerServiceImpl.judgeIsUsed()-->", jpae.getException());
		}
	}
	
	/**
	 * 根据组织机构、客户id，查找数量
	 * @author ZhangHui 2015/4/13
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long count(Long organization, Long businessId) throws ServiceException {
		try {
			return getDAO().count(organization, businessId);
		} catch (DaoException e) {
			throw new ServiceException("CustomerServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 增加一条企业-供应商关系
	 * @author ZhangHui 2015/4/13
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addProviderRelation(BusinessUnit customer, Long providerId) throws ServiceException {
		try {
			long count = getDAO().countProviderRelation(customer.getId(), providerId);
			if(count < 1){
				getDAO().addProviderRelation(customer.getId(), providerId);
			}
			/**
			 *  供应商新增客户信息时，如果选择的客户类型为“产品销往客户”，那么在客户端应主动新增一条供应商类型为“产品来源客户”的供应商
			 *  @author tangxin 2015/6/2
			 */
			CustomerAndProviderType ctype = customer.getDiyType(); // 客户 OR 供应商 类型
			if(customer.getOrganization() != null && ctype != null && ctype.getType().equals(1)) {
				// 只有当供应商选择的客户类型为产品销往客户时，才主动创建 供应商类型
				CustomerAndProviderType orig_type = customerTypeService.getDAO().findByCustomerAndOrganization(customer.getOrganization(), 2, PRODUCT_FROM_CUSTOMER);
				if(orig_type == null){
					// 当前企业下的 产品来源客户 类型不存在 ，新建
					orig_type = new CustomerAndProviderType(null,PRODUCT_FROM_CUSTOMER,2,customer.getOrganization());
					customerTypeService.create(orig_type);
				}
				// 供应商 和 供应商类型 的关系
				BusinessUnitToType orig_btype = businessUnitToTypeService.findByBusIdAndOrgIdAndType(providerId, customer.getOrganization(), 2L);
				if(orig_btype == null){
					// 关系不存在，新增
					businessUnitToTypeService.getDAO().createBusinessUnitTypeBySql(providerId,orig_type.getId(),customer.getOrganization(),2L);
				}else{
					// 关系已存在，更新
					businessUnitToTypeService.getDAO().updateBusinessUnitType(providerId,orig_type.getId(),customer.getOrganization(),2L);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException("CustomerServiceImpl.addProviderRelation()-->" + e.getMessage(), e.getException());
		}
	}
	
	@Override
	public CustomerDAO getDAO() {
		return customerDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PagingSimpleModelVO<BusinessUnit> searchCustomerList(int page,
			int pageSize, String keyword, Long organization) {
		PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
		
		try {
			List<BusinessUnit> customers = customerDAO.searchCustomerList(keyword, organization, page, pageSize);
			List<BusinessUnit> businessUnit =  new ArrayList<BusinessUnit>();
			for (BusinessUnit buvo : customers) {
				CustomerAndProviderType ctype = buvo.getDiyType();
				int type = 1;
				if(ctype != null){
					type = (ctype.getType() != null ? ctype.getType() : 1);
				}
				List<ContactInfo> listContacts = contactInfoService.getListByBusIdAndType(1, 1, buvo.getId(), type, organization);
				if(listContacts!=null&&listContacts.size()>0){
				buvo.setContact(listContacts.get(0).getName());
				buvo.setTelephone(listContacts.get(0).getTel_1());
				buvo.setContacts(listContacts);
			    }
				businessUnit.add(buvo);
			}
			Long count = customerDAO.searchCustomerCount(keyword, organization);
			result.setCount(count);
			result.setListOfModel(businessUnit);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PagingSimpleModelVO<BusinessUnit> sourceOrsoldCustomer(int type ,int page,
			int pageSize, String configure, Long organization) {
		PagingSimpleModelVO<BusinessUnit> result = new PagingSimpleModelVO<BusinessUnit>();
		
		try {
			List<BusinessUnit> customers = customerDAO.sourceOrsoldCustomer(type,configure, organization, page, pageSize);
			List<BusinessUnit> businessUnit =  new ArrayList<BusinessUnit>();
			for (BusinessUnit buvo : customers) {
				CustomerAndProviderType ctype = buvo.getDiyType();
				int _type = 1;
				if(ctype != null){
					_type = (ctype.getType() != null ? ctype.getType() : 1);
				}
				List<ContactInfo> listContacts = contactInfoService.getListByBusIdAndType(1, 1, buvo.getId(), _type, organization);
				if(listContacts!=null&&listContacts.size()>0){
				buvo.setContact(listContacts.get(0).getName());
				buvo.setTelephone(listContacts.get(0).getTel_1());
				buvo.setContacts(listContacts);
			    }
				businessUnit.add(buvo);
			}
			Long count = customerDAO.sourceOrsoldCustomer(type,configure, organization);
			result.setCount(count);
			result.setListOfModel(businessUnit);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
