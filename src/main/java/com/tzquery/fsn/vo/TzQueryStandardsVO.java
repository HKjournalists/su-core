/**
 * 
 */
package com.tzquery.fsn.vo;

/**
 * 执行标准VO
 * @author ChenXiaolin 2015-12-2
 */
public class TzQueryStandardsVO {

	private String staCattgory;	//标准分类
	private String staName;		//标准名称
	private String gb;			//对应国标
	
	public String getStaCattgory() {
		return staCattgory;
	}
	public void setStaCattgory(String staCattgory) {
		this.staCattgory = staCattgory;
	}
	public String getStaName() {
		return staName;
	}
	public void setStaName(String staName) {
		this.staName = staName;
	}
	public String getGb() {
		return gb;
	}
	public void setGb(String gb) {
		this.gb = gb;
	}
	
}
