package com.gettec.fsnip.fsn.vo.account;

import java.util.Date;

/**
 * 台账系统企业供销关系企业VO
 * @author HuangYog 
 * @email HuangYong@fsnip.com
 * Create Date 2015-05-17
 */
public class BusRelationVO {
    private Long id;
    private String busName;//企业名称
    private String licNo;//营业执照号
    private int type; //供销关系 0代表供应，1代表销售
    private String createDate;//创建时间
    private String contact;//连续方式
    private String address;//联系地址

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBusName() {
        return busName;
    }
    public void setBusName(String busName) {
        this.busName = busName;
    }
    public String getLicNo() {
        return licNo;
    }
    public void setLicNo(String licNo) {
        this.licNo = licNo;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    
    public BusRelationVO(Long id, String busName, String licNo, int type,
            String createDate) {
        super();
        this.id = id;
        this.busName = busName;
        this.licNo = licNo;
        this.type = type;
        this.createDate = createDate;
    }
    
    public BusRelationVO() {    }
    
    
}
