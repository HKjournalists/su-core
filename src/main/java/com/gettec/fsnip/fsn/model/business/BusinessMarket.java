package com.gettec.fsnip.fsn.model.business;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.gettec.fsnip.fsn.model.base.District;
import com.gettec.fsnip.fsn.model.base.Office;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 交易市场
 * @author YuanBin Hao
 */
@Entity(name = "business_market")
public class BusinessMarket extends Model{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@OneToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH}, optional=false, fetch = FetchType.EAGER)
	@JoinColumn(name="business_id", nullable = false)
	private BusinessUnit business;//企业信息
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= SysArea.class)
	@JoinColumn(name="administrative", nullable = true)
	private SysArea administrative;    //行政区划
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= Office.class)
	@JoinColumn(name="office", nullable = true)
	private Office office;    //管辖食药监机关

	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= District.class)
	@JoinColumn(name="placeType", nullable = true)
	private District placeType;    //场所类型
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= District.class)
	@JoinColumn(name="areaType", nullable = true)
	private District areaType;    //地域类型
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= District.class)
	@JoinColumn(name="territoryType", nullable = true)
	private District territoryType;    //地区类型
	
	@Column(name = "prepositionQS")
	private String prepositionQS;//前置许可证号
	
	@Column(name = "buildName")
	private String buildName;//开办方名称
	
	@Column(name = "note")
	private String note;//备注
	
	@Column(name = "publish_flag")
	private boolean publishFlag;//是否已经上传到fdams

	public boolean isPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(boolean publishFlag) {
		this.publishFlag = publishFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BusinessUnit getBusiness() {
		return business;
	}

	public void setBusiness(BusinessUnit business) {
		this.business = business;
	}

	

	public SysArea getAdministrative() {
		return administrative;
	}

	public void setAdministrative(SysArea administrative) {
		this.administrative = administrative;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public District getPlaceType() {
		return placeType;
	}

	public void setPlaceType(District placeType) {
		this.placeType = placeType;
	}

	public District getAreaType() {
		return areaType;
	}

	public void setAreaType(District areaType) {
		this.areaType = areaType;
	}

	public District getTerritoryType() {
		return territoryType;
	}

	public void setTerritoryType(District territoryType) {
		this.territoryType = territoryType;
	}

	public String getPrepositionQS() {
		return prepositionQS;
	}

	public void setPrepositionQS(String prepositionQS) {
		this.prepositionQS = prepositionQS;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public BusinessMarket(){
		
	}
	
	public BusinessMarket(Long id,BusinessUnit business,SysArea administrative,Office office,District placeType,
			District areaType,District territoryType,String prepositionQS,String buildName,String note,boolean publishFlag) {
		super();
		this.id=id;
		this.business=business;
		this.administrative=administrative;
		this.office=office;
		this.placeType=placeType;
		this.areaType=areaType;
		this.territoryType=territoryType;
		this.prepositionQS=prepositionQS;
		this.buildName=buildName;
		this.note=note;
		this.publishFlag=publishFlag;
	}
}
