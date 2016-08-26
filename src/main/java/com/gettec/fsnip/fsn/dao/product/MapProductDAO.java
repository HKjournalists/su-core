package com.gettec.fsnip.fsn.dao.product;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.product.MapProduct;

public interface MapProductDAO extends BaseDAO<MapProduct> {
	public MapProduct findByProductId(long productId);
}
