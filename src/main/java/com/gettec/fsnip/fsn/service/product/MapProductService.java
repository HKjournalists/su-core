package com.gettec.fsnip.fsn.service.product;

import com.gettec.fsnip.fsn.dao.product.MapProductDAO;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface MapProductService extends BaseService<MapProduct,MapProductDAO> {
	public MapProduct findByProductId(long productId);
	boolean isProductExist(Long id,long productId);
}
