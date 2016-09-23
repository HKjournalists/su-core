package com.gettec.fsnip.fsn.model.product;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gettec.fsnip.fsn.model.common.Model;
import com.lhfs.fsn.util.PageUtil;
import com.lhfs.fsn.vo.product.BrandVO;
import com.lhfs.fsn.vo.product.CategoryVO;

public class SearchResult extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3831816463467903617L;
	
	
	/*private List<Product> productList;  //搜索结果集
	 * 应用分页，置入page.List
*/	private List<CategoryVO> categoryList;	//当前分类集
	private List<BrandVO> businessBrandList;	//品牌集合
	private String keyword;		//搜索关键字，为空表示从Category进入
	private String order;	
	/* 
	 * 	第一位表示排序类型：0为综合，1为自检，2为送检，3为抽检
	 *	第二位表示排序顺序：0为升序，1为降序
	 *	比如01 就是 综合降序 排列
	 */
	private List<CategoryVO> navigation;	//导航信息
	private Map<String,Set<String>> features; //features信息
	private String currentFeatures; //当前勾选的features信息
	private PageUtil page = new PageUtil();
	private CategoryVO selectedCategory; //当前勾选的category信息
	private String brand;
	
	
	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public List<CategoryVO> getCategoryList() {
		return categoryList;
	}


	public void setCategoryList(List<CategoryVO> categoryList) {
		this.categoryList = categoryList;
	}


	public List<BrandVO> getBusinessBrandList() {
		return businessBrandList;
	}


	public void setBusinessBrandList(List<BrandVO> businessBrandList) {
		this.businessBrandList = businessBrandList;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getOrder() {
		return order;
	}


	public void setOrder(String order) {
		this.order = order;
	}


	public List<CategoryVO> getNavigation() {
		return navigation;
	}


	public void setNavigation(List<CategoryVO> navigation) {
		this.navigation = navigation;
	}


	public Map<String, Set<String>> getFeatures() {
		return features;
	}


	public void setFeatures(Map<String, Set<String>> features) {
		this.features = features;
	}


	public String getCurrentFeatures() {
		return currentFeatures;
	}


	public void setCurrentFeatures(String currentFeatures) {
		this.currentFeatures = currentFeatures;
	}


	public PageUtil getPage() {
		return page;
	}


	public void setPage(PageUtil page) {
		this.page = page;
	}


	public SearchResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public CategoryVO getSelectedCategory() {
		return selectedCategory;
	}


	public void setSelectedCategory(CategoryVO selectedCategory) {
		this.selectedCategory = selectedCategory;
	}


	public SearchResult(List<CategoryVO> categoryList,
			List<BrandVO> businessBrandList, String keyword,
			String order, List<CategoryVO> navigation,
			Map<String, Set<String>> features, String currentFeatures,
			PageUtil page, CategoryVO selectedCategory, String brand) {
		super();
		this.categoryList = categoryList;
		this.businessBrandList = businessBrandList;
		this.keyword = keyword;
		this.order = order;
		this.navigation = navigation;
		this.features = features;
		this.currentFeatures = currentFeatures;
		this.page = page;
		this.selectedCategory = selectedCategory;
		this.brand = brand;
	}

}
