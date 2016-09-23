package com.gettec.fsnip.fsn.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.product.ProductAreaDAO;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.product.ProductArea;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductAreaService;

/**
 * 产品所属区域service层接口实现
 * @author TangXin
 *
 */
@Service(value="productAreaService")
public class ProductAreaServiceImpl extends BaseServiceImpl<ProductArea,ProductAreaDAO>
	implements ProductAreaService{

	@Autowired
	private ProductAreaDAO productAreaDAO;
	
	@Override
	public ProductAreaDAO getDAO() {
		return productAreaDAO;
	}

	/**
	 * 获取所有的产品区域信息
	 * @return List<ProductArea>
	 * @throws ServiceException
	 * @author TangXin
	 */
	@Override
	public List<ProductArea> getAll() throws ServiceException{
		try{
			return getDAO().findAll();
		}catch(JPAException jpae){
			throw new ServiceException("ProductAreaServiceImpl.getAll()->"+jpae.getMessage(),jpae);
		}
	}
}
