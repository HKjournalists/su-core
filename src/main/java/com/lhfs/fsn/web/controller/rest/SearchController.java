package com.lhfs.fsn.web.controller.rest;

import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_SEARCH;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_SEARCH_APP;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_SEARCH_BY_BUSNAME;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_SEARCH_V215;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.SearchResult;
import com.lhfs.fsn.cache.EhCacheFactory;
import com.lhfs.fsn.service.search.SearchService;
import com.lhfs.fsn.vo.product.ProductCategoryVO;
import com.lhfs.fsn.web.controller.RESTResult;

@Controller
@Scope("prototype")
@RequestMapping("/portal/search")
public class SearchController {
	private final static Logger log = Logger.getLogger(SearchController.class);
	@Autowired private SearchService searchLFService;

	/**
	 * 大众门户和手机App搜索接口
	 * @updater TangXin 2015-03-30
	 */
	@RequestMapping(method = RequestMethod.GET, value = "")
	public RESTResult<SearchResult> getSearchResult(
			@RequestParam String keyword, 
			@RequestParam String category,
			@RequestParam(required=false) Integer catLevel, //产品类别的级别（一级：1、二级：2、三级：3）
			@RequestParam String brand, 
			@RequestParam String feature,
			@RequestParam String order,
			@RequestParam Integer pageNum,
			@RequestParam Integer pageSize,
			@RequestParam(value="sysType", required = false) String sysType, //大众门户或者手机app
			@RequestParam(value="nutriLabel", required=false) String nutriLabel, //产品营养指数
			@RequestParam(value="version", required=false) String version //接口调用的版本号
			) {
		try{


			SearchResult searchResult = null;
			catLevel = (catLevel==null ? 1 : catLevel);
			nutriLabel = (nutriLabel == null ? "" : nutriLabel);
			version = (version== null ? "" : version);
			/* 大众门户搜索接口的实现 */
			if(sysType==null){
				if("v2.1.5".equals(version)){
					/**
					 * 企业门户 v2.1.5 版本提供大众门户接口
					 */
					searchResult = searchProductByVersion(keyword, category,catLevel, brand,feature, order, pageNum, pageSize, nutriLabel,"");
				} else {
					/* v2.1.5 版本之前的搜索接口 */
					searchResult = searchProductNoneVersion(keyword, category,catLevel, brand,feature, order, pageNum, pageSize, nutriLabel);
				}
			}else if(sysType.equals("app")){ // 手机App
				searchResult = searchProductOfApp(keyword, category,catLevel, brand,feature, order, pageNum, pageSize, nutriLabel, version);
			}
			return new RESTResult<SearchResult>(1, searchResult);
		}catch(Exception e){
			e.printStackTrace();
			return new RESTResult<SearchResult>(0, "非法操作或者数据缺失！");
		}
	}

	/**
	 * 给大众门户提供的搜索接口：根据企业名称查找企业产品
	 * @updater TangXin 2015-03-30
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/byEnterpriseName")
	public RESTResult<SearchResult> getResult(@RequestParam String keyword,
			@RequestParam String category, @RequestParam String brand,
			@RequestParam String feature, @RequestParam String order,
			@RequestParam Integer pageNum, @RequestParam Integer pageSize,
			@RequestParam String enterpriseName,@RequestParam(required=false) Integer catLevel,
			@RequestParam(required=false) String nutriLabel, //产品营养指数
			@RequestParam(required=false) String version //产品营养指数
			)throws UnsupportedEncodingException {
		// 调用service的函数获得结果
		SearchResult searchResult = null;
		catLevel = (catLevel==null ? 1:catLevel);
		nutriLabel = (nutriLabel == null ? "" : nutriLabel);
		version = (version == null ? "" : version);
		Element result = (Element) EhCacheFactory.getCacheElement(CACHE_SEARCH_BY_BUSNAME + "_" + keyword + category + catLevel + pageNum +
				brand + order + enterpriseName + nutriLabel + version);
		if(result == null){ // 大众门户
			/* v2.1.5版本 提供portal热点企业产品的新接口 */
			if("v2.1.5".equals(version)){
				searchResult = searchProductByVersion(keyword, category, catLevel, brand, feature, order, pageNum, pageSize, nutriLabel, enterpriseName);
			} else {
				/* v2.1.5版本之前提供的老接口 */
				searchResult = searchLFService.getSearchResult(keyword, category,catLevel, brand, feature, order, pageNum, pageSize, enterpriseName, false, nutriLabel);
			}
			EhCacheFactory.put(CACHE_SEARCH_BY_BUSNAME + "_" + keyword + category + catLevel + pageNum + brand + order + enterpriseName + nutriLabel + version, searchResult);
		}else{
			searchResult = (SearchResult) result.getObjectValue();
		}
		/* 如果查询结果为空，返回异常信息 */
		if (searchResult == null) {
			return new RESTResult<SearchResult>(0, "非法操作或者数据缺失！");
		}
		return new RESTResult<SearchResult>(1, searchResult);
	}

	/**
	 * 给大众门户提供接口：获取所有分类信息，用来动态加载左侧分类菜单
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="/getCategory")
	public RESTResult<List<ProductCategory>> getCategory(){
		List<ProductCategory> categoryList = searchLFService.getCategoryList();
		if (categoryList == null)
			return new RESTResult<List<ProductCategory>>(0, "非法操作或者数据缺失！");
		log.info(categoryList.toString());
		return new RESTResult<List<ProductCategory>>(1, categoryList);
	}

	/**
	 * 手机App接口：获取所有商品分类信息，层级显示。
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.GET, value="/getCategoryToApp")
	public RESTResult<List<ProductCategoryVO>> getCategoryToApp(@RequestParam(value="version", required = false) String version){
		List<ProductCategoryVO> categoryList = searchLFService.getCategoryListToApp(version);
		if (categoryList == null)
			return new RESTResult<List<ProductCategoryVO>>(0, "非法操作或者数据缺失！");
		return new RESTResult<List<ProductCategoryVO>>(1, categoryList);
	}

	/**
	 * v2.1.5版本之前 大众门户接口 搜索产信息（不带版本号信息）
	 * @author tangxin 2015/03/30
	 */
	private SearchResult searchProductNoneVersion(String keyword, String category, Integer catLevel, String brand, String feature, 
			String order, Integer pageNum, Integer pageSize, String nutriLabel) throws UnsupportedEncodingException {
		SearchResult searchResult = null;
		Element result = (Element) EhCacheFactory.getCacheElement(CACHE_SEARCH + "_" + keyword + category + pageNum + brand + order + nutriLabel);
		if(result == null){ // 大众门户
			searchResult = searchLFService.getSearchResult(keyword, category,catLevel, brand,feature, order, pageNum, 
					pageSize, "", false, nutriLabel);
			EhCacheFactory.put(CACHE_SEARCH + "_" + keyword + category + pageNum + brand + order + nutriLabel, searchResult);
		}else{
			searchResult = (SearchResult) result.getObjectValue();
		}
		return searchResult;
	}

	/**
	 * v2.1.5版本  大众门户接口 搜索产信息（带版本号信息）
	 * @author tangxin 2015/03/31
	 */
	private SearchResult searchProductByVersion(String keyword, String category, Integer catLevel, String brand, String feature, 
			String order, Integer pageNum, Integer pageSize, String nutriLabel,String enterpriseName) throws UnsupportedEncodingException {
		SearchResult searchResult = null;
		Element result = (Element) EhCacheFactory.getCacheElement(CACHE_SEARCH_V215 + "_" + keyword + category + pageNum + brand + order + nutriLabel + enterpriseName);
		if(result == null){ // 大众门户
			searchResult = searchLFService.getSearchProduct(keyword, category,catLevel, brand,feature, order, pageNum, pageSize, enterpriseName, false, nutriLabel);
			EhCacheFactory.put(CACHE_SEARCH_V215 + "_" + keyword + category + pageNum + brand + order + nutriLabel + enterpriseName, searchResult);
		}else{
			searchResult = (SearchResult) result.getObjectValue();
		}
		return searchResult;
	}

	/**
	 * 手机app产品搜索接口 搜索产信息（不带版本号信息）
	 * @author tangxin 2015/03/30
	 */
	private SearchResult searchProductOfApp(String keyword, String category, Integer catLevel, String brand, String feature, 
			String order, Integer pageNum, Integer pageSize, String nutriLabel, String version) throws UnsupportedEncodingException {
		SearchResult searchResult = null;
		Element result = (Element) EhCacheFactory.getCacheElement(CACHE_SEARCH_APP + "_" + keyword + category + pageNum + brand + order + nutriLabel + version);
		if(result == null){
			searchResult = searchLFService.getSearchResult(keyword, category,catLevel, brand, feature, order, pageNum, pageSize, nutriLabel, version, 118, 108);
			//EhCacheFactory.put(CACHE_SEARCH_APP + "_" + keyword + category + pageNum + brand + order + nutriLabel + version, searchResult);
		}else{
			searchResult = (SearchResult) result.getObjectValue();
		}
		return searchResult;
	}
}