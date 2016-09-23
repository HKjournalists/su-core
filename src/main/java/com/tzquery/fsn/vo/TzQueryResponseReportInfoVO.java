/**
 * 
 */
package com.tzquery.fsn.vo;

/**
 * @author ChenXiaolin 2015-12-1
 */
public class TzQueryResponseReportInfoVO {

	private String proBarcode;  //产品条形码
	private String proName;		//产品名称
	private String reportNum;	//报告编号
	private String proBatch;	//产品批次
	private String testType;	//检测类型
	private String testUnit;	//检测单位
	private String testResult;	//检测结果
	private String reportPdf;	//报告原件PDF
	
	public String getProBarcode() {
		return proBarcode;
	}
	public void setProBarcode(String proBarcode) {
		this.proBarcode = proBarcode;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getReportNum() {
		return reportNum;
	}
	public void setReportNum(String reportNum) {
		this.reportNum = reportNum;
	}
	public String getProBatch() {
		return proBatch;
	}
	public void setProBatch(String proBatch) {
		this.proBatch = proBatch;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getTestUnit() {
		return testUnit;
	}
	public void setTestUnit(String testUnit) {
		this.testUnit = testUnit;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String getReportPdf() {
		return reportPdf;
	}
	public void setReportPdf(String reportPdf) {
		this.reportPdf = reportPdf;
	}
	
}
