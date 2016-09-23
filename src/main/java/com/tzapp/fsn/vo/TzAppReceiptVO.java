package com.tzapp.fsn.vo;

public class TzAppReceiptVO {

	private long id;
	private String barcode;
	private String name;
	private String dealDate;
	private String busName;//供应商名称
	private int inStatus;//待收货和已收货状态
	private int returnStatus;//拒收状态
	private String refuseReason;//拒收原因
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public int getInStatus() {
		return inStatus;
	}
	public void setInStatus(int inStatus) {
		this.inStatus = inStatus;
	}
	public int getReturnStatus() {
		return returnStatus;
	}
	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	
}
