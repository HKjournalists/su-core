package com.gettec.fsnip.fsn.vo.waste;

import java.util.HashSet;
import java.util.Set;

import com.gettec.fsnip.fsn.model.market.Resource;

public class WasteDisposaVO {
	private Long id;
	private String handler;
	private String handleNumber;
	private String handleWay;
	private String handleTime;
	private String participation;
	private String destory;
	private long qiyeId;
	private String createTime;
	private Set<Resource> piceFile = new HashSet<Resource>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public String getHandleNumber() {
		return handleNumber;
	}
	public void setHandleNumber(String handleNumber) {
		this.handleNumber = handleNumber;
	}
	public String getHandleWay() {
		return handleWay;
	}
	public void setHandleWay(String handleWay) {
		this.handleWay = handleWay;
	}
	public String getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}
	public String getParticipation() {
		return participation;
	}
	public void setParticipation(String participation) {
		this.participation = participation;
	}
	public String getDestory() {
		return destory;
	}
	public void setDestory(String destory) {
		this.destory = destory;
	}
	
	public long getQiyeId() {
		return qiyeId;
	}
	public void setQiyeId(long qiyeId) {
		this.qiyeId = qiyeId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Set<Resource> getPiceFile() {
		return piceFile;
	}
	public void setPiceFile(Set<Resource> piceFile) {
		this.piceFile = piceFile;
	}
	
}
