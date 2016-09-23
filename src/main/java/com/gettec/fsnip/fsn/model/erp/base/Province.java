package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Province Entity<br>
 * @author Administrator
 */
@Entity(name="HAT_PROVINCE")
public class Province implements Serializable {
	private static final long serialVersionUID = -3747631060193010360L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
	
	@Column(name="PROVINCEID", length=6)
	private String provinceId;
	
	@Column(name="PROVINCE", length=40)
	private String province;
	
	@Column(name="short_prov", length=4)
	private String shortProv; //省对应的简称
	
	public Province() {
		super();
	}
	
	public Province(String provinceId, String province) {
		super();
		this.provinceId = provinceId;
		this.province = province;
	}
	
	public Province(int id, String provinceId, String province,String shortProv) {
		super();
		this.id = id;
		this.provinceId = provinceId;
		this.province = province;
		this.setShortProv(shortProv);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

    public String getShortProv() {
        return shortProv;
    }

    public void setShortProv(String shortProv) {
        this.shortProv = shortProv;
    }
}
