package com.gettec.fsnip.fsn.model.market;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.vo.business.report.BusinessUnitOfReportVO;

@Entity(name="T_TEST_TEMP_BUSUNIT")
public class MkTempBusUnit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228456151572682151L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="LICENSE_NO")
	private String licenseNo;
	
	@Column(name="QS_NO")
	private String qsNo;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="CREAT_USER_REALNAME")
	private String createUserRealName;
	
	@Column(name="LAST_MODIFY_TIME")
	private Date lastModifyTime;
	
	@Column(name="ORGANIZATION")
	private Long organization;
	
	@Column(name="qsno_formatId")
	private Integer qsnoFormatId; //qs号输入的规则

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getQsNo() {
		return qsNo;
	}

	public void setQsNo(String qsNo) {
		this.qsNo = qsNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateUserRealName() {
		return createUserRealName;
	}

	public void setCreateUserRealName(String createUserRealName) {
		this.createUserRealName = createUserRealName;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public Integer getQsnoFormatId() {
		return qsnoFormatId;
	}

	public void setQsnoFormatId(Integer qsnoFormatId) {
		this.qsnoFormatId = qsnoFormatId;
	}

	public MkTempBusUnit(){}
	
	public MkTempBusUnit(BusinessUnitOfReportVO bus_vo){
		this.licenseNo = bus_vo.getLicenseno();
		this.name = bus_vo.getName();
		this.address = bus_vo.getAddress();
	}
}
