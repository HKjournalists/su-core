package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.erp.MerchandiseCategoryDAO;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.MerchandiseCategoryService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("merchandiseCategoryService")
public class MerchandiseCategoryServiceImpl extends BaseServiceImpl<ProductCategory, MerchandiseCategoryDAO> 
		implements MerchandiseCategoryService {
	@Autowired private MerchandiseCategoryDAO merchandiseCategoryDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PagingSimpleModelVO<ProductCategory> getPaging(int page,
			int size, String keywords, Long organization) {
		try {
			PagingSimpleModelVO<ProductCategory> result = new PagingSimpleModelVO<ProductCategory>();
			String condition = "";
			if(keywords != null && keywords.trim()!="") {
				condition = " where e.name like '%" + keywords + "%'  order by id ASC";
			}
			condition += " order by id ASC";
			Long count = merchandiseCategoryDAO.count(condition);
			result.setCount(count);
			result.setListOfModel(merchandiseCategoryDAO.getPaging(page, size, condition));
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PagingSimpleModelVO<ProductCategory> getAll(Long organization) {
		try {
			PagingSimpleModelVO<ProductCategory> result = new PagingSimpleModelVO<ProductCategory>();
			result.setListOfModel(merchandiseCategoryDAO.findAll(organization, null));
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public PagingSimpleModelVO<ProductCategory> getMerchandiseCategoryfilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<ProductCategory> result = new PagingSimpleModelVO<ProductCategory>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = merchandiseCategoryDAO.countMerchandiseCategory(realConfigure);
		result.setCount(count);
		result.setListOfModel(merchandiseCategoryDAO.getMerchandiseCategoryfilter_(page, pageSize, realConfigure));
		return result;
	}
	
	public long countMerchandiseCategoryfilter(String configure) {
		String realConfigure = getConfigure(configure);
		return merchandiseCategoryDAO.countMerchandiseCategory(realConfigure);
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
				return getOutStorageTypeId(merchandiseCategoryDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getOutStorageTypeId(merchandiseCategoryDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getOutStorageTypeId(merchandiseCategoryDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getOutStorageTypeId(merchandiseCategoryDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getOutStorageTypeId(merchandiseCategoryDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getOutStorageTypeId(merchandiseCategoryDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getOutStorageTypeId(List<ProductCategory> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}

	@Override
	public MerchandiseCategoryDAO getDAO() {
		// TODO Auto-generated method stub
		return null;
	}
}
