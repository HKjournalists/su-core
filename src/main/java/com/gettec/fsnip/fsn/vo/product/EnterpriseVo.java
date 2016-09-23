package com.gettec.fsnip.fsn.vo.product;

import java.util.ArrayList;
import java.util.List;



/**
 * 大众门户VO企业专栏接口
 * @author ZhaWanNeng
 */
public class EnterpriseVo {
	private Long id;
	private String name;//名称
	private String logo;//企业Logo
	private String about;//企业简介
	private String website;  // 企业guanw
	private int countOfReport;//企业报告总数
	private int countOfProduct;//企业产品总数
	private List<ProductReportVo> productList = new ArrayList<ProductReportVo>();//企业产品列表
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
	public List<ProductReportVo> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductReportVo> productList) {
		this.productList = productList;
	}
	
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public EnterpriseVo() {
		super();
	}
	
	public EnterpriseVo(Long id, String name, String logo, int countOfReport,
			int countOfProduct, List<ProductReportVo> productList) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.countOfReport = countOfReport;
		this.countOfProduct = countOfProduct;
		this.productList = productList;
	}
	public EnterpriseVo(Long id, String name, String logo, String about,
			int countOfReport, int countOfProduct,
			List<ProductReportVo> productList) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.about = about;
		this.countOfReport = countOfReport;
		this.countOfProduct = countOfProduct;
		this.productList = productList;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public EnterpriseVo(Long id, String name, String logo, String about,
			String website, int countOfReport, int countOfProduct,
			List<ProductReportVo> productList) {
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
	
}
