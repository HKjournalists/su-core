/**
 * 
 */
package com.tzquery.fsn.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回的产品信息VO
 * @author ChenXiaolin 2015-12-1
 */
public class TzQueryResponseProInfoVO {

	private String proId;			//产品ID
	private String proBarcode;		//产品条形码
	private String proName;			//产品名称
	private String proCategory;		//产品分类
	private String proQs;			//产品许可证号
	private String proFormat;		//规格型号
	private String checkWay;		//检验方式
	private String salesArea;		//销售范围
	private String whetherEexport;	//是否出口
	private String proBrand;		//注册商标
	private String issueUnit;		//发证单位
	private String issueDate;		//发证日期
	private String validDate;		//有效期至
	private String proStatus;		//生产状态
	private String relationType;	//关系类型
	List<TzQueryStandardsVO> standards = new ArrayList<TzQueryStandardsVO>();//执行标准列表
	
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getProBarcode() {
		return proBarcode;
	}
	public void setProBarcode(String proBarcode) {
		this.proBarcode = proBarcode;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getProCategory() {
		return proCategory;
	}
	public void setProCategory(String proCategory) {
		this.proCategory = proCategory;
	}
	public String getProQs() {
		return proQs;
	}
	public void setProQs(String proQs) {
		this.proQs = proQs;
	}
	public String getProFormat() {
		return proFormat;
	}
	public void setProFormat(String proFormat) {
		this.proFormat = proFormat;
	}
	public String getCheckWay() {
		return checkWay;
	}
	public void setCheckWay(String checkWay) {
		this.checkWay = checkWay;
	}
	public String getSalesArea() {
		return salesArea;
	}
	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea;
	}
	public String getWhetherEexport() {
		return whetherEexport;
	}
	public void setWhetherEexport(String whetherEexport) {
		this.whetherEexport = whetherEexport;
	}
	public String getProBrand() {
		return proBrand;
	}
	public void setProBrand(String proBrand) {
		this.proBrand = proBrand;
	}
	public String getIssueUnit() {
		return issueUnit;
	}
	public void setIssueUnit(String issueUnit) {
		this.issueUnit = issueUnit;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public String getProStatus() {
		return proStatus;
	}
	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public List<TzQueryStandardsVO> getStandards() {
		return standards;
	}
	public void setStandards(List<TzQueryStandardsVO> standards) {
		this.standards = standards;
	}
	
}
