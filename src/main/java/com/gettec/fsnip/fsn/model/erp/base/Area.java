package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Area Entity<br>
 * @author Administrator
 */
@Entity(name="HAT_AREA")
public class Area implements Serializable {
	private static final long serialVersionUID = -3747631060193010360L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
	
	@Column(name="AREAID", length=6)
	private String areaId;
	
	@Column(name="AREA", length=40)
	private String area;

	@Column(name="cityId", length=40)
	private String cityId;
	
	public Area() {
		super();
	}
	
	public Area(String areaId, String area, String cityId) {
		super();
		this.areaId = areaId;
		this.area = area;
		this.cityId = cityId;
	}
	
	public Area(int id, String areaId, String area, String cityId) {
		super();
		this.id = id;
		this.areaId = areaId;
		this.area = area;
		this.cityId = cityId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
}
