package com.gettec.fsnip.fsn.dao.product.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductLogDAO;
import com.gettec.fsnip.fsn.model.product.ProductLog;

/**
 * ProductLogDAOImpl customized operation implementation
 * 
 * @author LongXianZhen 2015/06/03
 */
@Repository(value="productLogDAO")
public class ProductLogDAOImpl extends BaseDAOImpl<ProductLog>
		implements ProductLogDAO {
		
	
}