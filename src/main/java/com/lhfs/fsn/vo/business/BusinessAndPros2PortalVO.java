package com.lhfs.fsn.vo.business;

import java.util.List;

import com.lhfs.fsn.vo.product.ProductSortVo;

/**
 * 大众门户企业信息VO
 * @author lxz
 */
public class BusinessAndPros2PortalVO {
	private Long id;
	private String name;  // 企业名称
	private String logo;	 //企业logo
	private String about;  // 企业简介
    private List<ProductSortVo> productList; //主营商品

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
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}

    public List<ProductSortVo> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductSortVo> productList) {
		this.productList = productList;
	}
	public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
	
}
