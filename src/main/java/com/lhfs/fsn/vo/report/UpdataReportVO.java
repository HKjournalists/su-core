package com.lhfs.fsn.vo.report;

import java.util.Date;

/**
 * Create Time 2015-03-25
 * 用于封装portal报告更新申请
 * @author HuangYog 
 * @email huangyong@fsnip.com
 */
public class UpdataReportVO {
    private Long id;
    private Long productId;//产品id
    private String reportType;//需要跟新的报告类型
    private Date applyDate; //申请时间
    private Integer applyTimes;// 申请次数
    private String productBarcode;// 产品条形码
    private String productName;// 产品条形码
    private String updateTime;// 跟新时间，用于向portal 发送跟新消息

    public UpdataReportVO() {}

    public UpdataReportVO(Long id, Long productId, String reportType,Date applyDate, Integer applyTimes) {
        this.id = id;
        this.productId = productId;
        this.reportType = reportType;
        this.applyDate = applyDate;
        this.applyTimes = applyTimes;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getReportType() {
        return reportType;
    }
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
    public Date getApplyDate() {
        return applyDate;
    }
    
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
    public Integer getApplyTimes() {
        return applyTimes;
    }
    public void setApplyTimes(Integer applyTimes) {
        this.applyTimes = applyTimes;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    
}
