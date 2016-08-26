package com.gettec.fsnip.fsn.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 基础表（同fdams）
 * @author Administrator
 */
@Entity(name = "sys_dict")
public class District extends Model{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "label")
	private String label;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "sort")
	private int sort;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
