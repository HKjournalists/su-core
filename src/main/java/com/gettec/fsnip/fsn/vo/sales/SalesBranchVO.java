package com.gettec.fsnip.fsn.vo.sales;

import com.gettec.fsnip.fsn.model.sales.SalesBranch;


public class SalesBranchVO {

	private Long id;
	
	private String guid;  //系统全局id
	
	private String name;
	
	private String contact; //联系人
	
	private String telephone;
	
	private String province; //省
	
	private String city; //市
	
	private String counties; //县
	
	private String street; //街道地址
	
	private String longitude; //经度
	
	private String latitude; //纬度
	
	private String address;	//地址，使用省、市、县、已经街道地址拼接成的整体地址

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounties() {
		return counties;
	}

	public void setCounties(String counties) {
		this.counties = counties;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public SalesBranchVO(){};
	
	public SalesBranchVO(SalesBranch branch) {
		if(branch == null) {
			return ;
		}
		this.id = branch.getId();
		this.guid = branch.getGuid();
		this.name = branch.getName();
		this.city = branch.getCity();
		this.contact = branch.getContact();
		this.counties = branch.getCounties();
		this.province = branch.getProvince();
		this.street = branch.getStreet();
		this.telephone = branch.getTelephone();
		this.latitude = branch.getLatitude();
		this.longitude = branch.getLongitude();
		this.address = "";
		if(this.province != null) this.address = this.province;
		if(this.city != null) this.address += this.city;
		if(this.counties != null) this.address += this.counties;
		if(this.street != null) this.address += this.street;
	}
	
	public SalesBranch toEntity(SalesBranch salesBranch){
		if(salesBranch == null) {
			salesBranch = new SalesBranch();
		}
		salesBranch.setName(this.name);
		salesBranch.setCity(this.city);
		salesBranch.setContact(this.contact);
		salesBranch.setCounties(this.counties);
		salesBranch.setProvince(this.province);
		salesBranch.setStreet(this.street);
		salesBranch.setLatitude(this.latitude);
		salesBranch.setLongitude(this.longitude);
		salesBranch.setTelephone(this.telephone);
		return salesBranch;
	}
	
}
