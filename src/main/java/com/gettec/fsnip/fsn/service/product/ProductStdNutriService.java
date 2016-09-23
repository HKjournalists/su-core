package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.ProductStdNutriDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Nutrition;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProductStdNutriService extends BaseService<Nutrition, ProductStdNutriDAO>{

	List<Nutrition> getListOfStandNutri() throws ServiceException;

	Nutrition findByName(String name) throws ServiceException;

}
