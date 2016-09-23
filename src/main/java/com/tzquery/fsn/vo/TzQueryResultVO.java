package com.tzquery.fsn.vo;

public class TzQueryResultVO {

	String message = "success";//成功或失败信息提示
	boolean status = true;//返回状态 false：失败 true：成功
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
