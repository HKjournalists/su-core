package com.lhfs.fsn.vo.sales;

import java.util.List;

import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;

/**
 * 企业信息VO 企业App使用
 * @author tangxin 2015-04-28
 */
public class EnterpriseIntroductionVO {

	private Long id;
	private Long organization;
	private String introduction;
	private String contact;
	private String telPhone;
	private String email;
	private String address;
	private String officialSite;
	private List<RecommendBuyVO> recommendInfoList = null;
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
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getTelPhone() {
		return telPhone;
	}
	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOfficialSite() {
		return officialSite;
	}
	public void setOfficialSite(String officialSite) {
		this.officialSite = officialSite;
	}
	public List<RecommendBuyVO> getRecommendInfoList() {
		return recommendInfoList;
	}
	public void setRecommendInfoList(List<RecommendBuyVO> recommendInfoList) {
		this.recommendInfoList = recommendInfoList;
	}
	
	public EnterpriseIntroductionVO(){}
	
	public EnterpriseIntroductionVO(Long id,Long orgid,String intro,String contact,String telPhone,
			String email,String addre,String webSite){
		this.id = id;
		this.organization = orgid;
		this.introduction = intro;
		this.contact = contact;
		this.telPhone = telPhone;
		this.email = email;
		this.officialSite = webSite;
		this.address = addre;
	}
	
}
