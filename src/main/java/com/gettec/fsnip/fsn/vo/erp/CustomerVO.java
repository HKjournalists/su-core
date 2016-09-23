package com.gettec.fsnip.fsn.vo.erp;

import java.io.Serializable;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.erp.base.OrderType;
import com.gettec.fsnip.fsn.model.erp.base.ReceivingNote;

public class CustomerVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2905639804507897328L;
	
	private BusinessUnit businessUnit;
	private OrderType ordertype;
	private ReceivingNote receivingNote;
	
	
	
	
	public ReceivingNote getReceivingNote() {
		return receivingNote;
	}
	public void setReceivingNote(ReceivingNote receivingNote) {
		this.receivingNote = receivingNote;
	}
	public OrderType getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(OrderType ordertype) {
		this.ordertype = ordertype;
	}
	
	public BusinessUnit getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}
	/**
	 * @param customer
	 * @param provider
	 */
	public CustomerVO(BusinessUnit businessUnit,OrderType ordertype) {
		super();
		this.businessUnit = businessUnit;
		this.ordertype = ordertype;
	}
	/**
	 * 
	 */
	public CustomerVO() {
		super();
	}
	
	
}
