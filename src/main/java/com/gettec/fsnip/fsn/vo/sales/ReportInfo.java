package com.gettec.fsnip.fsn.vo.sales;


/**
 * 相册结构话字段VO
 * @author tangxin 2015-05-05
 *
 */
public class ReportInfo {

	
	private long censorReportNumber; //送检报告数量
	private long allReportNumber; //总报告数量
	private long zjReportNumber;
	private long cjReportNumber;
	private double riskIndex;
	public long getCensorReportNumber() {
		return censorReportNumber;
	}
	public void setCensorReportNumber(long censorReportNumber) {
		this.censorReportNumber = censorReportNumber;
	}
	public long getAllReportNumber() {
		return allReportNumber;
	}
	public void setAllReportNumber(long allReportNumber) {
		this.allReportNumber = allReportNumber;
	}
	public long getZjReportNumber() {
		return zjReportNumber;
	}
	public void setZjReportNumber(long zjReportNumber) {
		this.zjReportNumber = zjReportNumber;
	}
	public long getCjReportNumber() {
		return cjReportNumber;
	}
	public void setCjReportNumber(long cjReportNumber) {
		this.cjReportNumber = cjReportNumber;
	}
	public double getRiskIndex() {
		return riskIndex;
	}
	public void setRiskIndex(double riskIndex) {
		this.riskIndex = riskIndex;
	}
	@Override
	public String toString() {
		return "ReportInfo [censorReportNumber=" + censorReportNumber + ", allReportNumber=" + allReportNumber
				+ ", zjReportNumber=" + zjReportNumber + ", cjReportNumber=" + cjReportNumber + ", riskIndex="
				+ riskIndex + "]";
	}
}
