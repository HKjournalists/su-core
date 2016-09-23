package com.gettec.fsnip.fsn.vo.report;

import java.util.Date;

/**
 * 用于封装testlab审核报告界面的机构化检测报告字段
 * @author ZhangHui 2015/5/6
 */
public class StructuredReportOfTestlabVO {
	/**
	 * 报告id
	 */
	private long id;
	
	/**
	 * 报告编号
	 */
	private String serviceOrder;
	
	/**
	 * 样品名称
	 */
	private String sampleName;
	
	/**
	 * 条形码
	 */
	private String barcode;
	
	/**
	 * 批次
	 */
	private String batchSerialNo;
	
	/**
	 * 生产企业名称
	 */
	private String producerName;
	
	/**
	 * 检测类型
	 */
	private String testType;
	
	/**
	 * 结构化人员
	 */
	private String operator;
	
	/**
	 * testlab报告接收时间
	 */
	private Date receiveDate;
	
	/**
	 * 报告的发布状态
	 * 		0 代表 testlab正在审核;
	 *      1 代表 testlab通过审核;
	 *      2 代表 testlab退回;
	 *      3 代表 录入人员提交，但未被发布人员发布到testlab;
	 *      4 代表 经销商提交但未通过商超审核;
	 *      5 代表 商超退回;
	 *      6 代表 商超审核通过，但未被食安云完善;
	 */
	private String publishFlag;
	
	/**
	 * 报告的结构化状态
	 * 		1 代表待结构化;
	 * 		2 代表结构化完成
	 * 		4 代表testlab退回
	 * 		8 代表终极审核通过
	 */
	private int status;
	
	/**
	 * 报告的组织机构
	 */
	private long organization;
	/**
	 * 待审核结构化检测报告退回次数
	 */
	private int backCount = 0;
	
	private int suppliersBackCount=0;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}
	
	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public long getOrganization() {
		return organization;
	}

	public void setOrganization(long organization) {
		this.organization = organization;
	}

	public StructuredReportOfTestlabVO() {
		super();
	}

	public StructuredReportOfTestlabVO(long id, String serviceOrder,
			String sampleName, String batchSerialNo, String producerName,
			String testType, String operator, Date receiveDate,
			String publishFlag, int status, long organization) {
		super();
		this.id = id;
		this.serviceOrder = serviceOrder;
		this.sampleName = sampleName;
		this.batchSerialNo = batchSerialNo;
		this.producerName = producerName;
		this.testType = testType;
		this.operator = operator;
		this.receiveDate = receiveDate;
		this.publishFlag = publishFlag;
		this.status = status;
		this.organization = organization;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getBackCount() {
		return backCount;
	}

	public void setBackCount(int backCount) {
		this.backCount = backCount;
	}

	public int getSuppliersBackCount() {
		return suppliersBackCount;
	}

	public void setSuppliersBackCount(int suppliersBackCount) {
		this.suppliersBackCount = suppliersBackCount;
	}
	
}
