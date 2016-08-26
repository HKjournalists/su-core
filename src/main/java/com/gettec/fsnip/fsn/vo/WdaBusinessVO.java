package com.gettec.fsnip.fsn.vo;

import java.util.Date;

public class WdaBusinessVO {
	     	
	private Long fsnBusinessId;     //fsn生产企业id
	
	private String name;     // 企业名称
	
	private String address;       // 地址
	
	private Date lastDate;        // 最后上传时间
	
	private String orgCode;      // 组织机构代码
	
	private String telephone;  // 电话

	public Long getFsnBusinessId() {
		return fsnBusinessId;
	}

	public void setFsnBusinessId(Long fsnBusinessId) {
		this.fsnBusinessId = fsnBusinessId;
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

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
}