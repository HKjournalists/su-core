package com.gettec.fsnip.fsn.service.product;

import java.util.List;

import com.gettec.fsnip.fsn.dao.product.ProductRecommendUrlDAO;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;
import com.gettec.fsnip.fsn.service.common.BaseService;

public interface ProductRecommendUrlService extends BaseService<ProductRecommendUrl, ProductRecommendUrlDAO>{

	List<ProductRecommendUrl> getProductRecommendUrl(Long proId, String identify);
	List<ProductRecommendUrl> getProductRecommendUrl(Long proId);
}
