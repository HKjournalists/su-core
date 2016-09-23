/**
 * 
 */
package com.tzquery.fsn.vo;

/**
 * 请求参数的VO
 * @author ChenXiaolin 2015-12-1
 */
public class TzQueryRequestParamVO {

	private String busId;           //企业ID
	private String busName;         //企业名称
	private String firstBusId;      //一级企业ID
	private String firstBusName;    //一级企业名称
	private String proId;           //产品ID
	private String proName;         //产品名称
	private String proFirstCategory;//产品分类--只传一级分类的code
	private String proBarcode;      //条形码
	private String provice;         //流通区域--省
	private String city;			//流通区域--市
	private String area;			//流通区域--区
	private String proBatch;		//产品批次
	private String transType;		//交易类型
	private String transSDate;		//交易的起始日期
	private String transEDate;		//交易的截止日期
	private int page;				//第几页
	private int pageSize;			//页大小
	private int salesType;			//0：采购 1：销售
	private String format;			//规格型号
	private String proStatus;		//生产状态
	private String weatherExp;		//规是否出口
	private String licenseNo;		//营业执照号
	
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
	public String getFirstBusId() {
		return firstBusId;
	}
	public void setFirstBusId(String firstBusId) {
		this.firstBusId = firstBusId;
	}
	public String getFirstBusName() {
		return firstBusName;
	}
	public void setFirstBusName(String firstBusName) {
		this.firstBusName = firstBusName;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getProFirstCategory() {
		return proFirstCategory;
	}
	public void setProFirstCategory(String proFirstCategory) {
		this.proFirstCategory = proFirstCategory;
	}
	public String getProBarcode() {
		return proBarcode;
	}
	public void setProBarcode(String proBarcode) {
		this.proBarcode = proBarcode;
	}
	public String getProvice() {
		return provice;
	}
	public void setProvice(String provice) {
		this.provice = provice;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getProBatch() {
		return proBatch;
	}
	public void setProBatch(String proBatch) {
		this.proBatch = proBatch;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransSDate() {
		return transSDate;
	}
	public void setTransSDate(String transSDate) {
		this.transSDate = transSDate;
	}
	public String getTransEDate() {
		return transEDate;
	}
	public void setTransEDate(String transEDate) {
		this.transEDate = transEDate;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
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
	public int getSalesType() {
		return salesType;
	}
	public void setSalesType(int salesType) {
		this.salesType = salesType;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getProStatus() {
		return proStatus;
	}
	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}
	public String getWeatherExp() {
		return weatherExp;
	}
	public void setWeatherExp(String weatherExp) {
		this.weatherExp = weatherExp;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
}
