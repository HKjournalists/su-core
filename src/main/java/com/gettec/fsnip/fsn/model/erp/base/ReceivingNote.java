package com.gettec.fsnip.fsn.model.erp.base;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * ReceivingNote Entity<br>
 * 收货单
 * @author Administrator
 */
@Entity(name="t_meta_receivingnote")
public class ReceivingNote extends Model implements Serializable{
	private static final long serialVersionUID = -2602208877291212273L;
	
	@Id
	@Column(name="re_num",length=20,nullable=false)
	private String re_num; //收货单编号
	
	@Column(name="re_date")
	private Date re_date;//收货日期
	
	@Column(name="re_pdate")
	private Date re_pdate;//预计收货日
	
	@Column(name="re_source",length=10, nullable=false)
	private String re_source;//来源
	
	@Column(name="re_outofbill_num",length=50, nullable=false)
	private String re_outofbill_num;//出货单号
	
	@Column(name="organization")
	private Long organization;//组织机构
	
	@Column(name="re_checkman",length=30, nullable=false)
	private String re_checkman;//检验人
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = BusinessUnit.class)
	@JoinColumn(name="re_provide_num", nullable=false)
	private BusinessUnit re_provide_num;  // 供应商
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = ContactInfo.class)
	@JoinColumn(name="re_contact_id", nullable=false)
	private ContactInfo re_contact_id;//联系人id
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Unit.class)
	@JoinColumn(name="re_unit")
	private Unit re_unit;//单位
	
	@Column(name="re_remarks",length=255)
	private String re_remarks;//备注
	
	@Column(name="re_before_pay")
	private float re_before_pay;//预付款
	
	@Column(name="re_totalmoney")
	private float re_totalmoney;//总金额
	
	@Column(name="re_fact_pay")
	private Float re_fact_pay;//实付款
	
	public enum state{已确认,已收货};
	//状态
	@Enumerated(EnumType.STRING)
	private state re_purchase_check;//审核状态（已确认，已收货）
	
	@Column(name="re_user_name",length=20)
	private String userName;//操作人
	
	@Transient
	private List<PurchaseorderInfo> contacts = new ArrayList<PurchaseorderInfo>(); // 商品列表
	
	@Transient
	private List<ReceivingNoteToContactinfo> infos = new ArrayList<ReceivingNoteToContactinfo>(); // 收货单与商品关联列表
	
	public Date getRe_pdate() {
		return re_pdate;
	}

	public void setRe_pdate(Date re_pdate) {
		this.re_pdate = re_pdate;
	}

	public String getRe_remarks() {
		return re_remarks;
	}

	public void setRe_remarks(String re_remarks) {
		this.re_remarks = re_remarks;
	}

	public float getRe_totalmoney() {
		return re_totalmoney;
	}

	public void setRe_totalmoney(float re_totalmoney) {
		this.re_totalmoney = re_totalmoney;
	}

	public Date getRe_date() {
		return re_date;
	}

	public void setRe_date(Date re_date) {
		this.re_date = re_date;
	}
	
	public state getRe_purchase_check() {
		return re_purchase_check;
	}
	
	public void setRe_purchase_check(state re_purchase_check) {
		this.re_purchase_check = re_purchase_check;
	}

	public String getRe_outofbill_num() {
		return re_outofbill_num;
	}

	public void setRe_outofbill_num(String re_outofbill_num) {
		this.re_outofbill_num = re_outofbill_num;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public List<PurchaseorderInfo> getContacts() {
		return contacts;
	}

	public void setContacts(List<PurchaseorderInfo> contacts) {
		this.contacts = contacts;
	}

	public List<ReceivingNoteToContactinfo> getInfos() {
		return infos;
	}

	public void setInfos(List<ReceivingNoteToContactinfo> infos) {
		this.infos = infos;
	}

	public String getRe_num() {
		return re_num;
	}

	public void setRe_num(String re_num) {
		this.re_num = re_num;
	}

	public String getRe_source() {
		return re_source;
	}

	public void setRe_source(String re_source) {
		this.re_source = re_source;
	}

	public String getRe_checkman() {
		return re_checkman;
	}

	public void setRe_checkman(String re_checkman) {
		this.re_checkman = re_checkman;
	}

	public BusinessUnit getRe_provide_num() {
		return re_provide_num;
	}

	public void setRe_provide_num(BusinessUnit re_provide_num) {
		this.re_provide_num = re_provide_num;
	}

	public ContactInfo getRe_contact_id() {
		return re_contact_id;
	}

	public void setRe_contact_id(ContactInfo re_contact_id) {
		this.re_contact_id = re_contact_id;
	}

	public Unit getRe_unit() {
		return re_unit;
	}

	public void setRe_unit(Unit re_unit) {
		this.re_unit = re_unit;
	}

	public float getRe_before_pay() {
		return re_before_pay;
	}

	public void setRe_before_pay(float re_before_pay) {
		this.re_before_pay = re_before_pay;
	}

	public Float getRe_fact_pay() {
		return re_fact_pay;
	}

	public void setRe_fact_pay(Float re_fact_pay) {
		this.re_fact_pay = re_fact_pay;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	
	
	public ReceivingNote(){
		super();
	}
	/**
	 * @param re_num
	 * @param re_checkman
	 * @param re_date
	 */
	public ReceivingNote(String re_num,String re_checkman,Date re_date){
		super();
		this.re_num = re_num;
		this.re_checkman = re_checkman;
		this.re_date = re_date;
	}
	
/*	public void addRelationShipInfo(PurchaseorderInfo info){
		ReceivingNoteToContactinfoPK id = new ReceivingNoteToContactinfoPK(this.re_num,info.getPo_id());
		ReceivingNoteToContactinfo relationship = new ReceivingNoteToContactinfo(id);
		if(info.isDirect()){
			relationship.setIs_direct(true);
		}
		this.infos.add(relationship);
	}*/
	
	public void addPurchaseorderInfo(PurchaseorderInfo info){
		this.contacts.add(info);
	}
}
