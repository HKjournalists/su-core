package com.gettec.fsnip.fsn.model.sales;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 电子资料
 * @author tangxin 2014/04/23
 *
 */
@Entity(name = "t_bus_electronic_data")
public class ElectronicData extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "business_id")
	private Long businessId;
	
	@Column(name = "organization")
	private Long organization;
	
	@Column(name = "guid")
	private String guid;
	
	@Column(name = "name")
	private String name;
		
	/*@Column(name = "annex_url")
	private String annexUrl; //电子资料附件的url
*/
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "create_user")
	private String createUser;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "update_user")
	private String updateUser;
	
	@Column(name = "del_status")
	private Integer delStatus;
	
	@Column(name="type")
	private int type;

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

/*	public String getAnnexUrl() {
		return annexUrl;
	}

	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl;
	}*/

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ElectronicData [id=" + id + ", businessId=" + businessId + ", organization=" + organization + ", guid="
				+ guid + ", name=" + name + ", createTime=" + createTime + ", createUser=" + createUser
				+ ", updateTime=" + updateTime + ", updateUser=" + updateUser + ", delStatus=" + delStatus + ", type="
				+ type + "]";
	}
}
