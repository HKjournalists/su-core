package com.gettec.fsnip.fsn.service.product.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.gettec.fsnip.fsn.dao.product.ProductInstanceDAO;
import com.gettec.fsnip.fsn.exception.DaoException;
import com.gettec.fsnip.fsn.exception.JPAException;
import com.gettec.fsnip.fsn.exception.ServiceException;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.service.business.BusinessBrandService;
import com.gettec.fsnip.fsn.service.business.BusinessUnitService;
import com.gettec.fsnip.fsn.service.business.ProductionLicenseService;
import com.gettec.fsnip.fsn.service.common.impl.BaseServiceImpl;
import com.gettec.fsnip.fsn.service.market.FtpService;
import com.gettec.fsnip.fsn.service.market.MkTestTemplateService;
import com.gettec.fsnip.fsn.service.market.ResourceService;
import com.gettec.fsnip.fsn.service.product.ProductInstanceService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.util.DateUtil;
import com.lhfs.fsn.service.product.impl.ProductServiceImpl;
import com.lhfs.fsn.vo.SampleVO;

/**
 * ProductInstance service implementation
 * 
 * @author Ryan Wang
 */
@Service(value="productInstanceService")
public class ProductInstanceServiceImpl extends BaseServiceImpl<ProductInstance, ProductInstanceDAO> 
		implements ProductInstanceService{
	
	@Autowired protected BusinessUnitService businessUnitService;
	@Autowired protected ProductInstanceDAO productInstanceDAO;
	@Autowired protected ProductService productService;
	@Autowired protected FtpService ftpService;
	@Autowired protected ResourceService testResourceService;
	@Autowired protected MkTestTemplateService templateService;
	@Autowired protected BusinessBrandService businessBrandService;
	@Autowired protected ProductionLicenseService productionLicenseService;
	@Autowired private ProductServiceImpl productLFService;
	@Autowired private BusinessUnitService businessUnitServicee;
	
	@Override
	public ProductInstanceDAO getDAO() {
		return productInstanceDAO;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductInstance findLastByPID(Long productId) {
		return getDAO().findLastBySP(null, productId);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductInstance findByBSP(String batchSerialNo, String serial,
			Long productId) {
		return getDAO().findByBSP(batchSerialNo, serial, productId);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductInstance findByBSB(String batchSerialNo, String serial,
			String barcode) {
		return getDAO().findByBSB(batchSerialNo, serial, barcode);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductInstance findLastBySP(String serial, Long productId) {
		return getDAO().findLastBySP(serial, productId);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public ProductInstance findByBatchAndProductId(String batchSerialNo, Long productId) {
		return getDAO().findByBatchAndProductId(batchSerialNo, productId);
	}
	
	/**
	 * 根据产品ID和仓库ID获取商品库存中的商品实例，主要是获取批次
	 * @param productId
	 * @param storageId
	 * @param organization
	 * @return 产品实例集合
	 * Author 郝圆彬
	 * 2014-10-27
	 * 新增
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
	public List<ProductInstance> getProductInstancesByStorageInfoAndStorage(Long productId,String storageId,Long organization) {
		return getDAO().getProductInstancesByStorageInfoAndStorage(productId,storageId,organization);		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<ProductInstance> findProductInstancesByPID(Long productId) {
		return getDAO().findProductInstancesByPID(productId);
	}

	@Override
	public List<ProductInstance> getProductInstancesByBatchAndProductId(
			String batch, Long productId) throws ServiceException {
		return getDAO().findProductInstances(batch, null, productId);
	}
	/**
	 * 根据产品barcode和当前登录企业ID查找是否有质检报告
	 * @param barcode 条形码
	 * @param producerId 企业ID
	 * @return 产品实例
	 */
	@Override
	public ProductInstance findByBarcodeAndProducerId(String barcode,Long producerId)throws ServiceException{
		try {
			return getDAO().findByBarcodeAndProducerId(barcode,producerId);
		} catch (DaoException daoe) {
			throw new ServiceException("(ProductInstanceServiceImpl--findByBarcodeAndProducerId)-->"+daoe.getMessage(),daoe);
		}
	}

	/**
	 * 根据产品id 和批次 在实例中查询相关的实例id
	 */
	@Override
	public List<Long> getInstanceIdForProductIdAndbatch(String batch, Long id) throws ServiceException {
		try {
			String sql = "SELECT e.id FROM product_instance e WHERE e.product_id = ?1 AND e.batch_serial_no = ?2 ";
			return getDAO().getListBySQLWithoutType(Long.class, sql, new Object[]{id,batch});
		} catch (JPAException daoe) {
			throw new ServiceException("(ProductInstanceServiceImpl--getInstanceIdForProductIdAndbatch()-->"+daoe.getMessage(),daoe);
		}
	}
	/**
	 * 根据sample，producer的json对象和isBatch获取ProductInstance
	 * @param sample
	 * @param producerVO
	 * @param isBatch
	 * @return ProductInstance
	 * @throws JPAException
	 * @throws ServiceException
	 * @author 未知<br>
	 * 最后更新者：査万能
	 * 最后更新时间：2015/3/16
	 */
    @SuppressWarnings("deprecation")
	@Override
    
	public ProductInstance addSampleProduct(JSONObject sample,JSONObject producerJSON,Boolean isBatch) throws ServiceException {
    	try {
    		String barcode = sample.getString("barCode");
    		String seria = sample.getString("serial");
    		String sampleName = sample.getString("name");
    		String productionDate = sample.getString("proDate");
    		/* 处理批次 */
    		String batchSeriaNo = sample.getString("batch_serial_no");
    		if(batchSeriaNo.equals("")){
    			batchSeriaNo = productionDate; //暂时使用生产日期作为批次号
    		}
    		/* 处理生产日期 */
    		productionDate = productionDate.concat(" 00:00:00");
    		Date proDatetime = new Date(productionDate.replace("-", "/"));
    		Product product = getProduct(barcode, sampleName);
    		BusinessUnit producer=null;
    		if(!JSONObject.fromObject(producerJSON).isNullObject()){
    			producer = getProducer(isBatch, producerJSON.getString("name"), product);
    		}else {
    			producer= businessUnitService.findByName("--");
			}
    		ProductInstance proIns = new ProductInstance();
    		proIns.setBatchSerialNo(batchSeriaNo);
    		proIns.setSerial(seria);
    		proIns.setProduct(product);
    		proIns.setProducer(producer);
    		proIns.setProductionDate(proDatetime);
    		create(proIns);
    		return proIns;
		} catch (ServiceException sex) {
			throw new ServiceException("ProductInstanceServiceImpl.addSampleProduct()-->" + sex.getMessage(), sex.getException());
		}
	}

	/**
	 * 
	 * @param isBatch 是否为批次
	 * @param producerName 生产企业名称
	 * @param product 产品对象
	 * @return BusinessUnit
	 * @throws ServiceException
	 * @author ZhangHui<br>
	 * 最后更新时间：2015/3/12
	 */
	private BusinessUnit getProducer(Boolean isBatch, String producerName, Product product) throws ServiceException {
		BusinessUnit producer = null;
		if(isBatch){
			if(!producerName.trim().equals("")){
				BusinessUnit orig_producer = businessUnitService.findByName(producerName);
				producer = orig_producer;
				if(orig_producer == null){
					/* 新增生产企业 */
					producer = new BusinessUnit(producerName);
					businessUnitService.create(producer);
				}
			}
		}else if(product.getBusinessBrand() != null){
			producer = product.getBusinessBrand().getBusinessUnit();
		}
		if(producer == null){
			producer = businessUnitService.findByName("--");
		}
		return producer;
	}

	/**
	 * 根据产品条形码或者产品名称获取产品
	 * @param barcode 产品条形码
	 * @param sampleName 产品名称
	 * @return Product
	 * @throws ServiceException
	 * @author ZhangHui<br>
	 * 最后更新时间：2015/3/12
	 */
	private Product getProduct(String barcode, String sampleName) throws ServiceException {
		Product product = null;
		if(StringUtils.isNotEmpty(barcode) || barcode!="null"){
		    product = productService.findByBarcode(barcode);
		}
		if(product == null){
			product = productService.findByName(sampleName);
		}
		if(product == null){
			product = productLFService.findById(new Long(1));
		}
		return product;
	}
	/**
	 * 根据产品条形码和批次号查找产品实例
	 * @param barcode 条形码
	 * @param batchSeriaNo 批次号
	 * @author ZhaWanNeng
	 * 更新时间2015/3/17
	 */
	@Override
	public ProductInstance findInstance(String barcode, String batchSeriaNo) throws ServiceException {
		try {
			return getDAO().findInstance(barcode, batchSeriaNo);
		} catch (DaoException dex) {
			throw new ServiceException("", dex.getException());
		}
	}
	/**
	 * 根据产品id获取样品id
	 * @param productId
	 * @author ZhaWanNeng
	 */
	@Override
	public List<Long> findInstancebyProductId(Long productId)
			throws ServiceException {
		try {
			return productInstanceDAO.findInstancebyProductId(productId);
		} catch (DaoException dex) {
			throw new ServiceException("findInstancebyProductId(Long productId)出现异常", dex.getException());
		}
	}
	
	/**
	 * 保存产品实例
	 * @param sample
	 * @param batch
	 * @param organizationID
	 * @return Map<String, Object>
	 * @author LongxXianZhen
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public Map<String, Object> saveProductInstance(SampleVO sample,
			boolean batch, Long organizationID) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			Product pro=null;
			/* 根据条形码查询产品 */
			try {
				 pro=productService.findByBarcode(sample.getBarCode());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			ProductInstance productInstance=new ProductInstance();
			if(pro!=null){
				if("".equals(pro.getImgUrl())||pro.getImgUrl()==null){ //产品不为空且产品没有图片则保存LIMS传过来的图片
					String productImg = sample.getProductImg();
					if(!productImg.equals("")||productImg != null){
						Product saveImgToProduct=null;
						try {
							saveImgToProduct = testResourceService.setImgToProduct(productImg,pro.getBarcode());
						} catch (ServiceException e) {
							e.printStackTrace();
						}
						pro.setProAttachments(saveImgToProduct.getProAttachments());
						pro.setImgUrl(saveImgToProduct.getImgUrl());
						try {
							productService.update(pro);
						} catch (ServiceException e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				if(batch){ // 查询出的产品为空 且是批次业务则根据产品信息新增产品 
					pro=productService.saveProduct(sample,organizationID);
					if(pro==null){
						map.put("msg", "产品新增失败");
						map.put("status", "false");
						return map;
					}
				}else{
					map.put("msg", "匹配不上产品且为一般业务不能新增");
					map.put("status", "false");
					return map;
				}
			}
			productInstance.setBatchSerialNo(sample.getBatch_serial_no());
			productInstance.setProduct(pro);
			try {
				productInstance.setProductionDate(DateUtil.StringToDate(sample.getProDate(), "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
				map.put("msg", "生产企业日期格式不正确-->"+e.getMessage());
				map.put("status", "false");
				return map;
			}
			if(batch){
				/* 企业 */
				BusinessUnit bu=null;
				String businessName=sample.getProducer().getName();
				if(businessName==null||businessName.equals("")||businessName.equals("null")){
					bu=businessUnitServicee.findByName("--");
				}else{
					bu=businessUnitServicee.findByName(businessName);
				}
				if(bu==null){
					Map<String,Object> testeeMap=businessUnitServicee.saveBusinessUnit(sample.getProducer());
					bu=(BusinessUnit)testeeMap.get("business");
				}
				productInstance.setProducer(bu);
			}else{
				productInstance.setProducer(pro.getBusinessBrand().getBusinessUnit());
			}
			
			try {
				productInstanceDAO.persistent(productInstance);
			} catch (JPAException e) {
				((Throwable) e.getException()).printStackTrace();
				map.put("msg", "产品实例新增失败-->"+((Throwable) e.getException()).getMessage());
				map.put("status", "false");
				return map;
			}
			map.put("status", "true");
			map.put("productInstance", productInstance);
			return map;
		} catch (ServiceException e) {
			((Throwable) e.getException()).printStackTrace();
			map.put("msg", "产品实例新增失败-->"+((Throwable) e.getException()).getMessage());
			map.put("status", "false");
			return map;
		}catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "产品实例新增失败-->"+e.getLocalizedMessage());
			map.put("status", "false");
			return map;
		}
	}
	
	/**
	 * 功能描述：保存产品信息<br>
	 * 作用于保存泊银等其他外部系统的样品数据。
	 * @author ZhangHui 2015/4/24
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveBYProductIns(TestResult test_result, Long organization) {
		try {
			if(test_result == null){ return false;}
			ProductInstance sample = test_result.getSample();
			if(sample == null){ return false; }
			Product product = sample.getProduct();
			if(product == null){ return false; }
			String barcode = product.getBarcode();
			String name = product.getName();
			if(barcode==null || "".equals(barcode) || name==null || "".equals(name)){return false;}
			
			/**
			 * 1. 保存品牌
			 */
			businessBrandService.saveBrand(sample, organization);
			
			/**
			 * 2. 产品及实例处理
			 */
			Product origProduct = productService.findByBarcode(product.getBarcode());
			if(origProduct == null){
				/* 新增产品、新增实例 */
				ProductInstance new_sample = new ProductInstance(sample);
				new_sample.setProducer(test_result.getSample().getProducer());
				Product new_product = new Product(sample.getProduct());
				new_product.setOrganization(organization);
				new_product.setProducer(test_result.getSample().getProducer());
				new_sample.setProduct(new_product);
				sample = save(new_sample, origProduct);
				test_result.setSample(new_sample);
			}else{
				/* 新增实例  */
				sample.setId(null);
				sample = save(sample, origProduct);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 功能描述：保存产品信息<br>
	 * 作用于保存泊银等其他外部系统的样品数据。
	 * @author ZhangHui 2015/4/24
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private ProductInstance save(ProductInstance sample, Product orig_product) throws ServiceException{
		try {
			Product product = sample.getProduct();
			if(orig_product == null){
				/* 1   新增产品  + 新增实例 */
				// 处理产品分类  ???????????????????????????????????
				productService.create(product);
				create(sample);
			}else{
				/* 2   新增产品实例 */
				ProductInstance new_sample = new ProductInstance(sample);
				new_sample.setBatchSerialNo(sample.getBatchSerialNo()); // 批次
				new_sample.setProduct(orig_product); // 产品
				new_sample.setProducer(sample.getProducer()); // 生产企业
				create(new_sample);
				
				sample.setId(new_sample.getId());
				product.setId(orig_product.getId());
				// product.getCategory().setId(product.getCategory().getId()); 处理产品分类??????????????????
			}
			return sample;
		}catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException("ProductInstanceServiceImpl.save()-->" + e.getMessage(), e.getException());
		}
	}
}
