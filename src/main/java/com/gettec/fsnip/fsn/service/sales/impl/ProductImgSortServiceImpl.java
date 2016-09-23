package com.gettec.fsnip.fsn.service.sales.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.sales.ProductImgSortDAO;
import com.gettec.fsnip.fsn.model.sales.ProductImgSort;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.sales.ProductImgSortService;

/**
 * 产品相册排序service层
 * @author tangxin 2015/04/24
 *
 */
@Service(value = "productImgSortService")
public class ProductImgSortServiceImpl extends
		BaseServiceImpl<ProductImgSort, ProductImgSortDAO> implements
		ProductImgSortService {

	@Autowired private ProductImgSortDAO productImgSortDAO;
	
	@Override
	public ProductImgSortDAO getDAO() {
		return productImgSortDAO;
	}

}
