package com.gettec.fsnip.fsn.service.erp.impl;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gettec.fsnip.fsn.dao.erp.BusinessTypeDAO;
import com.gettec.fsnip.fsn.model.erp.base.BusinessType;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.BusinessTypeService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("businessTypeService")
public class BusinessTypeServiceImpl extends BaseServiceImpl<BusinessType, BusinessTypeDAO> implements BusinessTypeService{

	@Autowired
	private BusinessTypeDAO businessTypeDAO;

	public PagingSimpleModelVO<BusinessType> getBusinessTypefilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<BusinessType> result = new PagingSimpleModelVO<BusinessType>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = businessTypeDAO.countBusinessType(realConfigure);
		result.setCount(count);
		result.setListOfModel(businessTypeDAO.getBusinessTypefilter_(page, pageSize, realConfigure));
		return result;
	}
	
	public long countBusinessTypefilter(String configure) {
		String realConfigure = getConfigure(configure);
		return businessTypeDAO.countBusinessType(realConfigure);
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
				return getProviderTypeId(businessTypeDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(businessTypeDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(businessTypeDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(businessTypeDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(businessTypeDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(businessTypeDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<BusinessType> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId,"e.id");
	}

	@Override
	public BusinessTypeDAO getDAO() {
		return businessTypeDAO;
	}
}
