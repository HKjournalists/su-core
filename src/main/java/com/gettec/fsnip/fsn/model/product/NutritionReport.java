package com.gettec.fsnip.fsn.model.product;

import java.util.List;

import com.gettec.fsnip.fsn.model.common.Model;

public class NutritionReport extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String per;   //计算单位
	private String cstm;  //适宜人群
	private String ingredient;  //主要成分
	private List<PriNutrition> list;

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public String getCstm() {
		return cstm;
	}

	public void setCstm(String cstm) {
		this.cstm = cstm;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public List<PriNutrition> getList() {
		return list;
	}

	public void setList(List<PriNutrition> nlist) {
		this.list = nlist;
	}

	@Override
	public String toString() {
		return "NutritionReport [per=" + per + ", cstm=" + cstm
				+ ", ingredient=" + ingredient + ", list=" + list + "]";
	}
}
