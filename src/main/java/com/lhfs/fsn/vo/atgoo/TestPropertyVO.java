package com.lhfs.fsn.vo.atgoo;

import java.io.Serializable;

/**
 * 检测项目VO 提供给爱特够系统
 * @author longxianzhen 2015/7/30
 *
 */
public class TestPropertyVO implements Serializable{

	 Long id;

	 String name;//检测项目名称
	
	 String unit; //单位
	
	 String techIndicator;  //技术指标

	 String result;  //结论

	 String assessment; //单项评价
	
	 String standard;  //检测标准

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTechIndicator() {
		return techIndicator;
	}

	public void setTechIndicator(String techIndicator) {
		this.techIndicator = techIndicator;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	
	
}
