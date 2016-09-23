package com.gettec.fsnip.fsn.model.deal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.enums.DealProblemTypeEnums;
import com.gettec.fsnip.fsn.model.common.Model;

@Entity(name = "deal_to_problem")
public class DealToProblem extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
    private long id ; // bigint(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
	
	
	@Column(name = "business_id")
	private long businessId ; //BIGINT(20) NOT NULL  COMMENT '企业的ID',
	
	@Column(name = "jgname")
	private String jgname ; //VARCHAR(255) NOT NULL  COMMENT '企业的ID',
	
	@Column(name = "license_no")
	private String licenseNo ; //VARCHAR(50) DEFAULT NULL  COMMENT '营业执照号',
	
	@Column(name = "production_date")
	private Date productionDate; //VARCHAR(255) DEFAULT NULL COMMENT '生产时间',
	
	@Column(name = "product_name")
	private String productName; //VARCHAR(255) DEFAULT NULL COMMENT '产品名称',
	
	@Column(name = "business_name")
	private String businessName; //VARCHAR(200) DEFAULT NULL COMMENT '企业名称',
	
	@Column(name = "barcode")
	private String barcode; //VARCHAR(100) DEFAULT NULL COMMENT '条形码',
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "problem_type")
	private DealProblemTypeEnums problemType; //INT(10) DEFAULT NULL COMMENT '问题类型',
	
	@Column(name = "create_time")
	private Date createTime; //DATETIME DEFAULT NULL COMMENT '用户问题发现时间',
	
	@Column(name = "deal_time")
	private Date dealTime; //DATETIME DEFAULT NULL COMMENT '企业处理时间',  
	
	@Column(name = "finish_time")
	private Date finishTime; //DATETIME DEFAULT NULL COMMENT '企业处理完成时间',  
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "deal_status", nullable = false)
	private DealProblemTypeEnums dealStatus; //INT(2) DEFAULT NULL COMMENT '企业处理：已处理/未处理',
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "commit_status")
	private DealProblemTypeEnums commitStatus; //INT(3) DEFAULT NULL COMMENT '管理员：提交企业/提交监管/忽略',
	
	@Column(name = "problem_code")
	private String problemCode; //发现问题的行政区域代码
	
	@Column(name = "longitude")
	private  double longitude; //发现问题的经度
	
	@Column(name = "latitude")
	private double latitude; //发现问题的纬度
	
	@Column(name = "address")
	private String address; //发现问题的地址
	
	@Column(name = "info_source")
	private String infoSource; //信息来源
	
	@Column(name = "info_status")
	private String infoStatus; //信息来源
	
	@Column(name = "remark")
	private String remark; //VARCHAR(1000) DEFAULT NULL COMMENT '备注',
	
	@Column(name = "backup")
	private String backup; //备用位,
	
	@Column(name = "operator")
	private String operator; //处理人,
	
	@Column(name = "opunit")
	private String OpUnit; //处理单位,
	
	@Column(name = "scode")
	private String scode; //原始流水号,
	
	@Transient
	private String dealType; //处理状态,
	
	@Transient
	private String status; //管理员：提交企业/提交监管/忽略,
	
	@Transient
	private String problem; //问题类型,

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}
    
	public String getJgname() {
		return jgname;
	}

	public void setJgname(String jgname) {
		this.jgname = jgname;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public DealProblemTypeEnums getProblemType() {
		return problemType;
	}

	public void setProblemType(DealProblemTypeEnums problemType) {
		this.problemType = problemType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public DealProblemTypeEnums getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(DealProblemTypeEnums dealStatus) {
		this.dealStatus = dealStatus;
	}

	public DealProblemTypeEnums getCommitStatus() {
		return commitStatus;
	}

	public void setCommitStatus(DealProblemTypeEnums commitStatus) {
		this.commitStatus = commitStatus;
	}

	public String getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInfoSource() {
		return infoSource;
	}

	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}

	public String getInfoStatus() {
		return infoStatus;
	}

	public void setInfoStatus(String infoStatus) {
		this.infoStatus = infoStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBackup() {
		return backup;
	}

	public void setBackup(String backup) {
		this.backup = backup;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOpUnit() {
		return OpUnit;
	}

	public void setOpUnit(String opUnit) {
		OpUnit = opUnit;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}
	
}
