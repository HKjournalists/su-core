package com.lhfs.fsn.vo.product;

import java.util.Date;

/**
 * 食安监产品溯源VO
 * @author YongHuang
 */
public class SajTraceabilityVO {
	 private String resourceBusName;//来源企业
	 private String directionBusName;//去向企业
	 private long receiveNum;//收货数量
	 private Date receiveDate;//收货日期

	public String getResourceBusName() {
		return resourceBusName;
	}

	public void setResourceBusName(String resourceBusName) {
		this.resourceBusName = resourceBusName;
	}

	public String getDirectionBusName() {
		return directionBusName;
	}

	public void setDirectionBusName(String directionBusName) {
		this.directionBusName = directionBusName;
	}

	public long getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(long receiveNum) {
		this.receiveNum = receiveNum;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

}
