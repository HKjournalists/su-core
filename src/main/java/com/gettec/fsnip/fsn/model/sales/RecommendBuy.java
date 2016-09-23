package com.gettec.fsnip.fsn.model.sales;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 推荐购买方法
 * @author tangxin 2015/04/25
 *
 */
@Entity(name = "t_bus_recommend_buy")
public class RecommendBuy extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "business_id")
	private Long businessId;
	
	@Column(name = "organization")
	private Long organization;
	
	@Column(name = "guid")
	private String guid;  //系统全局id
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "way")
	private String way; //购买途径
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "create_user")
	private String createUser;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "update_user")
	private String updateUser;
	
	@Column(name = "del_status")
	private Integer delStatus; // 假删除状态，0默认值，1表示删除。

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
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

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	
}
