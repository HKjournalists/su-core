package com.gettec.fsnip.fsn.model.business;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 交易市场下商户信息
 * @author YuanBin Hao
 */
@Entity(name = "business_market_to_business")
public class MarketToBusiness extends Model {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;	
	
	@OneToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH}, fetch = FetchType.EAGER)
	@JoinColumn(name="business_id")
	private BusinessUnit business;//企业信息
	
	@Column(name = "name",nullable = false)
	private String name; //商户名称
	
	@Column(name = "license",nullable = false)
	private String license; //营业执照号
	
	@Column(name = "personInCharge",nullable = false)
	private String personInCharge; //负责人
	
	@Column(name = "telephone",nullable = false)
	private String telephone; //联系电话
	
	@Column(name = "email",nullable = false)
	private String email; //邮箱
	
	@Column(name = "date")
	private Date date; //时间
	
	@Column(name = "count")
	private Long count; //当天已发邮件次数
	
	@Column(name = "organization",nullable = false)
	private Long organization; //组织机构

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public BusinessUnit getBusiness() {
		return business;
	}

	public void setBusiness(BusinessUnit business) {
		this.business = business;
	}
	
	public MarketToBusiness(){
		
	};
	
	public MarketToBusiness(Long id,BusinessUnit business,Long organization,String name,String license,String personInCharge,String telephone,String email,Date date,Long count){
		super();
		this.id=id;
		this.business=business;
		this.organization=organization;
		this.name=name;
		this.license=license;
		this.personInCharge=personInCharge;
		this.telephone=telephone;
		this.email=email;
		this.date=date;
		this.count=count;
	};
}
