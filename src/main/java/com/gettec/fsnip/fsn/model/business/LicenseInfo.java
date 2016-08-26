package com.gettec.fsnip.fsn.model.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * License Entity<br>
 * 营业执照信息
 * @author Hui Zhang
 */
@Entity(name = "license_info")
public class LicenseInfo extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@Column(name = "license_no")
	private String licenseNo;        // 营业执照注册号
	
	@Column(name = "license_name")
	private String licensename;     // 经营主体名称
	
	@Column(name = "legal_name")
	private String legalName;       // 法人代表(负责人)
	
	@Column(name = "start_time")
	private Date startTime;        // 有效期: 开始时间
	
	@Column(name = "end_time")
	private Date endTime;          // 有效期: 结束时间
	
	@Column(name = "registration_time")
	private Date registrationTime;  // 注册时间
	
	@Column(name="establish_time")
	private Date establishTime;     //成立时间
	
	@Column(name = "issuing_authority")
	private String issuingAuthority;  // 发照机关
	
	@Column(name = "subject_type")
	private String subjectType;      // 主体类型
	
	@Column(name = "business_address")
	private String businessAddress;  // 经营场所(企业地址)
	
	@Column(name = "tolerance_range")
	private String toleranceRange;    // 许可范围
	
	@Column(name = "registered_capital")
	private String registeredCapital;   // 注册资金
	
	@Column(name = "practical_capital")
	private String practicalCapital;   // 实收资金
	
	@Column(name="other_address")
	private String otherAddress;      //地址别名
	
	@Transient
	private boolean rhFlag;   //由于仁怀系统界面跟企业基本信息界面字段有差异，使用该字段来标志对某些非公共字段进行单独更新

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public String getLicensename() {
		return licensename;
	}

	public void setLicensename(String licensename) {
		this.licensename = licensename;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
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

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getRegistrationTime() {
		return registrationTime;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEstablishTime() {
		return establishTime;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEstablishTime(Date establishTime) {
		this.establishTime = establishTime;
	}
	
	public String getIssuingAuthority() {
		return issuingAuthority;
	}

	public void setIssuingAuthority(String issuingAuthority) {
		this.issuingAuthority = issuingAuthority;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getToleranceRange() {
		return toleranceRange;
	}

	public void setToleranceRange(String toleranceRange) {
		this.toleranceRange = toleranceRange;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}
	
	public String getPracticalCapital() {
		return practicalCapital;
	}

	public void setPracticalCapital(String practicalCapital) {
		this.practicalCapital = practicalCapital;
	}

	public String getOtherAddress() {
		return otherAddress;
	}

	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}

	public boolean isRhFlag() {
		return rhFlag;
	}

	public void setRhFlag(boolean rhFlag) {
		this.rhFlag = rhFlag;
	}
	
	public LicenseInfo(){}
	
	public LicenseInfo(String licenseNo){
		this.licenseNo = licenseNo;
	}
}
