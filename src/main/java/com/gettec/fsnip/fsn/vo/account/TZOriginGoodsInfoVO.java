package com.gettec.fsnip.fsn.vo.account;

/**
 * Created by HY on 2015/6/4.
 * desc: 台账溯源产品详情VO
 */
public class TZOriginGoodsInfoVO {
    private Long id ;//顺序编号
    private Long productId;
    private String productName;
    private String barcode;
    private String qsNo;
    private String productBand;//产品商标
    private String format;
    private String place; //产地
    private Long busId;
    private String busName;


    public TZOriginGoodsInfoVO() {    }

    public TZOriginGoodsInfoVO(Long productId, String productName, String barcode, String qsNo, String productBand, String format, String place) {
        this.productId = productId;
        this.productName = productName;
        this.barcode = barcode;
        this.qsNo = qsNo;
        this.productBand = productBand;
        this.format = format;
        this.place = place;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQsNo() {
        return qsNo;
    }

    public void setQsNo(String qsNo) {
        this.qsNo = qsNo;
    }

    public String getProductBand() {
        return productBand;
    }

    public void setProductBand(String productBand) {
        this.productBand = productBand;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
