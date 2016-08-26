package com.lhfs.fsn.dao.search;

import java.util.List;
import java.util.Map;

import com.gettec.fsnip.fsn.dao.common.BaseDAO;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.SearchResult;
import com.lhfs.fsn.vo.product.BrandVO;
import com.lhfs.fsn.vo.product.CategoryVO;
import com.lhfs.fsn.vo.product.ProductCategoryVO;


public interface SearchDao extends BaseDAO<SearchResult>{

    List<CategoryVO> getCategoryList(String keyword, String category,Integer catLevel, String brand);

	List<BrandVO> getBrandList(String keyword, String category,Integer catLevel,
			String brand, String[] featureList, String nutriLabel);

	List<CategoryVO> getNavigation(String category, String root);

	List<Object[]> getResultList(String keyword, String category,Integer catLevel, String brand,
			String[] featureList, String ordername, String ordertype, int start, int pageSize, String nutriLabel);

	int getProductCount(String keyword, String category,Integer catLevel, String brand,
			String[] featureList, String ordername, String ordertype, String nutriLabel);

	List<ProductCategory> getCategory();

	CategoryVO getCategoryByCode(String category,Integer catLevel);

	List<BrandVO> getAllBrandList();
	
	List<CategoryVO> getCategoryListByBusName(String brand,String enterpriseName,String category,Integer catLevel);
	
	List<BusinessBrand> getBrandListByBusName(String enterpriseName);
	
	List<ProductCategoryVO> getFirstCategory();
	
	List<CategoryVO> getALLFirstCategoryVO();
	
	List<ProductCategoryVO> getChildrenCategory(String code);

	int countAllProduct();

	List<Object[]>  getResultListByZH( String ordername, String ordertype, int start, int pageSize);

	List<ProductCategoryVO> getThridCategory(Long secondCategoryId);

	int countProductByCondition(String keyword, String category,
			Integer catLevel, String enterpriseName, String brand,
			String nutriLabel,String ordername);
	
	List<Object[]>  getListProductsByCondition(String keyword, String enterpriseName, String category,Integer catLevel, String brand,
			String ordername, String ordertype, int start, int pageSize, String nutriLabel);

	Map getListBrandAndCategoryByCondition(String keyword, String category,
			Integer catLevel, String brand, String enterpriseName,
			String nutriLabel);

	List<CategoryVO> getListFirstCategoryByCondition(String keyword,
			String category, Integer catLevel, String brand,
			String enterpriseName, String nutriLabel);
	
	/**
	 * 根据3级分类id，查询出一级、二级、三级分类vo
	 * @author tangxin 2015/04/22
	 */
	Map getFirstAndSecondAndThridCategoryByThridId(String categoryId);
	
    /**
	 * 获取下一及产品分类
	 * @author tangxin 2015/04/22
	 */
	List<CategoryVO> getNextCategory(String category, Integer catLevel);
	
	public int countAllProductbyRisk();
}
