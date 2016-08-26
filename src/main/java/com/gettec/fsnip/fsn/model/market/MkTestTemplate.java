package com.gettec.fsnip.fsn.model.market;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gettec.fsnip.fsn.model.test.TestResult;

@Entity(name="T_TEST_TEMPLATE")
public class MkTestTemplate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228456151572682151L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="BAR_CODE")
	private String barCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REPORT_ID", nullable = true)
	private TestResult report;
	
	@Column(name="ORG_ID")
	private Long orignizatonId;
	
	@Column(name="USER_NAME")
	private String userName;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public TestResult getReport() {
		return report;
	}

	public void setReport(TestResult report) {
		this.report = report;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrignizatonId() {
		return orignizatonId;
	}

	public void setOrignizatonId(Long orignizatonId) {
		this.orignizatonId = orignizatonId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
