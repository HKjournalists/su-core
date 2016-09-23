package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * AddressInfo Entity<br>
 * @author Administrator
 */
@Entity(name="T_META_ADDRESS")
public class AddressInfo extends Model {
	private static final long serialVersionUID = 4451990499170794321L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
	
	@Column(name="PROVINCE", length=50)
	private String province;
	
	@Column(name="CITY", length=50)
	private String city;
	
	@Column(name="AREA", length=50)
	private String area;
	
	@Column(name="OTHER", length=500)
	private String other;
	
	private Long organization;
	
	public AddressInfo() {
		super();
	}
	
	public AddressInfo(String province, String city, String area, String other) {
		super();
		this.province = province;
		this.city = city;
		this.area = area;
		this.other = other;
	}
	
	public AddressInfo(String province, String city, String area, String other, Long organization) {
		super();
		this.province = province;
		this.city = city;
		this.area = area;
		this.other = other;
		this.organization = organization;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
}
