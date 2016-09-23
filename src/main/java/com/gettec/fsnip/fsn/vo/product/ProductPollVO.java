package com.gettec.fsnip.fsn.vo.product;

import java.util.ArrayList;
import java.util.List;

public class ProductPollVO {

	private String productName;
	
	private String batchSerialNo;
	
	//["7/1", "7/2"]
	private List<String> days = new ArrayList<String>();
	
	//[66, 88]
	private List<Integer> likePerDay = new ArrayList<Integer>();
	
	//[33, 44]
	private List<Integer> complainantPerDay = new ArrayList<Integer>();
	
	//[3.3, 4.4]
	private List<Double> rateAvgPerDay = new ArrayList<Double>();
	
	private Integer like;
	
	private Integer complainant;
	
	private Double rateAvg;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public List<String> getDays() {
		return days;
	}

	public void setDays(List<String> days) {
		this.days = days;
	}

	public List<Integer> getLikePerDay() {
		return likePerDay;
	}

	public void setLikePerDay(List<Integer> likePerDay) {
		this.likePerDay = likePerDay;
	}

	public List<Integer> getComplainantPerDay() {
		return complainantPerDay;
	}

	public void setComplainantPerDay(List<Integer> complainantPerDay) {
		this.complainantPerDay = complainantPerDay;
	}

	public List<Double> getRateAvgPerDay() {
		return rateAvgPerDay;
	}

	public void setRateAvgPerDay(List<Double> rateAvgPerDay) {
		this.rateAvgPerDay = rateAvgPerDay;
	}

	public Integer getLike() {
		return like;
	}

	public void setLike(Integer like) {
		this.like = like;
	}

	public Integer getComplainant() {
		return complainant;
	}

	public void setComplainant(Integer complainant) {
		this.complainant = complainant;
	}

	public Double getRateAvg() {
		return rateAvg;
	}

	public void setRateAvg(Double rateAvg) {
		this.rateAvg = rateAvg;
	}

}
