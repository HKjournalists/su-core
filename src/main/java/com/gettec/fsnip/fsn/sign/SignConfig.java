package com.gettec.fsnip.fsn.sign;

/**
 * 签名的参数配置文件
 * @author ZhangHui 2015/4/23
 */
public class SignConfig{
	/**
	 * 企业的唯一标识
	 */
	private String bus_no;
	
	/**
	 * 秘钥
	 */
	private String key;
	
	/**
	 * 状态
	 */
	private int status;
	
	/**
	 * 字符编码格式
	 */
	private String input_charset = "utf-8";
	
	/**
	 * 签名方式
	 */
	private String sign_type = "MD5";
	
	public String getBus_no() {
		return bus_no;
	}
	public void setBus_no(String bus_no) {
		this.bus_no = bus_no;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getInput_charset() {
		return input_charset;
	}
	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
	public SignConfig(String bus_no, String key, int status) {
		this.bus_no = bus_no;
		this.key = key;
		this.status = status;
	}
}
