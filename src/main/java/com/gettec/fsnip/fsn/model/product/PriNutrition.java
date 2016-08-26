package com.gettec.fsnip.fsn.model.product;

import com.gettec.fsnip.fsn.model.common.Model;

public class PriNutrition extends Model {

	private static final long serialVersionUID = -6477887154455534151L;
	
	private String name;    //营养名称
	private String unit;    //营养单位
	private String value;   //每单位中的含量
	private String dailyIntake;   //每一天应摄入的量
	private String nrv;     //所占一天摄入量百分比
	private String per;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDailyIntake() {
		return dailyIntake;
	}
	public void setDailyIntake(String dailyIntake) {
		this.dailyIntake = dailyIntake;
	}
	public String getNrv() {
		return nrv;
	}
	public void setNrv(String nrv) {
		this.nrv = nrv;
	}
	public String getPer() {
		return per;
	}
	public void setPer(String per) {
		this.per = per;
	}

}
