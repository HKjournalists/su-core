package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.vo.ProductCertificationVO;

public interface BusinessCertificationDAO extends BaseDAO<BusinessCertification>{

	List<BusinessCertification> getListOfCertificationByProductId(Long productId) throws DaoException;
	
	List<BusinessCertification> getListOfCertificationByBusinessId(Long businessId) throws DaoException;

	List<ProductCertificationVO> getListOfCertificationVOByBusinessId(Long businessId) throws DaoException;
	
	Long countProductByBusinessCertificationId(Long busCertId) throws DaoException;

	long countByBusIdAndType(Long businessId, int type) throws DaoException;

	List<BusinessCertification> getListOfCertificationByBusIdAndType(
			Long businessId, int type, int page, int pageSize) throws DaoException;
}
