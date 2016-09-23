package com.gettec.fsnip.fsn.model.erp.buss;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * FlittingOrder Entity<br>
 * 商品调拨单
 * @author Administrator
 */
@Entity(name="T_BUSS_FLITTING_ORDER")
public class FlittingOrder extends Model{
	private static final long serialVersionUID = 3702273497213504157L;

	@Id
	@Column(name="NO", length = 50)
	private String no;//商品调拨单号
	
	@Column(name="CREATE_TIME")
	private Date createTime;//创建时间
	
	@Column(name="CREATE_USER_ID")
	private Long createUserID;//创建人ID
	
	@Column(name="CREATE_USER_NAME", length=20)
	private String createUserName;//创建人姓名
	
	@Column(name="NOTE", length = 200)
	private String note;//备注
	
	@Column(name="organization", length = 200)
	private Long organization;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
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

	/**
	 * @param no
	 * @param createTime
	 * @param createUserID
	 * @param createUserName
	 * @param note
	 */
	public FlittingOrder(String no, Date createTime, Long createUserID,
			String createUserName, String note) {
		super();
		this.no = no;
		this.createTime = createTime;
		this.createUserID = createUserID;
		this.createUserName = createUserName;
		this.note = note;
	}

	/**
	 * @param createTime
	 * @param createUserID
	 * @param createUserName
	 * @param note
	 */
	public FlittingOrder(Date createTime, Long createUserID,
			String createUserName, String note) {
		super();
		this.createTime = createTime;
		this.createUserID = createUserID;
		this.createUserName = createUserName;
		this.note = note;
	}

	/**
	 * 
	 */
	public FlittingOrder() {
		super();
	}
	
	
}
