package com.lhfs.fsn.vo.product;


/** 
 * 大众门户产品风险指数VO
 * @author Administrator
 */
public class ProductRiskVo {
	private Long id;
	private String name;
	private String imgUrl;
	private String riskIndex;
	private String testPropertyName;
	private Boolean riskSucceed;
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
	public String getRiskIndex() {
		return riskIndex;
	}
	public void setRiskIndex(String riskIndex) {
		this.riskIndex = riskIndex;
	}
	public String getTestPropertyName() {
		return testPropertyName;
	}
	public void setTestPropertyName(String testPropertyName) {
		this.testPropertyName = testPropertyName;
	}
	public Boolean getRiskSucceed() {
		return riskSucceed;
	}
	public void setRiskSucceed(Boolean riskSucceed) {
		this.riskSucceed = riskSucceed;
	}
	
	public ProductRiskVo() {
		super();
	}
	public ProductRiskVo(Long id, String name, String imgUrl,
			String riskIndex, String testPropertyName, Boolean riskSucceed) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.riskIndex = riskIndex;
		this.testPropertyName = testPropertyName;
		this.riskSucceed = riskSucceed;
	}
}
