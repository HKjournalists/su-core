package com.gettec.fsnip.fsn.vo.dishs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

import com.gettec.fsnip.fsn.model.market.Resource;

public class DishsNoShowVO {
    
	private Long id;          //菜品ID
	private Long showId;   //今日菜品ID 
	private String dishsName;     //菜品名称
	private String alias;	 //别名
	private String baching;	 //配料
	private Long qiyeId;     //企业ID
	private String remark;    //备注信息
	private String showFlag;//是否显示:0表示否;1表示是
	private String sampleFlag;//已留样:0否;1是
	private String showTime; //展示日期
	private Long resourceId; //菜品图片id
	
	private Resource dishsnoFile;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getShowId() {
		return showId;
	}
	public void setShowId(Long showId) {
		this.showId = showId;
	}
	public String getDishsName() {
		return dishsName;
	}
	public void setDishsName(String dishsName) {
		this.dishsName = dishsName;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getBaching() {
		return baching;
	}
	public void setBaching(String baching) {
		this.baching = baching;
	}
	public Long getQiyeId() {
		return qiyeId;
	}
	public void setQiyeId(Long qiyeId) {
		this.qiyeId = qiyeId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}
	public String getSampleFlag() {
		return sampleFlag;
	}
	public void setSampleFlag(String sampleFlag) {
		this.sampleFlag = sampleFlag;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public Resource getDishsnoFile() {
		return dishsnoFile;
	}
	public void setDishsnoFile(Resource dishsnoFile) {
		this.dishsnoFile = dishsnoFile;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	
}
