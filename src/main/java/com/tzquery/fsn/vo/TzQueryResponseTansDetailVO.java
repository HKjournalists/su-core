/**
 * 
 */
package com.tzquery.fsn.vo;

/**
 * 交易明细VO
 * @author ChenXiaolin 2015-12-03
 */
public class TzQueryResponseTansDetailVO {

	private String transTarget;		//交易对象
	private String license;			//营业执照号
	private String transType;		//交易类型
	private String proBarcode;		//产品条形码
	private String proName;			//产品名称
	private String proFormat;		//产品规格
	private String proBatch;		//产品批次
	private String proDate;			//生产日期
	private String transDate;		//交易时间
	private Long transAmount;		//交易数量
	
	public String getTransTarget() {
		return transTarget;
	}
	public void setTransTarget(String transTarget) {
		this.transTarget = transTarget;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
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
	public String getProFormat() {
		return proFormat;
	}
	public void setProFormat(String proFormat) {
		this.proFormat = proFormat;
	}
	public String getProBatch() {
		return proBatch;
	}
	public void setProBatch(String proBatch) {
		this.proBatch = proBatch;
	}
	public String getProDate() {
		return proDate;
	}
	public void setProDate(String proDate) {
		this.proDate = proDate;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public Long getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(Long transAmount) {
		this.transAmount = transAmount;
	}
	
}
