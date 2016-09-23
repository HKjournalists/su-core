package com.lhfs.fsn.vo.product;

/**
 * 提供给监管系统VO
 * @author XinTang
 */
public class ProductVOWda {
	private Long id;
	private String name;
	private String barcode;
	private String format;
	private String producer;
	private String expiration;
	private String eMail;
	private String telephone;
	private Boolean allBatchExpire;// 所有的批次是否过期||true 是 false 否
	
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
		this.name = name;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public Boolean getAllBatchExpire() {
		return allBatchExpire;
	}
	public void setAllBatchExpire(Boolean allBatchExpire) {
		this.allBatchExpire = allBatchExpire;
	}
	
}