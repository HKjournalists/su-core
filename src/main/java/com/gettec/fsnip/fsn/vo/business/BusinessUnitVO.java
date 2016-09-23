package com.gettec.fsnip.fsn.vo.business;

public class BusinessUnitVO {

	private Long id;
	private String name;
	private String licenseNO;
	private String circulationPermitNO;
	private String place;
	private String administrativeLevel;
	private String district;
	private String contact;
	private String legalRepresentative;
	private String tel;
	private String fax;
	private String email;
	private String mobile;
	private String zipCode;
	private String address;
	private String note;
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
	public String getLicenseNO() {
		return licenseNO;
	}
	public void setLicenseNO(String licenseNO) {
		this.licenseNO = licenseNO;
	}
	public String getCirculationPermitNO() {
		return circulationPermitNO;
	}
	public void setCirculationPermitNO(String circulationPermitNO) {
		this.circulationPermitNO = circulationPermitNO;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getAdministrativeLevel() {
		return administrativeLevel;
	}
	public void setAdministrativeLevel(String administrativeLevel) {
		this.administrativeLevel = administrativeLevel;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getLegalRepresentative() {
		return legalRepresentative;
	}
	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public BusinessUnitVO(){}
	
	public BusinessUnitVO(Long id, String name){
		this.id = id;
		this.name = name;
	}
}
