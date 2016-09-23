package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OutBillToOutGoodsPK implements Serializable {
	private static final long serialVersionUID = 1475173414012284405L;

	@Column(name="OUTOFBILL_NO", length=20)
	private String outOfBillNo;
	
	@Column(name="OUTGOODS_ID")
	private Long outGoodsId;
	
	public OutBillToOutGoodsPK() {
		super();
	}
	
	public OutBillToOutGoodsPK(String outOfBillNo, Long outGoodsId) {
		super();
		this.outOfBillNo = outOfBillNo;
		this.outGoodsId = outGoodsId;
	}

	public String getOutOfBillNo() {
		return outOfBillNo;
	}

	public void setOutOfBillNo(String outOfBillNo) {
		this.outOfBillNo = outOfBillNo;
	}

	public Long getOutGoodsId() {
		return outGoodsId;
	}

	public void setOutGoodsId(Long outGoodsId) {
		this.outGoodsId = outGoodsId;
	}
}
