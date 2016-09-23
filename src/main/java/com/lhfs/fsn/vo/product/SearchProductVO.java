package com.lhfs.fsn.vo.product;

import java.util.List;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.market.Resource;

/**
 * 大众门户产品搜索VO
 * @author Administrator
 */
public class SearchProductVO {
	private Long id;
	private String name;
	private Float qscoreSelf; 
	private Float qscoreCensor;	
	private Float qscoreSample;
	private String imgUrl; 	
	private String cstm; 
	private Integer reportCounts;
	private List<Certification> productCertification;
	private Long selfCount;  // 自检报告数量
	private Long censorCount;  //  送检报告数量
	private Long sampleCount;  //  抽检报告数量
	private Double riskIndex;   //风险指数
	private Boolean riskSucceed ;   //风险指数的计算成功:0：失败、1：成功
	private String testPropertyName;   //风险指数计算相关的检测项目名称的字符串
	private String nutriLabel; //营养指数
	private List<Resource> imgList;
	private boolean isImportedProduct;// 是否是进口食品 true:是 false:不是
	private int certificationStatus;////0：未通过认证、1：通过食安认证、2：通过联盟认证

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

	public Float getQscoreSelf() {
		return qscoreSelf;
	}

	public void setQscoreSelf(Float qscoreSelf) {
		this.qscoreSelf = qscoreSelf;
	}

	public Float getQscoreCensor() {
		return qscoreCensor;
	}

	public void setQscoreCensor(Float qscoreCensor) {
		this.qscoreCensor = qscoreCensor;
	}

	public Float getQscoreSample() {
		return qscoreSample;
	}

	public void setQscoreSample(Float qscoreSample) {
		this.qscoreSample = qscoreSample;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCstm() {
		return cstm;
	}

	public void setCstm(String cstm) {
		this.cstm = cstm;
	}

	public List<Certification> getProductCertification() {
		return productCertification;
	}

	public void setProductCertification(List<Certification> productCertification) {
		this.productCertification = productCertification;
	}

	public Integer getReportCounts() {
		return reportCounts;
	}

	public void setReportCounts(Integer reportCounts) {
		this.reportCounts = reportCounts;
	}

	public Long getSelfCount() {
		return selfCount;
	}

	public void setSelfCount(Long selfCount) {
		this.selfCount = selfCount;
	}

	public Long getCensorCount() {
		return censorCount;
	}

	public void setCensorCount(Long censorCount) {
		this.censorCount = censorCount;
	}

	public Long getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(Long sampleCount) {
		this.sampleCount = sampleCount;
	}

	public Double getRiskIndex() {
		return riskIndex;
	}

	public void setRiskIndex(Double riskIndex) {
		this.riskIndex = riskIndex;
	}

	public Boolean getRiskSucceed() {
		return riskSucceed;
	}

	public void setRiskSucceed(Boolean riskSucceed) {
		this.riskSucceed = riskSucceed;
	}

	public String getTestPropertyName() {
		return testPropertyName;
	}

	public void setTestPropertyName(String testPropertyName) {
		this.testPropertyName = testPropertyName;
	}

	public String getNutriLabel() {
		return nutriLabel;
	}

	public void setNutriLabel(String nutriLabel) {
		this.nutriLabel = nutriLabel;
	}

	public List<Resource> getImgList() {
		return imgList;
	}

	public void setImgList(List<Resource> imgList) {
		this.imgList = imgList;
	}

	public boolean isImportedProduct() {
		return isImportedProduct;
	}

	public void setImportedProduct(boolean isImportedProduct) {
		this.isImportedProduct = isImportedProduct;
	}

	public int getCertificationStatus() {
		return certificationStatus;
	}

	public void setCertificationStatus(int certificationStatus) {
		this.certificationStatus = certificationStatus;
	}
	
}
