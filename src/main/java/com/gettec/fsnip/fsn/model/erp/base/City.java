package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * City Entity<br>
 * @author Administrator
 */
@Entity(name="HAT_CITY")
public class City implements Serializable {
	private static final long serialVersionUID = -3747631060193010360L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
	
	@Column(name="CITYID", length=6)
	private String cityId;
	
	@Column(name="CITY", length=40)
	private String city;
	
	@Column(name="provinceId", length=40)
	private String provinceId;
	
	public City() {
		super();
	}
	
	public City(String cityId, String city, String provinceId) {
		super();
		this.cityId = cityId;
		this.city = city;
		this.provinceId = provinceId;
	}
	
	public City(int id, String cityId, String city, String provinceId) {
		super();
		this.id = id;
		this.cityId = cityId;
		this.city = city;
		this.provinceId = provinceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
}
