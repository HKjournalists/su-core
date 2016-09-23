package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReceivingNoteToContactinfoPK implements Serializable{
	private static final long serialVersionUID = -4133163261667218951L;
	
	@Column(name="receivenote_no", length=20)
	private String receivenote_no;
	
	@Column(name="CONTACT_ID")
	private Long contact_id;

	public String getReceivenote_no() {
		return receivenote_no;
	}

	public void setReceivenote_no(String receivenote_no) {
		this.receivenote_no = receivenote_no;
	}

	public Long getContact_id() {
		return contact_id;
	}

	public void setContact_id(Long contact_id) {
		this.contact_id = contact_id;
	}
	
	public ReceivingNoteToContactinfoPK(){
		super();
	}
	/**
	 * @param receivenote_no
	 * @param contact_id
	 */
	public ReceivingNoteToContactinfoPK(String receivenote_no,long contact_id){
		super();
		this.receivenote_no = receivenote_no;
		this.contact_id = contact_id;
	}
	
}
