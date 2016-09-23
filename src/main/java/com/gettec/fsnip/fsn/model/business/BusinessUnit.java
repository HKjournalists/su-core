package com.gettec.fsnip.fsn.model.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.gettec.fsnip.fsn.model.base.District;
import com.gettec.fsnip.fsn.model.base.Office;
import com.gettec.fsnip.fsn.model.base.SysArea;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToType;
import com.gettec.fsnip.fsn.model.erp.base.BusinessUnitToTypePK;
import com.gettec.fsnip.fsn.model.erp.base.ContactInfo;
import com.gettec.fsnip.fsn.model.erp.base.CustomerAndProviderType;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfo;
import com.gettec.fsnip.fsn.model.erp.base.CustomerToContactinfoPK;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * BusinessUnits Entity<br>
 * @author Ryan Wang
 */

@Entity(name = "business_unit")
public class BusinessUnit extends Model{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;     // 企业名称
	
	@Column(name = "address")
	private String address;	 //地址
	
	@Column(name = "type")
	private String type;     //企业类型
	
	@ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)
	@JoinColumn(name = "license_no", nullable = true)
	private LicenseInfo license; // 营业执照信息
	
	@Column(name = "contact")
	private String contact;     // 联系人
	
	@Column(name = "note")
	private String note;        // 备注
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_code", nullable = true)
	private OrganizingInstitution orgInstitution;  // 组织机构代码信息
	
	@Column(name = "person_in_charge")
	private String personInCharge;  // 法定代表人
	
	@Column(name = "postal_code")
	private String postalCode;     // 邮政编码
	
	@Column(name = "telephone")
	private String telephone;      // 联系电话
	
	@Column(name = "mobile")
	private String mobile;          
	
	@Column(name = "fax")
	private String fax;      // 传真
	
	@Column(name = "about")
	private String about;      // 企业简介
	
	@Column(name = "website")
	private String website;      // 企业官网

	@Column(name = "email")
	private String email;    //邮箱
	
	@ManyToOne(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
//	@ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)
	@JoinColumn(name = "distribution_no", nullable = true)
	private CirculationPermitInfo distribution;  // 流通许可证号
	
	@Column(name = "region")
	private String region;       //地区
	
	@Column(name = "sampleLocal")
	private String sampleLocal;    //抽样场所
	
	@Column(name = "administrativeLevel")
	private String administrativeLevel;  //行政级别

	@Column(name = "organization")
	private Long organization;  // 组织机构ID
	
	@Column(name = "parentOrgnization")
	private Long parentOrganizationId;   //父组织机构
	
	@Column(name = "enterpriteDate")
	private Date enterpriteDate;
	
	@Column(name="other_address")
	private String otherAddress;
	
	@Column(name="wda_back_flag")
	private boolean wdaBackFlag;  //监管系统审核退回标志
	
	@Column(name="wda_back_msg")
	private String wdaBackMsg;   ///监管系统审核退回原因
	
	@Column(name="rtsp_url")
	private String rtspUrl;   ///餐饮企业视频监控地址
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
	@JoinColumn(name = "tax_register_id", nullable = true)
	private TaxRegisterInfo  taxRegister;  // 税务登记证信息
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "liquor_sales_id", nullable = true)
	private LiquorSalesLicenseInfo liquorSalesLicense;  // 酒类销售许可证信息
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="business_pdf_to_resource",joinColumns={@JoinColumn(name="business_id")}, inverseJoinColumns = {@JoinColumn(name="resource_id")})
	private Set<Resource> busPdfAttachments = new HashSet<Resource>();
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="t_meta_enterprise_to_provider",joinColumns={@JoinColumn(name="business_id")}, inverseJoinColumns = {@JoinColumn(name="provider_id")})
	private Set<BusinessUnit> providers = new HashSet<BusinessUnit>();  // 供应商列表
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="t_meta_enterprise_to_customer",joinColumns={@JoinColumn(name="business_id")}, inverseJoinColumns = {@JoinColumn(name="customer_id")})
	private Set<BusinessUnit> customers = new HashSet<BusinessUnit>();  // 客户列表
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lims_busunit_id", nullable = true)
	private BusinessUnitToLims businessUnitToLims;  // lims保存企业的扩展信息
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id", nullable = true)
	private SysArea sysArea;  // 所属区划
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id", nullable = true)
	private Office office;  // 管辖食药监机关
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="t_business_to_sys_dict",joinColumns={@JoinColumn(name="business_id")}, inverseJoinColumns = {@JoinColumn(name="dict_id")})
	private Set<District> district = new HashSet<District>();
	
	@Column(name = "sign_flag")
	private boolean signFlag;
	
	/**
	 * 生产企业唯一标识
	 * @author Zhanghui 2015/4/24
	 */
	@Column(name = "guid")
	private String guid;
	
	@Transient
	private Set<BusinessUnit> customerlist = new HashSet<BusinessUnit>();  // 客户列表
	
	@Transient
	private Set<Resource> logoAttachments = new HashSet<Resource>();
	
	@Transient
	private Set<Resource> orgAttachments = new HashSet<Resource>();
	
	@Transient
	private Set<Resource> licAttachments = new HashSet<Resource>();
	
	@Transient
	private Set<Resource> disAttachments = new HashSet<Resource>();
	
	@Transient
	private Set<Resource> qsAttachments = new HashSet<Resource>();
	
	@Transient
	private Set<Resource> taxRegAttachments = new HashSet<Resource>();
	
	@Transient
	private Set<Resource> liquorAttachments = new HashSet<Resource>();
	
	@Transient
	private String liquorCode;
	
	@Transient
	private List<FieldValue> fieldValues = new ArrayList<FieldValue>(); // 扩展信息列表
	
	@Transient
	private List<ProducingDepartment> proDepartments = new ArrayList<ProducingDepartment>();  // 生产车间
	
	@Transient
	private List<ProducingDepartment> subDepartments = new ArrayList<ProducingDepartment>();  // 挂靠酒厂
	
	@Transient
	private List<BusinessBrand> brands = new ArrayList<BusinessBrand>();  // 品牌
	
	@Transient
	private List<BusinessCertification> listOfCertification = new ArrayList<BusinessCertification>();// 其他认证信息
	
	/*@Transient
	private QsNoAndFormatVo qsNoAndFormatVo;  // qs号 和qs号的录入规则  */
	@Transient
	private String step;  // 记录仁怀数据录入步骤
	
	@Transient
	private boolean bindQsFlag;  // qs绑定标记
	
	@Transient
	private List<ContactInfo> contacts = new ArrayList<ContactInfo>();
	
	@Transient
	private List<CustomerToContactinfo> infos = new ArrayList<CustomerToContactinfo>(); // 客户/供应商与联系人的关系列表
	
	@Transient
	private CustomerAndProviderType diyType = new CustomerAndProviderType();
	
	@Transient
	private BusinessUnitToType BuToType  = new BusinessUnitToType();
	
	@Transient
	private String nowPdfUrl;
	
	@Transient
	private Long marketOrg;

	@Transient
	private List<Resource> propagandaAttachments = new ArrayList<Resource>();

	public List<Resource> getQrAttachments() {
		return qrAttachments;
	}

	public void setQrAttachments(List<Resource> qrAttachments) {
		this.qrAttachments = qrAttachments;
	}

	@Transient
	private List<Resource> qrAttachments = new ArrayList<Resource>();

	public List<Resource> getPropagandaAttachments() {
		return propagandaAttachments;
	}

	public void setPropagandaAttachments(List<Resource> propagandaAttachments) {
		this.propagandaAttachments = propagandaAttachments;
	}

/*	public QsNoAndFormatVo getQsNoAndFormatVo() {
        return qsNoAndFormatVo;
    }

    public void setQsNoAndFormatVo(QsNoAndFormatVo qsNoAndFormatVo) {
        this.qsNoAndFormatVo = qsNoAndFormatVo;
    }*/
	@Transient
    private int countNum ;
	@Transient
    private String lincesNo ;
	
	@Transient
	private String regularity;//信用等级 
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public BusinessUnitToType getBuToType() {
		return BuToType;
	}

	public void setBuToType(BusinessUnitToType buToType) {
		BuToType = buToType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FieldValue> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(List<FieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public Long getParentOrganizationId() {
		return parentOrganizationId;
	}

	public CustomerAndProviderType getDiyType() {
		return diyType;
	}

	public void setDiyType(CustomerAndProviderType diyType) {
		this.diyType = diyType;
	}

	public void setParentOrganizationId(Long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

	public LicenseInfo getLicense() {
		return license;
	}

	public void setLicense(LicenseInfo license) {
		this.license = license;
	}

	public OrganizingInstitution getOrgInstitution() {
		return orgInstitution;
	}

	public void setOrgInstitution(OrganizingInstitution orgInstitution) {
		this.orgInstitution = orgInstitution;
	}

	public Set<Resource> getDisAttachments() {
		return disAttachments;
	}

	public void setDisAttachments(Set<Resource> disAttachments) {
		this.disAttachments = disAttachments;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public CirculationPermitInfo getDistribution() {
		return distribution;
	}

	public void setDistribution(CirculationPermitInfo distribution) {
		this.distribution = distribution;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSampleLocal() {
		return sampleLocal;
	}

	public void setSampleLocal(String sampleLocal) {
		this.sampleLocal = sampleLocal;
	}

	public String getAdministrativeLevel() {
		return administrativeLevel;
	}

	public void setAdministrativeLevel(String administrativeLevel) {
		this.administrativeLevel = administrativeLevel;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEnterpriteDate() {
		return enterpriteDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEnterpriteDate(Date enterpriteDate) {
		this.enterpriteDate = enterpriteDate;
	}

	public Set<Resource> getOrgAttachments() {
		return orgAttachments;
	}

	public void setOrgAttachments(Set<Resource> orgAttachments) {
		this.orgAttachments = orgAttachments;
	}

	public Set<Resource> getLicAttachments() {
		return licAttachments;
	}

	public void setLicAttachments(Set<Resource> licAttachments) {
		this.licAttachments = licAttachments;
	}

	public String getOtherAddress() {
		return otherAddress;
	}

	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}

	public Set<Resource> getQsAttachments() {
		return qsAttachments;
	}

	public void setQsAttachments(Set<Resource> qsAttachments) {
		this.qsAttachments = qsAttachments;
	}

	public Set<Resource> getLogoAttachments() {
		return logoAttachments;
	}

	public void setLogoAttachments(Set<Resource> logoAttachments) {
		this.logoAttachments = logoAttachments;
	}

	public TaxRegisterInfo getTaxRegister() {
		return taxRegister;
	}

	public void setTaxRegister(TaxRegisterInfo taxRegister) {
		this.taxRegister = taxRegister;
	}

	public LiquorSalesLicenseInfo getLiquorSalesLicense() {
		return liquorSalesLicense;
	}

	public void setLiquorSalesLicense(LiquorSalesLicenseInfo liquorSalesLicense) {
		this.liquorSalesLicense = liquorSalesLicense;
	}

	public List<ProducingDepartment> getProDepartments() {
		return proDepartments;
	}

	public void setProDepartments(List<ProducingDepartment> proDepartments) {
		this.proDepartments = proDepartments;
	}

	public List<ProducingDepartment> getSubDepartments() {
		return subDepartments;
	}

	public void setSubDepartments(List<ProducingDepartment> subDepartments) {
		this.subDepartments = subDepartments;
	}

	public List<BusinessBrand> getBrands() {
		return brands;
	}

	public void setBrands(List<BusinessBrand> brands) {
		this.brands = brands;
	}

	public List<ContactInfo> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactInfo> contacts) {
		this.contacts = contacts;
	}

	public List<CustomerToContactinfo> getInfos() {
		return infos;
	}

	public void setInfos(List<CustomerToContactinfo> infos) {
		this.infos = infos;
	}

	public BusinessUnit(){}
	
	public BusinessUnit(Long id, String name){
		this.id = id;
		this.name = name;
	}
	
	public BusinessUnit(String name){
		this.name = name;
	}
	
	public Set<Resource> getBusPdfAttachments() {
		return busPdfAttachments;
	}

	public void setBusPdfAttachments(Set<Resource> busPdfAttachments) {
		this.busPdfAttachments = busPdfAttachments;
	}

	@JsonIgnore
	public Set<BusinessUnit> getProviders() {
		return providers;
	}

	@JsonIgnore
	public void setProviders(Set<BusinessUnit> providers) {
		this.providers = providers;
	}

	@JsonIgnore
	public Set<BusinessUnit> getCustomers() {
		return customers;
	}

	@JsonIgnore
	public void setCustomers(Set<BusinessUnit> customers) {
		this.customers = customers;
	}

	public Set<Resource> getTaxRegAttachments() {
		return taxRegAttachments;
	}

	public void setTaxRegAttachments(Set<Resource> taxRegAttachments) {
		this.taxRegAttachments = taxRegAttachments;
	}

	public Set<Resource> getLiquorAttachments() {
		return liquorAttachments;
	}

	public void setLiquorAttachments(Set<Resource> liquorAttachments) {
		this.liquorAttachments = liquorAttachments;
	}

	public boolean isBindQsFlag() {
		return bindQsFlag;
	}

	public void setBindQsFlag(boolean bindQsFlag) {
		this.bindQsFlag = bindQsFlag;
	}

	public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.busPdfAttachments.remove(resource);
		}
	}
	
	// 删除供应商
	public void removeProvider(BusinessUnit remove) {
		this.providers.remove(remove);
	}
	
	// 删除客户
	public void removeCustomer(BusinessUnit remove) {
		this.customers.remove(remove);
	}
	
	public void addContactInfo(ContactInfo info){
		this.contacts.add(info);
	}
	
	public void addRelationShipInfo(ContactInfo info){
		CustomerToContactinfoPK id = new CustomerToContactinfoPK(this.id,info.getId());
		CustomerToContactinfo relationship = new CustomerToContactinfo(id);
		relationship.setType(this.diyType.getType());
		if(info.isDirect()){
			relationship.setDirect(true);
		}
		this.infos.add(relationship);
	}
	
	public void addRelationShipBusinessUnitToType(CustomerAndProviderType Type){
		BusinessUnitToTypePK id = new BusinessUnitToTypePK(this.id,Type.getId());
		BusinessUnitToType relationship = new BusinessUnitToType(id);
		this.BuToType = relationship;
	}

	public String getNowPdfUrl() {
		return nowPdfUrl;
	}

	public void setNowPdfUrl(String nowPdfUrl) {
		this.nowPdfUrl = nowPdfUrl;
	}

	public boolean isWdaBackFlag() {
		return wdaBackFlag;
	}

	public void setWdaBackFlag(boolean wdaBackFlag) {
		this.wdaBackFlag = wdaBackFlag;
	}

	public String getWdaBackMsg() {
		return wdaBackMsg;
	}

	public void setWdaBackMsg(String wdaBackMsg) {
		this.wdaBackMsg = wdaBackMsg;
	}

	public List<BusinessCertification> getListOfCertification() {
		return listOfCertification;
	}

	public void setListOfCertification(
			List<BusinessCertification> listOfCertification) {
		this.listOfCertification = listOfCertification;
	}

	public BusinessUnitToLims getBusinessUnitToLims() {
		return businessUnitToLims;
	}

	public void setBusinessUnitToLims(BusinessUnitToLims businessUnitToLims) {
		this.businessUnitToLims = businessUnitToLims;
	}

	public SysArea getSysArea() {
		return sysArea;
	}

	public void setSysArea(SysArea sysArea) {
		this.sysArea = sysArea;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Set<District> getDistrict() {
		return district;
	}

	public void setDistrict(Set<District> district) {
		this.district = district;
	}

	public boolean isSignFlag() {
		return signFlag;
	}

	public void setSignFlag(boolean signFlag) {
		this.signFlag = signFlag;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Long getMarketOrg() {
		return marketOrg;
	}

	public void setMarketOrg(Long marketOrg) {
		this.marketOrg = marketOrg;
	}

	public Set<BusinessUnit> getCustomerlist() {
		return customerlist;
	}

	public void setCustomerlist(Set<BusinessUnit> customerlist) {
		this.customerlist = customerlist;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
     
	public int getCountNum() {
		return countNum;
	}

	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
 
	public String getLincesNo() {
		return lincesNo;
	}

	public void setLincesNo(String lincesNo) {
		this.lincesNo = lincesNo;
	}
	
	public String getRegularity() {
		return regularity;
	}

	public void setRegularity(String regularity) {
		this.regularity = regularity;
	}

	public String getLiquorCode() {
		return liquorCode;
	}

	public void setLiquorCode(String liquorCode) {
		this.liquorCode = liquorCode;
	}

	@Override
	public String toString() {
		return "BusinessUnitNew [id=" + id + ", name=" + name + ", address="
				+ address + ", type=" + type + ", contact="
				+ contact + ", note=" + note
				+ ", personInCcharge=" + personInCharge + ", postalCode=" + postalCode + ", telephone=" + telephone
				+ ", mobile=" + mobile + "]";
	}

	public String getRtspUrl() {
		return rtspUrl;
	}

	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
	}
}
