package com.gettec.fsnip.fsn.vo.account;

import java.util.List;

/**
 * Created by HY on 2015/5/18.
 * desc:封装前台提交的台账退货信息
 */
public class AccountOutVO {
    private Long id;
    private Long accountNo;
    private Long outBusId;
    private Long inBusId;
    private int outStatus;
    private int inStatus;
    private int type;
    private String inDate;
    private String outDate;
    private List<ReturnProductVO> proList;
    
    //add by ltg 20160815 增加字段记录退货商名称和供应商名称 start
    private String outBusName;
    private String inBusName;
    //add by ltg 20160815 增加字段记录退货商名称和供应商名称 end

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public int getInStatus() {
        return inStatus;
    }

    public void setInStatus(int inStatus) {
        this.inStatus = inStatus;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public Long getOutBusId() {
        return outBusId;
    }

    public void setOutBusId(Long outBusId) {
        this.outBusId = outBusId;
    }

    public Long getInBusId() {
        return inBusId;
    }

    public void setInBusId(Long inBusId) {
        this.inBusId = inBusId;
    }

    public int getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(int outStatus) {
        this.outStatus = outStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ReturnProductVO> getProList() {
        return proList;
    }

    public void setProList(List<ReturnProductVO> proList) {
        this.proList = proList;
    }

	public String getOutBusName() {
		return outBusName;
	}

	public void setOutBusName(String outBusName) {
		this.outBusName = outBusName;
	}

	public String getInBusName() {
		return inBusName;
	}

	public void setInBusName(String inBusName) {
		this.inBusName = inBusName;
	}

}