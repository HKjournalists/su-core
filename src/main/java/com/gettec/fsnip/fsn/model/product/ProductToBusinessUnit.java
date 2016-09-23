package com.gettec.fsnip.fsn.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * ProductToBusinessUnit Entity<br>
 * 企业-产品-qs 关系表 
 * @author ZhangHui 2015/6/4
 */
@Entity(name = "product_to_businessUnit")
public class ProductToBusinessUnit extends Model{
	
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * 外键： product id
	 */
	@Column(name = "PRODUCT_ID")
	private Long product_id;

	/**
	 * 外键：production_license_info id
	 */
	@Column(name = "qs_id")
	private Long qs_id;
	
	/**
	 * 外键：business_unit id
	 */
	@Column(name = "business_id")
	private Long business_id;

	/**
	 * 表示此qs号是否是生产企业给产品绑定的
	 * 		0 代表 是流通企业在录报告时，给产品关联的qs号
	 * 		1 代表 是生产企业在产品新增/编辑时，给产品绑定的qs号
	 */
	@Column(name = "bind")
	private int bind;
	
	/**
	 * 表示此qs号被生产企业绑定的事实是否有效
	 * 		0 代表 当bind=1 时，此时被生产企业绑定的事实无效（此时qs号的授权已经被收回）
	 * 		1 代表 当bind=1 时，此时被生产企业绑定的事实有效（此时该生产企业拥有该qs的使用权）
	 */
	@Column(name = "effect")
	private int effect = 1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public Long getQs_id() {
		return qs_id;
	}

	public void setQs_id(Long qs_id) {
		this.qs_id = qs_id;
	}

	public Long getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(Long business_id) {
		this.business_id = business_id;
	}

	public int getBind() {
		return bind;
	}

	public void setBind(int bind) {
		this.bind = bind;
	}

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}
}