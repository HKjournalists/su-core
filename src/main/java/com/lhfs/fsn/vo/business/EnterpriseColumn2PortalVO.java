package com.lhfs.fsn.vo.business;

import java.util.List;

import com.lhfs.fsn.vo.product.Product2EnterpriseColumnVO;

/**
 * 大众门户企业信息VO
 * @author YongHuang
 */
public class EnterpriseColumn2PortalVO {
	private Long id;
	private String name;  // 企业名称
	private String logo;	 //企业logo
	private String about;  // 企业简介
	private String website;  // 企业官网
	private int countOfReport;//企业报告总数
	private int countOfProduct;//企业产品总数
    private List<Product2EnterpriseColumnVO> productList; //主营商品

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
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public List<Product2EnterpriseColumnVO> getProductList() {
		return productList;
	}
	public void setProductList(List<Product2EnterpriseColumnVO> productList) {
		this.productList = productList;
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
		return countOfProduct;	}
	public void setCountOfProduct(int countOfProduct) {
		this.countOfProduct = countOfProduct;
	}
	public EnterpriseColumn2PortalVO(Long id, String name, String logo,
			String about, String website, int countOfReport,
			int countOfProduct, 
			List<Product2EnterpriseColumnVO> productList) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.about = about;
		this.website = website;
		this.countOfReport = countOfReport;
		this.countOfProduct = countOfProduct;
		this.productList = productList;
	}
	public EnterpriseColumn2PortalVO() {
		super();
	}
	
}
