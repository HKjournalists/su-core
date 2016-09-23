package com.gettec.fsnip.fsn.vo.privilege;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.vo.BaseVO;


public class UserVO extends BaseVO{
	private Long id;
	private String userName;
	private String realUserName;
	private String password;
	private String organizationId;
	private String organizationName;
	private Date registerDate;
	private Date lastLoginDate;
	private Set<RoleVO> userRole;
	private List<Long> selectedRoleIds;
	private String displayPermissionNames;
	
	public UserVO(){
		
	}
	public UserVO(Long id, String userName, String realUserName) {
		super();
		this.id = id;
		this.userName = userName;
		this.realUserName = realUserName;
	}

	public UserVO(Long id) {
		// TODO Auto-generated constructor stub
		super();
		this.id = id;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealUserName() {
		return realUserName;
	}

	public void setRealUserName(String realUserName) {
		this.realUserName = realUserName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Set<RoleVO> getUserRole() {
		return userRole;
	}

	public void setUserRole(Set<RoleVO> userRole) {
		this.userRole = userRole;
	}

	public List<Long> getSelectedRoleIds() {
		return selectedRoleIds;
	}

	public void setSelectedRoleIds(List<Long> selectedRoleIds) {
		this.selectedRoleIds = selectedRoleIds;
	}

	public String getDisplayPermissionNames() {
		return displayPermissionNames;
	}

	public void setDisplayPermissionNames(String displayPermissionNames) {
		this.displayPermissionNames = displayPermissionNames;
	}
	public UserVO(Long id, String userName, String realUserName,
			String password, String organizationName, Date registerDate,
			Date lastLoginDate, Set<RoleVO> userRole,
			List<Long> selectedRoleIds, String displayPermissionNames) {
		super();
		this.id = id;
		this.userName = userName;
		this.realUserName = realUserName;
		this.password = password;
		this.organizationName = organizationName;
		this.registerDate = registerDate;
		this.lastLoginDate = lastLoginDate;
		this.userRole = userRole;
		this.selectedRoleIds = selectedRoleIds;
		this.displayPermissionNames = displayPermissionNames;
	}
	public UserVO(Long id, String realUserName) {
		super();
		this.id = id;
		this.realUserName = realUserName;
	}
	public UserVO(String userName) {
		super();
		this.userName = userName;
	}
}