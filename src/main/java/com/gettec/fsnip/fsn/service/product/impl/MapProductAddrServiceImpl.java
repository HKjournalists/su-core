package com.gettec.fsnip.fsn.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.product.MapProductAddrDAO;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.model.product.MapProductAddr;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.MapProductAddrService;
import com.gettec.fsnip.fsn.vo.erp.PagingSimpleModelVO;
@Service
public class MapProductAddrServiceImpl extends BaseServiceImpl<MapProductAddr, MapProductAddrDAO> implements MapProductAddrService {
	@Autowired MapProductAddrDAO mapProductAddr;

	@Override
	public MapProductAddrDAO getDAO() {
		return mapProductAddr;
	}

	@Override
	public List<MapProduct> getAllMapProducts() {
		return mapProductAddr.getAllMapProducts();
	}
	
}
