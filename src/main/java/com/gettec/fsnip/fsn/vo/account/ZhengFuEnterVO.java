package com.gettec.fsnip.fsn.vo.account;

import java.util.Date;

public class ZhengFuEnterVO {
	private Long businessId;
	private String businessName;
	private String businessType;
	private Long productQuantity;
	private Long notPublishProQuantity;//未发布报告的产品数量
    private Long reportQuantity;
    private Date enterpriteDate;
    private Long org;//组织机构

	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Long getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Long productQuantity) {
		this.productQuantity = productQuantity;
	}
	public Long getReportQuantity() {
		return reportQuantity;
	}
	public void setReportQuantity(Long reportQuantity) {
		this.reportQuantity = reportQuantity;
	}
	public Date getEnterpriteDate() {
		return enterpriteDate;
	}
	public void setEnterpriteDate(Date enterpriteDate) {
		this.enterpriteDate = enterpriteDate;
	}
	public Long getNotPublishProQuantity() {
		return notPublishProQuantity;
	}
	public void setNotPublishProQuantity(Long notPublishProQuantity) {
		this.notPublishProQuantity = notPublishProQuantity;
	}
	public Long getOrg() {
		return org;
	}
	public void setOrg(Long org) {
		this.org = org;
	}
	
}
