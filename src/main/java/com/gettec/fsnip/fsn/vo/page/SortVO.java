package com.gettec.fsnip.fsn.vo.page;

public class SortVO {
	private String field;
	private String dir;
	
	
	
	
	public SortVO() {
		super();
	}
	public SortVO(String field, String dir) {
		super();
		this.field = field;
		this.dir = dir;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	

}
