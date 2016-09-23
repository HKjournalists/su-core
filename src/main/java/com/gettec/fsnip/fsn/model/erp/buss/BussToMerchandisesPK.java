package com.gettec.fsnip.fsn.model.erp.buss;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gettec.fsnip.fsn.model.erp.base.BusinessType;

@Embeddable
public class BussToMerchandisesPK implements Serializable {
	private static final long serialVersionUID = 3042262156564363976L;

	@Column(name="NO_1", length = 50)
	private String no_1;//业务编号
	
	@Column(name="NO_2", length = 50)
	private Long no_2;//商品实例编号
	
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = BusinessType.class)
	@JoinColumn(name="TYPE_ID")
	private BusinessType type;//业务类型

	public String getNo_1() {
		return no_1;
	}

	public void setNo_1(String no_1) {
		this.no_1 = no_1;
	}

	public Long getNo_2() {
		return no_2;
	}

	public void setNo_2(Long no_2) {
		this.no_2 = no_2;
	}

	public BusinessType getType() {
		return type;
	}

	public void setType(BusinessType type) {
		this.type = type;
	}

	/**
	 * @param no_1
	 * @param no_2
	 * @param type
	 */
	public BussToMerchandisesPK(String no_1, Long no_2, BusinessType type) {
		super();
		this.no_1 = no_1;
		this.no_2 = no_2;
		this.type = type;
	}

	/**
	 * 
	 */
	public BussToMerchandisesPK() {
		super();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((no_1 == null) ? 0 : no_1.hashCode());
		result = (int) (prime * result + no_2);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BussToMerchandisesPK other = (BussToMerchandisesPK) obj;
		if (no_1 == null) {
			if (other.no_1 != null)
				return false;
		} else if (!no_1.equals(other.no_1))
			return false;
		if (no_2 != other.no_2)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
