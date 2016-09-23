package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.product.ProductDestroyRecordDAO;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProductDestroyRecordService extends BaseService<ProductDestroyRecord, ProductDestroyRecordDAO> {
	//Set<Resource> addResource(ProductDestroyRecord productDestroyRecord);
	List<ProductDestroyRecord> getbyOrgId(String orgname,int page,int pageSize,String keyword);
//	List<ProductDestroyRecord> getbyOrgIdandKeyword(String orgname,int page,int pageSize);
	long countbyOrg(String orgname,String keyword);
}
