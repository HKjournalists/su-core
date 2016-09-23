package com.gettec.fsnip.fsn.model.sentiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

@Entity(name="t_sentiment_org_to_topic")
public class OrganizationTopic extends Model {

	/**
	 * <p> @字段描述：   						</p>
	 * <p> @创建人：        liangzhiming	</p>
	 * <p> @创建时间：    2015-8-20 下午4:08:29	</p>
	 */
	private static final long serialVersionUID = -7612038250383426955L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name="organizationId")
	private Long organizationId;
	
	@Column(name="topicId")
	private String topicId;
	
	@Column(name = "organizationName")
	private String organizationName;
	
	@Column(name = "topicName")
	private String topicName;
	
	@Transient
	private String keyword;
	
	@Transient
	private String searchWords;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}


	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSearchWords() {
		return searchWords;
	}

	public void setSearchWords(String searchWords) {
		this.searchWords = searchWords;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	

}
