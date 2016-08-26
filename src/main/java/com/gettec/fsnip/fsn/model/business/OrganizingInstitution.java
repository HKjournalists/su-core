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
 * 组织机构代码证件信息
 * @author Hui Zhang
 */
@Entity(name = "organizing_institution")
public class OrganizingInstitution extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@Column(name = "org_code")
	private String orgCode;      // 组织机构代码
	
	@Column(name = "org_name")
	private String orgName;           // 组织机构名称
	
	@Column(name = "start_time")
	private Date startTime;        // 有效期: 开始时间
	
	@Column(name = "end_time")
	private Date endTime;          // 有效期: 结束时间
	
	@Column(name = "units_awarded")
	private String unitsAwarded;  // 颁发单位
	
	@Column(name = "org_type")
	private String orgType;  // 组织机构类型
	
	@Column(name = "org_address")
	private String orgAddress;      // 组织机构地址

	@Column(name="other_address")
	private String otherAddress;
	
	@Column(name="register_no")
	private String registerNo;    //登记号
	
	@Transient
	private boolean rhFlag;   //由于仁怀系统界面跟企业基本信息界面字段有差异，使用该字段来标志对某些非公共字段进行单独更新
	
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public String getUnitsAwarded() {
		return unitsAwarded;
	}

	public void setUnitsAwarded(String unitsAwarded) {
		this.unitsAwarded = unitsAwarded;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getOtherAddress() {
		return otherAddress;
	}

	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}

	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	public boolean isRhFlag() {
		return rhFlag;
	}

	public void setRhFlag(boolean rhFlag) {
		this.rhFlag = rhFlag;
	}
	
	public OrganizingInstitution(){}
	
	public OrganizingInstitution(String orgCode){
		this.orgCode = orgCode;
	}
}
