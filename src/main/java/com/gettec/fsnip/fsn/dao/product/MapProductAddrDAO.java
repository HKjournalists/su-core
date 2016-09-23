package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.model.product.MapProductAddr;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface MapProductAddrDAO extends BaseDAO<MapProductAddr> {

	List<MapProduct> getAllMapProducts();

}
