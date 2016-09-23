package com.gettec.fsnip.fsn.vo.sales;

import java.util.Date;
import java.util.List;

import com.gettec.fsnip.fsn.model.sales.PromotionCase;
import com.gettec.fsnip.fsn.model.sales.SalesResource;

/**
 * 促销活动 VO
 * @author tangxin 2015-05-03
 *
 */
public class PromotionCaseVO {

	private Long id;
	private String guid;
	private String name;
	private String area;
	private Date startDate;
	private Date endDate;
	private String introduction;
	private List<SalesResource> resource;
	
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public List<SalesResource> getResource() {
		return resource;
	}
	public void setResource(List<SalesResource> resource) {
		this.resource = resource;
	}
	
	public PromotionCaseVO(){}
	
	public PromotionCaseVO(PromotionCase promotion) {
		if(promotion == null) {
			return;
		}
		this.id = promotion.getId();
		this.guid = promotion.getGuid();
		this.name = promotion.getName();
		this.area = promotion.getArea();
		this.introduction = promotion.getIntroduction();
		this.startDate = promotion.getStartDate();
		this.endDate = promotion.getEndDate();
	}
	
	public PromotionCase toEntity(PromotionCase promotion){
		if(promotion == null) {
			promotion = new PromotionCase();
		}
		promotion.setName(this.name);
		promotion.setArea(this.area);
		promotion.setIntroduction(this.introduction);
		promotion.setStartDate(this.startDate);
		promotion.setEndDate(this.endDate);
		return promotion;
	}
}
