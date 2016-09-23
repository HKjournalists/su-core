package com.gettec.fsnip.fsn.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gettec.fsnip.fsn.model.base.Nutrition;
import com.gettec.fsnip.fsn.model.common.Model;

@Entity(name="nutri_rpt")
public class ProductNutrition extends Model {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "product_id", nullable=false)
	private Long productId;
	
	@Column(name = "name", length=30)
	private String name; // 营养名称
	
	@Column(name = "unit", length=30)
	private String unit; // 每个计算单位中的营养值
	
	@Column(name = "value", length=30)
	private String value;
	
	@Column(name = "per", length=30)
	private String per;   // 计算单位如每100ml,每份
	
	@Column(name = "nrv", length=25)
	private String nrv;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nutri_id", nullable = true)
	private Nutrition nutrition;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public String getNrv() {
		return nrv;
	}

	public void setNrv(String nrv) {
		this.nrv = nrv;
	}

	public Nutrition getNutrition() {
		return nutrition;
	}

	public void setNutrition(Nutrition nutrition) {
		this.nutrition = nutrition;
	}
}
