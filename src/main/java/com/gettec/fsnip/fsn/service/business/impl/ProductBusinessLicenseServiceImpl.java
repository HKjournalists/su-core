package com.gettec.fsnip.fsn.service.business.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.dao.business.ProductBusinessLicenseDAO;
import com.gettec.fsnip.fsn.model.product.ProductBusinessLicense;
import com.gettec.fsnip.fsn.service.business.ProductBusinessLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
@Service(value="productBusinessLicenseService")
public class ProductBusinessLicenseServiceImpl extends BaseServiceImpl<ProductBusinessLicense, ProductBusinessLicenseDAO>
implements ProductBusinessLicenseService{
	@Autowired ProductBusinessLicenseDAO productBusinessLicenseDAO;
	@Override
	public ProductBusinessLicenseDAO getDAO() {
		return productBusinessLicenseDAO;
	}
	
	public List<ProductBusinessLicense> getProductBusinessLicenseListByProductId(long productId){
		return this.productBusinessLicenseDAO.getProductBusinessLicenseListByProductId(productId);
	}
}
