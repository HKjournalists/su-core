package com.tzapp.fsn.vo;

/**
 * 快速收货扫描，手动输入,过滤搜索
 * 收货扫描，收货搜素,审核搜索及扫描 公用的请求参数VO
 */
public class TzAppSearchAndScanVO {

	private Long org;//当前组织机构ID
	private String scanBarcode;//扫描条形码
	private String searchBarcode;//条件搜索条形码
	private Long curBusId;////当前企业ID
	private String curBusName;//当前企业名称
	private Integer isReceipt;//是否是收货查询0：收货查询 0：退货查询
	private Integer isScan;//是否是扫描 0：扫描 1：搜索
	private String sProName;//产品名称
	private String sBusName;//供应商名称
	private String sLicNo;//供应营业执照号
	private String sDealDate;//处理日期（进货日期）
	private Integer sStatus;//收货状态0：待收货 1：已收货 2：已拒收(处理状态 0：未确认 1：已确认或报告审核状态：0:待审核 1：审核已通过 2：审核未通过)
	private Integer isTotal;//是否是报告总数 0：是 1：不是
	private int page;//第几页
	private int pageSize;//页大小
	private Long reportId;//报告ID
	private Integer publishFlag;//商超审核通过状态
	private String returnReason;//报告退回原因
	private boolean pass;//报告审核标识 true:审核通过标识 false：审核退回标识
	private Integer confirmTotal;//是否是已确认收货总数 0：是 1：不是
	private int width;            //图片的宽
	private int height;           //图片的高
	
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public Integer getPublishFlag() {
		return publishFlag;
	}
	public void setPublishFlag(Integer publishFlag) {
		this.publishFlag = publishFlag;
	}
	public Long getOrg() {
		return org;
	}
	public void setOrg(Long org) {
		this.org = org;
	}
	public String getScanBarcode() {
		return scanBarcode;
	}
	public void setScanBarcode(String scanBarcode) {
		this.scanBarcode = scanBarcode;
	}
	public String getSearchBarcode() {
		return searchBarcode;
	}
	public void setSearchBarcode(String searchBarcode) {
		this.searchBarcode = searchBarcode;
	}
	public Long getCurBusId() {
		return curBusId;
	}
	public void setCurBusId(Long curBusId) {
		this.curBusId = curBusId;
	}
	public String getCurBusName() {
		return curBusName;
	}
	public void setCurBusName(String curBusName) {
		this.curBusName = curBusName;
	}
	public Integer getIsReceipt() {
		return isReceipt;
	}
	public void setIsReceipt(Integer isReceipt) {
		this.isReceipt = isReceipt;
	}
	public Integer getIsScan() {
		return isScan;
	}
	public void setIsScan(Integer isScan) {
		this.isScan = isScan;
	}
	public String getsProName() {
		return sProName;
	}
	public void setsProName(String sProName) {
		this.sProName = sProName;
	}
	public String getsBusName() {
		return sBusName;
	}
	public void setsBusName(String sBusName) {
		this.sBusName = sBusName;
	}
	public String getsLicNo() {
		return sLicNo;
	}
	public void setsLicNo(String sLicNo) {
		this.sLicNo = sLicNo;
	}
	public String getsDealDate() {
		return sDealDate;
	}
	public void setsDealDate(String sDealDate) {
		this.sDealDate = sDealDate;
	}
	public Integer getsStatus() {
		return sStatus;
	}
	public void setsStatus(Integer sStatus) {
		this.sStatus = sStatus;
	}
	public Integer getIsTotal() {
		return isTotal;
	}
	public void setIsTotal(Integer isTotal) {
		this.isTotal = isTotal;
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
	public Integer getConfirmTotal() {
		return confirmTotal;
	}
	public void setConfirmTotal(Integer confirmTotal) {
		this.confirmTotal = confirmTotal;
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
	
}
