package com.gettec.fsnip.fsn.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * Product Entity<br>
 * 
 * 
 * @author WuBiao
 */
@Entity(name = "product_recommend_url")
public class ProductRecommendUrl extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "url_name")
	private String urlName;
	
	@Column(name = "pro_id")
	private Long proId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "pro_url")
	private String proUrl;
	
	@Column(name = "identify")
	private String identify;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProId() {
		return proId;
	}

	public void setProId(Long proId) {
		this.proId = proId;
	}
	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProUrl() {
		return proUrl;
	}

	public void setProUrl(String proUrl) {
		this.proUrl = proUrl;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}
	
}