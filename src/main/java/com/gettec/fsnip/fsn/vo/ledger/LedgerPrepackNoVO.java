package com.gettec.fsnip.fsn.vo.ledger;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.gettec.fsnip.fsn.model.market.Resource;


public class LedgerPrepackNoVO {

	private Long id;
	
	private String productName;     // 产品名称

	private String alias;	 //别名

	private String standard;	 //规格

	private String number;     //数量
	
	private String purchaseTime;	 //采购时间

	private String companyName;	 //供货商名称
	
	private String companyPhone;	 //供货商联系电话

	private String supplier;	 //供货商联系人

	private String companyAddress;     //供货商地址
	
	private long qiyeId; 
	
    private Resource licResource = new Resource() ;  // 营业执照图片
	
	private Resource qsResource = new Resource();  // 营业执照图片
	
	private Resource disResource = new Resource();  // 营业执照图片

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public long getQiyeId() {
		return qiyeId;
	}

	public void setQiyeId(long qiyeId) {
		this.qiyeId = qiyeId;
	}

	public Resource getLicResource() {
		return licResource;
	}

	public void setLicResource(Resource licResource) {
		this.licResource = licResource;
	}

	public Resource getQsResource() {
		return qsResource;
	}

	public void setQsResource(Resource qsResource) {
		this.qsResource = qsResource;
	}

	public Resource getDisResource() {
		return disResource;
	}

	public void setDisResource(Resource disResource) {
		this.disResource = disResource;
	}

	
}
