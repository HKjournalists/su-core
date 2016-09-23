package com.gettec.fsnip.fsn.model.sys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="T_SYS_ATTRIBUTES")
public class SystemAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1866786282899688363L;

	@Id
	@Column(name="SYS_ATTR_NAME")
	private String attributeName;
	
	@Column(name="SYS_ATTR_VALUE")
	private String attributeValue;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * @param attributeName
	 * @param attributeValue
	 */
	public SystemAttribute(String attributeName, String attributeValue) {
		super();
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	/**
	 * 
	 */
	public SystemAttribute() {
		super();
	}
	
}
