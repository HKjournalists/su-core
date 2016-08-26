package com.gettec.fsnip.fsn.model.erp.buss;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.gettec.fsnip.fsn.model.common.Model;

/**
 * 销往企业关系表
 * @author ZhangHui 2015/4/8
 */
@Entity(name="t_meta_from_to_business")
public class FromToBussiness extends Model{
	private static final long serialVersionUID = 1803980393210447808L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;

	@Column(name="pro_id", nullable = true)
	private Long proId;  // 产品id
	
	@Column(name="from_bus_id", nullable = true)
	private Long fromBusId;  // 出货商id
	
	@Column(name="to_bus_id", nullable = true)
	private Long toBusId;  // 收货商id
	
	@Column(name="del", nullable = true)
	private boolean del; // true: 已删除  false: 未删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProId() {
		return proId;
	}

	public void setProId(Long proId) {
		this.proId = proId;
	}

	public Long getFromBusId() {
		return fromBusId;
	}

	public void setFromBusId(Long fromBusId) {
		this.fromBusId = fromBusId;
	}

	public Long getToBusId() {
		return toBusId;
	}

	public void setToBusId(Long toBusId) {
		this.toBusId = toBusId;
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}

	public FromToBussiness(){}
	
	public FromToBussiness(Long proId, Long fromBusId, Long toBusId, boolean del) {
		super();
		this.proId = proId;
		this.fromBusId = fromBusId;
		this.toBusId = toBusId;
		this.del = del;
	}
}
