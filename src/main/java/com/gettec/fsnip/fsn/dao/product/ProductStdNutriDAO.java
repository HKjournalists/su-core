package com.gettec.fsnip.fsn.dao.product;


import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.base.Nutrition;

public interface ProductStdNutriDAO  extends BaseDAO<Nutrition>{

	List<Nutrition> getListOfStandNutri() throws DaoException;

	Nutrition findByName(String name) throws DaoException;

}
