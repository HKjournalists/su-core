package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * Unit Entity<br>
 * 计量单位
 * @author Administrator
 */
@SuppressWarnings("serial")
@Entity(name="T_META_UNIT")
public class Unit extends Model {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME", unique=true, length=20)
	private String name;//单位名称

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

	/**
	 * @param name
	 */
	public Unit(String name) {
		super();
		this.name = name;
	}

	/**
	 * @param id
	 * @param name
	 */
	public Unit(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/**
	 * 
	 */
	public Unit() {
		super();
	}
	
	
}
