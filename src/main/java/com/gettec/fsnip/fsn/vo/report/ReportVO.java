package com.gettec.fsnip.fsn.vo.report;

/**
 * Create Time 2015-03-25
 * 用于封装申请报告更新处理过程中返回的报告信息
 * @author HuangYog 
 * @email huangyong@fsnip.com
 */
public class ReportVO {
    private Long reportId;//报告id
    private String testDate; //检验日期
    private String reportType;//报告类型
    private String publicFlag;//报告状态
    
    public ReportVO() {}
    
    public ReportVO(Long reportId, String testDate, String reportType,String publicFlag) {
        this.reportId = reportId;
        this.testDate = testDate;
        this.reportType = reportType;
        this.publicFlag = publicFlag;
    }

    public Long getReportId() {
        return reportId;
    }
    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
    public String getReportType() {
        return reportType;
    }
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    public String getTestDate() {
        return testDate;
    }
    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(String publicFlag) {
        this.publicFlag = publicFlag;
    }
    
}
