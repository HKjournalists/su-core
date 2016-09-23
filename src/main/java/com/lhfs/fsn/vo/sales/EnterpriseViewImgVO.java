package com.lhfs.fsn.vo.sales;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业封面图片vo 企业App使用
 * @author tangxin 2015-04-28
 */
public class EnterpriseViewImgVO {
	
	private Long id;
	private Long organization;
	private String enterpriseName;
	private String logo;
	private String enterpriseImgUrl;
	private String description;
	
	private List<String> enterpriseImgUrls= new ArrayList<String>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrganization() {
		return organization;
	}
	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getEnterpriseImgUrl() {
		return enterpriseImgUrl;
	}
	public void setEnterpriseImgUrl(String enterpriseImgUrl) {
		this.enterpriseImgUrl = enterpriseImgUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<String> getEnterpriseImgUrls() {
		return enterpriseImgUrls;
	}
	public void setEnterpriseImgUrls(List<String> enterpriseImgUrls) {
		this.enterpriseImgUrls = enterpriseImgUrls;
	}
	
	public void addEnterseImgUrls(String url)
	{
		this.enterpriseImgUrls.add(url);
	}
	
	public EnterpriseViewImgVO(){}
	
	public EnterpriseViewImgVO(Long id, Long organization, 
			String name, String logo, String enterImg, String description){
		this.id = id;
		this.organization = organization;
		this.enterpriseName = name;
		this.enterpriseImgUrl = enterImg;
		this.logo = logo;
		this.description = description;
	}
}
