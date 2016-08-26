package com.gettec.fsnip.fsn.service.market.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.market.MKCategoryInfoDAO;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.MkCategoryInfoService;

@Service(value="MkCategoryInfoService")
public class MkCategoryInfoServiceImpl extends BaseServiceImpl<ProductCategoryInfo, MKCategoryInfoDAO> 
implements  MkCategoryInfoService{
	@Autowired private MKCategoryInfoDAO categoryInfoDAO;
	@Override
	public MKCategoryInfoDAO getDAO() {	
		return categoryInfoDAO;
	}
	
	

}
