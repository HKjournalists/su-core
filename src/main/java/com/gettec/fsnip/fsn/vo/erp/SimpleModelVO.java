package com.gettec.fsnip.fsn.vo.erp;

import java.io.Serializable;

public class SimpleModelVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3180654363729738313L;

	private Long id;
	private String name;
	private boolean active;
	
	private String updateName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	
	public String getUpdateName() {
		return updateName;
	}
	
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * @param id
	 * @param name
	 * @param active
	 */
	public SimpleModelVO(Long id, String name, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.active = active;
	}
	/**
	 * @param id
	 * @param name
	 * @param active
	 */
	public SimpleModelVO() {
		super();
	}
	
}
