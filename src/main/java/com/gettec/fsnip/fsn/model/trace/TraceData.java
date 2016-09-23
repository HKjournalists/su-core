package com.gettec.fsnip.fsn.model.trace;

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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

@Entity(name = "trace_data")
public class TraceData  extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@Column(name = "productID")
	private Long productID;
	@Column(name = "address")
	private String address;
	@Column(name = "areaCode")
	private String areaCode;
	@Column(name = "batchCode")
	private String batchCode;
	@Column(name = "className")
	private String className;
	@Column(name = "degree")
	private String degree;
	@Column(name = "departmentID")
	private String departmentID;
	@Column(name = "description")
	private String description;
	@Column(name = "expireDate")
	private String expireDate;
	@Column(name = "factoryName")
	private String factoryName;
	@Column(name = "netContent")
	private String netContent;
	@Column(name = "orgCode")
	private String orgCode;
	@Column(name = "packageSpec")
	private String packageSpec;
	@Column(name = "prodLevel")
	private String prodLevel;
	@Column(name = "prodLine")
	private String prodLine;
	@Column(name = "prodMixture")
	private String prodMixture;
	@Column(name = "prodPics")
	private String prodPics;
	@Temporal(TemporalType.DATE)
	@Column(name = "productDate")
	private Date productDate;
	@Column(name = "productName")
	private String productName;
	@Column(name = "gpsList")
	private String gpsList;
	@Column(name = "timeTrack")
	private String timeTrack;
	@Column(name = "volume")
	private String volume;
	@Column(name = "workShop")
	private String workShop;
	@Column(name = "keyWord")
	private String keyWord;
	@Column(name = "fsnCode")
	private String fsnCode;
	@Column(name = "sourceArea")
	private String sourceArea;
	
//	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
//	@JoinColumn(name = "sourceCertifyResource")
//	private Resource sourceCertify;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="trace_data_to_resource",joinColumns={@JoinColumn(name="TRACE_DATA_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> sourceCertifyList = new HashSet<Resource>();//报告原件
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "growEnvironmentResource")
	private Resource growEnvironmentResource;
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "businessPromiseResource")
	private Resource businessPromiseResource;
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "buyLink")
	private Resource buyLink;
	@Temporal(TemporalType.DATE)
	@Column(name = "sourceDate")
	private Date sourceDate;
	@Column(name = "processor")
	private String processor;
	@Column(name = "packagePlant")
	private String packagePlant;
	@Column(name = "warehouseDate")
	@Temporal(TemporalType.DATE)
	private Date warehouseDate;
	@Column(name = "leaveDate")
	@Temporal(TemporalType.DATE)
	private Date leaveDate;
	@Column(name = "organization")
	private Long organization;
	@Transient
	private boolean isTraceData;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductID() {
		return productID;
	}
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(String departmentID) {
		this.departmentID = departmentID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getNetContent() {
		return netContent;
	}
	public void setNetContent(String netContent) {
		this.netContent = netContent;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getPackageSpec() {
		return packageSpec;
	}
	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}
	public String getProdLevel() {
		return prodLevel;
	}
	public void setProdLevel(String prodLevel) {
		this.prodLevel = prodLevel;
	}
	public String getProdLine() {
		return prodLine;
	}
	public void setProdLine(String prodLine) {
		this.prodLine = prodLine;
	}
	public String getProdMixture() {
		return prodMixture;
	}
	public void setProdMixture(String prodMixture) {
		this.prodMixture = prodMixture;
	}
	public String getProdPics() {
		return prodPics;
	}
	public void setProdPics(String prodPics) {
		this.prodPics = prodPics;
	}
	public Date getProductDate() {
		return productDate;
	}
	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getGpsList() {
		return gpsList;
	}
	public void setGpsList(String gpsList) {
		this.gpsList = gpsList;
	}
	public String getTimeTrack() {
		return timeTrack;
	}
	public void setTimeTrack(String timeTrack) {
		this.timeTrack = timeTrack;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getWorkShop() {
		return workShop;
	}
	public void setWorkShop(String workShop) {
		this.workShop = workShop;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getFsnCode() {
		return fsnCode;
	}
	public void setFsnCode(String fsnCode) {
		this.fsnCode = fsnCode;
	}
	public String getSourceArea() {
		return sourceArea;
	}
	public void setSourceArea(String sourceArea) {
		this.sourceArea = sourceArea;
	}
//	public Resource getSourceCertify() {
//		return sourceCertify;
//	}
//	public void setSourceCertify(Resource sourceCertify) {
//		this.sourceCertify = sourceCertify;
//	}
	
	public Set<Resource> getSourceCertifyList() {
		return sourceCertifyList;
	}
	public void setSourceCertifyList(Set<Resource> sourceCertifyList) {
		this.sourceCertifyList = sourceCertifyList;
	}
	public Resource getGrowEnvironmentResource() {
		return growEnvironmentResource;
	}
	public void setGrowEnvironmentResource(Resource growEnvironmentResource) {
		this.growEnvironmentResource = growEnvironmentResource;
	}
	public Resource getBusinessPromiseResource() {
		return businessPromiseResource;
	}
	public void setBusinessPromiseResource(Resource businessPromiseResource) {
		this.businessPromiseResource = businessPromiseResource;
	}
	public Resource getBuyLink() {
		return buyLink;
	}
	public void setBuyLink(Resource buyLink) {
		this.buyLink = buyLink;
	}
	public Date getSourceDate() {
		return sourceDate;
	}
	public void setSourceDate(Date sourceDate) {
		this.sourceDate = sourceDate;
	}
	public String getProcessor() {
		return processor;
	}
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	public String getPackagePlant() {
		return packagePlant;
	}
	public void setPackagePlant(String packagePlant) {
		this.packagePlant = packagePlant;
	}
	public Date getWarehouseDate() {
		return warehouseDate;
	}
	public void setWarehouseDate(Date warehouseDate) {
		this.warehouseDate = warehouseDate;
	}
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public Long getOrganization() {
		return organization;
	}
	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	public boolean isTraceData() {
		return isTraceData;
	}
	public void setTraceData(boolean isTraceData) {
		this.isTraceData = isTraceData;
	}
	
}
