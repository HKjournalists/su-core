package com.gettec.fsnip.fsn.vo.receive;

/**
 * 用于接收泊银等其他外部系统的检测项目和结论
 * @author ZhangHui 2015/4/23
 */
public class TestPropertyVO {
	/**
	 * 检测项目名称
	 */
	private String name;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * 技术指标
	 */
	private String indicant;
	/**
	 * 检测结果
	 */
	private String result;
	/**
	 * 单项评价
	 */
	private String conclusion;
	/**
	 * 执行标准
	 */
	private String standard;
	
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
	public String getIndicant() {
		return indicant;
	}
	public void setIndicant(String indicant) {
		this.indicant = indicant;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getConclusion() {
		return conclusion;
	}
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
}
