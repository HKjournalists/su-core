package com.gettec.fsnip.fsn.vo.privilege;

import java.util.Date;

import com.gettec.fsnip.fsn.vo.BaseVO;


public class PermissionVO extends BaseVO{
	private Long id;
	private String permissionName;
	private String displayName; // permission name in locale
	private String permissionUrl;
	private Long parentId;
	private String description;
	private Date createdDate;
	private String apiFlag;
	private String parentName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPermissionUrl() {
		return permissionUrl;
	}
	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getApiFlag() {
		return apiFlag;
	}
	public void setApiFlag(String apiFlag) {
		this.apiFlag = apiFlag;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public PermissionVO(){
		super();
	}
	public PermissionVO(Long id, String permissionName, String displayName,
			String permissionUrl, Long parentId, String description,
			Date createdDate, String apiFlag, String parentName) {
		super();
		this.id = id;
		this.permissionName = permissionName;
		this.displayName = displayName;
		this.permissionUrl = permissionUrl;
		this.parentId = parentId;
		this.description = description;
		this.createdDate = createdDate;
		this.apiFlag = apiFlag;
		this.parentName = parentName;
	}
	
}
