package com.gettec.fsnip.fsn.model.test;

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
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

/**
 * ImportedProductTestResult Entity<br>
 * @author LongXianZhen 2015/05/26
 */
@Entity(name = "test_result_imported_product")
public class ImportedProductTestResult extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2062886548616541135L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name="test_result_id")
	private Long testResultId;

	@Column(name = "sanitary_cert_no")
	private String sanitaryCertNo;
	
	@Column(name="createDate")
	private Date createDate;     //创建时间
	
	@Column(name = "creator")
	private String creator;  // 创建者
	
	@Column(name = "lastModifyUser")
	private String lastModifyUser;      // 最后更新者
	
	@Column(name = "lastModifyDate")
	private Date lastModifyDate;  // 最后更新时间
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="sanitary_cert_to_resource",joinColumns={@JoinColumn(name="test_result_imp_pro_id")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> sanitaryAttachments = new HashSet<Resource>();
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="sanitary_cert_pdf_to_resource",joinColumns={@JoinColumn(name="test_result_imp_pro_id")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> sanitaryPdfAttachments = new HashSet<Resource>();
	
	@Column(name = "del")
	private boolean del;  // 删除标志
	
	@Transient
	private String sanitaryPdfURL;      // 卫生证pdf路径 用于portal接口返回
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestResultId() {
		return testResultId;
	}

	public void setTestResultId(Long testResultId) {
		this.testResultId = testResultId;
	}

	public String getSanitaryCertNo() {
		return sanitaryCertNo;
	}

	public void setSanitaryCertNo(String sanitaryCertNo) {
		this.sanitaryCertNo = sanitaryCertNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public Set<Resource> getSanitaryAttachments() {
		return sanitaryAttachments;
	}

	public void setSanitaryAttachments(Set<Resource> sanitaryAttachments) {
		this.sanitaryAttachments = sanitaryAttachments;
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}
	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.sanitaryAttachments.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.sanitaryAttachments.add(resource);
		}
	}

	public Set<Resource> getSanitaryPdfAttachments() {
		return sanitaryPdfAttachments;
	}

	public void setSanitaryPdfAttachments(Set<Resource> sanitaryPdfAttachments) {
		this.sanitaryPdfAttachments = sanitaryPdfAttachments;
	}
	public void removePdfResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.sanitaryPdfAttachments.remove(resource);
		}
	}

	public void addPdfResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.sanitaryPdfAttachments.add(resource);
		}
	}

	public String getSanitaryPdfURL() {
		return sanitaryPdfURL;
	}

	public void setSanitaryPdfURL(String sanitaryPdfURL) {
		this.sanitaryPdfURL = sanitaryPdfURL;
	}
	
}
