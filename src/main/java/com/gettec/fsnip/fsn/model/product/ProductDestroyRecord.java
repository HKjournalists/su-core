package com.gettec.fsnip.fsn.model.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.recycle.Process_mode;

/*
 *销毁日志
 *@author xuetaoyang 2016/08/11
 *
 */
@Entity
@Table(name = "product_recycle_destroy_record")
public class ProductDestroyRecord extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "product_name")
	private String name; // 产品名称

	@Column(name = "product_code")
	private String barcode; // 产品条形码

	@Column(name = "format")
	private String format; // 规格

	@Column(name = "batch")
	private String batch; // 批次

	@Column(name = "number")
	private String number; // 数量

//	@Enumerated(EnumType.ORDINAL)
	@Column(name = "problem_describe")
	private String  problem_describe; // 问题描述

	@Column(name = "process_time")
	private String process_time; // 处理时间

	@Column(name = "deal_address")
	private String deal_address; // 处理时间
	
	@Column(name = "deal_person")
	private String deal_person; // 处理时间
	
//	@Enumerated(EnumType.ORDINAL)
	@Column(name = "process_mode")
	@Type(type="com.gettec.fsnip.fsn.recycle.UserEnumType",
	parameters={@Parameter(name="enumClass",value="com.gettec.fsnip.fsn.recycle.Process_mode")})
	private Process_mode  process_mode; // 处理方式

	@Column(name = "record_id")
	private String record_id; // 处理记录ID

	@Column(name = "handle_name")
	private String handle_name; // 处理企业名称

	@Column(name = "recieve_name")
	private String recieve_name; // 接收企业名称（只限退货）

	@Column(name = "operation_user")
	private String operation_user; // 操作者

	@Column(name = "organization")
	private Long organization; //所属企业组织机构ID

	@Column(name = "remark")
	private String remark; // 备注
	
	/* 销毁证明上传  */
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="DESTROY_RECORD_TO_RESOURCE",joinColumns={@JoinColumn(name="DESTROY_RECORD_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
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


	public Process_mode getProcess_mode() {
		return process_mode;
	}

	public void setProcess_mode(Process_mode process_mode) {
		this.process_mode = process_mode;
	}
	public String getProblem_describe() {
		return problem_describe;
	}

	public void setProblem_describe(String problem_describe) {
		this.problem_describe = problem_describe;
	}

	public String getDeal_address() {
		return deal_address;
	}

	public void setDeal_address(String deal_address) {
		this.deal_address = deal_address;
	}

	public String getDeal_person() {
		return deal_person;
	}

	public void setDeal_person(String deal_person) {
		this.deal_person = deal_person;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}


	public Long getOrganization() {
		return Organization;
	}

	public void setOrganization(Long Organization) {
		this.Organization = Organization;
	}
}
