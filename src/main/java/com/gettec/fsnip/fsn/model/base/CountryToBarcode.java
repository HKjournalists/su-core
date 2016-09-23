package com.gettec.fsnip.fsn.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 基础表（国家与条码的关系）
 * @author longxianzhen 2015/05/22
 */
@Entity(name = "country_to_barcode")
public class CountryToBarcode extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="country_id", nullable=false)
	private Country country;  // 国家
	
	@Transient
	private Long countryId;
	
	@Column(name = "bar3Int_start")
	private int bar3IntStart;
	
	@Column(name = "bar3Int_end")
	private int bar3IntEnd;
	
	@Column(name = "bar3_start")
	private String bar3Start;
	
	@Column(name = "bar3_end")
	private String bar3End;
	
	@Column(name = "createDate")
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public int getBar3IntStart() {
		return bar3IntStart;
	}

	public void setBar3IntStart(int bar3IntStart) {
		this.bar3IntStart = bar3IntStart;
	}

	public int getBar3IntEnd() {
		return bar3IntEnd;
	}

	public void setBar3IntEnd(int bar3IntEnd) {
		this.bar3IntEnd = bar3IntEnd;
	}

	public String getBar3Start() {
		return bar3Start;
	}

	public void setBar3Start(String bar3Start) {
		this.bar3Start = bar3Start;
	}

	public String getBar3End() {
		return bar3End;
	}

	public void setBar3End(String bar3End) {
		this.bar3End = bar3End;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
