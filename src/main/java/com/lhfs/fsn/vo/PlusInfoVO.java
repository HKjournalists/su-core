package com.lhfs.fsn.vo;

public class PlusInfoVO {
	private String is_sampling = "0";	//通过政府抽检产品合格（0：否、1：是）
	private String is_active = "0";	//主动送检且合格（0：否、1：是）
	private String is_better = "0";	//特征性指标优于国家产品标准（0：否、1：是）
	private String organic_food = "0";	//有机食品（0：否、1：是）
	private String green_food = "0";	//绿色食品（0：否、1：是）
	private String pollution_free_food = "0";	//无公害食品（0：否、1：是）
	private String non_gmo_food = "0";	//非转基因食品（0：否、1：是）
	private String international_certification = "0";	//国际食品认证（0：否、1：是）
	public String getIs_sampling() {
		return is_sampling;
	}
	public void setIs_sampling(String is_sampling) {
		this.is_sampling = is_sampling;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	public String getIs_better() {
		return is_better;
	}
	public void setIs_better(String is_better) {
		this.is_better = is_better;
	}
	public String getOrganic_food() {
		return organic_food;
	}
	public void setOrganic_food(String organic_food) {
		this.organic_food = organic_food;
	}
	public String getGreen_food() {
		return green_food;
	}
	public void setGreen_food(String green_food) {
		this.green_food = green_food;
	}
	public String getPollution_free_food() {
		return pollution_free_food;
	}
	public void setPollution_free_food(String pollution_free_food) {
		this.pollution_free_food = pollution_free_food;
	}
	public String getNon_gmo_food() {
		return non_gmo_food;
	}
	public void setNon_gmo_food(String non_gmo_food) {
		this.non_gmo_food = non_gmo_food;
	}
	public String getInternational_certification() {
		return international_certification;
	}
	public void setInternational_certification(String international_certification) {
		this.international_certification = international_certification;
	}
}
