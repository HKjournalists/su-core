package com.lhfs.fsn.vo;

import java.util.List;

/**
 * 用于server返回页面的结果信息
 * 
 * @author Administrator
 * 
 */
public class ResultVO {
	String errorMessage;
	String  message;
	String status = "false";
	List<ErrorMessageVO> error;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ErrorMessageVO> getError() {
		return error;
	}

	public void setError(List<ErrorMessageVO> error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
