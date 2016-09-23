package com.gettec.fsnip.fsn.model.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * TestPropertyCategory Entity<br>
 * 
 * 
 * @author Ryan Wang
 */
@Entity(name = "test_property_category")
public class TestPropertyCategory extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2062886548616541135L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "code", length = 20)
	private String code;

	@Column(name = "name", length = 50)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
