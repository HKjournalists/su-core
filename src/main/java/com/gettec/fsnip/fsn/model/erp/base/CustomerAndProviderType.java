package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * CustomerAndProviderType Entity<br>
 * 客户、供应商类型
 * @author Administrator
 */
@Entity(name = "t_meta_customer_and_provider_type")
public class CustomerAndProviderType extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 266360272571867791L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NAME",unique=true, length=50)
	private String name;//客户类别名称
	
	@Column(name="type")
	private Integer type;//1代表客户类型，2代表供应商类型
	
	@Column(name="organization")
	private Long organization;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	
	public CustomerAndProviderType(){
		super();
	}
	
	public CustomerAndProviderType(Long id,String name,Integer type){
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public CustomerAndProviderType(Long id,String name){
		super();
		this.id = id;
		this.name = name;
	}
	
	public CustomerAndProviderType(String name){
		super();
		this.name = name;
	}
	
	public CustomerAndProviderType(Long id,String name,Integer type,Long organization){
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.organization = organization;
	}
}
