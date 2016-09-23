package com.gettec.fsnip.fsn.model.erp.base;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.product.Product;

/**
 * PurchaseorderInfo Entity<br>
 * @author Administrator
 */
@Entity(name = "t_meta_purchaseorder_info")
public class PurchaseorderInfo extends Model{
	private static final long serialVersionUID = -8655929824465370900L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="po_id", nullable=false)
	private Long po_id;
	
	@Column(name="po_ismode")
	private boolean po_ismode;//样品否
	
	@Column(name="po_isgift")
	private boolean po_isgift;//赠品否
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Product.class)
	@JoinColumn(name="po_product_info_id",referencedColumnName="barcode", nullable=true)
	private Product product;//商品信息id
	
	@Column(name="po_receivenum")
	private long po_receivenum;//收货数量
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Unit.class)
	@JoinColumn(name="po_unit", nullable=false)
	private Unit po_unit;//单位
	
	@Column(name="po_fact_receivenum")
	private long po_fact_receivenum;//实收数量
	
	@Column(name="po_batch",length=30,nullable = false)
	private String po_batch;//批次
	
	@Column(name="po_chbackmoney")
	private float po_chbackmoney;//验退金额

	@Column(name="po_price",nullable=false)
	private float po_price;//单价
	
	@Column(name="po_isneedqr")
	private boolean po_isneedqr;//是否需要质检报告
	
	@Column(name="hasreport",length=50)
	private Boolean hasreport;//是否有质检报告
	
	@Column(name="po_remark",length=50)
	private String po_remark;//备注
	
	@Column(name="po_mtype",length=50)
	private String po_mtype;//币种
	
	@Column(name="po_storage_address",length=100,nullable = false)
	private String po_storage_address;//仓库地址
	
	@Transient
	private boolean isDirect;
	
	
	
	public String getPo_batch() {
		return po_batch;
	}

	public void setPo_batch(String po_batch) {
		this.po_batch = po_batch;
	}
	public String getPo_mtype() {
		return po_mtype;
	}

	public void setPo_mtype(String po_mtype) {
		this.po_mtype = po_mtype;
	}

	public String getPo_storage_address() {
		return po_storage_address;
	}

	public void setPo_storage_address(String po_storage_address) {
		this.po_storage_address = po_storage_address;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public boolean isDirect() {
		return isDirect;
	}

	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

	public Long getPo_id() {
		return po_id;
	}

	public void setPo_id(Long po_id) {
		this.po_id = po_id;
	}

	public boolean isPo_ismode() {
		return po_ismode;
	}

	public void setPo_ismode(boolean po_ismode) {
		this.po_ismode = po_ismode;
	}

	public boolean isPo_isgift() {
		return po_isgift;
	}

	public void setPo_isgift(boolean po_isgift) {
		this.po_isgift = po_isgift;
	}

	public long getPo_receivenum() {
		return po_receivenum;
	}

	public void setPo_receivenum(long po_receivenum) {
		this.po_receivenum = po_receivenum;
	}

	public Unit getPo_unit() {
		return po_unit;
	}

	public void setPo_unit(Unit po_unit) {
		this.po_unit = po_unit;
	}

	public long getPo_fact_receivenum() {
		return po_fact_receivenum;
	}

	public void setPo_fact_receivenum(long po_fact_receivenum) {
		this.po_fact_receivenum = po_fact_receivenum;
	}

	public float getPo_chbackmoney() {
		return po_chbackmoney;
	}

	public void setPo_chbackmoney(float po_chbackmoney) {
		this.po_chbackmoney = po_chbackmoney;
	}

	public float getPo_price() {
		return po_price;
	}

	public void setPo_price(float po_price) {
		this.po_price = po_price;
	}

	public boolean isPo_isneedqr() {
		return po_isneedqr;
	}

	public void setPo_isneedqr(boolean po_isneedqr) {
		this.po_isneedqr = po_isneedqr;
	}

	public Boolean getHasreport() {
		return hasreport;
	}

	public void setHasreport(Boolean hasreport) {
		this.hasreport = hasreport;
	}

	public String getPo_remark() {
		return po_remark;
	}

	public void setPo_remark(String po_remark) {
		this.po_remark = po_remark;
	}
	
	public PurchaseorderInfo(){
		super();
	}
	/**
	 * @param po_id
	 * @param po_ismode
	 * @param po_isgift
	 * @param po_product_info_id
	 * @param po_receivenum
	 * @param po_unit
	 * @param po_fact_receivenum
	 * @param po_rangediscound
	 * @param po_discoundrate
	 * @param po_disbackmoney
	 * @param po_price
	 * @param po_isneedqr
	 * @param po_qualityreport
	 * @param po_remark
	 * @param po_batch
	 * @param po_storage_address
	 */
	public PurchaseorderInfo(long po_id,boolean po_ismode,boolean po_isgift,Product product,long po_receivenum,Unit po_unit,long po_fact_receivenum,
			float po_price,boolean po_isneedqr,boolean hasreport,String po_remark,String batch,String po_storage_address,String po_mtype,boolean isDirect){
		super();
		this.po_id = po_id;
		this.po_ismode = po_ismode;
		this.po_isgift = po_isgift;
		this.product = product;
		this.po_receivenum = po_receivenum;
		this.po_unit = po_unit;
		this.po_fact_receivenum = po_fact_receivenum;
		this.po_price = po_price;
		this.po_isneedqr = po_isneedqr;
		this.hasreport = hasreport;
		this.po_remark = po_remark;
		this.po_batch = batch;
		this.po_storage_address = po_storage_address;
		this.po_mtype = po_mtype;
	}
}
