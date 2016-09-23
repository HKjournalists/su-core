package com.gettec.fsnip.fsn.model.procurement;

import com.gettec.fsnip.fsn.model.common.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * ProcurementUsageRecord Entity<br>
 * @author lxz
 */
@Entity(name = "procurement_usage_record")
public class ProcurementUsageRecord extends Model{
	
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "procurement_id")
	private Long procurementId;  // 采购信息表id

	@Column(name = "procurement_name")
	private String procurementName;  // 采购名称

	@Column(name = "use_date")
	private Date useDate;  // 使用日期
	
	@Column(name = "use_num")
	private Integer useNum;   // 使用数量
	
	@Column(name = "purpose")
	private String purpose;   // 用途
	
	@Column(name = "remark")
	private String remark;   // 备注

	@Column(name = "create_date")
	private Date createDate;   // 创建日期

	@Column(name = "creator")
	private String creator;   // 创建者

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcurementId() {
		return procurementId;
	}

	public void setProcurementId(Long procurementId) {
		this.procurementId = procurementId;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public Integer getUseNum() {
		return useNum;
	}

	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProcurementName() {
		return procurementName;
	}

	public void setProcurementName(String procurementName) {
		this.procurementName = procurementName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}