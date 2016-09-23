package com.gettec.fsnip.fsn.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

@Entity(name = "product_sales")
public class ProductSales extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
		
	@Column(name = "batch_serial_no")
	private String batchSerialNo;     //产品批次号

	@Column(name = "target_customer")
	private String targetCustomer;  //销售对象
	
	@Column(name = "sales_territory")
	private String salesTerritory;  //销售地区
	
	@Column(name = "sales_quantity")
	private String salesQuantity;   //销售数量
		
	@Column(name = "sales_date")
	private Date salesDate;  // 发布时间
	
	@Column(name = "pArea_id")
	private int pAreaId; 
	
	@Column(name = "mArea_id")
	private int mAreaId; 
	
	@Column(name = "cArea_id")
	private int cAreaId; 

	@Column(name = "organization")
	private Long organizationId;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getSalesDate() {
		return salesDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public Long getOrganizationId() {
		return organizationId;
	}
	
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public int getpAreaId() {
		return pAreaId;
	}

	public void setpAreaId(int pAreaId) {
		this.pAreaId = pAreaId;
	}

	public int getmAreaId() {
		return mAreaId;
	}

	public void setmAreaId(int mAreaId) {
		this.mAreaId = mAreaId;
	}

	public int getcAreaId() {
		return cAreaId;
	}

	public void setcAreaId(int cAreaId) {
		this.cAreaId = cAreaId;
	}

	@Transient
	private Long productId;
	
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id", nullable=false)
	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public String getTargetCustomer() {
		return targetCustomer;
	}

	public void setTargetCustomer(String targetCustomer) {
		this.targetCustomer = targetCustomer;
	}

	public String getSalesTerritory() {
		return salesTerritory;
	}

	public void setSalesTerritory(String salesTerritory) {
		this.salesTerritory = salesTerritory;
	}

	public String getSalesQuantity() {
		return salesQuantity;
	}

	public void setSalesQuantity(String salesQuantity) {
		this.salesQuantity = salesQuantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
}
