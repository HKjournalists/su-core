package com.gettec.fsnip.fsn.model.ledger;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

@Entity(name = "ledger_prepackno")
public class LedgerPrepackNo extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "product_name")
	private String productName;     // 产品名称

	@Column(name = "alias")
	private String alias;	 //别名

	@Column(name = "standard")
	private String standard;	 //规格

	@Column(name = "number")
	private String number;     //数量
	
	@Column(name = "purchase_time")
	private Date purchaseTime;	 //采购时间

	@Column(name = "company_name")
	private String companyName;	 //供货商名称
	
	@Column(name = "company_phone")
	private String companyPhone;	 //供货商联系电话

	@Column(name = "supplier")
	private String supplier;	 //供货商联系人

	@Column(name = "company_address")
	private String companyAddress;     //供货商地址
	
	@Column(name = "qiyeId")
	private long qiyeId;     //企业Id
	
	@OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name="LIC_RESOURCE_ID")
	private Resource licResource;  // 营业执照图片
	
	@OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name="QS_RESOURCE_ID")
	private Resource qsResource;  // 营业执照图片
	
	@OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name="DIS_RESOURCE_ID")
	private Resource disResource;  // 营业执照图片

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

	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
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
