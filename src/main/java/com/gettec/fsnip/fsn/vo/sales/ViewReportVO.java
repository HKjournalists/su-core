package com.gettec.fsnip.fsn.vo.sales;

/**
 * 产品相册预览报告的VO
 * @author tangxin 2015-05-06
 *
 */
public class ViewReportVO {

	private Long reportId; //报告Id
	private String productName; //产品名称
	private String productFormat; //产品规格
	private String productDesc; //产品描述
	private String reportNo; //报告编号
	private String reportType; //报告类型
	private long selfNumber; //自检报告数量
	private long censorNumber; //送检报告数量
	private long sampleNumber; //抽检报告数量
	private String pdfPath;
	
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductFormat() {
		return productFormat;
	}
	public void setProductFormat(String productFormat) {
		this.productFormat = productFormat;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public long getSelfNumber() {
		return selfNumber;
	}
	public void setSelfNumber(long selfNumber) {
		this.selfNumber = selfNumber;
	}
	public long getCensorNumber() {
		return censorNumber;
	}
	public void setCensorNumber(long censorNumber) {
		this.censorNumber = censorNumber;
	}
	public long getSampleNumber() {
		return sampleNumber;
	}
	public void setSampleNumber(long sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
	@Override
	public String toString() {
		return "ViewReportVO [reportId=" + reportId + ", productName=" + productName + ", productFormat="
				+ productFormat + ", productDesc=" + productDesc + ", reportNo=" + reportNo + ", reportType="
				+ reportType + ", selfNumber=" + selfNumber + ", censorNumber=" + censorNumber + ", sampleNumber="
				+ sampleNumber + ", pdfPath=" + pdfPath + "]";
	}
	
	
	
}
