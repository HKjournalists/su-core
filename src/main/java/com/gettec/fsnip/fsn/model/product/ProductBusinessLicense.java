package com.gettec.fsnip.fsn.model.product;

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
/**
 * description:为了实现一个产品下，可以出现多个生产企业
 * @author wb
 * DATE：2016.05.17 15:30
 */
@Entity(name="product_business_license")
public class ProductBusinessLicense extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
    /**
     * 产品ID
     */
	@Column(name="product_id")
	private Long productId;
	/**
	 * 企业ID
	 */
	@Column(name="business_id")
	private Long businessId;
//	/**
//	 * 营业执照号
//	 */
//	@Column(name="license_no")
//	private String licenseNo;
	/**
	 * 创建日期
	 */
	@Column(name="create_date")
	private Date createDate;
	/**
	 * 企业名称
	 */
	@Column(name="business_name")
	private String businessName;

	
	@OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name="RESOURCE_ID")
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
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
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
