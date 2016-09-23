package com.gettec.fsnip.fsn.model.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 流通企业—生产企业关系表
 * @author Xin Tang
 */
@Entity(name="t_liutong_to_produce")
public class LiutongToProduce extends Model{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "liutong_org_id")
	private Long organization;
	
	@Column(name = "produce_bus_id")
	private Long producerId;
	
	@Column(name = "producer_name")
	private String producerName;
	
	@Column(name = "full_flag")
	private boolean fullFlag;
	
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

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public boolean isFullFlag() {
		return fullFlag;
	}

	public void setFullFlag(boolean fullFlag) {
		this.fullFlag = fullFlag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
