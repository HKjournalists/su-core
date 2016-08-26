package com.gettec.fsnip.fsn.vo.receive;

/**
 * 用于封装返回消息
 * @author ZhangHui 2015/4/23
 */
public class ResultVO {
	/**
	 * 是否成功
	 */
	private boolean success = true;
	
	/**
	 * 消息提示
	 */
	private String message = "成功";
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
