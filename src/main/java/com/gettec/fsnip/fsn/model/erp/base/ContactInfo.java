package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * ContactInfo Entity<br>
 * 联系人信息
 * @author Administrator
 */
@Entity(name="T_META_CONTACT_INFO")
public class ContactInfo extends Model implements Serializable {
	private static final long serialVersionUID = 2462331656470921935L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;//用户ID
	
	@Column(name="NAME", length=50)
	private String name;//用户名称
	
	@Column(name="TEL_1", length=20)
	private String tel_1;//手机
	
	@Column(name="TEL_2", length=20)
	private String tel_2;//座机
	
	@Column(name="EMAIL", length=50)
	private String email;//电子邮箱
	
	@Column(name="IM_ACCOUNT", length=20)
	private String im_account;//即时通讯账号
	
	@Column(name="ADDR", length=100)
	private String addr;//地址
	
	@Column(name="ZIP_CODE", length=6)
	private String zipcode;//邮编
	
	@Column(name="PROVINCE", length=50)
	private String province;
	
	@Column(name="CITY", length=50)
	private String city;
	
	@Column(name="AREA", length=50)
	private String area;
	
	@Column(name="organization")
	private Long organization;//用户ID
	
	@Transient
	private boolean isDirect;
	
	
	public boolean isDirect() {
		return isDirect;
	}
	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	
	public Long getOrganization() {
		return organization;
	}
	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel_1() {
		return tel_1;
	}
	public void setTel_1(String tel_1) {
		this.tel_1 = tel_1;
	}
	public String getTel_2() {
		return tel_2;
	}
	public void setTel_2(String tel_2) {
		this.tel_2 = tel_2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIm_account() {
		return im_account;
	}
	public void setIm_account(String im_account) {
		this.im_account = im_account;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
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
	/**
	 * @param id
	 * @param name
	 * @param tel_1
	 * @param tel_2
	 * @param email
	 * @param im_account
	 * @param addr
	 * @param zipcode
	 * @param isDirect
	 */
	public ContactInfo(Long id, String name, String tel_1, String tel_2,
			String email, String im_account, String zipcode, String addr,
			boolean isDirect, String province, String city, String area) {
		super();
		this.id = id;
		this.name = name;
		this.tel_1 = tel_1;
		this.tel_2 = tel_2;
		this.email = email;
		this.im_account = im_account;
		this.zipcode = zipcode;
		this.addr = addr;
		this.isDirect = isDirect;
		this.province = province;
		this.city = city;
		this.area = area;
		}
	
	
	/**
	 * @param name
	 * @param tel_1
	 * @param tel_2
	 * @param email
	 * @param im_account
	 * @param addr
	 * @param zipcode
	 * @param isDirect
	 */
	public ContactInfo(String name, String tel_1, String tel_2, String email,
			String im_account, String addr, String zipcode, boolean isDirect,
			String province, String city, String area) {
		super();
		this.name = name;
		this.tel_1 = tel_1;
		this.tel_2 = tel_2;
		this.email = email;
		this.im_account = im_account;
		this.addr = addr;
		this.zipcode = zipcode;
		this.isDirect = isDirect;
		this.province = province;
		this.city = city;
		this.area = area;
	}
	/**
	 * 
	 */
	public ContactInfo() {
		super();
	}
	
	public void updateContactInfo(ContactInfo info){
		this.name = info.getName();
		this.tel_1 = info.getTel_1();
		this.tel_2 = info.getTel_2();
		this.email = info.getEmail();
		this.zipcode = info.getZipcode();
		this.im_account = info.getIm_account();
		this.addr = info.getAddr();
		this.isDirect = info.isDirect();
		this.province = info.getProvince();
		this.city = info.getCity();
		this.area = info.getArea();
	}
}
