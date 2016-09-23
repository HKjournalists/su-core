package com.gettec.fsnip.fsn.service.erp.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.erp.MerchandiseInfoDAO;
import com.gettec.fsnip.fsn.dao.erp.buss.MerchandiseInstanceDAO;
import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.MerchandiseInfoService;
import com.gettec.fsnip.fsn.service.market.MkCategoryInfoService;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

@Service("merchandiseInfoService")
public class MerchandiseInfoServiceImpl extends BaseServiceImpl<Product, MerchandiseInfoDAO> 
		implements MerchandiseInfoService {
	@Autowired private MerchandiseInfoDAO merchandiseInfoDAO;
	@Autowired private MerchandiseInstanceDAO merchandiseInstanceDAO;
	@Autowired private ProductDAO productDAO;
	@Autowired private MkCategoryInfoService categoryInfoService;

	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Product update(Product e) {
		boolean result = false;
		try {
			ProductCategoryInfo productCategory = categoryInfoService.findById(e.getCategory().getId());
			e.setCategory(productCategory);
			String configure = "";
			configure = configure + " , note = '" + e.getNote() + "'";
			configure = configure + " where id = " + e.getId();
			result = merchandiseInfoDAO.updateMerchandiseInfo(configure);
			return e;
		} catch (Exception e2) {
			e2.printStackTrace();
			result = false;
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Product> getAllMerchandiseInfo(Long organization) {
		try {
			String configure = " WHERE e.organization = " + organization+" and e.safeNumber is not NULL and e.type is not NULL and e.firstStorage is not NULL";
			return merchandiseInfoDAO.findAll(organization, configure);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Product findById(String no) {
		try {
			return merchandiseInfoDAO.findById(no);
		} catch (Exception e) {
			return null;
		}
	}
	
	public PagingSimpleModelVO<Product> getMerchandiseInfofilter(int page, int pageSize,String configure, Long organization) {
		PagingSimpleModelVO<Product> result = new PagingSimpleModelVO<Product>();
		String realConfigure = getConfigure(configure);
		realConfigure += " and e.organization = " + organization;
		long count = merchandiseInfoDAO.countMerchandiseInfo(realConfigure);
		result.setCount(count);
		result.setListOfModel(merchandiseInfoDAO.getMerchandiseInfofilter_(page, pageSize, realConfigure));
		return result;
	}
	
	public long countMerchandiseInfofilter(String configure) {
		String realConfigure = getConfigure(configure);
		return merchandiseInfoDAO.countMerchandiseInfo(realConfigure);
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
				return getProviderTypeId(merchandiseInfoDAO.getfilter("eq", value, "name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("neq",value,"name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("startswith",value,"name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("endswith",value,"name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("contains",value,"name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("doesnotcontain",value,"name"));
			}
		}
		if(field.equals("format")){
			if(mark.equals("eq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("eq", value, "format"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("neq",value,"format"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("startswith",value,"format"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("endswith",value,"format"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("contains",value,"format"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("doesnotcontain",value,"format"));
			}
		}
		if(field.equals("firstStorage_name")){
			if(mark.equals("eq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("eq", value, "firstStorage.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("neq",value,"firstStorage.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("startswith",value,"firstStorage.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("endswith",value,"firstStorage.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("contains",value,"firstStorage.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("doesnotcontain",value,"firstStorage.name"));
			}
		}
		if(field.equals("category_name")){
			if(mark.equals("eq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("eq", value, "category.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("neq",value,"category.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("startswith",value,"category.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("endswith",value,"category.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("contains",value,"category.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("doesnotcontain",value,"category.name"));
			}
		}
		if(field.equals("type_name")){
			if(mark.equals("eq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("eq", value, "type.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("neq",value,"type.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("startswith",value,"type.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("endswith",value,"type.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("contains",value,"type.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("doesnotcontain",value,"type.name"));
			}
		}
		if(field.equals("unit_name")){
			if(mark.equals("eq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("eq", value, "unit.name"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("neq",value,"unit.name"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("startswith",value,"unit.name"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("endswith",value,"unit.name"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("contains",value,"unit.name"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("doesnotcontain",value,"unit.name"));
			}
		}
		if(field.equals("barcode")){
			if(mark.equals("eq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("eq", value, "barcode"));
			}else if(mark.equals("neq")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("neq",value,"barcode"));
			}else if(mark.equals("startswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("startswith",value,"barcode"));
			}else if(mark.equals("endswith")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("endswith",value,"barcode"));
			}else if(mark.equals("contains")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("contains",value,"barcode"));
			}else if(mark.equals("doesnotcontain")){
				return getProviderTypeId(merchandiseInfoDAO.getfilter("doesnotcontain",value,"barcode"));
			}
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("e.id",mark,value);
		}
		return null;
	}
	
	private String getProviderTypeId(List<Product> serviceProviderId){
		List<Long> searchId = new ArrayList<Long>();
		for(int i=0;i<serviceProviderId.size();i++){
			searchId.add(new Long(serviceProviderId.get(i).getId()));
		}
		return FilterUtils.FieldConfigure(searchId, "e.id");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean judgeIsUsed(Product product, Long organization)
			throws ServiceException {
		boolean flag = true;
		try {
			Product pro = productDAO.findByBarcode(product.getBarcode());
			String condition = " WHERE e.initializeProduct.product.id = ?1";
			long countInstance = merchandiseInstanceDAO.count(condition, new Object[]{pro.getId()});
			if(countInstance <= 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public MerchandiseInfoDAO getDAO() {
		return merchandiseInfoDAO;
	}
}
