package com.gettec.fsnip.fsn.model.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.vo.account.ReturnProductVO;

/**
 * Entity<br> 库存主表
 * @author: chenxiaolin
 */

@Entity(name = "tz_stock")
public class TZStock extends Model{

    private static final long serialVersionUID = 874496855894016138L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "business_id")
	private Long businessId;	 //当前企业ID
	
	@Column(name = "product_id")
	private Long productId;	 //产品ID
	
	@Column(name = "product_num")
	private Long productNum;  // 商品数量

	@Column(name = "qs_no")
	private String qsNo;  // 商品qs号

	@Column(name = "report_status")
	private int reportStatus;  // 0 没有报告，1有报告

	public TZStock() {	}

	public TZStock(ReturnProductVO vo) {
		this.productId = vo.getProductId();
		this.productNum = vo.getReturnCount();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
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

	public String getQsNo() {
		return qsNo;
	}

	public void setQsNo(String qsNo) {
		this.qsNo = qsNo;
	}

	public int getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(int reportStatus) {
		this.reportStatus = reportStatus;
	}
}
