package com.gettec.fsnip.fsn.vo.sales;

/**
 * 检测项目vo
 * @author tangxin 2015-06-07
 */
public class TestItemVO {

	private String name;
	private String result;
	private String standard;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public TestItemVO(String name, String result, String standard) {
		super();
		this.name = name;
		this.result = result;
		this.standard = standard;
	}
	
}
