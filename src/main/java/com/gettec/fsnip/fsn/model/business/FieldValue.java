package com.gettec.fsnip.fsn.model.business;

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
 * 表单元素值
 * @author Hui Zhang
 */
@Entity(name = "fields_value")
public class FieldValue extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "value")
	private String value;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "field_id", nullable = true)
	private Field field;
	
	@Column(name = "template_id")
	private Long templateId;
	
	@Column(name = "busunit_id")
	private Long busunitId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Long getBusunitId() {
		return busunitId;
	}

	public void setBusunitId(Long busunitId) {
		this.busunitId = busunitId;
	}
}
