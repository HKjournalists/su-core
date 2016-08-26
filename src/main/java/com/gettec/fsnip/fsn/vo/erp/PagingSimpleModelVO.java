package com.gettec.fsnip.fsn.vo.erp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagingSimpleModelVO<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9100683083657827675L;

	private Long count;
	private List<E> listOfModel = new ArrayList<E>();
	
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<E> getListOfModel() {
		return listOfModel;
	}
	public void setListOfModel(List<E> listOfModel) {
		this.listOfModel = listOfModel;
	}
	/**
	 * @param count
	 * @param listOfModel
	 */
	public PagingSimpleModelVO(Long count, List<E> listOfModel) {
		super();
		this.count = count;
		this.listOfModel = listOfModel;
	}
	/**
	 * 
	 */
	public PagingSimpleModelVO() {
		super();
	}
	
}
