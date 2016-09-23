package com.gettec.fsnip.fsn.model.business;

import javax.persistence.*;


import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * License Entity<br>
 * 税务登记证件信息
 * @author Xianzhen Long
 */
@Entity(name = "tax_register_cert")
public class TaxRegisterInfo extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "taxer_name")
	private String taxerName;           // 纳税人名称
	
	@Column(name = "legal_name")
	private String legalName;        // 法定代表人
	
	@Column(name = "address")
	private String address;          // 地址
	
	@Column(name = "register_type")
	private String registerType;  // 登记注册类型
	
	@Column(name = "business_scope")
	private String businessScope;  // 经营范围
	
	@Column(name = "approve_setUp_authority")
	private String approveSetUpAuthority;      // 批准设立机关

	@Column(name="issuing_authority")
	private String issuingAuthority;   //发证机关
	
	@Column(name="withholding_obligations")
	private String withholdingObligations;   //扣缴义务

	@Transient
	private Set<Resource> taxAttachments = new HashSet<Resource>();//税务登记证图片

	public Set<Resource> getTaxAttachments() {
		return taxAttachments;
	}

	public void setTaxAttachments(Set<Resource> taxAttachments) {
		this.taxAttachments = taxAttachments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaxerName() {
		return taxerName;
	}

	public void setTaxerName(String taxerName) {
		this.taxerName = taxerName;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public String getApproveSetUpAuthority() {
		return approveSetUpAuthority;
	}

	public void setApproveSetUpAuthority(String approveSetUpAuthority) {
		this.approveSetUpAuthority = approveSetUpAuthority;
	}

	public String getIssuingAuthority() {
		return issuingAuthority;
	}

	public void setIssuingAuthority(String issuingAuthority) {
		this.issuingAuthority = issuingAuthority;
	}

	public String getWithholdingObligations() {
		return withholdingObligations;
	}

	public void setWithholdingObligations(String withholdingObligations) {
		this.withholdingObligations = withholdingObligations;
	}
	
	
}
