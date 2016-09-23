package com.gettec.fsnip.fsn.vo.business;

/**
 * 轻量级企业信息封装<br>
 * 作用于：qs授权时，页面下拉选择当前企业的所有相关企业（母企业、子企业、兄弟企业）
 * @author ZhangHui 2015/5/18
 */
public class BusinessUnitLightVO {
	/**
	 * 企业id
	 */
	private Long id;
	
	/**
	 * 企业名称
	 */
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public BusinessUnitLightVO() {
		super();
	}
	
	public BusinessUnitLightVO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
