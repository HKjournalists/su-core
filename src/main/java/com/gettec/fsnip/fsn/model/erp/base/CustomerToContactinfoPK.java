package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CustomerToContactinfoPK implements Serializable {
	private static final long serialVersionUID = 953634666492080819L;
	
	@Column(name="CUSTOMER_NO")
	private Long customerNo;
	
	@Column(name="CONTACT_ID")
	private Long contactID;
	

	public Long getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(Long customerNo) {
		this.customerNo = customerNo;
	}

	public Long getContactID() {
		return contactID;
	}

	public void setContactID(Long contactID) {
		this.contactID = contactID;
	}

	/**
	 * @param customerNo
	 * @param contactID
	 */
	public CustomerToContactinfoPK(Long customerNo, Long contactID) {
		super();
		this.customerNo = customerNo;
		this.contactID = contactID;
	}

	/**
	 * 
	 */
	public CustomerToContactinfoPK() {
		super();
	}
	
	
}
