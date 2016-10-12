package com.gettec.fsnip.fsn.model.procurement;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ProcurementOnlineSaleGoods Entity<br>
 * 
 * @author suxiang
 */
@Entity(name = "procurement_online_sale_goods")
public class OnlineSaleGoods extends Model {

	private static final long serialVersionUID = 4020559110956347118L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name; // 在线销售商品名称

	@Column(name = "barcode")
	private String barcode; // 在线销售商品条形码

	@Column(name = "unit")
	private String unit; // 在线销售商品单位

	@Column(name = "batch")
	private String batch; // 批次

	@Column(name = "procurement_num")
	private Integer procurementNum; // 采购数量

	@Column(name = "procurement_date")
	private Date procurementDate; // 采购日期

	@Column(name = "expire_date")
	private Date expireDate; // 过期日期

	@Column(name = "surplus_num")
	private Integer surplusNum; // 剩余数量

	@Column(name = "remark")
	private String remark; // 备注

	@Column(name = "food_type")
	private String foodType; // 食品分类

	@Column(name = "production_date")
	private Date productionDate; // 生产日期

	@Column(name = "expiration")
	private Integer expiration; // 保质期 天

	@Column(name = "production_name")
	private String productionName; // 生产企业名称

	@Column(name = "standard")
	private String standard; // 执行标准

	@Column(name = "formate")
	private String formate; // 规格

	@Column(name = "organization_id")
	private Long organizationId; // 所属企业机构id

	@Column(name = "create_date")
	private Date createDate; // 创建日期

	@Column(name = "creator")
	private String creator; // 创建者

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "procurement_online_sale_goods_to_source", joinColumns = { @JoinColumn(name = "online_id") }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID") })
	private Set<Resource> hgAttachments = new HashSet<Resource>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Integer getProcurementNum() {
		return procurementNum;
	}

	public void setProcurementNum(Integer procurementNum) {
		this.procurementNum = procurementNum;
	}

	public Date getProcurementDate() {
		return procurementDate;
	}

	public void setProcurementDate(Date procurementDate) {
		this.procurementDate = procurementDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Integer getExpiration() {
		return expiration;
	}

	public void setExpiration(Integer expiration) {
		this.expiration = expiration;
	}

	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getFormate() {
		return formate;
	}

	public void setFormate(String formate) {
		this.formate = formate;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Set<Resource> getHgAttachments() {
		return hgAttachments;
	}

	public void setHgAttachments(Set<Resource> hgAttachments) {
		this.hgAttachments = hgAttachments;
	}

	

}