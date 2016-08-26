package com.gettec.fsnip.fsn.model.erp.buss;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

@Entity(name="T_BUSS_STORAGE_LOG")
public class StorageChangeLog extends Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4681734832994821751L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="MERCHANDISE_NAME")
	private String merchandiseName;//商品名称
	
	@Column(name="BATCH_NUMBER")//批次号
	private String batch_number;
	
	@Column(name="OPERATION_COUNT")
	private int count;//操作数量
	
	@Column(name="STORAGE_1")
	private String storage_1;//出入库目标仓库
	
	@Column(name="STORAGE_2")
	private String storage_2;//调入仓库
	
	@Column(name="BUSINESS_TYPE")
	private String businessType;//类型
	
	@Column(name="SOURCE_NO")
	private String sourceNo;//来源编号
	
	@Column(name="LOG_TIME")
	private Date logTime = new Date();
	
	@Column(name="USER_NAME")
	private String userName;

	@Column(name="organization")
	private Long organization;
	
	@Transient
	private String desc;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getMerchandiseName() {
		return merchandiseName;
	}

	public void setMerchandiseName(String merchandiseName) {
		this.merchandiseName = merchandiseName;
	}

	public String getBatch_number() {
		return batch_number;
	}

	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	public String getStorage_1() {
		return storage_1;
	}

	public void setStorage_1(String storage_1) {
		this.storage_1 = storage_1;
	}

	public String getStorage_2() {
		return storage_2;
	}

	public void setStorage_2(String storage_2) {
		this.storage_2 = storage_2;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @param id
	 * @param merchandiseName
	 * @param batch_number
	 * @param storage_1
	 * @param storage_2
	 * @param businessType
	 * @param sourceNo
	 * @param logTime
	 * @param userName
	 */
	public StorageChangeLog(Long id, String merchandiseName,
			String batch_number, String storage_1, String storage_2,
			String businessType, String sourceNo, Date logTime, String userName) {
		super();
		this.id = id;
		this.merchandiseName = merchandiseName;
		this.batch_number = batch_number;
		this.storage_1 = storage_1;
		this.storage_2 = storage_2;
		this.businessType = businessType;
		this.sourceNo = sourceNo;
		this.logTime = logTime;
		this.userName = userName;
	}

	/**
	 * @param merchandiseName
	 * @param batch_number
	 * @param storage_1
	 * @param storage_2
	 * @param businessType
	 * @param sourceNo
	 * @param logTime
	 * @param userName
	 */
	public StorageChangeLog(String merchandiseName, String batch_number,
			String storage_1, String storage_2, String businessType,
			String sourceNo, int count, String userName) {
		super();
		this.merchandiseName = merchandiseName;
		this.batch_number = batch_number;
		this.storage_1 = storage_1;
		this.storage_2 = storage_2;
		this.businessType = businessType;
		this.sourceNo = sourceNo;
		this.userName = userName;
		this.count = count;
	}

	/**
	 * 
	 */
	public StorageChangeLog() {
		super();
	}
	
	
}
