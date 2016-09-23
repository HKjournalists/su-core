package com.lhfs.fsn.vo;

/**
 * 提供给监管系统VO
 * @author XinTang
 */
public class BusinessUnitVO {
	private Long id ;
	private String name;
	private String address;
	private String contact;
	private String contact_phone;
	private String personInCharge;
	private String email;
	private String fax;
	private String postalCode;
	private String license_no;
	private String distribution_no;
	private String administrativeLevel;
	private String region;
	private String sampleLocal;
	
	
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
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContact_phone() {
		return contact_phone;
	}
	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getLicense_no() {
		return license_no;
	}
	public void setLicense_no(String license_no) {
		this.license_no = license_no;
	}
	public String getDistribution_no() {
		return distribution_no;
	}
	public void setDistribution_no(String distribution_no) {
		this.distribution_no = distribution_no;
	}
	public String getAdministrativeLevel() {
		return administrativeLevel;
	}
	public void setAdministrativeLevel(String administrativeLevel) {
		this.administrativeLevel = administrativeLevel;
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
	
}