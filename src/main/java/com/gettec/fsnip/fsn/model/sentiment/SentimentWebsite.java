package com.gettec.fsnip.fsn.model.sentiment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "t_sentimen_website")
public class SentimentWebsite implements Serializable{

	/**
	 * <p> @字段描述：   						</p>
	 * <p> @创建人：        liangzhiming	</p>
	 * <p> @创建时间：    2015-8-27 下午5:03:18	</p>
	 */
	private static final long serialVersionUID = 6020021698019673401L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="website_name")
	private String websiteName;
	
	@Column(name="website_url")
	private String websiteUrl;
	
	@Column(name="organizationId")
	private Long organizationId;

	@Column(name="organizationName")
	private String organizationName;
	
	@Column(name="status")
	private int status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
