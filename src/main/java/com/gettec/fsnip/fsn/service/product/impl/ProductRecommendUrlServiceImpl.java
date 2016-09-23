package com.gettec.fsnip.fsn.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductRecommendUrlDAO;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.product.ProductRecommendUrlService;

@Service(value="recommendUrlService")
public class ProductRecommendUrlServiceImpl extends BaseServiceImpl<ProductRecommendUrl,ProductRecommendUrlDAO>
implements ProductRecommendUrlService{
	@Autowired protected ProductRecommendUrlDAO recommendUrlDAO;
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductRecommendUrlDAO getDAO() {
		return recommendUrlDAO;
	}

	@Override
	public List<ProductRecommendUrl> getProductRecommendUrl(Long proId,String type) {
		return recommendUrlDAO.getProductRecommendUrl(proId,type);
	}
	
	public List<ProductRecommendUrl> getProductRecommendUrl(Long proId) {
		return recommendUrlDAO.getProductRecommendUrl(proId);
	}
}
