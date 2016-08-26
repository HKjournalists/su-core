package com.gettec.fsnip.fsn.vo.account;

/**
 * Created by HY on 2015/5/20.
 * desc:
 */
public class TZStoreVO {

    private Long ztStoreId;
    private Long productId;
    private String barcode;
    private String productName;
    private String sqNo;
    private String productFormat;
    private Long productNum;

    public Long getZtStoreId() {
        return ztStoreId;
    }

    public void setZtStoreId(Long ztStoreId) {
        this.ztStoreId = ztStoreId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSqNo() {
        return sqNo;
    }

    public void setSqNo(String sqNo) {
        this.sqNo = sqNo;
    }

    public String getProductFormat() {
        return productFormat;
    }

    public void setProductFormat(String productFormat) {
        this.productFormat = productFormat;
    }

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }
}
