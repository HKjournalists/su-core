package com.lhfs.fsn.vo.business;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;

/**
 * 企业基本信息轻量级封装的VO
 * @author TangXin
 *
 */
public class LightBusUnitVO {
	
	private Long id; //企业id
	private String name; //企业名称
	private String address; //企业地址
	private String otherAddress; //街道地址
	private String type; //企业类型
	private String licenseNo; //营业执照号
	private Long organization; //组织机构
	private boolean signFlag;//签名标志
	private boolean passFlag;//签名标志
	/**
	 * 返回给前台 生产企业入报告时用来判断生产日期是否必填
	 * true :必填  false：非必填
	 * @author longxianzhen 2015/07/17
	 */
	private boolean proDateIsRequired;//生产日期是否必填
	
	public boolean isSignFlag() {
		return signFlag;
	}

	public void setSignFlag(boolean signFlag) {
		this.signFlag = signFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getOtherAddress() {
		return otherAddress;
	}

	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public LightBusUnitVO() {
		super();
	}

	public boolean isProDateIsRequired() {
		return proDateIsRequired;
	}

	public void setProDateIsRequired(boolean proDateIsRequired) {
		this.proDateIsRequired = proDateIsRequired;
	}

	public boolean isPassFlag() {
		return passFlag;
	}

	public void setPassFlag(boolean passFlag) {
		this.passFlag = passFlag;
	}

	public LightBusUnitVO(Long id, String name, String address,
			String otherAddress, String type, String licenseNo,
			Long organization, boolean signFlag) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.otherAddress = otherAddress;
		this.type = type;
		this.licenseNo = licenseNo;
		this.organization = organization;
		this.signFlag = signFlag;
	}

	public LightBusUnitVO(BusinessUnit busUnit){
		if(busUnit == null) {return;}
		this.id=busUnit.getId();
		this.name=busUnit.getName();
		this.address=busUnit.getAddress();
		this.otherAddress = busUnit.getOtherAddress();
		this.type = busUnit.getType();
		this.licenseNo = busUnit.getLicense()==null?null:busUnit.getLicense().getLicenseNo();
		this.organization = busUnit.getOrganization();
		this.signFlag=busUnit.isSignFlag();
	}
	
}
