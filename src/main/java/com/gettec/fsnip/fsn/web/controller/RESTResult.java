package com.gettec.fsnip.fsn.web.controller;

public class RESTResult<T> {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int INVALID = 2;
	public static final int UNAUTHORIZED = 3;
	
	private int statusCode;
	
	private String message;
	
	private boolean success=false;
	
	private T data;
	
	public RESTResult(int statusCode) {
		this(statusCode, null, null);
	}
	
	public RESTResult(int statusCode, T object) {
		this(statusCode, null, object);
	}
	
	public RESTResult(int statusCode, String message, T data) {
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}

	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
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
	public RESTResult(){
	}

}
