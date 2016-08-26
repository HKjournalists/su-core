package com.gettec.fsnip.fsn.model.thirdReport;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

@Entity(name = "t_test_third")
public class Thirdreport extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1033965731590642843L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id ;
	
	@Column(name = "test_result_id")
	private String testResultId;//报告id
	
	@Column(name = "service_order")
	private String serviceOrder;//报告编号
	
	@Column(name = "report_count")
	private long reportCount;//第几次抽检
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="t_test_thirdreport_to_resource",joinColumns={@JoinColumn(name="THIRD_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> reportArray = new HashSet<Resource>();//报告原件
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="t_test_thirdcheck_to_resource",joinColumns={@JoinColumn(name="THIRD_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> checkArray = new HashSet<Resource>();//检测报告
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="t_test_thirdbuy_to_resource",joinColumns={@JoinColumn(name="THIRD_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> buyArray = new HashSet<Resource>();//购买凭证

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTestResultId() {
		return testResultId;
	}

	public void setTestResultId(String testResultId) {
		this.testResultId = testResultId;
	}

	public String getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public long getReportCount() {
		return reportCount;
	}

	public void setReportCount(long reportCount) {
		this.reportCount = reportCount;
	}

	public Set<Resource> getReportArray() {
		return reportArray;
	}

	public void setReportArray(Set<Resource> reportArray) {
		this.reportArray = reportArray;
	}

	public Set<Resource> getCheckArray() {
		return checkArray;
	}

	public void setCheckArray(Set<Resource> checkArray) {
		this.checkArray = checkArray;
	}

	public Set<Resource> getBuyArray() {
		return buyArray;
	}

	public void setBuyArray(Set<Resource> buyArray) {
		this.buyArray = buyArray;
	}

	
}
