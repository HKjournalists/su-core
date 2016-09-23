package com.lhfs.fsn.vo;

public class ProductJGVO {

	private long id ; //产品ID
	private String proName; //产品名称
	private String format;  //规格型号
	private String status;  //状态
	private String categoryName; //产品分类
	private String productType;//产品类型(2代码表进出口)
	private String qsNo;  //生产许可证号
	private String regularity; //执行标准
	private String certificateDate; //发展日期
	private String certificateUnit; //发展单位
	private String certificateEndDate; //发展单位
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getQsNo() {
		return qsNo;
	}
	public void setQsNo(String qsNo) {
		this.qsNo = qsNo;
	}
	public String getRegularity() {
		return regularity;
	}
	public void setRegularity(String regularity) {
		this.regularity = regularity;
	}
	public String getCertificateDate() {
		return certificateDate;
	}
	public void setCertificateDate(String certificateDate) {
		this.certificateDate = certificateDate;
	}
	public String getCertificateUnit() {
		return certificateUnit;
	}
	public void setCertificateUnit(String certificateUnit) {
		this.certificateUnit = certificateUnit;
	}
	public String getCertificateEndDate() {
		return certificateEndDate;
	}
	public void setCertificateEndDate(String certificateEndDate) {
		this.certificateEndDate = certificateEndDate;
	}
	
	
}
