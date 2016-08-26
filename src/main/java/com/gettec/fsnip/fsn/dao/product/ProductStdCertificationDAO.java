package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.base.Certification;

public interface ProductStdCertificationDAO extends BaseDAO<Certification>{

	List<Certification> getListOfStandCertification() throws DaoException;

	Certification findByName(String name) throws DaoException;
	
	List<Certification> getListOfStandCertificationByProductId(Long productId) throws DaoException;

}
