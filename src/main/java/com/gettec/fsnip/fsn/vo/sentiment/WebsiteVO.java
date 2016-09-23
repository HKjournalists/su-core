package com.gettec.fsnip.fsn.vo.sentiment;

import java.io.Serializable;

import com.gettec.fsnip.sso.client.util.AccessUtils;

//网站列表VO
public class WebsiteVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Website_ID;  
	private String Website_Name;
	private String Website_No;
	private String Domain_Code;
	private String Website_Main_Page_URL; 
	
	private String OrgName; //组织机构名称
	
	
	
	
	
	public String getWebsite_Name() {
		return Website_Name;
	}
	
	
	public void setWebsite_Name(String website_Name) {
		Website_Name = website_Name;
	}

	
	public String getWebsite_ID() {
		return Website_ID;
	}
	public void setWebsite_ID(String website_ID) {
		Website_ID = website_ID;
	}
	public String getWebsite_No() {
		return Website_No;
	}
	public void setWebsite_No(String website_No) {
		Website_No = website_No;
	}
	public String getDomain_Code() {
		return Domain_Code;
	}
	public void setDomain_Code(String domain_Code) {
		Domain_Code = domain_Code;
	}
	public String getWebsite_Main_Page_URL() {
		return Website_Main_Page_URL;
	}
	public void setWebsite_Main_Page_URL(String website_Main_Page_URL) {
		Website_Main_Page_URL = website_Main_Page_URL;
	}
	public String getOrgName() {
		this.OrgName = AccessUtils.getUserOrgName().toString();
		return OrgName;
	}
	public void setOrgName(String orgName) {
		OrgName = orgName;
	}
}
