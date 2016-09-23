package com.gettec.fsnip.fsn.model.product;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.business.ProductionLicenseInfo;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.resource.ResourceOfProlicinfoApplicantClaim;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * 生产许可证信息（企业申请认领时，待审核qs信息）
 * @author ZhangHui 2015/5/29
 */
@Entity(name = "production_license_info_applicant_claim_backup")
public class ProductionLicenseInfoApplicantClaim extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * 外键：关联 production_license_info id
	 */
	@Column(name = "qs_id")
	private Long qs_id;
	
	/**
	 * 生产企业名称
	 */
	@Column(name = "busunit_name")
	private String busunitName;
	
	/**
	 * 产品名称
	 */
	@Column(name = "product_name")
	private String productName;

	/**
	 * 有效期: 开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 有效期: 结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;

	/**
	 * 住所
	 */
	@Column(name = "accommodation")
	private String accommodation;

	/**
	 * 住所别名
	 */
	@Column(name = "accommodation_other_address")
	private String accOtherAddress;

	/**
	 * 生产地址
	 */
	@Column(name = "production_address")
	private String productionAddress;

	/**
	 * 地址别名
	 */
	@Column(name = "production_other_address")
	private String proOtherAddress;

	/**
	 * 检验方式
	 */
	@Column(name = "check_type")
	private String checkType;
	
	/**
	 * 输入规则
	 */
	@Column(name = "qsformat_id")
	private int qsformatId;
	
	/**
	 * 生产许可证图片
	 */
	@Transient
	private List<ResourceOfProlicinfoApplicantClaim> qsAttachments;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQs_id() {
		return qs_id;
	}

	public void setQs_id(Long qs_id) {
		this.qs_id = qs_id;
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

	public String getAccOtherAddress() {
		return accOtherAddress;
	}

	public void setAccOtherAddress(String accOtherAddress) {
		this.accOtherAddress = accOtherAddress;
	}

	public String getProductionAddress() {
		return productionAddress;
	}

	public void setProductionAddress(String productionAddress) {
		this.productionAddress = productionAddress;
	}

	public String getProOtherAddress() {
		return proOtherAddress;
	}

	public void setProOtherAddress(String proOtherAddress) {
		this.proOtherAddress = proOtherAddress;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public int getQsformatId() {
		return qsformatId;
	}

	public void setQsformatId(int qsformatId) {
		this.qsformatId = qsformatId;
	}

	public List<ResourceOfProlicinfoApplicantClaim> getQsAttachments() {
		return qsAttachments;
	}

	public void setQsAttachments(
			List<ResourceOfProlicinfoApplicantClaim> qsAttachments) {
		this.qsAttachments = qsAttachments;
	}

	public ProductionLicenseInfoApplicantClaim(){}
	
	public ProductionLicenseInfoApplicantClaim(ProductionLicenseInfo proLicInfo) {
		this.qs_id = proLicInfo.getId();
		this.busunitName = proLicInfo.getBusunitName();
		this.productName = proLicInfo.getProductName();
		this.startTime = proLicInfo.getStartTime();
		this.endTime = proLicInfo.getEndTime();
		this.accommodation = proLicInfo.getAccommodation();
		this.accOtherAddress = proLicInfo.getAccOtherAddress();
		this.productionAddress = proLicInfo.getProductionAddress();
		this.proOtherAddress = proLicInfo.getProOtherAddress();
		this.checkType = proLicInfo.getCheckType();
		this.qsformatId = proLicInfo.getQsnoFormat().getId();
	}
}
