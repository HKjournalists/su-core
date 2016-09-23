package com.gettec.fsnip.fsn.vo.erp;

/**
 * 用于封装客户管理新增客户中的客户名称
 * @author HuangYog
 * 2015/04/11
 */
public class BusinessNameVO {
	
	private Long id;
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
	public BusinessNameVO() {}
	
	public BusinessNameVO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
}
