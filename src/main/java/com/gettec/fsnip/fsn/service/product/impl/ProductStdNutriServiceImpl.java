package com.gettec.fsnip.fsn.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.product.ProductStdNutriDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Nutrition;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductStdNutriService;

@Service(value="productStdNutriService")
public class ProductStdNutriServiceImpl  extends BaseServiceImpl<Nutrition, ProductStdNutriDAO>
		implements ProductStdNutriService{

	@Autowired protected ProductStdNutriDAO productStdNutriDAO;
	
	@Override
	public ProductStdNutriDAO getDAO() {
		return productStdNutriDAO;
	}

	/**
	 * 获取标准的营养列表
	 * @throws ServiceException 
	 */
	@Override
	public List<Nutrition> getListOfStandNutri() throws ServiceException {
		try {
			return productStdNutriDAO.getListOfStandNutri();
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】获取标准的营养列表，出现异常！", dex.getException());
		}
	}
	
	/**
	 * 按名称获取一条标准营养信息
	 * @throws ServiceException 
	 */
	@Override
	public Nutrition findByName(String name) throws ServiceException {
		try {
			return productStdNutriDAO.findByName(name);
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】按名称获取一条标准营养信息，出现异常！", dex.getException());
		}
	}
	
}
