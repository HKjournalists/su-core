package com.lhfs.fsn.vo.report;

/**
 * 食安监报告VO
 * @author YongHuang
 */
public class ResultToShianjianVO {
    private ReportVO selfReport;//自检报告
    private ReportVO inspectReport;//送检报告
    private ReportVO casualReport;//抽检报告
    
    public ReportVO getSelfReport() {
        return selfReport;
    }
    public void setSelfReport(ReportVO selfReport) {
        this.selfReport = selfReport;
    }
    public ReportVO getInspectReport() {
        return inspectReport;
    }
    public void setInspectReport(ReportVO inspectReport) {
        this.inspectReport = inspectReport;
    }
    public ReportVO getCasualReport() {
        return casualReport;
    }
    public void setCasualReport(ReportVO casualReport) {
        this.casualReport = casualReport;
    }
}
