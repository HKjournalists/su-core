package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;
import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.ProductCategory;

public interface MerchandiseCategoryDAO extends BaseDAO<ProductCategory> {

	public List<ProductCategory> getMerchandiseCategoryfilter_(int from,int size,String configure);
	
	public long countMerchandiseCategory(String configure);

	public ProductCategory findByName(String name) throws DaoException;
}
