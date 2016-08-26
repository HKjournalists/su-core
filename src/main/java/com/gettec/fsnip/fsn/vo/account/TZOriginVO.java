package com.gettec.fsnip.fsn.vo.account;

import java.util.List;

/**
 * Created by HY on 2015/6/4.
 * desc:台账溯源VO
 */
public class TZOriginVO {
    private Long trailId;//轨迹记录id
    private Long supplyBusId;//供应商主体ID
    private String supplyBusName;//供应商主体Name
    private long storeNum;//商品库存数量
    private Long tradingBusId;//交易记录的企业ID
    private String tradingBusName;//交易记录的企业Name
    private String tradingType;//交易类型
    private long tradingNum;//交易数量
    private String tradingbatch;//交易批次
    private String tradingDate;//交易时间
    private boolean hasChildren;
    private String showName;//显示的树形名称
    private  String batch ;
    private  String id ;

    private List<TZOriginVO> items;

    public Long getTrailId() {
        return trailId;
    }

    public void setTrailId(Long trailId) {
        this.trailId = trailId;
    }

    public Long getSupplyBusId() {
        return supplyBusId;
    }

    public void setSupplyBusId(Long supplyBusId) {
        this.supplyBusId = supplyBusId;
    }

    public String getSupplyBusName() {
        return supplyBusName;
    }

    public void setSupplyBusName(String supplyBusName) {
        this.supplyBusName = supplyBusName;
    }

    public long getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(long storeNum) {
        this.storeNum = storeNum;
    }

    public Long getTradingBusId() {
        return tradingBusId;
    }

    public void setTradingBusId(Long tradingBusId) {
        this.tradingBusId = tradingBusId;
    }

    public String getTradingBusName() {
        return tradingBusName;
    }

    public void setTradingBusName(String tradingBusName) {
        this.tradingBusName = tradingBusName;
    }

    public String getTradingType() {
        return tradingType;
    }

    public void setTradingType(String tradingType) {
        this.tradingType = tradingType;
    }

    public long getTradingNum() {
        return tradingNum;
    }

    public void setTradingNum(long tradingNum) {
        this.tradingNum = tradingNum;
    }

    public String getTradingbatch() {
        return tradingbatch;
    }

    public void setTradingbatch(String tradingbatch) {
        this.tradingbatch = tradingbatch;
    }

    public String getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(String tradingDate) {
        this.tradingDate = tradingDate;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
