package com.lhfs.fsn.service.product.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.NutritionReport;
import com.gettec.fsnip.fsn.model.product.PriNutrition;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ImportedProductService;
import com.gettec.fsnip.fsn.service.product.ProductCategoryInfoService;
import com.gettec.fsnip.fsn.service.product.ProductStdCertificationService;
import com.gettec.fsnip.fsn.util.ImgUtils;
import com.lhfs.fsn.dao.product.ProductCategoryDao;
import com.lhfs.fsn.dao.product.ProductDao;
import com.lhfs.fsn.dao.testReport.TestReportDao;
import com.lhfs.fsn.service.product.ProductService;
import com.lhfs.fsn.service.testReport.TestReportService;
import com.lhfs.fsn.util.CharUtil;
import com.lhfs.fsn.vo.atgoo.ProductVO;
import com.lhfs.fsn.vo.business.BussinessUnitVOToPortal;
import com.lhfs.fsn.vo.product.ProductIdAndNameVO;
import com.lhfs.fsn.vo.product.ProductInfoVO;
import com.lhfs.fsn.vo.product.ProductInfoVOToPortal;
import com.lhfs.fsn.vo.product.ProductSortVo;
import com.lhfs.fsn.vo.product.ProductVOWda;
import com.lhfs.fsn.vo.product.SearchProductVO;
import com.lhfs.fsn.web.controller.RESTResult;

@Service(value="productLFService")
public class ProductServiceImpl extends BaseServiceImpl<Product, ProductDao> 
		implements ProductService {
	@Autowired private ProductDao productLFDAO;
	@Autowired private TestReportDao testReportDao;
	@Autowired private TestReportService testReportService;
	@Autowired private ProductStdCertificationService productStdCertificationService;
	@Autowired private ImportedProductService importedProductService;
	@Autowired private ResourceService resourceService;
	@Autowired private ProductionLicenseService productionLicenseService;
	@Autowired private BusinessUnitService businessUnitService;
	@Autowired private ProductCategoryInfoService productCategoryInfoService;
	
	@Override
	public ProductDao getDAO() {
		return productLFDAO;
	}

	/**
	 * 根据产品id获取产品信息
	 * @param id
	 * @return Product
	 */
	@Transactional(readOnly=true)
	public Product findProductById(long id) {
		try {
			
			//Product prod = findById(id);
			Product prod =productLFDAO.findProductBasicById(id);
			if(prod == null)
				return null;
			completeProduct(prod);
			return prod;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 对图片尺寸按要求进行处理(目前只针对手机APP)
	 */
	@Override
	public Product findProductById(long id, int proImgWidth, int proImgHeight, 
			int certImgWidth, int certImgHeight, int docImgWidth, int docImgHeight){
		/* 1.获取产品 */
		Product product = findProductById(id);
		/* 2.按规则重新生成产品图片链接地址 */
		String new_proImgUrl = "";
		String old_imgUrls = product.getImgUrl();
		String[] old_imgUrls_array = old_imgUrls.split("\\|");
		for(int i=0; i<old_imgUrls_array.length; i++){
			new_proImgUrl += ImgUtils.getImgPath(old_imgUrls_array[i], proImgWidth, proImgHeight) + "|";
		}
		product.setImgUrl(new_proImgUrl);
		/* 3.按规则重新生成认证图片链接地址 */
		List<Certification> certs = product.getProductCertification();
		for(Certification cert : certs){
			String new_certImgUrl = "";
			String new_certDocUrl = "";
			new_certImgUrl += ImgUtils.getImgPath(cert.getImgUrl(), certImgWidth, certImgHeight);
			cert.setImgUrl(new_certImgUrl);
			new_certDocUrl += ImgUtils.getImgPath(cert.getDocumentUrl(), docImgWidth, docImgHeight);
			cert.setDocumentUrl(new_certDocUrl);
		}
		/* 3.返回结果 */
		return product;
	}

	/**
	 * 根据产品id获取营养报告信息
	 * @param id
	 * @return NutritionReport
	 */
    public NutritionReport findNutritionById(long id) {
		List<Object[]> list = getDAO().findNutritionById(id);
		NutritionReport report = new NutritionReport();
		List<PriNutrition> nlist = new ArrayList<PriNutrition>();
		if (list != null && list.size()>0) {
			Object[] obj = list.get(0);
			report.setCstm(obj[1]!=null?obj[1].toString():"");
			report.setIngredient(obj[2]!=null?obj[2].toString():"");
			report.setPer(obj[5]!=null?obj[5].toString():"");
		}
		for (Object[] obj : list) {
			PriNutrition nutrition = new PriNutrition();
			nutrition.setName(obj[3]!=null?obj[3].toString():"");
			nutrition.setUnit(obj[4]!=null?obj[4].toString():"");
			nutrition.setPer(obj[5]!=null?obj[5].toString():"");
			nutrition.setValue(obj[6]!=null?obj[6].toString():"");
			nutrition.setDailyIntake(obj[7]!=null?obj[7].toString():"");
			nutrition.setNrv(obj[8]!=null?obj[8].toString():"");
			nlist.add(nutrition);
		}
		report.setList(nlist);
		return report;
	}
	
    /**
     * 构建产品详细信息（大众门户接口）
     * updater tangxin 2015/03/31
     */
	public void completeProduct(Product prod){
		try {
			/*Set<BusinessCertification> tPCList = prod.getListOfCertification();
			List<Certification> tCList = new ArrayList<Certification>();
			Certification t_c = new Certification();
			if (tPCList != null){
				for (BusinessCertification t_pc : tPCList) {
					t_c = t_pc.getCert();
					t_c.setDocumentUrl(t_pc.getCertResource().getUrl());
					tCList.add(t_c);
					t_c = null;
				}
			}*/
			/* 处理生产许可证 */
			//completeQS(prod,tCList);
			/* 处理营养指数 */
			String nutriLabel = prod.getNutriLabel() != null ? prod.getNutriLabel() : "";
			nutriLabel = nutriLabel.replaceAll(";", "|");
			if(nutriLabel.endsWith("|")) {
				nutriLabel = nutriLabel.substring(0, nutriLabel.length()-1);
			}
			prod.setNutriLabel(nutriLabel);
			//prod.setProductCertification(tCList);
			prod.setReport(findNutritionById(prod.getId()));
			/* 处理产品类别 */
			ProductCategoryInfo pc=productCategoryInfoService.findById(prod.getCategory().getId());
			prod.setCategory(pc);
			//大众门户调用时给 portalFlag = true
			boolean portalFlag = true;
			Integer trCensorNum=testReportDao.countByIdAndType(prod.getId(), "企业送检",null,portalFlag);
			Integer trSampleNum=testReportDao.countByIdAndType(prod.getId(), "政府抽检",null,portalFlag);
			Integer trSelfNum=testReportDao.countByIdAndType(prod.getId(), "企业自检",null,portalFlag);
			
			prod.setHasCensor(trCensorNum>0?true:false);
			prod.setHasSample(trSampleNum>0?true:false);
			prod.setHasSelf(trSelfNum>0?true:false);
			
			prod.setSelfCount(trSelfNum);
			prod.setCensorCount(trCensorNum);
			prod.setSampleCount(trSampleNum);

		/*	String imgUrl = prod.getImgUrl();
			if(imgUrl != null){
				String[] imgUrlList = imgUrl.split("\\|");
				prod.setImgUrlList(imgUrlList);
			}*/
			
			TestRptJson tSelf=testReportService.getReportJson(prod.getId(), "企业自检", 1, null, false, null,portalFlag);
			prod.setSelfRpt(tSelf);
			
			TestRptJson tCensor=testReportService.getReportJson(prod.getId(), "企业送检", 1, null, false, null,portalFlag);
			prod.setCensorRpt(tCensor);
			
			TestRptJson tSample = testReportService.getReportJson(prod.getId(), "政府抽检", 1, null, false, null,portalFlag);
			prod.setSampleRpt(tSample);
			
			//大众门户VO查询某个产品详情接口时,返回同类产品,返回12条数据
			List<ProductSortVo> recommandProductList = productLFDAO.getClassifyProductByid(prod.getId(), 12, 1);
			//处理产品图片
			for(ProductSortVo pSortVo : recommandProductList){
				List<Resource> imgList=resourceService.getProductImgListByproId(pSortVo.getId());//查找产品图片集合
        		if(imgList.size()>0){
        			pSortVo.setImgUrl(imgList.get(0).getUrl());
        		}else{
        			String imgUrl = pSortVo.getImgUrl();
        			if(imgUrl != null){
        				String[] imgUrlListArray = imgUrl.split("\\|");
        				if(imgUrlListArray.length>0){
        					pSortVo.setImgUrl(imgUrlListArray[0]);
        				}
        			}
        		}
			}
			prod.setRecommandProductList(recommandProductList);
			/* 查询产品进口食品信息 */
			//if(prod.getProductType()!=1){
			ImportedProduct impPro=importedProductService.findByProductId(prod.getId());
			prod.setImportedProduct(impPro);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 处理产品的生产许可信息
	 * @author longxianzhen 2015/06/26
	 */
	private void completeQS(Product prod, List<Certification> tCList) throws ServiceException {
		List<ProductionLicenseInfo> pls=productionLicenseService.getListByProId(prod.getId());
		Certification ce=productStdCertificationService.findByName("生产许可");
		for(ProductionLicenseInfo pl:pls){
			Set<Resource> qsAttas=pl.getQsAttachments();
			if(qsAttas.size()<=0){
				continue;
			}
			Certification cert=new Certification();
			
			cert.setImgUrl(ce.getImgUrl());
			cert.setName("生产许可");
			String documentUrl=qsAttas.iterator().next().getUrl();
			cert.setDocumentUrl(documentUrl);
			tCList.add(cert);
		}
	}

	/**
	 * 获取热点产品的总数
	 * @throws ServiceException 
	 */
	@Override
	public long countOfHotProduct() throws ServiceException {
		try {
			return getDAO().countOfHotProduct();
		} catch (DaoException dex) {
			throw new ServiceException("获取热点产品的总数，出现异常！", dex);
		}
	}
	
    /**
     * 根据产品的报告数量降序排列
     * 返回商品
     * @param page
     * @param pageSize
     * @return List<Product>
     * @throws ServiceException
     */
	@Override
    public List<SearchProductVO> getHotProductByProductId( Long productId) throws ServiceException {
		try {
			/* 1. 获取热点企业基本信息列表 */
			List<SearchProductVO> listProductVO = getDAO().getHotProductByProductId(productId);
        	/* 2. 添加认证信息 */
			setProductCert(listProductVO);
        	return listProductVO;
        } catch (DaoException dex) {
            throw new ServiceException("ProductServiceImpl.getHotProductByProductId()-->" + dex.getMessage(), dex.getException());
        }
    }
	
	/**
     * 根据产品的报告数量降序排列,并将指定的产品id集合过滤掉
     * 返回商品
     * @param page
     * @param pageSize
     * @return List<Product>
     * @throws ServiceException
     */
	@Override
    public List<SearchProductVO> getListOfHotProductWithPage(String productIds, int page, int pageSize) throws ServiceException {
		try {
			String condition = "";
			if(productIds!=null && !productIds.equals("")){
				condition = " and pro.id not in("+productIds+") ";
			}
			/* 1. 获取热点企业基本信息列表 */
			List<SearchProductVO> listProductVO = getDAO().getListOfHotProductWithPage(condition, page,pageSize);
        	/* 2. 添加认证信息 */
			setProductCert(listProductVO);
        	return listProductVO;
        } catch (DaoException dex) {
            throw new ServiceException("[DaoException]ProductServiceImpl.getListOfHotProductWithPage()-->" + dex.getMessage(), dex.getException());
        } catch (ServiceException sex) {
            throw new ServiceException("[ServiceException]ProductServiceImpl.getListOfHotProductWithPage()-->" + sex.getMessage(), sex.getException());
        }
    }
	
	/**
	 * 给产品添加认证信息
	 * @param listProductVO
	 * @throws ServiceException
	 */
	private void setProductCert(List<SearchProductVO> listProductVO) throws ServiceException {
		try {
			if(listProductVO!=null&&listProductVO.size()>0){
	    		for(SearchProductVO spvo:listProductVO){
	        		spvo.setProductCertification(
	        			productStdCertificationService.getListOfStandCertificationByProductId(spvo.getId()));

	        		/**
	        		 * 处理产品图片
	        		 * 当资源表不为空时取资源表的图片
	        		 * 为空时取取产品表拼接的图片
	        		 * @author longxianzhen 2015/06/11
	        		 */
	        		List<Resource> imgList=resourceService.getProductImgListByproId(spvo.getId());//查找产品图片集合
	        		if(imgList.size()>0){
	        			spvo.setImgList(imgList);
	        		}else{
	        			String imgUrl = spvo.getImgUrl();
	        			if(imgUrl != null){
	        				String[] imgUrlListArray = imgUrl.split("\\|");
	        				for(String url:imgUrlListArray){
	        					Resource re=new Resource();
	        					re.setUrl(url);
	        					imgList.add(re);
	        				}
	        				spvo.setImgList(imgList);
	        			}
	        		}
	        		/* 判断该产品是不是进口产品 */
	        		ImportedProduct impPro=null;
					try {
						impPro = importedProductService.findByProductId(spvo.getId());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
	        		if(impPro==null||"中国".equals(impPro.getCountry().getName())){
	        			spvo.setImportedProduct(false);
	        		}else{
	        			spvo.setImportedProduct(true);
	        		}
	        	}
	    	}
		} catch (ServiceException sex) {
			throw new ServiceException("ProductServiceImpl.setProductCert() 给产品添加认证信息,出现异常！", sex.getException());
		}
	}

	/**
	 * 已知产品类别，查找报告最多的若干个产品
	 */
	@Override
	public List<SearchProductVO> getListOfProductByCategory(String category, int page, int pageSize) throws ServiceException {
		List<SearchProductVO> listProductVO=null;
		try {
			listProductVO = getDAO().getListOfProductByCategory(category, page, pageSize);
			if(listProductVO!=null&&listProductVO.size()>0){
				for(SearchProductVO spvo:listProductVO){
					spvo.setSelfCount(testReportDao.countByProductIdAndTestType(spvo.getId(), "企业自检"));
					spvo.setCensorCount(testReportDao.countByProductIdAndTestType(spvo.getId(), "企业送检"));
					spvo.setSampleCount(testReportDao.countByProductIdAndTestType(spvo.getId(), "政府抽检"));
				}
			}
			return listProductVO;
		} catch (DaoException dex) {
			throw new ServiceException("已知产品类别，查找报告最多的若干个产品，出现异常！", dex);
		}
	}
	
	/**
     * 已知企业名称，按产品名称模糊查询返回商品列表
     * @param page
     * @param pageSize 
     * @throws ServiceException
     * @updater TangXin 2015/03/31
     */
	@Override
	public List<Object[]> getListOfProductByName(String enterpriseName,String category,Integer catLevel,String brand,
			int page, int pageSize, String ordername, String ordertype, String nutriLabel) throws ServiceException {
		try {
			List<Object[]> listOfProductOrderByCountOfReport = getDAO().getListOfProductByName2(enterpriseName,category,catLevel,brand,
					page,pageSize, ordername, ordertype, nutriLabel);
			return listOfProductOrderByCountOfReport;
		} catch (DaoException dex) {
			throw new ServiceException("已知企业名称，按产品名称模糊查询返回商品列表，出现异常！", dex);
		}
	}
	
	/**
     * 给食安监提供的根据产品id 得到产品基本信息和产品认证
     * @param id
     * @return ProductInfoVO
     */
    @Override
    public ProductInfoVO getProductInfoAndCertById(Long id) {
        ProductInfoVO productInfoVO = new ProductInfoVO();
        try {
            if (id.longValue() != -1) {
                Product product = findById(id);
                if (product != null) {
                    productInfoVO.setId(product.getId()); 
                    productInfoVO.setName(product.getName());
                    productInfoVO.setBarcode(product.getBarcode());
                    productInfoVO.setBusinessBrand(product.getBusinessBrand().getName()); //商标
                    productInfoVO.setCategory(product.getCategory().getName()); //  产品类别
                    productInfoVO.setExpirationDate(product.getExpirationDate()); // 保质天数
                    productInfoVO.setFormat(product.getFormat());
                    productInfoVO.setImgUrlList(product.getImgUrl());//产品图片
                    productInfoVO.setProductCertification(product.getListOfCertification()); // 认证信息
                    String regularitys = "";
    				for(ProductCategoryInfo regularity : product.getRegularity()){
    					if(regularity.getName()==null||regularity.getName()==""){
    						continue;
    					}else{
    						regularitys = regularitys+regularity.getName()+";";
    					}
    				}
                    productInfoVO.setRegularity(regularitys); //  执行标准
                    productInfoVO.setDes(product.getDes());// 产品介绍
                    productInfoVO.setIngredient(product.getIngredient());//产品配料
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return productInfoVO;
    }

    /**
     * 根据传来的产品barcode 获取product的 商品 id,名称、商标 和 图片
     * @param barcode
     * @return Map<String, String>
     * @throws ServiceException
     */
	@Override
	public Map<String, String> getProductInfoSomeByBarcode(String barcode)
			throws ServiceException {
		try {
			Map<String, String> map = new HashMap<String, String>();
			Product product = getDAO().findByBarcode(barcode);
			map.put("id", product.getId()+"");
			map.put("name", product.getName());
			map.put("businessBrand", product.getBusinessBrand().getName());
			map.put("imageUrl", product.getImgUrl().split("\\|")[0]);
			return map;
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getProductInfoSomeByBarcode()-->" + e.getMessage(), e.getException());
		}
	}

	/**
	 * 根据商品名称或是条码，或是名称和条目得到相关商品
     * @param name 商品名称
     * @param barcode 商品barcode
     * @return List<ProductInfoVO> 
	 */
    @Override
    public List<Product> getListOfProduct(int page,int pageSize,String name, String barcode,Long orgId) throws ServiceException {
        try {
        	return getDAO().findByNameAndBarcode(page,pageSize,name,barcode,orgId);
        } catch (Exception dex) {
            throw new  ServiceException("ProductServiceImpl.getListOfProduct()-->" + dex.getMessage(), dex.getMessage());
        }
    }

    /**
     * 根据商品名称或是条码，或是名称和条目得到相关商品 的总条数
     * @param name 商品名称
     * @param barcode 商品barcode
     * @return long 
     */
    @Override
    public long getListOfProductCount(String name, String barcode,long orgId) throws ServiceException {
        
        try {
            return getDAO().getListOfProductCount(name,barcode,orgId);
        } catch (DaoException dex) {
            throw new  ServiceException("ProductServiceImpl.getListOfProductCount()-->" + dex.getMessage(), dex.getException());
        }
    }

    /**
     * 根据批次或是生产日期范围，得到相关商品的总条数
     * @param batch 批次
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return long
     */
    @Override
    public Long getListOfProductCountByBatchOrProductDate(String batch,
            String startTime, String endTime,long productId) throws ServiceException {
        try {
            return getDAO().getListOfProductCountByBatchOrProductDate(batch,startTime,endTime,productId);
        } catch (DaoException dex) {
            throw new  ServiceException("ProductServiceImpl.getListOfProductCountByBatchOrProductDate()-->" + dex.getMessage(), dex.getException());
        }
    }

    /**
     * 根据批次或是生产日期范围，得到相关商品
     * @param page 当前页
     * @param pageSize 显示条数
     * @param batch 批次
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<ProductInfoVO>
     */
    @Override
    public List<ProductInfoVO> getListOfProductByBatchOrProductDate(int page,
            int pageSize, String batch, String startTime, String endTime,long productId)
            throws ServiceException {
        List<ProductInfoVO> productInfoVOs = new ArrayList<ProductInfoVO>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Product product = null;
        ProductInfoVO productInfoVO = null;
        String endT = null;
        try {
        	product = getDAO().findById(productId);
			if (startTime != "" && endTime == "") {
				endTime = sdf.format(new Date());
			}else if(endTime != ""){
				Calendar cc = Calendar.getInstance();
				cc.setTime(sdf.parse(endTime));
				int day = cc.get(Calendar.DATE);
				cc.set(Calendar.DATE, day + 1);
				endT = sdf.format(cc.getTime());
			}
        	List<ProductInstance> productInstList = getDAO().getListOfProductInstByBatchOrProductDate(page,pageSize,batch,startTime,endT,productId);
            if (productInstList!=null) {
                for (ProductInstance roductInstance : productInstList) {
                    productInfoVO = new ProductInfoVO();
                    productInfoVO.setId(roductInstance.getId());
                    productInfoVO.setName(product.getName());
                    productInfoVO.setBarcode(product.getBarcode());
                    productInfoVO.setExpiration(product.getExpiration());
                    productInfoVO.setBatch(roductInstance.getBatchSerialNo());
                    productInfoVOs.add(productInfoVO);
                }

            }
            return productInfoVOs;
        } catch (Exception dex) {
            throw new  ServiceException("ProductServiceImpl.getListOfProductByBatchOrProductDate()-->" + dex.getMessage(), dex.getMessage());
        }
    }
	
    /**
     * 根据企业名称或是营业执照号 ，获取该企业下经营的所有产品
     * @param name 企业名称
     * @param licenseNo 营业执照号
     * @return List<ProductInfoVO>
     */
    @Override
    public List<ProductInfoVO> loadProductListForBusinessUnit(String name,String licenseNo) throws ServiceException {
        try {
        	List<Product> productList = getDAO().loadProductListForBusinessUnit(name,licenseNo);
        	List<ProductInfoVO> productInfoVOs = new ArrayList<ProductInfoVO>();
        	if (productList.size() > 0) {
        		for (Product product : productList) {
        			ProductInfoVO productInfoVO = new ProductInfoVO();
        			productInfoVO.setId(product.getId());
        			productInfoVO.setName(product.getName());
        			productInfoVO.setBarcode(product.getBarcode());
        			productInfoVO.setExpiration(product.getExpiration());
        			productInfoVOs.add(productInfoVO);
        		}
        	}	
        	return productInfoVOs;
        } catch (DaoException dex) {
        	throw new  ServiceException("ProductServiceImpl.getListOfProduct()-->" + dex.getMessage(), dex.getException());
    	}
    }
    
    /**
     * 统计超市没有报告的产品
     * @param organizationId
     * @return
     * @throws ServiceException
     */
    public long countMarketProduct(Long organizationId, boolean isHaveReport,String name,String barcode) throws ServiceException {
    	try{
    		return getDAO().countMarketProduct(organizationId,isHaveReport,name,barcode);
    	}catch(DaoException daoe){
    		throw new ServiceException("ProductServiceImpl.countMarketProduct() "+daoe.getException(),daoe);
    	}
    }

	@Override
	public List<ProductInstance> getProductInstanceListById(int page,int pageSize,Long id)
			throws ServiceException {
		try {
            return getDAO().getProductInstanceListById(page,pageSize,id);
        } catch (DaoException dex) {
            throw new  ServiceException("ProductServiceImpl.getProductInstanceListById()-->" + dex.getMessage(), dex.getException());
        }
	}

	@Override
	public Long getCountByproId(Long id) throws ServiceException {
		try {
            return getDAO().getCountByproId(id);
        } catch (DaoException dex) {
            throw new  ServiceException("ProductServiceImpl.getCountByproId()-->" + dex.getMessage(), dex.getException());
        }
	}    
    
    /**
     *  监管系统获取超市没有报告产品
     */
	@Override
	public List<ProductVOWda> getListOfMarketByMarketIdWithPage(Boolean flag,Long busId, boolean isHaveReport,
			int page, int pageSize,String name,String barcode) throws ServiceException {
		try{
			return getDAO().getListOfMarketByMarketIdWithPage(flag,busId, isHaveReport, page, pageSize,name,barcode);
		}catch(DaoException jpae){
			throw new ServiceException("ProductServiceImpl.getListOfMarketByMarketIdWithPage() "+jpae.getMessage(),jpae.getException());
		}
	}

	/**
	 * 监管接口，根据organization统计没有生产许可证的产品数量
	 * @param organization
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public long countNoneProlicinfoByOrgId(Long organization,String name,String barcode) throws ServiceException{
		try{
			return getDAO().countNoneProLicinfoByOrganization(organization,name,barcode);
		}catch(DaoException daoe){
			throw new ServiceException("ProductServiceImpl.countNoneProlicinfoByOrgId() "+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 监管接口，查询超市没有生产许可证的产品列表
	 * @param organization
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ProductVOWda> getListOfMarketNoneProlicinfoByOrgIdWithPage(Boolean flag,Long organization,
			int page,int pageSize,String name,String barcode) throws ServiceException{
		try{
			return getDAO().getListOfMarketNoneProlicinfoByOrgIdWithPage(flag,organization, page, pageSize,name,barcode);
		}catch(DaoException daoe){
			throw new ServiceException("ProductServiceImpl.getListOfMarketNoneProlicinfoByOrgIdWithPage() "+daoe.getMessage(),daoe);
		}
	}

	/**
	 * 食安监接口：根据产品的barcode 查找相关的batch（模糊查询）
	 */
	@Override
	public List<Object> getBatchForBarcode(String barcode)throws ServiceException {
		try{
			return getDAO().getBatchForBarcode(barcode);
		}catch(DaoException daoe){
			throw new ServiceException("ProductServiceImpl.getBatchForBarcode() "+daoe.getMessage(),daoe);
		}
	}

	//cxl
	@Override
	public List<Product> getListOfMarketAllProductsWithPage(
			Long organization, int page, int pageSize) throws ServiceException {
		try{
			return getDAO().getListOfMarketAllProductsWithPage(organization,page,pageSize);
		}catch(DaoException daoe){
			throw new ServiceException("ProductServiceImpl.getListOfMarketAllProductsWithPage() "+daoe.getMessage(),daoe);
		}
	}

	//cxl
	@Override
	public long countMarketAllProduct(Long organization)
			throws ServiceException {
		try{
			return getDAO().countMarketAllProduct(organization);
		}catch(DaoException daoe){
			throw new ServiceException("ProductServiceImpl.countMarketAllProduct() "+daoe.getMessage(),daoe);
		}
	}

	/**
	 * * 根据产品条形码查找该产品
	 * @param barcode 产品条形码
	 * @return Product
	 */
	@Override
	public Product findProductByBarcode(String barcode) throws ServiceException {
		try{
			return getDAO().findByBarcode(barcode);
		}catch(Exception daoe){
			throw new ServiceException("ProductServiceImpl.countMarketAllProduct() "+daoe.getMessage(),daoe);
		}
	}

	/**
	 * 根据名称和营业执照号获取产品数量
	 * @param name
	 * @param licenseNo
	 */
	@Override
	public long getCountByBusNameOrLisNo(String name, String licenseNo)
			throws ServiceException {
		try{
			return getDAO().getCountByBusNameOrLisNo(name,licenseNo);
		}catch(Exception daoe){
			throw new ServiceException("ProductServiceImpl.getCountByBusNameOrLisNo() "+daoe.getMessage(),daoe);
		}
	}
	
	/**
	 * 根据指定商品列和值查询满足条件的商品列表
	 * @param queryName
	 * @param queryParam
	 * @return
	 */
	@Override
	public RESTResult<ProductIdAndNameVO> searchProductInfoByPQ(String queryName,
			String queryParam) {
		List<ProductIdAndNameVO> list = null;
		try {
			queryParam = CharUtil.changeURLCode(queryParam, "UTF-8");
			boolean bCn = CharUtil.isChinese(queryParam.substring(0, 1)); // 传入的参数第一个字符时中文还是字母
			list = productLFDAO.searchBrandInfoByPQ("name", queryParam, bCn);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new RESTResult<ProductIdAndNameVO>(1,list);
	}
	
	/**
	 * Portal 接口:根据产品id集合返回产品集合
	 * @author LongXianZhen 2015/06/25
	 */
	@Override
	public List<ProductIdAndNameVO> getProductByProIds(String proIds)
			throws ServiceException {
		try {
			 List<ProductIdAndNameVO> pros=productLFDAO.getProductByProIds(proIds);
			 for(ProductIdAndNameVO pVO:pros){
				 /*处理产品图片*/
			        List<Resource> imgList = resourceService.getProductImgListByproId(pVO.getId());//查找产品图片集合
					//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
					if(imgList==null||imgList.size()==0){
						if(pVO.getImgUrl() != null){
							String[] imgUrlListArray = pVO.getImgUrl().split("\\|");
							for(String url:imgUrlListArray){
								Resource re=new Resource();
								re.setUrl(url);
								imgList.add(re);
							}
						}
					}
					if(imgList.size()>0){
						pVO.setImgUrl(imgList.get(0).getUrl());
					}
			 }
			 return pros;
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getProductByProIds() "+e.getMessage(),e.getException());
		}
	}
	
	/**
	 * 获取进口食品列表
	 * @author LongXianZhen 2015/07/01
	 */
	@Override
	public List<ProductIdAndNameVO> getImportProductList(int page, int pageSize)
			throws ServiceException {
		try {
			List<ProductIdAndNameVO> pros=productLFDAO.getImportProductList(page,pageSize);
			 for(ProductIdAndNameVO pVO:pros){
				 /*处理产品图片*/
			        List<Resource> imgList = resourceService.getProductImgListByproId(pVO.getId());//查找产品图片集合
					//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
					if(imgList==null||imgList.size()==0){
						if(pVO.getImgUrl() != null){
							String[] imgUrlListArray = pVO.getImgUrl().split("\\|");
							for(String url:imgUrlListArray){
								Resource re=new Resource();
								re.setUrl(url);
								imgList.add(re);
							}
						}
					}
					if(imgList.size()>0){
						pVO.setImgUrl(imgList.get(0).getUrl());
					}
			 }
			 return pros;
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getImportProductList() "+e.getMessage(),e.getException());
		}
	}

	/**
	 * 获取进口食品数量
	 * @author LongXianZhen 2015/07/01
	 */
	@Override
	public int getImportProductCount() throws ServiceException {
		try{
			return productLFDAO.getImportProductCount();
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getImportProductList() "+e.getMessage(),e.getException());
		}
	}
	/**
	 * 获取进口食品专区报告数量
	 * @return
	 * @throws ServiceException
	 */
	public int getImportProductReportCount() throws ServiceException{
		try{
			return productLFDAO.getImportProductReportCount();
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getImportProductReportCount() "+e.getMessage(),e.getException());
		}
	}
	
	/**
	 * 根据产品id获取 ProductVO 目前提供给爱特购系统使用
	 * @param id 产品id
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public ProductVO findById_(Long id) throws ServiceException{
		try{
			return getDAO().findById_(id);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 根据产品 barcode 获取 ProductVO 目前提供给爱特购系统使用
	 * @param barcode 产品条形码
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public ProductVO findByBarcode_(String barcode) throws ServiceException{
		try{
			return getDAO().findByBarcode_(barcode);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 根据产品id获取 ProductVO 目前提供给爱特购系统使用
	 * @param id 产品id
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List findTestResultByBarcode(List<String> barCodes) throws ServiceException{
		try{
			return getDAO().findTestResultByBarcode(barCodes);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 根据产品 barcode 获取 ProductVO 目前提供给爱特购系统使用
	 * @param barcode 产品条形码
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List findTestResultByProductIds(List<String> products) throws ServiceException{
		try{
			return getDAO().findTestResultByProduct(products);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}

	/**
	 * 根据条形码查询食品详细信息
	 * @author LongXianZhen 2015/08/06
	 */
	@Override
	public ProductInfoVOToPortal getProductInfoByBarcode(String barcode)
			throws ServiceException {
		try{
			ProductInfoVOToPortal pro=productLFDAO.getProductInfoByBarcode(barcode);
			if(pro!=null){
				pro.setNutritionReport(findNutritionById(pro.getId()));//获取营养报告
				/* 获取检测报告 */
				/*List<TestResult> trCensorList = testReportDao.getResultListByIdAndType(pro.getId().longValue(), "企业送检");
				List<TestResult> trSampleList = testReportDao.getResultListByIdAndType(pro.getId().longValue(), "政府抽检");
				List<TestResult> trSelfList = testReportDao.getResultListByIdAndType(pro.getId().longValue(), "企业自检");
				pro.setSelfRpt(testReportService.getReportJson(pro.getId().longValue(), "企业自检", 1, null, true, trSelfList));
				pro.setCensorRpt(testReportService.getReportJson(pro.getId().longValue(), "企业送检", 1, null, true, trCensorList));
				pro.setSampleRpt(testReportService.getReportJson(pro.getId().longValue(), "政府抽检", 1, null, true, trSampleList));*/
				//portal调用和大众门户调用时，portalFlag=true
				boolean portalFlag = true;
				TestRptJson tSelf=testReportService.getReportJson(pro.getId(), "企业自检", 1, null, false, null,portalFlag);
				pro.setSelfRpt(tSelf);
				
				TestRptJson tCensor=testReportService.getReportJson(pro.getId(), "企业送检", 1, null, false, null,portalFlag);
				pro.setCensorRpt(tCensor);
				
				TestRptJson tSample = testReportService.getReportJson(pro.getId(), "政府抽检", 1, null, false, null,portalFlag);
				pro.setSampleRpt(tSample);
				/*处理产品图片*/
		        List<Resource> imgList = resourceService.getProductImgListByproId(pro.getId());//查找产品图片集合
				//为了兼容老数据如果资源表里没有产品图片则从产品表imgUrl字段中取值
				if(imgList==null||imgList.size()==0){
					if(pro.getImgUrl() != null){
						String[] imgUrlListArray = pro.getImgUrl().split("\\|");
						for(String url:imgUrlListArray){
							Resource re=new Resource();
							re.setUrl(url);
							imgList.add(re);
						}
					}
				}
				pro.setImgList(imgList);
				/* 处理生产企业 */
				//List<BussinessUnitVOToPortal> bus=businessUnitService.getBuVOToPortalByProId(pro.getId());
				List<BussinessUnitVOToPortal> bus=businessUnitService.getBuVOToPortalByBarcode(barcode);
				pro.setBusinessUnit(bus);
				/* 处理执行标准 */
				List<ProductCategoryInfo> res=productCategoryInfoService.getRegularityByProId(pro.getId());
				if(res!=null&&res.size()>0){
					pro.setRegularity(res.get(0).getName());
				}
			}
			return pro;
		} catch (DaoException e) {
			throw new ServiceException("ProductServiceImpl.getProductInfoByBarcode() "+e.getMessage(),e.getException());
		}
	}
	
	
	/**
	 * 功能描述：根据二维码id获取产品条形码
	 * @throws DaoException
	 * @author liuyuanjing 2015/12/28
	 */
	@Override
	public String findProductBarcodeByQRcode(String QRcode)  throws ServiceException{
		try{
			return getDAO().findProductBarcodeByQRcode(QRcode);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
	/**
	 * 功能描述：根据二维码id获取产品条形码
	 * @throws DaoException
	 * @author liuyuanjing 2015/12/28
	 */
	@Override
	public String findProductIdByQRcode(String QRcode)  throws ServiceException{
		try{
			return getDAO().findProductIdByQRcode(QRcode);
		}catch(DaoException daoe){
			throw new ServiceException(daoe.getMessage(), daoe);
		}
	}
	
}
