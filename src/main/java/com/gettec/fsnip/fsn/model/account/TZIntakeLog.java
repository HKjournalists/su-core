package com.gettec.fsnip.fsn.model.account;

import com.gettec.fsnip.fsn.model.common.Model;

import javax.persistence.*;

/**
 * Created by HY on 2015/7/6.
 * desc: 商品入库操作日志！
 */
@Entity(name = "tz_intake_log")
public class TZIntakeLog extends Model {
    private static final long serialVersionUID = 6636664837651586203L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_id")
    private Long busId;	 //当前企业ID

    @Column(name = "product_id")
    private Long productId;	 //当前产品ID

    @Column(name = "ops_date")
    private String operationDate;	 //操作时间

    @Column(name = "operator")
    private String operator;	 //操作人员

    @Column(name = "pos_count")
    private long operationCount;	 //操作数量

    @Column(name = "stock_id")
    private Long stockId;	 //库存主表id

    @Column(name = "stockinfo_id")
    private Long stockinfoId ;	 //库存详情表id

    @Column(name = "UUID")
    private String uuid ;	 //库存详情表id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getOperationCount() {
        return operationCount;
    }

    public void setOperationCount(long operationCount) {
        this.operationCount = operationCount;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStockinfoId() {
        return stockinfoId;
    }

    public void setStockinfoId(Long stockinfoId) {
        this.stockinfoId = stockinfoId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
