package com.gettec.fsnip.fsn.vo.account;

import java.util.Date;

/**
 * Created by HY on 2015/5/19.
 * desc:
 */
public class RetrunAccountVO {

    private Long id;//单据id
    private String accountNo;//单据编号
    private String outBusName; // 供货商名称
    private String outBusLic; // 供货商营业执照
    private String inBusName;//购货商名称
    private String inBusLic;//购货商名称
    private String accountDate; // 交易时间
    private Integer outStatus; // 交易时间
    private Integer inStatus; // 交易时间

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getOutBusName() {
        return outBusName;
    }

    public void setOutBusName(String outBusName) {
        this.outBusName = outBusName;
    }

    public String getOutBusLic() {
        return outBusLic;
    }

    public void setOutBusLic(String outBusLic) {
        this.outBusLic = outBusLic;
    }

    public String getInBusName() {
        return inBusName;
    }

    public void setInBusName(String inBusName) {
        this.inBusName = inBusName;
    }

    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }

    public Integer getInStatus() {
        return inStatus;
    }

    public void setInStatus(Integer inStatus) {
        this.inStatus = inStatus;
    }

    public String getInBusLic() {
        return inBusLic;
    }

    public void setInBusLic(String inBusLic) {
        this.inBusLic = inBusLic;
    }
}
