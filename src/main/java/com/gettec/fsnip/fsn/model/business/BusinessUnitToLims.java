package com.gettec.fsnip.fsn.model.business;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 定制版LIMS往FSN新增的企业信息
 * （BusinessUnit外键关联与此表） 
 * @author Xin Tang
 */
@Entity(name = "business_unit_to_lims")
public class BusinessUnitToLims {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "edition")
	private String edition; //环境：XX LIMS
	
	@Column(name = "create_user")
	private String createByUser; //创建者名称 
	
	@Column(name = "create_time")
	private Date createTime; //创建日期

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getCreateByUser() {
		return createByUser;
	}

	public void setCreateByUser(String createByUser) {
		this.createByUser = createByUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
