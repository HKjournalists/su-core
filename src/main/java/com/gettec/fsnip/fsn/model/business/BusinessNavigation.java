package com.gettec.fsnip.fsn.model.business;

import javax.persistence.*;


import com.gettec.fsnip.fsn.model.common.Model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 * 企业快速导航
 */
@Entity(name = "business_navigation")
public class BusinessNavigation extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7604701838862280805L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@Column(name = "big_option")
	private String bigOption;
	
	@Column(name = "small_option")
	private String smallOption;
	
	@Column(name = "navigation_url")
	private String navigationURL;
	
	@Column(name = "business_id")
	private long businessID;
	
	@Column(name = "organization")
	private Long organization;  // 组织机构ID

	@Column(name = "addressId")
	private int addressId;

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBigOption() {
		return bigOption;
	}

	public void setBigOption(String bigOption) {
		this.bigOption = bigOption;
	}

	public String getSmallOption() {
		return smallOption;
	}

	public void setSmallOption(String smallOption) {
		this.smallOption = smallOption;
	}

	public String getNavigationURL() {
		return navigationURL;
	}

	public void setNavigationURL(String navigationURL) {
		this.navigationURL = navigationURL;
	}

	public long getBusinessID() {
		return businessID;
	}

	public void setBusinessID(long businessID) {
		this.businessID = businessID;
	}
	
	
	
	
	
	
	
}
