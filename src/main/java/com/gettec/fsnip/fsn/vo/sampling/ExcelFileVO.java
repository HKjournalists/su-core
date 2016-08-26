package com.gettec.fsnip.fsn.vo.sampling;

import java.util.ArrayList;
import java.util.List;

public class ExcelFileVO {
	private String fileName;  //文件名称
	private List<SheetVO> sheetList = new ArrayList<SheetVO>();
	
	private String message;
	private boolean flag =true;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<SheetVO> getSheetList() {
		return sheetList;
	}
	public void setSheetList(List<SheetVO> sheetList) {
		this.sheetList = sheetList;
	}
	
	
	
}
