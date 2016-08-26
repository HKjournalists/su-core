package com.gettec.fsnip.fsn.model.test;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * TestResultHandler Entity<br>
 * 报告结构化处理人员
 * 
 * @author LongXianZhen
 *  2015-04-28
 */
@Entity(name = "test_result_handler")
public class TestResultHandler extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Transient
	private Long testResultId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "test_result_id", nullable = true)
	private TestResult testResult;

	@Column(name = "handler")
	private String handler;
	
	@Column(name = "creation_time")
	private Date creationTime;
	
	/**
	 * 报告的发布状态
	 * 		1     代表 待结构化;
	 *      2     代表 结构化完成;
	 *      4     代表 testlab退回至结构化人员;
	 *      8     代表 结构化完成后通过testlab终极审核
	 *      12  代表 [食安云]将报告直接退回至供应商
	 */
	@Column(name = "status")
	private int status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestResultId() {
		return testResultId;
	}

	public void setTestResultId(Long testResultId) {
		this.testResultId = testResultId;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
