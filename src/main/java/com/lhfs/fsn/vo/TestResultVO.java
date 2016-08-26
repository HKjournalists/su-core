package com.lhfs.fsn.vo;

import java.util.List;

/**
 * 提供给监管系统VO
 * @author XinTang
 */
public class TestResultVO {
	private String approveBy;
	private String auditBy;
	private String comment;
	private String equipment;
	private String keyTester;
	private boolean pass;
	private String result;
	private String sampleQuantity;
	private String samplingDate;
	private String samplingLocation;
	private String standard;
	private String testDate;
	private List<TestPropertieVO> testProperties;
	public String getApproveBy() {
		return approveBy;
	}
	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}
	public String getAuditBy() {
		return auditBy;
	}
	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getKeyTester() {
		return keyTester;
	}
	public void setKeyTester(String keyTester) {
		this.keyTester = keyTester;
	}
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSampleQuantity() {
		return sampleQuantity;
	}
	public void setSampleQuantity(String sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}
	public String getSamplingDate() {
		return samplingDate;
	}
	public void setSamplingDate(String samplingDate) {
		this.samplingDate = samplingDate;
	}
	public String getSamplingLocation() {
		return samplingLocation;
	}
	public void setSamplingLocation(String samplingLocation) {
		this.samplingLocation = samplingLocation;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public List<TestPropertieVO> getTestProperties() {
		return testProperties;
	}
	public void setTestProperties(List<TestPropertieVO> testProperties) {
		this.testProperties = testProperties;
	}
	
}