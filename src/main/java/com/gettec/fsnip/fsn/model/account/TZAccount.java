package com.gettec.fsnip.fsn.model.account;

import com.gettec.fsnip.fsn.model.common.Model;

import javax.persistence.*;

/**
 * Account Entity<br>
 * @author
 */

@Entity(name = "tz_business_account")
public class TZAccount extends Model{

	private static final long serialVersionUID = 5774720641197657539L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "account_no")
	private String accountNo;     // 单据编号

	@Column(name = "out_business_id")
	private Long outBusinessId;	 //发货企业ID

	@Column(name = "in_business_id")
	private Long inBusinessId;	 //收货企业ID

	@Column(name = "create_time")
	private String createTime;     //创建时间

	@Column(name = "out_status")
	private Integer outStatus;  // 发货状态，0：未确认，1：确认

	@Column(name = "in_status")
	private Integer inStatus;     // 收货状态，0：未确认，1：确认

	@Column(name = "return_status")
	private int returnStatus;     // 收货台账中是否退货 0:未退回 1:已退回

	@Column(name = "out_date")
	private String outDate;        // 发货日期

	@Column(name = "in_date" )
	private String inDate;  // 发货日期

	@Column(name = "type")
	private Integer type;  // 类型，0：默认，1：表示自己进货

	@Column(name = "refuseReason")
	private String refuseReason;//拒收原因（针对台账App）

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Long getOutBusinessId() {
		return outBusinessId;
	}

	public void setOutBusinessId(Long outBusinessId) {
		this.outBusinessId = outBusinessId;
	}

	public Long getInBusinessId() {
		return inBusinessId;
	}

	public void setInBusinessId(Long inBusinessId) {
		this.inBusinessId = inBusinessId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getOutStatus() {
		return outStatus;
	}

	public void setOutStatus(Integer outStatus) {
		this.outStatus = outStatus;
	}

	public Integer getInStatus() {
		return inStatus;
	}

	public void setInStatus(Integer inStatus) {
		this.inStatus = inStatus;
	}

	public int getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

}