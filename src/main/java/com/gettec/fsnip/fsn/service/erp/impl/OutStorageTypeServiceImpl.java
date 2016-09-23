package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.OutStorageTypeDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.OutStorageRecordDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.OutStorageTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

@Service("outStorageTypeService")
public class OutStorageTypeServiceImpl extends BaseServiceImpl<OutStorageType, OutStorageTypeDAO> implements OutStorageTypeService{

	@Autowired
	private OutStorageTypeDAO outStorageTypeDAO;
	@Autowired
	private OutStorageRecordDAO outStorageRecordDAO;
	
	/**
	 * 删除出库类型
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
			throw new ServiceException("OutStorageTypeServiceImpl.remove()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	/**
	 * 更新出库类型
	 * @param vo
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateOutStorageType(SimpleModelVO vo, Long organization) throws ServiceException {
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
			OutStorageType orig_type = findById(vo.getId());
			orig_type.setName(vo.getUpdateName());
			update(orig_type);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]OutStorageServiceImpl.updateOutStorageType()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OutStorageServiceImpl.updateOutStorageType()-->" + sex.getMessage(), sex.getException());
		}
	}
	
	public PagingSimpleModelVO<OutStorageType> getOutStorageTypefilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<OutStorageType> result = new PagingSimpleModelVO<OutStorageType>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = outStorageTypeDAO.countOutStorageType(realConfigure);
		result.setCount(count);
		result.setListOfModel(outStorageTypeDAO.getOutStorageTypefilter_(page, pageSize, realConfigure));
		return result;
	}
	
	public long countOutStorageTypefilter(String configure) {
		String realConfigure = getConfigure(configure);
		return outStorageTypeDAO.countOutStorageType(realConfigure);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(field.equals("name")){
			if(mark.equals("eq")){
				return getOutStorageTypeId(outStorageTypeDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getOutStorageTypeId(outStorageTypeDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getOutStorageTypeId(outStorageTypeDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getOutStorageTypeId(outStorageTypeDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getOutStorageTypeId(outStorageTypeDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getOutStorageTypeId(outStorageTypeDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getOutStorageTypeId(List<OutStorageType> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}

	@Override
	public OutStorageTypeDAO getDAO() {
		return outStorageTypeDAO;
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
			String condition = " WHERE e.type.id = ?1";
			long count = outStorageRecordDAO.count(condition, new Object[]{id});
			if(count > 0) {
				return false;
			}
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("OutStorageTypeServiceImpl.judgeIsUsed()-->" + jpae.getMessage(), jpae.getException());
		}
	}

	/**
	 * 新增出库类型
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
			OutStorageType outStorageType = new OutStorageType(vo.getName());
			outStorageType.setOrganization(organization);
			create(outStorageType);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]OutStorageTypeServiceImpl.add()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]OutStorageTypeServiceImpl.add()" + sex.getMessage(), sex.getException());
		}
	}
}
