package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * StorageInfo Entity<br>
 * 仓库信息
 * @author Administrator
 */
@Entity(name="T_META_STORAGE_INFO")
public class StorageInfo extends Model {
	private static final long serialVersionUID = -3850721960403066728L;

	@Id
	@Column(name="NO",length=20)
	private String no; //仓库编号
	
	@Column(name="NAME",length=50)
	private String name; //仓库名称
	
	@Column(name="manager",length=20)
	private String manager; //仓库管理员
	
	@Column(name="organization")
	private Long organization;
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public Long getOrganization() {
		return organization;
	}
	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param no
	 * @param name
	 * @param active
	 */
	public StorageInfo(String no, String name) {
		super();
		this.no = no;
		this.name = name;
	}
	
	public StorageInfo(String no, String name,String manager, Long organization) {
		super();
		this.no = no;
		this.name = name;
		this.manager = manager;
		this.organization = organization;
	}
	
	/**
	 * 
	 */
	public StorageInfo() {
		super();
	}
	
	
}
