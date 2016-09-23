package com.gettec.fsnip.fsn.vo.sales;

import com.gettec.fsnip.fsn.model.sales.SalesResource;

import java.util.List;

/**
 * 用于封装销售案例的案例信息及图片集
 * Created by HY on 2015/5/7.
 */
public class SalesCaseAlbumVO {
    private String name;
    private String details;
    private List<SalesResource> urlList;

    public SalesCaseAlbumVO(String name, String details, List<SalesResource> urlList) {
        this.name = name;
        this.details = details;
        this.urlList = urlList;
    }

    public SalesCaseAlbumVO() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<SalesResource> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<SalesResource> urlList) {
        this.urlList = urlList;
    }
}
