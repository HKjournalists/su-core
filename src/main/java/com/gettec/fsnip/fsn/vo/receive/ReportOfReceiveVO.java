package com.gettec.fsnip.fsn.vo.receive;

import java.util.Date;

/**
 * 用于接收泊银等其他外部系统的报告数据
 * （包括报告信息、检测项目信息、产品信息、生产企业信息、零售商信息）
 * @author ZhangHui 2015/4/23
 */
public class ReportOfReceiveVO {
	/**
	 * 检测设备序列号
	 */
	private String device_sn;
	/**
	 * 检测设备名称
	 */
	private String device_name;
	/**
	 * 检测用户名
	 */
	private String username;
	/**
	 * 检测部门
	 */
	private String deptname;
	/**
	 * 检测样品名称
	 */
	private String name;
	/**
	 * 检测样品类型
	 */
	private String category_code;
	/**
	 * 检测样品条码
	 */
	private String barcode;
	/**
	 * 检测样品生产厂商
	 */
	private String manufacturer;
	/**
	 * 检测样品商标
	 */
	private String brand;
	/**
	 * 检测样品规格
	 */
	private String spec;
	/**
	 * 检测样品批号
	 */
	private String batch;
	/**
	 * 检测样品生产日期
	 */
	private String manufacture_date;
	/**
	 * 检测时间
	 */
	private Date test_date;
	/**
	 * 检测样品零售商名称
	 */
	private String  retailer;
	/**
	 * 检测样品零售商负责人名称
	 */
	private String person_in_charge;
	/**
	 * 检测样品零售商电话
	 */
	private String phone_in_charge;
	/**
	 * 检测地点
	 */
	private String address;
	/**
	 * 检测环境信息
	 */
	private String env;
	/**
	 * 检测数量
	 */
	private String amount;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 检测样品照片文件名
	 */
	private String attachments;
	/**
	 * 检测数据
	 */
	private SpecimenVO specimendata;
	/**
	 * 检测项目和结论
	 */
	private TestPropertyVO testdata;
	
	public String getDevice_sn() {
		return device_sn;
	}
	public void setDevice_sn(String device_sn) {
		this.device_sn = device_sn;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getManufacture_date() {
		return manufacture_date;
	}
	public void setManufacture_date(String manufacture_date) {
		this.manufacture_date = manufacture_date;
	}
	public Date getTest_date() {
		return test_date;
	}
	public void setTest_date(Date test_date) {
		this.test_date = test_date;
	}
	public String getRetailer() {
		return retailer;
	}
	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}
	public String getPerson_in_charge() {
		return person_in_charge;
	}
	public void setPerson_in_charge(String person_in_charge) {
		this.person_in_charge = person_in_charge;
	}
	public String getPhone_in_charge() {
		return phone_in_charge;
	}
	public void setPhone_in_charge(String phone_in_charge) {
		this.phone_in_charge = phone_in_charge;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	public SpecimenVO getSpecimendata() {
		return specimendata;
	}
	public void setSpecimendata(SpecimenVO specimendata) {
		this.specimendata = specimendata;
	}
	public TestPropertyVO getTestdata() {
		return testdata;
	}
	public void setTestdata(TestPropertyVO testdata) {
		this.testdata = testdata;
	}
}
