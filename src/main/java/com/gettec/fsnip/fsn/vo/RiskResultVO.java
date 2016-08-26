package com.gettec.fsnip.fsn.vo;

import java.util.Date;

/**
 * 用于计算风险指数时需求的检测报告信息
 * @author ZhaWanNeng
 *
 */
public class RiskResultVO {
    private	Long id;
    private  Date testDate;
    private  String standard;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getTestDate() {
		return testDate;
	}
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}

   
}
