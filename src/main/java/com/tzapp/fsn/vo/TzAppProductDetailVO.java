package com.tzapp.fsn.vo;

public class TzAppProductDetailVO {

	private Long proId;//产品ID
	private String name;//产品名称
	private String barcode;//条形码
	private String batch;//批次
	private String url;//产品图片路径
	private Long count;//数量
	private double price;//单价
	private Long id;//产品详情ID
	private String proFullUrl;//产品原图 
	
	public Long getProId() {
		return proId;
	}
	public void setProId(Long proId) {
		this.proId = proId;
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
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProFullUrl() {
		return proFullUrl;
	}
	public void setProFullUrl(String proFullUrl) {
		this.proFullUrl = proFullUrl;
	}
	
}
