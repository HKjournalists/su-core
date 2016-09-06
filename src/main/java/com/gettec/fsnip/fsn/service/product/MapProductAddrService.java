package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.MapProductAddrDAO;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.model.product.MapProductAddr;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;

public interface MapProductAddrService extends BaseService<MapProductAddr, MapProductAddrDAO> {

	List<MapProduct> getAllMapProducts();

}
