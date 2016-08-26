package com.gettec.fsnip.fsn.vo.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.recycle.Process_mode;
import com.gettec.fsnip.fsn.recycle.Recycle_reason;

public class ProductDestroyRecordVo {

	
	private Long id;

	private String name; // 产品名称

	private String barcode; // 产品条形码

	private String batch; // 批次

	private String number; // 数量

	private String  problem_describe; // 问题描述

	private String process_time; // 处理时间
	
	private String  process_mode; // 处理方式
	
	private String record_id; // 处理记录ID

	private String handle_name; // 处理企业名称
	
	private String recieve_name; // 接收企业名称（只限退货）
	
	private String operation_user; // 操作者
	
	
	/* 销毁证明上传  */
	
	private Set<Resource> recAttachments = new HashSet<Resource>();
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}


	public String getProcess_time() {
		return process_time;
	}

	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public String getRecieve_name() {
		return recieve_name;
	}

	public void setRecieve_name(String recieve_name) {
		this.recieve_name = recieve_name;
	}

	public String getOperation_user() {
		return operation_user;
	}

	public void setOperation_user(String operation_user) {
		this.operation_user = operation_user;
	}
	
	public Set<Resource> getRecAttachments() {
		return recAttachments;
	}

	public void setRecAttachments(Set<Resource> recAttachments) {
		this.recAttachments = recAttachments;
	}
	public void addrecResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.recAttachments.add(resource);
		}
	}
	public void removerecResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.recAttachments.remove(resource);
		}
	}

	public String getHandle_name() {
		return handle_name;
	}

	public void setHandle_name(String handle_name) {
		this.handle_name = handle_name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}



	public String getProcess_mode() {
		return process_mode;
	}

	public void setProcess_mode(String process_mode) {
		this.process_mode = process_mode;
	}

	public String getProblem_describe() {
		return problem_describe;
	}

	public void setProblem_describe(String problem_describe) {
		this.problem_describe = problem_describe;
	}


}
