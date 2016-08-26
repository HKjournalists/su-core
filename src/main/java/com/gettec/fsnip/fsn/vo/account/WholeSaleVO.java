package com.gettec.fsnip.fsn.vo.account;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批发台账VO
 * @author chenxiaolin 
 */
public class WholeSaleVO {
    private Long id; //台账ID
    //private Long productId;//商品ID
    private String name; //商品名称
    private String barcode;//条形码
    private String qsNumber;//QS号
    private String format;//产品规格型号
    private Long  count;//数量
    private BigDecimal price;//单价
    private String productionDate;//生产日期
    private String batch;//批次
    private String overDate;//过期日期
    
   /* public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}*/
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
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getQsNumber() {
        return qsNumber;
    }
    public void setQsNumber(String qsNumber) {
        this.qsNumber = qsNumber;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getProductionDate() {
        return productionDate;
    }
    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
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
    
}
