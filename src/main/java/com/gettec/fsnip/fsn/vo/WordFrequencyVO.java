package com.gettec.fsnip.fsn.vo;

import java.io.Serializable;

public class WordFrequencyVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8408578900507894072L;

	private String wordName;
	private Integer wordID;
	private String foodClassName;
	private int newsCount;
	private int weiboCount;
	private int total;
	
	
	/**
	 * 
	 */
	public WordFrequencyVO() {
		super();
	}

	/**
	 * @param wordName
	 * @param foodClassName
	 * @param newsCount
	 * @param weiboCount
	 * @param total
	 */
	public WordFrequencyVO(String wordName, String foodClassName,
			int newsCount, int weiboCount, int total, int id) {
		super();
		this.wordName = wordName;
		this.foodClassName = foodClassName;
		this.newsCount = newsCount;
		this.weiboCount = weiboCount;
		this.total = total;
		this.wordID = id;
	}
	
	public String getWordName() {
		return wordName;
	}
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	public String getFoodClassName() {
		return foodClassName;
	}
	public void setFoodClassName(String foodClassName) {
		this.foodClassName = foodClassName;
	}
	public int getNewsCount() {
		return newsCount;
	}
	public void setNewsCount(int newsCount) {
		this.newsCount = newsCount;
	}
	public int getWeiboCount() {
		return weiboCount;
	}
	public void setWeiboCount(int weiboCount) {
		this.weiboCount = weiboCount;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public Integer getWordID() {
		return wordID;
	}

	public void setWordID(Integer wordID) {
		this.wordID = wordID;
	}
}
