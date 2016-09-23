package com.gettec.fsnip.fsn.vo.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BrandCategoryTreeNode implements Serializable {
	private static final long serialVersionUID = 7624897861350341747L;
	
	private String id = null;
	
	private String name = null;
	
	private String type = null;
	
	private boolean expanded = false;
	
	private List<BrandCategoryTreeNode> childrenNodes = new ArrayList<BrandCategoryTreeNode>(0);
	
	private boolean hasChildren = false;

	private long leafId = -1L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<BrandCategoryTreeNode> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(List<BrandCategoryTreeNode> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}

	public long getLeafId() {
		return leafId;
	}

	public void setLeafId(long leafId) {
		this.leafId = leafId;
	}
}

