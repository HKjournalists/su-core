package com.gettec.fsnip.fsn.dao.product;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.SampleProduct;

public interface SampleProductDAO extends BaseDAO<SampleProduct>{

	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public SampleProduct checkSampleProduct(SampleProduct sampleProduct) throws ServiceException;
}