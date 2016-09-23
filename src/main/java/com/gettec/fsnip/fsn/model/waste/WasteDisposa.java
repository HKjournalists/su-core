package com.gettec.fsnip.fsn.model.waste;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;


/**
 * 废弃物处理
 * @author YuanBin Hao
 */
@Entity(name = "waste_dispoa")
public class WasteDisposa extends Model {

	private static final long serialVersionUID = 4855415224298725011L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "handler")
	private String handler;     // 处理人

	@Column(name = "handle_number")
	private String handleNumber;	 //处理数量

	@Column(name = "handle_way")
	private String handleWay;	 //处理方式

	@Column(name = "handle_time")
	private Date handleTime;     //处理时间
	
	@Column(name = "participation")
	private String participation;	 //参与部门

	@Column(name = "destory")
	private String destory;	 //销毁地点

	@Column(name = "pic_file")
	private String picFile;     //上传图片

	@Column(name = "qiyeId")
	private long qiyeId;     //
	
	@Column(name = "create_time")
	private String createTime;     //创建时间
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="PICEFILE_TO_RESOURCE",joinColumns={@JoinColumn(name="PICEFILE_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> piceFile = new HashSet<Resource>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getQiyeId() {
		return qiyeId;
	}

	public void setQiyeId(long qiyeId) {
		this.qiyeId = qiyeId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getHandleNumber() {
		return handleNumber;
	}

	public void setHandleNumber(String handleNumber) {
		this.handleNumber = handleNumber;
	}

	public String getHandleWay() {
		return handleWay;
	}

	public void setHandleWay(String handleWay) {
		this.handleWay = handleWay;
	}

	

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getParticipation() {
		return participation;
	}

	public void setParticipation(String participation) {
		this.participation = participation;
	}

	public String getDestory() {
		return destory;
	}

	public void setDestory(String destory) {
		this.destory = destory;
	}

	public String getPicFile() {
		return picFile;
	}

	public void setPicFile(String picFile) {
		this.picFile = picFile;
	}

	public Set<Resource> getPiceFile() {
		return piceFile;
	}

	public void setPiceFile(Set<Resource> piceFile) {
		this.piceFile = piceFile;
	}
	
	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.piceFile.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.piceFile.add(resource);
		}
	}
}
