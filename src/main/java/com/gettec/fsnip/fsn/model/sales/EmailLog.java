package com.gettec.fsnip.fsn.model.sales;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 邮件发送日志
 * @author tangxin 2014/04/23
 *
 */
@Entity(name = "t_bus_email_log")
public class EmailLog extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "business_id")
	private Long businessId;
	
	@Column(name = "organization")
	private Long organization;
	
	@Column(name = "title")
	private String titile;  //邮件标题
	
	@Column(name = "content")
	private String content; //邮件内容
	
	@Column(name = "addressee")
	private String addressee; //收件人
	
	@Column(name = "annex_name")
	private String annex_name; //邮件附件名称
	
	@Column(name = "electronic_data_id")
	private Long electDateId; //电子资料id
	
	@Column(name = "contarts_ids")
	private String contartsIds; //电子合同id集合
	
	@Column(name = "send_time")
	private Date sendTime; //邮件发送时间
	
	@Column(name = "send_user")
	private String sendUser;
	
	@Column(name = "del_status")
	private Integer delStatus; // 假删除状态，0默认值，1表示删除。

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

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getAnnex_name() {
		return annex_name;
	}

	public void setAnnex_name(String annex_name) {
		this.annex_name = annex_name;
	}

	public Long getElectDateId() {
		return electDateId;
	}

	public void setElectDateId(Long electDateId) {
		this.electDateId = electDateId;
	}

	public String getContartsIds() {
		return contartsIds;
	}

	public void setContartsIds(String contartsIds) {
		this.contartsIds = contartsIds;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	
}
