package com.gettec.fsnip.fsn.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 基础表（同fdams）
 * @author Administrator
 */
@Entity(name = "sys_office")
public class Office extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "parent_id")
	private Long parentId;
	
	@Column(name = "parent_ids")
	private String parentIds;
	
	@Column(name = "area_id")
	private Long areaId;
	
	@Column(name = "code")
	private String code;  //区域编码
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private char type;

	@Column(name = "dqjbxh")
	private char dqjbxh;  //地区级别编号  考核指标会用到
	
	@Transient
	private boolean hasChildren; 
	
	@Transient
	private long leafId = -1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public char getDqjbxh() {
		return dqjbxh;
	}

	public void setDqjbxh(char dqjbxh) {
		this.dqjbxh = dqjbxh;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public long getLeafId() {
		return leafId;
	}

	public void setLeafId(long leafId) {
		this.leafId = leafId;
	}
	
}
