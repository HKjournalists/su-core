package com.tzapp.fsn.vo;

import javax.servlet.http.HttpServletRequest;

public class TzAppRequestParamVO {

	private Long id;//收货单ID
	private Long proId;//产品ID
	private String outBusName;//供应商名称
	private String outLicNo;//供应商营业执照号
	private String dealDate;//交易日期
	private String busId;//当前企业ID
	private String busName;//当前企业名称
	private String barcode;
	private String sBusName;
	private int page;
	private int pageSize;
	private String sBarcode;
	private String sLicNo;
	private String sInDate;
	private Integer sInStatus;
	private String refuseReason;//拒收原因
	private Long curOrg;//当前登录企业的组织机构ID
	private int status;//状态 ：用于收货查询和退货查询时显示字段不一样时的动态处理 0:收货查询 1：退货查询
	private String sName;//搜索时的产品名称
	private int publishFlag;//审核状态
	private Integer sStatus;
	private String outOrg;
	private int isTotal;//是否是报告总数 0：报告总数 1：待审核报告数
	private int type;//拒收或报告退回标识 1：报告退回 2：拒收
	private HttpServletRequest requset;
	private boolean scan;//是否是扫描查询 true:扫描查询  false:搜索查询
	private int width;            // 图片的宽
	private int height;           // 图片的高
	private Long reportId;        // 报告ID
	private Long acInfoId;        // 台账信息ID
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOutBusName() {
		return outBusName;
	}
	public void setOutBusName(String outBusName) {
		this.outBusName = outBusName;
	}
	public String getOutLicNo() {
		return outLicNo;
	}
	public void setOutLicNo(String outLicNo) {
		this.outLicNo = outLicNo;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getsBusName() {
		return sBusName;
	}
	public void setsBusName(String sBusName) {
		this.sBusName = sBusName;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getsBarcode() {
		return sBarcode;
	}
	public void setsBarcode(String sBarcode) {
		this.sBarcode = sBarcode;
	}
	public String getsLicNo() {
		return sLicNo;
	}
	public void setsLicNo(String sLicNo) {
		this.sLicNo = sLicNo;
	}
	public String getsInDate() {
		return sInDate;
	}
	public void setsInDate(String sInDate) {
		this.sInDate = sInDate;
	}
	public Integer getsInStatus() {
		return sInStatus;
	}
	public void setsInStatus(Integer sInStatus) {
		this.sInStatus = sInStatus;
	}
	public Long getProId() {
		return proId;
	}
	public void setProId(Long proId) {
		this.proId = proId;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public Long getCurOrg() {
		return curOrg;
	}
	public void setCurOrg(Long curOrg) {
		this.curOrg = curOrg;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public int getPublishFlag() {
		return publishFlag;
	}
	public void setPublishFlag(int publishFlag) {
		this.publishFlag = publishFlag;
	}
	public Integer getsStatus() {
		return sStatus;
	}
	public void setsStatus(Integer sStatus) {
		this.sStatus = sStatus;
	}
	public String getOutOrg() {
		return outOrg;
	}
	public void setOutOrg(String outOrg) {
		this.outOrg = outOrg;
	}
	public int getIsTotal() {
		return isTotal;
	}
	public void setIsTotal(int isTotal) {
		this.isTotal = isTotal;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public HttpServletRequest getRequset() {
		return requset;
	}
	public void setRequset(HttpServletRequest requset) {
		this.requset = requset;
	}
	public boolean isScan() {
		return scan;
	}
	public void setScan(boolean scan) {
		this.scan = scan;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public Long getAcInfoId() {
		return acInfoId;
	}
	public void setAcInfoId(Long acInfoId) {
		this.acInfoId = acInfoId;
	}
	
}
