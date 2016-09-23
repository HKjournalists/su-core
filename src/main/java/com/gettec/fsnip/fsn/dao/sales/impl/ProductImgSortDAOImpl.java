package com.gettec.fsnip.fsn.dao.sales.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.sales.ProductImgSortDAO;
import com.gettec.fsnip.fsn.model.sales.ProductImgSort;

/**
 * 产品相册排序dao层实现
 * @author tangxin 2015/04/24
 *
 */
@Repository(value = "productImgSortDAO")
public class ProductImgSortDAOImpl extends BaseDAOImpl<ProductImgSort>
		implements ProductImgSortDAO {

}
