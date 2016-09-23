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
 * InitializeProduct Entity<br>
 * 商品初始化表
 * @author Administrator
 */
@Entity(name="t_meta_initialize_product")
public class InitializeProduct extends Model {
	private static final long serialVersionUID = 1201032556782606230L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Product.class)
	@JoinColumn(name="product_id", nullable = false)
	private Product product;
	
	@Column(name="organization",nullable = false)
	private Long organization;
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= StorageInfo.class)
	@JoinColumn(name="first_storage_id", nullable = true)
	private StorageInfo firstStorage;    //first storage, 首选存储仓库
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= MerchandiseType.class)
	@JoinColumn(name="TYPE_ID", nullable = true)
	private MerchandiseType type;//type 商品类型
	
	@Column(name="SAFE_NUMBER",nullable = true)
	private Long safeNumber; //safeNumber 商品安全库存
	
	@Column(name="INSPECTION_REPORT")
	private Boolean inspectionReport; // inspectionReport 是否需要质检报告
	
	@Column(name="Local")
	private Boolean isLocal = false; // inspectionReport 是否需要质检报告
	
	@Column(name="DEL")
	private Boolean isDel; // inspectionReport 是否需要质检报告
	
	@Transient
	private String selectedCustomerNames;  // 列举所有的销往客户名称


	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}
	
	public StorageInfo getFirstStorage() {
		return firstStorage;
	}

	public void setFirstStorage(StorageInfo firstStorage) {
		this.firstStorage = firstStorage;
	}

	public MerchandiseType getType() {
		return type;
	}

	public void setType(MerchandiseType type) {
		this.type = type;
	}

	public Long getSafeNumber() {
		return safeNumber;
	}

	public void setSafeNumber(Long safeNumber) {
		this.safeNumber = safeNumber;
	}

	public Boolean getInspectionReport() {
		return inspectionReport;
	}

	public void setInspectionReport(Boolean inspectionReport) {
		this.inspectionReport = inspectionReport;
	}
	public Boolean getIsLocal() {
		return isLocal;
	}
	
	public void setIsLocal(Boolean isLocal) {
		this.isLocal = isLocal;
	}
	
	public String getSelectedCustomerNames() {
		return selectedCustomerNames;
	}

	public void setSelectedCustomerNames(String selectedCustomerNames) {
		this.selectedCustomerNames = selectedCustomerNames;
	}
	
	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}

	/**
	 * @param id
	 * @param product
	 * @param organization
	 */
	public InitializeProduct(Long id, Product product,
			StorageInfo firstStorage,MerchandiseType type,Long safeNumber,Boolean inspectionReport,Boolean isLocal,Long organization) {
		super();
		this.id = id;
		this.product = product;
		this.firstStorage = firstStorage;
		this.type = type;
		this.safeNumber = safeNumber;
		this.inspectionReport =inspectionReport;
		this.isLocal=isLocal;
		this.organization = organization;
	}

	public InitializeProduct() {
		super();
	}
}
