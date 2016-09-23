package com.lhfs.fsn.vo.report;

/**
 * Create Time 2015-03-25
 * 向portal发送报告更新消息的封装
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 */
public class ReportUpdateMsgVO {
    private Long productId;
    private String reportType;
    private String updateTime;
    
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
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
