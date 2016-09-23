package com.lhfs.fsn.vo.product;


/**
 * 大众门户VO查询某个产品详情接口时,返回同类产品
 * @author ZhaWanNeng
 */
public class ProductSortVo {
	private Long id;
	private String name;
	private String imgUrl;
	private String barcode;
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public ProductSortVo(Long id, String name, String imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
	}
	
	public ProductSortVo() {
	}
	
}
