package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * MerchandiseType Entity<br>
 * 商品类别
 * @author Administrator
 */
@Entity(name="T_META_MERCHANDISE_TYPE")
public class MerchandiseType extends Model {
	private static final long serialVersionUID = 5875369752667993755L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME",unique=true, length=20)
	private String name;//商品类别名称

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

	/**
	 * @param name
	 */
	public MerchandiseType(String name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 */
	public MerchandiseType() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 */
	public MerchandiseType(Long id, String name) {
		super();
		this.setId(id);
		this.name = name;
	}
	
	public MerchandiseType(Long id, String name, Long organization) {
		super();
		this.setId(id);
		this.name = name;
		this.organization = organization;
	}
}
