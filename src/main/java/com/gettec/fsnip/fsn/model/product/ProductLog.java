package com.gettec.fsnip.fsn.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * ReportFlowLog Entity<br>
 * 产品操作日志
 * @author LongXianZhen 2015/06/03
 */
@Entity(name = "product_log")
public class ProductLog extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2062886548616541135L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "product_id")
	private Long productId;   //产品ID
	
	@Column(name = "product_name")
	private String productName;  //产品名称

	@Column(name = "handlers")
	private String handlers;  //操作者
	
	@Column(name = "barcode")
	private String barcode;  //条形码
	
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
	private Product product; //产品

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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getOperationData() {
		return operationData;
	}

	public void setOperationData(String operationData) {
		this.operationData = operationData;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public ProductLog(Long productId, String handlers, String barcode,
			String operationData, String operation, Date operationTime) {
		super();
		this.productId = productId;
		this.handlers = handlers;
		this.barcode = barcode;
		this.operationData = operationData;
		this.operation = operation;
		this.operationTime = operationTime;
	}
	
	public ProductLog(String handlers ,String operation , Product product) {
		super();
		this.handlers = handlers;
		this.operation=operation;
		this.product = product;
	}

	public ProductLog(String handlers, String operation, String errorMessage,
			Product product) {
		super();
		this.handlers = handlers;
		this.operation = operation;
		this.errorMessage = errorMessage;
		this.product = product;
	}

	public ProductLog(String handlers, String operation, String errorMessage,
			String handlersIP, Product product) {
		super();
		this.handlers = handlers;
		this.operation = operation;
		this.errorMessage = errorMessage;
		this.handlersIP = handlersIP;
		this.product = product;
	}

	public ProductLog() {
	}
}
