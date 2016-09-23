package com.gettec.fsnip.fsn.ws.vo;

public class Message {
	private String Sn;//软件序列号	是	格式XXX-XXX-XXX-XXXXX   接口提供
	private String Pwd;//密码	是	md5(sn+password) 32位大写密文  接口提供
	private String Mobile;//手机号	是	必填(支持10000个手机号,建议<=5000)多个英文逗号隔开
	private String Content;//内容	是	支持长短信(详细请看长短信扣费说明)
	private String Ext;//扩展码	否	例如：123（默认置空）
	private String stime;//定时时间	否	例如：2010-12-29 16:27:03（非定时置空）
	private String Rrid;//唯一标识	否	最长18位，只能是数字或者 字母 或者数字+字母的组合
	public String getSn() {
		return Sn;
	}
	public void setSn(String sn) {
		Sn = sn;
	}
	public String getPwd() {
		return Pwd;
	}
	public void setPwd(String pwd) {
		Pwd = pwd;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getExt() {
		return Ext;
	}
	public void setExt(String ext) {
		Ext = ext;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getRrid() {
		return Rrid;
	}
	public void setRrid(String rrid) {
		Rrid = rrid;
	}
}
