package com.gettec.fsnip.fsn.model.erp.buss;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;
/**
 * 
 * 出库类型:
 * 销售出库/盘亏/其他
 *
 */
@Entity(name="T_META_OUT_STORAGE_TYPE")
public class OutStorageType extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6026253039893031801L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME",unique=true, length=50)
	private String name;//出库类型名称

	@Column(name="organization")
	private Long organization;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	/**
	 * @param name
	 */
	public OutStorageType(String name) {
		super();
		this.name = name;
	}

	/**
	 * 
	 */
	public OutStorageType() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 */
	public OutStorageType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public OutStorageType(Long id, String name, Long organization) {
		super();
		this.id = id;
		this.name = name;
		this.organization = organization;
	}
}
