package com.gettec.fsnip.fsn.model.erp.buss;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * InStorageRecord Entity<br>
 * 商品入库记录
 * @author Administrator
 */
@Entity(name="T_BUSS_IN_STORAGE_RECORD")
public class InStorageRecord extends Model{
	private static final long serialVersionUID = -4611825350236848232L;

	@Id
	@Column(name="NO", length = 50)
	private String no;//商品入库单号
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = InStorageType.class)
	@JoinColumn(name = "TYPE_ID", nullable = false)
	private InStorageType type;//入库类型
	
	@Column(name="CREATE_TIME")
	private Date createTime;//创建时间
	
	@Column(name="CREATE_USER_ID")
	private Long createUserID;//创建人ID
	
	@Column(name="CREATE_USER_NAME", length=20)
	private String createUserName;//创建人姓名
	
	@Column(name="NOTE", length = 200)
	private String note;//备注
	
	@Column(name="organization", length = 200)
	private Long organization;//org

	public String getNo() {
		return no;
	}
	
	
	
	public Long getOrganization() {
		return organization;
	}



	public void setOrganization(Long organization) {
		this.organization = organization;
	}



	public void setNo(String no) {
		this.no = no;
	}

	public InStorageType getType() {
		return type;
	}

	public void setType(InStorageType type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(Long createUserID) {
		this.createUserID = createUserID;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
	
}
