package com.lhfs.fsn.vo.business;


/**
 * date : 2016.05.06
 * @author wb
 *
 */

public class BussinessBaseInfoVO {

	private Long id;
	
	private String licenseNo; //营业执照号
	
	private String enterprise_name;  // 企业名称
	
	private String enterprise_type;     //企业类型
	
	private String address;	 //地址
	
	private String contact;  // 联系电话

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

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public String getEnterprise_type() {
		return enterprise_type;
	}

	public void setEnterprise_type(String enterprise_type) {
		this.enterprise_type = enterprise_type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
}
