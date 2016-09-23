package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.ProductStdCertificationDAO;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProductStdCertificationService  
		extends BaseService<Certification, ProductStdCertificationDAO>{

	List<Certification> getListOfStandCertification() throws ServiceException;

	Certification findByName(String name) throws ServiceException;
	
	List<Certification> getListOfStandCertificationByProductId(Long productId) throws ServiceException;
}
