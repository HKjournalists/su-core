/**
 * 
 */
package com.tzquery.fsn.vo;

/**
 * 企业信息返回的VO
 * @author ChenXiaolin 2015-12-2
 */
public class TzQueryResponseBusVO {

	private String proId;         //产品ID
	private String proName;         //产品名称
	private String proFirstCategory;//产品分类--只传一级分类的code
	private String proBarcode;      //条形码
	private String provice;         //流通区域--省
	private String city;			//流通区域--市
	private String area;			//流通区域--区
	private String busName;			//企业名称
	private String firstBusName;    //一级企业名称
	private String busLic;			//营业执照号
	private String busType;			//企业类型
	private String busAddress;		//企业地址
	private String proRelation;		//产品关系
	private String busId;			//企业ID
	
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
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
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public String getFirstBusName() {
		return firstBusName;
	}
	public void setFirstBusName(String firstBusName) {
		this.firstBusName = firstBusName;
	}
	public String getBusLic() {
		return busLic;
	}
	public void setBusLic(String busLic) {
		this.busLic = busLic;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBusAddress() {
		return busAddress;
	}
	public void setBusAddress(String busAddress) {
		this.busAddress = busAddress;
	}
	public String getProRelation() {
		return proRelation;
	}
	public void setProRelation(String proRelation) {
		this.proRelation = proRelation;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	
}
