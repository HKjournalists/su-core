package com.gettec.fsnip.fsn.vo.product;

import java.util.List;

import com.gettec.fsnip.fsn.model.market.Resource;


/**
 * 用于封装产品管理界面展示的产品字段（轻量级封装）
 * @author ZhangHui 2015/4/11
 */
public class ProductManageViewVO {
	/**
	 * 产品id
	 */
	private long id;
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 商标名称
	 */
	private String brandName;
	/**
	 * 规格
	 */
	private String format;
	/**
	 * 条形码
	 */
	private String barcode;
	/**
	 * 适宜人群
	 */
	private String cstm;
	/**
	 * 配料
	 */
	private String ingredient;
	/**
	 * 列举所有的销往客户名称
	 */
	private String selectedCustomerNames;
	/**
	 * 产品的包装标识：
	 * 0：预包装
	 * 1：散装
	 * 2：无条码
	 */
	private String packageFlag;
	/**
	 * 产品本地标识：
	 * 0：自营产品
	 * 1：引进产品
	 */
	private String local;
	
	/* 生产企业名称  */
	private String enterpriseName;
	
	private String status;
	
	/* 产品图片url(多张时只取第一张) */
	private String imgUrl;
	
	/* 产品营养指数计算状态 0：未计算, 1：成功  2:计算失败 */
	char nutriStatus;
	
	private String licImg;
	
	private String orgImg;
	
	private String disImg;
	
	private String expiration;  //产品保质期
	
	private List<Resource> imgList;
	
	private boolean isSpecialProduct;
	
	public boolean isSpecialProduct() {
		return isSpecialProduct;
	}
	public void setSpecialProduct(boolean isSpecialProduct) {
		this.isSpecialProduct = isSpecialProduct;
	}
	public List<Resource> getImgList() {
		return imgList;
	}
	public void setImgList(List<Resource> imgList) {
		this.imgList = imgList;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCstm() {
		return cstm;
	}
	public void setCstm(String cstm) {
		this.cstm = cstm;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getSelectedCustomerNames() {
		return selectedCustomerNames;
	}
	public void setSelectedCustomerNames(String selectedCustomerNames) {
		this.selectedCustomerNames = selectedCustomerNames;
	}
	public String getPackageFlag() {
		return packageFlag;
	}
	public void setPackageFlag(String packageFlag) {
		this.packageFlag = packageFlag;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public char getNutriStatus() {
		return nutriStatus;
	}
	public void setNutriStatus(char nutriStatus) {
		this.nutriStatus = nutriStatus;
	}
	
	
	public String getLicImg() {
		return licImg;
	}
	public void setLicImg(String licImg) {
		this.licImg = licImg;
	}
	public String getOrgImg() {
		return orgImg;
	}
	public void setOrgImg(String orgImg) {
		this.orgImg = orgImg;
	}
	public String getDisImg() {
		return disImg;
	}
	public void setDisImg(String disImg) {
		this.disImg = disImg;
	}
	public ProductManageViewVO() {
		super();
	}
	
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public ProductManageViewVO(long id, String name, String brandName,
			String format, String barcode, String cstm, String ingredient, 
			String packageFlag, String selectedCustomerNames, char nutriStauts) {
		super();
		this.id = id;
		this.name = name;
		this.brandName = brandName;
		this.format = format;
		this.barcode = barcode;
		this.cstm = cstm;
		this.ingredient = ingredient;
		this.packageFlag = packageFlag;
		this.nutriStatus = nutriStauts;
		this.selectedCustomerNames = selectedCustomerNames;
	}
	
	public ProductManageViewVO(long id, String name, String brandName,String format, String barcode, 
			String cstm,String ingredient, String packageFlag, String selectedCustomerNames, String local,boolean isSpecialProduct) {
		super();
		this.id = id;
		this.name = name;
		this.brandName = brandName;
		this.format = format;
		this.barcode = barcode;
		this.ingredient = ingredient;
		this.packageFlag = packageFlag;
		this.selectedCustomerNames = selectedCustomerNames;
		this.local = local;
		this.isSpecialProduct=isSpecialProduct;
	}
	public ProductManageViewVO(long id, String name, String format,
			String barcode, String enterpriseName, String status,
			String expiration,String imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.format = format;
		this.barcode = barcode;
		this.enterpriseName = enterpriseName;
		this.status = status;
		this.expiration = expiration;
		this.imgUrl = imgUrl;
	}
	
}
