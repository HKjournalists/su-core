package com.gettec.fsnip.fsn.model.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 表单元素
 * （目前只用于委托登记单模块）
 * @author Hui Zhang
 */
@Entity(name = "fields")
public class Field extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "field_name")
	private String fieldName;
	
	@Column(name = "display")
	private String display;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}
