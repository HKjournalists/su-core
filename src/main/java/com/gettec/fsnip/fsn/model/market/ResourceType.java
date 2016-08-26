package com.gettec.fsnip.fsn.model.market;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="T_SYS_RESOURCE_TYPE")
public class ResourceType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3974154614138738699L;
	
	@Id
	@Column(name="RESOURCE_TYPE_ID")
	private Long rtId;
	
	@Column(name="RESOURCE_TYPE_NAME")
	private String rtName;
	
	@Column(name="RESOURCE_TYPE_DESC")
	private String rtDesc;
	
	@Column(name="CONTENT_TYPE")
	private String contentType;
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Long getRtId() {
		return rtId;
	}
	public void setRtId(Long rtId) {
		this.rtId = rtId;
	}
	public String getRtName() {
		return rtName;
	}
	public void setRtName(String rtName) {
		this.rtName = rtName;
	}
	public String getRtDesc() {
		return rtDesc;
	}
	public void setRtDesc(String rtDesc) {
		this.rtDesc = rtDesc;
	}
	public ResourceType(Long rtId, String rtName, String rtDesc) {
		super();
		this.rtId = rtId;
		this.rtName = rtName;
		this.rtDesc = rtDesc;
	}
	public ResourceType() {
		super();
	}
	
	@Override
	public int hashCode() {
		if(null == this.getRtId()){
			return super.hashCode();
		}
		return this.getRtId().intValue() * 31;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ResourceType) && (null != ((ResourceType)obj).getRtId() && ((ResourceType)obj).getRtId().intValue() == this.getRtId().intValue());
	}
	
	
}
