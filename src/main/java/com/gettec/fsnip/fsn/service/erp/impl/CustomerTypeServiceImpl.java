package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.BusinessUnitToTypeDAO;
import com.gettec.fsnip.fsn.dao.erp.CustomerTypeDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.CustomerTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

@Service("customerTypeService")
public class CustomerTypeServiceImpl extends BaseServiceImpl<CustomerAndProviderType, CustomerTypeDAO> 
		implements CustomerTypeService {
	@Autowired private CustomerTypeDAO customerTypeDAO;
	@Autowired private BusinessUnitToTypeDAO businessUnitToTypeDAO;

	/**
	 * 删除客户类型
	 * @param id
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Long id, Long organization) throws ServiceException {
		try {
			delete(id);
		} catch (ServiceException sex) {
			throw new ServiceException("CustomerTypeServiceImpl.remove()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 更新客户类型
	 * @param vo
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateCustomerType(SimpleModelVO vo, Long organization) throws ServiceException {
		try {
			/* 1. 判断名称是否唯一 */
			String condition = " WHERE e.name = ?1 AND e.organization = ?2 AND type = 1";
			Object[] params = new Object[]{vo.getUpdateName(), organization};
			long count = getDAO().count(condition, params);
			if(count > 0){
				return false;
			}
			if(vo.getId() == null){throw new ServiceException();}
			/* 2. 更新 */
			CustomerAndProviderType orig_type = getDAO().findById(vo.getId());
			orig_type.setName(vo.getUpdateName());
			update(orig_type);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]CustomerTypeServiceImpl.updateCustomerType()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]CustomerTypeServiceImpl.updateCustomerType()-->" + sex.getMessage(), sex.getException());
		}
	}


	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<CustomerAndProviderType> getPaging(int page, int size,
			String keywords, Long organization) throws ServiceException {
		PagingSimpleModelVO<CustomerAndProviderType> result = new PagingSimpleModelVO<CustomerAndProviderType>();
		String condition = " where e.organization= " + organization + " and e.type = 1";
		String configure = null;
		if(keywords != null && keywords.trim()!="") {
			condition = condition + " and e.name like '%" + keywords + "%' order by id ASC";
			configure = " and name like '%" + keywords + "%'";
		}else{
			condition = condition + " order by id ASC";
		}
		Long count;
		try {
			count = customerTypeDAO.count(condition);
			result.setCount(count);
			result.setListOfModel(customerTypeDAO.getPaging(page, size, condition));
		} catch (JPAException jpae) {
			throw new ServiceException("CustomerTypeServiceImpl-->getPaging", jpae.getException());
		}
		
		return result;
	}


	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<CustomerAndProviderType> getAll(Long organization) throws ServiceException {
		PagingSimpleModelVO<CustomerAndProviderType> result = new PagingSimpleModelVO<CustomerAndProviderType>();
		String configure = " WHERE e.organization = " + organization +"and e.type = 1";
		try {
			result.setListOfModel(customerTypeDAO.findAll(organization, configure));
		} catch (JPAException jpae) {
			throw new ServiceException("CustomerTypeServiceImpl-->getAll", jpae.getException());
		}
		return result;
	}

	/*begin filter cgw
	 * describe:客户类型的筛选，getCustomerTypefilter()的impl
	 * @param page int
	 * @param pageSize int 
	 * @param configure String */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<CustomerAndProviderType> getCustomerTypefilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<CustomerAndProviderType> result = new PagingSimpleModelVO<CustomerAndProviderType>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization + " and e.type = 1";
		long count = customerTypeDAO.countCtype(realConfigure);
		result.setCount(count);
		result.setListOfModel(customerTypeDAO.getCustomerTypefilter_(page, pageSize, realConfigure));
		return result;
	}
	//describe:将请求转换成语句
	private String getConfigure(String configure) {
		if(configure == null){
			return null;
		}
	    String new_configure = "where ";
	    String filter[] = configure.split("@@");
	    for(int i=0;i<filter.length;i++){
	    	String filters[] = filter[i].split("@");
	    	try {
	    		if(i==0){
	    			new_configure = new_configure + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}else{
	    			new_configure = new_configure +" AND " + splitJointConfigure(filters[0],filters[1],filters[2]);
	    		}
			} catch (Exception e) {
			}
	    }
	    return new_configure;
	}
	private String splitJointConfigure (String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(field.equals("name")){
			if(mark.equals("eq")){
				return getCustomerTypeId(customerTypeDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getCustomerTypeId(customerTypeDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getCustomerTypeId(customerTypeDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getCustomerTypeId(customerTypeDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getCustomerTypeId(customerTypeDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getCustomerTypeId(customerTypeDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}


	
	@Override
	public long countCustomerTypefilter(String configure) {
		String realConfigure = getConfigure(configure);
		return customerTypeDAO.countCtype(realConfigure);
	}
	
	private String getCustomerTypeId(List<CustomerAndProviderType> serviceCustomerId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceCustomerId.size();i++){
			searchId.add(new Long(serviceCustomerId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}

	@Override
	public CustomerTypeDAO getDAO() {
		return customerTypeDAO;
	}

	/**
	 * 判断是否被使用
	 * @param vo
	 * @param organization
	 * @param model
	 * @return
	 *    true:  没有被使用
	 *    false: 被使用
	 */
	@Override
	public boolean judgeIsUsed(Long id, Long organization) throws ServiceException{
		try {
			String condition = " WHERE e.id.typeId = ?1";
			long count = businessUnitToTypeDAO.count(condition, new Object[]{id});
			if(count > 0) {
				return false;
			}
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("CustomerTypeServiceImpl.judgeIsUsed()-->" + jpae.getMessage(),jpae.getException());
		}
	}

	/**
	 * 新增客户类型
	 * @param vo
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean add(SimpleModelVO vo, Long organization, int type) throws ServiceException {
		try {
			/* 1. 判断名称是否唯一 */
			String condition = " WHERE e.name = ?1 AND e.organization = ?2 AND type = ?3";
			Object[] params = new Object[]{vo.getName(), organization, type};
			long count = getDAO().count(condition, params);
			if(count > 0){
				return false;
			}
			/* 2. 新增 */
			CustomerAndProviderType customerType = new CustomerAndProviderType(vo.getName());
			customerType.setOrganization(organization); 
			customerType.setType(type);
			create(customerType);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]CustomerTypeServiceImpl.add()-->" + jpae.getMessage(),jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]CustomerTypeServiceImpl.add()-->" + sex.getMessage(), sex.getException());
		}
	}
}
