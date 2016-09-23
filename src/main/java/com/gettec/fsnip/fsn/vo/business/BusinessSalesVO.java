package com.gettec.fsnip.fsn.vo.business;

import java.util.List;

import com.gettec.fsnip.fsn.vo.sales.RecommendBuyVO;
import com.gettec.fsnip.fsn.vo.sales.SalesCaseVO;

/**
 * 封装销售首页中的企业信息
 * Created by HY on 2015/5/6.
 */
public class BusinessSalesVO {
    private Long busId;
    private String busName;//企业名称
    private String busNote;//企业简介
    private String busAddress;// 联系地址
    private String busTel;// 联系电话
    private String busEmail;// 联系邮箱
    private String busContact;// 联系人
    private String qrCodeUrl;// 企业二维码
    private String publicityUrl;// 企业二维码

    private List<RecommendBuyVO> recommendBuyVOs; //推荐购买方式

    private List<SalesCaseVO> salesCaseVOs; //销售案例

    public BusinessSalesVO() {

    }

    public BusinessSalesVO(Long busId, String busName, String busNote, String busAddress,
                           String busTel, String busEmail, String busContact,
                           String qrCodeUrl,String publicityUrl, List<RecommendBuyVO> recommendBuyVOs,
                           List<SalesCaseVO> salesCaseVOs) {
        this.busId = busId;
        this.busName = busName;
        this.busNote = busNote;
        this.busAddress = busAddress;
        this.busTel = busTel;
        this.busEmail = busEmail;
        this.busContact = busContact;
        this.qrCodeUrl = qrCodeUrl;
        this.recommendBuyVOs = recommendBuyVOs;
        this.salesCaseVOs = salesCaseVOs;
        this.publicityUrl = publicityUrl;
    }

    public String getPublicityUrl() {
        return publicityUrl;
    }

    public void setPublicityUrl(String publicityUrl) {
        this.publicityUrl = publicityUrl;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusNote() {
        return busNote;
    }

    public void setBusNote(String busNote) {
        this.busNote = busNote;
    }

    public String getBusAddress() {
        return busAddress;
    }

    public void setBusAddress(String busAddress) {
        this.busAddress = busAddress;
    }

    public String getBusTel() {
        return busTel;
    }

    public void setBusTel(String busTel) {
        this.busTel = busTel;
    }

    public String getBusEmail() {
        return busEmail;
    }

    public void setBusEmail(String busEmail) {
        this.busEmail = busEmail;
    }

    public String getBusContact() {
        return busContact;
    }

    public void setBusContact(String busContact) {
        this.busContact = busContact;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public List<RecommendBuyVO> getRecommendBuyVOs() {
        return recommendBuyVOs;
    }

    public void setRecommendBuyVOs(List<RecommendBuyVO> recommendBuyVOs) {
        this.recommendBuyVOs = recommendBuyVOs;
    }

    public List<SalesCaseVO> getSalesCaseVOs() {
        return salesCaseVOs;
    }

    public void setSalesCaseVOs(List<SalesCaseVO> salesCaseVOs) {
        this.salesCaseVOs = salesCaseVOs;
    }
    
}
