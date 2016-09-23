package com.gettec.fsnip.fsn.vo.product;

import java.util.Date;
import java.util.List;

import com.gettec.fsnip.fsn.model.test.TestProperty;


public class ProductVO {
	private String orderNumber; //序号
	private String companyName; //企业名称
	private String companyAddress;//企业地址
	private String sampleCompanyName;//抽样单位名称
	private String sampleCompanyAddress;//抽样单位地址
	private String productName;//产品名称
	private String format; //规格
	private String businessName; //商标名称
	private String productionDate; //生产日期字符串
	private String batch_serial_no;//批号
//	private String testName;//检测项目名称
//	private String result;//检测结果
//	private String techIndicator;//标准值
	
	private List<TestProperty> testProperty; //检测项目
	
	private String validMessage; //验证信息
	
	private Date production_Date; //生产日期
	
	
	
	
	
	
	public Date getProduction_Date() {
		return production_Date;
	}
	public void setProduction_Date(Date production_Date) {
		this.production_Date = production_Date;
	}
	public String getValidMessage() {
		return validMessage;
	}
	public void setValidMessage(String validMessage) {
		this.validMessage = validMessage;
	}
	public List<TestProperty> getTestProperty() {
		return testProperty;
	}
	public void setTestProperty(List<TestProperty> testProperty) {
		this.testProperty = testProperty;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getSampleCompanyName() {
		return sampleCompanyName;
	}
	public void setSampleCompanyName(String sampleCompanyName) {
		this.sampleCompanyName = sampleCompanyName;
	}
	public String getSampleCompanyAddress() {
		return sampleCompanyAddress;
	}
	public void setSampleCompanyAddress(String sampleCompanyAddress) {
		this.sampleCompanyAddress = sampleCompanyAddress;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getBatch_serial_no() {
		return batch_serial_no;
	}
	public void setBatch_serial_no(String batch_serial_no) {
		this.batch_serial_no = batch_serial_no;
	}
}
