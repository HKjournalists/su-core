package com.gettec.fsnip.fsn.vo.account;

import java.util.List;

/**
 * Created by HY on 2015/5/20.
 * desc:
 */
public class ReturnGoodsVO {

    private Long busAccountId;
    private int outStatus;
    private int inStatus;
    private String outTime; //退货时间
    private String inTime; //进货时间
    private BusRelationVO busRelationVO; // 对方企业
    private BusRelationVO outRelationVO;
    private List<ReturnProductVO> returnProductVOList; // 退货产品

    public Long getBusAccountId() {
        return busAccountId;
    }

    public void setBusAccountId(Long busAccountId) {
        this.busAccountId = busAccountId;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public BusRelationVO getBusRelationVO() {
        return busRelationVO;
    }

    public void setBusRelationVO(BusRelationVO busRelationVO) {
        this.busRelationVO = busRelationVO;
    }

    public List<ReturnProductVO> getReturnProductVOList() {
        return returnProductVOList;
    }

    public void setReturnProductVOList(List<ReturnProductVO> returnProductVOList) {
        this.returnProductVOList = returnProductVOList;
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

    public BusRelationVO getOutRelationVO() {
        return outRelationVO;
    }

    public void setOutRelationVO(BusRelationVO outRelationVO) {
        this.outRelationVO = outRelationVO;
    }
}
