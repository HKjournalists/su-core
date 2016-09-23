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
import com.gettec.fsnip.fsn.dao.erp.ProviderTypeDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.ProviderTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

@Service("providerTypeService")
public class ProviderTypeServiceImpl extends BaseServiceImpl<CustomerAndProviderType, ProviderTypeDAO> implements ProviderTypeService {
	@Autowired private ProviderTypeDAO providerTypeDAO;
	@Autowired private BusinessUnitToTypeDAO businessUnitToTypeDAO;
	
	/**
	 * 删除供应商类型
	 * @param id
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Long id, Long organization) throws ServiceException {
		try {
			delete(id);
		} catch (ServiceException sex) {
			throw new ServiceException("ProviderTypeServiceImpl.remove()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 更新供应商类型
	 * @param vo
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateProviderType(SimpleModelVO vo, Long organization) throws ServiceException {
		try {
			/* 1. 判断名称是否唯一 */
			String condition = " WHERE e.name = ?1 AND e.organization = ?2 AND type = 2";
			Object[] params = new Object[]{vo.getUpdateName(), organization};
			long count = getDAO().count(condition, params);
			if(count > 0){
				return false;
			}
			if(vo.getId() == null){throw new ServiceException();}
			/* 2. 更新 */
			CustomerAndProviderType orig_type = findById(vo.getId());
			orig_type.setName(vo.getUpdateName());
			update(orig_type);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]ProviderTypeServiceImpl.updateCustomerType()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]ProviderTypeServiceImpl.updateCustomerType()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<CustomerAndProviderType> getPaging(int page, int size,
			String keywords, Long organization) throws ServiceException {
		PagingSimpleModelVO<CustomerAndProviderType> result = new PagingSimpleModelVO<CustomerAndProviderType>();
		String condition = " where e.organization = " + organization + " and e.type = 2";
		String configure = null;
		if(keywords != null && keywords.trim()!="") {
			condition = condition + " and e.name like '%" + keywords + "%' order by id ASC";
			configure = " and name like '%" + keywords + "%'";
		}else{
			condition = condition + " order by id ASC";
		}
		Long count;
		try {
			count = providerTypeDAO.count(condition);
			result.setCount(count);
			result.setListOfModel(providerTypeDAO.getPaging(page, size, condition));
		} catch (JPAException jpae) {
			throw new ServiceException("ProviderTypeServiceImpl-->getPaging", jpae.getException());
		}
		return result;
	}


	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<CustomerAndProviderType> getAll(Long organization) throws ServiceException {
		PagingSimpleModelVO<CustomerAndProviderType> result = new PagingSimpleModelVO<CustomerAndProviderType>();
		String configure = " WHERE e.organization = " + organization +" and e.type = 2";
		try {
			result.setListOfModel(providerTypeDAO.findAll(organization, configure));
		} catch (JPAException jpae) {
			throw new ServiceException("ProviderTypeServiceImpl-->getAll", jpae.getException());
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<CustomerAndProviderType> getProviderTypefilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<CustomerAndProviderType> result = new PagingSimpleModelVO<CustomerAndProviderType>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization + " and e.type = 2";
		long count = providerTypeDAO.countProviderType(realConfigure);
		result.setCount(count);
		result.setListOfModel(providerTypeDAO.getProviderTypefilter_(page, pageSize, realConfigure));
		return result;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public long countProviderTypefilter(String configure) {
		String realConfigure = getConfigure(configure);
		return providerTypeDAO.countProviderType(realConfigure);
	}
	
	//describe:将请求转换成语句
	//start
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
				e.printStackTrace();
			}
	    }
	    return new_configure;
	}
	
	private String splitJointConfigure (String field,String mark,String value){
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(field.equals("name")){
			if(mark.equals("eq")){
				return getProviderTypeId(providerTypeDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(providerTypeDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(providerTypeDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(providerTypeDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(providerTypeDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(providerTypeDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<CustomerAndProviderType> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}
	//end


	@Override
	public ProviderTypeDAO getDAO() {
		return providerTypeDAO;
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
	public boolean judgeIsUsed(Long id, Long organization)
			throws ServiceException {
		try {
			String condition = " WHERE e.id.typeId = ?1";
			long count = businessUnitToTypeDAO.count(condition, new Object[]{id});
			if(count > 0) {
				return false;
			} else {
				return true;
			}
		} catch (JPAException jpae) {
			throw new ServiceException("ProviderTypeServiceImpl.judgeIsUsed()" + jpae.getMessage(), jpae.getException());
		}
	}
}
