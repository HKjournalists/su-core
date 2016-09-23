package com.gettec.fsnip.fsn.model.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.product.Product;

@Entity(name = "product_visit_statistics")
public class ProductVisitStatistics extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9123707751003352113L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Transient
	private Long productId;
	
	@Column(name = "app_statistics")
	private Long appStatistics;
	
	@Column(name = "portal_statistics")
	private Long portalStatistics;

	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getAppStatistics() {
		return appStatistics;
	}

	public void setAppStatistics(Long appStatistics) {
		this.appStatistics = appStatistics;
	}

	public Long getPortalStatistics() {
		return portalStatistics;
	}

	public void setPortalStatistics(Long portalStatistics) {
		this.portalStatistics = portalStatistics;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
}
