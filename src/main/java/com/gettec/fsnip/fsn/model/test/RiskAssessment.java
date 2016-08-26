package com.gettec.fsnip.fsn.model.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 *  风险指数的历史数据
 * TestResult Entity<br>
 * @author zhaWanNeng
 */
@SuppressWarnings("serial")
@Entity(name = "risk_assessment")
public class RiskAssessment extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "risk_Index")
	private Double riskIndex;  // 风险指数
	
	@Column(name = "product_id")
	private Long productid;  // 产品实例
	
	@Column(name = "risk_succeed")
	private Boolean risk_succeed = false;   //风险指数的计算成功:0：失败、1：成功
	
	@Column(name = "risk_failure")
	private String  riskFailure;   //风险指数计算失败的原因
	
	@Column(name = "user_name")
	private String  userName;   //记录人
	
	@Column(name = "risk_Date")
	private Date  riskDate;   //风险指数的计算时间
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="risk_to_property",joinColumns={@JoinColumn(name="risk_id")}, inverseJoinColumns = {@JoinColumn(name="property_id")})
	private Set<TestProperty> testPropertys = new HashSet<TestProperty>();  
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getRiskIndex() {
		return riskIndex;
	}

	public void setRiskIndex(Double riskIndex) {
		this.riskIndex = riskIndex;
	}

	public Set<TestProperty> getTestPropertys() {
		return testPropertys;
	}

	public void setTestPropertys(Set<TestProperty> testPropertys) {
		this.testPropertys = testPropertys;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Boolean getRisk_succeed() {
		return risk_succeed;
	}

	public void setRisk_succeed(Boolean risk_succeed) {
		this.risk_succeed = risk_succeed;
	}

	public String getRiskFailure() {
		return riskFailure;
	}

	public void setRiskFailure(String riskFailure) {
		this.riskFailure = riskFailure;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getRiskDate() {
		return riskDate;
	}

	public void setRiskDate(Date riskDate) {
		this.riskDate = riskDate;
	}
    
	public RiskAssessment() {
		super();
	}

}
