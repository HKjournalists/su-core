package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.InitializeProductDAO;
import com.gettec.fsnip.fsn.dao.erp.MerchandiseTypeDAO;
import com.gettec.fsnip.fsn.dao.erp.OutGoodsInfoDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.MerchandiseTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
import com.gettec.fsnip.fsn.vo.erp.SimpleModelVO;

@Service("merchandiseTypeService")
public class MerchandiseTypeServiceImpl extends BaseServiceImpl<MerchandiseType, MerchandiseTypeDAO> 
		implements MerchandiseTypeService {

	@Autowired
	private MerchandiseTypeDAO merchandiseTypeDAO;
	@Autowired
	private OutGoodsInfoDAO outGoodsInfoDAO;
	@Autowired
	private InitializeProductDAO initializeProductDAO;
	
	/**
	 * 删除商品类型
	 * @param name  
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
			throw new ServiceException("MerchandiseTypeServiceImpl.remove()-->" + sex.getMessage(), sex.getException());
		}
	}

	public PagingSimpleModelVO<MerchandiseType> getMerchandiseTypefilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<MerchandiseType> result = new PagingSimpleModelVO<MerchandiseType>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = merchandiseTypeDAO.countMerchandiseType(realConfigure);
		result.setCount(count);
		result.setListOfModel(merchandiseTypeDAO.getMerchandiseTypefilter_(page, pageSize, realConfigure));
		return result;
	}
	
	public long countMerchandiseTypefilter(String configure) {
		String realConfigure = getConfigure(configure);
		return merchandiseTypeDAO.countMerchandiseType(realConfigure);
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
				e.printStackTrace();
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
				return getOutStorageTypeId(merchandiseTypeDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getOutStorageTypeId(merchandiseTypeDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getOutStorageTypeId(merchandiseTypeDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getOutStorageTypeId(merchandiseTypeDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getOutStorageTypeId(merchandiseTypeDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getOutStorageTypeId(merchandiseTypeDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getOutStorageTypeId(List<MerchandiseType> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}

	@Override
	public MerchandiseTypeDAO getDAO() {
		return merchandiseTypeDAO;
	}

	/**
	 * 判断产品是否被使用
	 * @param name
	 * @param organization
	 * @return
	 */
	@Override
	public boolean judgeIsUsed(Long id, Long organization)
			throws ServiceException {
		try {
			String condition = " WHERE e.type.id = ?1 ";
			Object[] params = new Object[]{id};
			long count = initializeProductDAO.count(condition, params);
			if(count > 0){
				return false;
			}
			count = outGoodsInfoDAO.count(condition, params); // 出库
			if(count > 0 ){
				return false;
			}
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("MerchandiseTypeServiceImpl.judgeIsUsed()" + jpae.getMessage(), jpae.getException());
		}	
	}

	/**
	 * 更新产品类型
	 * @param vo
	 * @param organization
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateMerchandiseType(SimpleModelVO vo, Long organization) throws ServiceException {
		try {
			/* 1. 判断名字是否已经存在 */
			String condition = " WHERE e.name = ?1 AND e.organization = ?2";
			Object[] params = new Object[]{vo.getUpdateName(), organization};
			long count = getDAO().count(condition, params);
			if(count > 0){
				return false;
			}
			if(vo.getId()==null){throw new ServiceException();}
			/* 2. 更新 */
			MerchandiseType orig_type = findById(vo.getId());
			orig_type.setName(vo.getUpdateName());
			update(orig_type);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]MerchandiseTypeServiceImpl.updateMerchandiseType()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]MerchandiseTypeServiceImpl.updateMerchandiseType()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 新增商品类型
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
			MerchandiseType merchandiseType = new MerchandiseType(vo.getName());
			merchandiseType.setOrganization(organization);
			create(merchandiseType);
			return true;
		} catch (JPAException jpae) {
			throw new ServiceException("[JPAException]MerchandiseTypeServiceImpl.add()-->" + jpae.getMessage(), jpae.getException());
		} catch (ServiceException sex) {
			throw new ServiceException("[ServiceException]MerchandiseTypeServiceImpl.add()-->" + sex.getMessage(), sex.getException());
		}
	}
}
