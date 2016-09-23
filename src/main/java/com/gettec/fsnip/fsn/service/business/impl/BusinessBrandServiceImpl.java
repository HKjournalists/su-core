package com.gettec.fsnip.fsn.service.business.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.gettec.fsnip.fsn.dao.business.BusinessBrandDAO;
import com.gettec.fsnip.fsn.dao.business.BusinessUnitDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.service.business.BrandCategoryService;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.transfer.BusinessBrandTransfer;
import com.gettec.fsnip.fsn.transfer.BusinessUnitTransfer;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;

/**
 * BusinessBrand service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="businessBrandService")
public class BusinessBrandServiceImpl extends BaseServiceImpl<BusinessBrand, BusinessBrandDAO> 
		implements BusinessBrandService{

	@Autowired private BusinessBrandDAO businessBrandDAO;
	@Autowired private ResourceService testResourceService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private BusinessUnitDAO businessUnitDAO;
	@Autowired private BrandCategoryService brandCategoryService;
	@Autowired private ProductService productService;

	@Override
	public BusinessBrandDAO getDAO() {
		return businessBrandDAO;
	}
	
	/**
	 * 新增商标
	 * @param businessBrand
	 * @param info
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createBusinessBrand(BusinessBrand businessBrand, AuthenticateInfo info)
			throws ServiceException {
		try {
			/* 1. 处理新增图片 */
			testResourceService.saveBusinessBrandResources(businessBrand);
			/* 2. 新增商标信息 */
			businessBrand.setOrganization(info.getOrganization());
			businessBrand.setLastModifyTime(new Date());
			businessBrand.setBusinessUnit(businessUnitService.findByInfo(info, false, false));
			create(businessBrand);
		} catch (ServiceException sex) {
			throw sex;
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateBusinessBrand(BusinessBrand businessBrand)
			throws ServiceException {
		/* 1. 处理新增图片 */
		testResourceService.saveBusinessBrandResources(businessBrand);
		/* 2. 更新商标信息 */
		BusinessBrand orig_brand = findById(businessBrand.getId());
		orig_brand.setLogAttachments(businessBrand.getLogAttachments());
		orig_brand.setLastModifyTime(new Date());
		orig_brand.setRegistrationDate(businessBrand.getRegistrationDate());
		orig_brand.setName(businessBrand.getName());
		orig_brand.setBrandCategory(brandCategoryService.findById(businessBrand.getBrandCategory().getId()));
		update(orig_brand);
	}

	/**
	 * 按名称查找一条商标信息
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BusinessBrand findByName(String name) {
		return businessBrandDAO.findByName(name);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public BusinessBrand findById(Object id){
		try {
			return  businessBrandDAO.findById(id);
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 按组织机构Id获取商标列表
	 * @throws ServiceException 
	 */
	@Override
	public List<BusinessBrand> getListOfBrandByOrgnizationIdWithPage(Long organizationId, String configure,
			int page, int pageSize) throws ServiceException {
		try {
			Map<String,Object> conMap=getConfigure(organizationId, configure);
			List<BusinessBrand> businessBrandList= businessBrandDAO.getListOfBrandByConditionWithPage(conMap, page, pageSize);
			BusinessBrandTransfer.transfer(businessBrandList);
			return businessBrandList;
		} catch (DaoException dex) {
			throw new ServiceException("countByOrgnizationId.getListOfBrandByOrgnizationIdWithPage()->"+dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 按组织机构Id获取当前登录企业的商标总数
	 * @throws ServiceException 
	 */
	@Override
	public long countByOrgnizationId(Long organizationId, String configure) throws ServiceException {
		try {
			Map<String,Object> cofMap=this.getConfigure(organizationId, configure);
			return businessBrandDAO.countBrandByCondition(cofMap);
		} catch (DaoException dex) {
			throw new ServiceException("BusinessBrandServiceImpl.countByOrgnizationId()->"+dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 按组织机构id获取所有的商标信息
	 * @throws ServiceException 
	 */
	@Override
	public List<BusinessBrand> getListOfBrandByOrganization(Long organization) throws ServiceException {
		try {
			return businessBrandDAO.getListByOrganization(organization);
		} catch (DaoException dex) {
			throw new ServiceException("BusinessBrandServiceImpl.getListOfBrandByOrganization()->"+dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 保存商标信息
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(List<BusinessBrand> brands, Long busunitId) throws ServiceException {
		try {
			if(brands == null || brands.size()<1){ return; }
			/* 1. 从数据库中删除已经在页面删除的记录  */
			List<BusinessBrand> nowBrands = brands;
			List<BusinessBrand> origBrands = businessBrandDAO.getListByBusunitId(busunitId);
			List<BusinessBrand> removes = getRemoves(origBrands, nowBrands);
			if(!CollectionUtils.isEmpty(removes)){
				for(BusinessBrand brand : removes){
					businessBrandDAO.remove(businessBrandDAO.findById(brand.getId()));
				}
			}
			for(BusinessBrand brand : nowBrands){
				if(brand.getId() == null){
					/* 2. 从数据库中新增在页面新添加的记录  */
					if(brand.getName().length()>1){
						brand.setBusinessUnit(businessUnitDAO.findById(busunitId));
						businessBrandDAO.persistent(brand);
					}
				}else{
					/* 3. 从数据库中更新在页面更改的记录  */
					BusinessBrand orig_brand = businessBrandDAO.findById(brand.getId());
					orig_brand.setName(brand.getName());
					businessBrandDAO.merge(orig_brand);
				}
			}
		} catch (Exception e) {
			throw new ServiceException("BusinessBrandServiceImpl.save()->"+e.getMessage(), e);
		}
	}
	
	/**
	 * 对比前后的品牌列表，获取删除的集合
	 */
	private List<BusinessBrand> getRemoves(List<BusinessBrand> origBrands, List<BusinessBrand> nowBrands) {
		List<BusinessBrand> removes = new ArrayList<BusinessBrand>();
		List<Long> currentId = new ArrayList<Long>();
		
		for (BusinessBrand brand : nowBrands) {
			Long id = brand.getId();
			if (id != null) {
				currentId.add(id);
			}
		}
		for (BusinessBrand brand : origBrands) {
			if (brand.getId()!=null && !currentId.contains(brand.getId())) {
				removes.add(brand);
			}
		}
		return removes;
	}
	
	/**
	 * 验证当前品牌是否已经存在,true表示不存在，false表示存在
	 * @throws ServiceException
	 */
	@Override
	public boolean validateBrandName(BusinessBrand businessBrand) throws ServiceException {
		try{
			/* 1. 确定品牌类型级别 */
			int level = 1;
			String categoryName = businessBrand.getBrandCategory().getName();
			String parentCategoryName = null;
			if(categoryName.contains(".")){
				level = 2;
				String[] array = categoryName.replace(".", "_").split("_");
				parentCategoryName = array[0];
			}
			/* 1. 编辑状态 
			if(businessBrand != null && businessBrand.getId() != null){
				BusinessBrand orig_brand = findById(businessBrand.getId());
				if(orig_brand.getName().equals(businessBrand.getName())){
					return true;
				}
			}*/
			/* 2. 新增状态 */
			long count = getDAO().countbyNameAndCategoryId(businessBrand.getId(), businessBrand.getName(), businessBrand.getBrandCategory().getId());
			if(count > 0){
				return false;
			}else if(level == 1){
				return true;
			}
			count = getDAO().countByNameAndCategoryName(businessBrand.getName(), parentCategoryName, businessBrand.getId());
			return count>0?false:true;
		}catch(DaoException dep){
			throw new ServiceException("BusinessBrandServiceImpl.validateBrandName()->"+dep.getMessage(),dep.getException());
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
	 * 获取所有商标名称
	 */
	@Override
	public List<String> getAllBrandName()throws ServiceException{
		try{
			return businessBrandDAO.getAllBrandName();
		}catch(DaoException daoe){
			throw new ServiceException("BusinessBrandServiceImpl.getAllBrandName()->"+daoe.getMessage(),daoe.getMessage());
		}
	}
	
	/**
	 * 分割页面的过滤信息
	 * @param field
	 * @param mark
	 * @param value
	 * @return
	 * @throws ServiceException
	 */
	private String splitJointConfigure(String field,String mark,String value) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("id",mark,value);
		}
		if(field.equals("serviceOrder")){
			return FilterUtils.getConditionStr("serviceOrder",mark,value,true);
		}
		if(field.equals("sample_productionDate")){
			return FilterUtils.getConditionTime("sample.productionDate",mark,value);
		}
		if(field.equals("sample_batchSerialNo")){
			return FilterUtils.getConditionStr("sample.batchSerialNo",mark,value,true);
		}
		if(field.equals("testOrgnization")){
			return FilterUtils.getConditionStr("testOrgnization",mark,value,true);
		}
		if(field.equals("lastModifyUserName")){
			return FilterUtils.getConditionStr("lastModifyUserName",mark,value,true);
		}
		if(field.equals("backResult")){
			return FilterUtils.getConditionStr("backResult",mark,value,true);
		}
		if(field.equals("tips")){
			return FilterUtils.getConditionStr("tips",mark,value,true);
		}
		if(field.equals("sample_product.name")){
			return FilterUtils.getConditionStr("sample.product.name",mark,value);
		}
		if(field.equals("mkPublishFlag")){
			if(value.equals("发布")){
				value = "1";
			}else if(value.equals("未发布")){
				value = "0";
			}
			return FilterUtils.getConditionStr("mkPublishFlag",mark,value);
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("name",mark,value);
		}
		if(field.equals("businessUnit_name")){
			return FilterUtils.getConditionStr("e.businessUnit.name",mark,value);
		}
		if(field.equals("registrationDate")){
			return FilterUtils.getConditionStr("registrationDate",mark,value);
		}
		return null;
	}

	/**
	 * 根据品牌名称和组织机构判断条数是否为1
	 * @param brandName
	 * @param organization
	 * @return boolean
	 * @throws ServiceException 
	 */
	@Override
	public boolean validateBrandName(String brandName, Long organization) throws ServiceException {
		try {
			long count = getDAO().countByNameAndOrgId(brandName, organization);
			return count==1?true:false;
		} catch (DaoException dex) {
			throw new ServiceException("BusinessBrandServiceImpl.validateBrandName()-->" + dex.getMessage() , dex.getException());
		}
	}

	/**
	 * 根据品牌名称和类型id查找一条品牌信息
	 * @param brandName
	 * @param categoryId
	 * @return BusinessBrand
	 * @throws ServiceException
	 */
	@Override
	public BusinessBrand findByNameAndCategoryId(String brandName, Long categoryId) throws ServiceException {
		try {
			return getDAO().findByNameAndCategoryId(brandName, categoryId);
		} catch (DaoException dex) {
			throw new ServiceException("BusinessBrandServiceImpl.findByNameAndCategoryId()-->" + dex.getMessage() , dex.getException());
		}
	}

	/**
	 * 根据品牌名称和类型id查找一条品牌信息
	 * @param bussunitId
	 * @param organization
	 * @return BusinessBrand
	 * @throws ServiceException
	 */
	@Override
	public BusinessBrand findByNameAndBussunitId(String brandName, Long bussunitId) throws ServiceException {
		try {
			return getDAO().findByNameAndBussunitId(brandName, bussunitId);
		} catch (DaoException dex) {
			throw new ServiceException("BusinessBrandServiceImpl.findByNameAndBussunitId()-->" + dex.getMessage() , dex.getException());
		}
	}
	
	/**
	 * 功能描述：保存产品的品牌信息
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BusinessBrand saveBrand(BusinessBrand brand, BusinessUnit orig_producer, Long organization) 
			throws ServiceException {
		
		try {
			if(organization==null || brand.getName()==null || orig_producer.getId()==null){
				throw new Exception("参数为空");
			}
			
			// 如果品牌为空，则赋予默认值
			if("".equals(brand.getName().replace(" ", ""))){
				brand.setName("--");
			}
			
			BusinessBrand orig_brand = null;
			if(brand.getId() != null){
				orig_brand = findById(brand.getId());
			}else{
				orig_brand = findByNameAndCategoryId(brand.getName(), null);
				if(orig_brand == null){
					brand.setId(null);
					brand.setBusinessUnit(orig_producer);
					create(brand);
				}
			}
			
			return orig_brand;
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]ProductServiceImpl.saveBrand()-->" + sex.getMessage(), sex.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductServiceImpl.saveBrand()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 保存产品的商标(报告录入界面)
	 * @param sample
	 * @param isLocal
	 * @param organization 
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveBrand(ProductInstance sample, Long organization) throws ServiceException {
		try {
			Product product = sample.getProduct();
			BusinessBrand brand = product.getBusinessBrand();
			if(brand.getId() != null){
				return;
			}
			if(brand.getName().replace(" ", "").equals("")){
				brand.setName("--");
			}
			BusinessBrand orig_brand = null;
			if(!product.isLocal() || brand.getId() == null){
				/* 1. 流通企业 */
				orig_brand = findByNameAndCategoryId(brand.getName(), null);
				if(orig_brand == null){
					brand.setId(null);
					brand.setBusinessUnit(sample.getProducer());
					create(brand);
					orig_brand = brand;
				}
			}else{
				/* 2.2 生产企业 */
				Long brandId = brand.getId();
				if(brand.getId() == null){
					brandId = productService.findByBarcodeAndBrandNameAndBusunitId(product.getBarcode(),
							brand.getName(), sample.getProducer().getId());
				}
				if(brandId == null){
					throw new Exception("按barcode、brandName、bussunitId 无法找到一条品牌！");
				}
				orig_brand = findById(brandId);
			}
			product.setBusinessBrand(orig_brand);
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]ProductServiceImpl.saveBrand()报告录入界面-->" + sex.getMessage(), sex.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductServiceImpl.saveBrand()报告录入界面-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 功能描述：保存商标信息<br>
	 * 作用于保存泊银等其他外部系统的样品数据。
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveBusinessBrand(ProductInstance sample, Long organization) throws ServiceException {
		try {
			if(sample == null){ return; }
			Product product = sample.getProduct();
			if(product == null){ return; }
			BusinessBrand brand = product.getBusinessBrand();
			if(brand == null){ return; }
			if(brand.getId() != null){return;}
			
			/**
			 * 如果品牌为空，给一个默认值
			 */
			String brandName = brand.getName().replace(" ", "");
			if("".equals(brandName)){
				brand.setName("--");
			}
			BusinessBrand orig_brand = findByNameAndCategoryId(brand.getName(), null);
			if(orig_brand == null){
				brand.setId(null);
				brand.setBusinessUnit(sample.getProducer());
				create(brand);
				orig_brand = brand;
			}
			product.setBusinessBrand(orig_brand);
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]ProductServiceImpl.saveBusinessBrand()-->" + e.getMessage(), e.getException());
		}
	}

	@Override
	@Transactional
	public BusinessBrand checkBusinessBrand(BusinessBrand businessBrand)
			throws ServiceException {
		return businessBrandDAO.checkBusinessBrand(businessBrand);
	}
}