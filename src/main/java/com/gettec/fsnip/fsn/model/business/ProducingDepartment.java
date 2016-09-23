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
 * License Entity<br>
 * 生产车间
 * @author Xianzhen Long
 */
@Entity(name = "producing_department")
public class ProducingDepartment extends Model{
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;           // 名称
	
	@Column(name = "legal_name")
	private String legalName;        // 法定代表人
	
	@Column(name = "address")
	private String address;          // 地址
	
	@Column(name = "telephone")
	private String telephone;  			// 联系电话
	
	@Column(name = "in_commission_number")
	private String inCommissionNum; 				 // 投产窖池数
	
	@Column(name = "year")
	private String year;      				// 年限

	@Column(name="department_flga")
	private boolean departmentFlag;   //车间标志  为1时是生产车间，为0时是认可的‘挂靠’酒厂
	
	@Column(name = "business_id")
	private Long businessId;         //企业id business_unit 的id
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="producing_department_to_resource",joinColumns={@JoinColumn(name="pro_dep_id")}, inverseJoinColumns = {@JoinColumn(name="resource_id")})
	private Set<Resource> depAttachments = new HashSet<Resource>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	

	public String getInCommissionNum() {
		return inCommissionNum;
	}

	public void setInCommissionNum(String inCommissionNum) {
		this.inCommissionNum = inCommissionNum;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	

	public boolean isDepartmentFlag() {
		return departmentFlag;
	}

	public void setDepartmentFlag(boolean departmentFlag) {
		this.departmentFlag = departmentFlag;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Set<Resource> getDepAttachments() {
		return depAttachments;
	}

	public void setDepAttachments(Set<Resource> depAttachments) {
		this.depAttachments = depAttachments;
	}
	
	public void addDepResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.depAttachments.add(resource);
		}
	}
	
	public void removeDepResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.depAttachments.remove(resource);
		}
	}
	
}
