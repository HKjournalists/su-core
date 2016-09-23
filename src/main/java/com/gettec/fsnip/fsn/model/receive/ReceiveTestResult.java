package com.gettec.fsnip.fsn.model.receive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 泊银等外部系统的样品数据
 * @author ZhangHui 2015/4/24
 */
@SuppressWarnings("serial")
@Entity(name = "receive_test_result")
public class ReceiveTestResult extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * 检测报告ID
	 */
	@Column(name = "receive_id")
	private String receive_id;
	
	/**
	 * 检测设备序列号
	 */
	@Column(name = "device_sn")
	private String device_sn;
	
	/**
	 * 检测设备名称
	 */
	@Column(name = "device_name")
	private String device_name;
	
	/**
	 * 检测用户名
	 */
	@Column(name = "username")
	private String username;
	
	/**
	 * 检测部门
	 */
	@Column(name = "deptname")
	private String deptname;
	
	/**
	 * 检测样品名称
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * 检测样品类型代码
	 */
	@Column(name = "type")
	private String type;
	
	/**
	 * 检测样品条码
	 */
	@Column(name = "barcode")
	private String barcode;
	
	/**
	 * 检测样品生产厂商唯一标识
	 */
	@Column(name = "manufacturer_id")
	private String manufacturer_id;
	
	/**
	 * 检测样品生产厂商名称
	 */
	@Column(name = "manufacturer_name")
	private String manufacturer_name;
	
	/**
	 * 检测样品生产厂商营业执照
	 */
	@Column(name = "manufacturer_licenseno")
	private String manufacturer_licenseno;
	
	/**
	 * 检测样品商标
	 */
	@Column(name = "brand")
	private String brand;
	
	/**
	 * 检测样品规格
	 */
	@Column(name = "spec")
	private String spec;
	
	/**
	 * 检测样品批号
	 */
	@Column(name = "batch")
	private String batch;
	
	/**
	 * 检测样品生产日期
	 */
	@Column(name = "manufacture_date")
	private Date manufacture_date;

	/**
	 * 检测时间
	 */
	@Column(name = "test_date")
	private Date test_date;
	
	/**
	 * 检测样品零售商唯一标识
	 */
	@Column(name = "retailer_id")
	private String retailer_id;
	
	/**
	 * 检测样品零售商名称
	 */
	@Column(name = "retailer_name")
	private String retailer_name;

	/**
	 * 检测样品零售商营业执照号
	 */
	@Column(name = "retailer_licenseno")
	private String retailer_licenseno;
	
	/**
	 * 检测样品零售商负责人名称
	 */
	@Column(name = "person_in_charge")
	private String person_in_charge;
	
	/**
	 * 检测样品零售商电话
	 */
	@Column(name = "phone_in_charge")
	private String phone_in_charge;
	
	/**
	 * 检测地点
	 */
	@Column(name = "address")
	private String address;
	
	/**
	 * 检测环境信息
	 */
	@Column(name = "env")
	private String env;
	
	/**
	 * 检测数量
	 */
	@Column(name = "amount")
	private String amount;
	
	/**
	 * 备注
	 */
	@Column(name = "memo")
	private String memo;
	
	/**
	 * 检测报告合格标记
	 */
	@Column(name = "pass")
	private int pass;
	
	/**
	 * 报告来源标识
	 */
	@Column(name = "edition")
	private String edition;
	
	/**
	 * 检测样品照片文件名
	 */
	@Column(name = "attachments")
	private String attachments;
	
	/**
	 * 检测样品照片ftp url
	 */
	@Column(name = "ftp_attachments")
	private String ftp_attachments;
	
	/**
	 * 检测类别
	 */
	@Column(name = "test_type")
	private String test_type;
	
	/**
	 * 样品过期时间
	 */
	@Column(name = "deadline")
	private Date deadline;
	
	/**
	 * 样品生产许可证
	 */
	@Column(name = "qs_no")
	private String qs_no;
	
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date create_time;
	
	/**
	 * 外键：关联test_result
	 */
	@Column(name = "test_result_id")
	private Long test_result_id;
	
	/**
	 * 检测数据
	 */
	@Transient
	private List<ReceiveSpecimendata> recSpecimens = new ArrayList<ReceiveSpecimendata>();
	
	/**
	 * 检测项目和结论
	 */
	@Transient
	private List<ReceiveTestProperty> recTestProperties = new ArrayList<ReceiveTestProperty>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(String receive_id) {
		this.receive_id = receive_id;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getManufacturer_id() {
		return manufacturer_id;
	}

	public void setManufacturer_id(String manufacturer_id) {
		this.manufacturer_id = manufacturer_id;
	}

	public String getManufacturer_name() {
		return manufacturer_name;
	}

	public void setManufacturer_name(String manufacturer_name) {
		this.manufacturer_name = manufacturer_name;
	}

	public String getManufacturer_licenseno() {
		return manufacturer_licenseno;
	}

	public void setManufacturer_licenseno(String manufacturer_licenseno) {
		this.manufacturer_licenseno = manufacturer_licenseno;
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

	public Date getManufacture_date() {
		return manufacture_date;
	}

	public void setManufacture_date(Date manufacture_date) {
		this.manufacture_date = manufacture_date;
	}

	public Date getTest_date() {
		return test_date;
	}

	public void setTest_date(Date test_date) {
		this.test_date = test_date;
	}

	public String getRetailer_id() {
		return retailer_id;
	}

	public void setRetailer_id(String retailer_id) {
		this.retailer_id = retailer_id;
	}

	public String getRetailer_name() {
		return retailer_name;
	}

	public void setRetailer_name(String retailer_name) {
		this.retailer_name = retailer_name;
	}

	public String getRetailer_licenseno() {
		return retailer_licenseno;
	}

	public void setRetailer_licenseno(String retailer_licenseno) {
		this.retailer_licenseno = retailer_licenseno;
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

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public Long getTest_result_id() {
		return test_result_id;
	}

	public void setTest_result_id(Long test_result_id) {
		this.test_result_id = test_result_id;
	}

	public List<ReceiveSpecimendata> getRecSpecimens() {
		return recSpecimens;
	}

	public void setRecSpecimens(List<ReceiveSpecimendata> recSpecimens) {
		this.recSpecimens = recSpecimens;
	}

	public List<ReceiveTestProperty> getRecTestProperties() {
		return recTestProperties;
	}

	public void setRecTestProperties(List<ReceiveTestProperty> recTestProperties) {
		this.recTestProperties = recTestProperties;
	}

	public String getTest_type() {
		return test_type;
	}

	public void setTest_type(String test_type) {
		this.test_type = test_type;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getQs_no() {
		return qs_no;
	}

	public void setQs_no(String qs_no) {
		this.qs_no = qs_no;
	}

	public String getFtp_attachments() {
		return ftp_attachments;
	}

	public void setFtp_attachments(String ftp_attachments) {
		this.ftp_attachments = ftp_attachments;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
