package com.gettec.fsnip.fsn.vo.account;

import java.util.Date;

/**
 * 进货,批发,收货 公用显示的VO
 * @author chenxiaolin
 */
public class PurchaseAccountVO {
    private Long id;
    private String num;//单据标号
    private String outBusName;//供应商名称
    private String inBusName;//本企业名称
    private String licNo;//供应商营业执照号
    private String buylicNo;//购货商营业执照号
    private String createDate;//创建时间
    private String barcode;//条形码
    private Integer outStatus;//收货确认状态 0:为确认 1：确认
    private Integer inStatus;//收货确认状态 0:为确认 1：确认
    private Long outBusId;//供应商ID
    private Long inBusId;//供应商ID
    private Integer type;//进货状态
    private int returnStatus;//退货状态
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getOutBusName() {
		return outBusName;
	}
	public void setOutBusName(String outBusName) {
		this.outBusName = outBusName;
	}
	public String getInBusName() {
		return inBusName;
	}
	public void setInBusName(String inBusName) {
		this.inBusName = inBusName;
	}
	public String getLicNo() {
		return licNo;
	}
	public void setLicNo(String licNo) {
		this.licNo = licNo;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	public Long getOutBusId() {
		return outBusId;
	}
	public void setOutBusId(Long outBusId) {
		this.outBusId = outBusId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getInBusId() {
		return inBusId;
	}
	public void setInBusId(Long inBusId) {
		this.inBusId = inBusId;
	}
	public String getBuylicNo() {
		return buylicNo;
	}
	public void setBuylicNo(String buylicNo) {
		this.buylicNo = buylicNo;
	}
	public int getReturnStatus() {
		return returnStatus;
	}
	public void setReturnStatus(int returnStatus) {
		this.returnStatus = returnStatus;
	}
	
}
