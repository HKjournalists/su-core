package com.gettec.fsnip.fsn.dao.erp;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.product.Product;


public interface MerchandiseInfoDAO extends BaseDAO<Product> {

	public List<Product> getMerchandiseInfofilter_(int from,int size,String configure);
	public long countMerchandiseInfo(String configure);
	
	public boolean updateMerchandiseInfo(String configure);
}
