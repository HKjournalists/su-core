package com.gettec.fsnip.fsn.model.market;



import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;

import com.gettec.fsnip.fsn.model.test.TestProperty;

@Entity(name="T_TEST_TEMP_ITEM")
public class MkTempReportItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228456151572682151L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="ITEM_NAME")
	private String itemName;
	
	@Column(name="ITEM_UNIT")
	private String itemUnit;
	
	@Column(name="SPECIFICATION")
	private String specification;
	
	@Column(name="TEST_RESULT")
	private String testResult;
	
	@Column(name="ITEM_CONCLUSION")
	private String conclusion;
	
	@Column(name="STANDARD")
	private String standard;
	
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.DETACH}, optional=true, fetch=FetchType.EAGER)
	@JoinColumn(name="REPORT_ID")
	private MkTempReport tempReport;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	@JsonBackReference
	public MkTempReport getTempReport() {
		return tempReport;
	}
	
	@JsonBackReference
	public void setTempReport(MkTempReport tempReport) {
		this.tempReport = tempReport;
	}
	
	public MkTempReportItem(){}
	
	public MkTempReportItem(TestProperty item) {
		this.itemName = item.getName();
		this.itemUnit = item.getUnit();
		this.specification = item.getTechIndicator();
		this.testResult = item.getResult();
		this.conclusion = item.getAssessment();
		this.standard = item.getStandard();
	}

}
