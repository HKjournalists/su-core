package com.gettec.fsnip.fsn.vo.erp;

import java.io.Serializable;

public class OutVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5971256740293760089L;

	private String outOfBillNo;
	
	private String outDate;

	public String getOutOfBillNo() {
		return outOfBillNo;
	}

	public void setOutOfBillNo(String outOfBillNo) {
		this.outOfBillNo = outOfBillNo;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
}
