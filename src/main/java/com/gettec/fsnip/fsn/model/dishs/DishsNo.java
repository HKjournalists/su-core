package com.gettec.fsnip.fsn.model.dishs;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity(name = "dishs_no")
public class DishsNo extends Model{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "dishs_name")
	private String dishsName;     //菜品名称

	@Column(name = "alias")
	private String alias;	 //别名

	@Column(name = "baching")
	private String baching;	 //配料
	
	@Column(name = "qiyeId")
	private Long qiyeId;
	
	@Column(name = "remark") //备注信息
	private String remark;

	@Column(name = "about") //菜品简介
	private String about;

	@Column(name = "price") //菜品价格
	private double price;

	@Column(name = "characteristic") //味道特色
	private String characteristic;

	public Long getQiyeId() {
		return qiyeId;
	}

	public void setQiyeId(Long qiyeId) {
		this.qiyeId = qiyeId;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="dishsno_to_resource",joinColumns={@JoinColumn(name="dishsno_id")}, inverseJoinColumns = {@JoinColumn(name="resource_id")})
	private Set<Resource> dishsnoFile = new HashSet<Resource>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDishsName() {
		return dishsName;
	}

	public void setDishsName(String dishsName) {
		this.dishsName = dishsName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getBaching() {
		return baching;
	}

	public void setBaching(String baching) {
		this.baching = baching;
	}

	public Set<Resource> getDishsnoFile() {
		return dishsnoFile;
	}

	public void setDishsnoFile(Set<Resource> dishsnoFile) {
		this.dishsnoFile = dishsnoFile;
	}
	
	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.dishsnoFile.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.dishsnoFile.add(resource);
		}
	}

	public String getRemark() {
		return remark;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
