package com.gettec.fsnip.fsn.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.product.ProductStdCertificationDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductStdCertificationService;

@Service(value="productStdCertificationService")
public class ProductStdCertificationServiceImpl extends BaseServiceImpl<Certification, ProductStdCertificationDAO>
	implements ProductStdCertificationService{

	@Autowired private ProductStdCertificationDAO productStdCertificationDAO;
	
	@Override
	public ProductStdCertificationDAO getDAO() {
		return productStdCertificationDAO;
	}

	/**
	 * 获取标准的认证信息类别
	 * @throws ServiceException 
	 */
	@Override
	public List<Certification> getListOfStandCertification() throws ServiceException {
		try {
			return productStdCertificationDAO.getListOfStandCertification();
		} catch (DaoException dex) {
			throw new ServiceException("ProductStdCertificationServiceImpl.getListOfStandCertification() "+dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 按名称查找一条标准的认证信息
	 * @throws ServiceException 
	 */
	@Override
	public Certification findByName(String name) throws ServiceException {
		try {
			return productStdCertificationDAO.findByName(name);
		} catch (DaoException dex) {
			throw new ServiceException("【service-error】按名称查找一条标准的认证信息，出现异常！", dex.getException());
		}
	}

	/**
	 * 按产品id查找产品的认证信息集合
	 * @throws ServiceException 
	 */
	@Override
	public List<Certification> getListOfStandCertificationByProductId(
			Long productId) throws ServiceException {
		try{
			return productStdCertificationDAO.getListOfStandCertificationByProductId(productId);
		}catch(DaoException dex){
			throw new ServiceException("【service-error】按产品id查找产品的认证信息集合时，出现异常！", dex.getException());
		}
	}
	
}
