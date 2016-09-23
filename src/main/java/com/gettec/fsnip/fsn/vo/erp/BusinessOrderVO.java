package com.gettec.fsnip.fsn.vo.erp;

import java.util.ArrayList;
import java.util.List;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.erp.buss.BussToMerchandises;
import com.gettec.fsnip.fsn.model.erp.buss.FlittingOrder;
import com.gettec.fsnip.fsn.model.erp.buss.OutStorageRecord;

public class BusinessOrderVO extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6282840756458214301L;

	private String no;
	private Long typeInstance;
	private String note;
	private List<BussToMerchandises> merchandises = new ArrayList<BussToMerchandises>();
	private OutStorageRecord result;
	private FlittingOrder result1;
	
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Long getTypeInstance() {
		return typeInstance;
	}

	public void setTypeInstance(Long typeInstance) {
		this.typeInstance = typeInstance;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<BussToMerchandises> getMerchandises() {
		return merchandises;
	}

	public void setMerchandises(List<BussToMerchandises> merchandises) {
		this.merchandises = merchandises;
	}

	public OutStorageRecord getResult() {
		return result;
	}

	public void setResult(OutStorageRecord result) {
		this.result = result;
	}

	
	public FlittingOrder getResult1() {
		return result1;
	}

	public void setResult1(FlittingOrder result1) {
		this.result1 = result1;
	}

	/**
	 * @param no
	 * @param typeInstance
	 * @param note
	 * @param merchandises
	 */
	public BusinessOrderVO(String no, Long typeInstance, String note,
			List<BussToMerchandises> merchandises) {
		super();
		this.no = no;
		this.typeInstance = typeInstance;
		this.note = note;
		this.merchandises = merchandises;
	}

	
	/**
	 * @param no
	 */
	public BusinessOrderVO(String no) {
		super();
		this.no = no;
	}

	/**
	 * 
	 */
	public BusinessOrderVO() {
		super();
	}
	
}
