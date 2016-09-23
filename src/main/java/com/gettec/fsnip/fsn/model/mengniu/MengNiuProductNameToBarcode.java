package com.gettec.fsnip.fsn.model.mengniu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

@Entity(name="mengniu_product_name_to_barcode")
public class MengNiuProductNameToBarcode extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue
	private Long id;
	@Column(name="product_name", nullable=true)
	private String productName;
	@Column(name="barcode", nullable=true)
	private String barcode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
}
