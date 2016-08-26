package com.gettec.fsnip.fsn.vo.thirdreport;

import java.util.ArrayList;
import java.util.List;


import com.gettec.fsnip.fsn.model.market.Resource;

public class ThirdreportVo {
	
	private long id ;
	private String serviceOrder;
	private long reportCount;
	private List<Resource> reportArray = new ArrayList<Resource>();
	private List<Resource> checkArray = new ArrayList<Resource>();
	private List<Resource> buyArray = new ArrayList<Resource>();
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public List<Resource> getReportArray() {
		return reportArray;
	}
	public void setReportArray(List<Resource> reportArray) {
		this.reportArray = reportArray;
	}
	public List<Resource> getCheckArray() {
		return checkArray;
	}
	public void setCheckArray(List<Resource> checkArray) {
		this.checkArray = checkArray;
	}
	public List<Resource> getBuyArray() {
		return buyArray;
	}
	public void setBuyArray(List<Resource> buyArray) {
		this.buyArray = buyArray;
	}
	
	

}
