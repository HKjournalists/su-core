package com.gettec.fsnip.fsn.model.product;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.gettec.fsnip.fsn.model.common.Model;
@Entity(name = "t_map_product")
public class MapProduct extends Model {
private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@Column(name = "product_id")
	private Long productId;
	@Column(name = "organization")
	private long organization;
	@Column(name = "lat")
	private float lat;
	@Column(name = "lng")
	private float lng;
	@Column(name = "product_name")
	private String productName;
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER,mappedBy="mapProduct")
	private List<MapProductAddr> mapProductAddrList;
	public List<MapProductAddr> getMapProductAddrList() {
		return mapProductAddrList;
	}
	public void setMapProductAddrList(List<MapProductAddr> mapProductAddrList) {
		this.mapProductAddrList = mapProductAddrList;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public long getOrganization() {
		return organization;
	}
	public void setOrganization(long organization) {
		this.organization = organization;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
}
