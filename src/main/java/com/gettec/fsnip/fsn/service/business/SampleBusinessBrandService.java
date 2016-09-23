package com.gettec.fsnip.fsn.service.business;

import com.gettec.fsnip.fsn.dao.business.SampleBusinessBrandDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface SampleBusinessBrandService extends BaseService<SampleBusinessBrand, SampleBusinessBrandDAO>{
	
	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public SampleBusinessBrand checkBusinessBrand(SampleBusinessBrand sampleBusinessBrand) throws ServiceException;


}
