package com.lhfs.fsn.vo;

public class ResourceVO {
	/**
	 * 图片ID
	 */
	private String qs_Id ;
	/**
	 * 图片名称
	 */
	private String urlName ;
	/**
	 * 生产许可证号
	 */
	private String qs_no ;
	/**
	 * 生产许可证图片PATH
	 */
	private String qs_URL;
	
	public String getQs_Id() {
		return qs_Id;
	}
	public void setQs_Id(String qs_Id) {
		this.qs_Id = qs_Id;
	}
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	public String getQs_no() {
		return qs_no;
	}
	public void setQs_no(String qs_no) {
		this.qs_no = qs_no;
	}
	public String getQs_URL() {
		return qs_URL;
	}
	public void setQs_URL(String qs_URL) {
		this.qs_URL = qs_URL;
	}
}
