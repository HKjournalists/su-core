package com.gettec.fsnip.fsn.service.product;

import com.gettec.fsnip.fsn.dao.product.ProductLogDAO;
import com.gettec.fsnip.fsn.model.product.ProductLog;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * ProductLogService customized operation implementation
 * 
 * @author LongXianZhen 2015/06/03
 */
public interface ProductLogService extends BaseService<ProductLog, ProductLogDAO>{

	void saveProductLog(ProductLog data);

	
	
}