package com.gettec.fsnip.fsn.model.market;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.gettec.fsnip.fsn.vo.report.ReportOfMarketVO;

@Entity(name="T_TEST_TEMP_REPORT")
public class MkTempReport implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6228456151572682151L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@Column(name="REPORT_NO")
	private String reportNo;
	
	@Column(name="TESTEE_NAME")
	private String testeeName;
	
	@Column(name="TEST_ORGNIZ_NAME")
	private String testOrgnizName;
	
	@Column(name="TEST_TYPE")
	private String testType;
	
	@Column(name="CONCLUSION")
	private String conclusion;
	
	@Column(name="TEST_PLACE")
	private String testPlace;
	
	@Column(name="SAM_PLACE")
	private String samplePlace;   // 抽样地点
	
	@Column(name="TEST_DATE")
	private Date testDate;
	
	@Column(name="SAM_COUNT")
	private String sampleCount;
	
	@Column(name="JUDGE_STANDARD")
	private String judgeStandard;
	
	@Column(name="RESULT_DESCRIBE")
	private  String testResultDescribe;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="PRO_DAARESS")
	private String proAddress;   //产地
	
	@Column(name="CREAT_USER_REALNAME")
	private String createUserRealName;
	
	@Column(name="LAST_MODIFY_TIME")
	private Date lastModifyTime;
	
	@Column(name="ORGANIZATION")
	private Long organization;
	
	@Column(name = "END_DATE")
	private Date endDate;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER, mappedBy="tempReport")
	private List<MkTempReportItem> listOfItems = new ArrayList<MkTempReportItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getTesteeName() {
		return testeeName;
	}

	public void setTesteeName(String testeeName) {
		this.testeeName = testeeName;
	}

	public String getTestOrgnizName() {
		return testOrgnizName;
	}

	public void setTestOrgnizName(String testOrgnizName) {
		this.testOrgnizName = testOrgnizName;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getTestPlace() {
		return testPlace;
	}

	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public String getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(String sampleCount) {
		this.sampleCount = sampleCount;
	}

	public String getJudgeStandard() {
		return judgeStandard;
	}

	public void setJudgeStandard(String judgeStandard) {
		this.judgeStandard = judgeStandard;
	}

	public String getTestResultDescribe() {
		return testResultDescribe;
	}

	public void setTestResultDescribe(String testResultDescribe) {
		this.testResultDescribe = testResultDescribe;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProAddress() {
		return proAddress;
	}

	public void setProAddress(String proAddress) {
		this.proAddress = proAddress;
	}

	public String getCreateUserRealName() {
		return createUserRealName;
	}

	public void setCreateUserRealName(String createUserRealName) {
		this.createUserRealName = createUserRealName;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public List<MkTempReportItem> getListOfItems() {
		return listOfItems;
	}

	public void setListOfItems(List<MkTempReportItem> listOfItems) {
		this.listOfItems = listOfItems;
	}
	
	public String getSamplePlace() {
		return samplePlace;
	}

	public void setSamplePlace(String samplePlace) {
		this.samplePlace = samplePlace;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public MkTempReport(){}
	
	public MkTempReport(ReportOfMarketVO report_vo){
		this.reportNo = report_vo.getServiceOrder();
		this.testeeName = report_vo.getTestee()!=null ? report_vo.getTestee() : "";
		this.testOrgnizName = report_vo.getTestOrgnization();
		this.testType = report_vo.getTestType();
		this.conclusion = report_vo.isPass()?"合格":"不合格";
		this.sampleCount = report_vo.getSampleQuantity();
		this.judgeStandard = report_vo.getStandard();
		this.testResultDescribe = report_vo.getResult();
		this.remark = report_vo.getComment();
		this.testDate = report_vo.getTestDate();
		this.testPlace = report_vo.getTestPlace();
		this.samplePlace = report_vo.getSamplingLocation();
	}

	public void removeItems(Set<MkTempReportItem> items) {
		for(MkTempReportItem item : items){
			item.setTempReport(null);
			this.listOfItems.remove(item);
		}
	}
}
