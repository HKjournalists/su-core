package com.gettec.fsnip.fsn.model.wdn;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hughxiang on 2016/1/22.
 */
@Entity(name = "wdn_order_info")
public class WdnOrderInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long ID;

    @Column(name="orderId")
    private String orderId;

    @Column(name="title")
    private String title;

    @Column(name="issn")
    private String issn;

    @Column(name="authors")
    private String authors;

    @Column(name="year")
    private String year;//出版年

    @Column(name="volume")
    private String volume;//卷

    @Column(name="issue")
    private String issue;//期

    @Column(name="journal")
    private String journal;//期刊

    @Column(name="dataSource")
    private String dataSource;

    @Column(name="applyDate")
    private Date applyDate;//请求时间

    @Column(name="userId")
    private Long userId;

    @Column(name="realName")
    private String userRealName;

    @Column(name="phone")
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="status")
    private String status;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WdnOrderInfo() {
    }

    public WdnOrderInfo(String orderId, String title, String issn, String authors, String year, String volume,
                        String issue, String journal, String dataSource, Date applyDate, Long userId, String phone, String email) {
        this.orderId = orderId;
        this.title = title;
        this.issn = issn;
        this.authors = authors;
        this.year = year;
        this.volume = volume;
        this.issue = issue;
        this.journal = journal;
        this.dataSource = dataSource;
        this.applyDate = applyDate;
        this.userId = userId;
        this.phone = phone;
        this.email = email;
    }
}
