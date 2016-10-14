package com.gettec.fsnip.fsn.service.product.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gettec.fsnip.fsn.model.trace.TraceData;
import com.gettec.fsnip.fsn.service.trace.TraceDataService;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.dao.product.ProductDAO;
import com.gettec.fsnip.fsn.dao.product.ProductInstanceDAO;
import com.gettec.fsnip.fsn.dao.product.ProductRecommendUrlDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.InitializeProduct;
import com.gettec.fsnip.fsn.model.erp.base.Unit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductBusinessLicense;
import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductNutrition;
import com.gettec.fsnip.fsn.model.product.ProductRecommendUrl;
import com.gettec.fsnip.fsn.nutrition.NutritionProcessor;
import com.gettec.fsnip.fsn.nutrition.label.NutritionVO;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.ProductBusinessLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.erp.InitializeProductService;
import com.gettec.fsnip.fsn.service.erp.UnitService;
import com.gettec.fsnip.fsn.service.market.MkCategoryService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.BusinessCertificationService;
import com.gettec.fsnip.fsn.service.product.ImportedProductService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.service.product.ProductNutriService;
import com.gettec.fsnip.fsn.service.product.ProductRecommendUrlService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.service.product.ProductTobusinessUnitService;
import com.gettec.fsnip.fsn.transfer.ProductTransfer;
import com.gettec.fsnip.fsn.util.FilterUtils;
import com.gettec.fsnip.fsn.util.NutritionUtil;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.gettec.fsnip.fsn.util.UploadUtil;
import com.gettec.fsnip.fsn.vo.ResultVO;
import com.gettec.fsnip.fsn.vo.product.ProductLismVo;
import com.gettec.fsnip.fsn.vo.product.ProductManageViewVO;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;
import com.gettec.fsnip.fsn.vo.sales.DetailAlbumVO;
import com.lhfs.fsn.vo.SampleVO;
import com.lhfs.fsn.vo.product.ProductBarcodeToQRcodeVO;
import com.lhfs.fsn.vo.product.ProductListVo;
import com.lhfs.fsn.vo.product.ProductRiskVo;


/**
 * Product service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="productService")
public class ProductServiceImpl extends BaseServiceImpl<Product, ProductDAO> 
		implements ProductService{
	@Autowired private ProductDAO productDAO;
	@Autowired private ProductInstanceDAO productInstanceDAO;
	@Autowired private ProductNutriService productNutriService;
	@Autowired private ResourceService testResourceService;
	@Autowired private BusinessBrandService businessBrandService;
	@Autowired private BusinessCertificationService businessCertificationService;
	@Autowired private ProductTobusinessUnitService productToBusinessUnitService;
	@Autowired private UnitService unitService; 
	@Autowired private BusinessUnitService businessUnitServicee;
	@Autowired private InitializeProductService initializeProductService;
	@Autowired private ProductCategoryInfoService productCategoryInfoService;
	@Autowired private MkCategoryService categoryService;
	@Autowired private ImportedProductService importedProductService;
	@Autowired private ResourceService resourceService;
	@Autowired private TraceDataService traceDataService;
	@Autowired private ProductRecommendUrlDAO recommendUrlDAO;
	@Autowired private ProductRecommendUrlService recommendUrlService;
	@Autowired private ProductBusinessLicenseService productBusinessLicenseService;
	
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductDAO getDAO() {
		return productDAO;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public boolean checkExistBarcode(String barcode) throws ServiceException {
		try {
			return getDAO().checkExistBarcode(barcode);
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}
	
	public Product eagerFindById(Long id) throws ServiceException {
		try {
			Product product = getDAO().findById(id);
			if(product != null){
				product.setProductInstances(productInstanceDAO.findProductInstancesByPID(id));
			}
			return product;
		} catch (JPAException jpae) {
			throw new ServiceException("", jpae.getException());
		}
	}
	
	/**
	 * 根据条件获取条形码集合有分页处理
	 * @param condition
	 * @param page
	 * @param pageSize
	 * @return List<String>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	public List<String> getBarcodeListByCondition(String condition,int page ,int pageSize) throws ServiceException{
		try {
			return getDAO().getBarcodeListByCondition(condition,page,pageSize);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getBarcodeListByCondition()-->"+dex.getMessage(), dex.getException());
		}
	}
	
    @Override
	public Product getProductByBarcode(String barcode) throws ServiceException {		
		try {
			return getDAO().findByBarcode(barcode);
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}
	
	/**
	 * 按企业组织机构查找产品数量
	 * @author ZhangHui 2015/4/11
	 */
	@Override
	public long count(Long organization, String configure) throws ServiceException {
		try {
			if(organization == null){
				return 0;
			}
			String condition = "";
			if(configure == null || "null".equals(configure)){
				condition = " WHERE organization = ?1";
				//如果等于configure = 0的情况下，是不需要查询条件的，因此在如下判断中，给你了一个1=1的条件来满足查询sql的常规书写
			}else{
				condition = getConfigure(configure) + " AND organization = ?1";
				
			}
			/*if(condition != null) {
				condition += " and packageFlag = '0' ";
			}*/
			/* count方法是使用面向对象查询，模式给product实体使用别名e */
			condition = condition.replace("product", "e");
			return getDAO().count(condition, new Object[]{organization});
		} catch (JPAException e) {
			throw new ServiceException("ProductServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}
	
	public long count(String configure) throws ServiceException {
		try {
			String condition = "";
			if(configure == null || "null".equals(configure)){
				//如果等于configure = 0的情况下，是不需要查询条件的，因此在如下判断中，给你了一个1=1的条件来满足查询sql的常规书写
			} else if(configure != null&&"0".equals(configure)){
			}else{
				condition = getConfigure(configure);
			}
			condition = condition.replace("product", "e");
			return getDAO().count(condition, new Object[]{});
		} catch (JPAException e) {
			throw new ServiceException("ProductServiceImpl.count()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 获取轻量级产品信息
	 * @author ZhangHui 2015/4/11
	 */
	@Override
	public List<ProductManageViewVO> getLightProductVOsByPage(Long organization, String configure, 
			int page, int pageSize, Long fromBusId, boolean isDel) throws ServiceException {
		try {
			String condition = getConfigureOfIgnoreBrand(configure);
			
			String condition_barnd = "";
			if(configure!=null && "".equals(condition)){
				/* 此时configure应该为品牌名称  */
				condition_barnd = getConfigure(configure);
			}
			return getDAO().getLightProductVOsByPage(page, pageSize, condition, condition_barnd, organization, fromBusId, isDel);
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getLightProductVOsByPage()-->" + e.getMessage(), e.getException());
		}
	}
	
	@Override
	public List<ProductManageViewVO> getLightProductVOsByPage(String configure, 
			int page, int pageSize) throws ServiceException {
		try {
			String condition = null;
			if(configure!=null&&!"0".equals(configure)){
				condition = getConfigureOfIgnoreBrand(configure);
			}
			String condition_barnd = "";
			if(configure!=null && "".equals(condition)){
				/* 此时configure应该为品牌名称  */
				condition_barnd = getConfigure(configure);
			}
			if(configure!=null&&"0".equals(configure)){
				condition = "0";
			}
			return getDAO().getLightProductVOsByPage(page, pageSize, condition, condition_barnd);
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getLightProductVOsByPage()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 获取轻量级产品信息(包括引进产品)
	 * @author ZhangHui 2015/4/11
	 */
	@Override
	public List<ProductManageViewVO> getAllLightProductVOsByPage(Long organization, String configure, 
			int page, int pageSize, Long fromBusId, boolean isDel) throws ServiceException {
		try {
			String condition = getConfigureOfIgnoreBrand(configure);
			String condition_barnd = "";
			if(configure!=null && "".equals(condition)){
				/* 此时configure应该为品牌名称  */
				condition_barnd = getConfigure(configure);
			}
			
			return getDAO().getAllLightProductVOsByPage(page, pageSize, condition, condition_barnd, organization, fromBusId, isDel);
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getLightProductVOsByPage()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 获取产品数量(包括引进产品)
	 * @author ZhangHui 2015/4/14
	 */
	@Override
	public long countAllProduct(Long organization, String configure) throws ServiceException {
		try {
			String condition = getConfigureOfIgnoreBrand(configure);
			String condition_barnd = "";
			if(configure!=null && "".equals(condition)){
				/* 此时configure应该为品牌名称  */
				condition_barnd = getConfigure(configure);
			}
			
			return getDAO().countAllProduct(condition, condition_barnd, organization);
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.countAllProduct()-->" + e.getMessage(), e.getException());
		}
	}
	
	/**
	 * 背景：产品新增/编辑页面
	 * 功能描述：新增产品
	 * @param organization          当前正在执行产品新增操作的父组织机构id
	 * @param current_business_name 当前正在执行产品新增操作的企业名称
	 * @param myOrganization        当前正在执行产品新增操作的企业组织机构id
	 * @param isNew 
	 * 			true  代表 新增
	 * 			false 代表 更新
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/3
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveProduct(Product product, String current_business_name, Long organization, boolean isNew)
				throws ServiceException {
		try {
			if(product==null || current_business_name==null ||
					"".equals(current_business_name) || organization==null){
				throw new Exception("参数为空");
			}
			
			// 1. 处理单位
			if(product.getUnit() != null){
				Unit orig_unit = unitService.findByBusunitName(product.getUnit().getName());
				if(orig_unit == null){
					unitService.create(product.getUnit());
				}else{
					product.setUnit(orig_unit);
				}
			}
			
			// 2. 处理生产企业信息
			BusinessUnit orig_producer = businessUnitServicee.saveProducerName(product.getProducer().getName());
			if(orig_producer!=null){
				product.setProducer(orig_producer);
			}
			
			// 3. 处理品牌
			BusinessBrand orig_brand = businessBrandService.saveBrand(product.getBusinessBrand(), orig_producer, organization);
			if(orig_brand !=null){
				product.setBusinessBrand(orig_brand);
			}
			// 4. 处理产品认证信息
			if(product.getListOfCertification()!=null){
				Set<BusinessCertification> newProCert = new HashSet<BusinessCertification>();
				for(BusinessCertification proCert : product.getListOfCertification()){
					if(proCert.getId()!=null){
						BusinessCertification orig_proCert = businessCertificationService.findById(proCert.getId());
						if(orig_proCert!=null){
							newProCert.add(orig_proCert);
						}
					}
				}
				product.setListOfCertification(newProCert);
			}
			
			// 5. 处理执行标准
			if(product.getRegularity()!=null){
				product.setRegularity(handleRegularity(product.getRegularity()));
			}
			
			// 6. 计算产品营养标签指数
			saveNutriLabel(product);
			
			// 7. 保存产品信息
			if(isNew){
				// 新增产品
				setProductValue(product, organization);
				create(product);
				InitializeProduct initProduct = new InitializeProduct();
				initProduct.setIsLocal(product.isLocal());
				initProduct.setOrganization(organization);
				initProduct.setProduct(product);
				initProduct.setIsDel(false);
				initializeProductService.create(initProduct);
			}else{
				// 更新产品
				if(product.getId() == null){
					throw new Exception("参数 产品 id 为空");
				}
				
				Product orig_product = getDAO().findById(product.getId());
				if(!orig_product.getName().equals(product.getName())){
					traceDataService.updataNameByproductId(product.getId(),product.getName());
				}
				setProductValue(orig_product, product);
				update(orig_product);
				
	            InitializeProduct orig_initProduct = initializeProductService.findByProIdAndOrgId(product.getId(), organization);
	            if(orig_initProduct != null){
	                orig_initProduct.setIsLocal(product.isLocal());
	                initializeProductService.update(orig_initProduct);
	            }
	            
				orig_product = product;
			}
			//保存营业执照,生产许可,流通许可
			UploadUtil uploadUtil = new UploadUtil();
			String ftpPath = PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + product.getBarcode();
			String webUrl = PropertiesUtil.getProperty(SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + product.getBarcode();
			
			List<ProductBusinessLicense> productBusinessLicenseList=productBusinessLicenseService.getProductBusinessLicenseListByProductId(product.getId());
			//删除企业的营业执照
			for(ProductBusinessLicense _productBusinessLicense:productBusinessLicenseList){
				boolean delete=true;
				for(ProductBusinessLicense productBusinessLicense:product.getProductBusinessLicenseList()){
					if(productBusinessLicense.getId()!=null&&productBusinessLicense.getId().equals(_productBusinessLicense.getId())){
						delete=false;
						break;
					}
				}
				if(delete){
					productBusinessLicenseService.delete(_productBusinessLicense);
				}
			}
			
			if(product.getProductBusinessLicenseList()!=null){
				for(ProductBusinessLicense productBusinessLicense:product.getProductBusinessLicenseList()){
					BusinessUnit businessUnit=businessUnitServicee.saveProducerName(productBusinessLicense.getBusinessName());
					ProductBusinessLicense _productBusinessLicense=null;
					if(productBusinessLicense.getId()!=null){
						_productBusinessLicense=productBusinessLicenseService.findById(productBusinessLicense.getId());
					}else{
						productBusinessLicense.setCreateDate(new Date());
					}
					if(productBusinessLicense.getLicResource()!=null){
						if(productBusinessLicense.getLicResource().getFileName()==null){
							productBusinessLicense.setLicResource(null);
							businessUnitServicee.updateBusinessUnit("jg_lic_url",businessUnit.getId(),null);
						}else if(productBusinessLicense.getLicResource().getFile()!=null){
							String fileName=uploadUtil.createFileNameByDate(productBusinessLicense.getLicResource().getFileName());
							boolean isSuccess = uploadUtil.uploadFile(productBusinessLicense.getLicResource().getFile(), ftpPath, fileName);
							if(isSuccess){
								productBusinessLicense.getLicResource().setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
							}else{
								productBusinessLicense.getLicResource().setUrl(webUrl + "/" + fileName);
							}
							businessUnitServicee.updateBusinessUnit("jg_lic_url",businessUnit.getId(), productBusinessLicense.getLicResource().getUrl());
						}else{
							productBusinessLicense.setLicResource(_productBusinessLicense.getLicResource());
						}
					}
					if(productBusinessLicense.getQsResource()!=null){
						if(productBusinessLicense.getQsResource().getFileName()==null){
							productBusinessLicense.setQsResource(null);
							businessUnitServicee.updateBusinessUnit("jg_qs_url",businessUnit.getId(),null);
						}else if(productBusinessLicense.getQsResource().getFile()!=null){
							String fileName=uploadUtil.createFileNameByDate(productBusinessLicense.getQsResource().getFileName());
							boolean isSuccess = uploadUtil.uploadFile(productBusinessLicense.getQsResource().getFile(), ftpPath, fileName);
							if(isSuccess&&UploadUtil.IsOss()){
								productBusinessLicense.getQsResource().setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
							}else{
								productBusinessLicense.getQsResource().setUrl(webUrl + "/" + fileName);
							}
							businessUnitServicee.updateBusinessUnit("jg_qs_url",businessUnit.getId(), productBusinessLicense.getQsResource().getUrl());
						}else{
							productBusinessLicense.setQsResource(_productBusinessLicense.getQsResource());
						}
					}
					if(productBusinessLicense.getDisResource()!=null){
						if(productBusinessLicense.getDisResource().getFileName()==null){
							productBusinessLicense.setDisResource(null);
							businessUnitServicee.updateBusinessUnit("jg_dis_url",businessUnit.getId(),null);
						}else if(productBusinessLicense.getDisResource().getFile()!=null){
							String fileName=uploadUtil.createFileNameByDate(productBusinessLicense.getDisResource().getFileName());
							boolean isSuccess = uploadUtil.uploadFile(productBusinessLicense.getDisResource().getFile(), ftpPath, fileName);
							if(isSuccess){
								if(UploadUtil.IsOss()){
									productBusinessLicense.getDisResource().setUrl(uploadUtil.getOssSignUrl(ftpPath+"/"+fileName));
								}else{
									productBusinessLicense.getDisResource().setUrl(webUrl + "/" + fileName);
								}
							}
							businessUnitServicee.updateBusinessUnit("jg_dis_url",businessUnit.getId(), productBusinessLicense.getDisResource().getUrl());
						}else{
							productBusinessLicense.setDisResource(_productBusinessLicense.getDisResource());
						}
					}
					productBusinessLicense.setBusinessId(businessUnit.getId());
					productBusinessLicense.setProductId(product.getId());
					productBusinessLicense=this.productBusinessLicenseService.update(productBusinessLicense);
				}
			}
			
			// 8. 保存 企业-产品-qs 关系
			if(product.isLocal() && product.getQs_info()!=null){
				productToBusinessUnitService.save(product.getId(), orig_producer.getId(), product.isLocal(), product.getQs_info());
			}
			
			// 9. 如果是进口产品，则保存进口食品信息
			if(product.getImportedProduct()!=null){
				importedProductService.save(product);
			}
			
			// 10. 更新营养报告列表信息
			productNutriService.save(product);
			
			//11. 保存生产企业推荐的url连接地址  ；该新增功能为2015.10.10  author:wubiao
			List<ProductRecommendUrl> recUrlList = product.getProUrlList();
			for (ProductRecommendUrl revo : recUrlList) {
				revo.setProId(product.getId());
				revo.setStatus("0");
				if(revo.getId()==null){
					recommendUrlDAO.persistent(revo);
				}else{
					ProductRecommendUrl urlvo = recommendUrlService.findById(revo.getId());	
					urlvo.setUrlName(revo.getUrlName());
					urlvo.setProUrl(revo.getProUrl());
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException("[ServiceException]ProductServiceImpl.createProduct()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductServiceImpl.createProduct()-->" + e.getMessage(), e);
		}
	}
	
	/**
	 * 背景：报告录入页面
	 * 功能描述：保存产品
	 * @param current_business_id 当前正在执行产品新增操作的企业id
	 * @param myOrganization      当前正在执行产品新增操作的企业组织机构id
	 * @param can_edit_qs         
	 * 				true  代表前台允许用户编辑当前产品的qs号
	 * 				false 代表前台不允许用户更改当前产品的qs号
	 * @throws ServiceException
	 * @author ZhangHui 2015/6/5
	 * @return 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ProductOfMarketVO saveProduct(ProductOfMarketVO product_vo, Long current_business_id, Long organization, boolean can_edit_qs)
				throws ServiceException {
		try {
			if(product_vo==null || organization==null ||
					product_vo.getBarcode()==null || "".equals(product_vo.getBarcode())){
				throw new Exception("参数为空");
			}
			
			Product product = new Product();
			product.setCategory(product_vo.getCategory());
			product.setProAttachments(product_vo.getProAttachments()); // 产品图片
			
			// 1. 处理生产企业信息
			BusinessUnit orig_producer = businessUnitServicee.findById(product_vo.getProducer_id());
			product.setProducer(orig_producer);
			
			Product orig_pro = findByBarcode(product_vo.getBarcode());
			
			if(current_business_id == null){
				// 当前登录企业为结构化人员
				if(orig_pro == null){
					throw new Exception("当前产品不存在");
				}else{
					saveProductInfo(product, product_vo, orig_producer, organization);
					
					// 当产品已经在系统中存在时，只有在该产品没有被认领的情况下，更新产品信息
					setProductValueBeforeUpdate(orig_pro, product_vo, product);
					update(orig_pro);
					
					product_vo.setId(orig_pro.getId());
					return product_vo;
				}
			}
			
			/**
			 * 记录当前qs号有无被现有报告中的企业绑定
			 * 			true  代表 被绑定
			 * 			false 代表 没有被绑定
			 */
			boolean qs_bind = false;
			
			/**
			 * 记录当前产品有无被认领
			 * 			true  代表 被认领
			 * 			false 代表 没有被认领
			 */
			boolean pro_claim = false;
			
			if(orig_pro != null){
				if(current_business_id.equals(product_vo.getProducer_id())){
					qs_bind = true;
				}
				
				if(orig_pro.getProducer()!=null&&orig_pro.getOrganization()!=null && orig_pro.getOrganization().equals(orig_pro.getProducer().getOrganization())){
					pro_claim = true;
				}
			}
			
			if(!product_vo.isCan_edit_pro()){
				if(orig_pro == null){
					throw new Exception("条形码不存在");
				}
				
				// 保存 产品-企业-qs 关系
				if(can_edit_qs){
					productToBusinessUnitService.save(orig_pro.getId(), orig_producer.getId(), qs_bind, product_vo.getQs_info());
				}
				
				product_vo.setId(orig_pro.getId());
				return product_vo;
			}
			
			saveProductInfo(product, product_vo, orig_producer, organization);
			
			// 5. 保存产品
			if(orig_pro == null){
				// 新增产品
				setProductValue(product, organization);
				setProductValue(product, product_vo);
				create(product);
				
				InitializeProduct initProduct = new InitializeProduct();
				if(current_business_id.equals(product_vo.getProducer_id())){
					initProduct.setIsLocal(true);
				}
				initProduct.setOrganization(organization);
				initProduct.setProduct(product);
				initProduct.setIsDel(false);
				initializeProductService.create(initProduct);
			}else if(!pro_claim){
				// 当产品已经在系统中存在时，只有在该产品没有被认领的情况下，更新产品信息
				setProductValueBeforeUpdate(orig_pro, product_vo, product);
				update(orig_pro);
				
				InitializeProduct orig_initProduct = initializeProductService.findByProIdAndOrgId(product.getId(), organization);
	            if(orig_initProduct != null){
	                product.setLocal(orig_initProduct.getIsLocal());
	                initializeProductService.update(orig_initProduct);
	                
	                qs_bind = orig_initProduct.getIsLocal();
	            }
	            
	            product = orig_pro;
			}
			product_vo.setId(product.getId());
			
			// 6. 保存 企业-产品-qs 关系
			if(can_edit_qs){
				productToBusinessUnitService.save(product.getId(), orig_producer.getId(), qs_bind, product_vo.getQs_info());
			}
			
			return product_vo;
		} catch (ServiceException e) {
			throw new ServiceException("[ServiceException]ProductServiceImpl.createProduct()-->" + e.getMessage(), e.getException());
		} catch (Exception e) {
			throw new ServiceException("[Exception]ProductServiceImpl.createProduct()-->" + e.getMessage(), e);
		}
	}
	
	private void saveProductInfo(Product product, ProductOfMarketVO product_vo, BusinessUnit orig_producer, Long organization) throws Exception {
		// 1. 处理单位
		if(product_vo.getUnit()==null || "".equals(product_vo.getUnit())){
			throw new Exception("产品单位不能为空");
		}
		
		Unit orig_unit = unitService.findByBusunitName(product_vo.getUnit());
		if(orig_unit == null){
			Unit unit = new Unit(product_vo.getUnit());
			unitService.create(unit);
			orig_unit = unit;
		}
		product.setUnit(orig_unit);
		
		// 2. 处理品牌
		BusinessBrand brand = new BusinessBrand(product_vo.getBrand_name());
		BusinessBrand orig_brand = businessBrandService.saveBrand(brand, orig_producer, organization);
		product.setBusinessBrand(orig_brand);
		
		// 3. 处理执行标准
		Set<ProductCategoryInfo> orig_regularitys = handleRegularity(product_vo.getRegularity());
		product.setRegularity(orig_regularitys);
	}

	/**
	 * 功能描述：报告录入页面，产品更新前的赋值操作
	 * @throws Exception 
	 * @author ZhangHui 2015/6/7
	 */
	private void setProductValueBeforeUpdate(Product orig_product, ProductOfMarketVO product_vo, Product product) throws Exception {
		if(orig_product==null || product_vo==null || product==null){
			throw new Exception("参数为空");
		}
		
		setProductValue(orig_product, product_vo);
		
		orig_product.setProducer(product.getProducer());
		orig_product.setUnit(product.getUnit());
		orig_product.setBusinessBrand(product.getBusinessBrand());
		orig_product.setRegularity(product.getRegularity());
		
		// 保存产品类别
		setProductCategoryValue(orig_product, product_vo.getCategory());
		
		/**
		 * 处理产品图片
		 */
		Set<Resource> removes = testResourceService.getListOfRemoves(orig_product.getProAttachments(), product.getProAttachments());
		orig_product.removeResources(removes);
		
		Set<Resource> adds = testResourceService.getListOfAdds(product.getProAttachments());
		orig_product.addResources(adds);
	}
	
	/**
	 * 功能描述：报告录入页面，产品新增前赋值操作
	 * @throws Exception
	 * @author ZhangHui 2015/6/7
	 */
	private void setProductValue(Product product, ProductOfMarketVO product_vo) throws Exception {
		if(product==null || product_vo==null){
			throw new Exception("参数为空");
		}
		
		product.setName(product_vo.getName());  // 产品名称
		product.setBarcode(product_vo.getBarcode()); // 产品条形码
		product.setFormat(product_vo.getFormat());  // 规格
		product.setExpiration(product_vo.getExpiration()); // 保质期
		product.setExpirationDate(product_vo.getExpirationDate()); // 质保天数
		product.setStatus(product_vo.getStatus());  // 状态
		product.setRegularity(product_vo.getRegularity());
	}
	
	/**
	 * 赋值操作
	 * @throws ServiceException
	 */
	private void setProductValue(Product product, Long organization) throws ServiceException {
		try {
			product.setOrganization(organization);
			product.setLastModifyTime(new Date());
			product.setQscoreCensor(5f);
			product.setQscoreSample(5f);
			product.setQscoreSelf(5f);
			
			if(product.getCategory().getId()==null){
			    ProductCategoryInfo category = product.getCategory();
			    category.setCategoryFlag(true);
                ProductCategoryInfo orig_productCagegory = productCategoryInfoService.saveCategoryInfo(category);
                product.setCategory(orig_productCagegory);
                product.setSecondCategoryCode(orig_productCagegory.getCategory().getCode());
            }else{
                ProductCategoryInfo orig_productCagegory = productCategoryInfoService.findById(product.getCategory().getId());
                product.setCategory(orig_productCagegory);// 食品分类
                product.setSecondCategoryCode(orig_productCagegory.getCategory().getCode());
            }

			
		} catch (ServiceException sex) {
			throw new ServiceException("ProductServiceImpl.setProductValue()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 按产品id查找一条完整的产品信息（包括营养报告和认证图片信息等）
	 * @throws ServiceException 
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public Product findByProductId(Long id,String identify) throws ServiceException {
		try {
			Product product = findById(id);
			/* 查找营养报告  */
			List<ProductNutrition> listOfNutris = productNutriService.getListOfNutrisByProductId(id);
			product.setListOfNutrition(listOfNutris);
			/* 查找营养报告  */
			List<ProductRecommendUrl>  proUrlList = recommendUrlService.getProductRecommendUrl(id,identify);
			product.setProUrlList(proUrlList);
			
			/* 查找进口食品信息*/
			ImportedProduct impPro = importedProductService.findByProductId(id);
			List<ProductBusinessLicense> productBusinessLicenseList=this.productBusinessLicenseService.getProductBusinessLicenseListByProductId(id);
			product.setProductBusinessLicenseList(productBusinessLicenseList);
			product.setImportedProduct(impPro);
			ProductTransfer.transfer(product);
			return product;
		} catch (ServiceException sex) {
			throw sex;
		}
	}

	/**
	 * 保存产品之前，处理执行标准信息
	 * @param regularitys
	 * @return
	 * @throws ServiceException
	 * @author 郝圆彬
	 */
	private Set<ProductCategoryInfo> handleRegularity(Set<ProductCategoryInfo> regularitys) throws ServiceException{
		Set<ProductCategoryInfo> nowRegularity=new HashSet<ProductCategoryInfo>();
		for(ProductCategoryInfo proRegularity : regularitys){
			ProductCategoryInfo orig_Regularity = null;
			if(proRegularity.getId()!=null){
				orig_Regularity=productCategoryInfoService.findById(proRegularity.getId());
				if(orig_Regularity!=null){
					nowRegularity.add(orig_Regularity);
				}
			}else{
				proRegularity.setCategoryFlag(false);
				orig_Regularity = productCategoryInfoService.saveCategoryInfo(proRegularity);
				if(orig_Regularity!=null){
					nowRegularity.add(orig_Regularity);
				}
			}
		}
		return nowRegularity;
	} 
	
	/**
	 * 更新之前，赋值操作
	 * @param orig_product
	 * @param product
	 * @throws Exception 
	 */
	private void setProductValue(Product orig_product, Product product) throws Exception {
		if(orig_product==null || product==null){
			throw new Exception("参数为空");
		}
		
		orig_product.setName(product.getName());  // 产品名称
		orig_product.setOtherName(product.getOtherName()); // 产品别名
		orig_product.setBarcode(product.getBarcode()); // 产品条形码
		orig_product.setFormat(product.getFormat());  // 规格
		orig_product.setNetContent(product.getNetContent());  // 净含量
		orig_product.setRegularity(product.getRegularity()); // 执行标准
		orig_product.setExpiration(product.getExpiration()); // 保质期
		orig_product.setExpirationDate(product.getExpirationDate());
		orig_product.setStatus(product.getStatus());  // 状态
		orig_product.setCharacteristic(product.getCharacteristic());  // 特色
		orig_product.setCstm(product.getCstm());  // 适宜人群
		orig_product.setIngredient(product.getIngredient());// 配料
		orig_product.setDes(product.getDes()); // 产品描述
		orig_product.setNote(product.getNote()); // 备注
		orig_product.setUnit(product.getUnit()); // 单位
		orig_product.setAgricultureProduct(product.isAgricultureProduct()); //是否农产品
		orig_product.setAreaID(product.getAreaID());//区县
		orig_product.setCityID(product.getCityID());//城市
		orig_product.setProvinceID(product.getProvinceID());//省份
		// 产品分类
		setProductCategoryValue(orig_product, product.getCategory());
		
		// 单位
		orig_product.setUnit(product.getUnit());
		
		// 生产企业信息
		orig_product.setProducer(product.getProducer());
		
		// 品牌
		orig_product.setBusinessBrand(product.getBusinessBrand());
		
		// 产品认证信息
		orig_product.setListOfCertification(product.getListOfCertification());
		
		// 执行标准
		orig_product.setRegularity(product.getRegularity());
		
		// 产品营养成分
		orig_product.setListOfNutrition(product.getListOfNutrition());
		
		// 营养指标
		orig_product.setNutriLabel(product.getNutriLabel());
		
		// 营养指数计算状态
		orig_product.setNutriStatus(product.getNutriStatus());
		
		/**
		 * 处理产品图片
		 */
		Set<Resource> removes = testResourceService.getListOfRemoves(orig_product.getProAttachments(), product.getProAttachments());
		orig_product.removeResources(removes);
		
		Set<Resource> adds = testResourceService.getListOfAdds(product.getProAttachments());
		orig_product.addResources(adds);
	}
	
	/**
	 * 保存产品分类
	 * @throws ServiceException
	 */
	private void setProductCategoryValue(Product orig_product, ProductCategoryInfo category_info) throws ServiceException {
		if(category_info.getId()==null){
			// 如果ID为null，则三级分类为新增的
			category_info.setCategoryFlag(true);
            ProductCategoryInfo orig_productCagegory = productCategoryInfoService.saveCategoryInfo(category_info);
            orig_product.setCategory(orig_productCagegory);
            //新增三级分类
            orig_product.setSecondCategoryCode(orig_productCagegory.getCategory().getCode());
        }else{
            ProductCategoryInfo orig_productCagegory = productCategoryInfoService.findById(category_info.getId());
            orig_product.setCategory(productCategoryInfoService.findById(category_info.getId()));
            orig_product.setSecondCategoryCode(orig_productCagegory.getCategory().getCode());
        }
	}

	/**
	 * 保存营养标签
	 * @author ChenXiaolin
	 * 最后更新者：ZhangHui 2015/6/3
	 * 更新内容：计算营养标签后，增加持久到数据库操作
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveNutriLabel(Product product) {
		String secondCategory = "", nutriStr = "";// 获取二级分类code 显示的营养标签
		ProductCategoryInfo pci = product.getCategory();
		ProductCategory pc = pci != null ? pci.getCategory() : null;
		if (pc != null && pc.getCode() != null) {
			secondCategory = pc.getCode();
		}
		NutritionVO nutritionVO = new NutritionVO();// 传参VO
		nutritionVO.setSecondCategory(secondCategory);// 商品类别
		List<ProductNutrition> nowNutritions = product.getListOfNutrition();
		if(nowNutritions == null || nowNutritions.size() < 1) {
			product.setNutriLabel(nutriStr);
			product.setNutriStatus(NutritionUtil.FAILED); //计算失败
			return ;
		}
		ProductNutrition nutri = nowNutritions.get(0);
		String per = (nutri != null ? nutri.getPer() : "");
		per = per.replace("每", "");
		if (!"".equals(per) && "100g;100ml;420KJ".contains(per)) {
			NutritionProcessor proteinProcess = NutritionUtil.getNutritionProcessor();// 营养成分的处理器
			for (ProductNutrition pn : nowNutritions) {// 循环遍历每一条营养标签，交给责任链去处理，并返回处理结果
				nutritionVO.setPriNutrition(pn);
				nutritionVO = proteinProcess.doProcess(nutritionVO);
			}
			nutritionVO.doProducess(); // 处理暂存 营养成分 的方法,(在整个营养成分List遍历完后调用)
			nutriStr = nutritionVO.labelToString();// 获取标签列表
			if(nutriStr == null || "".equals(nutriStr)){
				product.setNutriStatus(NutritionUtil.FAILED); //计算失败
			} else {
				product.setNutriStatus(NutritionUtil.SUCCESS); //计算成功
			} 
		}
		product.setNutriLabel(nutriStr);
	}
	
	/**
	 * 拼接sql语句
	 * @author ZhangHui 2015/4/11
	 */
	private String getConfigure(String configure) throws ServiceException{
		
		String new_configure = "";
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2], false);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " WHERE " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
	    return new_configure;
	}
	
	/**
	 * 拼接sql语句（忽略品牌）
	 * @author ZhangHui 2015/4/11
	 */
	private String getConfigureOfIgnoreBrand(String configure) throws ServiceException{
		
		String new_configure = "";
		String filter[] = configure.split("@@");
		for(int i=0;i<filter.length;i++){
			String filters[] = filter[i].split("@");
			if(filters.length > 3){
				try {
					String config = splitJointConfigure(filters[0],filters[1],filters[2], true);
					if(config==null){
						continue;
					}
					if(i==0){
						new_configure = new_configure + " WHERE " + config;
					}else{
						new_configure = new_configure +" AND " + config;
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		}
	    return new_configure;
	}
	
	/**
	 * 分割页面的过滤信息
	 * @param field
	 * @param mark
	 * @param value
	 * @param isSon 
	 * @return
	 * @throws ServiceException
	 */
	private String splitJointConfigure(String field, String mark, String value, boolean isIgnoreBrand) throws ServiceException{
		try {
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("【Service-Error】数据筛选时，利用utf-8解码筛选条件时出现异常！",e);
		}
		if(field.equals("id")){
			return FilterUtils.getConditionStr("product.id",mark,value);
		}
		if(field.equals("name")){
			return FilterUtils.getConditionStr("product.name",mark,value);
		}
		if(!isIgnoreBrand && field.equals("brandName")){
			return FilterUtils.getConditionStr("businessBrand.name",mark,value);
		}
		if(field.equals("format")){
			return FilterUtils.getConditionStr("product.format",mark,value);
		}
		if(field.equals("barcode")){
			return FilterUtils.getConditionStr("product.barcode",mark,value);
		}
		if(field.equals("cstm")){
			return FilterUtils.getConditionStr("product.cstm",mark,value);
		}
		if(field.equals("ingredient")){
			return FilterUtils.getConditionStr("product.ingredient",mark,value);
		}
		if(field.equals("nutriStatus")){
			if("成功".indexOf(value) > -1){
				value = "1";
			} else if("失败".indexOf(value) > -1){
				value = "2";
			} else if("未计算".indexOf(value) > -1){
				value = "0";
			}
			return FilterUtils.getConditionStr("product.nutriStatus",mark,value);
		}
		return null;
	}
	
	/**
	 * 获取所有产品列表，有分页处理
	 */
	@Override
	public List<Product> getProductListByConfigWithPage(String configure,
			int page, int pageSize) throws ServiceException {
		try {
			return getDAO().getListByPage(page, pageSize, configure.equals("null")?"":getConfigure(configure));
		} catch (JPAException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		}
	}

	@Override
	public long allCountByConfig(String configure) throws ServiceException {
		try {
			return getDAO().count(configure.equals("null")?"":configure);
		} catch (JPAException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	@Override
	public List<Product> searchProductListByName(String name)
			throws ServiceException {
		try {

			return ProductTransfer.transfer(getDAO().searchProductListByName(name));
		} catch (DaoException jpae) {
			throw new ServiceException(jpae.getMessage(), jpae.getException());
		}
	}

	@Override
	public long getCountByName(String name) throws ServiceException {
		try {
			return getDAO().getCountByName(name);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 按产品名称和规格查找产品集合。（外包装和内包装）
	 * @param name 产品名称
	 * @param format 产品规格
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Product> getListByNameAndFormat(String name, String format) throws ServiceException {
		try {
			return getDAO().getListByNameAndFormat(name, format);
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】按产品名称和规格查找产品集合，出现异常！", jpae.getException());
		}
	}
	
	/**
	 * 按产品别名和规格查找产品集合。（外包装和内包装）
	 * @param name 产品名称
	 * @param format 产品规格
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Product> getListByOtherNameAndFormat(String otherName, String format) throws ServiceException {
		try {
			return getDAO().getListByOtherNameAndFormat(otherName, format);
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】按产品别名和规格查找产品集合，出现异常！", jpae.getException());
		}
	}
	
	/**
	 * 按产品别名和规格查找产品集合。（外包装和内包装）
	 * @param name 产品名称
	 * @param format 产品规格
	 * @param isObscure
	 *        true: 产品名称模糊查询
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Product> getListByOtherNameAndPDFformat(String otherName, String pdfFormat, 
			boolean isObscure) throws ServiceException {
		try {
			return getDAO().getListByOtherNameAndPDFformat(otherName, pdfFormat, isObscure);
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】按产品别名和规格查找产品集合，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按热词查找产品条数。
	 * @param organization 用户组织机构id
	 * @param hotWords  热词集合
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public long countByHotWord(Long organization, String hotWord) throws ServiceException {
		try {
			return getDAO().countByHotWord(organization, hotWord);
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】 按热词查找产品条数，出现异常！", jpae.getException());
		}
	}

	/**
	 * 按热词查找产品集合。
	 * @param organization 用户组织机构id
	 * @param hotWords  热词集合
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Product> getListByHotWordWithPage(Long organization,
			int page, int pageSize, String hotWord) throws ServiceException {
		try {
			return ProductTransfer.transfer(getDAO().getListByHotWordWithPage(organization, hotWord, page, pageSize));
		} catch (DaoException jpae) {
			throw new ServiceException("【service-error】 按热词查找产品集合，出现异常！", jpae.getException());
		}
	}
	
	/**
	 * 根据组织机构查找企业本地产品
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Product> getAllLocalProduct(int page, int size,Long organization) throws ServiceException{
		try {
			return ProductTransfer.transfer(getDAO().getAllLocalProduct(page,size,organization));
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getAllLocalProduct()-->", dex);
		}
	}
	
	/**
	 * 根据组织机构查找企业引进产品列表
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public List<Product> getAllNotLocalProduct(int page, int size,Long organization) throws ServiceException{
		try {
			return ProductTransfer.transfer(productDAO.getAllNotLocalProduct(page,size,organization));
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getAllNotLocalProduct()-->", dex);
		}
	}

	/**
	 * 根据组织机构查找企业本地产品总数
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public Long getCountOfAllLocalProduct(Long organization) throws ServiceException {
		try {
			return getDAO().getCountOfAllLocalProduct(organization);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getCountOfAllLocalProduct()-->", dex);
		}
	}

	/**
	 * 根据组织机构查找企业引进产品总数
	 * @param page 
	 * @param size 
	 * @param organization 
	 * @return
	 * @throws ServiceException 
	 */
	@Override
	public Long getCountOfAllNotLocalProduct(Long organization) throws ServiceException {
		try {
			return getDAO().getCountOfAllNotLocalProduct(organization);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getCountOfAllNotLocalProduct()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	public List<Product> getAllProductsByOrg(long organization){
		return this.productDAO.getAllProductsByOrg(organization);
	}

	/**
	 * 根据barcode、品牌名称、企业id查找品牌id
	 * @param barcode 
	 * @param brandName 
	 * @param bussunitId 
	 * @return Product
	 * @throws ServiceException 
	 */
	@Override
	public Long findByBarcodeAndBrandNameAndBusunitId(String barcode,
			String brandName, Long bussunitId) throws ServiceException {
		try {
			return getDAO().findByBarcodeAndBrandNameAndBusunitId(barcode, brandName, bussunitId);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.findByBarcodeAndBrandNameAndBusunitId()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 根据lims传来的JSON来保存Product
	 * @param productVO
	 * @param organization
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean createProduct(JSONObject productVO, Long organization) {
			String brandName = productVO.getString("businessBrand");
			Product product = new Product();
			String barcode = productVO.getString("barcode");
			try {
				/* 产品图片信息 */
				boolean isProductImg = productVO.containsKey("productImg");
				if(!isProductImg){
					String productImg = productVO.getString("productImg");
					if(!productImg.equals("")||productImg != null){
						product = testResourceService.setImgToProduct(productImg, barcode);
					}
				}
				/* 产品条形码*/
				product.setBarcode(productVO.getString("barcode").replaceAll(" ", ""));
				/* 品牌 */
				BusinessBrand orig_bussBrand = null;
				if(brandName==null || brandName.equals("") || brandName.equals("null")){
					orig_bussBrand = businessBrandService.findByName("--");
				}else{
					orig_bussBrand = businessBrandService.findByName(brandName);
				}
				if(orig_bussBrand == null){
					 BusinessBrand bb = new BusinessBrand();
					 bb.setName(brandName);
					 bb.setBusinessUnit(businessUnitServicee.findByName("--"));					 
					 businessBrandService.create(bb);
					 orig_bussBrand = bb;
				}
				product.setBusinessBrand(orig_bussBrand);
				/* 组织机构*/
				product.setOrganization(organization);
				/* 其他默认值 */
				product.setCharacteristic("");
				product.setQscoreSelf(Float.valueOf("5"));
				product.setQscoreSample(Float.valueOf("5"));
				product.setQscoreCensor(Float.valueOf("5"));
				/* 产品类别 */
				ProductCategoryInfo orig_proCategory = productCategoryInfoService.findByNameAndCategoryIdAndFlag("其他", 400L, true);
				if(orig_proCategory == null){
					ProductCategoryInfo new_proCategory = new ProductCategoryInfo();
					new_proCategory.setName("其他");
					ProductCategory category = categoryService.findById(400L);
					new_proCategory.setCategory(category);
					new_proCategory.setCategoryFlag(true);
					new_proCategory.setDisplay("其他");
					new_proCategory.setAddition(true);
					orig_proCategory = productCategoryInfoService.create(new_proCategory);
				}else if(orig_proCategory.isDel()){
					orig_proCategory.setDel(false);
					productCategoryInfoService.update(orig_proCategory);
				}
				product.setCategory(orig_proCategory);
				ProductCategory pc = orig_proCategory != null ? orig_proCategory.getCategory() : null;
				product.setSecondCategoryCode(pc != null ? pc.getCode() : "1607");
				/* 执行标准 */
				String regularity = productVO.getString("regularity");
				product.setRegularity(productCategoryInfoService.getRegularityByString(regularity, orig_proCategory.getCategory()));
				/*产品名字*/
				product.setName(productVO.getString("name"));
				/*产品规格*/
				product.setFormat(productVO.getString("format"));
				create(product);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
	}
	


	/**
	 * 查询所有的条形码
	 * @return List<String>
	 * @throws ServiceException
	 * @author LongXianZhen
	 */
	@Override
	public List<String> getAllBarcode() throws ServiceException {
		try {
			return getDAO().getAllBarcode();
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getAllBarcode()-->" + dex.getMessage(), dex.getException());
		}
		
		
	}
	/**
	 * 根据产品条形码查找产品
	 * @param barcode 条形码
	 * @author ZhaWanNeng
	 * 更新时间2015/3/17
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public Product findByBarcode(String barcode) throws ServiceException {
		try {
			return ProductTransfer.transfer(getDAO().findByBarcode(barcode));
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}
	/**
	 * 根据名字查找产品
	 * @param sampleName 名字
	 * @author ZhaWanNeng
	 * 更新时间2015/3/17
	 */
	@Override
	public Product findByName(String sampleName) throws ServiceException {
		try {
		return getDAO().findByName(sampleName);
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}

	/**
	 * 重新计算营养指标
	 * @author TangXin
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultVO recalculatedNutri(Long productID) throws ServiceException {
		try{
			ResultVO resultVO = new ResultVO();
			if(productID == null) {
				return resultVO;
			}
			Product orig_product = getDAO().findById(productID);
			List<ProductNutrition> orit_nutris = productNutriService.getListOfNutrisByProductId(productID);
			if(orit_nutris != null && orit_nutris.size() == 0){
				resultVO.setStatus("false");
				resultVO.setMessage("计算失败，当前产品无营养报告信息！");
			} else {
				orig_product.setListOfNutrition(orit_nutris);
				/* 计算产品营养标签指数  */
				saveNutriLabel(orig_product);
				update(orig_product);
				if(orig_product.getNutriStatus() != '1'){
					resultVO.setStatus("false");
					resultVO.setMessage("计算失败，当前产品营养报告的计算结果没有对应的营养指数！");
				}
			}
			return resultVO;
		}catch(Exception e){
			throw new ServiceException("[service-error]:计算商品营养指标时出现异常！" + e.getMessage(), e);
		}
	}
	/**
	 * 产品总数
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public Long productCount() throws ServiceException {
		try {
			return getDAO().productCount();
			} catch (DaoException dex) {
				throw new ServiceException("", dex.getException());
			}
	}
	/**
	 * 产品的营养指数排行
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public List<ProductListVo> getProductList(Long type, int pageSize, int page)
			throws ServiceException {
		try {
			return getDAO().getProductList(type, pageSize, page);
			} catch (DaoException dex) {
				throw new ServiceException("", dex.getException());
			}
	}
	/**
	 * 风险排行接口
	 * @return Long
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public List<ProductRiskVo> riskBillboard(String type, int pageSize, int page)
			throws ServiceException {
		try {
			return getDAO().riskBillboard(type, pageSize, page);
			} catch (DaoException dex) {
				throw new ServiceException("ProductServiceImpl.riskBillboard()-->"+dex.getMessage(), dex.getException());
			}
	}
	/**
	 * 获取产品的的一级分类的code
	 * @param name 一级分类的名称
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public String productCode(String name) throws ServiceException {
		try {
			return getDAO().productCode(name);
			} catch (DaoException dex) {
				throw new ServiceException("ProductServiceImpl.productCode()-->"+dex.getMessage(), dex.getException());
			}
	}
	/**
	 * 获取产品的风险排行的数量
	 * @param code 一级分类的code
	 * @return
	 * @throws DaoException
	 * @author ZhaWanNeng<br>
     * 最近更新人：査万能
     * 最近更新时间：2015/4/10
	 */
	@Override
	public int countriskBill(String code) throws ServiceException {
		try {
			return getDAO().countriskBill(code);
			} catch (DaoException dex) {
				throw new ServiceException("ProductServiceImpl.countriskBill()-->"+dex.getMessage(), dex.getException());
			}
	}

	/**
	 * 根据LIMS传过来的样品信息 新增产品
	 * @param sample
	 * @param organizationID
	 * @return Product
	 * @author LongXianZhen
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public Product saveProduct(SampleVO sample, Long organizationID) {
		String brandName = sample.getBusinessBrand().trim();
		String businessName=sample.getProducer().getName();
		Product product = new Product();
		String barcode = sample.getBarCode().replaceAll(" ", "");
		try {
			/* 产品图片信息 */
			String productImg = sample.getProductImg();
			if(productImg != null&&!productImg.equals("")){
				product = testResourceService.setImgToProduct(productImg, barcode);
			}
			/* 产品条形码*/
			product.setBarcode(barcode);
			
			/* 企业 */
			BusinessUnit bu=null;
			if(businessName==null||businessName.equals("")||businessName.equals("null")){
				bu=businessUnitServicee.findByName("--");
			}else{
				bu=businessUnitServicee.findByName(businessName);
			}
			if(bu==null){
				Map<String,Object> testeeMap=businessUnitServicee.saveBusinessUnit(sample.getProducer());
				bu=(BusinessUnit)testeeMap.get("business");
			}
			/* 品牌 */
			BusinessBrand orig_bussBrand = null;
			if(brandName==null || brandName.equals("") || brandName.equals("null")){
				orig_bussBrand = businessBrandService.findByName("--");
			}else{
				orig_bussBrand = businessBrandService.findByName(brandName);
			}
			if(orig_bussBrand == null){
				 BusinessBrand bb = new BusinessBrand();
				 bb.setName(brandName);
				 if(bu==null){
					 bb.setBusinessUnit(businessUnitServicee.findByName("--"));					 
				 }else{
					 bb.setBusinessUnit(bu);
				 }
				 businessBrandService.create(bb);
				 orig_bussBrand = bb;
			}
			product.setBusinessBrand(orig_bussBrand);
			
			/* 组织机构*/
			product.setOrganization(organizationID);
			/* 其他默认值 */
			product.setCharacteristic("");
			product.setQscoreSelf(Float.valueOf("5"));
			product.setQscoreSample(Float.valueOf("5"));
			product.setQscoreCensor(Float.valueOf("5"));
			/* 产品类别 */
			ProductCategoryInfo orig_proCategory = productCategoryInfoService.findByNameAndCategoryIdAndFlag("其他", 400L, true);
			if(orig_proCategory == null){
				ProductCategoryInfo new_proCategory = new ProductCategoryInfo();
				new_proCategory.setName("其他");
				ProductCategory category = categoryService.findById(400L);
				new_proCategory.setCategory(category);
				new_proCategory.setCategoryFlag(true);
				new_proCategory.setDisplay("其他");
				new_proCategory.setAddition(true);
				productCategoryInfoService.create(new_proCategory);
			}else if(orig_proCategory.isDel()){
				orig_proCategory.setDel(false);
				productCategoryInfoService.update(orig_proCategory);
			}
			product.setCategory(orig_proCategory);
			/* 执行标准 */
			String regularity = sample.getRegularity();
			product.setRegularity(productCategoryInfoService.getRegularityByString(regularity, orig_proCategory.getCategory()));
			/*产品名字*/
			product.setName(sample.getName().trim());
			/*产品规格*/
			product.setFormat(sample.getFormat());
			productDAO.persistent(product);
			return product;
		}catch(ServiceException e){
			((Throwable) e.getException()).printStackTrace();
			return null;
		}catch(JPAException e){
			((Throwable) e.getException()).printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 根据产品barcode获取产品id
	 * @author Zhanghui 2015/4/9
	 */
	@Override
	public Long getIdByBarcode(String barcode) throws ServiceException {
		try {
			return getDAO().getIdByBarcode(barcode);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getIdByBarcode()-->" + dex.getMessage(), dex);
		}
	}

	/**
	 * 
	 * @author Zhanghui 2015/4/9
	 */
	@Override
	public List<Product> getListByConfigure(String configure, Object[] params) throws ServiceException {
		try {
			//return getDAO().getListByCondition(configure, params);
			return getDAO().getProListByCondition(configure);
		} catch (DaoException jpae) {
			throw new ServiceException("ProductServiceImpl.getListByConfigure()-->" + jpae.getMessage(), jpae);
		}
	}

	/**
	 * 经销商只能加载出自己的产品条形码
	 * @author HuangYog
	 * Create date 2015/04/13
	 */
	@Override
	public List<String> getAllBarcode(Long myOrg) throws ServiceException {
		try {
			return getDAO().getAllBarcode(myOrg);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getAllBarcode()-->" + dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 获取产品信息和认证信息（轻量级）
     * @author tangxin	2015/04/15
     * @修改 LongXianZhen 2015/05/06
	 */
	@Override
	public ProductManageViewVO getProductAndCert(Long productId,Long toBusId,Long fromBusId)
			throws ServiceException {
		ProductManageViewVO productVO = null;
		try{
			//根据产品id和商超ID获取供应商
			//FromToBussiness fromBu=fromToBusService.getFromBuByproIdAndToBuId(productId,toBusId);
			//BusinessUnit fBu=null;
			Map<String,String> fBuMap=null;
			if(fromBusId!=null&&fromBusId!=-1){//如果供应商不为null则查询出带证照图片的供应商
				fBuMap=resourceService.getBusinessUnitCertById(fromBusId);
			}
			//Product product = getDAO().findById(productId);
			productVO = getDAO().findByProductManageViewVOByProId(productId);
			if(productVO != null) {
				/* 产品图片，默认取第一张 */
				productVO.setImgList(resourceService.getProductImgListByproId(productVO.getId()));//查询图片集合
				//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
//				if(imgList!=null&&imgList.size()==0){
//					if(productVO.getImgUrl() != null){
//						String[] imgUrlListArray = productVO.getImgUrl().split("\\|");
//						for(String url:imgUrlListArray){
//							Resource re=new Resource();
//							re.setUrl(url);
//							imgList.add(re);
//						}
//					}
//				}
//				if(imgList!=null&&imgList.size()>0){
//					productVO.setImgUrl(imgList.get(0).getUrl());
//				}
				//供应商不为null设置证照图片
				if(fBuMap!=null){
					String licUrl=fBuMap.get("licUrl");
					if(licUrl!=null){
						productVO.setLicImg(licUrl);
					}
					String orgUrl=fBuMap.get("orgUrl");
					if(orgUrl!=null){
						productVO.setOrgImg(orgUrl);
					}
					String disUrl=fBuMap.get("disUrl");
					if(disUrl!=null){
						productVO.setDisImg(disUrl);
					}
				}
			}
		}catch(Exception sex) {
			throw new ServiceException("ProductServiceImpl.getProductAndCert():Error;"+sex.getMessage(),sex);
		}
		return productVO;
	}
	
	/**
	 * 获取没计算风险指数所有的产品
	 * @return
	 * @author ZhaWanNeng	2015/04/17
	 */
	@Override
	public List<Product> getproductList(int pageSize, int page)
			throws ServiceException {
		try {
			return getDAO().getproductList(pageSize,  page);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getproductList()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 根据企业组织机构分页查询产品相册
	 * @author tangxin 2015-05-05
	 */
	@Override
	public List<DetailAlbumVO> getProductAlbums(Long organization, int page, int pageSize ,String cut) throws ServiceException{
		try {
			return getDAO().getProductAlbums(organization, page, pageSize, cut);
		} catch (DaoException dex) {
			throw new ServiceException(dex.getMessage(), dex.getException());
		}
	}

	/**
	 * 判断产品组织机构是否能修改
     * @author LongXianZhen	2015/05/06
	 */
	@Override
	public boolean judgeProductOrgModify(Long organization)
			throws ServiceException {
		try {
			return getDAO().judgeProductOrgModify(organization);
		} catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.judgeProductOrgModify()-->" + dex.getMessage(), dex.getException());
		}
	}
	
	/**
	 * 功能描述：根据产品条形码查找一条已经被生产企业认领的产品信息（如果没有被认领，则返回null）
     * @author ZhangHui 2015/06/04
	 * @throws ServiceException 
	 */
	@Override
	public Product findByBarcodeOfHasClaim(String barCode) throws ServiceException {
		try {
			return getDAO().findByBarcodeOfHasClaim(barCode);
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.findByBarcodeOfHasClaim()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 功能描述：检查产品有无被生产企业认领
	 * @return  true  代表已经被生产企业认领
	 * 			false 代表没有没生产企业认领
	 * @throws ServiceException 
     * @author ZhangHui 2015/06/05
	 */
	@Override
	public boolean checkClaimOfProduct(Long id) throws ServiceException {
		try {
			return getDAO().checkClaimOfProduct(id);
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.checkClaimOfProduct()-->" + e.getMessage(), e.getException());
		}
	}

	@Override
	@Transactional
	public Product checkProduct(Product product) throws ServiceException {
		return productDAO.checkProduct(product);
	}
	/**
	 * author：wubiao
	 * 日期：2015.10.10
	 * 描述：保存销售方推荐的购买url
	 * id非空，表是修改url，否则是添加
	 * @return 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveProUrl(ProductRecommendUrl rulvo) {
	 boolean success = true; 
		try {
			System.out.println(rulvo.getId()==null);
			if(rulvo.getId()!=null&&!"".equals(rulvo.getId()+"")){
				ProductRecommendUrl vo = recommendUrlService.findById(rulvo.getId());
				vo.setProUrl(rulvo.getProUrl());
				vo.setUrlName(rulvo.getUrlName());
			}else{
			    recommendUrlDAO.persistent(rulvo);
			}
		} catch (ServiceException e) {
			success = false;
			e.printStackTrace();
		} catch (JPAException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delUrl(Long id) {
		boolean flag = true;
		try {
			ProductRecommendUrl entity = recommendUrlService.findById(id);
			recommendUrlDAO.remove(entity);
		} catch (ServiceException e) {
			flag = false;
			e.printStackTrace();
		} catch (JPAException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateSpeicalProduct(long productId,boolean isSpeicalProduct){
		try {
			Product product=productDAO.findById(productId);
			product.setSpecialProduct(isSpeicalProduct);
			productDAO.merge(product);
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean setBarcodeToQRcode(String barcode ,Long productID,String QRStart,String QREnd){
		return this.getDAO().setBarcodeToQRcode(barcode,productID,QRStart,QREnd );
	}

	@Override
	public List<ProductBarcodeToQRcodeVO> getBarcodeToQRcode() throws ServiceException{
		try {
			return this.getDAO().getBarcodeToQRcode();
		}catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getBarcodeToQRcode()-->"+dex.getMessage(), dex.getException());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteBarcodeToQRcode(Long id) throws ServiceException {
		try {
			return this.getDAO().deleteBarcodeToQRcode(id);
		}catch (DaoException dex) {
			throw new ServiceException("ProductServiceImpl.getBarcodeToQRcode()-->"+dex.getMessage(), dex.getException());
		}
	}

	/**
	    * 根据条形码查询产品信息以及生产厂商信息
	    * @param barcode
	    * @param model
	    * @return
	    */
	public ProductLismVo getByBarcodeBusProLims(String barcode) throws ServiceException{
			ProductLismVo  busProLims = this.getDAO().getByBarcodeProList(barcode);
           return busProLims;
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int updateProductCertByBarcode(String barcode,int cert){
		return this.productDAO.updateProductCertByBarcode(barcode, cert);
	}
	@Override
	public Product getAllProductsByOrgandid(long organization,long id){
		return this.productDAO.getAllProductsByOrgandid(organization, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Long getByBarcodeProduct(String barcode) {
		
		return productDAO.getByBarcodeProduct(barcode);
	}
}