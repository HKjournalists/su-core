package com.gettec.fsnip.fsn.model.member;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Member Entity<br>
 * 
 * 
 * @author HCJ
 */
@Entity(name = "member")
public class Member extends Model{
	
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "orgId")
	private Long orgId;  // 企业id
	
	@Column(name = "name", length = 200)
	private String name;  // 名称
	
	@Column(name = "description", length = 5000)
	private String description;  // 个人简介

	@Column(name = "status", length = 10)
	private String status;  // 状态 
	
	@Column(name = "position", length = 200)
	private String position;   // 岗位
	
	@Column(name = "address", length = 200)
	private String address;   // 户籍地址
	
	@Column(name = "email", length = 200)
	private String email;   // 邮箱
	
	@Column(name = "tel", length = 200)
	private String tel;   // 固定电话
	
	@Column(name = "identificationNo", length = 200)
	private String identificationNo;   // 证件号码

	@Column(name = "sex")
	private String sex;   // 性别

	@Column(name = "credentialsType")
	private String credentialsType;   // 证件类型

	@Column(name = "nation")
	private String nation;   // 民族

	@Column(name = "appointUnit")
	private String appointUnit;   // 任免单位

	@Column(name = "personType")
	private String personType;   // 人员类型

	@Column(name = "workType")
	private String workType;   // 工种

	@Column(name = "issueUnit")
	private String issueUnit;   // 发证单位

	@Column(name = "mobilePhone")
	private String mobilePhone;   // 手机

	@Column(name = "origin")
	private String origin;  // 来源  1：录入  2：监管

	@Column(name = "healthCertificateNo")
	private String healthNo;   // 健康证编号
	
	@Transient
	private String qualificationNo;   // 从业资格证编号
	
	@Transient
	private String honorNo1;   // 荣誉证书编号1
	
	@Transient
	private String honorNo2;   // 荣誉证书编号2
	
	@Transient
	private String honorNo3;   // 荣誉证书编号3

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_HD_MEMBER_TO_RESOURCE",joinColumns={@JoinColumn(name="MEMBER_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> hdAttachments = new HashSet<Resource>();
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_HTH_MEMBER_TO_RESOURCE",joinColumns={@JoinColumn(name="MEMBER_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> hthAttachments = new HashSet<Resource>();
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_QC_MEMBER_TO_RESOURCE",joinColumns={@JoinColumn(name="MEMBER_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> qcAttachments = new HashSet<Resource>();
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_HN_MEMBER_TO_RESOURCE",joinColumns={@JoinColumn(name="MEMBER_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> hnAttachments = new HashSet<Resource>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getHealthNo() {
		return healthNo;
	}

	public void setHealthNo(String healthNo) {
		this.healthNo = healthNo;
	}

	public String getQualificationNo() {
		return qualificationNo;
	}

	public void setQualificationNo(String qualificationNo) {
		this.qualificationNo = qualificationNo;
	}

	public String getHonorNo1() {
		return honorNo1;
	}

	public void setHonorNo1(String honorNo1) {
		this.honorNo1 = honorNo1;
	}

	public String getHonorNo2() {
		return honorNo2;
	}

	public void setHonorNo2(String honorNo2) {
		this.honorNo2 = honorNo2;
	}

	public String getHonorNo3() {
		return honorNo3;
	}

	public void setHonorNo3(String honorNo3) {
		this.honorNo3 = honorNo3;
	}

	public Set<Resource> getHdAttachments() {
		return hdAttachments;
	}

	public void setHdAttachments(Set<Resource> hdAttachments) {
		this.hdAttachments = hdAttachments;
	}

	public Set<Resource> getHthAttachments() {
		return hthAttachments;
	}

	public void setHthAttachments(Set<Resource> hthAttachments) {
		this.hthAttachments = hthAttachments;
	}

	public Set<Resource> getQcAttachments() {
		return qcAttachments;
	}

	public void setQcAttachments(Set<Resource> qcAttachments) {
		this.qcAttachments = qcAttachments;
	}

	public Set<Resource> getHnAttachments() {
		return hnAttachments;
	}

	public void setHnAttachments(Set<Resource> hnAttachments) {
		this.hnAttachments = hnAttachments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public void removeHdResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.hdAttachments.remove(resource);
		}
	}

	public void addHdResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.hdAttachments.add(resource);
		}
	}
	public void removeHthResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.hthAttachments.remove(resource);
		}
	}
	
	public void addHthResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.hthAttachments.add(resource);
		}
	}
	public void removeQcResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.qcAttachments.remove(resource);
		}
	}
	
	public void addQcResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.qcAttachments.add(resource);
		}
	}
	public void removeHnResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.hnAttachments.remove(resource);
		}
	}
	
	public void addHnResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.hnAttachments.add(resource);
		}
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCredentialsType() {
		return credentialsType;
	}

	public void setCredentialsType(String credentialsType) {
		this.credentialsType = credentialsType;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAppointUnit() {
		return appointUnit;
	}

	public void setAppointUnit(String appointUnit) {
		this.appointUnit = appointUnit;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getIssueUnit() {
		return issueUnit;
	}

	public void setIssueUnit(String issueUnit) {
		this.issueUnit = issueUnit;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}