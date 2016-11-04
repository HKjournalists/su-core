package com.gettec.fsnip.fsn.model.business;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * License Entity<br>
 * 食品流通许可证信息
 * @author Hui Zhang
 */
@Entity(name = "circulation_permit_info")
public class CirculationPermitInfo extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@Column(name = "distribution_no")
	private String distributionNo;        // 食品流通许可证编号
	
	@Column(name = "licensing_authority")
	private String licensingAuthority;     // 许可机关
	
	@Column(name = "license_name")
	private String licenseName;       // 许可证名称
	
	@Column(name = "start_time")
	private Date startTime;        // 有效期: 开始时间
	
	@Column(name = "end_time")
	private Date endTime;          // 有效期: 结束时间
	
	@Column(name = "subject_type")
	private String subjectType;      // 主体类型
	
	@Column(name = "business_address")
	private String businessAddress;  // 经营场所
	
	@Column(name = "tolerance_range")
	private String toleranceRange;    // 许可范围
	
	@Column(name = "legal_name")
	private String legalName;   // 负责人
	
	@Column(name="other_address")
	private String otherAddress;
	
	@Column(name="tolerance_time")
	private Date toleranceTime;
	
	@Column(name="manage_type")
	private String manageType;
	
	@Column(name="manage_project")
	private String manageProject;


	public String getDistributionNo() {
		return distributionNo;
	}

	public void setDistributionNo(String distributionNo) {
		this.distributionNo = distributionNo;
	}

	public String getLicensingAuthority() {
		return licensingAuthority;
	}

	public void setLicensingAuthority(String licensingAuthority) {
		this.licensingAuthority = licensingAuthority;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
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

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getOtherAddress() {
		return otherAddress;
	}

	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getToleranceTime() {
		return toleranceTime;
	}
	
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setToleranceTime(Date toleranceTime) {
		this.toleranceTime = toleranceTime;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public String getManageProject() {
		return manageProject;
	}

	public void setManageProject(String manageProject) {
		this.manageProject = manageProject;
	}

	public CirculationPermitInfo(String distributionNo) {
		super();
		this.distributionNo = distributionNo;
	}

	public CirculationPermitInfo() {
		super();
	}
	
}
