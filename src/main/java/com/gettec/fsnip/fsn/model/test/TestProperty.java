package com.gettec.fsnip.fsn.model.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.MkTempReportItem;

/**
 * TestProperty Entity<br>
 * 
 * 
 * @author Ryan Wang
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Test-Item", propOrder = {
    "name", "standard", "unit", "techIndicator", "result", "assessment"/*,"testResultId"*/
})
@Entity(name = "test_property")
public class TestProperty extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2062886548616541135L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@XmlElement(name = "Name")
	@Column(name = "name", length = 50)
	private String name;
	
	@XmlElement(name = "Unit")
	@Column(name = "unit", length = 20)
	private String unit;
	
	@XmlElement(name = "Tech-Indicator")
	@Column(name = "tech_indicator", length = 50)
	private String techIndicator;

	@XmlElement(name = "Result")
	@Column(name = "result", length = 20)
	private String result;
	
	@XmlElement(name = "Assessment")
	@Column(name = "assessment", length = 50)
	private String assessment;
	
	@XmlElement(name = "Standard")
	@Column(name = "standard", length = 50)
	private String standard;
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category", nullable = true)
	private TestPropertyCategory category;
	
	@XmlTransient
	@Column(name="test_result_id", nullable=true)
	private Long testResultId;
	
	@Transient
	private boolean temporaryFlag = false; 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTechIndicator() {
		return techIndicator;
	}

	public void setTechIndicator(String techIndicator) {
		this.techIndicator = techIndicator;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public TestPropertyCategory getCategory() {
		return category;
	}

	public void setCategory(TestPropertyCategory category) {
		this.category = category;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public Long getTestResultId() {
		return testResultId;
	}

	public void setTestResultId(Long testResultId) {
		this.testResultId = testResultId;
	}
	
	public TestProperty(){}
	
	public TestProperty(String name, String unit, String techIndicator,
			String result, String assessment, String standard) {
		super();
		this.name = name;
		this.unit = unit;
		this.techIndicator = techIndicator;
		this.result = result;
		this.assessment = assessment;
		this.standard = standard;
	}

	public TestProperty(MkTempReportItem item) {
		this.id = item.getId();
		this.name = item.getItemName();
		this.unit = item.getItemUnit();
		this.techIndicator = item.getSpecification();
		this.result = item.getTestResult();
		this.assessment = item.getConclusion();
		this.standard = item.getStandard();
	}

	public boolean isTemporaryFlag() {
		return temporaryFlag;
	}

	public void setTemporaryFlag(boolean temporaryFlag) {
		this.temporaryFlag = temporaryFlag;
	}
	
}
