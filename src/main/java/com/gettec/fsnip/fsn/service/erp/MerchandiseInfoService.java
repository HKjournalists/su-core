package com.gettec.fsnip.fsn.service.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.erp.MerchandiseInfoDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface MerchandiseInfoService extends BaseService<Product, MerchandiseInfoDAO> {

	public Product findById(String no);
	
	/**
	 * 过滤
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param organization
	 * @return
	 */
	public PagingSimpleModelVO<Product> getMerchandiseInfofilter(int page, int pageSize,String configure, Long organization);
	
	/**
	 * 过滤
	 * @param configure
	 * @return
	 */
	public long countMerchandiseInfofilter(String configure);
	
	/*功能：判断商品是否被使用
	 * author：cgw
	 * date：2014-10-31*/
	public boolean judgeIsUsed(Product product,Long organization) throws ServiceException;

	List<Product> getAllMerchandiseInfo(Long organization);
}
