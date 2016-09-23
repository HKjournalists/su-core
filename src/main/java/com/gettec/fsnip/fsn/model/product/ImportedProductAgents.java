package com.gettec.fsnip.fsn.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;
/**
 * 进口产品国内代理商
 * @author longxianzhen 2015/05/22
 *
 */
@Entity(name="imported_product_domestic_agents")
public class ImportedProductAgents extends Model {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "imp_pro_id")
	private Long impProId;   //进口产品id
	
	@Column(name = "agent_name")
	private String agentName;   //国内代理商名称
	
	@Column(name = "agent_address")
	private String agentAddress;  //国内代理商地址
	
	@Column(name="createDate")
	private Date createDate;     //创建时间
	
	@Column(name = "creator")
	private String creator;  // 创建者
	
	@Column(name = "lastModifyUser")
	private String lastModifyUser;      // 最后更新者
	
	@Column(name = "lastModifyDate")
	private Date lastModifyDate;  // 最后更新时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getImpProId() {
		return impProId;
	}

	public void setImpProId(Long impProId) {
		this.impProId = impProId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentAddress() {
		return agentAddress;
	}

	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
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

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
}
