package com.gettec.fsnip.fsn.model.business;

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
 * 组织机构/营业执照 信息
 * @author Xin Tang
 */
@Entity(name = "t_liutong_field_value")
public class LiutongFieldValue extends Model {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "produce_bus_id")
	private Long producerId;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "full_flag")
	private boolean fullFlag;
	
	@Column(name = "display")
	private String display;
	
	@Column(name = "pass_flag")
	private String passFlag;
	
	@Column(name = "msg")
	private String msg;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="t_liutong_field_to_resource",joinColumns={@JoinColumn(name="liutong_field_id")}, inverseJoinColumns = {@JoinColumn(name="resource_id")})
	private Set<Resource> attachments = new HashSet<Resource>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProducerId() {
		return producerId;
	}

	public void setProducerId(Long producerId) {
		this.producerId = producerId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isFullFlag() {
		return fullFlag;
	}

	public void setFullFlag(boolean fullFlag) {
		this.fullFlag = fullFlag;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
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

	public Set<Resource> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Resource> attachments) {
		this.attachments = attachments;
	}
	
	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.attachments.remove(resource);
		}
	}
	
	public void removeResources(Resource remove) {
			this.attachments.remove(remove);
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.attachments.add(resource);
		}
	}
	
	public LiutongFieldValue(){}
	
	public LiutongFieldValue(LiutongFieldValue liu2fieldval){
		if(liu2fieldval==null){return;}
		this.id = liu2fieldval.getId();
		this.producerId = liu2fieldval.getProducerId();
		this.value = liu2fieldval.getValue();
		this.fullFlag = liu2fieldval.isFullFlag();
		this.display = liu2fieldval.getDisplay();
		this.passFlag = liu2fieldval.getPassFlag();
		this.msg = liu2fieldval.getMsg();
		this.attachments = liu2fieldval.getAttachments();
	}
}
