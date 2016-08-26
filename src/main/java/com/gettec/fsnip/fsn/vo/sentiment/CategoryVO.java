package com.gettec.fsnip.fsn.vo.sentiment;

import java.io.Serializable;

public class CategoryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long Subject_Category_ID; //分类ID
	private Long Client_ID; //客户ID
	private Long My_Website_Group_ID; ///网站分组ID
	
	
	public Long getSubject_Category_ID() {
		return Subject_Category_ID;
	}
	public void setSubject_Category_ID(Long subject_Category_ID) {
		Subject_Category_ID = subject_Category_ID;
	}
	public Long getClient_ID() {
		return Client_ID;
	}
	public void setClient_ID(Long client_ID) {
		Client_ID = client_ID;
	}
	public Long getMy_Website_Group_ID() {
		return My_Website_Group_ID;
	}
	public void setMy_Website_Group_ID(Long my_Website_Group_ID) {
		My_Website_Group_ID = my_Website_Group_ID;
	}
	
	
	
	
}
