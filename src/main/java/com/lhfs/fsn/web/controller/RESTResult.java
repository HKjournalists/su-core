package com.lhfs.fsn.web.controller;

import java.util.List;

public class RESTResult<T> {

	// REST请求状态值
	public static final int FAILURE = 0; // 失败
	public static final int SUCCESS = 1; // 成功
	public static final int INVALID = 2; // 无效
	public static final int UNAUTHORIZED = 3; // 未授权
	
	private int status; // RESTResult自身状态带代码
	
	private String message;
	
	private boolean success=false;
	
	private boolean isExist=false;
	
	
	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	private T data; // 返回数据
	
	private List<T> list; // 返回的LIST
	
	public RESTResult(int status) {
		this(status, null, null);
	}
	
	public RESTResult(int status, T object) {
		this(status, null, object);
	}
	
	public RESTResult(int status, String message){
		this.status = status;
		this.message = message;
	}
	
	public RESTResult(int status, List<T> oList){
		this.status = status;
		this.setList(oList);
	}
	
	public RESTResult(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "RESTResult [status=" + status + ", message=" + message
				+ ", success=" + success + ", data=" + data + ", list=" + list
				+ "]";
	}
	
}
