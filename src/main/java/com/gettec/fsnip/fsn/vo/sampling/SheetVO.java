package com.gettec.fsnip.fsn.vo.sampling;

import java.util.List;

import com.gettec.fsnip.fsn.vo.product.ProductVO;

public class SheetVO {
	private String sheetName;
	private List<ProductVO> list;
	private String message;
	private Boolean pass = Boolean.FALSE; //是否为合格抽检
	private Source source;   //抽样来源总局或地方
	private List<ProductVO> errorList; //存在错误的行
	public enum Source {  
		总局,地方
	}
	
	
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	public Boolean getPass() {
		return pass;
	}
	public void setPass(Boolean pass) {
		this.pass = pass;
	}
	public List<ProductVO> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<ProductVO> errorList) {
		this.errorList = errorList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<ProductVO> getList() {
		return list;
	}
	public void setList(List<ProductVO> list) {
		this.list = list;
	}
	
}
