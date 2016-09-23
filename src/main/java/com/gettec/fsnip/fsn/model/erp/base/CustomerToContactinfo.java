package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * CustomerToContactinfo Entity<br>
 * 客户_TO_联系人信息
 * @author Administrator
 */
@Entity(name="T_META_CUSTOMER_TO_CONTACT")
public class CustomerToContactinfo extends Model {
	private static final long serialVersionUID = -854660732078946626L;
	
	@EmbeddedId
	private CustomerToContactinfoPK id;
	
	/**
	 * CUSTOMER  type = 1
	 * PROVIDER  type = 2
	 */
	@Column(name="CUSTOMER_TYPE")
	private int type;
	
	@Column(name="IS_DIRECT")
	private boolean isDirect;

	public CustomerToContactinfoPK getId() {
		return id;
	}

	public void setId(CustomerToContactinfoPK id) {
		this.id = id;
	}

	public boolean isDirect() {
		return isDirect;
	}

	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param id
	 * @param isDirect
	 */
	public CustomerToContactinfo(CustomerToContactinfoPK id, boolean isDirect) {
		super();
		this.id = id;
		this.isDirect = isDirect;
	}

	/**
	 * 
	 */
	public CustomerToContactinfo() {
		super();
	}

	/**
	 * @param id
	 */
	public CustomerToContactinfo(CustomerToContactinfoPK id) {
		super();
		this.id = id;
	}
	
	
}
