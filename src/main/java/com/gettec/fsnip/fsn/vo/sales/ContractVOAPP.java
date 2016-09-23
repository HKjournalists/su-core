package com.gettec.fsnip.fsn.vo.sales;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * 企业APP 获取电子合同返回的VO
 * @author tangxin 2015-05-10
 *
 */
public class ContractVOAPP {

	private Long id;
	private String name;
	private Long attachmentsId;
	private String attachmentsUrl;
	private Date updateTime;
	
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
	public Long getAttachmentsId() {
		return attachmentsId;
	}
	public void setAttachmentsId(Long attachmentsId) {
		this.attachmentsId = attachmentsId;
	}
	public String getAttachmentsUrl() {
		return attachmentsUrl;
	}
	public void setAttachmentsUrl(String attachmentsUrl) {
		this.attachmentsUrl = attachmentsUrl;
	}
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
