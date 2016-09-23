package com.gettec.fsnip.fsn.vo.sentiment;

import java.io.Serializable;

//主题VO
public class SubjectVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String Subject_ID;//主题ID
	private Long Subject_Category_ID; //主题分类ID
	private Long Parent_Subject_Category_ID ;//上一级主题分类ID
	
	
	
	public String getSubject_ID() {
		return Subject_ID;
	}
	public void setSubject_ID(String subject_ID) {
		Subject_ID = subject_ID;
	}
	public Long getSubject_Category_ID() {
		return Subject_Category_ID;
	}
	public void setSubject_Category_ID(Long subject_Category_ID) {
		Subject_Category_ID = subject_Category_ID;
	}
	public Long getParent_Subject_Category_ID() {
		return Parent_Subject_Category_ID;
	}
	public void setParent_Subject_Category_ID(Long parent_Subject_Category_ID) {
		Parent_Subject_Category_ID = parent_Subject_Category_ID;
	}
}
