package com.lhfs.fsn.vo.business;

/**
 * date : 2016.05.06
 * @author wb
 *
 */
public class BussinessCredentialsVO {

	private String typeName; // 组织机构代码证
	
	private String documentFullPath; // 组织机构代码证图片URL
	
	private String code;// 工商营业执照号码
	
	private String type;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDocumentFullPath() {
		return documentFullPath;
	}

	public void setDocumentFullPath(String documentFullPath) {
		this.documentFullPath = documentFullPath;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
