package com.gettec.fsnip.fsn.model.market;

import javax.persistence.*;

import com.gettec.fsnip.fsn.model.common.Model;
import java.util.Date;

/**
 * 用于封装portal更新报告 信息
 * @author HuangYog
 *
 */
@Entity(name="update_report")
public class UpdataReport extends Model {
    
    /**
     * 
     */
    private static final long serialVersionUID = 924816485860692685L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID")
    private Long id;

    @Column(name="PRODUCT_ID", nullable=false)
    private Long productId;//产品id

    @Column(name="REPORT_TYPE", nullable=false)
    private String reportType;//需要跟新的报告类型
    
    //@Temporal(TemporalType.TIMESTAMP)
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="APPLY_DATE")
    private Date applyDate; //申请时间
    
    @Column(name="PRODUCT_BARCODE",nullable=false )
    private String productBarcode;//产品条形码

    @Column(name="PRODUCT_NAME",nullable=false )
    private String productName;//产品名称

    @Column(name="APPLY_TIMES",nullable=false)
    private Integer applyTimes;// 申请次数
    
    @Column(name="ORGANIZATION",nullable=false)
    private Long organization;// 组织机构
    
    @Column(name="HANDLE_STATUS",nullable=false)
    private Integer handleStatus;// 当前处于的状态   0 待处理状态  1 待处理中   2 已处理状态

    public UpdataReport() {}

    public UpdataReport(Long id, Long productId, String reportType,
            Date applyDate, String productBarcode, Integer applyTimes) {
        super();
        this.id = id;
        this.productId = productId;
        this.reportType = reportType;
        this.applyDate = applyDate;
        this.productBarcode = productBarcode;
        this.applyTimes = applyTimes;
    }
    
    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getReportType() {
        return reportType;
    }
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    public Date getApplyDate() {
        return applyDate;
    }
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
    public Integer getApplyTimes() {
        return applyTimes;
    }
    public void setApplyTimes(Integer applyTimes) {
        this.applyTimes = applyTimes;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }
    
    
}
