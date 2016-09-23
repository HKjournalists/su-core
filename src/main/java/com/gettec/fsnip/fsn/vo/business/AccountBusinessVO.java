package com.gettec.fsnip.fsn.vo.business;

/**
 * Created by HY on 2015/6/23.
 * desc: 台帐系统政府专员查看企业信息
 */
public class AccountBusinessVO {

    private String id; // 企业编号
    private String name; // 企业名称
    private String licNo; // 营业执照号
    private String personInCharge; // 法人代表
    private String busType; // 企业类型
    private String regDate; // 注册时间
    private String regAddr; // 注册地址
    private String linkMan; // 联系人
    private String linkTel; // 联系电话
    private String orgCode; //组织机构代码
    private String orgImage; //组织机构证
    private String licImage; //组织机构证
    private String email; //企业邮箱

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicNo() {
        return licNo;
    }

    public void setLicNo(String licNo) {
        this.licNo = licNo;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgImage() {
        return orgImage;
    }

    public void setOrgImage(String orgImage) {
        this.orgImage = orgImage;
    }

    public String getLicImage() {
        return licImage;
    }

    public void setLicImage(String licImage) {
        this.licImage = licImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountBusinessVO() {    }

}
