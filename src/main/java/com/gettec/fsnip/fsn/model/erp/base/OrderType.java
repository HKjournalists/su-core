package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * OrderType Entity<br>
 * @author Administrator
 */
@Entity(name="T_META_ORDER_TYPE")
public class OrderType extends Model {
	private static final long serialVersionUID = 1125830654356789745L;

	public final static int OrderType = 11;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ot_id")
	private Long ot_id;
	
	@Column(name="ot_type", length=30)
	private String ot_type;//单别
	
	@Column(name="ot_describe", length=50)
	private String ot_describe;//单别描述

	@Column(name="ot_belong_module", length=30)
	private String ot_belong_module;//所属模块
	
	@Column(name="ot_belong_order", length=30)
	private String ot_belong_order;//所属单据
	
	@Column(name="organization")
	private Long organization;

	public Long getOrganization() {
		return organization;
	}

	public Long getOt_id() {
		return ot_id;
	}

	public void setOt_id(Long ot_id) {
		this.ot_id = ot_id;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getOt_type() {
		return ot_type;
	}

	public void setOt_type(String ot_type) {
		this.ot_type = ot_type;
	}

	public String getOt_describe() {
		return ot_describe;
	}

	public void setOt_describe(String ot_describe) {
		this.ot_describe = ot_describe;
	}

	public String getOt_belong_module() {
		return ot_belong_module;
	}

	public void setOt_belong_module(String ot_belong_module) {
		this.ot_belong_module = ot_belong_module;
	}

	public String getOt_belong_order() {
		return ot_belong_order;
	}

	public void setOt_belong_order(String ot_belong_order) {
		this.ot_belong_order = ot_belong_order;
	}
	
	public OrderType(){
		super();
	}
	/**
	 * @param ot_type
	 * @param ot_describe
	 * @param ot_belong_module
	 * @param ot_belong_order
	 */
	public OrderType(String ot_type, String ot_describe, String ot_belong_module, String ot_belong_order) {
		super();
		this.ot_type = ot_type;
		this.ot_describe = ot_describe;
		this.ot_belong_module = ot_belong_module;
		this.ot_belong_order = ot_belong_order;
	}
	/**
	 * @param ot_type
	 * @param ot_describe
	 */
	public OrderType(Long ot_id,String ot_type) {
		super();
		this.ot_id = ot_id;
		this.ot_type = ot_type;
	}
}
