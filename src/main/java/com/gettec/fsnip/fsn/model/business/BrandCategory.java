package com.gettec.fsnip.fsn.model.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 品牌类型
 * @author HuiZhang
 */
@Entity
@Table(name = "brand_category")
public class BrandCategory extends Model{
	private static final long serialVersionUID = -43179401640252302L;
	private Long id = null;
	private String name = null;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", length = 10)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "CATEGORY_NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
