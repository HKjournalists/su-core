package com.lhfs.fsn.service.search;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.SearchResult;
import com.gettec.fsnip.fsn.service.common.BaseService;
import com.lhfs.fsn.dao.search.SearchDao;
import com.lhfs.fsn.vo.product.ProductCategoryVO;

public interface SearchService extends BaseService<SearchResult, SearchDao>{

	SearchResult getSearchResult(String keyword, String category,Integer catLevel, String brand,
			String feature, String order, Integer pageNum, Integer pageSize,
			String enterpriseName, boolean isApp, String nutriLabel) throws UnsupportedEncodingException;

	List<ProductCategory> getCategoryList();

	SearchResult getSearchResult(String keyword, String category,Integer catLevel, String brand,
			String feature, String order, Integer pageNum, Integer pageSize,
			String nutriLabel, String version, int width, int height) throws UnsupportedEncodingException;
	
	List<ProductCategoryVO> getCategoryListToApp(String appVersion);
	
	SearchResult getSearchProduct(String keyword, String category,Integer catLevel, String brand,
			String feature, String order, Integer pageNum, Integer pageSize,
			String enterpriseName, boolean isApp, String nutriLabel) throws UnsupportedEncodingException;
}