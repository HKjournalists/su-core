package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * BusinessType Entity<br>
 * 采购/销售/入库/出库/调度/
 * @author Administrator
 */
@Entity(name="T_META_BUSINESS_TYPE")
public class BusinessType extends Model {
	private static final long serialVersionUID = 5560848119459893935L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME",unique=true, length=50)
	private String name;
	
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
	public BusinessType(String name) {
		super();
		this.name = name;
	}

	/**
	 * 
	 */
	public BusinessType() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + id);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BusinessType other = (BusinessType) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public BusinessType(Long id, String name, Long organization) {
		super();
		this.id = id;
		this.name = name;
		this.organization = organization;
	}
	
	
}
