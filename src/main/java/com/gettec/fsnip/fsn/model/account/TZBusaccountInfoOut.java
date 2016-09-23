package com.gettec.fsnip.fsn.model.account;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HY on 2015/5/19.
 * desc:台账  保存供销关系中的产品信息
 */
@Entity(name = "tz_business_account_info_out")
public class TZBusaccountInfoOut extends Model {

    private static final long serialVersionUID = 5455742964435823189L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "business_account_id")
    private Long busAccountId;//台帐ID

    @Column(name = "product_id")
    private Long productId; //产品id

    @Column(name = "product_num")
    private Long productNum;//商品数量

    @Column(name = "product_price")
    private BigDecimal productPrice;// 商品单价

    @Column(name = "product_batch")
    private String productBatch; //商品批次

    @Column(name = "qs_no")
    private String qsNo; //商品qs号

    @Column(name = "production_date")
    private String productionDate;//生产日期

    @Column(name = "over_date")
    private String overDate;//过期日期
    
    @Column(name = "problem_describe")
    private String problem_describe;//问题描述

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusAccountId() {
        return busAccountId;
    }

    public void setBusAccountId(Long busAccountId) {
        this.busAccountId = busAccountId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductBatch() {
        return productBatch;
    }

    public void setProductBatch(String productBatch) {
        this.productBatch = productBatch;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getOverDate() {
        return overDate;
    }

    public void setOverDate(String overDate) {
        this.overDate = overDate;
    }

    public String getQsNo() {
        return qsNo;
    }

    public void setQsNo(String qsNo) {
        this.qsNo = qsNo;
    }

    public String getProblem_describe() {
		return problem_describe;
	}

	public void setProblem_describe(String problem_describe) {
		this.problem_describe = problem_describe;
	}

	public TZBusaccountInfoOut() {
    }

    public TZBusaccountInfoOut(Long busAccountId, Long productId,
                               Long productNum, BigDecimal productPrice,
                               String productBatch, String productionDate, String overDate) {
        this.busAccountId = busAccountId;
        this.productId = productId;
        this.productNum = productNum;
        this.productPrice = productPrice;
        this.productBatch = productBatch;
        this.productionDate = productionDate;
        this.overDate = overDate;
    }

    public TZBusaccountInfoOut(ReturnProductVO vo) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.id = vo.getId();
        this.productId = vo.getProductId();
        this.productNum = vo.getCount();
        this.productPrice = vo.getPrice();
        this.productBatch = vo.getBatch();
        this.qsNo = vo.getQsNumber();
        this.productionDate = vo.getProductionDate()!=null?vo.getProductionDate():"";
        this.overDate = vo.getOverDate()!=null?vo.getOverDate():"";
    }
}
