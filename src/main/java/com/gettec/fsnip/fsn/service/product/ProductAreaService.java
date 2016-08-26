package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.ProductAreaDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductArea;
import com.gettec.fsnip.fsn.service.common.BaseService;

/**
 * 产品所属区域Service层接口
 * @author TangXin
 *
 */
public interface ProductAreaService extends BaseService<ProductArea,ProductAreaDAO>{

	List<ProductArea> getAll() throws ServiceException;
	
}
