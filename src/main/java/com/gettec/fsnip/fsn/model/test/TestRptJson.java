package com.gettec.fsnip.fsn.model.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

public class TestRptJson extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9180103532211634209L;
	private long id;//主键
	private String result; //检测结果
	/**
	 * 修改：将原来的Date类型换成String类型
	 */
	private String testDate; //检测日期
	private String sampleName; //样品名称
	private String serial; //样品编号
	private String tradeMark; //商标
	private String enterprise; //生产企业
	private String samplingUnit; //抽样单位
	private String format; //规格型号
	private String sampleQuantity; //抽样量
	private String testee; //受检单位
	private String samplingLocation; //抽样地点
	private String standard; //判定依据
	private String testType; //抽验类型
	private String testOrgnization;//检验机构
	/**
	 * 修改：将原来的Date类型换成String类型
	 */
	private String samplingDate; //抽样日期
	private String batchSN; //批号/日期
	private String status; //样品状态
	private String instrument = ""; //仪器
	private String remark = ""; //备注
	private List<TestProperty> testPropertyList; //chart属性表
	private int total; //总数
	private int current; //当前
	private String pdfUrl;
	private String productionDate;
	private String expirationDate;//过期日期
	
	private String interceptionPdf;//一般用户看到的报告
	private String organizationName;//所属企业名称
	
	private Set<Resource> reportImgList = new HashSet<Resource>();//报告原件
	private Set<Resource> checkImgList = new HashSet<Resource>();//检测报告
	private Set<Resource> buyImgList = new HashSet<Resource>();//购买凭证
	
	
	/**
	 * 标志报告来自那个字段，便于portal上做特殊处理  
	 * @author lyj 2015-11-20
	 */
	private String  edition;           //报告来至lims的哪个版本
	
	/**
	 * 进口食品报告信息
	 * @author longxianzhen 20150701
	 */
	private ImportedProductTestResult impProTestResult;
	
	
	public ImportedProductTestResult getImpProTestResult() {
		return impProTestResult;
	}
	public void setImpProTestResult(ImportedProductTestResult impProTestResult) {
		this.impProTestResult = impProTestResult;
	}
	public String getInterceptionPdf() {
		return interceptionPdf;
	}
	public void setInterceptionPdf(String interceptionPdf) {
		this.interceptionPdf = interceptionPdf;
	}
	
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getPdfUrl() {
		return pdfUrl;
	}
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getTradeMark() {
		return tradeMark;
	}
	public void setTradeMark(String tradeMark) {
		this.tradeMark = tradeMark;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	public String getSamplingUnit() {
		return samplingUnit;
	}
	public void setSamplingUnit(String samplingUnit) {
		this.samplingUnit = samplingUnit;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getSampleQuantity() {
		return sampleQuantity;
	}
	public void setSampleQuantity(String sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}
	public String getTestee() {
		return testee;
	}
	public void setTestee(String testee) {
		this.testee = testee;
	}
	public String getSamplingLocation() {
		return samplingLocation;
	}
	public void setSamplingLocation(String samplingLocation) {
		this.samplingLocation = samplingLocation;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getSamplingDate() {
		return samplingDate;
	}
	public void setSamplingDate(String samplingDate) {
		this.samplingDate = samplingDate;
	}
	public String getBatchSN() {
		return batchSN;
	}
	public void setBatchSN(String batchSN) {
		this.batchSN = batchSN;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<TestProperty> getTestPropertyList() {
		return testPropertyList;
	}
	public void setTestPropertyList(List<TestProperty> testPropertyList) {
		this.testPropertyList = testPropertyList;
	}
	
	
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public TestRptJson() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public TestRptJson(String result, String testDate, String sampleName,
			String serial, String tradeMark, String enterprise,
			String samplingUnit, String format, String sampleQuantity,
			String testee, String samplingLocation, String standard,
			String testType, String samplingDate, String batchSN, String status,
			String instrument, String remark,
			List<TestProperty> testPropertyList, String pdfUrl, int total, int current,String interceptionPdf,String organizationName) {
		super();
		this.organizationName=organizationName;
		this.result = result;
		this.testDate = testDate;
		this.sampleName = sampleName;
		this.serial = serial;
		this.tradeMark = tradeMark;
		this.enterprise = enterprise;
		this.samplingUnit = samplingUnit;
		this.format = format;
		this.sampleQuantity = sampleQuantity;
		this.testee = testee;
		this.samplingLocation = samplingLocation;
		this.standard = standard;
		this.testType = testType;
		this.samplingDate = samplingDate;
		this.batchSN = batchSN;
		this.status = status;
		this.instrument = instrument;
		this.remark = remark;
		this.testPropertyList = testPropertyList;
		this.pdfUrl = pdfUrl;
		this.total = total;
		this.current = current;
		this.interceptionPdf=interceptionPdf;
	}
	public TestRptJson(String result, String testDate, String sampleName,
			String serial, String tradeMark, String enterprise,
			String samplingUnit, String format, String sampleQuantity,
			String testee, String samplingLocation, String standard,
			String testType, String samplingDate, String batchSN,
			String status, String instrument, String remark,
			List<TestProperty> testPropertyList, int total, int current,
			String pdfUrl, String productionDate, String interceptionPdf,
			String organizationName, ImportedProductTestResult impProTestResult) {
		super();
		this.organizationName=organizationName;
		this.result = result;
		this.testDate = testDate;
		this.sampleName = sampleName;
		this.serial = serial;
		this.tradeMark = tradeMark;
		this.enterprise = enterprise;
		this.samplingUnit = samplingUnit;
		this.format = format;
		this.sampleQuantity = sampleQuantity;
		this.testee = testee;
		this.samplingLocation = samplingLocation;
		this.standard = standard;
		this.testType = testType;
		this.samplingDate = samplingDate;
		this.batchSN = batchSN;
		this.status = status;
		this.instrument = instrument;
		this.remark = remark;
		this.testPropertyList = testPropertyList;
		this.pdfUrl = pdfUrl;
		this.total = total;
		this.current = current;
		this.interceptionPdf=interceptionPdf;
		this.impProTestResult = impProTestResult;
	}
	
	
	
	public TestRptJson(String result, String testDate, String sampleName,
			String serial, String tradeMark, String enterprise,
			String samplingUnit, String format, String sampleQuantity,
			String testee, String samplingLocation, String standard,
			String testType, String samplingDate, String batchSN, String status,
			String instrument, String remark,
			List<TestProperty> testPropertyList, String pdfUrl, int total, int current,String interceptionPdf,String organizationName,String edition) {
		super();
		this.organizationName=organizationName;
		this.result = result;
		this.testDate = testDate;
		this.sampleName = sampleName;
		this.serial = serial;
		this.tradeMark = tradeMark;
		this.enterprise = enterprise;
		this.samplingUnit = samplingUnit;
		this.format = format;
		this.sampleQuantity = sampleQuantity;
		this.testee = testee;
		this.samplingLocation = samplingLocation;
		this.standard = standard;
		this.testType = testType;
		this.samplingDate = samplingDate;
		this.batchSN = batchSN;
		this.status = status;
		this.instrument = instrument;
		this.remark = remark;
		this.testPropertyList = testPropertyList;
		this.pdfUrl = pdfUrl;
		this.total = total;
		this.current = current;
		this.interceptionPdf=interceptionPdf;
		this.edition=edition;
	}
	public Set<Resource> getReportImgList() {
		return reportImgList;
	}
	public void setReportImgList(Set<Resource> reportImgList) {
		this.reportImgList = reportImgList;
	}
	public Set<Resource> getCheckImgList() {
		return checkImgList;
	}
	public void setCheckImgList(Set<Resource> checkImgList) {
		this.checkImgList = checkImgList;
	}
	public Set<Resource> getBuyImgList() {
		return buyImgList;
	}
	public void setBuyImgList(Set<Resource> buyImgList) {
		this.buyImgList = buyImgList;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTestOrgnization() {
		return testOrgnization;
	}
	public void setTestOrgnization(String testOrgnization) {
		this.testOrgnization = testOrgnization;
	}
}
