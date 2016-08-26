package com.gettec.fsnip.fsn.vo.report;

import java.util.Set;

import com.gettec.fsnip.fsn.model.market.Resource;

public class ReportBackVO {
	private String returnMes;
	private Set<Resource> repBackAttachments;
	private String status;
	public String getReturnMes() {
		return returnMes;
	}
	public void setReturnMes(String returnMes) {
		this.returnMes = returnMes;
	}
	public Set<Resource> getRepBackAttachments() {
		return repBackAttachments;
	}
	public void setRepBackAttachments(Set<Resource> repBackAttachments) {
		this.repBackAttachments = repBackAttachments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
