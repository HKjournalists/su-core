package com.lhfs.fsn.vo;

import java.util.List;

/**
 * 提供给监管系统VO
 * @author XinTang
 */
public class TestResultsVO {
	private boolean batch;
	private String edition;
	private Long organizationID;
	private String serviceOrder;
	private String backURL;
	private String testType;
	private String orgName;
	private String publisher;
	private List<SampleVO> samples;
	public boolean isBatch() {
		return batch;
	}
	public void setBatch(boolean batch) {
		this.batch = batch;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public Long getOrganizationID() {
		return organizationID;
	}
	public void setOrganizationID(Long organizationID) {
		this.organizationID = organizationID;
	}
	public String getServiceOrder() {
		return serviceOrder;
	}
	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}
	public String getBackURL() {
		return backURL;
	}
	public void setBackURL(String backURL) {
		this.backURL = backURL;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public List<SampleVO> getSamples() {
		return samples;
	}
	public void setSamples(List<SampleVO> samples) {
		this.samples = samples;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
}