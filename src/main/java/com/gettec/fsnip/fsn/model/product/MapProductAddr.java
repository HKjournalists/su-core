package com.gettec.fsnip.fsn.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gettec.fsnip.fsn.model.common.Model;
@Entity(name = "t_map_product_addr")
public class MapProductAddr extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@Column(name = "lat")
	private float lat;
	@Column(name = "lng")
	private float lng;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "map_product_id")
	private MapProduct mapProduct;
	@Column(name = "[describe]")
	private String describe;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	@org.codehaus.jackson.annotate.JsonIgnore
	public MapProduct getMapProduct() {
		return mapProduct;
	}
	public void setMapProduct(MapProduct mapProduct) {
		this.mapProduct = mapProduct;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
}
