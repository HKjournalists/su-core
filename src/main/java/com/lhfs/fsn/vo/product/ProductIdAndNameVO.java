package com.lhfs.fsn.vo.product;

/**
 * 大众门户根据proIds返回的产品VO
 * @author jazhen
 * 
 * alter lyj 2015-09-18  新增统计报告数量字段 reportNum
 *
 */
public class ProductIdAndNameVO {
    
	private Long id;
	
	private String name;
	
	private String imgUrl;
	
	private String countryName;
	
//	private String productionArea;
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	private Long reportNum; //该产品的报告数量统计
	
	public Long getreportNum() {
		return reportNum;
	}
	public void setreportNum(Long reportNum) {
		this.reportNum = reportNum;
	}
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

	public ProductIdAndNameVO(Long id, String name,String imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl=imgUrl;
	}
	public ProductIdAndNameVO() {
		super();
	}
	public ProductIdAndNameVO(Long id, String name, String imgUrl,Long reportNum,String countryName) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.reportNum=reportNum;
		this.countryName=countryName;
	}
	
}
