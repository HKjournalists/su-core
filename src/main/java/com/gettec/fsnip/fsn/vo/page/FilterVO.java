package com.gettec.fsnip.fsn.vo.page;

import java.util.List;


public class FilterVO {
	private String field;
	private String operator;
	private Object value;
	
	private String logic;
	
	private List<FilterVO> filters;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public List<FilterVO> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterVO> filters) {
		this.filters = filters;
	}  
	
	
	
}
