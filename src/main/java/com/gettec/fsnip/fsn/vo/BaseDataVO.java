package com.gettec.fsnip.fsn.vo;

/**
 * Created by lxz on 2016/08/10
 * 用于封装供前台用户选择的值域数据信息
 */
public class BaseDataVO {
	/**
	 * ID
	 */
    private String id;

    /**
     * 值
     */
    private String value;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 数据类型
     */
    private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	public BaseDataVO() {
	}

	public BaseDataVO(String id, String value, String typeName, String type) {
		this.id = id;
		this.value = value;
		this.typeName = typeName;
		this.type = type;
	}
}
