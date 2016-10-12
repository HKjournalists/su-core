package com.gettec.fsnip.fsn.model.procurement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * SaleRecord Entity<br>
 * 
 * @author suxiang
 */
@Entity(name = "procurement_sale_record")
public class SaleRecord extends Model {

	private static final long serialVersionUID = -6971077862849826201L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "online_sale_id")
	private Long onlineSaleId; // 在售商品ID

	@Column(name = "online_sale_name")
	private String onlineSaleName; // 在售商品名称

	@Column(name = "sale_date")
	private Date saleDate; // 销售时间

	@Column(name = "sale_num")
	private Integer saleNum; // 销售数量

	@Column(name = "remark")
	private String remark; // 备注

	@Column(name = "creator")
	private String creator; // 创建者

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOnlineSaleId() {
		return onlineSaleId;
	}

	public void setOnlineSaleId(Long onlineSaleId) {
		this.onlineSaleId = onlineSaleId;
	}

	public String getOnlineSaleName() {
		return onlineSaleName;
	}

	public void setOnlineSaleName(String onlineSaleName) {
		this.onlineSaleName = onlineSaleName;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

}
