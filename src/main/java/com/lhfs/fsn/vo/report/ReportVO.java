package com.lhfs.fsn.vo.report;

import java.util.Date;
import java.util.List;

import com.gettec.fsnip.fsn.model.test.TestProperty;
import com.gettec.fsnip.fsn.model.test.TestResult;

/**
 * 大众门户报告VO
 * @author Administrator
 *
 */
public class ReportVO {
	Long id;
	String serviceOrder;  // 报告编号
	String sampleNO;  // 报告编号 + ‘-1’
	String testee;  //被检测人
	String businessName; //生产企业名称
	String businessAddress; //生产企业地址
	Date productionDate;  //生产日期
	String batchSerialNo;  //批次
	String sampleQuantity;// 抽样量
	String samplingLocation; // 抽样地点
	String testPlace; // 检验地点
	Date testDate;  // 检验日期
	Date expireDate;  // 过期时间
	String testType; // 检验类别
	String standard;  // 执行标准
	String testResultDescription;   //检测结果描述
	String testResult;  //检测结果
	String comment;  // 备注
	List<TestProperty> testProperties;  // 检测项目
	String pdfUrl;  //pdf路径
	
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServiceOrder() {
		return serviceOrder;
	}
	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}
	public String getSampleNO() {
		return sampleNO;
	}
	public void setSampleNO(String sampleNO) {
		this.sampleNO = sampleNO;
	}
	public String getTestee() {
		return testee;
	}
	public void setTestee(String testee) {
		this.testee = testee;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getBusinessAddress() {
		return businessAddress;
	}
	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}
	public Date getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	public String getBatchSerialNo() {
		return batchSerialNo;
	}
	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}
	public String getSampleQuantity() {
		return sampleQuantity;
	}
	public void setSampleQuantity(String sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}
	public String getSamplingLocation() {
		return samplingLocation;
	}
	public void setSamplingLocation(String samplingLocation) {
		this.samplingLocation = samplingLocation;
	}
	public String getTestPlace() {
		return testPlace;
	}
	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}
	public Date getTestDate() {
		return testDate;
	}
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getTestResultDescription() {
		return testResultDescription;
	}
	public void setTestResultDescription(String testResultDescription) {
		this.testResultDescription = testResultDescription;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<TestProperty> getTestProperties() {
		return testProperties;
	}
	public void setTestProperties(List<TestProperty> testProperties) {
		this.testProperties = testProperties;
	}
	public String getPdfUrl() {
		return pdfUrl;
	}
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}
	
	public ReportVO(){
		super();
	}
	
	public ReportVO(TestResult report){
		super();
		if(report==null){return;}
		this.id=report.getId();
		this.serviceOrder=report.getServiceOrder();
		this.testee=report.getTestee()!=null?report.getTestee().getName():"";
		this.businessName=report.getSample().getProducer().getName();
		this.businessAddress=report.getSample().getProducer().getAddress();
		this.productionDate=report.getSample().getProductionDate();
		this.batchSerialNo=report.getSample().getBatchSerialNo();
		this.sampleQuantity=report.getSampleQuantity();
		this.samplingLocation=report.getSamplingLocation();
		this.testPlace=report.getTestPlace();
		this.testDate=report.getTestDate();
		this.testType=report.getTestType();
		this.standard=report.getStandard();
		this.testResultDescription=report.getResult();
		this.testResult=report.getPass()?"合格":"不合格";
		this.comment=report.getComment();
		this.testProperties=report.getTestProperties();
		if(report.getRepAttachments().size()>0){
			this.pdfUrl=report.getRepAttachments().iterator().next().getUrl();
		}
	}
}
