package com.gettec.fsnip.fsn.vo.business;

/**
 * Created by xuetaoyang on 2016/10/26.
 */
public class BusinessVideoVo {
    private Long id; // 企业编号
    private String name; // 企业名称
    private String address; //企业地址
    private String busType; // 企业类型
    private int    count; //查询结果总数
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
