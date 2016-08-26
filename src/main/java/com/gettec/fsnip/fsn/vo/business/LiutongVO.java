package com.gettec.fsnip.fsn.vo.business;

import java.util.List;

import com.gettec.fsnip.fsn.model.business.LiutongFieldValue;
import com.gettec.fsnip.fsn.model.business.LiutongToProduce;
import com.gettec.fsnip.fsn.model.business.LiutongToProduceLicense;

public class LiutongVO {

	List<LiutongFieldValue> fieldValues;
	LiutongToProduce liutongToProduce;
	List<LiutongToProduceLicense> listLtQs;
	
	public List<LiutongFieldValue> getFieldValues() {
		return fieldValues;
	}
	public void setFieldValues(List<LiutongFieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}
	public LiutongToProduce getLiutongToProduce() {
		return liutongToProduce;
	}
	public void setLiutongToProduce(LiutongToProduce liutongToProduce) {
		this.liutongToProduce = liutongToProduce;
	}
	public List<LiutongToProduceLicense> getListLtQs() {
		return listLtQs;
	}
	public void setListLtQs(List<LiutongToProduceLicense> listLtQs) {
		this.listLtQs = listLtQs;
	}
}
