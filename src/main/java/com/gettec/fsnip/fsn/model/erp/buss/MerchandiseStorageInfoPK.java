package com.gettec.fsnip.fsn.model.erp.buss;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * 商品库存复合主键
 *
 */
@Embeddable
public class MerchandiseStorageInfoPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1550852857835139567L;

	@Column(name="NO_1", length = 20)
	private String no_1; //仓库编号
	
	@Column(name="NO_2", length = 20)
	private Long no_2;//商品实例编号

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

	/**
	 * @param no_1
	 * @param no_2
	 */
	public MerchandiseStorageInfoPK(String no_1, Long no_2) {
		super();
		this.no_1 = no_1;
		this.no_2 = no_2;
	}

	/**
	 * 
	 */
	public MerchandiseStorageInfoPK() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((no_1 == null) ? 0 : no_1.hashCode());
		result = (int) (prime * result + no_2);
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
		MerchandiseStorageInfoPK other = (MerchandiseStorageInfoPK) obj;
		if (no_1 == null) {
			if (other.no_1 != null)
				return false;
		} else if (!no_1.equals(other.no_1))
			return false;
		if (no_2 != other.no_2)
			return false;
		return true;
	}

}
