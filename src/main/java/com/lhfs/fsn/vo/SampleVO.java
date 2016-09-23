package com.lhfs.fsn.vo;

/**
 * 提供给监管系统VO
 * @author XinTang
 */
public class SampleVO {
	private String name;
	private String barCode;
	private String batch_serial_no;
	private String businessBrand;
	private String format;
	private String proDate;
	private String regularity;
	private String status;
	private String jsonURL;
	private String pdfURL;
	private String sampleNO;
	private String reportNo;
	private Long sampleId;
	private int reportStatus;
	private String productImg;
	private BusinessUnitVO producer;
	private BusinessUnitVO testee;
	
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getBatch_serial_no() {
		return batch_serial_no;
	}
	public void setBatch_serial_no(String batch_serial_no) {
		this.batch_serial_no = batch_serial_no;
	}
	public String getBusinessBrand() {
		return businessBrand;
	}
	public void setBusinessBrand(String businessBrand) {
		this.businessBrand = businessBrand;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJsonURL() {
		return jsonURL;
	}
	public void setJsonURL(String jsonURL) {
		this.jsonURL = jsonURL;
	}
	public String getPdfURL() {
		return pdfURL;
	}
	public void setPdfURL(String pdfURL) {
		this.pdfURL = pdfURL;
	}
	public String getProDate() {
		return proDate;
	}
	public void setProDate(String proDate) {
		this.proDate = proDate;
	}
	public String getRegularity() {
		return regularity;
	}
	public void setRegularity(String regularity) {
		this.regularity = regularity;
	}
	public String getSampleNO() {
		return sampleNO;
	}
	public void setSampleNO(String sampleNO) {
		this.sampleNO = sampleNO;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSampleId() {
		return sampleId;
	}
	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}
	public int getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(int reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	public BusinessUnitVO getProducer() {
		return producer;
	}
	public void setProducer(BusinessUnitVO producer) {
		this.producer = producer;
	}
	public BusinessUnitVO getTestee() {
		return testee;
	}
	public void setTestee(BusinessUnitVO testee) {
		this.testee = testee;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
}