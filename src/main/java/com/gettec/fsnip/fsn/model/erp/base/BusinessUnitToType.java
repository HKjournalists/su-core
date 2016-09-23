package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * BusinessUnitToType Entity<br>
 * 企业-企业类型
 * @author Administrator
 */
@Entity(name="t_meta_business_diy_type")
public class BusinessUnitToType extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2784787415373896633L;
	@EmbeddedId
	private BusinessUnitToTypePK id;
	
	@Column(name="organization")
	private Long organization;//组织机构id
	
	@Column(name="type")
	private Long type;//1代表客户、2代表供应商

	public BusinessUnitToTypePK getId() {
		return id;
	}

	public void setId(BusinessUnitToTypePK id) {
		this.id = id;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	
	public BusinessUnitToType(){
		super();
	}
	
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public BusinessUnitToType(BusinessUnitToTypePK id){
		super();
		this.id = id;
	}
	
}
