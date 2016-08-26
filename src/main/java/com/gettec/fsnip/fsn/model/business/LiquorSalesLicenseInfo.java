package com.gettec.fsnip.fsn.model.business;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;


import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;
/**
 * License Entity<br>
 * 酒类销售许可证信息
 * @author Xianzhen Long
 */
@Entity(name = "liquor_sales_license")
public class LiquorSalesLicenseInfo extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "certificate_no")
	private String certificateNo;           // 许可证编号
	
	@Column(name = "legal_name")
	private String legalName;        // 法定代表人
	
	@Column(name = "address")
	private String address;          // 地址
	
	@Column(name = "business_type")
	private String businessType;  // 经营类型
	
	@Column(name = "business_scope")
	private String businessScope;  // 经营范围
	
	@Column(name = "start_time")
	private Date startTime;      //有效期: 开始时间
	
	@Column(name="end_time")
	private Date endTime;   //有效期: 结束时间	

	@Column(name="issuing_authority")
	private String issuingAuthority;   //发证机关

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
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

	

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndTime() {
		return endTime;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getStartTime() {
		return startTime;
	}
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	

	public String getIssuingAuthority() {
		return issuingAuthority;
	}

	public void setIssuingAuthority(String issuingAuthority) {
		this.issuingAuthority = issuingAuthority;
	}

	
}
