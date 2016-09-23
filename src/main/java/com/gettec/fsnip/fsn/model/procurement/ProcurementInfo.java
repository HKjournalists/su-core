package com.gettec.fsnip.fsn.model.procurement;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ProcurementInfo Entity<br>
 * @author lxz
 */
@Entity(name = "procurement_info")
public class ProcurementInfo extends Model{
	
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;  // 采购材料名称
	
	@Column(name = "provider_id")
	private Long providerId;  // 供应商企业id

	@Column(name = "provider_name")
	private String providerName;  // 供应商名称
	
	@Column(name = "format")
	private String format;   // 规格
	
	@Column(name = "batch")
	private String batch;   // 批次
	
	@Column(name = "procurement_num")
	private Integer procurementNum;   // 采购数量
	
	@Column(name = "procurement_date")
	private Date procurementDate;   // 采购日期
	
	@Column(name = "expire_date")
	private Date expireDate;   // 过期日期

	@Column(name = "remark")
	private String remark;   // 备注
	
	@Column(name = "surplus_num")
	private Integer surplusNum;   // 剩余数量

	@Column(name = "type")
	private Integer type;   // 采购类型   1：原辅料  2：添加剂  3：包装材料

	@Column(name = "create_date")
	private Date createDate;   // 创建日期

	@Column(name = "creator")
	private String creator;   // 创建者

	@Column(name = "organization_id")
	private Long organizationId;   // 所属企业机构id

	

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="procurement_info_to_resource",joinColumns={@JoinColumn(name="procurement_id")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
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



	public Long getProviderId() {
		return providerId;
	}



	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}



	public String getProviderName() {
		return providerName;
	}



	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}



	public String getFormat() {
		return format;
	}



	public void setFormat(String format) {
		this.format = format;
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



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public Integer getType() {
		return type;
	}



	public void setType(Integer type) {
		this.type = type;
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



	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	public Set<Resource> getHgAttachments() {
		return hgAttachments;
	}



	public void setHgAttachments(Set<Resource> hgAttachments) {
		this.hgAttachments = hgAttachments;
	}
	
	public Integer getSurplusNum() {
		return surplusNum;
	}



	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}
	
}