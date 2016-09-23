package com.gettec.fsnip.fsn.dao.business;



import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ProductBusinessLicense;

public interface ProductBusinessLicenseDAO  extends BaseDAO<ProductBusinessLicense>{

	void saveProductBusinessLicense(ProductBusinessLicense entity);

	ProductBusinessLicense getsetBusinessId(Long businseeId,Long productId);
	/**
	 * 更加当前的id删除该条数据
	 * @param id
	 */
	void getsetProductId(Long id);
	/**
	 * 删除图片ID删除该数据
	 * @param id
	 */
	void delResourceIdProductBusinessLicense(Long resourceId);
	
	/**
	 * 修改图片
	 * @param resource
	 */
	void updateResource(Resource resource);

	void updateProductBusiness(Long id, String stgImg);
	
	List<ProductBusinessLicense> getProductBusinessLicenseListByProductId(long productId);

}
