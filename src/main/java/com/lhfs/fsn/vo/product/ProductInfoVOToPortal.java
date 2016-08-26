package com.lhfs.fsn.vo.product;

import java.util.List;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.NutritionReport;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.lhfs.fsn.vo.business.BussinessUnitVOToPortal;

/**
 * 食安监产品VO
 * @author YongHuang
 */
public class ProductInfoVOToPortal {
    private Long id; //产品ID
    private String name;  // 产品名称
    private String businessBrand;  // 商标
    private String regularity;  //  执行标准
    private String barcode;  //  条形码
    private String format;   // 规格'
    private String expirationDate;  // 保质天数
    private List<Resource> imgList; //产品图片
    private String imgUrl;
    private NutritionReport nutritionReport;    //产品营养报告   
    private List<BussinessUnitVOToPortal> businessUnit; //生产企业
	private TestRptJson selfRpt; //自检报告
	private TestRptJson censorRpt; //送检报告
	private TestRptJson sampleRpt; //抽检报告
	private String shareUrl;
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
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
	public String getBusinessBrand() {
		return businessBrand;
	}
	public void setBusinessBrand(String businessBrand) {
		this.businessBrand = businessBrand;
	}
	public String getRegularity() {
		return regularity;
	}
	public void setRegularity(String regularity) {
		this.regularity = regularity;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public List<Resource> getImgList() {
		return imgList;
	}
	public void setImgList(List<Resource> imgList) {
		this.imgList = imgList;
	}
	public NutritionReport getNutritionReport() {
		return nutritionReport;
	}
	public void setNutritionReport(NutritionReport nutritionReport) {
		this.nutritionReport = nutritionReport;
	}
	public List<BussinessUnitVOToPortal> getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(List<BussinessUnitVOToPortal> businessUnit) {
		this.businessUnit = businessUnit;
	}
	public TestRptJson getSelfRpt() {
		return selfRpt;
	}
	public void setSelfRpt(TestRptJson selfRpt) {
		this.selfRpt = selfRpt;
	}
	public TestRptJson getCensorRpt() {
		return censorRpt;
	}
	public void setCensorRpt(TestRptJson censorRpt) {
		this.censorRpt = censorRpt;
	}
	public TestRptJson getSampleRpt() {
		return sampleRpt;
	}
	public void setSampleRpt(TestRptJson sampleRpt) {
		this.sampleRpt = sampleRpt;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}
