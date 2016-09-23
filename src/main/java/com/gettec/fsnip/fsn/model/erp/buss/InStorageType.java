package com.gettec.fsnip.fsn.model.erp.buss;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * InStorageType Entity<br>
 * 入库类型: 采购入库/盘盈/其他
 * @author Administrator
 */
@Entity(name="T_META_IN_STORAGE_TYPE")
public class InStorageType extends Model {
	private static final long serialVersionUID = -7550298006098002099L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME",unique=true, length=50)
	private String name;//入库类型名称
	
	@Column(name="organization")
	private Long organization;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param name
	 */
	public InStorageType(String name) {
		super();
		this.name = name;
	}

	/**
	 * 
	 */
	public InStorageType() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 */
	public InStorageType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public InStorageType(Long id, String name, Long organization) {
		super();
		this.id = id;
		this.name = name;
		this.organization = organization;
	}
}
