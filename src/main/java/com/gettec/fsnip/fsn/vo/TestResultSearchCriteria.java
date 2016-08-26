package com.gettec.fsnip.fsn.vo;

import com.gettec.fsnip.fsn.util.RoleEnum;

public class TestResultSearchCriteria{

	private RoleEnum testerType;
	private int roleId;
	private Long testerId;
	private Long productInstanceId;
	private Long businessUnitId;
	private boolean publishFlag;
	private int page;
	private int pageSize;
	private String configure;
	
	public String getConfigure() {
		return configure;
	}
	public void setConfigure(String configure) {
		this.configure = configure;
	}
	public boolean isPublishFlag() {
		return publishFlag;
	}
	public void setPublishFlag(boolean publishFlag) {
		this.publishFlag = publishFlag;
	}
	public RoleEnum getTesterType() {
		if(testerType == null && this.roleId != 0){
			return RoleEnum.getRole(this.roleId);
		}
		return testerType;
	}
	public void setTesterType(RoleEnum testerType) {
		this.testerType = testerType;
	}
	public Long getTesterId() {
		return testerId;
	}
	public void setTesterId(Long testerId) {
		this.testerId = testerId;
	}
	public Long getProductInstanceId() {
		return productInstanceId;
	}
	public void setProductInstanceId(Long productInstanceId) {
		this.productInstanceId = productInstanceId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public Long getBusinessUnitId() {
		return businessUnitId;
	}
	public void setBusinessUnitId(Long businessUnitId) {
		this.businessUnitId = businessUnitId;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}