package com.lhfs.fsn.vo;

/**
 * 用于server返回页面的结果信息
 * 
 * @author Administrator
 * 
 */
public class ErrorMessageVO {
	
	String  message;
	Long sampleId ;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getSampleId() {
		return sampleId;
	}
	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}
	public ErrorMessageVO(String message, Long sampleId) {
		super();
		this.message = message;
		this.sampleId = sampleId;
	}
	
}
