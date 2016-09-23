package com.gettec.fsnip.fsn.model.account;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.vo.account.AccountOutVO;

import javax.persistence.*;
import java.util.List;

/**
 * Created by HY on 2015/5/18.
 * desc:台账 保存供销关系中的企业信息
 */
@Entity(name = "tz_business_account_out")
public class TZBusAccountOut extends Model {

    private static final long serialVersionUID = -883243161844527912L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_no")
    private Long accountNo;//单据编号

    @Column(name = "out_business_id")
    private Long outBusId;//退货企业ID

    @Column(name = "in_business_id")
    private Long inBusId;//收货企业ID

    @Column(name = "create_time")
    private String createDate;//创建时间

    @Column(name = "out_status")
    private int outStatus;//退货状态，0：未确认，1：确认

    @Column(name = "in_status")
    private int inStatus;//收货状态，0：未确认，1：确认

    @Column(name = "out_date")
    private String outDate;//退货日期

    @Column(name = "in_date")
    private String inDate; //收货日期

    @Column(name = "account_id")
    private String accountId; //用于追溯是那张收货单产生的退货信息

    @Transient
    private List<TZBusaccountInfoOut> outInfoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        accountNo = accountNo;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(int outStatus) {
        this.outStatus = outStatus;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<TZBusaccountInfoOut> getOutInfoList() {
        return outInfoList;
    }

    public void setOutInfoList(List<TZBusaccountInfoOut> outInfoList) {
        this.outInfoList = outInfoList;
    }

    public TZBusAccountOut() {

    }

    public TZBusAccountOut(AccountOutVO vo) {
        if(vo instanceof AccountOutVO){
            this.id = vo.getId();
            this.accountNo = vo.getAccountNo();
            this.outBusId = vo.getOutBusId();
            this.inBusId = vo.getInBusId();
            this.outStatus = vo.getOutStatus();
        }
    }

    public TZBusAccountOut setTZBusAccountOut(AccountOutVO vo){
        TZBusAccountOut accountOut = null;
        if(vo instanceof AccountOutVO){
            accountOut = new TZBusAccountOut();
            accountOut.setId(vo.getId());
            accountOut.setAccountNo(vo.getAccountNo());
            accountOut.setOutBusId(vo.getOutBusId());
            accountOut.setInBusId(vo.getInBusId());
            accountOut.setOutStatus(vo.getOutStatus());
            accountOut.setOutDate(vo.getOutDate());
            accountOut.setInDate(vo.getInDate());
        }
        return accountOut ;
    }
}
