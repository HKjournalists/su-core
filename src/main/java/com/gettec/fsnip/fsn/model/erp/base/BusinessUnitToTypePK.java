package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.gettec.fsnip.fsn.model.common.Model;

@Embeddable
public class BusinessUnitToTypePK extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5488603153115067448L;
	
	@Column(name="business_id")
	private Long businessId;
	
	@Column(name="type_id")
	private Long typeId;

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public BusinessUnitToTypePK(){
		super();
	}
	
	public BusinessUnitToTypePK(Long businessId,Long typeId){
		super();
		this.businessId = businessId;
		this.typeId = typeId;
	}
	

}
