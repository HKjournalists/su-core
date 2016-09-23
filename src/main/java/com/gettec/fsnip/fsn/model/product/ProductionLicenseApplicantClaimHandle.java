package com.gettec.fsnip.fsn.model.product;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * 生产许可证认领申请/处理
 * @author ZhangHui 2015/5/29
 */
@Entity(name="prolicinfo_qs_applicant_claim")
public class ProductionLicenseApplicantClaimHandle extends Model{
	private static final long serialVersionUID = 7100327203340801253L;
	
	@Id 
	@GeneratedValue
	private Long id;
	
	/**
	 * 外键：production_license_info_applicant_claim_backup id
	 */
	@Column(name="qs_id_applicant_claim_backup", nullable=false)
	private Long qs_id;

	/**
	 * 申请企业id
	 */
	@Column(name="business_id", nullable=false)
	private Long business_id;

	/**
	 * 申请人
	 */
	@Column(name="applicant")
	private String applicant = "";

	/**
	 * 申请时间
	 */
	@Column(name="applicant_time")
	private Date applicant_time;

	/**
	 * 处理人
	 */
	@Column(name="handler")
	private String handler = "";

	/**
	 * 处理时间
	 */
	@Column(name="handle_time")
	private Date handle_time;

	/**
	 * 处理结果 
	 * 	1 代表待审核
	 * 	2 代表审核通过
	 *  4 代表未通过审核
	 *  8 代表该申请已经过期（审核通过后，企业主动解除与qs号的认领关系）
	 */
	@Column(name="handle_result")
	private int handle_result;

	/**
	 * 备注
	 */
	@Column(name="note")
	private String note = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQs_id() {
		return qs_id;
	}

	public void setQs_id(Long qs_id) {
		this.qs_id = qs_id;
	}

	public Long getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(Long business_id) {
		this.business_id = business_id;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getApplicant_time() {
		return applicant_time;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setApplicant_time(Date applicant_time) {
		this.applicant_time = applicant_time;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getHandle_time() {
		return handle_time;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setHandle_time(Date handle_time) {
		this.handle_time = handle_time;
	}

	public int getHandle_result() {
		return handle_result;
	}

	public void setHandle_result(int handle_result) {
		this.handle_result = handle_result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
