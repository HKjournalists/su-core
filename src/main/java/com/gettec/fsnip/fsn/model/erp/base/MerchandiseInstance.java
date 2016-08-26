package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * MerchandiseInstance Entity<br>
 * 商品实例
 * @author Administrator
 */
@Entity(name="T_META_MERCHANDISE_INFO_INSTANCE")
public class MerchandiseInstance extends Model implements Serializable {
	private static final long serialVersionUID = 1201032556782606230L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="INSTANCE_ID")
	private Long instanceID;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = InitializeProduct.class)
	@JoinColumn(name="product_id", nullable = false)
	private InitializeProduct initializeProduct;
	
	@Column(name="BATCH_NUMBER", length=20)
	private String batch_number;

	public Long getInstanceID() {
		return instanceID;
	}

	public void setInstanceID(Long instanceID) {
		this.instanceID = instanceID;
	}

	public InitializeProduct getInitializeProduct() {
		return initializeProduct;
	}

	public void setInitializeProduct(InitializeProduct initializeProduct) {
		this.initializeProduct = initializeProduct;
	}

	public String getBatch_number() {
		return batch_number;
	}

	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	/**
	 * @param instanceID
	 * @param info
	 * @param batch_number
	 */
	public MerchandiseInstance(Long instanceID, InitializeProduct initializeProduct,
			String batch_number) {
		super();
		this.instanceID = instanceID;
		this.initializeProduct = initializeProduct;
		this.batch_number = batch_number;
	}

	/**
	 * 
	 */
	public MerchandiseInstance() {
		super();
	}
	
	
}
