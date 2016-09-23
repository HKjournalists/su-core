package com.gettec.fsnip.fsn.model.sales;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 销售资料排序
 * @author tangxin 2015-05-07
 */
@Entity(name = "t_sales_data_sort")
public class SalesDataSort extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "business_id")
	private Long businessId;
	
	@Column(name = "organization")
	private Long organization;
	
	@Column(name = "guid")
	private String guid; //全局id
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="sort_field_id", nullable=false)
	private SortField sortField;  //  排序字段
	
	@Column(name = "object_id")
	private Long objectId; //排序对象的id,需结合排序字段联合使用才有效
	
	@Column(name = "no")
	private String no; //营业执照号、组织机构代码、qs号
	
	@Column(name = "sort")
	private Integer sort; //排序数字
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "create_user")
	private String createUser;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "update_user")
	private String updateUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public SortField getSortField() {
		return sortField;
	}

	public void setSortField(SortField sortField) {
		this.sortField = sortField;
	}
	
	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public SalesDataSort(){
		super();
	}
	
	public SalesDataSort(Long id, Long businessId, Long organization,
			Long enterpriseId, String guid, SortField sortField, Long objectId,
			String no, Integer sort, Date createTime, String createUser,
			Date updateTime, String updateUser) {
		super();
		this.id = id;
		this.businessId = businessId;
		this.organization = organization;
		this.guid = guid;
		this.sortField = sortField;
		this.objectId = objectId;
		this.no = no;
		this.sort = sort;
		this.createTime = createTime;
		this.createUser = createUser;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
	}
	
}
