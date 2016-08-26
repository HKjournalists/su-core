package com.gettec.fsnip.fsn.service.product;

import com.gettec.fsnip.fsn.dao.product.SampleProductDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.SampleProduct;
import com.gettec.fsnip.fsn.service.common.BaseService;


public interface SampleProductService extends BaseService<SampleProduct, SampleProductDAO>{
	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public SampleProduct checkSampleProduct(SampleProduct sampleProduct) throws ServiceException;
}