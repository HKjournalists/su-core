package com.gettec.fsnip.fsn.model.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.product.ProductToBusinessUnit;
import com.gettec.fsnip.fsn.model.product.SampleProductInstance;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * 抽样报告信息
 * @author Ryan Wang
 */
@SuppressWarnings("serial")
@Entity(name = "sample_test_result")
public class SampleTestResult extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "testee_id", nullable = true)
	private BusinessUnit testee;  // 被检测单位/人

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sample_id", nullable = true)
	private SampleProductInstance sample;  // 产品实例

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sample_brand_id", nullable = true)
	private SampleBusinessBrand brand;  // 商标
	
	@Column(name = "sample_quantity")
	private String sampleQuantity;   //抽样数量
	
	@Column(name = "sampling_location")
	private String samplingLocation; // 抽样地点
	
	@Column(name = "test_place")
	private String testPlace; // 检验地点
	
	@Column(name = "sampling_date")
	private Date samplingDate;  // easy无此字段
	
	@Column(name = "test_date")
	private Date testDate;  // 检验日期
	
	@Column(name = "test_type")
	private String testType; // 检验类别
	
	@Column(name = "equipment")
	private String equipment;         //主要仪器
	
	@Column(name = "standard")
	private String standard;  // 执行标准
	
	@Column(name = "result")
	private String result;             //检测结果描述
	
	@Column(name = "pass")
	private Boolean pass = Boolean.FALSE;  // 检验结论
	
	@Column(name = "comment")
	private String comment;  // 备注
	
	@Column(name = "approve_by")
	private String approveBy;  // easy 无此字段
	
	@Column(name = "audit_by")
	private String auditBy;  // easy 无此字段
	
	@Column(name = "key_tester")
	private String keyTester;  // easy 无此字段
	
	@Transient
	private List<TestProperty> testProperties = new ArrayList<TestProperty>();  // 检测项目
	
	@Transient
	private ImportedProductTestResult impProTestResult;  //进口食品报告信息
	 
	@Column(name = "fullPdfPath")
	private String fullPdfPath;   // 完整的没有截取的报告路径
	
	@Column(name="interceptionPdfPath") 
	private String interceptionPdfPath;  // 政府抽检的截取前两页pdf路径；企业自检和企业送检均与fullPdfPath的值一致
	
	/**
	 * 报告发布标记
	 * 		0 代表 testlab正在审核;
	 * 		1 代表 testlab通过审核;
	 * 		2 代表 testlab退回至企业;
	 * 		3 代表 发布人员未发布到testlab
	 * 		4 代表 经销商提交，但未通过商超审核
	 * 		5 代表 商超退回至供应商
	 * 		6 代表 商超审核通过
	 * 		7 代表 [食安云]将报告直接退回至供应商
	 */
	@Column(name = "publish_flag")
	private char publishFlag;
	
	@Column(name = "limsSampleId")
	private Long limsSampleId;  // LIMS样品ID
	
	@Column(name = "limsIdentification")
	private String limsIdentification;  // lims报告唯一标识
	
	@Column(name = "service_order")
	private String serviceOrder;  // 报告编号
	
	@Column(name = "sample_no")
	private String sampleNO;  // 报告编号 + ‘-1’

	@Column(name = "receiveDate")
	private Date receiveDate;  // 发布到testlab时间
	
	@Column(name = "publishDate")
	private Date publishDate;  // 发布到portal时间
	
	@Column(name = "edition")
	private String edition;           //报告来至lims的哪个版本
	
	@Column(name = "organization")
	private Long organization;        //报告所属公司的组织机构id
	
	@Column(name = "organization_name")
	private String organizationName;  // 报告所属公司的组织机构名称
	
	/**
	 * dealer             代表 供应商
	 * icb                代表 工商数据
	 * lims2                                   代表 lims2.0 数据
	 * parse_pdf          代表 解析pdf数据(目前只给蒙牛提供此功能)
	 * receive_from_lims  代表 从lims接收
	 */
	@Column(name = "dbflag", length = 20)
	private String dbflag;
	
	@Column(name = "back_result")
	private String backResult;

	@Column(name = "back_time")
	private Date backTime;
	
	@Column(name = "backLimsURL")
	private String backLimsURL;     //退回到LIMS接口路径

	/**
	 * 报告创建用户
	 * @author ZhangHui 2015/6/16
	 */
	@Column(name = "create_user")
	private String create_user;
	
	/**
	 * 报告创建时间
	 * @author ZhangHui 2015/6/16
	 */
	@Column(name = "create_time")
	private Date create_time;
	
	@Column(name = "last_modify_user")
	private String lastModifyUserName;
	
	@Column(name = "last_modify_time")
	private Date lastModifyTime;
	
	@Column(name = "pub_user_name")
	private String pubUserName;  // 发布者名称
	
	@Column(name = "tips")
	private String tips;
	
	@Column(name = "auto_report_flag")
	private boolean autoReportFlag;
	
	@Column(name="test_orgnization")
	private String testOrgnization;  // 检验机关
	
	@Column(name="upload_path")
	private String uploadPath;
	
	@Column(name="SIGN_FLAG")
	private boolean signFlag;
	
	@Column(name = "end_date")
	private Date endDate;//报告过期时间
	
	@Column(name = "check_org_name")
	private String checkOrgName;//报告审核人
	
	//@Column(name="mk_db_flag")
	//private char mkDbFlag; // 1:工商，2:lims 2.0 , 3:录入数据(默认为3), 4:解析pdf
	
/*	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_TEST_REPORT_TO_RESOURCE",
			joinColumns={@JoinColumn(name="REPORT_ID")}, 
			inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> repAttachments = new HashSet<Resource>();
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_TEST_REPORT_BACK_TO_RESOURCE",
			joinColumns={@JoinColumn(name="REPORT_ID")}, 
			inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> repBackAttachments = new HashSet<Resource>();*/
	
	/**
	 * 报告是否删除的标记
	 * 		0 代表未删除 （为默认值）
	 * 		1 代表已经删除
	 * @author ZhangHui 2015/6/17
	 */
	@Column(name = "del")
	private int del = 0;
	/**
	 * 以结构化数据退回报表次数
	 */
	@Column(name = "back_count")
	private int backCount = 0;
	
	@Column(name = "suppliers_back_count")
	private int suppliersBackCount=0;
	
	@Transient
	private String status;   // 状态
	
	@Transient
	private boolean newFlag;
	
	@Transient
	private String ftpPath;
	
	@Transient
	private List<ProductToBusinessUnit> pro2Bus; // 当前报告对应产品关联的qs号列表
	
	@Transient
	private List<Map<Object,String>> listOfBusunitName; // 当前报告对应产品已绑定qs号的企业列表
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getReceiveDate() {
		return receiveDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getTestPlace() {
		return testPlace;
	}

	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getPublishDate() {
		return publishDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public Long getLimsSampleId() {
		return limsSampleId;
	}

	public void setLimsSampleId(Long limsSampleId) {
		this.limsSampleId = limsSampleId;
	}

    public String getBackLimsURL() {
		return backLimsURL;
	}

	public void setBackLimsURL(String backLimsURL) {
		this.backLimsURL = backLimsURL;
	}

	public String getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public String getFullPdfPath() {
		return fullPdfPath;
	}

	public void setFullPdfPath(String fullPdfPath) {
		this.fullPdfPath = fullPdfPath;
	}

	public List<ProductToBusinessUnit> getPro2Bus() {
		return pro2Bus;
	}

	public void setPro2Bus(List<ProductToBusinessUnit> pro2Bus) {
		this.pro2Bus = pro2Bus;
	}

	public List<Map<Object,String>> getListOfBusunitName() {
		return listOfBusunitName;
	}

	public void setListOfBusunitName(List<Map<Object,String>> listOfBusunitName) {
		this.listOfBusunitName = listOfBusunitName;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getDbflag() {
		return dbflag;
	}

	public void setDbflag(String dbflag) {
		this.dbflag = dbflag;
	}

	public String getBackResult() {
		return backResult;
	}

	public void setBackResult(String backResult) {
		this.backResult = backResult;
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getLastModifyUserName() {
		return lastModifyUserName;
	}

	public void setLastModifyUserName(String lastModifyUserName) {
		this.lastModifyUserName = lastModifyUserName;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getTestOrgnization() {
		return testOrgnization;
	}

	public void setTestOrgnization(String testOrgnization) {
		this.testOrgnization = testOrgnization;
	}

	public String getSampleNO() {
		return sampleNO;
	}

	public void setSampleNO(String sampleNO) {
		this.sampleNO = sampleNO;
	}

	public char getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(char publishFlag) {
		this.publishFlag = publishFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getPass() {
		return pass;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getTestDate() {
		return testDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public List<TestProperty> getTestProperties() {
		return testProperties;
	}

	public void setTestProperties(List<TestProperty> testProperties) {
		this.testProperties.clear();
		this.testProperties = testProperties;
	}

	public BusinessUnit getTestee() {
		return testee;
	}

	public void setTestee(BusinessUnit testee) {
		this.testee = testee;
	}

	public SampleProductInstance getSample() {
		return sample;
	}

	public void setSample(SampleProductInstance sample) {
		this.sample = sample;
	}

	public String getSampleQuantity() {
		return sampleQuantity;
	}

	public void setSampleQuantity(String sampleQuantity) {
		this.sampleQuantity = sampleQuantity;
	}

	public String getSamplingLocation() {
		return samplingLocation;
	}

	public void setSamplingLocation(String samplingLocation) {
		this.samplingLocation = samplingLocation;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getSamplingDate() {
		return samplingDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setSamplingDate(Date samplingDate) {
		this.samplingDate = samplingDate;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public String getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public String getKeyTester() {
		return keyTester;
	}

	public void setKeyTester(String keyTester) {
		this.keyTester = keyTester;
	}

	public SampleBusinessBrand getBrand() {
		return brand;
	}

	public void setBrand(SampleBusinessBrand brand) {
		this.brand = brand;
	}
	
	public boolean isSignFlag() {
		return signFlag;
	}

	public void setSignFlag(boolean signFlag) {
		this.signFlag = signFlag;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getPubUserName() {
		return pubUserName;
	}

	public void setPubUserName(String pubUserName) {
		this.pubUserName = pubUserName;
	}

	public String getInterceptionPdfPath() {
		return interceptionPdfPath;
	}

	public void setInterceptionPdfPath(String interceptionPdfPath) {
		this.interceptionPdfPath = interceptionPdfPath;
	}

	public boolean isNewFlag() {
		return newFlag;
	}

	public void setNewFlag(boolean newFlag) {
		this.newFlag = newFlag;
	}

	public boolean isAutoReportFlag() {
		return autoReportFlag;
	}

	public void setAutoReportFlag(boolean autoReportFlag) {
		this.autoReportFlag = autoReportFlag;
	}

	/*public Set<Resource> getRepAttachments() {
		return repAttachments;
	}

	public void setRepAttachments(Set<Resource> repAttachments) {
		this.repAttachments = repAttachments;
	}*/

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/*public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.repAttachments.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.repAttachments.add(resource);
		}
	}*/

	public String getLimsIdentification() {
		return limsIdentification;
	}

	public void setLimsIdentification(String limsIdentification) {
		this.limsIdentification = limsIdentification;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public SampleTestResult() {
		super();
	}

	
	public ImportedProductTestResult getImpProTestResult() {
		return impProTestResult;
	}

	public void setImpProTestResult(ImportedProductTestResult impProTestResult) {
		this.impProTestResult = impProTestResult;
	}

	/*public Set<Resource> getRepBackAttachments() {
		return repBackAttachments;
	}

	public void setRepBackAttachments(Set<Resource> repBackAttachments) {
		this.repBackAttachments = repBackAttachments;
	}*/

	public String getCheckOrgName() {
		return checkOrgName;
	}

	public void setCheckOrgName(String checkOrgName) {
		this.checkOrgName = checkOrgName;
	}

	public int getBackCount() {
		return backCount;
	}

	public void setBackCount(int backCount) {
		this.backCount = backCount;
	}

	public int getSuppliersBackCount() {
		return suppliersBackCount;
	}

	public void setSuppliersBackCount(int suppliersBackCount) {
		this.suppliersBackCount = suppliersBackCount;
	}

	
}