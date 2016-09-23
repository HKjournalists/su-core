package com.gettec.fsnip.fsn.model.erp.buss;

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
import com.gettec.fsnip.fsn.model.erp.base.BusinessType;
import com.gettec.fsnip.fsn.model.erp.base.MerchandiseInstance;
import com.gettec.fsnip.fsn.model.erp.base.StorageInfo;
import com.gettec.fsnip.fsn.model.erp.base.Unit;

/**
 * 业务_2_商品记录
 * @author Administrator
 */
@Entity(name="T_BUSS_BUSINESS_TO_MERCHANDISES")
public class BussToMerchandises extends Model{
	private static final long serialVersionUID = 1803980393210447808L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;

	@Column(name="NO_1", length = 50)
	private String no_1;//业务编号
	
	@Column(name="NO_2", length = 50)
	private Long no_2;//商品实例编号
	
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = BusinessType.class)
	@JoinColumn(name="TYPE_ID")
	private BusinessType type;//业务类型
	
	@Column(name="DISPLAY_NAME", length=100)
	private String displayName; //展示名称: 商品编号_商品名称_商品规格
	
	@Column(name="COUNT")
	private int count; //数量
	
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Unit.class)
	@JoinColumn(name="UNIT", nullable = true)
	private Unit unit;//单位
	
	@Column(name="PRICE")
	private double price;//单价
	
	@Column(name="AMOUNT")
	private double amount;//总价
	/*
	 * 仓库1
	 * 采购:预计入库仓库 
	 * 销售:预计出库仓库
	 * 调拨:调入仓库
	 */
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = StorageInfo.class)
	@JoinColumn(name="STORAGE_1")
	private StorageInfo storage_1;
	
	/*
	 * 仓库2
	 * 采购:实际入库仓库 
	 * 销售:实际出库仓库
	 * 调拨:调出仓库
	 */
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = StorageInfo.class)
	@JoinColumn(name="STORAGE_2")
	private StorageInfo storage_2;

	@Column(name="batch_number", length=100)
	private String batch_number;
	
	@Column(name="unit_name", length=100)
	private String unit_name;
	
	@Transient
	private Long merchandiseNo;
	
	@Transient
	private MerchandiseInstance instance;
	
	public MerchandiseInstance getInstance() {
		return instance;
	}

	public void setInstance(MerchandiseInstance instance) {
		this.instance = instance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNo_1() {
		return no_1;
	}

	public void setNo_1(String no_1) {
		this.no_1 = no_1;
	}

	public Long getNo_2() {
		return no_2;
	}

	public void setNo_2(Long no_2) {
		this.no_2 = no_2;
	}

	public BusinessType getType() {
		return type;
	}

	public void setType(BusinessType type) {
		this.type = type;
	}

	public String getBatch_number() {
		return batch_number;
	}

	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public Long getMerchandiseNo() {
		return merchandiseNo;
	}

	public void setMerchandiseNo(Long merchandiseNo) {
		this.merchandiseNo = merchandiseNo;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public StorageInfo getStorage_1() {
		return storage_1;
	}

	public void setStorage_1(StorageInfo storage_1) {
		this.storage_1 = storage_1;
	}

	public StorageInfo getStorage_2() {
		return storage_2;
	}

	public void setStorage_2(StorageInfo storage_2) {
		this.storage_2 = storage_2;
	}

	/**
	 * @param id
	 * @param displayName
	 * @param count
	 * @param unit
	 * @param price
	 * @param amount
	 * @param storage_1
	 * @param storage_2
	 */
	public BussToMerchandises(Long id, String displayName,
			int count, Unit unit, double price, double amount,
			StorageInfo storage_1, StorageInfo storage_2,String batch_number,String unit_name) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.count = count;
		this.unit = unit;
		this.price = price;
		this.amount = amount;
		this.storage_1 = storage_1;
		this.storage_2 = storage_2;
		this.batch_number = batch_number;
		this.unit_name = unit_name;
	}

	/**
	 * 
	 */
	public BussToMerchandises() {
		super();
	}
	
	
}
