package com.tzapp.fsn.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认收货时前台封装传到后台的Vo对象
 */
public class ConfirmReceiptVO {

	private Long org;//组织机构ID
	private Long tzId;//收货单ID(台账ID)
	private Long id;//产品详情ID
	private Long returnCount;//实收数量
	private List<ConfirmReceiptVO> listVO = 
		new ArrayList<ConfirmReceiptVO>();//产品详情List
	
	public Long getOrg() {
		return org;
	}
	public void setOrg(Long org) {
		this.org = org;
	}
	public Long getTzId() {
		return tzId;
	}
	public void setTzId(Long tzId) {
		this.tzId = tzId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(Long returnCount) {
		this.returnCount = returnCount;
	}
	public List<ConfirmReceiptVO> getListVO() {
		return listVO;
	}
	public void setListVO(List<ConfirmReceiptVO> listVO) {
		this.listVO = listVO;
	}
	
}
