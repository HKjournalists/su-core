package com.gettec.fsnip.fsn.model.resort;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

@Entity(name = "resorts")
public class Resorts extends Model {
	
	private static final long serialVersionUID = 5422053820803926781L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id; // 序号

	@Column(name = "name")
	private String name;// 景区名称

	@Column(name = "level")
	private String level;// 等级

	@Column(name = "resort_info")
	private String resortInfo;// 景区简介

	@Column(name = "resort_price")
	private String resortPrice;// 门票价格

	@Column(name = "reserve_telephone")
	private String reserveTelephone;// 订票电话

	@Column(name = "resort_type")
	private String resortType;// 景区类型

	@Column(name = "resort_address")
	private String resortAddress;// 景区地址

	@Column(name = "ranks")
	private String rank;// 经纬度

	@Column(name = "place_name")
	private String placeName;// 覆盖物名称

	@Column(name = "longitude")
	private Float longitude; // 经度

	@Column(name = "latitude")
	private Float latitude; // 纬度

	@Column(name = "organization")
	private long currentUserOrganization; // 当组织机构ID

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "resorts_logo_to_source", joinColumns = { @JoinColumn(name = "resorts_id") }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID") })
	private Set<Resource> logoAttachments = new HashSet<Resource>();// 景区logo图片

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "resorts_info_to_source", joinColumns = { @JoinColumn(name = "resorts_id") }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID") })
	private Set<Resource> infoAttachments = new HashSet<Resource>();// 景区简介图片

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getResortAddress() {
		return resortAddress;
	}

	public void setResortAddress(String resortAddress) {
		this.resortAddress = resortAddress;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level == null ? null : level.trim();
	}

	public String getResortInfo() {
		return resortInfo;
	}

	public void setResortInfo(String resortInfo) {
		this.resortInfo = resortInfo == null ? null : resortInfo.trim();
	}

	public String getResortPrice() {
		return resortPrice;
	}

	public void setResortPrice(String resortPrice) {
		this.resortPrice = resortPrice == null ? null : resortPrice.trim();
	}

	public String getReserveTelephone() {
		return reserveTelephone;
	}

	public void setReserveTelephone(String reserveTelephone) {
		this.reserveTelephone = reserveTelephone == null ? null
				: reserveTelephone.trim();
	}

	public String getResortType() {
		return resortType;
	}

	public void setResortType(String resortType) {
		this.resortType = resortType == null ? null : resortType.trim();
	}

	public Set<Resource> getLogoAttachments() {
		return logoAttachments;
	}

	public void setLogoAttachments(Set<Resource> logoAttachments) {
		this.logoAttachments = logoAttachments;
	}

	public Set<Resource> getInfoAttachments() {
		return infoAttachments;
	}

	public void setInfoAttachments(Set<Resource> infoAttachments) {
		this.infoAttachments = infoAttachments;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public long getCurrentUserOrganization() {
		return currentUserOrganization;
	}

	public void setCurrentUserOrganization(long currentUserOrganization) {
		this.currentUserOrganization = currentUserOrganization;
	}

}