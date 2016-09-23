package com.gettec.fsnip.fsn.vo.deal;

import java.util.Date;

public class DealProblemLogVO {
	
	// 问题消息流水号
		private String pcode;
		
		// 处理人
		private String operator;
		
		// 处理单位
		private String opunit;
		
		// 产品条码
		private String barcode;
		
		// 生产日期
		private Date producttime;
		
		// 产品名称
		private String productname;
		
		// 操作时间
		private Date optime;
		
		// 修改前状态
		private int fstatus;
		
		// 修改后状态
		private int bstatus;
		
		// 备注
		private String remark;

		public String getPcode() {
			return pcode;
		}

		public void setPcode(String pcode) {
			this.pcode = pcode;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

		public String getOpunit() {
			return opunit;
		}

		public void setOpunit(String opunit) {
			this.opunit = opunit;
		}

		public String getBarcode() {
			return barcode;
		}

		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		public Date getProducttime() {
			return producttime;
		}

		public void setProducttime(Date producttime) {
			this.producttime = producttime;
		}

		public String getProductname() {
			return productname;
		}

		public void setProductname(String productname) {
			this.productname = productname;
		}

		public Date getOptime() {
			return optime;
		}

		public void setOptime(Date optime) {
			this.optime = optime;
		}

		public int getFstatus() {
			return fstatus;
		}

		public void setFstatus(int fstatus) {
			this.fstatus = fstatus;
		}

		public int getBstatus() {
			return bstatus;
		}

		public void setBstatus(int bstatus) {
			this.bstatus = bstatus;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	
//	private int BStatus;	//修改后状态
//	private int FStatus;	//修改前状态
//	private String barcode;	//产品条码
//	private boolean jgFlag = true;
//	private String opUnit;	//处理单位
//	private String operator	; //处理人
//	private String optime;//	操作时间
//	private String pcode;
//	private String productName;	//产品名称
//	private String productionDate;	//生产日期
	
}
