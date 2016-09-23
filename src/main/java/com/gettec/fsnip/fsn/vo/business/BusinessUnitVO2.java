package com.gettec.fsnip.fsn.vo.business;

public class BusinessUnitVO2 {
	private Long id;
	private String name;
	private Long countOfReport; // 企业报告总数
	private String logo;  // 企业logo
	
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
	
	public Long getCountOfReport() {
		return countOfReport;
	}
	public void setCountOfReport(Long countOfReport) {
		this.countOfReport = countOfReport;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public BusinessUnitVO2(){}
	
	public BusinessUnitVO2(Long id, String name){
		this.id = id;
		this.name = name;
	}
	
	public BusinessUnitVO2(Long id, String name, Long countOfReport){
		this.id = id;
		this.name = name;
		this.countOfReport = countOfReport;
	}
}
