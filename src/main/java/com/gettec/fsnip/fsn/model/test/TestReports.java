package com.gettec.fsnip.fsn.model.test;

import com.gettec.fsnip.fsn.model.common.Model;

public class TestReports extends Model{
	/**
	 * 检测报告
	 */
	private static final long serialVersionUID = -1901699383068554359L;
	private TestRptJson selfRpt;
	private TestRptJson censorRpt;
	private TestRptJson sampleRpt;
	boolean hasSelf = false;
	boolean hasCensor = false;
	boolean hasSample = false;
	boolean hasRpt = false;
	public TestRptJson getSelfRpt() {
		return selfRpt;
	}
	public void setSelfRpt(TestRptJson self) {
		this.selfRpt = self;
	}
	public TestRptJson getCensorRpt() {
		return censorRpt;
	}
	public void setCensorRpt(TestRptJson censor) {
		this.censorRpt = censor;
	}
	public TestRptJson getSampleRpt() {
		return sampleRpt;
	}
	public void setSampleRpt(TestRptJson sample) {
		this.sampleRpt = sample;
	}
	public boolean isHasSelf() {
		return hasSelf;
	}
	public void setHasSelf(boolean hasSelf) {
		this.hasSelf = hasSelf;
	}
	public boolean isHasCensor() {
		return hasCensor;
	}
	public void setHasCensor(boolean hasCensor) {
		this.hasCensor = hasCensor;
	}
	public boolean isHasSample() {
		return hasSample;
	}
	public void setHasSample(boolean hasSample) {
		this.hasSample = hasSample;
	}
	public boolean isHasRpt() {
		return hasRpt;
	}
	public void setHasRpt(boolean hasRpt) {
		this.hasRpt = hasRpt;
	}
	
	@Override
	public String toString() {
		return "TestReports [selfRpt=" + selfRpt + ", censorRpt=" + censorRpt
				+ ", sampleRpt=" + sampleRpt + ", hasSelf=" + hasSelf
				+ ", hasCensor=" + hasCensor + ", hasSample=" + hasSample
				+ ", hasRpt=" + hasRpt + "]";
	}
	public TestReports() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TestReports(TestRptJson self, TestRptJson censor, TestRptJson sample) {
		super();
		this.selfRpt = self;
		this.censorRpt = censor;
		this.sampleRpt = sample;
		this.hasSelf = (self!=null);
		this.hasCensor = (censor!=null);
		this.hasSample = (sample!=null);
		this.hasRpt = (this.hasCensor||this.hasSample||this.hasSelf);
	}
	
	
	

}
