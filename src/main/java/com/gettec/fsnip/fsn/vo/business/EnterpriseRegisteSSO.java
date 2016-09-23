package com.gettec.fsnip.fsn.vo.business;

import java.util.List;

import com.gettec.fsnip.fsn.vo.common.OrganizationVO;
import com.gettec.fsnip.fsn.vo.common.UserVO;

/**
 * 用于企业注册审核
 * @author Administrator XianZhen Long
 */
public class EnterpriseRegisteSSO {
	
	private List<OrganizationVO> organizations;
	private UserVO user;
	public List<OrganizationVO> getOrganizations() {
		return organizations;
	}
	public void setOrganizations(List<OrganizationVO> organizations) {
		this.organizations = organizations;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
}
