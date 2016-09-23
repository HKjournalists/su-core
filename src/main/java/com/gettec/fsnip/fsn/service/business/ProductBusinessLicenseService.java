package com.gettec.fsnip.fsn.service.business;

import java.util.List;
import java.util.Set;



import com.gettec.fsnip.fsn.dao.business.ProductBusinessLicenseDAO;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductBusinessLicense;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProductBusinessLicenseService  extends BaseService<ProductBusinessLicense, ProductBusinessLicenseDAO>{
	public List<ProductBusinessLicense> getProductBusinessLicenseListByProductId(long productId);
}
