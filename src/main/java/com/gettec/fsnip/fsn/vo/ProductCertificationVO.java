package com.gettec.fsnip.fsn.vo;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;

public class ProductCertificationVO {
	
	private Long id;  //企业其他认证实体id
	private String name; //认证名称
	private String url;  //认证图片的路径
	private Date endDate; //认证信息截止日期
	private String fileName; //认证图片名称
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public Date getEndDate() {
		return endDate;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
