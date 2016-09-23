package com.gettec.fsnip.fsn.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * kms提供的产品标签
 * @author TangXin
 */
@Entity(name="t_std_kms_label")
public class KmsLabel extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="kms_label_id")
	private Long kmsLabelId;
	
	@Column(name="label_name")
	private String labelName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getKmsLabelId() {
		return kmsLabelId;
	}

	public void setKmsLabelId(Long kmsLabelId) {
		this.kmsLabelId = kmsLabelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
}