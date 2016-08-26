package com.lhfs.fsn.vo;

import java.util.ArrayList;
import java.util.List;

import com.gettec.fsnip.fsn.model.test.TestRptJson;

public class TestBusinessUnitVo {
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
	private String jgLicUrl;
	private String jgDisUrl;
	private String jgQsUrl;
	private Long productId;
	private String proName;
	private String other_name;
	private String status;
	private String pformat;
	private String pformat_pdf;
	private String pregularity;
	private String pbarcode;
	private String pqscore_censor;
	private String pqscore_sample;
	private String pimgurl;
	private String pingredient;
	private String pexpiration_date;
	private String pexpiration;
	private Long testId;
	private String fullPdfPath;
	private Long resourceId;
	private String imgName;
	private String imgLogoUrl ;
	private String imgOrgUrl ;
	private String imgTaxUrl ;
	private List<TestRptJson> testResultList  = new ArrayList<TestRptJson>(); 
	
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
	public String getJgLicUrl() {
		return jgLicUrl;
	}
	public void setJgLicUrl(String jgLicUrl) {
		this.jgLicUrl = jgLicUrl;
	}
	public String getJgDisUrl() {
		return jgDisUrl;
	}
	public void setJgDisUrl(String jgDisUrl) {
		this.jgDisUrl = jgDisUrl;
	}
	public String getJgQsUrl() {
		return jgQsUrl;
	}
	public void setJgQsUrl(String jgQsUrl) {
		this.jgQsUrl = jgQsUrl;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getOther_name() {
		return other_name;
	}
	public void setOther_name(String other_name) {
		this.other_name = other_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPformat() {
		return pformat;
	}
	public void setPformat(String pformat) {
		this.pformat = pformat;
	}
	public String getPformat_pdf() {
		return pformat_pdf;
	}
	public void setPformat_pdf(String pformat_pdf) {
		this.pformat_pdf = pformat_pdf;
	}
	public String getPregularity() {
		return pregularity;
	}
	public void setPregularity(String pregularity) {
		this.pregularity = pregularity;
	}
	public String getPbarcode() {
		return pbarcode;
	}
	public void setPbarcode(String pbarcode) {
		this.pbarcode = pbarcode;
	}
	public String getPqscore_censor() {
		return pqscore_censor;
	}
	public void setPqscore_censor(String pqscore_censor) {
		this.pqscore_censor = pqscore_censor;
	}
	public String getPqscore_sample() {
		return pqscore_sample;
	}
	public void setPqscore_sample(String pqscore_sample) {
		this.pqscore_sample = pqscore_sample;
	}
	public String getPimgurl() {
		return pimgurl;
	}
	public void setPimgurl(String pimgurl) {
		this.pimgurl = pimgurl;
	}
	public String getPingredient() {
		return pingredient;
	}
	public void setPingredient(String pingredient) {
		this.pingredient = pingredient;
	}
	public String getPexpiration_date() {
		return pexpiration_date;
	}
	public void setPexpiration_date(String pexpiration_date) {
		this.pexpiration_date = pexpiration_date;
	}
	public String getPexpiration() {
		return pexpiration;
	}
	public void setPexpiration(String pexpiration) {
		this.pexpiration = pexpiration;
	}
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	public String getFullPdfPath() {
		return fullPdfPath;
	}
	public void setFullPdfPath(String fullPdfPath) {
		this.fullPdfPath = fullPdfPath;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	public String getImgLogoUrl() {
		return imgLogoUrl;
	}
	public void setImgLogoUrl(String imgLogoUrl) {
		this.imgLogoUrl = imgLogoUrl;
	}
	public String getImgOrgUrl() {
		return imgOrgUrl;
	}
	public void setImgOrgUrl(String imgOrgUrl) {
		this.imgOrgUrl = imgOrgUrl;
	}
	public String getImgTaxUrl() {
		return imgTaxUrl;
	}
	public void setImgTaxUrl(String imgTaxUrl) {
		this.imgTaxUrl = imgTaxUrl;
	}
	public List<TestRptJson> getTestResultList() {
		return testResultList;
	}
	public void setTestResultList(List<TestRptJson> testResultList) {
		this.testResultList = testResultList;
	}
	
}
