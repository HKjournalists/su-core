package com.gettec.fsnip.fsn.service.erp;

import com.gettec.fsnip.fsn.dao.erp.BusinessTypeDAO;
import com.gettec.fsnip.fsn.model.erp.base.BusinessType;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface BusinessTypeService extends BaseService<BusinessType, BusinessTypeDAO>{

	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<BusinessType> getBusinessTypefilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countBusinessTypefilter(String configure);

}
