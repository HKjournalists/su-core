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

@Entity(name = "deal_problem")
public class DealProblem extends Model{
	
	
	private static final long serialVersionUID = 3410374874508885412L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long	id;	//序号
	
	@Column
	private String  province;   //省份
	
	@Column
	private String  city;   //城市
	
	@Column
	private String  counties;   //县区
	
	@Column(name = "longitude")
	private float longitude; //经度
	
	@Column(name = "latitude")
	private float latitude; //纬度
	
	@Column(name = "product_name")
	private String  productName;   //产品名称
	
	@Column(name = "business_name")
	private String  businessName;   //企业名称
	
	@Column(name = "license_no")
	private String  licenseNo;   //营业执照
	
	private String	barcode;	//条形码
	
	@Column(name = "product_time")
	private Date	productTime;   //生产日期
	
	@Column(name = "create_time")
	private Date createTime;   //用户发现时间
	
	@Column(name = "deal_time")
	private Date dealTime;   //企业处理完成时间
	
	@Column(name = "request_deal_time")
	private Date requestDealTime;   //将要求处理完成时间
	
	@Column(name = "user_id")
	private long userId;	

	/**
	 * 问题类型:
	 * 1代表：产品信息错误，
	 * 2代表：有产品信息，无报告信息，
	 * 3代表：报告过期，未上传新报告，	
	 * 4代表：无生产企业证照，
	 * 5代表：生产企业证照错误，
	 * 6代表：无产品信息	
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "problem_type")
	private DealProblemTypeEnums	problemType;//问题类型
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "origin")
	private DealProblemTypeEnums  origin;   //信息来源:0监管/1食安测/2终端
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "commit_status")
	private DealProblemTypeEnums commitStatus;   //管理员：0提交企业1提交监管/2忽略
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "complain_status")
	private DealProblemTypeEnums complainStatus;   //投诉处理/新增投诉0/已提交投诉1/忽略2
	
	@Column(name = "remark")
	private String  remark;   //备注

	@Column(name = "business_id")
	private long businessId;
	
	@Column(name = "s_code") //流水号
	private String Scode;
	
	@Transient 
	private String showProblemType;//在页面显示问题类型是什么
	@Transient 
	private String showOrigin;//在页面显示投诉来源是哪
	@Transient 
	private String showCommitStatus;//在页面显示提交状态
	@Transient 
	private String showComplainStatus;//在页面显示投诉状态

	public String getShowProblemType() {
		return showProblemType;
	}

	public void setShowProblemType(String showProblemType) {
		this.showProblemType = showProblemType;
	}

	public String getShowOrigin() {
		return showOrigin;
	}

	public void setShowOrigin(String showOrigin) {
		this.showOrigin = showOrigin;
	}


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getShowCommitStatus() {
		return showCommitStatus;
	}

	public void setShowCommitStatus(String showCommitStatus) {
		this.showCommitStatus = showCommitStatus;
	}

	public String getShowComplainStatus() {
		return showComplainStatus;
	}

	public void setShowComplainStatus(String showComplainStatus) {
		this.showComplainStatus = showComplainStatus;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounties() {
		return counties;
	}

	public void setCounties(String counties) {
		this.counties = counties;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
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

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getProductTime() {
		return productTime;
	}

	public void setProductTime(Date productTime) {
		this.productTime = productTime;
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

	public Date getRequestDealTime() {
		return requestDealTime;
	}

	public void setRequestDealTime(Date requestDealTime) {
		this.requestDealTime = requestDealTime;
	}

	public DealProblemTypeEnums getProblemType() {
		return problemType;
	}

	public void setProblemType(DealProblemTypeEnums problemType) {
		this.problemType = problemType;
	}

	public DealProblemTypeEnums getOrigin() {
		return origin;
	}

	public void setOrigin(DealProblemTypeEnums origin) {
		this.origin = origin;
	}

	public DealProblemTypeEnums getCommitStatus() {
		return commitStatus;
	}

	public void setCommitStatus(DealProblemTypeEnums commitStatus) {
		this.commitStatus = commitStatus;
	}

	public DealProblemTypeEnums getComplainStatus() {
		return complainStatus;
	}

	public void setComplainStatus(DealProblemTypeEnums complainStatus) {
		this.complainStatus = complainStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public String getScode() {
		return Scode;
	}

	public void setScode(String scode) {
		Scode = scode;
	}
	
	//-------------get set 方法区------------------------------
	
}
