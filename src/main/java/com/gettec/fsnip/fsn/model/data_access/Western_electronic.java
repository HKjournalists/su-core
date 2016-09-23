package com.gettec.fsnip.fsn.model.data_access;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 西部电子商务数据接入情况表<br>
 * @author litg
 */
@Entity(name="western_electronic")
public class Western_electronic extends Model {
	private static final long serialVersionUID = 4451990499170794321L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@Column(name="status", length=1)
	private String status;
	
	@Column(name="percent", length=10)
	private String percent;
	
	@Column(name="complete_num")
	private int complete_num;
	
	@Column(name="company_num")
	private int company_num;
	
	@Column(name="product_num")
	private int product_num;
	
	@Column(name="report_num")
	private int report_num;
	
	@Column(name="trace_num")
	private int trace_num;
	
	@Column(name="success_num")
	private int success_num;
	
	@Column(name="fail_num")
	private int fail_num;
	
	@Column(name="operation_user", length=20)
	private String operation_user;
	
	@Column(name="operation_time")
	private String operation_time;
	
	@Column(name="complete_time")
	private String complete_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public int getComplete_num() {
		return complete_num;
	}

	public void setComplete_num(int complete_num) {
		this.complete_num = complete_num;
	}

	public int getCompany_num() {
		return company_num;
	}

	public void setCompany_num(int company_num) {
		this.company_num = company_num;
	}

	public int getProduct_num() {
		return product_num;
	}

	public void setProduct_num(int product_num) {
		this.product_num = product_num;
	}

	public int getReport_num() {
		return report_num;
	}

	public void setReport_num(int report_num) {
		this.report_num = report_num;
	}

	public int getTrace_num() {
		return trace_num;
	}

	public void setTrace_num(int trace_num) {
		this.trace_num = trace_num;
	}

	public int getSuccess_num() {
		return success_num;
	}

	public void setSuccess_num(int success_num) {
		this.success_num = success_num;
	}

	public int getFail_num() {
		return fail_num;
	}

	public void setFail_num(int fail_num) {
		this.fail_num = fail_num;
	}

	public String getOperation_user() {
		return operation_user;
	}

	public void setOperation_user(String operation_user) {
		this.operation_user = operation_user;
	}

	public String getOperation_time() {
		return operation_time;
	}

	public void setOperation_time(String operation_time) {
		this.operation_time = operation_time;
	}

	public String getComplete_time() {
		return complete_time;
	}

	public void setComplete_time(String complete_time) {
		this.complete_time = complete_time;
	}
}