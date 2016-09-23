package com.gettec.fsnip.fsn.vo.sales;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.sales.Contract;
import com.gettec.fsnip.fsn.model.sales.SalesResource;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * 电子合同VO
 * @author tangxin 2015/04/29
 */
public class ContractVO {
	
	private Long id;
	private String guid;
	private String name;
	private String remark;
	private Date updateTime;
	private SalesResource resource;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public SalesResource getResource() {
		return resource;
	}
	public void setResource(SalesResource resource) {
		this.resource = resource;
	}
	public ContractVO(){}
	
	public ContractVO(Contract contract){
		this.id = contract.getId();
		this.guid = contract.getGuid();
		this.name = contract.getName();
		this.remark = contract.getRemark();
		this.updateTime = contract.getUpdateTime();
	}
	
	public Contract toEntity(Contract contract){
		if(contract == null) {
			contract = new Contract();
		}
		contract.setName(this.name);
		contract.setRemark(this.remark);
		return contract;
	}
}
