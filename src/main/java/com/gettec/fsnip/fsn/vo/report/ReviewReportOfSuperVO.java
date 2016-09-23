package com.gettec.fsnip.fsn.vo.report;

import java.util.Date;

/**
 * 用于封装商超报告查看界面的报告字段
 * @author ZhangHui 2015/4/9
 */
public class ReviewReportOfSuperVO {
	/**
	 * 报告id
	 */
	private long id;
	
	/**
	 * 报告编号
	 */
	private String serviceOrder;
	
	/**
	 * 批次
	 */
	private String batchSerialNo;
	
	/**
	 * 生产企业名称
	 */
	private String producerName;
	
	/**
	 * 生产日期
	 */
	private Date productionDate;
	
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
	 * pdf的web存放完整路径
	 */
	private String pdfUrl;
	
	/**
	 * 创建当前报告的企业组织机构
	 */
	private Long organization;
	
	/**
	 * 对报告的操作权利
	 * 		true  代表 当前商超可以操作该报告（退回或通过），为默认值
	 * 		false 代表 当前商超不可以操作该报告
	 */
	private boolean operateRight;
	
	private String checkOrgName;

	public String getCheckOrgName() {
		return checkOrgName;
	}

	public void setCheckOrgName(String checkOrgName) {
		this.checkOrgName = checkOrgName;
	}

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

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public boolean isOperateRight() {
		return operateRight;
	}

	public void setOperateRight(boolean operateRight) {
		this.operateRight = operateRight;
	}

	public ReviewReportOfSuperVO() {
		super();
	}

	public ReviewReportOfSuperVO(long id, String serviceOrder,
			String batchSerialNo, String producerName, Date productionDate,
			String publishFlag, String pdfUrl, Long organization, int operateRight_status,String checkOrgName) {
		super();
		this.id = id;
		this.serviceOrder = serviceOrder;
		this.batchSerialNo = batchSerialNo;
		this.producerName = producerName;
		this.productionDate = productionDate;
		this.publishFlag = publishFlag;
		this.pdfUrl = pdfUrl;
		this.organization = organization;
		this.checkOrgName=checkOrgName;
		
		if(operateRight_status == 0){
			this.operateRight = true;
		}
	}
}
