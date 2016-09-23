package com.lhfs.fsn.vo.atgoo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品信息VO 为爱特够系统提供
 * @author tangxin 2016/6/26
 *
 */
public class ProductVO implements Serializable{
	/**
	 * 产品id
	 */
	long id;
	
	/**
	 * 营养成分
	 */
	String ingredient;
	/**
	 * 适用人群
	 */
	String cstm;
	/**
	 * 安全指数
	 */
	String riskIndex;
	/**
	 * 检测日期
	 */
	String testDate;
	
	/**
	 * 产品状态
	 */
	String status;
	
	/**
	 * 生产日期
	 */
	String productionDate;
	
	/**
	 * 批次
	 */
	String batchSerialNo; 
	
	/**
	 * 判定依据
	 */
	String judgeGist;
	
	/**
	 * 检验结论
	 */
	String result;
	/**
	 * 安全指数计算项目
	 */
	String testPropertyName;
	
	/**
	 * 最新检测报告ID
	 */
	Long testResultId;
	/**
	 * 
	 * 大众门户产品详细地址
	 */
	String detailUrl;
	/**
	 * 产品的认证信息
	 */
	List<CertificationVO> certification = new ArrayList<CertificationVO>();
	/**
	 * 产品的营养报告
	 */
	List<NutritionVO> nutritionReport = new ArrayList<NutritionVO>();
	
	/**
	 * 产品的营养报告
	 */
	List<TestPropertyVO> testProperty = new ArrayList<TestPropertyVO>();
	
	List testList = new ArrayList();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getCstm() {
		return cstm;
	}
	public void setCstm(String cstm) {
		this.cstm = cstm;
	}
	public String getRiskIndex() {
		return riskIndex;
	}
	public void setRiskIndex(String riskIndex) {
		this.riskIndex = riskIndex;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTestPropertyName() {
		return testPropertyName;
	}
	public void setTestPropertyName(String testPropertyName) {
		this.testPropertyName = testPropertyName;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public List<CertificationVO> getCertification() {
		return certification;
	}
	public void setCertification(List<CertificationVO> certification) {
		this.certification = certification;
	}
	public List<NutritionVO> getNutritionReport() {
		return nutritionReport;
	}
	public void setNutritionReport(List<NutritionVO> nutritionReport) {
		this.nutritionReport = nutritionReport;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getBatchSerialNo() {
		return batchSerialNo;
	}
	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}
	public String getJudgeGist() {
		return judgeGist;
	}
	public void setJudgeGist(String judgeGist) {
		this.judgeGist = judgeGist;
	}
	public List<TestPropertyVO> getTestProperty() {
		return testProperty;
	}
	public void setTestProperty(List<TestPropertyVO> testProperty) {
		this.testProperty = testProperty;
	}
	public Long getTestResultId() {
		return testResultId;
	}
	public void setTestResultId(Long testResultId) {
		this.testResultId = testResultId;
	}
	public List getTestList() {
		return testList;
	}
	public void setTestList(List  testList) {
		this.testList = testList;
	}
	
	
	
}
