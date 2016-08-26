package com.gettec.fsnip.fsn.model.erp.base;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * OutOfBill Entity<br>
 * @author Administrator
 */
@Entity(name="T_META_OUT_OF_BILL")
public class OutOfBill extends Model{
	private static final long serialVersionUID = -3720462989994392441L;

	@Id
	@Column(name="OUTOFBILL_NO", length=50)
	private String outOfBillNo;
	
	@Column(name="OUT_DATE")
	private Date outDate;
	
	@Column(name="SOURCE", length=50)
	private String source;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = BusinessUnit.class)
	@JoinColumn(name="CUSTOMER_NO", nullable = true)
	private BusinessUnit customer;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = ContactInfo.class)
	@JoinColumn(name="CONTACTINFO_ID", nullable = false)
	private ContactInfo contactInfo;
	
	//发票
	@Column(name="INVOICE", length=50)
	private String invoice;

	@Column(name="TRANSPORTATION", length=50)
	private String transportation;
	
	@Column(name="NOTE", length=200)
	private String note;
	
	@Column(name="OUT_OF_BILL_STATE", length=50)
	private String outOfBillState;
	
	@Column(name="organization")
	private Long organization;
	
	@Column(name="orgname",length=255)
	private String orgname;
	
	@Column(name="TOTAL_PRICE")
	private double totalPrice;
	
	@Column(name="CONTACT_PROVINCE", length=20)
	private String contactProvince;
	
	@Column(name="CONTACT_CITY", length=20)
	private String contactCity;
	
	@Column(name="CONTACT_AREA", length=20)
	private String contactArea;
	
	@Column(name="CONTACT_ADDR", length=200)
	private String contactAddr;
	
	@Column(name="CONTACT_TEL", length=20)
	private String contactTel;
	
	@Column(name="CONTACT_ZIPCODE", length=20)
	private String contactZipcode;
	
	@Column(name="USER_NAME", length=20)
	private String userName;
	
	public OutOfBill() {
		super();
	}

	public OutOfBill(String outOfBillNo, Date outDate, String source, BusinessUnit customer,
			ContactInfo contactInfo, String invoice, String transportation,
			String note, String outOfBillState, double totalPrice) {
		super();
		this.outOfBillNo = outOfBillNo;
		this.outDate = outDate;
		this.source = source;
		this.customer = customer;
		this.contactInfo = contactInfo;
		this.invoice = invoice;
		this.transportation = transportation;
		this.note = note;
		this.outOfBillState = outOfBillState;
		this.totalPrice = totalPrice;
	}
	
	@Transient
	private List<OutGoodsInfo> contacts = new ArrayList<OutGoodsInfo>();
	@Transient
	private List<OutBillToOutGoods> infos = new ArrayList<OutBillToOutGoods>();
	
	public void addRelationShipInfo(OutGoodsInfo info){
		OutBillToOutGoodsPK id = new OutBillToOutGoodsPK(this.outOfBillNo, info.getId());
		OutBillToOutGoods relationship = new OutBillToOutGoods(id);
		this.infos.add(relationship);
	}
	
	public void addContactInfo(OutGoodsInfo info){
		this.contacts.add(info);
	}
	
	public String getOutOfBillNo() {
		return outOfBillNo;
	}

	public void setOutOfBillNo(String outOfBillNo) {
		this.outOfBillNo = outOfBillNo;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public Long getOrganization() {
		return organization;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getContactProvince() {
		return contactProvince;
	}

	public void setContactProvince(String contactProvince) {
		this.contactProvince = contactProvince;
	}

	public String getContactCity() {
		return contactCity;
	}

	public void setContactCity(String contactCity) {
		this.contactCity = contactCity;
	}

	public String getContactArea() {
		return contactArea;
	}

	public void setContactArea(String contactArea) {
		this.contactArea = contactArea;
	}

	public String getContactAddr() {
		return contactAddr;
	}

	public void setContactAddr(String contactAddr) {
		this.contactAddr = contactAddr;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactZipcode() {
		return contactZipcode;
	}

	public void setContactZipcode(String contactZipcode) {
		this.contactZipcode = contactZipcode;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public BusinessUnit getCustomer() {
		return customer;
	}

	public void setCustomer(BusinessUnit customer) {
		this.customer = customer;
	}

	public String getOutOfBillState() {
		return outOfBillState;
	}

	public void setOutOfBillState(String outOfBillState) {
		this.outOfBillState = outOfBillState;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getTransportation() {
		return transportation;
	}

	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public List<OutGoodsInfo> getContacts() {
		return contacts;
	}

	public void setContacts(List<OutGoodsInfo> contacts) {
		this.contacts = contacts;
	}

	public List<OutBillToOutGoods> getInfos() {
		return infos;
	}

	public void setInfos(List<OutBillToOutGoods> infos) {
		this.infos = infos;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
