package com.gettec.fsnip.fsn.model.test;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * ReportFlowLog Entity<br>
 * 报告流转日志
 * 
 * @author LongXianZhen 2015/04/29
 */
@Entity(name = "report_flow_log")
public class ReportFlowLog extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2062886548616541135L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "test_result_id")
	private Long testResultId;

	@Column(name = "handlers")
	private String handlers;
	
	@Column(name = "barcode")
	private String barcode;
	
	@Column(name = "batch_serial_no")
	private String batchSerialNo;
	
	@Column(name = "service_order")
	private String serviceOrder;
	
	@Column(name = "operation")
	private String operation;
	
	@Column(name = "operation_time")
	private Date operationTime;

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

	public String getHandlers() {
		return handlers;
	}

	public void setHandlers(String handlers) {
		this.handlers = handlers;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public String getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public ReportFlowLog(Long testResultId, String handlers, String barcode,
			String batchSerialNo, String serviceOrder, String operation) {
		super();
		this.testResultId = testResultId;
		this.handlers = handlers;
		this.barcode = barcode;
		this.batchSerialNo = batchSerialNo;
		this.serviceOrder = serviceOrder;
		this.operation = operation;
	}

	public ReportFlowLog() {
	}
}
