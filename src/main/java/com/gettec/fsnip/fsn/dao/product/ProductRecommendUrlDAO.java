package com.gettec.fsnip.fsn.dao.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;


public interface ProductRecommendUrlDAO extends BaseDAO<ProductRecommendUrl>{

	List<ProductRecommendUrl> getProductRecommendUrl(Long proId,String type);
	List<ProductRecommendUrl> getProductRecommendUrl(Long proId);
}
