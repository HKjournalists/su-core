package com.gettec.fsnip.fsn.vo.account;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品退货VO
 * @author HuangYog productId
 * @email HuangYong@fsnip.com
 */
public class ReturnProductVO {

    private Long id; //id
    private Long productId; //产品id
    private String name; //商品名称
    private String barcode;//条形码
    private String qsNumber;//QS号
    private String format;//产品规格型号
    private Long count;//数量
    private BigDecimal price;//单价
    private String productionDate;//生产日期
    private String batch;//批次
    private String overDate;//过期日期
    private int type; // 类型，0：加库存，1：减库存
    private Long returnCount;// 退货数量
    private Long countTotal;// 退货初始总数量
    private int busType;//1:生产企业 0：非生产企业
    private List<ProductionDateVO> birthDateList; //产品实例生产日期
    private List<String> qsNoList;
    private String uuid;//一条记录的唯一标识
    private Long busId;//当前企业ID
    private int expday;//产品有效天数

    private Long reportId;
    private String reportUrl;


    private String createType;//标识数据来源 流水库存:0，台账库存:1，2用户手动修改库存数据
    
    private String problem_describe;//问题描述

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getQsNoList() {
        return qsNoList;
    }

    public int getBusType() {
        return busType;
    }


    public void setBusType(int busType) {
        this.busType = busType;
    }


    public void setQsNoList(List<String> qsNoList) {
        this.qsNoList = qsNoList;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ProductionDateVO> getBirthDateList() {
        return birthDateList;
    }

    public void setBirthDateList(List<ProductionDateVO> birthDateList) {
        this.birthDateList = birthDateList;
    }

    public Long getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Long returnCount) {
        this.returnCount = returnCount;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Long countTotal) {
        this.countTotal = countTotal;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public int getExpday() {
        return expday;
    }

    public void setExpday(int expday) {
        this.expday = expday;
    }

	public String getProblem_describe() {
		return problem_describe;
	}

	public void setProblem_describe(String problem_describe) {
		this.problem_describe = problem_describe;
	}
}