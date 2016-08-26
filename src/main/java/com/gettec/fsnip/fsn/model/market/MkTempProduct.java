package com.gettec.fsnip.fsn.model.market;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.vo.product.ProductOfMarketVO;

@Entity(name="T_TEST_TEMP_PRODUCT")
public class MkTempProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228456151572682151L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="BAR_CODE")
	private String barCode;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="SPECIFICATION")
	private String specification;
	
	@Column(name="MODEL_NO")
	private String modelNo;
	
	@Column(name="BRAND")
	private String brand;
	
	@Column(name="PRO_STATUS")
	private String productStatus;
	
	@Column(name="EXPIRATION")
	private String expiration;
	
	@Column(name="EXPIR_DAY")
	private String expirDay;
	
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name="MIN_UNIT")
	private String minUnit;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="PRO_DATE")
	private String proDate;
	
	@Column(name="BATCH_NO")
	private String batchNo;
	
	@Column(name="SAM_ADDRESS")
	private String sampleAddress;
	
	@Column(name="CREAT_USER_REALNAME")
	private String createUserRealName;
	
	@Column(name="LAST_MODIFY_TIME")
	private Date lastModifyTime;
	
	@Column(name="ORGANIZATION")
	private Long organization;
	
	@Column(name="CATEGORYPARENTID")
	private Long categoryparentid;
	
	@Column(name="CATEGORYPARENTCODE")
	private String categoryparentcode;
	
	public Long getCategoryparentid() {
		return categoryparentid;
	}

	public void setCategoryparentid(Long categoryparentid) {
		this.categoryparentid = categoryparentid;
	}

	public String getCategoryparentcode() {
		return categoryparentcode;
	}

	public void setCategoryparentcode(String categoryparentcode) {
		this.categoryparentcode = categoryparentcode;
	}

	@Transient
	private String categoryName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getExpirDay() {
		return expirDay;
	}

	public void setExpirDay(String expirDay) {
		this.expirDay = expirDay;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getProDate() {
		return proDate;
	}

	public void setProDate(String proDate) {
		this.proDate = proDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSampleAddress() {
		return sampleAddress;
	}

	public void setSampleAddress(String sampleAddress) {
		this.sampleAddress = sampleAddress;
	}

	public String getCreateUserRealName() {
		return createUserRealName;
	}

	public void setCreateUserRealName(String createUserRealName) {
		this.createUserRealName = createUserRealName;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public MkTempProduct() {
		super();
	}

	public MkTempProduct(Product product){
		this.barCode = product.getBarcode();  // 条形码
		this.name = product.getName();  // 产品名称
		this.modelNo = product.getFormat(); // 规格
		this.productStatus = product.getStatus();  // 商品状态
		this.expirDay = product.getExpirationDate();  // 保质天数
		this.standard = (product.getRegularityStr()==""?null:product.getRegularityStr()); // 执行标准
        this.expiration = product.getExpiration();//保质期
		
		ProductCategoryInfo thirdCategroy = product.getCategory();
        this.category = thirdCategroy==null?null:thirdCategroy.getName(); // 三级名称
        this.categoryparentid=(thirdCategroy==null?null:thirdCategroy.getCategory().getId());//二级id
        this.categoryparentcode=(thirdCategroy==null?null:thirdCategroy.getCategoryOneCode());//一级code
        
        if(product.getBusinessBrand()!=null){
        	this.brand = product.getBusinessBrand().getName();//商标
        }
        
        if(product.getUnit() != null){
        	this.minUnit = product.getUnit().getName(); //计量单位
        }
	}
	
	public MkTempProduct(ProductOfMarketVO product_vo){
		this.proDate = product_vo.getProductionDateStr();
		this.batchNo = product_vo.getBatchSerialNo();
		
		this.barCode = product_vo.getBarcode();  // 条形码
		this.name = product_vo.getName();  // 产品名称
		this.modelNo = product_vo.getFormat(); // 规格
		this.productStatus = product_vo.getStatus();  // 商品状态
		this.expirDay = product_vo.getExpirationDate();  // 保质天数
		
		ProductCategoryInfo thirdCategroy = product_vo.getCategory();
        this.category = thirdCategroy==null?null:thirdCategroy.getName(); // 三级名称
        this.categoryparentid=(thirdCategroy==null?null:thirdCategroy.getCategory().getId());  //二级id
        this.categoryparentcode=(thirdCategroy==null?null:thirdCategroy.getCategoryOneCode()); //一级code
        
        this.standard = (product_vo.getRegularityStr()==""?null:product_vo.getRegularityStr()); // 执行标准
        this.brand = product_vo.getBrand_name();//商标
        this.expiration = product_vo.getExpiration();//保质期
        this.minUnit = product_vo.getUnit();//计量单位
	}
}
