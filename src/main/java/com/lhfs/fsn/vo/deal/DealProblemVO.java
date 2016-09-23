package com.lhfs.fsn.vo.deal;

import java.util.Date;

public class DealProblemVO {
	private String Scode; // 原始流水号
	private String businessName; // 企业名称
	private String licenseNo; // 企业营业执照号码
	private String barcode; // 产品条码
	private String productionDate; // 生产日期
	private String productName; // 产品名称
	private int WTCode; // 问题类型代码
	private Date TJtime; // 问题提交时间
	private Date Yjtime; // 监管要求完成时间
	private Date Wctime; // 处理完成时间
	private String DqCode; // 发现问题的行政区域代码
	private float longitude; // 经度
	private float latitude; // 纬度
	private String Address; // 发现问题的地址
	private String XXLY; // 消息来源
	private String MStatus; // 消息状态
	private String Remark; // 备注
	private String Backup; // 备用位
	public String getScode() {
		return Scode;
	}
	public void setScode(String scode) {
		Scode = scode;
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
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getWTCode() {
		return WTCode;
	}
	public void setWTCode(int wTCode) {
		WTCode = wTCode;
	}
	public Date getTJtime() {
		return TJtime;
	}
	public void setTJtime(Date tJtime) {
		TJtime = tJtime;
	}
	public Date getYjtime() {
		return Yjtime;
	}
	public void setYjtime(Date yjtime) {
		Yjtime = yjtime;
	}
	public Date getWctime() {
		return Wctime;
	}
	public void setWctime(Date wctime) {
		Wctime = wctime;
	}
	public String getDqCode() {
		return DqCode;
	}
	public void setDqCode(String dqCode) {
		DqCode = dqCode;
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
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getXXLY() {
		return XXLY;
	}
	public void setXXLY(String xXLY) {
		XXLY = xXLY;
	}
	public String getMStatus() {
		return MStatus;
	}
	public void setMStatus(String mStatus) {
		MStatus = mStatus;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getBackup() {
		return Backup;
	}
	public void setBackup(String backup) {
		Backup = backup;
	}
	
}
