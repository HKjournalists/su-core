package com.gettec.fsnip.fsn.model.business;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * EnterpriseRegiste Entity<br>
 * 企业用户注册信息
 * @author XianZhen Long
 */
@Entity(name = "enterprise_registe")
public class EnterpriseRegiste extends Model{
	private static final long serialVersionUID = -6263810349857034210L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "userName")
	private String userName;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "enterpriteName")
	private String enterpriteName;
	
	@Column(name = "enterptiteAddress")
	private String enterptiteAddress;
	
	@Column(name = "enterpriteType")
	private String enterpriteType;
	
	@Column(name = "legalPerson")
	private String legalPerson;
	
	@Column(name = "status")
	private String status;
	
	@Column(name="licenseNo")
	private String licenseNo;
	
	@Column(name="organizationNo")
	private String organizationNo;
	
	@Column(name="serviceNo")
	private String serviceNo;
	
	@Column(name="productNo")
	private String productNo;
	
	@Column(name="passNo")
	private String passNo;
	
	@Column(name = "enterpriteDate")
	private Date enterpriteDate;
	
	@Column(name="otherAddress")
	private String otherAddress;
	
	@Column(name = "telephone")
	private String telephone;
	
	@Column(name = "origin")
	private String origin;
	
	@Transient
	private boolean signFlag;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEnterpriteDate() {
		return enterpriteDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEnterpriteDate(Date enterpriteDate) {
		this.enterpriteDate = enterpriteDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/* 企业logo  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_BUSINESS_LOGO_TO_RESOURCE",joinColumns={@JoinColumn(name="ENTERPRISE_REGISTE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> logoAttachments = new HashSet<Resource>();

	/* 组织机构代码证件  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_ORG_LICENSE_TO_RESOURCE",joinColumns={@JoinColumn(name="ENTERPRISE_REGISTE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> orgAttachments = new HashSet<Resource>();
	
	/* 营业执照  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_BUSINESS_LICENSE_TO_RESOURCE",joinColumns={@JoinColumn(name="ENTERPRISE_REGISTE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> licAttachments = new HashSet<Resource>();
	
	/* 食品流通许可证  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_BUSINESS_DISTRIBUTION_TO_RESOURCE",joinColumns={@JoinColumn(name="ENTERPRISE_REGISTE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> disAttachments = new HashSet<Resource>();
	
	/* 生产许可证  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_BUSINESS_PRODUCTIONlICENSE_TO_RESOURCE",joinColumns={@JoinColumn(name="ENTERPRISE_REGISTE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> qsAttachments = new HashSet<Resource>();
	
	/* 税务登记证信息 */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="t_tax_register_cert_to_resource",joinColumns={@JoinColumn(name="ENTERPRISE_REGISTE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> taxRegAttachments = new HashSet<Resource>();
	
	/* 酒类销售许可证信息/餐饮服务许可证  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="t_liquor_sales_license_to_resource",joinColumns={@JoinColumn(name="ENTERPRISE_REGISTE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> liquorAttachments = new HashSet<Resource>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEnterpriteName() {
		return enterpriteName;
	}

	public void setEnterpriteName(String enterpriteName) {
		this.enterpriteName = enterpriteName;
	}

	public String getEnterptiteAddress() {
		return enterptiteAddress;
	}

	public void setEnterptiteAddress(String enterptiteAddress) {
		this.enterptiteAddress = enterptiteAddress;
	}

	public String getEnterpriteType() {
		return enterpriteType;
	}

	public void setEnterpriteType(String enterpriteType) {
		this.enterpriteType = enterpriteType;
	}

	public Set<Resource> getLogoAttachments() {
		return logoAttachments;
	}

	public void setLogoAttachments(Set<Resource> logoAttachments) {
		this.logoAttachments = logoAttachments;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public Set<Resource> getOrgAttachments() {
		return orgAttachments;
	}

	public void setOrgAttachments(Set<Resource> orgAttachments) {
		this.orgAttachments = orgAttachments;
	}

	public Set<Resource> getLicAttachments() {
		return licAttachments;
	}

	public void setLicAttachments(Set<Resource> licAttachments) {
		this.licAttachments = licAttachments;
	}

	public Set<Resource> getDisAttachments() {
		return disAttachments;
	}

	public void setDisAttachments(Set<Resource> disAttachments) {
		this.disAttachments = disAttachments;
	}

	public void addLogoResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.logoAttachments.add(resource);
		}
	}
	
	public void addLicResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.licAttachments.add(resource);
		}
	}
	
	public void addOrgResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.orgAttachments.add(resource);
		}
	}
	
	public void addDisResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.disAttachments.add(resource);
		}
	}
	
	public void addQsResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.qsAttachments.add(resource);
		}
	}
	
	public void addTaxRegResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.taxRegAttachments.add(resource);
		}
	}
	
	public void addLiquorResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.liquorAttachments.add(resource);
		}
	}
	
	public void removeLogoResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.logoAttachments.remove(resource);
		}
	}
	
	public void removeOrgResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.orgAttachments.remove(resource);
		}
	}

	public void removeLicenseResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.licAttachments.remove(resource);
		}
	}
	
	public void removeDisResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.disAttachments.remove(resource);
		}
	}
	
	public void removeQsResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.qsAttachments.remove(resource);
		}
	}

	public void removeTaxRegResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.taxRegAttachments.remove(resource);
		}
	}
	
	public void removeLiquorResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.liquorAttachments.remove(resource);
		}
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getOrganizationNo() {
		return organizationNo;
	}

	public void setOrganizationNo(String organizationNo) {
		this.organizationNo = organizationNo;
	}

	public String getServiceNo() {
		return serviceNo;
	}

	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getPassNo() {
		return passNo;
	}

	public void setPassNo(String passNo) {
		this.passNo = passNo;
	}

	public String getOtherAddress() {
		return otherAddress;
	}

	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}

	public Set<Resource> getQsAttachments() {
		return qsAttachments;
	}

	public void setQsAttachments(Set<Resource> qsAttachments) {
		this.qsAttachments = qsAttachments;
	}

	public Set<Resource> getTaxRegAttachments() {
		return taxRegAttachments;
	}

	public void setTaxRegAttachments(Set<Resource> taxRegAttachments) {
		this.taxRegAttachments = taxRegAttachments;
	}

	public Set<Resource> getLiquorAttachments() {
		return liquorAttachments;
	}

	public void setLiquorAttachments(Set<Resource> liquorAttachments) {
		this.liquorAttachments = liquorAttachments;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public boolean isSignFlag() {
		return signFlag;
	}

	public void setSignFlag(boolean signFlag) {
		this.signFlag = signFlag;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
}
