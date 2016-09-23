package com.gettec.fsnip.fsn.vo.business;

import java.util.Date;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.BusinessUnitToLims;



public class BusinessUnitLIMSVO {
	
	private String displayName; //"无照" or LicenseNo+"busUnitName"
	
	private String name;  //企业名称
	
	private String address; //地址
	
	private String licenseNo; //营业执照号
	
	private String contact;   //联系人
	
	private String email; //邮箱
	
	private String distributionNo; //流通许可证
	
	private String region; //地区
	
	private String sampleLocal; //抽样场所
	
	private String administrativeLevel; //行政区域
	
	private String personInCharge; //法定代表人(负责人)
	
	private String postalCode; //邮政编码
	
	private String tel; //联系电话
	
	private String fax;  //传真
	
	private String note; //备注
	
	/*扩展信息*/
	private String edition; //环境：XX LIMS
	
	private String createByUser; //创建者名称 
	
	private Date createTime; //创建日期
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDistributionNo() {
		return distributionNo;
	}
	public void setDistributionNo(String distributionNo) {
		this.distributionNo = distributionNo;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getSampleLocal() {
		return sampleLocal;
	}
	public void setSampleLocal(String sampleLocal) {
		this.sampleLocal = sampleLocal;
	}
	public String getAdministrativeLevel() {
		return administrativeLevel;
	}
	public void setAdministrativeLevel(String administrativeLevel) {
		this.administrativeLevel = administrativeLevel;
	}
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getCreateByUser() {
		return createByUser;
	}
	public void setCreateByUser(String createByUser) {
		this.createByUser = createByUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public BusinessUnit transformEntity(BusinessUnit orgBusUnit){
		if(orgBusUnit==null){orgBusUnit = new BusinessUnit();}
		orgBusUnit.setName(this.name);
		orgBusUnit.setAddress(this.address);
		orgBusUnit.setContact(this.contact);
		orgBusUnit.setEmail(this.email);
		orgBusUnit.setRegion(this.region);
		orgBusUnit.setSampleLocal(this.sampleLocal);
		orgBusUnit.setAdministrativeLevel(this.administrativeLevel);
		orgBusUnit.setPersonInCharge(this.personInCharge);
		orgBusUnit.setPostalCode(this.postalCode);
		orgBusUnit.setTelephone(this.tel);
		orgBusUnit.setFax(this.fax);
		orgBusUnit.setNote(this.note);
		if(orgBusUnit.getBusinessUnitToLims()==null){orgBusUnit.setBusinessUnitToLims(new BusinessUnitToLims());}
		orgBusUnit.getBusinessUnitToLims().setEdition(this.edition);
		orgBusUnit.getBusinessUnitToLims().setCreateByUser(this.createByUser);
		orgBusUnit.getBusinessUnitToLims().setCreateTime(new Date());
		return orgBusUnit;
	}
}
