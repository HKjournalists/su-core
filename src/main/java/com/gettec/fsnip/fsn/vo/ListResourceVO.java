package com.gettec.fsnip.fsn.vo;

import java.io.Serializable;
import java.util.List;

import com.gettec.fsnip.fsn.model.market.Resource;

public class ListResourceVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Resource> listResource;
	/*报告检测类别，如果是政府抽检，在合成pdf的时候，需要截取前两页*/
	String reportTestType;

	public List<Resource> getListResource() {
		return listResource;
	}

	public void setListResource(List<Resource> listResource) {
		this.listResource = listResource;
	}

	public String getReportTestType() {
		return reportTestType;
	}

	public void setReportTestType(String reportTestType) {
		this.reportTestType = reportTestType;
	}
	
}
