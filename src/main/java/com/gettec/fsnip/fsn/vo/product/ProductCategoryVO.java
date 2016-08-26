package com.gettec.fsnip.fsn.vo.product;

/**
 * 泊银等外部系统，产品分类VO
 * @author Zhanghui 2015/4/27
 */
public class ProductCategoryVO {
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 分类code
	 */
	private String code;
	/**
	 * 父分类code
	 */
	private String parentCode;
	
	private int level;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	public ProductCategoryVO(){}
	
	public ProductCategoryVO(String name, String code, String parentCode, int level){
		 this.name = name;
		 this.code = code;
		 this.parentCode = parentCode;
		 this.level = level;
	}
	
	public ProductCategoryVO(Long id, String name, String parentCode, int level){
		 this.id = id;
		 this.name = name;
		 this.parentCode = parentCode;
		 this.level = level;
	}
}
