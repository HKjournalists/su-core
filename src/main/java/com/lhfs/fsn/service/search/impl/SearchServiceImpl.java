package com.lhfs.fsn.service.search.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.SearchResult;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.model.trace.TraceDataProductName;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.MkCategoryInfoService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ImportedProductService;
import com.gettec.fsnip.fsn.service.trace.TraceDataProductNameService;
import com.gettec.fsnip.fsn.service.trace.TraceDataService;
import com.gettec.fsnip.fsn.util.ImgUtils;
import com.lhfs.fsn.dao.common.CertificationDao;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.dao.search.SearchDao;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.service.search.SearchService;
import com.lhfs.fsn.util.PageUtil;
import com.lhfs.fsn.util.StringUtil;
import com.lhfs.fsn.vo.product.BrandVO;
import com.lhfs.fsn.vo.product.CategoryVO;
import com.lhfs.fsn.vo.product.CertificationVO;
import com.lhfs.fsn.vo.product.Product4SearchVO;
import com.lhfs.fsn.vo.product.ProductCategoryVO;

@Service(value = "searchLFService")
public class SearchServiceImpl extends BaseServiceImpl<SearchResult, SearchDao>
implements SearchService {
	public static final String DEFAULT_ORDER = "01"; // 搜索结果 默认按照综合降序
	public static final int DEFAULT_PAGENUM = 1; // 搜索结果默认为第一页
	public static final int PAGESIZE = 40;
	@Autowired
	private SearchDao searchDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductService productService;
	@Autowired
	private CertificationDao certificationDao;
	@Autowired
	private MkCategoryInfoService mkCategoryInfoService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ImportedProductService importedProductService;
	@Autowired
	private TraceDataProductNameService traceDataProductNameService;
	@Autowired
	private TraceDataService traceDataService;

	/**
	 * 获取搜索结果
	 * 
	 * @param keyword
	 * @param category
	 * @param catLevel
	 *            产品类别的级别（一级：1、二级：2、三级：3）
	 * @param brand
	 * @param feature
	 * @param order
	 * @param pageNum
	 * @param pageSize
	 * @param enterpriseName
	 * @param isApp
	 * @return
	 * @throws UnsupportedEncodingException
	 * @updater TangXin 2015/03/31
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchResult getSearchResult(String keyword, String category,
			Integer catLevel, String brand, String feature, String order,
			Integer pageNum, Integer pageSize, String enterpriseName,
			boolean isApp, String nutriLabel)
					throws UnsupportedEncodingException {
		boolean isNull = false;
		if (keyword.equals("") && category.equals("") && brand.equals("")
				&& "".equals(nutriLabel)) {
			isNull = true;
		}
		/* 1. 处理参数 */
		nutriLabel = StringUtil.isBlank(nutriLabel) ? "" : StringUtil
				.getUTF8Code(nutriLabel);
		keyword = StringUtil.isBlank(keyword) ? "" : StringUtil
				.getUTF8Code(keyword);
		category = StringUtil.isBlank(category) ? "" : category;
		brand = StringUtil.isBlank(brand) ? "" : StringUtil.getUTF8Code(brand);
		feature = StringUtil.isBlank(feature) ? "" : StringUtil
				.getUTF8Code(feature);
		order = StringUtil.isBlank(order) ? DEFAULT_ORDER : order; // 处理排序信息
		// 默认按照综合降序
		pageNum = StringUtil.isBlank(pageNum) ? DEFAULT_PAGENUM : pageNum; // 处理分页页码信息
		// 默认第一页
		String ordertype = ""; // 升序 or 降序
		String root = ""; // 当前分类的root节点，如010101的root为01
		String[] featureList = null;
		/* 解析特征信息 */
		if (!"".equals(feature)) {
			featureList = feature.split(",");
			for (String a : featureList) {
				String[] tmp = a.split("--");
				if (tmp.length != 2)
					return null;
			}
		}

		/* 2.解析排序信息 */
		Map<String, String> orderCondition = getOrderCondition(order);
		String ordername = orderCondition.get("ordername"); // 排序的字段名
		ordertype = orderCondition.get("ordertype"); // 升序或降序排列

		/* 3.查询符合条件的数据 */
		List<CategoryVO> categoryList = new ArrayList<CategoryVO>();
		List<BrandVO> brandVOList = new ArrayList<BrandVO>();
		List<CategoryVO> navigation = new ArrayList<CategoryVO>();
		CategoryVO selectedCategory = new CategoryVO();

		/* 4.获取page */
		PageUtil page = searchPage(enterpriseName, keyword, category, catLevel,
				brand, featureList, ordername, ordertype, pageNum, pageSize,
				nutriLabel);

		/* 4.1.如果查询的数据列表为空，将查询总记录赋值为0 */
		if (page != null
				&& (page.getList() == null || page.getList().size() == 0)
				&& page.getTotalrecord() == 1) {
			page.setTotalrecord(0);
		}
		/* 非手机app，即大众门户,需要返回分类和商标信息 */
		if (!isApp) {
			/* 获取categoryList、brandVOList、navigation */
			Map map = searchList(isNull, enterpriseName, keyword, category,
					catLevel, brand, root, featureList, nutriLabel);
			categoryList = (List<CategoryVO>) map.get("categoryList");
			brandVOList = (List<BrandVO>) map.get("brandVOList");
			navigation = (List<CategoryVO>) map.get("navigation");
			/* 获取selectedCategory */
			if (!"".equals(category)) {
				selectedCategory = searchDao.getCategoryByCode(category,
						catLevel);
			}
		}
		/* 4. 返回 */
		SearchResult searchResult = new SearchResult(categoryList, brandVOList,
				keyword, order, navigation, null, feature, page,
				selectedCategory, brand);
		return searchResult;
	}

	/**
	 * 获取categoryList、brandVOList、navigation
	 * 
	 * @param isNull
	 * @param enterpriseName
	 * @param keyword
	 * @param category
	 * @param brand
	 * @param root
	 * @param featureList
	 * @updater TangXin 2015/03/31
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map searchList(boolean isNull, String enterpriseName,
			String keyword, String category, Integer catLevel, String brand,
			String root, String[] featureList, String nutriLabel) {
		List<CategoryVO> categoryList = null;
		List<CategoryVO> navigation = null;
		List<BrandVO> brandVOList = new ArrayList<BrandVO>();
		if (isNull && enterpriseName.equals("")) {
			categoryList = searchDao.getALLFirstCategoryVO();
			brandVOList = searchDao.getAllBrandList();
		} else {
			categoryList = searchDao.getCategoryList(keyword, category,
					catLevel, brand);
			brandVOList = searchDao.getBrandList(keyword, category, catLevel,
					brand, featureList, nutriLabel);
			try {
				/* 将一二三级菜单都封装到 List<ProductCategory> navigation 中 */
				if (catLevel != null && catLevel == 3) {
					/* 1.先将第三级类别查出来封装到 ProductCategory中 */
					ProductCategoryInfo categoryInfo = mkCategoryInfoService
							.findById(Long.valueOf(category));
					if (categoryInfo == null) {
						navigation = new ArrayList<CategoryVO>();
						navigation.add(new CategoryVO());
					} else {
						/* 第三级分类 */
						ProductCategory productCategory = new ProductCategory(
								categoryInfo.getId(), categoryInfo.getName(),
								"", categoryInfo.getDisplay());
						/* 2.将第三及分类中的二级分类的CODE 来查询出一级分类 */
						navigation = searchDao.getNavigation(categoryInfo
								.getCategory().getCode(), root);
						/* 3.将第三级分类添加到LIST中 */
						navigation.add(new CategoryVO(productCategory));
					}
				} else {
					/* 将一二级菜单都封装到 List<ProductCategory> navigation 中 */
					navigation = searchDao.getNavigation(category, root);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map map = new HashMap();
		map.put("categoryList", categoryList);
		map.put("brandVOList", brandVOList);
		map.put("navigation", navigation);
		return map;
	}

	/**
	 * 用于将搜索中加载出来的产品信息封装到VO中
	 * 
	 * @param objects
	 * @return List<Product4SearchVO>
	 * @author HuangYog
	 * @updater tangxin 2015-03-30
	 */
	private List<Product4SearchVO> pottedProductToVO(List<Object[]> objs,
			String version) {
		List<Product4SearchVO> product4SearchVOs = new ArrayList<Product4SearchVO>();
		if (objs != null && objs.size() > 0) {
			for (int i = 0; i < objs.size(); i++) {
				Object[] obj = (Object[]) objs.get(i);
				Long id = obj[0] != null ? Long.valueOf(obj[0].toString()) : -1;
				String name = obj[1] != null ? obj[1].toString() : "";
				String imgUrl = obj[2] != null ? obj[2].toString() : "";
				// 过滤url最后的"|"
				String lastChar = imgUrl.equals("") ? "" : imgUrl.substring(
						imgUrl.length() - 1, imgUrl.length());
				if (lastChar.equals("|") || lastChar == "|") {
					imgUrl = imgUrl.substring(0, imgUrl.length() - 1);
				}
				String cstm = obj[3] != null ? obj[3].toString() : "";
				Float qscoreSelf = obj[4] != null ? Float.valueOf(obj[4]
						.toString()) : -1;
				Float qscoreCensor = obj[5] != null ? Float.valueOf(obj[5]
						.toString()) : -1;
				Float qscoreSample = obj[6] != null ? Float.valueOf(obj[6]
						.toString()) : -1;
				Boolean qsriskSucceed = obj[7] != null ? Boolean.valueOf(obj[7]
						.toString()) : false;
				Double qsriskIndex = obj[8] != null ? Double.valueOf(obj[8]
						.toString()) : null;
				String qstestPropertyName = obj[9] != null ? obj[9].toString()
						: "";
				String nutriLabel = obj[10] != null ? obj[10].toString() : "";
				/* 报告数量 v2.1.5版本新增字段 */
				Long reportNum = 0L;
				if (version != null && "v2.1.5".equals(version)) {
					reportNum = obj[11] != null ? Long.valueOf(obj[11]
							.toString()) : 0L;
				}
				/* 处理营养指数 */
				nutriLabel = nutriLabel.replaceAll(";", "|");
				if (nutriLabel.endsWith("|")) {
					nutriLabel = nutriLabel.substring(0,
							nutriLabel.length() - 1);
				}
				List<CertificationVO> productCertification = new ArrayList<CertificationVO>();
				if (id != -1) {
					productCertification = certificationDao
							.findByPuroductId(id);
				}
				/* 处理产品图片 */
				List<Resource> imgList = null;
				try {
					imgList = resourceService.getProductImgListByproId(id);
				} catch (ServiceException e) {
					e.printStackTrace();
				}// 查找产品图片集合
				if (imgList != null && imgList.size() == 0) {
					if (imgUrl != null) {
						String[] imgUrlListArray = imgUrl.split("\\|");
						for (String url : imgUrlListArray) {
							Resource re = new Resource();
							re.setUrl(url);
							imgList.add(re);
						}
					}
				}
				Product4SearchVO product4SearchVO = new Product4SearchVO(id,
						name, imgUrl, cstm, qscoreSelf, qscoreCensor,
						qscoreSample, productCertification, qsriskIndex,
						qsriskSucceed, qstestPropertyName, nutriLabel,
						reportNum, imgList);
				//如果是app则有这个参数,如果是大众门户则没有这个参数
				if(obj.length>12){
					product4SearchVO.setBusinessUnitName(obj[12]==null?null:obj[12].toString());
				}
				/* 判断该产品是不是进口产品 */
				ImportedProduct impPro = null;
				try {
					impPro = importedProductService.findByProductId(id);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				if (impPro == null
						|| "中国".equals(impPro.getCountry().getName())) {
					product4SearchVO.setImportedProduct(false);
				} else {
					product4SearchVO.setImportedProduct(true);
				}
				product4SearchVOs.add(product4SearchVO);
			}
		}
		return product4SearchVOs;
	}

	/**
	 * 获取page
	 * 
	 * @param enterpriseName
	 *            企业名称
	 * @param keyword
	 *            关键字
	 * @param category
	 *            产品类型
	 * @param brand
	 *            商标
	 * @updater TangXin 2015/03/31
	 */
	private PageUtil searchPage(String enterpriseName, String keyword,
			String category, Integer catLevel, String brand,
			String[] featureList, String ordername, String ordertype,
			Integer pageNum, Integer pageSize, String nutriLabel) {
		int count = 0;
		PageUtil page = null;
		List<Object[]> productList = null;
		if ("".equals(enterpriseName)) {
			if (ordername.equals("综合") && keyword.equals("")
					&& category.equals("") && brand.equals("")
					&& "".equals(nutriLabel)) {
				count = searchDao.countAllProduct();
				page = new PageUtil(count, pageNum, pageSize);
				productList = searchDao.getResultListByZH(ordername, ordertype,
						page.getStartindex(), page.getPagesize());
			} else {
				count = searchDao.getProductCount(keyword, category, catLevel,
						brand, featureList, ordername, ordertype, nutriLabel);
				page = new PageUtil(count, pageNum, pageSize);
				productList = searchDao.getResultList(keyword, category,
						catLevel, brand, featureList, ordername, ordertype,
						page.getStartindex(), page.getPagesize(), nutriLabel);
			}
		} else {
			count = productDao.countProductByBusNameAndCategoryAndBrand(
					enterpriseName, category, catLevel, brand, ordername,
					ordertype, nutriLabel);
			try {
				page = new PageUtil(count, pageNum, pageSize);
				productList = productService.getListOfProductByName(
						enterpriseName, category, catLevel, brand,
						page.getStartindex(), page.getPagesize(), ordername,
						ordertype, nutriLabel);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		/*
		 * 将List<Object[]> 封装成List<Product4SearchVO>
		 * 第二个产品为版本号，老版本的接口（v2.1.5以前）都不传版本好信息
		 */
		List<Product4SearchVO> product4SearchVOs = pottedProductToVO(
				productList, "");
		page.setList(product4SearchVOs);
		return page;
	}

	/**
	 * 大众门户搜索接口（新），根据指定的筛选条件筛选获取产品列表
	 * 
	 * @author tangxin 2015/04/01
	 */
	private PageUtil searchProduct(String enterpriseName, String keyword,
			String category, Integer catLevel, String brand, String ordername,
			String ordertype, Integer pageNum, Integer pageSize,
			
			String nutriLabel, boolean isNull, boolean isApp) {
		int count = 0;
		PageUtil page = null;
		List<Object[]> productList = null;
		String _keyword = keyword;
		if (isNull) {
			if ("风险指数".equals(ordername)) {
				count = searchDao.countAllProductbyRisk();
			} else {
				count = searchDao.countAllProduct();
			}
			page = new PageUtil(count, pageNum, pageSize);
			productList = searchDao.getResultListByZH(ordername, ordertype,
					page.getStartindex(), page.getPagesize());
		} else {
			TraceData traceData = traceDataProductNameService.isKeywordTraceData(keyword);
			Long pi =null;
			if (traceData.isTraceData()) {
				if(keyword.length()==39){
					_keyword=keyword.substring(26, 39);
					try {
						if(productService.getProductInfoByBarcode(_keyword)!=null){
							pi=productService.getProductInfoByBarcode(_keyword).getId();
						}
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}else{
					TraceDataProductName traceDataProductName = traceDataProductNameService.getBarcodeByProductName(traceData.getProductName());
					if (traceDataProductName != null) {
						_keyword = traceDataProductName.getBarcode();
						pi = traceDataProductName.getProductId();
					}
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				String prodate=dateFormat.format(traceData.getProductDate());
				String zero="0000000000000000";
				String bc = traceData.getBatchCode();
				String fsnCode=pi.toString();
				if (traceDataService.checkbyproductIDandbatchCode(pi, bc)) {
					//生成食安唯一码
					if(fsnCode.length()<=16){
						int endindex=16-fsnCode.length();
						fsnCode=zero.substring(0,endindex)+fsnCode+prodate+zero.substring(0,7);
						String s=traceDataService.CheckCode(fsnCode);
						fsnCode=fsnCode+s;
					}else{
						return null;

					}
					traceData.setFsnCode(fsnCode);
					traceData.setKeyWord(keyword);
					traceData.setProductID(pi);
					try {
						traceDataService.create(traceData);
					} catch (ServiceException e) {
						e.printStackTrace();
					}

				}
			}
			count = searchDao.countProductByCondition(_keyword, category,
					catLevel, enterpriseName, brand, nutriLabel, ordername);
			page = new PageUtil(count, pageNum, pageSize);
			productList = searchDao.getListProductsByCondition(_keyword,
					enterpriseName, category, catLevel, brand, ordername,
					ordertype, page.getStartindex(), page.getPagesize(),
					nutriLabel);
			page.setCode(keyword);
		}
		/* 将List<Object[]> 封装成List<Product4SearchVO> */
		List<Product4SearchVO> product4SearchVOs = pottedProductToVO(
				productList, "v2.1.5");
		page.setList(product4SearchVOs);
		return page;
	}

	/**
	 * 对图片尺寸按要求进行处理(目前只针对手机APP)
	 * 
	 * @updater TangXin 2015/03/31
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SearchResult getSearchResult(String keyword, String category,
			Integer catLevel, String brand, String feature, String order,
			Integer pageNum, Integer pageSize, String nutriLabel,
			String version, int width, int height)
					throws UnsupportedEncodingException {
		/* 1.获取产品列表 */
		SearchResult searchResult = null;
		if ("v2.1.5".equals(version)) {
			searchResult = getSearchProduct(keyword, category, catLevel, brand,
					feature, order, pageNum, pageSize, "", true, nutriLabel);
		} else {
			searchResult = getSearchResult(keyword, category, catLevel, brand,
					feature, order, pageNum, pageSize, "", true, nutriLabel);
		}
		List<Product4SearchVO> products = (List<Product4SearchVO>) searchResult
				.getPage().getList();
		/* 2.按规则重新生成产品图片链接地址 */
		for (Product4SearchVO product : products) {
			String new_imgUrl = "";
			String old_imgUrls = product.getImgUrl();
			String[] old_imgUrls_array = old_imgUrls.split("\\|");
			for (int i = 0; i < old_imgUrls_array.length; i++) {
				new_imgUrl += ImgUtils.getImgPath(old_imgUrls_array[i], width,
						height) + "|";
			}
			product.setImgUrl(new_imgUrl);
		}
		/* 3.返回结果 */
		return searchResult;
	}

	public List<ProductCategory> getCategoryList() {
		return searchDao.getCategory();
	}

	/**
	 * 获取所有分类信息，提供给手机app使用
	 * 
	 * @param version
	 *            App 版本号,老版本为空
	 * @return List<ProductCategoryVO>
	 * @author TangXin
	 */
	public List<ProductCategoryVO> getCategoryListToApp(String appVersion) {
		try {
			/* 商品一级分类 */
			List<ProductCategoryVO> firstProductCategory = searchDao
					.getFirstCategory();
			if (firstProductCategory == null) {
				return null;
			}
			for (ProductCategoryVO fpc : firstProductCategory) {
				/* 商品二级分类 */
				List<ProductCategoryVO> secondProductCategoryVO = searchDao
						.getChildrenCategory(fpc.getCode());
				if (secondProductCategoryVO != null) {
					for (ProductCategoryVO spc : secondProductCategoryVO) {
						/* 商品的三级分类 */
						if (appVersion == null || "".equals(appVersion)) {
							spc.setChildren(searchDao.getChildrenCategory(spc
									.getCode()));
						} else {
							spc.setChildren(searchDao.getThridCategory(spc
									.getId()));
						}
					}
					fpc.setChildren(secondProductCategoryVO);
				}
			}
			return firstProductCategory;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取排序条件，默认然后综合降序排列(不返还null值)
	 * 
	 * @author TangXin 2015/03/30
	 */
	private Map<String, String> getOrderCondition(String order) {
		Map<String, String> oderCondi = new HashMap<String, String>();
		if (order == null || "".equals(order)) {
			oderCondi.put("ordername", "综合");
			oderCondi.put("ordertype", "asc");
			return oderCondi;
		}
		if ("00".equals(order)) {
			oderCondi.put("ordername", "综合");
			oderCondi.put("ordertype", "asc");
		} else if ("01".equals(order)) {
			oderCondi.put("ordername", "综合");
			oderCondi.put("ordertype", "desc");
		} else if ("10".equals(order)) {
			oderCondi.put("ordername", "企业自检");
			oderCondi.put("ordertype", "asc");
		} else if ("11".equals(order)) {
			oderCondi.put("ordername", "企业自检");
			oderCondi.put("ordertype", "desc");
		} else if ("20".equals(order)) {
			oderCondi.put("ordername", "企业送检");
			oderCondi.put("ordertype", "asc");
		} else if ("21".equals(order)) {
			oderCondi.put("ordername", "企业送检");
			oderCondi.put("ordertype", "desc");
		} else if ("30".equals(order)) {
			oderCondi.put("ordername", "政府抽检");
			oderCondi.put("ordertype", "asc");
		} else if ("31".equals(order)) {
			oderCondi.put("ordername", "政府抽检");
			oderCondi.put("ordertype", "desc");
		} else if ("40".equals(order)) {
			oderCondi.put("ordername", "风险指数");
			oderCondi.put("ordertype", "asc");
		} else if ("41".equals(order)) {
			oderCondi.put("ordername", "风险指数");
			oderCondi.put("ordertype", "desc");
		}
		return oderCondi;
	}

	/**
	 * 新加的产品搜索接口
	 * 
	 * @author tangxin 2015/03/31
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SearchResult getSearchProduct(String keyword, String category,
			Integer catLevel, String brand, String feature, String order,
			Integer pageNum, Integer pageSize, String enterpriseName,
			boolean isApp, String nutriLabel){
		try{
			boolean isNull = false;
			if (keyword.equals("") && category.equals("") && brand.equals("")
					&& "".equals(nutriLabel) && "".equals(enterpriseName)) {
				isNull = true;
			}
			/* 1.处理参数 */
			nutriLabel = StringUtil.isBlank(nutriLabel) ? "" : StringUtil
					.getUTF8Code(nutriLabel);
			keyword = StringUtil.isBlank(keyword) ? "" : StringUtil
					.getUTF8Code(keyword);
			category = StringUtil.isBlank(category) ? "" : category;
			brand = StringUtil.isBlank(brand) ? "" : StringUtil.getUTF8Code(brand);
			feature = StringUtil.isBlank(feature) ? "" : StringUtil
					.getUTF8Code(feature);
			order = StringUtil.isBlank(order) ? DEFAULT_ORDER : order; // 处理排序信息
			// 默认按照综合降序
			pageNum = StringUtil.isBlank(pageNum) ? DEFAULT_PAGENUM : pageNum; // 处理分页页码信息
			// 默认第一页
			String ordertype = ""; // 升序 or 降序

			/* 2.解析排序信息 */
			Map<String, String> orderCondition = getOrderCondition(order);
			String ordername = orderCondition.get("ordername"); // 排序的字段名
			ordertype = orderCondition.get("ordertype"); // 升序或降序排列

			/* 3.查询符合条件的数据 */
			List<CategoryVO> categoryList = new ArrayList<CategoryVO>();
			List<BrandVO> brandVOList = new ArrayList<BrandVO>();
			List<CategoryVO> navigation = new ArrayList<CategoryVO>();
			CategoryVO selectedCategory = null;

			/* 4.获取page */
			PageUtil page = searchProduct(enterpriseName, keyword, category,
					catLevel, brand, ordername, ordertype, pageNum, pageSize,
					nutriLabel, isNull, isApp);

			/* 4.1.如果查询的数据列表为空，将查询总记录赋值为0 */
			if (page != null
					&& (page.getList() == null || page.getList().size() == 0)
					&& page.getTotalrecord() == 1) {
				page.setTotalrecord(0);
			}
			/* 非手机app，即大众门户,需要返回分类和商标信息 */
			if (!isApp) {
				if (isNull) {
					/* 所有的筛选条件为空时，获取所有的分类信息和品牌信息 */
					categoryList = searchDao.getALLFirstCategoryVO();
					brandVOList = searchDao.getAllBrandList();
				} else {
					/* 当搜索参数只有category时,对产品分类单独查询 */
					if (!"".equals(category) && "".equals(keyword)
							&& "".equals(enterpriseName) && "".equals(nutriLabel)) {
						Map<String, Object> mapBrand = searchDao
								.getListBrandAndCategoryByCondition(keyword,
										category, catLevel, brand, enterpriseName,
										nutriLabel);
						brandVOList = (List<BrandVO>) mapBrand.get("brandVOList");
						/* 如果筛选的是三级分类，则将一级分类和二级分类查出来 */
						if (catLevel != null && catLevel == 3) {
							Map<String, Object> map = searchDao
									.getFirstAndSecondAndThridCategoryByThridId(category);
							if (map != null) {
								navigation.add((CategoryVO) map
										.get("firstCategory"));
								navigation.add((CategoryVO) map
										.get("secondCategory"));
								// navigation.add((CategoryVO)map.get("thirdCategory"));
								selectedCategory = (CategoryVO) map
										.get("thirdCategory");
							}
						} else {
							/* 如果是二级分类，将一级分类查出来 */
							if (catLevel == 2) {
								CategoryVO firstCategory = searchDao
										.getCategoryByCode(
												category.substring(0, 2), catLevel);
								navigation.add(firstCategory);
							}
							/* 查询选择的分类 */
							selectedCategory = searchDao.getCategoryByCode(
									category, catLevel);
							/* 获取下一级分类的集合 */
							categoryList = searchDao.getNextCategory(category,
									catLevel);
						}
						navigation.add(selectedCategory);
					} else {
						/* 当搜索条件不只产品类别时，按条件查询分类和品牌 */
						Map<String, Object> map = searchDao
								.getListBrandAndCategoryByCondition(keyword,
										category, catLevel, brand, enterpriseName,
										nutriLabel);
						List<CategoryVO> firstCategory = searchDao
								.getListFirstCategoryByCondition(keyword, category,
										catLevel, brand, enterpriseName, nutriLabel);
						brandVOList = (List<BrandVO>) map.get("brandVOList");
						Map<String, Object> navigationMap = getNavigation(map,
								firstCategory);
						// navigation = (List<CategoryVO>)
						// navigationMap.get("navigation");
						categoryList = firstCategory;// (List<CategoryVO>)
						// navigationMap.get("categoryList");
						selectedCategory = (CategoryVO) navigationMap
								.get("selectedCategory");
						// 获取selectedCategory
						if (!"".equals(category)) {
							selectedCategory = searchDao.getCategoryByCode(
									category, catLevel);
						}
						/* 产品为空时查询当前分类的父级分类 */
						if (navigation.size() == 0) {
							/* 如果筛选的是三级分类，则将一级分类和二级分类查出来 */
							if (catLevel != null && catLevel == 3) {
								Map<String, Object> maps = searchDao
										.getFirstAndSecondAndThridCategoryByThridId(category);
								if (maps != null) {
									navigation.add((CategoryVO) maps
											.get("firstCategory"));
									navigation.add((CategoryVO) maps
											.get("secondCategory"));
									categoryList.clear();
								}
							} else if (catLevel == 2) {/* 如果是二级分类，将一级分类查出来 */
								/* 产品第三级分类 */
								categoryList = (List<CategoryVO>) map
										.get("thirdCategory");
								CategoryVO firstCategorys = searchDao
										.getCategoryByCode(
												category.substring(0, 2), catLevel);
								navigation.add(firstCategorys);
							} else if (catLevel == 1 && !"".equals(category)) {
								categoryList = (List<CategoryVO>) map
										.get("secondCategory");
							}
							if (selectedCategory != null) {
								navigation.add(selectedCategory);
							}
						}
					}
				}
				if (selectedCategory == null) {
					selectedCategory = new CategoryVO();
				}
			}

			SearchResult searchResult = new SearchResult(categoryList, brandVOList,
					keyword, order, navigation, null, feature, page,
					selectedCategory, brand);
			return searchResult;
		}catch(Exception e){
			e.printStackTrace();
			return new SearchResult();
		}
	}

	/**
	 * 大众门户搜索接口，获取导航信息
	 * 
	 * @author tangxin 2015/04/01
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getNavigation(Map<String, Object> map,
			List<CategoryVO> firstCategory) {
		/* 产品第二级分类 */
		List<CategoryVO> secondCategory = (List<CategoryVO>) map
				.get("secondCategory");
		/* 产品第三级分类 */
		List<CategoryVO> thirdCategory = (List<CategoryVO>) map
				.get("thirdCategory");
		/* 已选择的品牌 */
		CategoryVO selectedCategory = null;
		/* 导航信息 */
		List<CategoryVO> navigation = new ArrayList<CategoryVO>();
		/* 当前方法的返回数据 */
		Map<String, Object> resutlMap = new HashMap<String, Object>();
		if (firstCategory.size() == 1) {
			/* 一级分类的数量为1，则选中一级分类 */
			selectedCategory = firstCategory.get(0);
		} else {
			/* 一级分类的数量为大于1，则返回一级分类为搜索的分类 */
			resutlMap.put("categoryList", firstCategory);
		}
		if (secondCategory.size() == 1) {
			navigation.add(firstCategory.get(0));
			selectedCategory = secondCategory.get(0);
		} else if (firstCategory.size() == 1) {
			navigation.add(firstCategory.get(0));
			resutlMap.put("categoryList", secondCategory);
		}
		if (thirdCategory.size() == 1) {
			navigation.add(secondCategory.get(0));
			selectedCategory = thirdCategory.get(0);
		} else if (firstCategory.size() == 1 && secondCategory.size() == 1) {
			navigation.add(secondCategory.get(0));
			resutlMap.put("categoryList", thirdCategory);
		}
		resutlMap.put("navigation", navigation);
		resutlMap.put("selectedCategory", selectedCategory);
		return resutlMap;
	}

	@Override
	public SearchDao getDAO() {
		return searchDao;
	}
}