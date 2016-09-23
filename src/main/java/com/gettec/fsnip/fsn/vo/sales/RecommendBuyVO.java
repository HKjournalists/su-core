package com.gettec.fsnip.fsn.vo.sales;

import com.gettec.fsnip.fsn.model.sales.RecommendBuy;

public class RecommendBuyVO {

	private Long id;
	private String guid;
	private String name;
	private String way;
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
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	
	public RecommendBuyVO(){}
	
	public RecommendBuyVO(RecommendBuy recBuy){
		if(recBuy == null) {
			return;
		}
		this.id = recBuy.getId();
		this.guid = recBuy.getGuid();
		this.name = recBuy.getName();
		this.way = recBuy.getWay();
	}
	
	public RecommendBuy toEntity(RecommendBuy recBuy){
		if(recBuy == null) {
			recBuy = new RecommendBuy();
		}
		recBuy.setName(this.name);
		recBuy.setWay(this.way);
		return recBuy;
	}
}
