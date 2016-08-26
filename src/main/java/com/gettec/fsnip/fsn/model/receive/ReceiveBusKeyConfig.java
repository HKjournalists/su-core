package com.gettec.fsnip.fsn.model.receive;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 泊银等外部系统的秘钥配置表
 * @author ZhangHui 2015/4/24
 */
@SuppressWarnings("serial")
@Entity(name = "receive_bus_key_config")
public class ReceiveBusKeyConfig extends Model{
	@Id
	@Column(name = "NO")
	private String no;
	
	/**
	 * 企业秘钥
	 */
	@Column(name = "KEY")
	private String key;

	/**
	 * 状态
	 */
	@Column(name = "STATUS")
	private String status;

	/**
	 * 备注
	 */
	@Column(name = "NOTE")
	private String note;

	/**
	 * 检测结果
	 */
	@Column(name = "BUS_NAME")
	private String bus_name;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private Date create_date;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBus_name() {
		return bus_name;
	}

	public void setBus_name(String bus_name) {
		this.bus_name = bus_name;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
}
