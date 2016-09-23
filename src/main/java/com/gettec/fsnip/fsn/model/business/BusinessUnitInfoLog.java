package com.gettec.fsnip.fsn.model.business;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * BusinessUnitInfoLog Entity<br>
 * 企业信息操作日志
 * @author LongXianZhen 2015/06/04
 */
@Entity(name = "business_unit_info_log")
public class BusinessUnitInfoLog extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2062886548616541135L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "business_unit_id")
	private Long businessUnitId;   //企业ID
	
	@Column(name = "business_unit_name")
	private String businessUnitName;  //企业名称

	@Column(name = "handlers")
	private String handlers;  //操作者
	
	@Column(name = "operation_data")
	private String operationData;  //操作数据

	@Column(name = "operation")
	private String operation;  //所做操作
	
	@Column(name = "operation_time")
	private Date operationTime;   //操作时间
	
	@Column(name = "errorMessage")
	private String errorMessage;  //错误消息
	
	@Column(name = "handlersIP")
	private String handlersIP;  //操作者IP地址
	
	@Transient
	private BusinessUnit businessUnit; //企业

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHandlers() {
		return handlers;
	}

	public void setHandlers(String handlers) {
		this.handlers = handlers;
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
	


	public String getOperationData() {
		return operationData;
	}

	public void setOperationData(String operationData) {
		this.operationData = operationData;
	}

	

	public Long getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Long businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public BusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getHandlersIP() {
		return handlersIP;
	}

	public void setHandlersIP(String handlersIP) {
		this.handlersIP = handlersIP;
	}

	public BusinessUnitInfoLog(String handlers, String operation,
			BusinessUnit businessUnit) {
		super();
		this.handlers = handlers;
		this.operation = operation;
		this.businessUnit = businessUnit;
	}

	public BusinessUnitInfoLog(String handlers, String operation,
			String errorMessage, BusinessUnit businessUnit) {
		super();
		this.handlers = handlers;
		this.operation = operation;
		this.errorMessage = errorMessage;
		this.businessUnit = businessUnit;
	}

	public BusinessUnitInfoLog(String handlers, String operation,
			String errorMessage, String handlersIP, BusinessUnit businessUnit) {
		super();
		this.handlers = handlers;
		this.operation = operation;
		this.errorMessage = errorMessage;
		this.handlersIP = handlersIP;
		this.businessUnit = businessUnit;
	}

	public BusinessUnitInfoLog() {
	}
}
