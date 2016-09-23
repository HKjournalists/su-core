package com.lhfs.fsn.vo;

import java.util.Date;

public class BusinessTestResultVO {
	
	private Long id;
    private Long busId; //企业ID
    private String businessName; //企业名称
    private String licenseNo;//营业执照
    private Long batchId;  //批次ID
	private String batchSerialNo;   // 批次
    private Long brandId;   //商标ID
	private String branName; //商标名称
	private String sampleQuantity;   //抽样数量
	private String samplingLocation; // 抽样地点
	private String testPlace; // 检验地点
	private Date samplingDate;  // easy无此字段
	private Date testDate;  // 检验日期
	private String testType; // 检验类别
	private String equipment;         //主要仪器
	private String standard;  // 执行标准
	private String result;             //检测结果描述
	private Boolean pass = Boolean.FALSE;  // 检验结论
	private String comment;  // 备注
	private String approveBy;  // easy 无此字段
	private String auditBy;  // easy 无此字段
	private String keyTester;  // easy 无此字段
	private String fullPdfPath;   // 完整的没有截取的报告路径
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
	private char publishFlag;
	private Long limsSampleId;  // LIMS样品ID
	private String limsIdentification;  // lims报告唯一标识
	private String serviceOrder;  // 报告编号
	private String sampleNO;  // 报告编号 + ‘-1’
	private Date receiveDate;  // 发布到testlab时间
	private Date publishDate;  // 发布到portal时间
	private String edition;           //报告来至lims的哪个版本
	private Long organization;        //报告所属公司的组织机构id
	private String organizationName;  // 报告所属公司的组织机构名称
	/**
	 * dealer             代表 供应商
	 * icb                代表 工商数据
	 * lims2                                   代表 lims2.0 数据
	 * parse_pdf          代表 解析pdf数据(目前只给蒙牛提供此功能)
	 * receive_from_lims  代表 从lims接收
	 */
	private String dbflag;
	private String backResult;
	private Date backTime;
	private String backLimsURL;     //退回到LIMS接口路径
	private String create_user; //报告创建用户
	private Date create_time; //报告创建时间
	private Date lastModifyTime;
	private String pubUserName;  // 发布者名称
	private String tips;
	private boolean autoReportFlag;
	private String testOrgnization;  // 检验机关
	private String uploadPath;
	private boolean signFlag;
	private Date endDate;//报告过期时间
	private String checkOrgName;//报告审核人
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBranName() {
		return branName;
	}

	public void setBranName(String branName) {
		this.branName = branName;
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

	public String getTestPlace() {
		return testPlace;
	}

	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}

	public Date getSamplingDate() {
		return samplingDate;
	}

	public void setSamplingDate(Date samplingDate) {
		this.samplingDate = samplingDate;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
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

	public Boolean getPass() {
		return pass;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getFullPdfPath() {
		return fullPdfPath;
	}

	public void setFullPdfPath(String fullPdfPath) {
		this.fullPdfPath = fullPdfPath;
	}

	public String getInterceptionPdfPath() {
		return interceptionPdfPath;
	}

	public void setInterceptionPdfPath(String interceptionPdfPath) {
		this.interceptionPdfPath = interceptionPdfPath;
	}

	public char getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(char publishFlag) {
		this.publishFlag = publishFlag;
	}

	public Long getLimsSampleId() {
		return limsSampleId;
	}

	public void setLimsSampleId(Long limsSampleId) {
		this.limsSampleId = limsSampleId;
	}

	public String getLimsIdentification() {
		return limsIdentification;
	}

	public void setLimsIdentification(String limsIdentification) {
		this.limsIdentification = limsIdentification;
	}

	public String getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(String serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public String getSampleNO() {
		return sampleNO;
	}

	public void setSampleNO(String sampleNO) {
		this.sampleNO = sampleNO;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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

	public String getBackLimsURL() {
		return backLimsURL;
	}

	public void setBackLimsURL(String backLimsURL) {
		this.backLimsURL = backLimsURL;
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

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getPubUserName() {
		return pubUserName;
	}

	public void setPubUserName(String pubUserName) {
		this.pubUserName = pubUserName;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public boolean isAutoReportFlag() {
		return autoReportFlag;
	}

	public void setAutoReportFlag(boolean autoReportFlag) {
		this.autoReportFlag = autoReportFlag;
	}

	public String getTestOrgnization() {
		return testOrgnization;
	}

	public void setTestOrgnization(String testOrgnization) {
		this.testOrgnization = testOrgnization;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public boolean isSignFlag() {
		return signFlag;
	}

	public void setSignFlag(boolean signFlag) {
		this.signFlag = signFlag;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCheckOrgName() {
		return checkOrgName;
	}

	public void setCheckOrgName(String checkOrgName) {
		this.checkOrgName = checkOrgName;
	}

	
}
