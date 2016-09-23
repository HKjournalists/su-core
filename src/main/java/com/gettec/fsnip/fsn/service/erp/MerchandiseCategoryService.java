package com.gettec.fsnip.fsn.service.erp;


import com.gettec.fsnip.fsn.dao.erp.MerchandiseCategoryDAO;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface MerchandiseCategoryService extends BaseService<ProductCategory, MerchandiseCategoryDAO>{
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<ProductCategory> getMerchandiseCategoryfilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countMerchandiseCategoryfilter(String configure);
}
