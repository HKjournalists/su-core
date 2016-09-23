package com.gettec.fsnip.fsn.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

/**
 * 基础表-国家认证
 * @author Administrator
 */
@Entity(name = "certification")
public class Certification extends Model{
	private static final long serialVersionUID = 9068999504293633535L;
	@Id @GeneratedValue
	private Long id;
	
	@Column(name="imgurl", nullable=true, length=255)
	private String imgUrl;
	
	@Column(name="message", nullable=true, length=255) 
	private String message;
	
	@Column(name="name", nullable=true, length=100) 
	private String name;
	
	@Column(name = "std_status")
	private Integer stdStatus; //标准认证的状态，0是标准认证，1是用户自己添加的认证。

	@Transient
	private String documentUrl;

	@Transient
	private Resource certIconResource; //荣誉证书小图标

	public Resource getCertIconResource() {
		return certIconResource;
	}

	public void setCertIconResource(Resource certIconResource) {
		this.certIconResource = certIconResource;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}
	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStdStatus() {
		return stdStatus;
	}
	public void setStdStatus(Integer stdStatus) {
		this.stdStatus = stdStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Certification [id=" + id + ", imgUrl=" + imgUrl + ", message="
				+ message + ", name=" + name + "]";
	}
	public Certification() {
		super();
	}
	public Certification(Long id, String imgUrl, String message, String name,
			String documentUrl) {
		super();
		this.id = id;
		this.imgUrl = imgUrl;
		this.message = message;
		this.name = name;
		this.documentUrl = documentUrl;
	}
}
