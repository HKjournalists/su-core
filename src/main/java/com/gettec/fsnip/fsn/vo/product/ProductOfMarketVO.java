package com.gettec.fsnip.fsn.vo.product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.gettec.fsnip.fsn.model.market.MkTempProduct;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.ImportedProduct;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.vo.business.QsNoAndFormatVo;


/**
 * 用于封装报告录入页面的产品信息
 * @author ZhangHui 2015/6/4
 */
public class ProductOfMarketVO {
	
	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 产品名称
	 */
	private String name;
	
	/**
	 * 条形码
	 */
	private String barcode;
	
	/**
	 * 商标
	 */
	private String brand_name;

	/**
	 * 批次
	 */
	private String batchSerialNo;
	
	/**
	 * 生产日期字符串
	 */
	private String productionDateStr;
	
	/**
	 * 生产日期
	 */
	private Date productionDate;
	/**
	 * 过期日期
	 */
	private Date expireDate;
	
	/**
	 * 保质期 字符串
	 */
	private String expire_Date;
	
	/**
	 * 商品状态
	 */
	private String status;
	
	/**
	 * 规格
	 */
	private String format;
	
	/**
	 * 单位
	 */
	private String unit;
	
	/**
	 * 第三级category的id
	 */
	private ProductCategoryInfo category;
	
	/**
	 * 保质天数
	 */
	private String expirationDate;
	
	/** 
	 * 质保期
	 */
	private String expiration;
	
	/**
	 * 产品类型 
	 * 		1 代表 国产食品 
	 * 		2 代表 进口食品
	 * 		4 代表 国内分装食品（本身为进口食品，但经过了国内二次包装）
	 */
	private int productType;
	
	/**
	 * 进口产品
	 */
	private ImportedProduct importedProduct;
	
	/**
	 * 执行标准字符串
	 */
	private String regularityStr;
	
	/**
	 * 执行标准
	 */
	private Set<ProductCategoryInfo> regularity;
	
	/**
	 * 产品图片
	 */
	private Set<Resource> proAttachments = new HashSet<Resource>();
	
	/**
	 * 是否允许编辑产品信息
	 * 		true  代表 允许编辑
	 * 		false 代表 不允许编辑
	 */
	private boolean can_edit_pro = true;
	
	/**
	 * qs号信息
	 */
	private QsNoAndFormatVo qs_info;
	
	/**
	 * 生产企业id
	 */
	private Long producer_id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}
	
	public String getProductionDateStr() {
		return productionDateStr;
	}

	public void setProductionDateStr(String productionDateStr) {
		this.productionDateStr = productionDateStr;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public ProductCategoryInfo getCategory() {
		return category;
	}

	public void setCategory(ProductCategoryInfo category) {
		this.category = category;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public ImportedProduct getImportedProduct() {
		return importedProduct;
	}

	public void setImportedProduct(ImportedProduct importedProduct) {
		this.importedProduct = importedProduct;
	}

	public String getRegularityStr() {
		return regularityStr;
	}

	public void setRegularityStr(String regularityStr) {
		this.regularityStr = regularityStr;
	}

	public Set<ProductCategoryInfo> getRegularity() {
		return regularity;
	}

	public void setRegularity(Set<ProductCategoryInfo> regularity) {
		this.regularity = regularity;
	}

	public Set<Resource> getProAttachments() {
		return proAttachments;
	}

	public void setProAttachments(Set<Resource> proAttachments) {
		this.proAttachments = proAttachments;
	}

	public boolean isCan_edit_pro() {
		return can_edit_pro;
	}

	public void setCan_edit_pro(boolean can_edit_pro) {
		this.can_edit_pro = can_edit_pro;
	}

	public QsNoAndFormatVo getQs_info() {
		return qs_info;
	}

	public void setQs_info(QsNoAndFormatVo qs_info) {
		this.qs_info = qs_info;
	}

	public Long getProducer_id() {
		return producer_id;
	}

	public void setProducer_id(Long producer_id) {
		this.producer_id = producer_id;
	}

	public ProductOfMarketVO(){
		super();
	}
	
	public String getExpire_Date() {
		return expire_Date;
	}

	public void setExpire_Date(String expire_Date) {
		this.expire_Date = expire_Date;
	}

	public ProductOfMarketVO(MkTempProduct tempProduct) throws ParseException {
		super();
		this.productionDateStr = tempProduct.getProDate();
		this.batchSerialNo = tempProduct.getBatchNo();
		this.brand_name = tempProduct.getBrand();
		this.barcode = tempProduct.getBarCode();
		this.name = tempProduct.getName();
		this.format = tempProduct.getModelNo();
		this.status = tempProduct.getProductStatus();
		this.expirationDate = tempProduct.getExpirDay();
		this.expiration = tempProduct.getExpiration();
		this.unit = tempProduct.getMinUnit();
		this.regularityStr = tempProduct.getStandard();
		
		// 产品分类
		this.category = new ProductCategoryInfo(tempProduct);
	}

	public ProductOfMarketVO(Product product) {
		super();
		
		if(product == null){
			return;
		}
		
		this.id = product.getId();
		this.name = product.getName();
		this.barcode = product.getBarcode();
		
		if(product.getBusinessBrand() != null){
			this.brand_name = product.getBusinessBrand().getName();
		}
		
		this.status = product.getStatus();
		this.format = product.getFormat();
		
		if(product.getUnit() != null){
			this.unit = product.getUnit().getName();
		}
		
		this.category = product.getCategory();
		this.expirationDate = product.getExpirationDate();
		this.expiration = product.getExpiration();
		this.productType = product.getProductType();
		this.proAttachments = product.getProAttachments();
		
		this.regularityStr = "";
		if(product.getRegularity() != null){
			for(ProductCategoryInfo reg : product.getRegularity()){
				this.regularityStr += reg.getName() + ";";
			}
		}
	}

	public ProductOfMarketVO(ProductInstance sample) {
		this(sample.getProduct());
		
		if(sample.getProductionDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.productionDateStr = sdf.format(sample.getProductionDate());
		}
		
		this.batchSerialNo = sample.getBatchSerialNo();
		
		this.qs_info = new QsNoAndFormatVo(sample.getQs_no());
	}
}
