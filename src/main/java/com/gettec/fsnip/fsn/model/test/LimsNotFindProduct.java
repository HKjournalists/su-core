package com.gettec.fsnip.fsn.model.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * LimsNotFindProduct Entity<br>
 * lims没有找到产品的报告
 * 
 * @author LongXianZhen
 */
@Entity(name = "lims_not_find_product")
public class LimsNotFindProduct extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2062886548616541135L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "proName")
	private String proName;
	
	@Column(name = "barcode")
	private String barcode;
	
	@Column(name = "jsonURL")
	private String jsonURL;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getJsonURL() {
		return jsonURL;
	}

	public void setJsonURL(String jsonURL) {
		this.jsonURL = jsonURL;
	}
	
}
