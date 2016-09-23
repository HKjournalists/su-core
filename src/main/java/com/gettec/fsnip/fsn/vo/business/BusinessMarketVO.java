package com.gettec.fsnip.fsn.vo.business;

import com.gettec.fsnip.fsn.model.business.BusinessMarket;

/**
 * 根据type来判断产品是否被生产企业接管
 * @author ZhangHui 2015/5/1
 */
public class BusinessMarketVO {
	/**
	 * 企业id
	 */
	private long id;
	/**
	 * 企业名称
	 */
	private String name;
	/**
	 * 企业类型
	 */
	private String type;
	/**
	 * 企业组织机构
	 */
	private long organization;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getOrganization() {
		return organization;
	}
	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	
	public BusinessMarketVO(){}
	
	public BusinessMarketVO(long id, String name, String type, long organization) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.organization = organization;
	}
	
	public void entityToVO(BusinessMarket busMarket){
		this.setId(busMarket.getId());
		this.setName(busMarket.getBusiness()!=null?busMarket.getBusiness().getName():"");
		this.setOrganization(busMarket.getBusiness()!=null?busMarket.getBusiness().getOrganization():null);
	}
}
