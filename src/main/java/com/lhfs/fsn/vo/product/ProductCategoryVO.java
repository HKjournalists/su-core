package com.lhfs.fsn.vo.product;

import java.util.ArrayList;
import java.util.List;

import com.gettec.fsnip.fsn.model.product.ProductCategory;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;

/**
 * 大众门户产品类型VO
 * @author Administrator
 */
public class ProductCategoryVO {
	private Long id;
	private String name;
	private String code;
	private String display;
	private String imgUrl;
	private List<ProductCategoryVO> Children = new ArrayList<ProductCategoryVO>();
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public List<ProductCategoryVO> getChildren() {
		return Children;
	}
	public void setChildren(List<ProductCategoryVO> children) {
		Children = children;
	}
	
	@Override
	public String toString() {
		return "Children [id=" + id + ", name=" + name + ", code="
				+ code + ", display=" + display + ", imgUrl=" + imgUrl + "]";
	}
	
	public ProductCategoryVO(){
		super();
	}
	
	public ProductCategoryVO(ProductCategory pc){
		this.id=pc.getId();
		this.name=pc.getName();
		this.code=pc.getCode();
		this.display=pc.getDisplay();
		this.imgUrl=pc.getImgUrl();
	}
	
	public ProductCategoryVO(ProductCategoryInfo pcinfo){
		this.id=pcinfo.getId();
		this.name=pcinfo.getName();
		this.code= null;
		this.display=pcinfo.getDisplay();
		this.imgUrl= null;
	}
}
