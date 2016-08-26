package com.gettec.fsnip.fsn.model.erp.buss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import com.gettec.fsnip.fsn.model.common.Model;
/**
 * 
 * 商品出库记录
 *
 */
@Entity(name="T_BUSS_OUT_STORAGE_RECORD")
public class OutStorageRecord extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = -421103312504029076L;

	@Id
	@Column(name="NO", length = 50)
	private String no;//商品出库单号
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = OutStorageType.class)
	@JoinColumn(name = "TYPE_ID", nullable = false)
	private OutStorageType type;//出库类型
	
	@Column(name="CREATE_TIME")
	private Date createTime;//创建时间
	
	@Column(name="CREATE_USER_ID")
	private Long createUserID;//创建人ID
	
	@Column(name="CREATE_USER_NAME", length=20)
	private String createUserName;//创建人姓名
	
	@Column(name="NOTE", length = 200)
	private String note;//备注
	
	@Column(name="organization", length = 200)
	private Long organization;//备注

	@Transient
	private List<BussToMerchandises> merchandises = new ArrayList<BussToMerchandises>();
	
	public List<BussToMerchandises> getMerchandises() {
		return merchandises;
	}
	
	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public void setMerchandises(List<BussToMerchandises> merchandises) {
		this.merchandises = merchandises;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public OutStorageType getType() {
		return type;
	}

	public void setType(OutStorageType type) {
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
