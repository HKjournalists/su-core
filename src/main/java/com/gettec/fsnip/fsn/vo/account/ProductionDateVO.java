package com.gettec.fsnip.fsn.vo.account;

/**
 * Created by HY on 2015/5/22.
 * desc:
 */
public class ProductionDateVO {
    private String instanceId;
    private String birthDate;
    private String batch;
    private String overDate;
    private String reportId;
    private Boolean expire; //是否过期  true过期，false 没有过期

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getOverDate() {
        return overDate;
    }

    public void setOverDate(String overDate) {
        this.overDate = overDate;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Boolean getExpire() {
        return expire;
    }

    public void setExpire(Boolean expire) {
        this.expire = expire;
    }
}