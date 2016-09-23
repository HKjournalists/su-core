package com.lhfs.fsn.vo.atgoo;

import java.io.Serializable;

/**
 * 认证信息数VO 提供给爱特够系统
 * @author tangxin 2016/6/26
 *
 */
public class CertificationVO implements Serializable{

	/**
	 * 认证名称
	 */
	String name;
	/**
	 * 证书图标Url
	 */
	String iconUrl;
	/**
	 * 证书Url
	 */
	String certUrl;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getCertUrl() {
		return certUrl;
	}
	public void setCertUrl(String certUrl) {
		this.certUrl = certUrl;
	}
	
}
