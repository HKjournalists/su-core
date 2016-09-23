package com.lhfs.fsn.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 监管溯源VO
 * @author YongHuang
 */
public class TraceabilityVO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id; //企业id
    private String name;     // 企业名称
    private String address;  //地址
    private String type;     //企业类型
    private Long organization;  // 组织机构ID
    private String resourceBusName;//来源企业
    private String directionBusName;//去向企业
    private Date outDate;//发货日期
    private Date receiveDate;//收货日期
    private long outNum;//发货数量
    private long receiveNum;//收货数量
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

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

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public long getOutNum() {
		return outNum;
	}

	public void setOutNum(long outNum) {
		this.outNum = outNum;
	}

	public long getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(long receiveNum) {
		this.receiveNum = receiveNum;
	}
}
