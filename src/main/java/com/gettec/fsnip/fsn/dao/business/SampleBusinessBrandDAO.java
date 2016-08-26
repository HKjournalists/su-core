package com.gettec.fsnip.fsn.dao.business;

import com.gettec.fsnip.fsn.dao.common.ModelDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;

public interface SampleBusinessBrandDAO extends ModelDAO<SampleBusinessBrand>{

	/**
	 * 根据相应属性验证对象是否存在
	 * @param businessBrand
	 * @return
	 */
	public SampleBusinessBrand checkSampleBusinessBrand(SampleBusinessBrand sampleBusinessBrand) throws ServiceException;
}
