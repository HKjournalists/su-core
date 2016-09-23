package com.gettec.fsnip.fsn.dao.product.impl;

import org.springframework.stereotype.Repository;

import com.gettec.fsnip.fsn.dao.common.impl.BaseDAOImpl;
import com.gettec.fsnip.fsn.dao.product.ProductAreaDAO;
import com.gettec.fsnip.fsn.model.product.ProductArea;

/**
 * 有关产品所属区域的查询操作，基础数据，没有增、删、改操作
 * @author TangXin
 *
 */
@Repository(value="productAreaDAO")
public class ProductAreaDAOImpl extends BaseDAOImpl<ProductArea>
	implements ProductAreaDAO{

}
