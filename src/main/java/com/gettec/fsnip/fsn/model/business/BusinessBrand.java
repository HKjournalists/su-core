package com.gettec.fsnip.fsn.model.business;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.Product;

/**
 * 品牌
 * @author Ryan Wang
 */
@SuppressWarnings("serial")
@Entity(name = "business_brand")
public class BusinessBrand extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name", length = 200)
	private String name;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="brand_category_id", nullable=true)
	private BrandCategory brandCategory;
	
	@Column(name = "registration_date", length = 20)
	private String registrationDate;
	
	@Column(name = "organization", length = 20)
	private Long organization;
	
	@Column(name = "last_modify_time")
	private Date lastModifyTime;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="business_unit_id", nullable=false)
	private BusinessUnit businessUnit;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="BRAND_TO_RESOURCE",joinColumns={@JoinColumn(name="BRAND_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> logAttachments = new HashSet<Resource>();
	
	@Transient
	private List<Product> products;

	public BusinessBrand(){}
	
	public BusinessBrand(Long id, String brandName, String categoryName) {
		setId(id);
		setName(brandName);
		BrandCategory category = new BrandCategory();
		category.setName(categoryName);
		setBrandCategory(category);
	}
	
	public BusinessBrand(String brandName) {
		setName(brandName);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public BrandCategory getBrandCategory() {
		return brandCategory;
	}

	public void setBrandCategory(BrandCategory brandCategory) {
		this.brandCategory = brandCategory;
	}

	public Set<Resource> getLogAttachments() {
		return logAttachments;
	}

	public void setLogAttachments(Set<Resource> logAttachments) {
		this.logAttachments = logAttachments;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public BusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}
  
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.logAttachments.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.logAttachments.add(resource);
		}
	}
}
