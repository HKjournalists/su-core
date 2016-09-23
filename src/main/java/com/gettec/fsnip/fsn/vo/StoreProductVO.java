package com.gettec.fsnip.fsn.vo;

import java.util.Date;

/**
 * 用于用户中心查看收藏的产品接口
 * @author ZhaWanNeng
 *
 */
public class StoreProductVO {
	private Long productid;  // 关联产品
	
	private String  productImg;   //产品的第一张图片（如无则返回默认的logo图片）
	
	private String  productName;   //产品名称
	
	private Double  riskIndex;   //风险指数
	
	private String  nutriLabel;   //营养标签
	
	private int  reportNum;   //报告数量
	
	private Date  updateDate;   //产品收藏时间
	
	private String businessUnitName;

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public StoreProductVO() {
		super();
	}
    
	public Double getRiskIndex() {
		return riskIndex;
	}

	public void setRiskIndex(Double riskIndex) {
		this.riskIndex = riskIndex;
	}

	public String getNutriLabel() {
		return nutriLabel;
	}

	public void setNutriLabel(String nutriLabel) {
		this.nutriLabel = nutriLabel;
	}

	public int getReportNum() {
		return reportNum;
	}

	public void setReportNum(int reportNum) {
		this.reportNum = reportNum;
	}
    
	public StoreProductVO(Long productid, String productImg,
			String productName, Double riskIndex, String nutriLabel,
			int reportNum, Date updateDate,String busName) {
		super();
		this.productid = productid;
		this.productImg = productImg;
		this.productName = productName;
		this.riskIndex = riskIndex;
		this.nutriLabel = nutriLabel;
		this.reportNum = reportNum;
		this.updateDate = updateDate;
		this.businessUnitName=busName;
	}

	public StoreProductVO(Long productid, String productImg,
			String productName, Date updateDate) {
		super();
		this.productid = productid;
		this.productImg = productImg;
		this.productName = productName;
		this.updateDate = updateDate;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}
   
	
}
