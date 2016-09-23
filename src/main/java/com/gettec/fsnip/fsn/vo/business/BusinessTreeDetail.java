package com.gettec.fsnip.fsn.vo.business;

/**
 * 
 * @author HuiZhang 2015/5/19
 */
public class BusinessTreeDetail {
	private long id;
	private String name;
	private long childrenNum = 0; // the number of children;
	private long businessId;

	public BusinessTreeDetail() {}

	public BusinessTreeDetail(Long id, String name, long childrenNum, long productId) {
		setId(id);
		setName(name);
		setChildrenNum(childrenNum);
		setBusinessId(productId);
	}
	
	public long getChildrenNum() {
		return childrenNum;
	}
	public void setChildrenNum(long childrenNum) {
		this.childrenNum = childrenNum;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}
}
