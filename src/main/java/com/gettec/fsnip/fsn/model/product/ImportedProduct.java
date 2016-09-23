package com.gettec.fsnip.fsn.model.product;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.base.Country;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;
/**
 * 进口产品
 * @author longxianzhen 2015/05/22
 *
 */
@Entity(name="imported_product")
public class ImportedProduct extends Model {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	
	@Column(name = "product_id")
	private Long productId;
	
	@Transient
	private Long countryId;
	
	@Transient
	private ImportedProductAgents importedProductAgents;
	
	/*@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id", nullable=false)*/
	@Transient
	private Product product;  // 产品
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="country_id", nullable=false)
	private Country country;  // 国家
	
	@Column(name="createDate")
	private Date createDate;     //创建时间
	
	@Column(name = "creator")
	private String creator;  // 创建者
	
	@Column(name = "lastModifyUser")
	private String lastModifyUser;      // 最后更新者
	
	@Column(name = "lastModifyDate")
	private Date lastModifyDate;  // 最后更新时间
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="imported_product_label_to_resource",joinColumns={@JoinColumn(name="imp_pro_id")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> labelAttachments = new HashSet<Resource>();
	
	@Column(name = "del")
	private boolean del;  // 删除标志 0：未删除  1：已删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
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

	public Set<Resource> getLabelAttachments() {
		return labelAttachments;
	}

	public void setLabelAttachments(Set<Resource> labelAttachments) {
		this.labelAttachments = labelAttachments;
	}

	public ImportedProductAgents getImportedProductAgents() {
		return importedProductAgents;
	}

	public void setImportedProductAgents(ImportedProductAgents importedProductAgents) {
		this.importedProductAgents = importedProductAgents;
	}
	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.labelAttachments.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.labelAttachments.add(resource);
		}
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}
}
