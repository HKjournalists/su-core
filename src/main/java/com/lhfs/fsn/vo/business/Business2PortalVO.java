package com.lhfs.fsn.vo.business;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.lhfs.fsn.vo.product.ProductSimpleVO;

/**
 * 大众门户企业信息VO
 * @author YongHuang
 */
public class Business2PortalVO {
	private Long id;
	private String name;  // 企业名称
	private String logo;	 //企业logo
	private String type;     //企业类型
	private String about;  // 企业简介
	private String website;  // 企业guanw
	private Long organization;  // 企业组织机构
	private int countOfReport;//企业报告总数
	private int countOfProduct;//企业产品总数
    private List<ProductSimpleVO> productList; //主营商品

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

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public List<ProductSimpleVO> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductSimpleVO> productList) {
		this.productList = productList;
	}

	public Long getOrganization() {
		return organization;
	}
	public void setOrganization(Long organization) {
		this.organization = organization;
	}
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
	public int getCountOfReport() {
		return countOfReport;
	}
	public void setCountOfReport(int countOfReport) {
		this.countOfReport = countOfReport;
	}
	public int getCountOfProduct() {
		return countOfProduct;
	}
	public void setCountOfProduct(int countOfProduct) {
		this.countOfProduct = countOfProduct;
	}
	
}
