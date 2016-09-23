package com.lhfs.fsn.vo.atgoo;

import java.io.Serializable;

/**
 * 产品营养报告VO 提供给爱特够系统使用
 * @author tangxin 2016/6/26
 *
 */
public class NutritionVO implements Serializable{

	/**
	 * 项目
	 */
	String name;
	/**
	 * 含量
	 */
	String value;
	/**
	 * 单位
	 */
	String unit;
	/**
	 * 条件
	 */
	String per;
	/**
	 * 每日所需营养参考值
	 */
	String nrv;
	/**
	 * 每日推荐摄入量
	 */
	String dailyIntake;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPer() {
		return per;
	}
	public void setPer(String per) {
		this.per = per;
	}
	public String getNrv() {
		return nrv;
	}
	public void setNrv(String nrv) {
		this.nrv = nrv;
	}
	public String getDailyIntake() {
		return dailyIntake;
	}
	public void setDailyIntake(String dailyIntake) {
		this.dailyIntake = dailyIntake;
	}
	
}
