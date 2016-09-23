package com.gettec.fsnip.fsn.vo.privilege;

import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.vo.BaseVO;


public class RoleVO extends BaseVO{
	private Long id;
	private String roleName;
	private String displayName;
	private Set<PermissionVO> permission;
	private String displayPermissionNames;
	private List<Long> selectedPermissionIds;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Set<PermissionVO> getPermission() {
		return permission;
	}
	public void setPermission(Set<PermissionVO> permission) {
		this.permission = permission;
	}
	public String getDisplayPermissionNames() {
		return displayPermissionNames;
	}
	public void setDisplayPermissionNames(String displayPermissionNames) {
		this.displayPermissionNames = displayPermissionNames;
	}
	public List<Long> getSelectedPermissionIds() {
		return selectedPermissionIds;
	}
	public void setSelectedPermissionIds(List<Long> selectedPermissionIds) {
		this.selectedPermissionIds = selectedPermissionIds;
	}
	/**
	 * 
	 */
	public RoleVO() {
		super();
	}
	/**
	 * @param id
	 * @param roleName
	 */
	public RoleVO(Long id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}
	
}
