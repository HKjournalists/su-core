package com.lhfs.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_HOTPRODUCTS_BY_ID;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_HOTPRODUCTS_BY_IDS;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_HOTPRODUCTS_TOTAL_BY_ID;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_HOTPRODUCTS_TOTAL_BY_IDS;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_PRODUCTINFO_APP;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_PRODUCT_BY_CATEGORY;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_REPORT_CENSOR;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_REPORT_SAMPLE;
import static com.lhfs.fsn.cache.EhCacheUtils.CACHE_REPORT_SELF;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.market.StoreProduct;
import com.gettec.fsnip.fsn.model.product.MapProduct;
import com.gettec.fsnip.fsn.model.product.NutritionReport;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.erp.PurchaseorderInfoService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.market.StoreProductService;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.product.MapProductService;
import com.gettec.fsnip.fsn.service.product.ProductRecommendUrlService;
import com.gettec.fsnip.fsn.service.product.ProductStdCertificationService;
import com.gettec.fsnip.fsn.service.thirdreport.ThirdReportService;
import com.gettec.fsnip.fsn.service.trace.TraceDataProductNameService;
import com.gettec.fsnip.fsn.service.trace.TraceDataService;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.StoreProductVO;
import com.gettec.fsnip.fsn.web.IWebUtils;
import com.lhfs.fsn.cache.EhCacheFactory;
import com.lhfs.fsn.service.business.BusinessUnitService;
import com.lhfs.fsn.service.product.ProductInstanceService;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.util.CharUtil;
import com.lhfs.fsn.util.DateUtil;
import com.lhfs.fsn.vo.TraceabilityVO;
import com.lhfs.fsn.vo.product.ProInfoAndTraInfo;
import com.lhfs.fsn.vo.product.ProductIdAndNameVO;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductInfoVOToPortal;
import com.lhfs.fsn.vo.product.ProductVOWda;
import com.lhfs.fsn.vo.product.SearchProductVO;
import com.lhfs.fsn.vo.report.ReportVO;
import com.lhfs.fsn.vo.report.UpdataReportVO;
import com.lhfs.fsn.web.controller.RESTResult;

import net.sf.ehcache.Element;

@Controller
@RequestMapping("/portal/product")
public class ProductController {
	@Autowired private ProductService productLFService;
	@Autowired private StatisticsClient staClient;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductInstanceService productInstanceService;
	@Autowired private TestReportService testReportService;
	@Autowired private PurchaseorderInfoService purchaseorderInfoService;
	@Autowired private ProductStdCertificationService productStdCertificationService;
	@Autowired private UpdataReportService updataReportService;
	@Autowired private StoreProductService storeProductService;
	@Autowired private ResourceService resourceService;
	@Autowired private ProductRecommendUrlService recommendUrlService;
	@Autowired private TraceDataProductNameService traceDataProductNameService;
	@Autowired private TraceDataService  traceDataService;
	@Autowired private ThirdReportService thirdReportService;
	@Autowired private MapProductService mapProductService;

	/**
	 * portal接口：产品详情信息<br>
	 * 最后更新：ZhangHui 2015/5/8
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public RESTResult<Product> getProductById(
			@PathVariable long id,
			@RequestParam(value="sysType", required = false) String sysType, 
			@RequestParam(value="userId", required = false) Long userId,
			@RequestParam(value="code",required=false) String code,
			HttpServletRequest request) {
		Product prod = null;
		try {	
			String from=request.getHeader("Referer");
			String data = id + "_" + IWebUtils.APP_FLAG;
			if(from!=null && (!from.equals("")) && from.contains("fsn-portal")){
				data = id + "_" + IWebUtils.PORTAL_FLAG;
			}
			if(sysType==null){
				prod = productLFService.findProductById(id);
			}else if(sysType.equals("app")){
				Element result = (Element) EhCacheFactory.getCacheElement(CACHE_PRODUCTINFO_APP + "_" + id);
				if(result == null){
					prod = productLFService.findProductById(id, 320, 320, 128, 128, 640, 960);
					EhCacheFactory.put(CACHE_PRODUCTINFO_APP + "_" + id, prod);
				}else{
					prod = (Product) result.getObjectValue();
				}
			}
			if (prod == null) {
				return new RESTResult<Product>(0, "该物品不存在！");
			}
			/* 判断产品是否被该用户收藏 */
			if(userId!=null){
				Long StoreProductid = storeProductService.findStorebyUser4ProductId(id, userId);
				if(StoreProductid > 0){
					prod.setEnshrine(true);
				}else{
					prod.setEnshrine(false);
				}
			}
			/*处理产品图片*/
			List<Resource> imgList = resourceService.getProductImgListByproId(id);//查找产品图片集合
			//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
			if(imgList==null||imgList.size()==0){
				if(prod.getImgUrl() != null){
					String[] imgUrlListArray = prod.getImgUrl().split("\\|");
					for(String url:imgUrlListArray){
						Resource re=new Resource();
						re.setUrl(url);
						imgList.add(re);
					}
				}
			}
			prod.setProUrlList(recommendUrlService.getProductRecommendUrl(id));
			//获取推荐购买链接
			prod.setImgList(imgList);
			prod.setProductCertification(productStdCertificationService.getListOfStandCertificationByProductId(prod.getId()));
			staClient.offer(data);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		//如果存在追溯信息,追加追溯信息
		if(code!=null){
			TraceData _tradeData=traceDataProductNameService.isKeywordTraceData(code);
			prod.setTraceData(_tradeData);
		}
		return new RESTResult<Product>(1, prod);
	}
	/**
	 * 对产品图片按日期进行升序排序
	 * @param arraylist
	 * @author Longxianzhen 2015/06/02
	 */
	public static void sort(List<Resource> arraylist){ //实现冒泡算法

		for(int i=0;i<arraylist.size();i++) {
			for(int j=i+1;j<arraylist.size();j++) {
				if(arraylist.get(i).getUploadDate()!=null&&arraylist.get(j).getUploadDate()!=null){
					Resource a;
					if((arraylist.get(i)).getUploadDate().compareTo(arraylist.get(j).getUploadDate())>0) {   //比较两个日期的大小
						a=arraylist.get(i);
						arraylist.set((i),arraylist.get(j));
						arraylist.set(j,a);
					}
				}

			}
		}
	}

	/**
	 * 获取热点产品
	 * @param page
	 * @param pageSize
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getHotProduct/{page}/{pageSize}")
	public View getListOfHotProduct(@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize,
			@RequestParam(value="id", required=false) String productIds,
			@RequestParam(value="productId", required=false) Long proId,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		long total = 0L;
		List<SearchProductVO> products = null;
		try{
			/* 1. 根据id获取指定的一条热点产品 */
			if(proId!=null){
				Element products_element = (Element) EhCacheFactory.getCacheElement(CACHE_HOTPRODUCTS_BY_ID + "_" + page + pageSize + proId);
				if(products_element == null){
					products = productLFService.getHotProductByProductId(proId);
					EhCacheFactory.getInstance().getCache().put(new Element(CACHE_HOTPRODUCTS_BY_ID + "_" + page + pageSize + proId, products));
					total = products!=null?products.size():0L;
					//EhCacheFactory.put(CACHE_HOTPRODUCTS_TOTAL_BY_ID, total);
				} else {
					Element total_element = (Element) EhCacheFactory.getCacheElement(CACHE_HOTPRODUCTS_TOTAL_BY_ID);
					total = Long.parseLong(total_element.getObjectValue().toString());
					products = (List<SearchProductVO>) products_element.getObjectValue();
				}
			}else{
				/* 2. 获取热点产品列表 */
				Element products_element = (Element) EhCacheFactory.getCacheElement(CACHE_HOTPRODUCTS_BY_IDS + "_" + page + pageSize + (productIds==null?"":productIds));
				if(products_element == null){
					total = productLFService.countOfHotProduct();
					products = productLFService.getListOfHotProductWithPage(productIds, page, pageSize);
					EhCacheFactory.getInstance().getCache().put(new Element(CACHE_HOTPRODUCTS_BY_IDS + "_" + page + pageSize + (productIds==null?"":productIds), products));
					//EhCacheFactory.put(CACHE_HOTPRODUCTS_TOTAL_BY_IDS, total);
				}else{
					Element total_element = (Element) EhCacheFactory.getCacheElement(CACHE_HOTPRODUCTS_TOTAL_BY_IDS);
					total = Long.parseLong(total_element.getObjectValue().toString());
					products = (List<SearchProductVO>) products_element.getObjectValue();
				}
			}
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("data", products);
		model.addAttribute("total", total);
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * 给portal提供接口：已知产品类别，查找报告最多的若干个产品（并按报告数量做排序）
	 * @param page
	 * @param pageSize
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getListOfProductByCategory/{category}/{page}/{pageSize}")
	public View getListOfReport(@PathVariable(value="category") String category,@PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			List<SearchProductVO> listOfProduct = null;
			Element products_element = (Element) EhCacheFactory.getCacheElement(CACHE_PRODUCT_BY_CATEGORY + "_" + category + page + pageSize);
			if(products_element == null){
				listOfProduct = productLFService.getListOfProductByCategory(category,page, pageSize);
				EhCacheFactory.getInstance().getCache().put(new Element(CACHE_PRODUCT_BY_CATEGORY + "_" + category + page + pageSize, listOfProduct));
			} else {
				listOfProduct = (List<SearchProductVO>) products_element.getObjectValue();
			}
			model.addAttribute("data", listOfProduct);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}


	/*********提供个fdams监管系统的《移动巡查》接口***************
	 * 根据商品名称或是条码，或是名称+条码得到相关商品
	 * Author：cxl
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListOfProduct/{page}/{pageSize}")
	public View getListOfProduct( @PathVariable(value="page") int page,@PathVariable(value="pageSize") int pageSize,
			@RequestParam("name") String name, @RequestParam("barcode") String barcode,@RequestParam("orgId") long orgId,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		List<ProductInfoVO> productInfoVOs = new ArrayList<ProductInfoVO>();
		ProductInfoVO productInfoVO = null;
		BusinessUnit businessUnit = null;
		try {
			List<Product> productList = productLFService.getListOfProduct(page,pageSize,name,barcode,orgId);
			long count  = productLFService.getListOfProductCount(name,barcode,orgId);
			if (productList!=null) {
				for (Product product : productList) {
					Long org = product.getOrganization();
					if(org!=null){
						businessUnit = businessUnitService.findBusinessByOrg(product.getOrganization());
						if (businessUnit != null) {
							productInfoVO = new ProductInfoVO();
							productInfoVO.setBusinessName(businessUnit.getName());
							productInfoVO.setBarcode(product.getBarcode());
							productInfoVO.setExpiration(product.getExpiration());
							productInfoVO.setFormat(product.getFormat());
							productInfoVO.setName(product.getName());
							productInfoVO.setId(product.getId());
							productInfoVOs.add(productInfoVO);
						}
					}
				}
			}
			model.addAttribute("totalSize", count);
			model.addAttribute("data", productInfoVOs);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/*********提供个fdams监管系统的《移动巡查》接口***************
	 * 根据批次或是生产日期范围，得到相关商品
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListProductByBatchOrProductDate/{pid}/{page}/{pageSize}")
	public View getListProductByBatchOrProductDate( @PathVariable(value="pid") long pid,@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize,
			@RequestParam("batch") String batch, 
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			Long count  = productLFService.getListOfProductCountByBatchOrProductDate(batch,startTime,endTime,pid);
			List<ProductInfoVO> listOfProduct = productLFService.getListOfProductByBatchOrProductDate(page,pageSize,batch,startTime,endTime,pid);
			List<ProductInfoVO> list = new ArrayList<ProductInfoVO>();
			for(ProductInfoVO infoVO : listOfProduct){
				boolean flag;
				if(!infoVO.getBatch().equals("")){
					flag = judgeReportOverDate(pid,infoVO.getBatch());
					infoVO.setReportOverDate(flag);
				}else{
					infoVO.setReportOverDate(true);//批次为空值时暂时设置为过期(true)
				}
				list.add(infoVO);
			}

			model.addAttribute("totalSize", count);
			model.addAttribute("data", list);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**********提供个fdams监管系统的《移动巡查》接口**********
	 * 根据企业营养执照号或是企业名称，获取该企业下的产品
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getProductListForBusinessUnit")
	public View getProductListForBusinessUnit(@RequestParam(value="name") String name,@RequestParam(value="licenseNo") String licenseNo,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			List<ProductInfoVO> ProductList = productLFService.loadProductListForBusinessUnit(name,licenseNo);
			model.addAttribute("data", ProductList);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**********提供给fdams监管系统的《移动巡查》接口**********
	 * 根据id获取商品实例
	 * author:cxl
	 * @param req
	 * @param resp
	 * @param model
	 * @param id 产品ID
	 * @return JSON
	 */
	@RequestMapping(method=RequestMethod.GET,value="/getProductInstenc/{page}/{pageSize}/{id}")
	public View getProductInstenceById(@PathVariable(value="page")int page,@PathVariable(value="pageSize")int pageSize,@PathVariable(value="id")Long id, 
			HttpServletRequest req, HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();
		try {
			Product product = productLFService.findById(id);
			List<ProductInstance> productInstanceList = productLFService.getProductInstanceListById(page,pageSize,id);
			List<ProductInfoVO> productInfoVOs = new ArrayList<ProductInfoVO>();
			Long count  = productLFService.getCountByproId(id);
			for (ProductInstance productInstance : productInstanceList) {
				boolean flag;
				ProductInfoVO productInfoVO = new ProductInfoVO();
				productInfoVO.setId(productInstance.getId());
				productInfoVO.setName(product.getName());
				productInfoVO.setBarcode(product.getBarcode());
				productInfoVO.setExpiration(product.getExpiration());
				productInfoVO.setBatch(productInstance.getBatchSerialNo());
				if(!productInstance.getBatchSerialNo().equals("")){
					flag = judgeReportOverDate(id,productInstance.getBatchSerialNo());
					productInfoVO.setReportOverDate(flag);
				}else{
					productInfoVO.setReportOverDate(true);//批次为空值时暂时设置为过期(true)
				}
				productInfoVOs.add(productInfoVO);
			}
			model.addAttribute("totalSize", count);
			model.addAttribute("data", productInfoVOs);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 判断该实例的报告是否过期
	 * @param id 产品id
	 * @param batch 批次
	 * @return boolean true 过期    ;false  没有过去
	 */
	private boolean judgeReportOverDate( Long id, String batch){
		Boolean overDate = null;
		try {
			Map<String,List<ReportVO>> instanceMap = testReportService.getInstanceReport(id,batch); 
			if((instanceMap.get("reportVOSelfList").size()<1) && (instanceMap.get("reportVOcensorList").size()<1) ){
				overDate = true;
			}
			boolean selfReportDate = getNearReportEndDate(instanceMap.get("reportVOSelfList"));//自检       true 过期    false  没有过去
			boolean selfOcensorDate = getNearReportEndDate(instanceMap.get("reportVOcensorList"));//送检
			if(selfReportDate && selfOcensorDate){
				overDate = true;
			}else{
				overDate = false;
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return overDate;
	}

	/**
	 * 获取报告列表中最近的医疗报告的过期时间
	 * @param list
	 * @return
	 */
	private boolean getNearReportEndDate(List<ReportVO> list) {
		//将报告按检查时间从最早到最近排序
		for (int i = 0; i < list.size()-1; i++) {
			if((list.get(i)).getTestDate() == null){
				continue;
			}
			for (int j = i + 1; j < list.size(); j++) {
				if((list.get(j)).getTestDate() == null){
					continue;
				}
				if (j>1&&(list.get(i)).getTestDate().after(list.get(j).getTestDate()) ) {
					ReportVO report = list.get(j);
					ReportVO temp = list.get(i);
					list.remove(i);
					list.add(i, report);
					list.remove(j);
					list.add(j, temp);
				}
			}
		}
		if(list.size()>0){
			Date expireDate = list.get(list.size()-1).getExpireDate();// 报告过时时间
			if(expireDate!=null && expireDate.after(new Date())) {
				return false; //没有过期
			}else{
				return true;  //过期
			}
		}
		return true;
	}

	/*********提供个fdams监管系统的《移动巡查》接口***************
	 * 根据产品实例Id获取报告信息
	 * Author：cxl
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getReportInfoById/{id}")
	public View getReportBySampleId( @PathVariable(value="id") long id,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		ProductInfoVO productInfoVO = null;
		try {
			if(id>0){
				ProductInstance productInstance = productInstanceService.findById(id);
				if(productInstance!=null){
					Product product = productLFService.findById(productInstance.getProduct().getId());
					if(product!=null){
						productInfoVO = new ProductInfoVO();
						productInfoVO.setId(productInstance.getId());
						productInfoVO.setName(product.getName());
						productInfoVO.setBarcode(product.getBarcode());
						productInfoVO.setFormat(product.getFormat());
						productInfoVO.setBusinessBrand(product.getBusinessBrand().getName());
						productInfoVO.setExpiration(product.getExpiration());
						productInfoVO.setBatch(productInstance.getBatchSerialNo());
					}
				}
				model.addAttribute("data", productInfoVO);
			}
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**********提供个fdams监管系统的《移动巡查》接口**********
	 * 1、根据产品实例获取商品名称、商品规格、批次
	 * Author：cxl
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getProductAndProductInstenceById/{id}")
	public View getProductAndProductInstenceById(@PathVariable(value="id") long id,HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		ProductInfoVO productInfoVO = null;
		try{
			if(id>0){
				ProductInstance productInstance = productInstanceService.findById(id);
				if(productInstance!=null){
					Product product = productLFService.findById(productInstance.getProduct().getId());
					if(product!=null){
						productInfoVO = new ProductInfoVO();
						productInfoVO.setId(productInstance.getId());//产品实例Id
						productInfoVO.setName(product.getName());//商品名称
						productInfoVO.setFormat(product.getFormat());//规格
						productInfoVO.setBatch(productInstance.getBatchSerialNo());//批次
					}
				}
			}
			model.addAttribute("data", productInfoVO);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 监管系统获取超市没有报告产品
	 * @param organization
	 * @param page
	 * @param pageSize
	 * @param model
	 * @param flag 是否分页查询 true 分页
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET ,value = "/getMarketProducts/{flag}/{organization}/{isHaveReport}/{page}/{pageSize}")
	public View getMarketProducts(@PathVariable Boolean flag, @PathVariable Long organization, @PathVariable boolean isHaveReport, @PathVariable int page, 
			@RequestParam("name") String name,@RequestParam("barcode") String barcode,@PathVariable int pageSize,Model model){
		ResultVO resultVO = new ResultVO();
		String proName = "";
		try {
			if(!name.equals("")){
				name=URLDecoder.decode(name, "UTF-8");
				if(name.contains(" ")){
					proName = name.replace(" ", "+");
				}else{
					proName = name;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try{
			long counts = productLFService.countMarketProduct(organization, isHaveReport,proName,barcode);
			List<ProductVOWda> listProduct = productLFService.getListOfMarketByMarketIdWithPage(flag,organization, isHaveReport, page, pageSize,proName,barcode);
			//判断所有批次是否过期
			/*if(listProduct!=null){
				//for (ProductVOWda productVOWda : listProduct) {
				for (int j = 0 ; j< listProduct.size() ; j++) {
					long proId = listProduct.get(j).getId();
					List<ProductInstance> listProductInstence = productInstanceService.getProductInstenceListByProId(proId);
					if (listProductInstence != null) {
						//for (ProductInstance productInstance : listProductInstence) {
						List list = new ArrayList();
						for (int i = 0 ; i< listProductInstence.size() ; i++) {
							String batch = listProductInstence.get(i).getBatchSerialNo();
							if ( !batch.equals("")) {
								boolean allBatchExprie = judgeReportOverDate(proId,batch);
								list.add(allBatchExprie);
								if(!allBatchExprie){
									break;
								}
							}else{list.add(true);}
						}
						boolean haveExprie = list.contains(true);
						boolean noExprie = list.contains(false);
						if (haveExprie && !noExprie) {//全部过期才变红
							//productVOWda.setAllBatchExpire(true);
							listProduct.get(j).setAllBatchExpire(true);
						}else{//其他情况不变红
							//productVOWda.setAllBatchExpire(false);
							listProduct.get(j).setAllBatchExpire(false);
						}
					}
				}
			}*/
			model.addAttribute("data", listProduct == null ? new ArrayList<ProductVOWda>() : listProduct);
			model.addAttribute("counts", counts);
		}catch(Exception sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	} 

	/**
	 * 监管系统获取所有(包括有报告和无报告)产品
	 * @param organization
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 * Author:cxl
	 */
	@RequestMapping(method = RequestMethod.GET ,value = "/getMarketAllProducts/{organization}/{page}/{pageSize}")
	public View getMarketAllProducts(@PathVariable Long organization,@PathVariable int page, @PathVariable int pageSize,Model model){
		ResultVO resultVO = new ResultVO();
		List<ProductVOWda> listProductVOWda = null;
		ProductVOWda productVOWda = null;
		try{
			List<Product> listProduct = productLFService.getListOfMarketAllProductsWithPage(organization,page, pageSize);
			long counts = productLFService.countMarketAllProduct(organization);
			if(listProduct!=null){
				listProductVOWda=new ArrayList<ProductVOWda>();
				for (Product product : listProduct) {
					productVOWda = new ProductVOWda();
					productVOWda.setId(product.getId());
					productVOWda.setName(product.getName());
					productVOWda.setBarcode(product.getBarcode());
					productVOWda.setExpiration(product.getExpiration());
					productVOWda.setFormat(product.getFormat());
					listProductVOWda.add(productVOWda);
				}
			}
			model.addAttribute("data", listProductVOWda == null ? null: listProductVOWda);
			model.addAttribute("counts", counts);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	} 

	/**
	 * 监管接口，查询超市没有生产许可证的产品列表
	 * @param organization
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET ,value = "/getMarketNoneProlicinfoProducts/{flag}/{organization}/{page}/{pageSize}")
	public View getMarketNoneProlicinfoProducts(@PathVariable Boolean flag,@PathVariable Long organization,
			@RequestParam("name") String name,@RequestParam("barcode") String barcode,@PathVariable int page, @PathVariable int pageSize,Model model){
		ResultVO resultVO = new ResultVO();
		try {
			name=URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try{
			long counts = productLFService.countNoneProlicinfoByOrgId(organization,name,barcode);
			List<ProductVOWda> listProduct = productLFService.getListOfMarketNoneProlicinfoByOrgIdWithPage(flag,organization, page, pageSize,name,barcode);
			model.addAttribute("data", listProduct == null ? new ArrayList<ProductVOWda>() : listProduct);
			model.addAttribute("counts", counts);
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	} 

	/**
	 * 监管接口：查询erp中对应的 产品实例
	 * @param proId
	 * @param page
	 * @param pageSize
	 * @param batch
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET ,value = "/getErpProductInstance/{proId}/{page}/{pageSize}")
	public View getErpProductInstance(@PathVariable Long proId,@PathVariable int page,@PathVariable int pageSize,
			@RequestParam("batch") String batch,@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,Model model){
		ResultVO resultVO = new ResultVO();
		try{
			List<ProductInfoVO> listInstance = null;
			long counts = 0;
			if((startTime.equals("")&&!endTime.equals(""))||(!startTime.equals("")&&endTime.equals(""))){
				model.addAttribute("data", listInstance == null ? new ArrayList<ProductVOWda>() : listInstance);
				model.addAttribute("totalSize", counts);
				resultVO.setMessage("时间段不能只输入一个");

			}else{
				counts = purchaseorderInfoService.countGetErpProductInstance(proId,batch,startTime,endTime);
				listInstance = purchaseorderInfoService.getErpProductInstanceByProductId( page, pageSize,proId,batch,startTime,endTime);
				model.addAttribute("data", listInstance == null ? new ArrayList<ProductVOWda>() : listInstance);
				model.addAttribute("totalSize", counts);
			}
		}catch(ServiceException sex){
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setMessage(sex.getMessage());
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 提供fdams 的详细溯源接口
	 * @param barcode
	 * @param batch
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = { "/getDetailTraceability/{barcode}" })
	public View getDetailTraceability(@PathVariable(value = "barcode") String barcode,@RequestParam("batch") String batch,HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			Product product = productLFService.findProductByBarcode(barcode);
			if(product !=null){
				List<ProInfoAndTraInfo> traceabilityLists = new ArrayList<ProInfoAndTraInfo>();
				List<List<TraceabilityVO>> traceability = businessUnitService.productDetailTraceability(barcode, batch);
				if (traceability.size() > 0) {
					for(int i = 0 ; i<traceability.size() ; i++) {
						ProInfoAndTraInfo traceabList = new ProInfoAndTraInfo();
						traceabList.setTraceabilityVO(traceability.get(i));
						traceabList.setpBarcode(product.getBarcode());
						traceabList.setpId(product.getId());
						traceabList.setpName(product.getName());
						traceabList.setFlagTime(new Long(i));
						traceabilityLists.add(traceabList);
					}

				}
				model.addAttribute("data",traceabilityLists);
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 根据指定商品列和值查询满足条件的商品列表
	 * @param queryName {字段名称}
	 * @param queryParam {参数}
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value="ap")
	public RESTResult<ProductIdAndNameVO> ajaxSearchProductByParam (@RequestParam("queryName") String queryName, @RequestParam("queryParam") String queryParam) {
		try {
			queryParam = CharUtil.changeURLCode(queryParam, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return productLFService.searchProductInfoByPQ(queryName,queryParam);
	}

	/**
	 * Portal 接口:记录portal报告申请
	 * @author HuangYog
	 */
	@RequestMapping(method = RequestMethod.POST, value="/record")
	public View recordUpdateReportTimes (@RequestBody UpdataReportVO updataReportVO, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			//判断产品id和报告类型是否为空
			boolean pid = updataReportVO.getProductId() != null && !"".equals(updataReportVO.getProductId());
			boolean reportType = updataReportVO.getReportType() != null && !"".equals(updataReportVO.getReportType());
			/* 判断报告类型是否正常 */
			List<String> tyleLists = new ArrayList<String>();
			tyleLists.add("政府抽检");
			tyleLists.add("企业自检");
			tyleLists.add("企业送检");
			if (pid && reportType && tyleLists.contains(updataReportVO.getReportType())) {
				updataReportService.save(updataReportVO);
			}else {
				resultVO.setErrorMessage("企业类型不正确或是产品id不能为空");
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
		}catch(ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * 收藏/取消收藏产品接口
	 * @param id
	 * @param userid
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/{userid}/{type}")
	public View savStoreProduct(@PathVariable Long id,@PathVariable Long userid,@PathVariable Integer type, HttpServletRequest request, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			Product orig_product = productLFService.findById(id);
			if(orig_product==null){
				resultVO.setErrorMessage("无该产品");
				resultVO.setStatus(SERVER_STATUS_FAILED);
				model.addAttribute("result", resultVO);
				return JSON;
			}
			Long StoreProductid = storeProductService.findStorebyUser4ProductId(id, userid);
			/*取消收藏产品*/
			if(type == 0 ){
				if(StoreProductid !=null && StoreProductid !=0){
					storeProductService.delete(StoreProductid);
					resultVO.setErrorMessage("取消收藏产品成功");
					resultVO.setStatus(SERVER_STATUS_SUCCESS);
					model.addAttribute("result", resultVO);
					return JSON;
				}else{
					resultVO.setErrorMessage("取消收藏产品失败");
					resultVO.setStatus(SERVER_STATUS_FAILED);
					model.addAttribute("result", resultVO);
					return JSON;
				}
			}
			/*判断已经收藏该商品*/
			if(StoreProductid > 0){
				resultVO.setErrorMessage("已经收藏该商品");
				resultVO.setStatus(SERVER_STATUS_FAILED);
				model.addAttribute("result", resultVO);
				return JSON;
			}
			/*收藏*/
			StoreProduct storeProduct = new StoreProduct();
			storeProduct.setAddDate(new Date());
			storeProduct.setProductid(orig_product );
			Set<Resource> proAttachments = orig_product.getProAttachments();
			Iterator<Resource> iterator = proAttachments.iterator();
			if(iterator.hasNext()){
				Resource next = iterator.next();
				storeProduct.setProductImg(next.getUrl());
			}else {
				storeProduct.setProductImg("http://qa.fsnrec.com/portal/img/product/temp/temp.jpg");
			}
			storeProduct.setProductName(orig_product.getName());
			storeProduct.setUserId(userid);
			storeProductService.create(storeProduct );
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 用户中心查看收藏的产品接口
	 * @param size 每页的条数
	 * @param page 第几页
	 * @param productId 产品id
	 * @param userId 用户id
	 * @param startDate 查询报告更新时间的起始时间
	 * @param endDate 查询报告更新时间的最后时间
	 * @param nutrilabel 营养成分
	 * @param category 一级分类
	 * @throws Exception
	 */

	@RequestMapping(method = RequestMethod.GET,value = "search")
	public View searchStoreProduc(  @RequestParam Long userId,
			@RequestParam String startDate,
			@RequestParam String endDate,
			@RequestParam String nutritionLabel,
			@RequestParam String category,
			@RequestParam int pageSize,
			@RequestParam int page,
			HttpServletRequest request, Model model) throws Exception {
		ResultVO resultVO = new ResultVO();
		try {
			DateFormat sdfs = new SimpleDateFormat("yyyy/MM/dd");
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(startDate!=null && startDate != ""){
				boolean matches = DateUtil.matchesYYYYMMDD(startDate);
				if(!matches){
					resultVO.setMessage("时间格式不对");
					resultVO.setStatus(SERVER_STATUS_FAILED);
					model.addAttribute("result", resultVO);
					return JSON;
				}
				Date parse = sdf.parse(startDate);
				startDate = sdfs.format(parse);
			}
			if(endDate!=null && endDate != ""){
				boolean matches = DateUtil.matchesYYYYMMDD(endDate);
				if(!matches){
					resultVO.setMessage("时间格式不对");
					resultVO.setStatus(SERVER_STATUS_FAILED);
					model.addAttribute("result", resultVO);
					return JSON;
				}
				Date parse = sdf.parse(endDate);
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(parse);
				rightNow.add(Calendar.DAY_OF_YEAR,1);
				Date dt=rightNow.getTime();
				endDate = sdfs.format(dt);
			}
			if(userId == null || "".equals(userId) ||  "null".equals(userId)){
				model.addAttribute("result", resultVO);
				resultVO.setMessage("userId为空");
				resultVO.setStatus(SERVER_STATUS_FAILED);
				return JSON;
			}
			List<StoreProductVO> findStoreProduc = storeProductService.findStoreProduc(pageSize, page, userId, startDate, endDate, nutritionLabel, category);
			int storeCount = storeProductService.StoreCount(pageSize, page, userId, startDate, endDate, nutritionLabel, category);

			model.addAttribute("product", findStoreProduc);
			model.addAttribute("storeCount", storeCount);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * 批量收藏/取消收藏产品接口
	 * @param proIds 产品id 用逗号分隔的字符串
	 * @param userid 用户id
	 * @param type 收藏还是取消（1是收藏，0是取消收藏）
	 * @author LongXianZhen 2015/06/18
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/batchStoreProduct")
	public View savBathhStoreProduct(@RequestParam String proIds,@RequestParam Long userId,@RequestParam Integer type, 
			HttpServletRequest request, Model model) {
		ResultVO resultVO = new ResultVO();
		try {

			/*批量取消收藏产品*/
			if(type == 0 ){
				storeProductService.batchDelete(proIds,userId);
				resultVO.setErrorMessage("取消收藏产品成功");
				resultVO.setStatus(SERVER_STATUS_SUCCESS);
				model.addAttribute("result", resultVO);
				return JSON;

			}else{
				String[] proIdsArray=proIds.split(",");
				List<Long> proIdsList=new ArrayList<Long>();
				for(String bid:proIdsArray){
					proIdsList.add(Long.parseLong(bid));
				}
				for(Long proId:proIdsList){
					Product orig_product = productLFService.findById(proId);
					if(orig_product==null){
						continue;
					}
					Long storeProductid = storeProductService.findStorebyUser4ProductId(proId, userId);
					if(storeProductid>0){
						continue;
					}
					/*收藏*/
					StoreProduct storeProduct = new StoreProduct();
					storeProduct.setAddDate(new Date());
					storeProduct.setProductid(orig_product );
					Set<Resource> proAttachments = orig_product.getProAttachments();
					Iterator<Resource> iterator = proAttachments.iterator();
					if(iterator.hasNext()){
						Resource next = iterator.next();
						storeProduct.setProductImg(next.getUrl());
					}else {
						storeProduct.setProductImg("http://qa.fsnrec.com/portal/img/product/temp/temp.jpg");
					}
					storeProduct.setProductName(orig_product.getName());
					storeProduct.setUserId(userId);
					storeProductService.create(storeProduct );
				}
			}         
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * Portal 接口:根据产品id集合返回产品集合
	 * @author LongXianZhen 2015/06/25
	 */
	@RequestMapping(method = RequestMethod.GET, value="/getProducts")
	public View getProductByProIds (@RequestParam String proIds, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			List<ProductIdAndNameVO> pros=productLFService.getProductByProIds(proIds);
			model.addAttribute("list",pros);
		}catch(ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * Portal 接口:进口食品列表接口
	 * @author LongXianZhen 2015/07/01
	 */
	@RequestMapping(method = RequestMethod.GET, value="/importFood")
	public View getImportProductList(@RequestParam int page,@RequestParam int pageSize, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			List<ProductIdAndNameVO> pros=productLFService.getImportProductList(page,pageSize);
			int count=productLFService.getImportProductCount();
			int countOfReport=productLFService.getImportProductReportCount();
			model.addAttribute("data",pros);
			model.addAttribute("count",count);
			model.addAttribute("countOfReport",countOfReport);
		}catch(ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * Portal 接口:根据条形码查询食品详细接口
	 * @author LongXianZhen 2015/08/06
	 */
	@RequestMapping(method = RequestMethod.GET, value="/barcode")
	public View getProductInfoByBarcode(@RequestParam String barcode, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			ProductInfoVOToPortal pro=productLFService.getProductInfoByBarcode(barcode);
			pro.setShareUrl("http://www.fsnip.com/fsn-portal/product/read.shtml?id="+pro.getId());
			model.addAttribute("data",pro);
		}catch(Exception sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}

	/**
	 * Portal 接口:根据二维码id查询产品条形码
	 * @author LongXianZhen 2015/08/06
	 */
	@RequestMapping(method = RequestMethod.GET, value="/productQRcode/{QRcode}")
	public View getProductIdByQRcode( Model model,@PathVariable String QRcode,HttpServletRequest request) {
		ResultVO resultVO = new ResultVO();
		try {

			String proBarcode=productLFService.findProductBarcodeByQRcode(QRcode);
			String proId=productLFService.findProductIdByQRcode(QRcode);
			model.addAttribute("proBarcode",proBarcode);
			model.addAttribute("proId",proId);
		}catch(ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			sex.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}


	/**
	 * Portal 接口:根据产品id查询产品追溯信息接口
	 * @author xuetaoyang 2016/05/06
	 */
	@RequestMapping(method = RequestMethod.GET, value="/getTraceDatabyproductId")
	public  View  getTraceDatabyproductID( Model model, @RequestParam("productId") Long productId,
			@RequestParam(value="page",defaultValue="1",required=false) int page,
			@RequestParam(value="productDate",required=false) String productDate,
			HttpServletRequest request){
		TraceData traceData=null;
		if(productDate==null){
			traceData=traceDataService.findPagebyproductID(productId,page);
		}else{
			traceData=traceDataService.findPagebyproductIDandproductDate(productId, page, productDate);
		}
		long count=this.traceDataService.count(productId);
		model.addAttribute("traceData",traceData);
		model.addAttribute("count",count);
		return JSON;
	}
/**
	* Portal 接口:根据食安码查询产品信息接口
	 * @author zouhao 2016/06/08
	 */	
	@RequestMapping(method = RequestMethod.GET, value="/getProductByCode")
	public View  getProductByCode( Model model, @RequestParam("code") String code,HttpServletRequest request){
		try{
			long productId=0;
			if(code.length()==32){
			productId=Long.valueOf(code.substring(0,16));
			}else{
			productId=Long.valueOf(code);
			}
			Product prod = productLFService.findProductById(productId);
			/*处理产品图片*/
			List<Resource> imgList = resourceService.getProductImgListByproId(productId);//查找产品图片集合
			//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
			if(imgList==null||imgList.size()==0){
				if(prod.getImgUrl() != null){
					String[] imgUrlListArray = prod.getImgUrl().split("\\|");
					for(String url:imgUrlListArray){
						Resource re=new Resource();
						re.setUrl(url);
						imgList.add(re);
					}
				}
			}
			prod.setImgList(imgList);
			prod.setProductCertification(productStdCertificationService.getListOfStandCertificationByProductId(prod.getId()));
			model.addAttribute("product",prod);
		}catch(Exception e){
			e.printStackTrace();
		}
		return JSON;
	}
	/**
	* Portal 接口:根据食安码查询产品追溯信息接口
	 * @author xuetaoyang 2016/06/08
	 */	
	@RequestMapping(method = RequestMethod.GET, value="/getTraceDataByCode")
	public View  getTraceDataByCode( Model model, 
			@RequestParam("code") String code,
			HttpServletRequest request,
			@RequestParam(value="productdate",required=false) String productdate,
			@RequestParam(value="page",defaultValue="1",required=false) int page){
	//	TraceData traceData=null;
		long productId=0;
		if(code.length()==32&&(productdate==null||productdate.equals(""))){
		productId=Long.valueOf(code.substring(0,16));
		productdate=code.substring(16,24);
		}else{
		productId=Long.valueOf(code);
		}
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = sdf.parse(productdate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String productDate=new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
			TraceData	traceData=traceDataService.findPagebyproductIDandproductDate(productId, page, productDate);
			long count=this.traceDataService.count(productId);
			Product product=productLFService.findById(productId);
			model.addAttribute("businessUnitName",product.getProducer().getName());
			model.addAttribute("businessUnitabout",product.getProducer().getAbout());
			model.addAttribute("businessUnitAddr",product.getProducer().getAddress());
			model.addAttribute("businessUnitTel",product.getProducer().getTelephone());
			model.addAttribute("traceData",traceData);
			model.addAttribute("count",count);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSON;
	}
	/**
	 * Portal 接口:根据食安码查询产品报告信息接口
	 * @author xuetaoyang 2016/06/08
	 */
	@RequestMapping(method = RequestMethod.GET, value="/getReportByCode")
	public View  getReportByCode( Model model,
			@RequestParam("code") String code,
			@RequestParam(value="productdate",required=false) String productdate,
			@RequestParam String type,
			@RequestParam(value="page",defaultValue="1",required=false) int page){
	//	TraceData traceData=null;
		long productId=0;
		if(code.length()==32&&(productdate==null||productdate.equals(""))){
		productId=Long.valueOf(code.substring(0,16));
		productdate=code.substring(16,24);
		}else{
		productId=Long.valueOf(code);
		}
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		double risk=0;
		Date date = null;
		try {
			date = sdf.parse(productdate);
		} catch (ParseException e1) {
			e1.printStackTrace();
			
		}
		String productDate=new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
		Product  product=productLFService.findProductById(productId);
		try {
			Element result = null;
			if(("self").equals(type)){
				type = "企业自检";
				result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SELF + "_" + productId + page + productDate);
			}else if(("censor").equals(type)){
				type = "企业送检";
				result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_CENSOR + "_" + productId + page +productDate);
			}else if(("sample").equals(type)){
				type = "政府抽检";
				result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SAMPLE + "_" + productId + page + productDate);
			}
			long count=0;
			if(product.getRiskIndex()==null){
				risk=0;
			}else{
				risk=product.getRiskIndex();
			}
			TestRptJson testRptJson = null;
			//是否为portal调用;是为ture；否则：false
			boolean portalFlag = true;
				if(result == null){
					if(("企业自检").equals(type)){
						testRptJson = testReportService.getReportJsonByPreciseDate(productId, type,page, productDate,portalFlag);
						count=testReportService.countByProductIdAndTestTypeWithProductdate(productId, type, productDate);
					}
					else{
						testRptJson = testReportService.getReportJsonByDate(productId, type, page, productDate,portalFlag);
						count=1;
					}
				}
				else if(("企业自检").equals(type)){
					EhCacheFactory.put(CACHE_REPORT_SELF + "_" + productId  + page + productDate, testRptJson);
				}else if(("企业送检").equals(type)){
					EhCacheFactory.put(CACHE_REPORT_CENSOR + "_" + productId + page+ productDate, testRptJson);
				}else if(("政府抽检").equals(type)){
					EhCacheFactory.put(CACHE_REPORT_SAMPLE + "_" + productId +page + productDate, testRptJson);
				}
				else{
				testRptJson = (TestRptJson) result.getObjectValue();
			}
			if (testRptJson == null){
				/*return new RESTResult<TestRptJson>(0, "非法操作或者数据缺失！");
				return new RESTResult<TestRptJson>(1, testRptJson);*/
				return null;
				}
			else{
				model.addAttribute("count",count);
				model.addAttribute("risk",risk);
				model.addAttribute("testRptJson", testRptJson);
				return JSON;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
	}
	}
	/**
	* Portal 接口:根据食安码查询产品营养报告接口
	 * @author xuetaoyang 2016/06/08
	 */	
	@RequestMapping(method = RequestMethod.GET, value="/getNutritionReportByCode")
	public View  getNutritionReportByCode( Model model, 
			@RequestParam("code") String code){
		try{
		long productId=0;
		if(code.length()==32){
			productId=Long.valueOf(code.substring(0,16));
		}else{
			productId=Long.valueOf(code);
		}
		NutritionReport	nutritionReport=productLFService.findNutritionById(productId);
		if(nutritionReport==null){
			return null;
		}else{
			model.addAttribute("nutritionReport", nutritionReport);
			Product product=this.productLFService.findById(productId);
			Product _product=new Product();
			_product.setNutriLabel(product.getNutriLabel());
			model.addAttribute("product",_product);
		}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSON;
	}
	
	/**
	* Portal 接口:根据食安码查询产品营养报告接口
	 * @author xuetaoyang 2016/06/08
	 */	
	@RequestMapping(method = RequestMethod.GET, value="/getThirdReportByCode")
	public View  getThirdReportByCode( Model model, 
			@RequestParam("code") String code){
		long productId=0;
		if(code.length()==32){
		productId=Long.valueOf(code.substring(0,16));
		}else{
		productId=Long.valueOf(code);
		}
		String testType = "第三方检测";
		//报告列表
		List<TestResult> testResults = thirdReportService.getReportDetail(productId,testType);
		List<Long> countList = thirdReportService.getReportCounts(productId,testType);
		for (TestResult testResult : testResults) {
			model.addAttribute("createTime", testResult.getCreate_time());
			model.addAttribute("status", testResult.getPass());
		}
		model.addAttribute("countList", countList);
		
		//认证检测
		long reportCount = thirdReportService.getReportCount(productId, testType);
		List<String > standardList = thirdReportService.getStandards(productId);
		long standardCount = standardList.size();
		model.addAttribute("reportCount", reportCount);
		model.addAttribute("standards", standardList);
		model.addAttribute("standardCount", standardCount);
		
		
		return JSON;
	}		
	
	/**
	 * Portal 接口:根据食安码查询产品营养报告接口
	 * @author xuetaoyang 2016/06/08
	 */	
	@RequestMapping(method = RequestMethod.GET, value="/getThirdReportDetailsByCode")
	public View  getThirdReportDetailsByCode( Model model, 
			@RequestParam("code") String code){
		long productId=0;
		if(code.length()==32){
			productId=Long.valueOf(code.substring(0,16));
		}else{
			productId=Long.valueOf(code);
		}
		String testType = "第三方检测";
		//报告列表
		List<TestResult> testResults = thirdReportService.getReportDetail(productId,testType);
		List<Long> countList = thirdReportService.getReportCounts(productId,testType);
		for (TestResult testResult : testResults) {
			model.addAttribute("createTime", testResult.getCreate_time());
			model.addAttribute("status", testResult.getPass());
		}
		model.addAttribute("countList", countList);
		
		//认证检测
		long reportCount = thirdReportService.getReportCount(productId, testType);
		List<String > standardList = thirdReportService.getStandards(productId);
		long standardCount = standardList.size();
		model.addAttribute("reportCount", reportCount);
		model.addAttribute("standards", standardList);
		model.addAttribute("standardCount", standardCount);
		
		
		return JSON;
	}		
	
	
	@RequestMapping(method = RequestMethod.GET, value="/getMapProductByProductId")
	public View  getMapProductByProductId( Model model, 
			@RequestParam("productId") long productId){
		MapProduct mapProduct=mapProductService.findByProductId(productId);
		if(mapProduct==null){
			model.addAttribute("status",false);
		}else{
			model.addAttribute("status",true);
		}
		model.addAttribute("mapProduct",mapProduct);
		return JSON;
	}
	/**
	 * Portal 接口:查询拥有购买链接，认证及第三方检测的产品
	 * @author xuetaoyang 2016/06/08
	 */
	@RequestMapping(method = RequestMethod.GET, value="/getListOfBuylink")
	public View  getListOfBuylink( Model model){
		model.addAttribute("productOfMarketVO",traceDataService.getListOfBuylink());
		model.addAttribute("count",traceDataService.getListOfBuylink().size());
		return JSON;
	}
/*	/**
	* 团购产品，报告，溯源，营养 接口:根据productid查询产品接口
	 * @author xuetaoyang 2016/08/09
	 */	
/*	@RequestMapping(method = RequestMethod.GET, value="/getInfoByProductid")
	public View  getInfoByProductid( Model model ,
			@RequestParam("productid") String productid,
			@RequestParam(value="productdate",required=false) String productdate,
			@RequestParam String type){
		long productId=0;
		productId=Long.valueOf(productid);
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Product prod = productLFService.findProductById(productId);
		Date date = null;
		try {
			date = sdf.parse(productdate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String productDate=new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
		int page=1;
		//产品信息
		try{
			处理产品图片
			List<Resource> imgList = resourceService.getProductImgListByproId(productId);//查找产品图片集合
			//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
			if(imgList==null||imgList.size()==0){
				if(prod.getImgUrl() != null){
					String[] imgUrlListArray = prod.getImgUrl().split("\\|");
					for(String url:imgUrlListArray){
						Resource re=new Resource();
						re.setUrl(url);
						imgList.add(re);
					}
				}
			}
			prod.setImgList(imgList);
		//	prod.setProductCertification(productStdCertificationService.getListOfStandCertificationByProductId(prod.getId()));
			model.addAttribute("product",prod);
		}catch(Exception e){
			e.printStackTrace();
		}
		//溯源信息
		
		TraceData	traceData=traceDataService.findPagebyproductIDandproductDate(productId, page, productDate);
		model.addAttribute("traceData",traceData);
		//报告信息
			double risk=0;				
			try {
				Element result = null;
				if(("self").equals(type)){
					type = "企业自检";
					result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SELF + "_" + productId + page + productDate);
				}else if(("censor").equals(type)){
					type = "企业送检";
					result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_CENSOR + "_" + productId + page +productDate);
				}else if(("sample").equals(type)){
					type = "政府抽检";
					result = (Element) EhCacheFactory.getCacheElement(CACHE_REPORT_SAMPLE + "_" + productId + page + productDate);
				}
				if(prod.getRiskIndex()==null){
					risk=0;
				}else{
					risk=prod.getRiskIndex();
				}
				TestRptJson testRptJson = null;
				//是否为portal调用;是为ture；否则：false
				boolean portalFlag = true;
					if(result == null){
						if(("企业自检").equals(type)){
							testRptJson = testReportService.getReportJsonByPreciseDate(productId, type,page, productDate,portalFlag);
						}
						else{
							testRptJson = testReportService.getReportJsonByDate(productId, type, page, productDate,portalFlag);
						}
					}
					else if(("企业自检").equals(type)){
						EhCacheFactory.put(CACHE_REPORT_SELF + "_" + productId  + page + productDate, testRptJson);
					}else if(("企业送检").equals(type)){
						EhCacheFactory.put(CACHE_REPORT_CENSOR + "_" + productId + page+ productDate, testRptJson);
					}else if(("政府抽检").equals(type)){
						EhCacheFactory.put(CACHE_REPORT_SAMPLE + "_" + productId +page + productDate, testRptJson);
					}
					else{
					testRptJson = (TestRptJson) result.getObjectValue();
				}
				if (testRptJson == null){
					return null;
					}
				else{
					model.addAttribute("risk",risk);
					model.addAttribute("testRptJson", testRptJson);
					}
			}catch (Exception e) {
				// TODO: handle exception
			}	//营养信息
			NutritionReport	nutritionReport=productLFService.findNutritionById(productId);
			if(nutritionReport==null){
			return null;
			}else{
			model.addAttribute("nutritionReport", nutritionReport);
			}
		return JSON;
		
	}*/

}

















