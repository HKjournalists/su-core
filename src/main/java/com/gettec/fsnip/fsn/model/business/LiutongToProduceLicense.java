package com.gettec.fsnip.fsn.model.business;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 流通企业—QS关系表
 * @author Xin Tang
 */
@Entity(name = "t_liutong_produce_qs")
public class LiutongToProduceLicense extends Model {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "liutong_org_id")
	private Long organization;

	@Column(name = "produce_bus_id")
	private Long producerId;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "qs_no", nullable = true)
	private ProductionLicenseInfo qsInstance;

	@Column(name = "full_flag")
	private boolean fullFlag;

	@Column(name = "pass_flag")
	private String passFlag;

	@Column(name = "msg")
	private String msg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public Long getProducerId() {
		return producerId;
	}

	public void setProducerId(Long producerId) {
		this.producerId = producerId;
	}

	public ProductionLicenseInfo getQsInstance() {
		return qsInstance;
	}

	public void setQsInstance(ProductionLicenseInfo qsInstance) {
		this.qsInstance = qsInstance;
	}

	public boolean isFullFlag() {
		return fullFlag;
	}

	public void setFullFlag(boolean fullFlag) {
		this.fullFlag = fullFlag;
	}

	public String getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(String passFlag) {
		this.passFlag = passFlag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public LiutongToProduceLicense(){}
	
	public LiutongToProduceLicense(LiutongToProduceLicense qsLicen){
		if(qsLicen==null){return;}
		this.id = qsLicen.getId();
		this.organization = qsLicen.getOrganization();
		this.producerId = qsLicen.getProducerId();
		this.fullFlag = qsLicen.isFullFlag();
		this.passFlag = qsLicen.getPassFlag();
		this.msg = qsLicen.getMsg();
		this.qsInstance = new ProductionLicenseInfo(qsLicen.getQsInstance());
	}
}
