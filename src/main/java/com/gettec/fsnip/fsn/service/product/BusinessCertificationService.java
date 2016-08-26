package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.BusinessCertificationDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.gettec.fsnip.fsn.vo.ProductCertificationVO;

public interface BusinessCertificationService  
			extends BaseService<BusinessCertification, BusinessCertificationDAO>{

	List<BusinessCertification> getListOfCertificationByProductId(Long id) throws ServiceException;

	void save(BusinessUnit business) throws ServiceException;
	
	List<BusinessCertification> getListOfCertificationByBusinessId(Long businessId) throws ServiceException;
	
	List<ProductCertificationVO> getListOfCertificationVOByBusinessId(Long businessId) throws ServiceException;
	
	Long countProductByBusinessCertificationId(Long busCertId)throws ServiceException;

	long countByBusIdAndType(Long businessId, int type) throws ServiceException;

	List<BusinessCertification> getListOfCertificationByBusIdAndType(
			Long businessId, int type, int page, int pageSize) throws ServiceException;
	
}
