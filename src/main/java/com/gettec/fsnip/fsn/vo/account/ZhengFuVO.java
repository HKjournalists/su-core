package com.gettec.fsnip.fsn.vo.account;

/**
 * 政府所属VO
 * @author chenxiaolin
 */
public class ZhengFuVO {
    private Long id;
    private String serviceOrder;//报告编号
    private String name;//样品名称
    private String batchSerialNo;//批次
    private String testType;//检测类型
    private String publishDate;//发布日期
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServiceOrder() {
		return serviceOrder;
	}
	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBatchSerialNo() {
		return batchSerialNo;
	}
	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
}
