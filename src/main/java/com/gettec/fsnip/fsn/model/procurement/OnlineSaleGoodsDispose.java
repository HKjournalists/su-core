package com.gettec.fsnip.fsn.model.procurement;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ProcurementOnlineSaleGoodsDispose Entity<br>
 * 
 * @author suxiang
 */
@Entity(name = "procurement_online_sale_goods_dispose")
public class OnlineSaleGoodsDispose extends Model {

	private static final long serialVersionUID = -5942837645452673670L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "online_sale_id")
	private Long onlineSaleId; // 在线销售商品id

	@Column(name = "online_sale_name")
	private String onlineSaleName; // 在线销售商品名称

	@Column(name = "format")
	private String format; // 规格

	@Column(name = "batch")
	private String batch; // 批次

	@Column(name = "dispose_num")
	private Integer disposeNum; // 处理数量

	@Column(name = "dispose_date")
	private Date disposeDate; // 处理时间

	@Column(name = "dispose_cause")
	private String disposeCause; // 处理原因

	@Column(name = "dispose_place")
	private String disposePlace; // 处理地点

	@Column(name = "handler")
	private String handler; // 处理人

	@Column(name = "dispose_method")
	private String disposeMethod; // 处理方式

	@Column(name = "remark")
	private String remark; // 备注

	@Column(name = "create_date")
	private Date createDate; // 创建日期

	@Column(name = "creator")
	private String creator; // 创建者

	@Column(name = "organization_id")
	private Long organizationId; // 所属企业机构id

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "procurement_online_sale_goods_dispose_to_source", joinColumns = { @JoinColumn(name = "online_dispose_id") }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID") })
	private Set<Resource> onlineDisposeAttachments = new HashSet<Resource>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOnlineSaleId() {
		return onlineSaleId;
	}

	public void setOnlineSaleId(Long onlineSaleId) {
		this.onlineSaleId = onlineSaleId;
	}

	public String getOnlineSaleName() {
		return onlineSaleName;
	}

	public void setOnlineSaleName(String onlineSaleName) {
		this.onlineSaleName = onlineSaleName;
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

	public Integer getDisposeNum() {
		return disposeNum;
	}

	public void setDisposeNum(Integer disposeNum) {
		this.disposeNum = disposeNum;
	}

	public Date getDisposeDate() {
		return disposeDate;
	}

	public void setDisposeDate(Date disposeDate) {
		this.disposeDate = disposeDate;
	}

	public String getDisposeCause() {
		return disposeCause;
	}

	public void setDisposeCause(String disposeCause) {
		this.disposeCause = disposeCause;
	}

	public String getDisposePlace() {
		return disposePlace;
	}

	public void setDisposePlace(String disposePlace) {
		this.disposePlace = disposePlace;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getDisposeMethod() {
		return disposeMethod;
	}

	public void setDisposeMethod(String disposeMethod) {
		this.disposeMethod = disposeMethod;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Set<Resource> getOnlineDisposeAttachments() {
		return onlineDisposeAttachments;
	}

	public void setOnlineDisposeAttachments(Set<Resource> onlineDisposeAttachments) {
		this.onlineDisposeAttachments = onlineDisposeAttachments;
	}

	

}