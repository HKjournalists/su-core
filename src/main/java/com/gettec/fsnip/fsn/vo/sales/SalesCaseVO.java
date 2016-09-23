package com.gettec.fsnip.fsn.vo.sales;

import com.gettec.fsnip.fsn.model.sales.SalesCase;
import com.gettec.fsnip.fsn.model.sales.SalesResource;

import java.util.List;

/**
 * Created by HY on 2015/5/5.
 */
public class SalesCaseVO {
    private Long id;
    private String salesCaseName;//销售案例名称
    private String salesDetails;//客户名称
    private String guid;
    private String url; // 销售案例封面

    private List<SalesResource> resource; // 销售案例相册资源

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<SalesResource> getResource() {
        return resource;
    }

    public void setResource(List<SalesResource> resource) {
        this.resource = resource;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalesCaseName() {
        return salesCaseName;
    }

    public void setSalesCaseName(String salesCaseName) {
        this.salesCaseName = salesCaseName;
    }

    public String getSalesDetails() {
        return salesDetails;
    }

    public void setSalesDetails(String salesDetails) {
        this.salesDetails = salesDetails;
    }

    public SalesCaseVO() {
    }

    public SalesCaseVO(Long id, String salesCaseName, String salesDetails,String url) {
        this.id = id;
        this.salesCaseName = salesCaseName;
        this.salesDetails = salesDetails;
        this.url = url;
    }

    /**
     * 用于将数据库中查出来的对象封装到VO中
     * @author HY
     * Created Date 2015-05-04
     */
    public SalesCaseVO(SalesCase salescase) {
        if(salescase == null) {
            return;
        }
        this.id = salescase.getId();
        this.salesCaseName = salescase.getName();
        this.salesDetails = salescase.getDescription();
        this.guid = salescase.getGuid();
    }

    public SalesCase toEntity(SalesCase salesCase) {
        if(salesCase == null) {
            salesCase = new SalesCase();
        }
        salesCase.setName(this.salesCaseName);
        salesCase.setDescription(this.getSalesDetails());
        return salesCase;
    }
}
