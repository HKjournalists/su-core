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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * License Entity<br>
 * 生产许可证信息
 * @author Hui Zhang
 */
@Entity(name = "production_license_info")
public class ProductionLicenseInfo extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "qs_no")
	private String qsNo;        // 证书编号(生产许可证)
	
	@Column(name = "busunit_name")
	private String busunitName;     // 生产企业名称
	
	@Column(name = "product_name")
	private String productName;       // 产品名称
	
	@Column(name = "start_time")
	private Date startTime;        // 有效期: 开始时间
	
	@Column(name = "end_time")
	private Date endTime;          // 有效期: 结束时间
	
	@Column(name = "accommodation")
	private String accommodation;      // 住所
	
	@Column(name = "accommodation_other_address")
	private String accOtherAddress;      // 住所别名
	
	@Column(name = "production_address")
	private String productionAddress;  // 生产地址
	
	@Column(name = "production_other_address")
	private String proOtherAddress;  // 地址别名
	
	@Column(name = "check_type")
	private String checkType;    // 检验方式
	
	@ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JoinColumn(name = "qsformat_id", nullable = true)
	private LicenceFormat qsnoFormat;    // Qs号输入规则的id

	/* 生产许可证  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="productionLicenseInfo_to_resource",joinColumns={@JoinColumn(name="qs_id")}, 
			   inverseJoinColumns = {@JoinColumn(name="resource_id")})
	private Set<Resource> qsAttachments = new HashSet<Resource>();
    
    /**
     * 功能描述：在企业基本信息界面，新增或编辑一条qs信息时，记录该条qs号所属的主企业
     * @author ZhangHui 2015/5/21
     */
    @Transient
    private Long bussinessId;
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQsNo() {
		return qsNo;
	}

	public void setQsNo(String qsNo) {
		this.qsNo = qsNo;
	}

	public String getBusunitName() {
		return busunitName;
	}

	public void setBusunitName(String busunitName) {
		this.busunitName = busunitName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getStartTime() {
		return startTime;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndTime() {
		return endTime;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(String accommodation) {
		this.accommodation = accommodation;
	}

	public String getProductionAddress() {
		return productionAddress;
	}

	public void setProductionAddress(String productionAddress) {
		this.productionAddress = productionAddress;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getAccOtherAddress() {
		return accOtherAddress;
	}

	public void setAccOtherAddress(String accOtherAddress) {
		this.accOtherAddress = accOtherAddress;
	}

	public String getProOtherAddress() {
		return proOtherAddress;
	}

	public void setProOtherAddress(String proOtherAddress) {
		this.proOtherAddress = proOtherAddress;
	}
	
	public ProductionLicenseInfo(String qsNo){
		this.qsNo = qsNo;
	}

	public Set<Resource> getQsAttachments() {
		return qsAttachments;
	}

	public void setQsAttachments(Set<Resource> qsAttachments) {
		this.qsAttachments = qsAttachments;
	}

	public void addQsResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.qsAttachments.add(resource);
		}
	}
	
	public void removeQsResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.qsAttachments.remove(resource);
		}
	}

    public LicenceFormat getQsnoFormat() {
        return qsnoFormat;
    }

    public void setQsnoFormat(LicenceFormat qsnoFormat) {
        this.qsnoFormat = qsnoFormat;
    }

	public Long getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(Long bussinessId) {
		this.bussinessId = bussinessId;
	}
	
	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.qsAttachments.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.qsAttachments.add(resource);
		}
	}

	public ProductionLicenseInfo(){}
    
    public ProductionLicenseInfo(ProductionLicenseInfo qsInfo){
    	if(qsInfo==null){return;}
    	this.id = qsInfo.getId();
    	this.qsNo = qsInfo.getQsNo();
    	this.busunitName = qsInfo.getBusunitName();
    	this.productName = qsInfo.getProductName();
    	this.startTime = qsInfo.getStartTime();
    	this.endTime = qsInfo.getEndTime();
    	this.accommodation = qsInfo.getAccommodation();
    	this.accOtherAddress = qsInfo.getAccommodation();
    	this.productionAddress = qsInfo.getProductionAddress();
    	this.proOtherAddress = qsInfo.getProOtherAddress();
    	this.checkType = qsInfo.getCheckType();
    	this.qsnoFormat = new LicenceFormat(qsInfo.getQsnoFormat());
    	for(Resource qsres : qsInfo.getQsAttachments()){
    		Resource res = new Resource(qsres);
    		this.qsAttachments.add(res);
    	}
    }
}
