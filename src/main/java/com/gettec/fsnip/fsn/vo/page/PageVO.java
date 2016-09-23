package com.gettec.fsnip.fsn.vo.page;

import com.gettec.fsnip.sso.client.util.AccessUtils;


public class PageVO {
	
	private int page;
	private int pageSize;
	private FilterVO filter;
	private SortVO sort;
	
	private boolean property = true;  //是否组拼级联对象
	
	
	
	

	public boolean isProperty() {
		return property;
	}

	public void setProperty(boolean property) {
		this.property = property;
	}

	public String getUserOrgID(){
		return AccessUtils.getUserOrg().toString();
	}
	
	public FilterVO getFilter() {
		return filter;
	}
	public void setFilter(FilterVO filter) {
		this.filter = filter;
	}
	

	public SortVO getSort() {
		return sort;
	}
	public void setSort(SortVO sort) {
		this.sort = sort;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
