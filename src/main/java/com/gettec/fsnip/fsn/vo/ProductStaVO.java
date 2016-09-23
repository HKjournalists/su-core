package com.gettec.fsnip.fsn.vo;

import java.util.Date;

public class ProductStaVO {
	private String businessName;
	private String productName;
	private String barcode;
    private Long reportQuantity;
    private Long  notPublishReportQuantity;
    private Date lastPubDate;

	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getReportQuantity() {
		return reportQuantity;
	}
	public void setReportQuantity(Long reportQuantity) {
		this.reportQuantity = reportQuantity;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Date getLastPubDate() {
		return lastPubDate;
	}
	public void setLastPubDate(Date lastPubDate) {
		this.lastPubDate = lastPubDate;
	}
	public Long getNotPublishReportQuantity() {
		return notPublishReportQuantity;
	}
	public void setNotPublishReportQuantity(Long notPublishReportQuantity) {
		this.notPublishReportQuantity = notPublishReportQuantity;
	}

	
}
