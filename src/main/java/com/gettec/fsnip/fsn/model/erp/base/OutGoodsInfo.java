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

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;

/**
 * OutGoodsInfo Entity<br>
 * 商品信息
 * @author Administrator
 */
@Entity(name="T_META_OUT_GOODS_INFO")
public class OutGoodsInfo extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8437682859589134384L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NO", length=20)
	private String no;  // 商品barcode
	
	@Column(name="NAME", length=50)
	private String name;//商品名称
	
	@Column(name="MODE")
	private boolean mode;
	
	@Column(name="INSPECTION_REPORT")
	private boolean inspectionReport;
	
	@Column(name="HAS_INSPECTION_REPORT")
	private boolean hasInspectionReport;
	
	@Column(name="OUT_NUMBER")
	private Long outNumber;
	
	@Column(name="SPECIFICATION", length=20)
	private String specification;//商品规格
	
	@Column(name="UNIT_PRICE")
	private double unitPrice;//单价
	
	@Column(name="MONEY_TYPE", length=20)
	private String moneyType;
	
	@Column(name="TOTAL_AMOUNT")
	private double totalAmount;
	
	@Column(name="NOTE", length=200)
	private String note;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= StorageInfo.class)
	@JoinColumn(name="first_storage_id", nullable = false)
	private StorageInfo firstStorage;//first storage, 首选存储仓库
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= Unit.class)
	@JoinColumn(name="UNIT_ID", nullable = false)
	private Unit unit;//unit 单位
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= MerchandiseType.class)
	@JoinColumn(name="TYPE_ID", nullable = false)
	private MerchandiseType type;//type 商品类型
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = ProductCategoryInfo.class)
	@JoinColumn(name="category", nullable = false)
	private ProductCategoryInfo category;
	
	@Column(name="BATCH_NO", length=20)
	private String batchNo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public boolean isInspectionReport() {
		return inspectionReport;
	}
	public void setInspectionReport(boolean inspectionReport) {
		this.inspectionReport = inspectionReport;
	}
	public boolean isHasInspectionReport() {
		return hasInspectionReport;
	}
	public void setHasInspectionReport(boolean hasInspectionReport) {
		this.hasInspectionReport = hasInspectionReport;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public StorageInfo getFirstStorage() {
		return firstStorage;
	}
	public void setFirstStorage(StorageInfo firstStorage) {
		this.firstStorage = firstStorage;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public MerchandiseType getType() {
		return type;
	}
	public void setType(MerchandiseType type) {
		this.type = type;
	}
	
	public ProductCategoryInfo getCategory() {
		return category;
	}
	public void setCategory(ProductCategoryInfo category) {
		this.category = category;
	}
	public boolean isMode() {
		return mode;
	}
	public void setMode(boolean mode) {
		this.mode = mode;
	}
	public Long getOutNumber() {
		return outNumber;
	}
	public void setOutNumber(Long outNumber) {
		this.outNumber = outNumber;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public OutGoodsInfo() {
		super();
	}
	
	public OutGoodsInfo(Long id, String no, String name, boolean mode, Long outNumber, String specification,
			double unitPrice, String moneyType, double totalAmount, String note, StorageInfo firstStorage, 
			ProductCategoryInfo category, Unit unit, MerchandiseType type, String batchNo) {
		super();
		this.id = id;
		this.no = no;
		this.name = name;
		this.mode = mode;
		this.outNumber = outNumber;
		this.specification = specification;
		this.unitPrice = unitPrice;
		this.moneyType = moneyType;
		this.firstStorage = firstStorage;
		this.category = category;
		this.unit = unit;
		this.type = type;
		this.batchNo = batchNo;
		this.totalAmount = totalAmount;
		this.note = note;
	}
	
	public OutGoodsInfo(Long id, String no, String name, boolean mode, Long outNumber, String specification,
			double unitPrice, String moneyType, double totalAmount, String note, StorageInfo firstStorage, 
			ProductCategoryInfo category, Unit unit, MerchandiseType type, String batchNo,
			boolean inspectionReport, boolean hasInspectionReport) {
		super();
		this.id = id;
		this.no = no;
		this.name = name;
		this.mode = mode;
		this.outNumber = outNumber;
		this.specification = specification;
		this.unitPrice = unitPrice;
		this.moneyType = moneyType;
		this.firstStorage = firstStorage;
		this.category = category;
		this.unit = unit;
		this.type = type;
		this.batchNo = batchNo;
		this.totalAmount = totalAmount;
		this.note = note;
		this.inspectionReport = inspectionReport;
		this.hasInspectionReport = hasInspectionReport;
	}
}
