package com.gettec.fsnip.fsn.web.controller.rest.product;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_FAILED;
import static com.gettec.fsnip.fsn.web.IWebUtils.SERVER_STATUS_SUCCESS;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.base.Nutrition;
import com.gettec.fsnip.fsn.model.business.LicenceFormat;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductArea;
import com.gettec.fsnip.fsn.model.product.ProductBusinessLicense;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductDestroyRecord;
import com.gettec.fsnip.fsn.model.product.ProductLog;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;
import com.gettec.fsnip.fsn.model.product.QRCodeProductInfo;
import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.recycle.Process_mode;
import com.gettec.fsnip.fsn.recycle.Recycle_reason;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.LicenceFormatService;
import com.gettec.fsnip.fsn.service.business.ProductBusinessLicenseService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.erp.FromToBussinessService;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.UnitService;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.UpdataReportService;
import com.gettec.fsnip.fsn.service.product.ProductAreaService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.service.product.ProductDestroyRecordService;
import com.gettec.fsnip.fsn.service.product.ProductNutriService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.product.ProductStdCertificationService;
import com.gettec.fsnip.fsn.service.product.ProductStdNutriService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.service.product.QRCodeProductInfoService;
import com.gettec.fsnip.fsn.transfer.ProductTransfer;
import com.gettec.fsnip.fsn.util.HttpUtils;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.StatisticsClient;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.business.QsNoAndFormatVo;
import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;
import com.gettec.fsnip.fsn.vo.product.ProductDestroyRecordVo;
import com.gettec.fsnip.fsn.vo.product.ProductLIMS;
import com.gettec.fsnip.fsn.vo.product.ProductLismVo;
import com.gettec.fsnip.fsn.vo.product.ProductManageViewVO;
import com.gettec.fsnip.fsn.web.controller.RESTResult;
import com.gettec.fsnip.fsn.web.controller.rest.BaseRESTService;
import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.gettec.fsnip.sso.client.util.SSOClientUtil;
import com.gettec.fsnip.sso.client.vo.AuthenticateInfo;
import com.lhfs.fsn.vo.product.ProductBarcodeToQRcodeVO;

 /**
 * Product REST Service API
 * 
 * @author Ryan Wang
 */
@Controller
@RequestMapping("/product")
public class ProductRESTService extends BaseRESTService{
	@Autowired private ProductService productService;
	@Autowired private ProductNutriService productNutriService;
	@Autowired private ProductStdNutriService productStdNutriService;
	@Autowired private ProductStdCertificationService productStdCertificationService;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private UnitService unitService;
	@Autowired private ProductionLicenseService productionLicenseService;
	@Autowired private MkCategoryService mkCategoryService;
	@Autowired private ProductCategoryInfoService productCategoryInfoService;
	@Autowired private ProductAreaService productAreaService;
	@Autowired private QRCodeProductInfoService qRCodeProductInfoService;
	@Autowired private LicenceFormatService licenceFormatService;
	@Autowired private FromToBussinessService fromToBusService;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private UpdataReportService updataReportService;
	@Autowired private ProductTobusinessUnitService productTobusinessUnitService;
	@Autowired private ProductBusinessLicenseService productBusinessLicenseService;
	@Autowired private ProductDestroyRecordService productDestroyRecordService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired private StatisticsClient staClient;
	private final static Logger logger = Logger.getLogger(ProductRESTService.class);
	
	/**
	 * 根据Id查找产品信息
	 * @param id
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public View get(@PathVariable Long id,@RequestParam(required=false,defaultValue="FSN",value="identify") String identify, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			Product product = productService.findByProductId(id,identify);

			/* 查找qs号  */
			Long qsid = productTobusinessUnitService.getQsId(id, currentUserOrganization);
			product.setQs_info(new QsNoAndFormatVo(qsid));
			
			/* 查询销往企业 */
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			String selectedCustomerIds = fromToBusService.findIdstrs(product.getId(), fromBusId, false);
			product.setSelectedCustomerIds(selectedCustomerIds);
			
					
			List<ProductRecommendUrl> proUrl_0 = new ArrayList<ProductRecommendUrl>();
			List<ProductRecommendUrl> proUrl_1 = new ArrayList<ProductRecommendUrl>();
			for (ProductRecommendUrl vo : product.getProUrlList()) {
				if("0".equals(vo.getStatus())){
					proUrl_0.add(vo);
				}else{
					proUrl_1.add(vo);
				}
			}
			List<ProductBusinessLicense> productBusinessLicenseList=this.productBusinessLicenseService.getProductBusinessLicenseListByProductId(id);
			product.setProductBusinessLicenseList(productBusinessLicenseList);
//			List<Resource> licImgList = businessUnitService.getByIdResource(id,"pbl.RESOURCE_ID");
//			List<Resource> qsImgList = businessUnitService.getByIdResource(id,"pbl.QS_RESOURCE_ID");
//			List<Resource> disImgList = businessUnitService.getByIdResource(id,"pbl.DIS_RESOURCE_ID");
//			/* 返回 */
//			model.addAttribute("licImgList", licImgList);
//			model.addAttribute("qsImgList", qsImgList);
//			model.addAttribute("disImgList", disImgList);
			
			model.addAttribute("vo0", proUrl_0);
			model.addAttribute("vo1", proUrl_1);
			model.addAttribute("data", product);
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
	/**
	 * 根据Id查找产品信息
	 * @param id
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/barcode/{barcode}")
	public View getByBarcodeProduct(@PathVariable String barcode,@RequestParam(required=false,defaultValue="FSN",value="identify") String identify, HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			Long id = productService.getByBarcodeProduct(barcode);
			if(id == null){
				model.addAttribute("data",null);
				return JSON;
			}
			Product product = productService.findByProductId(id,identify);
			
			/* 查找qs号  */
			Long qsid = productTobusinessUnitService.getQsId(id, currentUserOrganization);
			product.setQs_info(new QsNoAndFormatVo(qsid));
			
			/* 查询销往企业 */
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			String selectedCustomerIds = fromToBusService.findIdstrs(product.getId(), fromBusId, false);
			product.setSelectedCustomerIds(selectedCustomerIds);
			
			
			List<ProductRecommendUrl> proUrl_0 = new ArrayList<ProductRecommendUrl>();
			List<ProductRecommendUrl> proUrl_1 = new ArrayList<ProductRecommendUrl>();
			for (ProductRecommendUrl vo : product.getProUrlList()) {
				if("0".equals(vo.getStatus())){
					proUrl_0.add(vo);
				}else{
					proUrl_1.add(vo);
				}
			}
			List<ProductBusinessLicense> productBusinessLicenseList=this.productBusinessLicenseService.getProductBusinessLicenseListByProductId(id);
			product.setProductBusinessLicenseList(productBusinessLicenseList);
			
			model.addAttribute("vo0", proUrl_0);
			model.addAttribute("vo1", proUrl_1);
			model.addAttribute("data", product);
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
	
	/**
	 * 按barcode查找该产品是否已经存在
	 * @param barcode
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/validateBarcodeUnique/{barcode}")
	public View validateBarcodeUnique(@PathVariable String barcode, HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			/* 验证条形码是否存在 */
			boolean isExist = productService.checkExistBarcode(barcode);
			boolean isLead = false;
			if(isExist){
				/* 如果条形码已经存在，验证该产品是否为当前登录企业的引进产品 */
				Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
				isLead = initializeProductService.checkLeadProduct(barcode, currentUserOrganization);
			}
			model.addAttribute("isExist", isExist);
			model.addAttribute("isLead", isLead);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * 根据组织机构查询
	 * @param barcode
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getAllProductsByOrg")
	public View getAllProductsByOrg(HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		model.addAttribute("productList",this.productService.getAllProductsByOrg(Long.valueOf(AccessUtils.getUserOrg().toString())));
		return JSON;
	}
	
	/**
	 * 按barcode查找一条产品信息
	 * @param barcode
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "product")
	public Product getByBarcode(@RequestParam String barcode) {
		try {
			StopWatch s = new StopWatch("ProductRESTService.getByBarcode");
			s.start("Search service");
			Product product = productService.getDAO().findByBarcode(barcode);
			s.stop();
			logger.info(s.getLastTaskName() + " [millis]: " + s.getLastTaskTimeMillis());
			return product;
		} catch (Exception dex) {
			return null;
		}
	}
	
	/**
	 * 背景：产品新增/编辑页面
	 * 功能描述：新增产品
	 * @param enterpriseName 当前正在执行产品新增操作的企业名称
	 * @author ZhangHui 2015/6/3
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{current_business_name}/{isNew}")
	public View createProduct(
			@RequestBody Product product,
			@PathVariable String current_business_name,
			@PathVariable boolean isNew,
			HttpServletRequest req, 
			HttpServletResponse resp, Model model) {
		
		ResultVO resultVO = new ResultVO();
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		String errorMsg="";//定义错误消息 报错时 记录到日志表
		try{
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			
			// 第一步：上传产品图片
			if(product==null || product.getBarcode()==null || "".equals(product.getBarcode().replace(" ", ""))){
				throw new Exception("参数为空");
			}
			
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + product.getBarcode();
			String webUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + product.getBarcode();
			for (Resource resource : product.getProAttachments()) {
				if (resource.getFile() != null) {
					String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
					String name = product.getBarcode() + "-" + randomStr + "." + resource.getType().getRtDesc();
					boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
					if(isSuccess){
						String url;
						if(UploadUtil.IsOss()){
							url=uploadUtil.getOssSignUrl(ftpPath+"/"+name);
						}else{
							url = webUrl + "/" + name;
						}
						resource.setUrl(url);
						resource.setName(name);
					}else{
						throw new Exception("产品图片上传失败");
					}
				}
			}
			
			// 第二步: 如果是进口产品，上传 中文标签图片
			ImportedProduct imp_product = product.getImportedProduct();
			if(imp_product != null){
				ftpPath += "/lab";
				webUrl += "/lab";
				for (Resource resource : imp_product.getLabelAttachments()) {
					if (resource.getFile() != null) {
						String randomStr = sdf.format(new Date()) + (new Random().nextInt(1000) + 1);
						String name = product.getBarcode() + "-" + randomStr + "." + resource.getType().getRtDesc();
						boolean isSuccess = uploadUtil.uploadFile(resource.getFile(), ftpPath, name);
						if(isSuccess){
							resource.setName(name);
							if(UploadUtil.IsOss()){
								resource.setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+name));
							}else{
								resource.setUrl(webUrl + "/" + name);
							}
						}else{
							throw new Exception("中文标签图片上传失败");
						}
					}
				}
			}
			uploadUtil.close();
			// 第三步：保存产品信息
			productService.saveProduct(product, current_business_name, currentUserOrganization, isNew);
			
			if(!isNew){
				// 同步更新报告申请更新表 中的相关产品信息
				updataReportService.changeApplyReportProductInfo(product);
			}
			
			// 第四步：保存销往企业
			String customerIds = product.getSelectedCustomerIds();
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			fromToBusService.save(resultVO, product.getId(), fromBusId, customerIds.split(","));
			model.addAttribute("data", ProductTransfer.transfer(product));
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			resultVO.setSuccess(false);
			errorMsg=errorMsg+"&&&&"+e.getMessage();
		} finally{
			String operation="进行产品新增操作";//所做操作 isNew=true：新增  isNew=false：更新
			if(!isNew){
				operation="进行产品更新操作";
			}
			/**
			 * 记录产品日志
			 * @author longxianzhen 2015/06/03
			 */
			ProductLog logData=new ProductLog(info.getUserName() , operation , errorMsg,HttpUtils.getIpAddr(req), product);
			staClient.offer(logData);//记录产品日志异步的
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/product/{id}")
	public RESTResult<Product> delte(@PathVariable("id") Long id,HttpServletRequest req, 
			HttpServletResponse resp) {
		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
		String errorMsg="";//定义错误消息 报错时 记录到日志表
		Product product =null;
		try {
			RESTResult<Product> result = null;
			product = productService.findById(id);
			productService.delete(id);
			result = new RESTResult<Product>(RESTResult.SUCCESS, product);
			return result;
		} catch (ServiceException sex) {
			((Throwable) sex.getException()).printStackTrace();
			errorMsg=((Throwable) sex.getException()).getMessage();
			return null;
		}finally{
			
			/**
			 * 记录产品日志
			 * @author longxianzhen 2015/06/03
			 */
			ProductLog logData=new ProductLog(info.getUserName(), "完成产品删除操作", errorMsg ,HttpUtils.getIpAddr(req),product);
			staClient.offer(logData);//记录产品日志异步的
		}
	}

	/**
	 * 查找当前登录企业的所有产品信息
	 * @author ZhangHui 2015/4/11
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping(method = RequestMethod.GET, value = "/getProducts/{configure}/{page}/{pageSize}")
	public View getListOfMyProduct(@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = productService.count(currentUserOrganization, configure);
			
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			List<ProductManageViewVO> listOfProduct = productService.getLightProductVOsByPage(
					currentUserOrganization, configure, page, pageSize, fromBusId, false);
			
			Map map = new HashMap();
			map.put("listOfProduct", listOfProduct);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getAllProduct/{configure}/{page}/{pageSize}")
	public View getAllProduct(@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			long total = productService.count(configure);
			
			List<ProductManageViewVO> listOfProduct = productService.getLightProductVOsByPage( configure, page, pageSize);
			
			Map map = new HashMap();
			map.put("listOfProduct", listOfProduct);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 查找当前登录企业的所有产品信息(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping(method = RequestMethod.GET, value = "/getAllProducts/{configure}/{page}/{pageSize}")
	public View getListOfProduct(@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			long total = productService.countAllProduct(currentUserOrganization, configure);
			
			Long fromBusId = businessUnitService.findIdByOrg(currentUserOrganization);
			List<ProductManageViewVO> listOfProduct = productService.getAllLightProductVOsByPage(
					currentUserOrganization, configure, page, pageSize, fromBusId, false);
			
			Map map = new HashMap();
			map.put("listOfProduct", listOfProduct);
			map.put("counts", total);
			model.addAttribute("data", map);
		} catch (Exception e) {
			e.printStackTrace();
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "productLIMS1/{barcode}")
	public View getByBarcodeLIMS(@PathVariable("barcode") String barcode, Model model) {
		try {
			List<Product> products = productService.getDAO().getListByBarcode(barcode);
			if(products==null){
				model.addAttribute("status", SERVER_STATUS_FAILED);
				return JSON;
			}
			List<ProductLIMS> proLimss= new ArrayList<ProductLIMS>();
			for(Product pro:products){
				ProductLIMS proLims = new ProductLIMS();
				proLims.setBarcode(pro.getBarcode());
				proLims.setName(pro.getName());
				proLims.setFormat(pro.getFormat()==null?"":pro.getFormat());
				proLims.setStatus(pro.getStatus()==null?"":pro.getStatus());
				String regularitys = "";
				for(ProductCategoryInfo regularity : pro.getRegularity()){
					if(regularity.getName()==null||regularity.getName()==""){
						continue;
					}else{
						regularitys = regularitys+regularity.getName()+";";
					}
				}
				proLims.setRegularity(regularitys);
				proLimss.add(proLims);
			}
			model.addAttribute("products", proLimss);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
			model.addAttribute("size", proLimss.size());
		} catch (Exception e) {
			model.addAttribute("status", SERVER_STATUS_FAILED);
		}
		return JSON;
	}
	
	/**
	 * 给LIMS抽样工作单处提供接口：
	 * 根据输入的内容模糊查询获取所有匹配上的条形码集合有分页处理
	 * @param barcode
	 * @param page
	 * @param pageSize
	 * @param model
	 * @author LongXianZhen
	 * @return View
	 */
	@RequestMapping(method = RequestMethod.GET, value = "barcodeList/{barcode}/{page}/{pageSize}")
	public View getBarcodeListByCondition(@PathVariable("barcode") String barcode,
			@PathVariable("page") int page,@PathVariable("pageSize") int pageSize,Model model) {
		try {
			List<String> barcodes=productService.getBarcodeListByCondition(barcode,page,pageSize);
			List<ProductLIMS> proLims=new ArrayList<ProductLIMS>();
			if(barcodes!=null){
				/** 查询结果不为null 取出BarCode封装到ProductLIMS中 */
				for(String bc:barcodes){
					ProductLIMS pLims=new ProductLIMS();
					pLims.setBarcode(bc);
					proLims.add(pLims);
				}
			}
			model.addAttribute("barcodes", proLims);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		} catch (ServiceException sex) {
			((Throwable) sex.getException()).printStackTrace();
			model.addAttribute("status", SERVER_STATUS_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("status", SERVER_STATUS_FAILED);
		}
		return JSON;	
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "productLIMS/{barcode}")
	public View getProductByBarcodeLIMS(@PathVariable("barcode") String barcode,Model model) {
		try {
			Product product = productService.getProductByBarcode(barcode);
			if(product==null){
				model.addAttribute("message", "通过该条形码没有找到相关的产品。");
				model.addAttribute("status", SERVER_STATUS_FAILED);
				return JSON;
			}
			ProductLIMS proLims = new ProductLIMS();
			proLims.setBarcode(product.getBarcode());
			proLims.setName(product.getName());
			proLims.setFormat(product.getFormat()==null?"":product.getFormat());
			proLims.setStatus(product.getStatus()==null?"":product.getStatus());
			String regularitys = "";
			for(ProductCategoryInfo regularity : product.getRegularity()){
				if(regularity.getName()==null||regularity.getName().equals("")){
					continue;
				}else{
					regularitys = regularitys+regularity.getName()+";";
				}
			}
			proLims.setRegularity(regularitys);
				
			model.addAttribute("product", proLims);
			model.addAttribute("status", SERVER_STATUS_SUCCESS);
		} catch (Exception e) {
			model.addAttribute("status", SERVER_STATUS_FAILED);
		}
		return JSON;
	}
	
	/* 获取标准的营养列表
	 * */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getStandNutris")
	public View getListOfStandNutri(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<Nutrition> listOfStandNutri = productStdNutriService.getListOfStandNutri();
			model.addAttribute("data", listOfStandNutri);
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
	
	/* 获取标准的认证信息类别
	 * */
	@SuppressWarnings("unused")
	@RequestMapping(method = RequestMethod.GET, value = "/getStandCertifications")
	public View getListOfStandCertification(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<Certification> listOfStandCertification = productStdCertificationService.getListOfStandCertification();
			model.addAttribute("data", listOfStandCertification);
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
	
	/**
	 * 营养信息分页
	 * @param page
	 * @param pageSize
	 * @param productId
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/getStandNutris/{page}/{pageSize}/{productId}")
    public View getListOfStandNutriByproductId(@PathVariable(value="page") int page,
            @PathVariable(value="pageSize") int pageSize, @PathVariable(value="productId") Long productId,
            HttpServletRequest req, HttpServletResponse resp, Model model) {
        ResultVO resultVO = new ResultVO();
        try{
            long total = productToBusinessUnitService.countByproductId(productId);
            List<ProductNutrition> listOfProductNutrition = productToBusinessUnitService.getListOfProductNutritionByProductIdWithPage(
                     productId, page, pageSize);
            Map map = new HashMap();
            map.put("counts", total);
            map.put("listOfProductNutrition", listOfProductNutrition);
            model.addAttribute("data", map);
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

	
	/**
	 * 根据产品名称模糊查询获得产品列表
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/searchProductListByName/{name}")
	public View searchProductListByName(@PathVariable(value="name") String name,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{			
			name=URLDecoder.decode(name, "UTF-8");
			long total = productService.getCountByName(name);
			List<Product> listOfProduct = productService.searchProductListByName(name);
			Map map = new HashMap();
			map.put("ProductList", listOfProduct);
			map.put("counts", total);
			model.addAttribute("data", map);
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

	/**
	 * 获取产品单位列表
	 * */
	@RequestMapping(method = RequestMethod.GET, value = "/getAllUnitName")
	public View getAllUnitName(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{			
			List<String> listUnitName=unitService.getAllUnitName();
			model.addAttribute("data", listUnitName);
		} catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			((Throwable) sex.getException()).printStackTrace();
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 点击营养报告对应的列自动搜索值 
	 * @param colName 点击的列号
	 * @param req
	 * @param resp
	 * @param model
	 * @return JSON
	 */
	 @SuppressWarnings("unused")
     @RequestMapping(method=RequestMethod.GET,value="/autoItems/{colName}")
     public View autoNutritionItems(@PathVariable(value="colName")int colName, 
             HttpServletRequest req, HttpServletResponse resp, Model model){
         ResultVO resultVO = new ResultVO();
         try {
             AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
             List<String> listOfItem = productNutriService.autoNutritionItems(colName);
             model.addAttribute("data", listOfItem);
          } catch (ServiceException sex) {
              resultVO.setErrorMessage(sex.getMessage());
              resultVO.setStatus(SERVER_STATUS_FAILED);
          } catch (Exception e) {
              resultVO.setErrorMessage(e.getMessage());
              resultVO.setStatus(SERVER_STATUS_FAILED);
          }
          return JSON;
     }
	 
	 /**
	 * 根据流通企业录入的条形码和企业名称验证产品是否绑定QS证号
	 * @param barcode 条形码
	 * @param busName 企业名称
	 * */
	@RequestMapping(method = RequestMethod.GET, value = "/getPro2BusByBarcodeAndBusName/{barcode}/{bus_name}")
	public View getPro2BusByBarcodeAndBusName(
			@PathVariable(value="barcode") String barcode,
			@PathVariable(value="bus_name") String bus_name,
			HttpServletRequest req, 
			HttpServletResponse resp, 
			Model model) {
		
		ResultVO resultVO = new ResultVO();
		try{			
			BusinessUnitOfReportVO bus_vo = productToBusinessUnitService.getBusUnitOfReportVOByBusname(bus_name, barcode);
			model.addAttribute("data", bus_vo);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 加载该企业能绑定的所有qs号
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="/loadSonqsno/{firstpart}")
    public View loadSonqsno(@PathVariable(value="firstpart") String firstpart,@RequestParam(value="formatId",required=false) Long formatId,
            HttpServletRequest req, HttpServletResponse resp, Model model){
        ResultVO resultVO = new ResultVO();
        try{
            AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
            Long myRealOrgnizationId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
            List<String> sonQs = productToBusinessUnitService.loadSonqsno(info.getOrganization(),myRealOrgnizationId,firstpart,formatId);
            model.addAttribute("data", sonQs);
        }catch(Exception e){
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setSuccess(false);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
	
	/**
	 * 加载产品分类中的第一大分类
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * 
	 * @author HuangYog
	 */
	@RequestMapping(method=RequestMethod.GET, value="/loadProduCtcategory")
    public View loadProduCtcategory(HttpServletRequest req, HttpServletResponse resp, Model model){
        ResultVO resultVO = new ResultVO();
        try{
            List<ProductCategory> produCtcategory = mkCategoryService.loadProduCtcategory();
            model.addAttribute("data", produCtcategory);
        }catch(Exception e){
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setSuccess(false);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
	
	/**
	 * 流通企业从product中查找所有barcode,
	 * 用于页面按产品条形码自动加载数据的功能。
	 * @param req
	 * @param resp
	 * @param businessType 企业类型 (主要用于区分商超，经销商和奇特流通企业)
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getAllBarCode")
	public View getAllBarcode(
			@RequestParam(value="businessType",required=false) String businessType,
			HttpServletRequest req,HttpServletResponse resp, Model model){
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			List<String> listOfBarcode = null;
			if("经销商".equals(businessType)){
				Long myOrg = info.getOrganization();
				listOfBarcode = productService.getAllBarcode(myOrg);
			}else{
				listOfBarcode = productService.getAllBarcode();
			}
			model.addAttribute("data", listOfBarcode);
		}catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	/**
	 * 按qs号查找一条生产许可证信息
	 * @param licenseNo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(method=RequestMethod.POST,value="/getLicenseInfoByQs/{qs}")
	public View getLicenseInfoByQs(@PathVariable(value="qs") String qs,HttpServletRequest req,HttpServletResponse resp,Model model){
		ResultVO resultVO = new ResultVO();
		try {
			model.addAttribute("data", productionLicenseService.getDAO().findById(qs));
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
			e.printStackTrace();
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 新增执行标准
	 * @param ProductCategoryInfo 执行标准信息
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author 郝圆彬
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/addRegularity")
	public View createRegularity(@RequestBody ProductCategoryInfo categoryInfo,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{		
			categoryInfo.setCategoryFlag(false);
			ProductCategoryInfo orig_categoryInfo=productCategoryInfoService.saveCategoryInfo(categoryInfo);
			if(orig_categoryInfo==null){//如果返回null，表示该执行标准已经存在
				resultVO.setErrorMessage("该标准已存在！新增失败！");
				resultVO.setStatus(SERVER_STATUS_FAILED);
			}
			model.addAttribute("data", orig_categoryInfo);
		}catch (ServiceException sex) {
			resultVO.setErrorMessage(sex.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 获取产品所属区域
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getAllProductArea")
	public View getAllProductArea(HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			List<ProductArea> listProArea = productAreaService.findAll();
			model.addAttribute("data", listProArea);
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
	
	/**
	 * 根据企业组织机构id获取该企业的二维码产品列表
	 * @param page
	 * @param pageSize
	 * @param configure
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getListQRCodeProductByOrg/{page}/{pageSize}/{configure}")
	public View getListQRCodeProductByOrg(@PathVariable(value="page") int page,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="configure") String configure,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			long count = qRCodeProductInfoService.countByOrganization(info.getOrganization(), configure);
			List<QRCodeProductInfo> listQRCodeProduct = qRCodeProductInfoService.getListByOrganization(info.getOrganization(),page,pageSize,configure);
			model.addAttribute("counts", count);
			model.addAttribute("data", listQRCodeProduct);
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
	
	/**
	 * 根据产品id加载该产品的二维码信息
	 * @param productId
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getQRCodeByProductId/{productId}")
	public View getListQRCodeByProductId(@PathVariable(value="productId") Long productId,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			QRCodeProductInfo qrcodeProduct = qRCodeProductInfoService.findByProductId(productId);
			/* 查找营养报告  */
			if(qrcodeProduct.getProduct()!=null){
				List<ProductNutrition> listOfNutris = productNutriService.getListOfNutrisByProductId(productId);
				qrcodeProduct.getProduct().setListOfNutrition(listOfNutris);
			}
			model.addAttribute("data", qrcodeProduct);
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
	
	/**
	 * 创建二维码产品信息
	 * @param qrcodeProductVO
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveQRCodeProduct")
	public View createQRCodeProduct(@RequestBody QRCodeProductInfo qrcodeProduct,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			qrcodeProduct = qRCodeProductInfoService.save(qrcodeProduct,currentUserOrganization,true,info);
			model.addAttribute("data", qrcodeProduct);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 更新二维码产品信息
	 * @param qrcodeProduct
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/saveQRCodeProduct")
	public View updateQRCodeProduct(@RequestBody QRCodeProductInfo qrcodeProduct,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			qrcodeProduct = qRCodeProductInfoService.save(qrcodeProduct,currentUserOrganization,false,info);
			model.addAttribute("data", qrcodeProduct);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} 
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
	 * 验证产品内部码是否重复，针对同一个企业内部验证。
	 * @param innerCode
	 * @param productId
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @author TangXin
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/validateInnerCode/{innerCode}")
	public View validateInnerCode(@PathVariable String innerCode, @RequestParam(value="productId", required=false) Long productId,
			HttpServletRequest req, HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try{
			AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			boolean status = qRCodeProductInfoService.validateInnerCode(innerCode, info.getOrganization(), productId);
			model.addAttribute("status", status);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		} 
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	/**
     * 加载qs号填写时需要选择的填写形式
     * @return
     */
    @RequestMapping(method=RequestMethod.GET, value="/loadlistFormatqs")
    public View loadlistFormatqs(HttpServletRequest req, HttpServletResponse resp, Model model){
        ResultVO resultVO = new ResultVO();
        try{
            Long orgnizationId = Long.parseLong(AccessUtils.getUserRealOrg().toString());
            List<LicenceFormat> qsformat = licenceFormatService.loadlistFormatqs(orgnizationId);
            model.addAttribute("data", qsformat);
        }catch(Exception e){
            resultVO.setErrorMessage(e.getMessage());
            resultVO.setSuccess(false);
            e.printStackTrace();
        }
        model.addAttribute("result", resultVO);
        return JSON;
    }
    
    /**
     * 重新计算营养指标
     * @author TangXin
     */
    @RequestMapping(method=RequestMethod.PUT,value="/recalculatedNutri/{productID}")
    public View recalculatedNutri(@PathVariable("productID") Long productID, HttpServletRequest rep, 
    		HttpServletResponse resp, Model model){
    	ResultVO resultVO = new ResultVO();
    	try{
    		resultVO = productService.recalculatedNutri(productID);
    	}catch(Exception e){
    		resultVO.setErrorMessage(e.getMessage());
            resultVO.setStatus("fasle");
    	}
    	model.addAttribute("result", resultVO);
    	return JSON;
    }
    
    /**
     * 获取某供应商提供给当前登录的商超的所有产品
     * @author ZhangHui 2015/4/9
     */
    @SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET,value="/getProductOfBusinessSuper/{configure}/{page}/{pageSize}/{fromBusId}")
    public View getProductOfBusinessSuper(@PathVariable(value="page")int page, 
    		@RequestParam(value="flag",required=false)boolean flag,
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="fromBusId") Long fromBusId,
			@PathVariable(value="configure") String configure, HttpServletRequest req, HttpServletResponse resp,
			Model model) {
    	ResultVO resultVO = new ResultVO();
    	try{
    		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			/* 获取当前登录的商超企业id */
    		Long toBusId = null;
				if(!flag){
					toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
				}
    		
    		/* 获取产品总数 */
    		Long total = fromToBusService.countsOfProduct2Super(fromBusId, toBusId, false,configure);
    		model.addAttribute("counts", total);
    		List<Product> products = new ArrayList<Product>();
    		/* 获取分页产品ids */
    		String proIdStrs = fromToBusService.findAllProIdStrsByPage(fromBusId, toBusId, false, page, pageSize,configure);
    		if("".equals(proIdStrs)){
    			model.addAttribute("data", products);
    		}else{
    			//String myCondition = " WHERE id IN(" + proIdStrs + ")";
        		/* 获取产品的详细信息 */
        		products = productService.getListByConfigure(proIdStrs, new Object[]{});
        		model.addAttribute("data", products);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
    	}
    	model.addAttribute("result", resultVO);
    	return JSON;
    }
    
    /**
     * 获取某供应商提供给当前登录的商超的待处理报告的产品
     * @author ZhangHui 2015/5/4
     */
    @SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET,value="/getProductOfBusinessSuper/onHandle/{configure}/{page}/{pageSize}/{fromBusId}")
    public View getProductOfBusinessSuperOnHandle(
    		@RequestParam(value="flag",required=false)boolean flag,
    		@PathVariable(value="page")int page, 
			@PathVariable(value="pageSize") int pageSize, @PathVariable(value="fromBusId") Long fromBusId,
			@PathVariable(value="configure") String configure, HttpServletRequest req, HttpServletResponse resp,
			Model model) {
    	ResultVO resultVO = new ResultVO();
    	try{
    		AuthenticateInfo info = SSOClientUtil.validUser(req, resp);
			Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
			/* 获取当前登录的商超企业id */
    		Long toBusId = null;
    		if(!flag){
    			/* 获取当前登录的商超企业id */
    			toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
    		}
    		
    		/* 获取产品总数 */
    		Long total = fromToBusService.countsOfOnHandle(fromBusId, toBusId, false,configure);
    		model.addAttribute("counts", total);
    		
    		/* 获取分页产品ids */
    		String proIdStrs = fromToBusService.findAllProIdStrsOfOnHandle(fromBusId, toBusId, false, page, pageSize,configure);
    		if("".equals(proIdStrs)){
    			model.addAttribute("data", new ArrayList<Product>());
    		}else{
    			//String myCondition = " WHERE id IN(" + proIdStrs + ")";
        		/* 获取产品的详细信息 */
        		List<Product> products = productService.getListByConfigure(proIdStrs, new Object[]{});
        		model.addAttribute("data", products);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
    	}
    	model.addAttribute("result", resultVO);
    	return JSON;
    }
    
    /**
    * 重新计算营养指标
    * @author TangXin
    */
   @RequestMapping(method=RequestMethod.GET,value="/getProductAndCert/{productID}/{fromBusId}")
   public View getProductAndCert(@PathVariable("productID") Long productID,@PathVariable("fromBusId") Long fromBusId,
		   HttpServletRequest rep, 
   		HttpServletResponse resp, Model model){
   	ResultVO resultVO = new ResultVO();
   	try{
   		Long currentUserOrganization = Long.parseLong(AccessUtils.getUserRealOrg().toString());
		/* 获取当前登录的商超企业id */
		Long toBusId = businessUnitService.findIdByOrg(currentUserOrganization);
   		ProductManageViewVO productVO = productService.getProductAndCert(productID,toBusId,fromBusId);
   		model.addAttribute("data", productVO);
   	}catch(Exception e){
   		resultVO.setErrorMessage(e.getMessage());
        resultVO.setStatus("fasle");
   	}
   	model.addAttribute("result", resultVO);
   	return JSON;
   }
   
   
   
   @RequestMapping(method=RequestMethod.POST,value="/productUrl/{type}")
   public View saveProUrl(@RequestBody ProductRecommendUrl vo,@PathVariable("type") String type,
		   HttpServletRequest rep, 
		   HttpServletResponse resp, Model model){
	   ResultVO resultVO = new ResultVO();
	   try{
		   vo.setIdentify(type);
		   boolean success = productService.saveProUrl(vo);
		   model.addAttribute("success", success);
	   }catch(Exception e){
		   resultVO.setErrorMessage(e.getMessage());
		   resultVO.setStatus("fasle");
	   }
	   model.addAttribute("result", resultVO);
	   return JSON;
   }
   @RequestMapping(method=RequestMethod.GET,value="/delUrl/{id}")
   public View delUrl(@PathVariable("id") Long id,
		   HttpServletRequest rep, 
		   HttpServletResponse resp, Model model){
	   ResultVO resultVO = new ResultVO();
	   try{
		   boolean success = productService.delUrl(id);
		   model.addAttribute("success", success);
	   }catch(Exception e){
		   resultVO.setErrorMessage(e.getMessage());
		   resultVO.setStatus("fasle");
	   }
	   model.addAttribute("result", resultVO);
	   return JSON;
   }
   /**
    * 根据条形码和数量生成二维码对应关系
    * @param barcode
    * @param QRNum
    * @param rep
    * @param resp
    * @param model
    * @return
    * liuyuanjing
    */
   
   @RequestMapping(method=RequestMethod.POST,value="/productToQRcode/{barcode}/{QRStart}/{QREnd}")
   public View setProductToQRcode(@PathVariable("barcode") String barcode,@PathVariable("QRStart") String QRStart,@PathVariable("QREnd") String QREnd,  HttpServletRequest rep, HttpServletResponse resp, Model model){
	   try {
		   Long productID= productService.getIdByBarcode(barcode);
		   boolean success= productService.setBarcodeToQRcode(barcode,productID, QRStart,QREnd);
		   model.addAttribute("success", success);
	   } catch (ServiceException e) {
		   e.printStackTrace();
	   }
	   return JSON;
   }
   
   
   /**
    * 根据条形码和数量生成二维码对应关系
    * @param barcode
    * @param QRNum
    * @param rep
    * @param resp
    * @param model
    * @return
    * liuyuanjing
    */

	@RequestMapping(method = RequestMethod.GET, value = "/getProductToQRcodeSource")
	public View getProductToQRcodeSource(HttpServletRequest rep,
			HttpServletResponse resp, Model model) {
		ResultVO resultVO = new ResultVO();
		try {
			List<ProductBarcodeToQRcodeVO> barcodeToQRcodeVOList = productService.getBarcodeToQRcode();
			Map map = new HashMap();
			map.put("barcodeToQRcodeVOList", barcodeToQRcodeVOList);
			model.addAttribute("data", map);
			resultVO.setStatus(SERVER_STATUS_SUCCESS);
		} catch (Exception e) {
			resultVO.setErrorMessage(e.getMessage());
			resultVO.setStatus(SERVER_STATUS_FAILED);
		}
		model.addAttribute("result", resultVO);
		return JSON;
	}
	
	   
	   @RequestMapping(method=RequestMethod.POST,value="/productToQRcode/delete/{id}")
	   public View deleteProductToQRcode(@PathVariable("id") Long id, HttpServletRequest rep, HttpServletResponse resp, Model model){
		   try {
			   boolean success= productService.deleteBarcodeToQRcode(id);
			   model.addAttribute("success", success);
		   } catch (ServiceException e) {
			   e.printStackTrace();
		   }
		   return JSON;
	   }
	
	   /**
	    * 根据条形码查询产品信息以及生产厂商信息
	    * @param barcode
	    * @param model
	    * @return
	    */
		@RequestMapping(method = RequestMethod.GET, value = "busProLims/{barcode}")
		public View getByBarcodeBusProLims(@PathVariable("barcode") String barcode, Model model) {
			try {
				ProductLismVo busProLims = productService.getByBarcodeBusProLims(barcode);
				model.addAttribute("result", busProLims);
				model.addAttribute("status", SERVER_STATUS_SUCCESS);
			
			} catch (Exception e) {
				model.addAttribute("status", SERVER_STATUS_FAILED);
			}
			return JSON;
		}
		 /**
		    * 问题产品销毁记录保存
		    * @author xuetaoyang 2016/08/16
		    * @return
		    */
		//销毁记录保存
		@RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT},value = "/savedestroy")
		public View save(Model model,@RequestBody ProductDestroyRecordVo productDestroyRecordVo){
			try {
				
				ProductDestroyRecord productDestroyRecord=new ProductDestroyRecord();
				productDestroyRecord.setHandle_name(AccessUtils.getUserOrgName().toString());
				productDestroyRecord.setOperation_user(AccessUtils.getUserName().toString());
				productDestroyRecord.setNumber(productDestroyRecordVo.getNumber());
				productDestroyRecord.setName(productDestroyRecordVo.getName());
				productDestroyRecord.setBarcode(productDestroyRecordVo.getBarcode());
				productDestroyRecord.setBatch(productDestroyRecordVo.getBatch());
				productDestroyRecord.setProblem_describe(productDestroyRecordVo.getProblem_describe());
				productDestroyRecord.setProcess_time(productDestroyRecordVo.getProcess_time());
				productDestroyRecord.setDeal_address(productDestroyRecordVo.getDeal_address());
				productDestroyRecord.setDeal_person(productDestroyRecordVo.getDeal_person());
				productDestroyRecord.setProcess_mode(Process_mode.getMap().get(productDestroyRecordVo.getProcess_mode()));
				productDestroyRecord.setId(productDestroyRecordVo.getId());
				productDestroyRecord.setRecAttachments(productDestroyRecordVo.getRecAttachments());
				productDestroyRecordService.update(productDestroyRecord);
				model.addAttribute("status",true);
			} catch (ServiceException e) {
				e.printStackTrace();
				model.addAttribute("status",false);
			}
			return JSON;
		}
		 /**
		    * 问题产品销毁记录删除
		    * @author xuetaoyang 2016/08/16
		    * @return
		    */
	//销毁记录删除
		@RequestMapping(value="/delete",method=RequestMethod.DELETE)
		public View deleteProductDestroyRecord(Model model,@RequestParam("id") long id){
			try {
				this.productDestroyRecordService.delete(id);
				model.addAttribute("status",true);
			} catch (ServiceException e) {
				e.printStackTrace();
				model.addAttribute("status",false);
			}
			return null;
			
		}
		 /**
		    * 查询登录企业问题产品销毁记录
		    * @author xuetaoyang 2016/08/16
		    * @return
		    */
	//查询该企业的销毁记录
		@RequestMapping("/getListDestroy")
		public View  getListDestroy(Model model,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,@RequestParam("keyword") String keyword){
			String orgname=AccessUtils.getUserName().toString();
			List<ProductDestroyRecord> list=productDestroyRecordService.getbyOrgId(orgname, page, pageSize,keyword);
			List<ProductDestroyRecordVo> arraylist=new ArrayList<ProductDestroyRecordVo>();
			for(int i=0;i<list.size();i++){
				ProductDestroyRecordVo _productDestroyRecordVo=new ProductDestroyRecordVo();
				_productDestroyRecordVo.setName(list.get(i).getName());
				_productDestroyRecordVo.setBarcode(list.get(i).getBarcode());
				_productDestroyRecordVo.setBatch(list.get(i).getBatch());
				_productDestroyRecordVo.setHandle_name(list.get(i).getHandle_name());
				_productDestroyRecordVo.setId(list.get(i).getId());
				_productDestroyRecordVo.setNumber(list.get(i).getNumber());
				_productDestroyRecordVo.setOperation_user(list.get(i).getOperation_user());
				_productDestroyRecordVo.setProblem_describe(list.get(i).getProblem_describe());
				_productDestroyRecordVo.setProcess_time(list.get(i).getProcess_time());
				_productDestroyRecordVo.setDeal_address(list.get(i).getDeal_address());
				_productDestroyRecordVo.setDeal_person(list.get(i).getDeal_person());
				_productDestroyRecordVo.setRecAttachments(list.get(i).getRecAttachments());
					if(list.get(i).getProcess_mode()==Process_mode.DESTROY){
						_productDestroyRecordVo.setProcess_mode(list.get(i).getProcess_mode().getName().toString());
					}else{
						_productDestroyRecordVo.setProcess_mode(list.get(i).getProcess_mode().getName());
				}
					arraylist.add(_productDestroyRecordVo);
			}
			model.addAttribute("list",arraylist);
			model.addAttribute("count",productDestroyRecordService.countbyOrg(orgname,keyword));
			return JSON;
		}
		 /**
		    * 根据记录id查询问题产品销毁记录
		    * @author xuetaoyang 2016/08/16
		    * @return
		    */
		//根据id查询销毁记录
		@RequestMapping(value="/getDestroyById",method = RequestMethod.GET)
		public View getDestroyById(Model model,@RequestParam("id") long id){
			try {
				ProductDestroyRecord productDestroyRecord=this.productDestroyRecordService.findById(id);
				ProductDestroyRecordVo productDestroyRecordVo=new ProductDestroyRecordVo();
				productDestroyRecordVo.setName(productDestroyRecord.getName());
				productDestroyRecordVo.setBarcode(productDestroyRecord.getBarcode());
				productDestroyRecordVo.setBatch(productDestroyRecord.getBatch());
				productDestroyRecordVo.setHandle_name(productDestroyRecord.getHandle_name());
				productDestroyRecordVo.setId(productDestroyRecord.getId());
				productDestroyRecordVo.setNumber(productDestroyRecord.getNumber());
				productDestroyRecordVo.setOperation_user(productDestroyRecord.getOperation_user());
				productDestroyRecordVo.setProblem_describe(productDestroyRecord.getProblem_describe());
				productDestroyRecordVo.setProcess_time(productDestroyRecord.getProcess_time());
				productDestroyRecordVo.setDeal_address(productDestroyRecord.getDeal_address());
				productDestroyRecordVo.setDeal_person(productDestroyRecord.getDeal_person());
				productDestroyRecordVo.setRecAttachments(productDestroyRecord.getRecAttachments());
				productDestroyRecordVo.setProcess_mode(productDestroyRecord.getProcess_mode().getName());
				model.addAttribute("productDestroyRecord",productDestroyRecordVo);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return JSON;
		}
		/**
		 * 根据产品id与组织机构查询产品
		 * @param barcode
		 * @param req
		 * @param resp
		 * @param model
		 * @return
		 */
		@RequestMapping(method = RequestMethod.GET, value = "/getAllProductsByOrgandId")
		public View getAllProductsByOrgandId(HttpServletRequest req, 
				HttpServletResponse resp, Model model,@RequestParam("id") long id) {
			Product pro=productService.getAllProductsByOrgandid(Long.valueOf(AccessUtils.getUserOrg().toString()), id);
			if(pro==null){
				model.addAttribute("product", null);
			}else{
				String name=pro.getName();
				model.addAttribute("product",name);
			}
			return JSON;
		}
	
}