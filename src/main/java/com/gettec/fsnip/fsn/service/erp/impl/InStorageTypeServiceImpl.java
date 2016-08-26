package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.InStorageTypeDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.InStorageRecordDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.InStorageType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.InStorageTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

@Service("inStorageTypeService")
public class InStorageTypeServiceImpl extends BaseServiceImpl<InStorageType, InStorageTypeDAO> implements InStorageTypeService{

	@Autowired
	private InStorageTypeDAO inStorageTypeDAO;
	@Autowired
	private InStorageRecordDAO inStorageRecordDAO;
	
	/**
	 * 删除入库类型
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
			throw new ServiceException("InStorageTypeServiceImpl.remove()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 更新入库类型
	 * @param vo
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateInStorageType(SimpleModelVO vo, Long organization) throws ServiceException {
		try {
			/* 1. 判断名称是否唯一 */
			String condition = " WHERE e.name = ?1 AND e.organization = ?2";
			Object[] params = new Object[]{vo.getUpdateName(), organization};
			long count = getDAO().count(condition, params);
			if(count > 0){
				return false;
			}
			if(vo.getId() == null){throw new ServiceException();}
			/* 2. 更新 */
			InStorageType orig_type = findById(vo.getId());
			orig_type.setName(vo.getUpdateName());
			update(orig_type);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]InStorageTypeServiceImpl.updateInStorageType()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]InStorageTypeServiceImpl.updateInStorageType()-->" + sex.getMessage(), sex.getException());
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<InStorageType> getInStorageTypefilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<InStorageType> result = new PagingSimpleModelVO<InStorageType>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = inStorageTypeDAO.countInStorageType(realConfigure);
		result.setCount(count);
		result.setListOfModel(inStorageTypeDAO.getInStorageTypefilter_(page, pageSize, realConfigure));
		return result;
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public long countInStorageTypefilter(String configure) {
		String realConfigure = getConfigure(configure);
		return inStorageTypeDAO.countInStorageType(realConfigure);
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
				// TODO: handle exception
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
				return getInStorageTypeId(inStorageTypeDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getInStorageTypeId(inStorageTypeDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getInStorageTypeId(inStorageTypeDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getInStorageTypeId(inStorageTypeDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getInStorageTypeId(inStorageTypeDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getInStorageTypeId(inStorageTypeDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getInStorageTypeId(List<InStorageType> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}

	@Override
	public InStorageTypeDAO getDAO() {
		return inStorageTypeDAO;
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
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean judgeIsUsed(Long id, Long organization)
			throws ServiceException {
		try {
			String condition = " where e.type.id = ?1";
			long count = inStorageRecordDAO.count(condition, new Object[]{id});
			if(count > 0) {
				return false;
			}
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("InStorageTypeServiceImpl.judgeIsUsed()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 新增入库类型
	 * @param vo
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean add(SimpleModelVO vo, Long organization) throws ServiceException {
		try {
			/* 1. 判断名称是否唯一 */
			String condition = " WHERE e.name = ?1 AND e.organization = ?2";
			Object[] params = new Object[]{vo.getName(), organization};
			long count = getDAO().count(condition, params);
			if(count > 0){
				return false;
			}
			/* 2. 新增 */
			InStorageType inStorageType = new InStorageType(vo.getName());
			inStorageType.setOrganization(organization);
			create(inStorageType);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]InStorageTypeServiceImpl.add()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]InStorageTypeServiceImpl.add()-->" + sex.getMessage(),sex.getException());
		}
	}
}
