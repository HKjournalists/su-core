package com.gettec.fsnip.fsn.vo.deal;

import java.util.Date;

public class DealProblemVO {
	
	// 原始流水号
		private String scode;
		
		// 监管机构名称
		private String jgname;
		
		// 企业名称
		private String qyname;
		
		// 企业营业执照号码
		private String qylicense;
		
		// 产品条码
		private String barcode;
		
		// 生产日期
		private Date productTime;
		
		// 产品名称
		private String productName;
		
		// 问题类型代码
		private Integer wtcode;
		
		// 问题提交时间
		private Date tjtime;
		
		// 监管要求完成时间
		private Date yjtime;
		
		// 处理完成时间
		private Date wctime;
		
		// 发现问题的行政区域代码
		private String dqcode;
		
		// 经度
		private double longitude;
		
		// 纬度
		private double latitude;
		
		// 发现问题的地址
		private String address;
		
		// 消息来源
		private String xxly;
		
		// 消息状态
		private int mstatus;
		
		// 提交账号
		private String createUserId;
		
		// 备注
		private String remark;
		
		// 备用位
		private String backup;

		public String getScode() {
			return scode;
		}

		public void setScode(String scode) {
			this.scode = scode;
		}

		public String getJgname() {
			return jgname;
		}

		public void setJgname(String jgname) {
			this.jgname = jgname;
		}

		public String getQyname() {
			return qyname;
		}

		public void setQyname(String qyname) {
			this.qyname = qyname;
		}

		public String getQylicense() {
			return qylicense;
		}

		public void setQylicense(String qylicense) {
			this.qylicense = qylicense;
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

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public Integer getWtcode() {
			return wtcode;
		}

		public void setWtcode(Integer wtcode) {
			this.wtcode = wtcode;
		}

		public Date getTjtime() {
			return tjtime;
		}

		public void setTjtime(Date tjtime) {
			this.tjtime = tjtime;
		}

		public Date getYjtime() {
			return yjtime;
		}

		public void setYjtime(Date yjtime) {
			this.yjtime = yjtime;
		}

		public Date getWctime() {
			return wctime;
		}

		public void setWctime(Date wctime) {
			this.wctime = wctime;
		}

		public String getDqcode() {
			return dqcode;
		}

		public void setDqcode(String dqcode) {
			this.dqcode = dqcode;
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

		public String getXxly() {
			return xxly;
		}

		public void setXxly(String xxly) {
			this.xxly = xxly;
		}

		public int getMstatus() {
			return mstatus;
		}

		public void setMstatus(int mstatus) {
			this.mstatus = mstatus;
		}

		public String getCreateUserId() {
			return createUserId;
		}

		public void setCreateUserId(String createUserId) {
			this.createUserId = createUserId;
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

		
	
//	private String Scode; // 原始流水号
//	private String businessName; // 企业名称
//	private String licenseNo; // 企业营业执照号码
//	private String barcode; // 产品条码
//	private String productionDate; // 生产日期
//	private String productName; // 产品名称
//	private int WTCode; // 问题类型代码
//	private String TJtime; // 问题提交时间
//	private String Yjtime; // 监管要求完成时间
//	private String Wctime; // 处理完成时间
//	private String DqCode; // 发现问题的行政区域代码
//	private float longitude; // 经度
//	private float latitude; // 纬度
//	private String Address; // 发现问题的地址
//	private String XXLY; // 消息来源
//	private String MStatus; // 消息状态
//	private String Remark; // 备注
//	private String Backup; // 备用位
	
}
