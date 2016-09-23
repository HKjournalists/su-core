package com.gettec.fsnip.fsn.vo.sales;

/**
 * 
 * @author tangxin
 * 销售系统 产品信息vo
 */
public class SalesProductVO {

	private Long id;//产品id
	private String name;//产品名称
	private String format;//规格
	private String desc;//产品描述
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
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public SalesProductVO(){
		super();
	}
	public SalesProductVO(Long id, String name, String format, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.format = format;
		this.desc = desc;
	}
	
}
