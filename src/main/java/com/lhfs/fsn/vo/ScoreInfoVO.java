package com.lhfs.fsn.vo;

public class ScoreInfoVO {
	private String self_num;	//上传自检批次数量
	private String active_degree;	//报告操作活跃度
	private String self_qualified_rate;	//自检报告合格率
	private String inspection_frequency;	//送检频次(1：月、2：季度、3：半年)
	private String compare_qualified_rate;	//结果比对合格率
	private String coverage_rate;	//检测指标覆盖率
	public String getSelf_num() {
		return self_num;
	}
	public void setSelf_num(String self_num) {
		this.self_num = self_num;
	}
	public String getActive_degree() {
		return active_degree;
	}
	public void setActive_degree(String active_degree) {
		this.active_degree = active_degree;
	}
	public String getSelf_qualified_rate() {
		return self_qualified_rate;
	}
	public void setSelf_qualified_rate(String self_qualified_rate) {
		this.self_qualified_rate = self_qualified_rate;
	}
	public String getInspection_frequency() {
		return inspection_frequency;
	}
	public void setInspection_frequency(String inspection_frequency) {
		this.inspection_frequency = inspection_frequency;
	}
	public String getCompare_qualified_rate() {
		return compare_qualified_rate;
	}
	public void setCompare_qualified_rate(String compare_qualified_rate) {
		this.compare_qualified_rate = compare_qualified_rate;
	}
	public String getCoverage_rate() {
		return coverage_rate;
	}
	public void setCoverage_rate(String coverage_rate) {
		this.coverage_rate = coverage_rate;
	}
}
