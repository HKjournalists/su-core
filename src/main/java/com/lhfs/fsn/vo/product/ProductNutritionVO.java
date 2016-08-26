package com.lhfs.fsn.vo.product;

/**
 * 大众门户产品营养VO
 * @author ZhaWanNeng
 */

public class ProductNutritionVO  {
	
	private String name; // 营养名称
	
	private String unit; // 每个计算单位中的营养值
	
	private String value;
	
	private String per;   // 计算单位如每100ml,每份
	
	private String nrv;
	
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

	
}
