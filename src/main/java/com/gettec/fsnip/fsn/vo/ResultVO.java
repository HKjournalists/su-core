package com.gettec.fsnip.fsn.vo;

/**
 * 用于server返回页面的结果信息
 * 
 * @author Administrator
 * 
 */
public class ResultVO {
	
	String message = "";
	String errorMessage = "";
	String status = "true";
	boolean success = true;
	boolean show = false;
	boolean continueflag = false;
	boolean isExist_report = false;  // 新增报告时，判断该报告是否已经存在。（用于批量上传pdf时，计数）
	String enterpriseName;
	
	Object object;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isContinueflag() {
		return continueflag;
	}

	public void setContinueflag(boolean continueflag) {
		this.continueflag = continueflag;
	}

	public boolean isExist_report() {
		return isExist_report;
	}

	public void setExist_report(boolean isExist_report) {
		this.isExist_report = isExist_report;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
