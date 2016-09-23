package com.lhfs.fsn.cache;

public interface EhCacheUtils {
	String CACHE_HOTPRODUCTS_BY_ID = "hotproducts_byid"; // 热点产品列表（分页）
	String CACHE_HOTPRODUCTS_BY_IDS = "hotproducts_byids"; // 热点产品根据指定多个id获取产品列表列表（分页）
	String CACHE_HOTPRODUCTS_TOTAL_BY_ID = "hotproduct_total_byId"; // 热点产品总数
	String CACHE_HOTPRODUCTS_TOTAL_BY_IDS = "hotproduct_total_byIds"; // 热点食品根据指定多个id获取产品数量
	String CACHE_PRODUCTINFO = "productinfo"; // 按id获取某一具体热点产品详细信息（大众门户）
	String CACHE_PRODUCTINFO_APP = "productinfo_app"; // 按id获取某一具体热点产品详细信息（手机APP）
	String CACHE_REPORT_SELF = "report_self"; // 按产品id获取某一自检报告的详细信息
	String CACHE_REPORT_CENSOR = "report_censor"; // 按产品id获取某一送检报告的详细信息
	String CACHE_REPORT_SAMPLE = "report_sample"; // 按产品id获取某一抽检报告的详细信息
	String CACHE_REPORT_THIRD="report_third";
	String CACHE_SEARCH = "search"; // 查询接口（大众门户）
	String CACHE_SEARCH_V215 = "search_v215"; // 查询接口（大众门户）
	String CACHE_SEARCH_APP = "search_app"; // 查询接口（手机APP）
	String CACHE_RENHUAI_BAIJIU = "renhuai_baijiu"; // 仁怀白酒企业和产品展示接口
	String CACHE_RENHUAI_BAIJIU_TOTAL = "cache_renhuai_baijiu_total"; // 仁怀白酒企业和产品展示相关企业的总是
	String CACHE_PRODUCT_BY_CATEGORY = "cache_product_by_category"; //portal 根据产品分类查询产品信息集合。
	String CACHE_HOT_BUSINESSUNIT = "cache_hot_business_unit"; //热点企业信息列表
	String CACHE_HOT_BUSUNIT_TOTAL = "cache_hot_busunit_total"; //热点企业信息总数
	String CACHE_SEARCH_BY_BUSNAME = "search_by_busname"; // 查询接口,通过企业名称查询企业产品
	String CACHE_ATGOO_PRODUCT = "cache_atgoo_product";
}
