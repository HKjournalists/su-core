package com.gettec.fsnip.fsn.vo.privilege;

import com.gettec.fsnip.fsn.vo.BaseVO;


public class OrganizationVO extends BaseVO{
	private String organizationName;
	private String organizationAddress;
	private String comments;
	
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getOrganizationAddress() {
		return organizationAddress;
	}
	public void setOrganizationAddress(String organizationAddress) {
		this.organizationAddress = organizationAddress;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
