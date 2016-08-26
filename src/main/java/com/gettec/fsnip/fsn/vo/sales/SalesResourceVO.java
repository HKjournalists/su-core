package com.gettec.fsnip.fsn.vo.sales;

import com.gettec.fsnip.fsn.model.market.ResourceType;
import com.gettec.fsnip.fsn.model.sales.SalesResource;

/**
 * 销售系统资源VO
 * @author tangxin 2015/04/29
 */
public class SalesResourceVO {

	private Long id;
	private String fileName;
	private String url;
	private ResourceType type;
	private byte[] file;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ResourceType getType() {
		return type;
	}
	public void setType(ResourceType type) {
		this.type = type;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public SalesResourceVO(){}
	
	public SalesResourceVO(SalesResource salesRes){
		if(salesRes == null) {
			return;
		}
		this.id = salesRes.getId();
		this.fileName = salesRes.getFileName();
		this.url = salesRes.getUrl();
		this.type = salesRes.getType();
	}
	
}
