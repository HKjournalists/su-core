package com.gettec.fsnip.fsn.model.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.business.SampleBusinessBrand;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.erp.base.Unit;
import com.gettec.fsnip.fsn.model.market.Resource;
import com.gettec.fsnip.fsn.model.test.TestRptJson;
import com.gettec.fsnip.fsn.vo.business.QsNoAndFormatVo;
import com.lhfs.fsn.vo.product.ProductSortVo;

/**
 * 抽样产品
 * 
 * 
 * @author Ryan Wang
 */
@Entity(name = "sample_product")
public class SampleProduct extends Model{
	
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name", length = 255)
	private String name;  // 产品名称
	
	@Column(name = "other_Name", length = 255)
	private String otherName;  // 产品别名（用于匹配pdf）

	@Column(name = "status", length = 50)
	private String status;  // 商品状态
	
	@Column(name = "format", length = 200)
	private String format;   // 规格
	
	@Column(name = "net_content", length = 200)
	private String netContent;   // 净含量
	
	@Column(name = "format_pdf", length = 200)
	private String pdfFormat;   // pdf中的包装方式
	
	/*@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="product_to_regularity",joinColumns={@JoinColumn(name="product_id")}, inverseJoinColumns = {@JoinColumn(name="regularity_id")})
	private Set<ProductCategoryInfo> regularity = new HashSet<ProductCategoryInfo>();  // 执行标准
*/	
	@Column(name = "barcode", length = 50)
	private String barcode;  //  条形码
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "ICB_category")
	private String ICB_category;  //  ICB_category
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="sample_business_brand_id", nullable=false)
	private SampleBusinessBrand sampleBusinessBrand;  // 商标
	
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity= Unit.class)
	@JoinColumn(name="UNIT_ID", nullable = true)
	private Unit unit;//unit 单位
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="producer_id", nullable=true)
	private BusinessUnit producer;  //  生产企业信息
	
	@Column(name="qscore_self")
	private Float qscoreSelf = 5.0f;   // 默认设置为5即可
	
	@Column(name="qscore_censor")
	private Float qscoreCensor = 5.0f; 
	
	@Column(name="qscore_sample")
	private Float qscoreSample = 5.0f;   // 默认设置为5即可
	
	@Column(name="imgurl")
	private String imgUrl;
	
	@Column(name="des")
	private String des;    //  产品简介
	
	@Column(name="cstm")
	private String cstm;   // 使用人群
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH}, targetEntity=ProductCategoryInfo.class)
	@JoinColumn(name="category_id", referencedColumnName = "id", nullable=true)
	private ProductCategoryInfo category;    //  第三级category的id
	    
    @Column(name = "category", length = 50)
    private String secondCategoryCode;  //  二级分类的categorycode
	    
	@Column(name="feature")
	private String feature;
	
	@Column(name="ingredient")
	private String ingredient;   // 配料
	
	@Column(name="characteristic")
	private String characteristic; // 产品特色
	
	@Column(name="expiration_date")
	private String expirationDate;  // 保质天数
	
	@Column(name="expiration")
	private String expiration;  // 质保期
	
	@Column(name = "organization", length = 20)
	private Long organization;//组织机构id
	
	@Column(name = "last_modify_time")
	private Date lastModifyTime; //最后更新时间
	
	/**
	 * 产品类型 
	 * 		1 代表 国产食品 
	 * 		2 代表 进口食品
	 * 		4 代表 国内分装食品（本身为进口食品，但经过了国内二次包装）
	 * @author LongXianZhen 2015/6/3
	 */
	@Column(name = "product_type")
	private int productType;
	
	@Column(name = "package_flag")
	private char packageFlag;   //产品的包装标志：0：预包装、1：散装、2：无条码
	
	@Column(name = "riskIndex")
	private  Double riskIndex;   //风险指数
	
	@Column(name = "riskIndex_Date")
	private Date riskIndexDate;   //风险指数的计算时间
	
	@Column(name = "risk_succeed")
	private Boolean risk_succeed = false;   //风险指数的计算成功:0：失败、1：成功
	
	@Column(name = "test_property_name")
	private String  testPropertyName;   //风险指数计算相关的检测项目名称的字符串
	
	@Column(name = "risk_failure")
	private String  riskFailure;   //风险指数计算失败的原因
	
/*	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="T_TEST_PRODUCT_TO_RESOURCE",joinColumns={@JoinColumn(name="PRODUCT_ID")}, inverseJoinColumns = {@JoinColumn(name="RESOURCE_ID")})
	private Set<Resource> proAttachments = new HashSet<Resource>();*/
	

/*	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="business_certification_to_product",joinColumns={@JoinColumn(name="PRODUCT_ID")}, inverseJoinColumns = {@JoinColumn(name="BUSINESS_CERT_ID")})
	private Set<BusinessCertification> listOfCertification = new HashSet<BusinessCertification>(); // 产品认证信息
*/	
	@Column(name = "nutri_label")
	private String nutriLabel; //营养指标 
	
	@Column(name = "nutri_status")
	private char nutriStatus; //计算营养指标的状态 
	
	@Transient
	private List<Certification> productCertification; // 认证信息（无）  portal加载时需要用到
	
	@Transient
	private NutritionReport report;    //产品营养报告   portal加载时需要用到
	
	@Transient
	private List<ProductRecommendUrl> proUrlList;    //产品营养报告   portal加载时需要用到
	
	@Transient
	private boolean isBindOfProducer;  // 该产品是否被平台已注册的生产企业绑定
	
	@Transient
	private boolean local; // true: 生产企业； false: 流通企业。（用于表示产品是当前企业自己所有，还是引进商品）
	
	@Transient
	private Integer selfCount;
	
	@Transient
	private Integer censorCount;
	
	@Transient
	private Integer sampleCount;
	
	@Transient
	private List<ProductInstance> productInstances;
	
	@Transient
	private ProductInstance latestProductInstance;
	
	@Transient
	private String regularityStr;  // 执行标准字符串
	
	@Transient
	private String identify;  // 平台标识符
	
	@Transient
	private String selectedCustomerIds;  // 销往客户Ids
	
	@Transient
	private String selectedCustomerNames;  // 列举所有的销往客户名称
	
	/**
	 * 背景: 1. 生产企业产品新增/编辑页面：下拉选择qs号;
	 * 	    2. 流通企业报告录入页面：给产品选择qs号
	 * 功能描述: 用于记录qs号的详细信息
	 * @author ZhangHui 2015/6/3
	 */
	@Transient
	private QsNoAndFormatVo qs_info;
	
	/**
	 * 背景：供应商产品新增/编辑页面，当产品条形码不是690-699开头（即不是国产）的产品，为进口产品
	 * 功能描述：ImportedProduct 类封装了进口产品特有的字段
	 * @author LongXianZhen 2015/6/3
	 */
	@Transient
	private ImportedProduct importedProduct; //进口产品
	
	
	/**
	 * 该产品是否已收藏 true:已收藏  false:未收藏  返回给portal使用
	 * @author longxianzhen  2015/06/09
	 */
	@Transient
	private boolean isEnshrine=false;
	
	/**
	 * 产品图片List  返回给portal使用
	 * @author longxianzhen  2015/06/09
	 */
	@Transient
	private List<Resource> imgList;
	
	public String getNutriLabel() {
		return nutriLabel;
	}

	public void setNutriLabel(String nutriLabel) {
		this.nutriLabel = nutriLabel;
	}

	public NutritionReport getReport() {
		return report;
	}

	public void setReport(NutritionReport report) {
		this.report = report;
	}
	@Transient
	private List<ProductSortVo> recommandProductList = new ArrayList<ProductSortVo>();    //大众门户VO查询某个产品详情接口时,返回同类产品
	
	public List<ProductSortVo> getRecommandProductList() {
		return recommandProductList;
	}

	public void setRecommandProductList(List<ProductSortVo> recommandProductList) {
		this.recommandProductList = recommandProductList;
	}
	@Transient
	private List<ProductNutrition> listOfNutrition;
	
	public List<Certification> getProductCertification() {
		return productCertification;
	}

	public void setProductCertification(List<Certification> productCertification) {
		this.productCertification = productCertification;
	}

	
	public String getSelectedCustomerNames() {
		return selectedCustomerNames;
	}

	public void setSelectedCustomerNames(String selectedCustomerNames) {
		this.selectedCustomerNames = selectedCustomerNames;
	}

	@Transient
	private String faceType;
	
	@Transient
	private boolean hasSelf;
	
	@Transient
	private boolean hasCensor;
	
	@Transient
	private boolean hasSample;
	
	@Transient
	private String[] imgUrlList;
	
	@Transient
	private TestRptJson selfRpt;
	
	@Transient
	private TestRptJson censorRpt;
	
	@Transient
	private TestRptJson sampleRpt;
	
	public String getICB_category() {
		return ICB_category;
	}

	public void setICB_category(String iCB_category) {
		ICB_category = iCB_category;
	}

	public Integer getSelfCount() {
		return selfCount;
	}
    
	public void setSelfCount(Integer selfCount) {
		this.selfCount = selfCount;
	}

	public Integer getCensorCount() {
		return censorCount;
	}

	public void setCensorCount(Integer censorCount) {
		this.censorCount = censorCount;
	}

	public BusinessUnit getProducer() {
		return producer;
	}

	public void setProducer(BusinessUnit producer) {
		this.producer = producer;
	}
	
	public String getRegularityStr() {
		return regularityStr;
	}

	public void setRegularityStr(String regularityStr) {
		this.regularityStr = regularityStr;
	}

	public Integer getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(Integer sampleCount) {
		this.sampleCount = sampleCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	/*public Set<Resource> getProAttachments() {
		return proAttachments;
	}

	public void setProAttachments(Set<Resource> proAttachments) {
		this.proAttachments = proAttachments;
	}*/

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public TestRptJson getSelfRpt() {
		return selfRpt;
	}

	public void setSelfRpt(TestRptJson selfRpt) {
		this.selfRpt = selfRpt;
	}

	public TestRptJson getCensorRpt() {
		return censorRpt;
	}

	public void setCensorRpt(TestRptJson censorRpt) {
		this.censorRpt = censorRpt;
	}

	public TestRptJson getSampleRpt() {
		return sampleRpt;
	}

	public void setSampleRpt(TestRptJson sampleRpt) {
		this.sampleRpt = sampleRpt;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String[] getImgUrlList() {
		return imgUrlList;
	}

	public void setImgUrlList(String[] imgUrlList) {
		this.imgUrlList = imgUrlList;
	}

	public boolean isHasSelf() {
		return hasSelf;
	}

	public void setHasSelf(boolean hasSelf) {
		this.hasSelf = hasSelf;
	}

	public boolean isHasCensor() {
		return hasCensor;
	}

	public void setHasCensor(boolean hasCensor) {
		this.hasCensor = hasCensor;
	}

	public boolean isHasSample() {
		return hasSample;
	}

	public void setHasSample(boolean hasSample) {
		this.hasSample = hasSample;
	}

	public String getFaceType() {
		return faceType;
	}

	public void setFaceType(String faceType) {
		this.faceType = faceType;
	}

	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

/*	public Set<BusinessCertification> getListOfCertification() {
		return listOfCertification;
	}

	public void setListOfCertification(
			Set<BusinessCertification> listOfCertification) {
		this.listOfCertification = listOfCertification;
	}*/

	/*public void setRegularity(Set<ProductCategoryInfo> regularity) {
		this.regularity = regularity;
	}
	
	public Set<ProductCategoryInfo> getRegularity() {
		return regularity;
	}*/
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public SampleBusinessBrand getSampleBusinessBrand() {
		return sampleBusinessBrand;
	}

	public void setSampleBusinessBrand(SampleBusinessBrand sampleBusinessBrand) {
		this.sampleBusinessBrand = sampleBusinessBrand;
	}

	public List<ProductInstance> getProductInstances() {
		return productInstances;
	}

	public void setProductInstances(List<ProductInstance> productInstances) {
		this.productInstances = productInstances;
	}

	public ProductInstance getLatestProductInstance() {
		return latestProductInstance;
	}

	public void setLatestProductInstance(ProductInstance latestProductInstance) {
		this.latestProductInstance = latestProductInstance;
	}
	
	public Float getQscoreSelf() {
		return qscoreSelf;
	}

	public void setQscoreSelf(Float qscoreSelf) {
		this.qscoreSelf = qscoreSelf;
	}

	public Float getQscoreCensor() {
		return qscoreCensor;
	}

	public void setQscoreCensor(Float qscoreCensor) {
		this.qscoreCensor = qscoreCensor;
	}

	public Float getQscoreSample() {
		return qscoreSample;
	}

	public void setQscoreSample(Float qscoreSample) {
		this.qscoreSample = qscoreSample;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getCstm() {
		return cstm;
	}

	public void setCstm(String cstm) {
		this.cstm = cstm;
	}

	public ProductCategoryInfo getCategory() {
		return category;
	}

	public void setCategory(ProductCategoryInfo category) {
		this.category = category;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	
	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	
	public List<ProductNutrition> getListOfNutrition() {
		return listOfNutrition;
	}

	public void setListOfNutrition(List<ProductNutrition> listOfNutrition) {
		this.listOfNutrition = listOfNutrition;
	}

	public String getPdfFormat() {
		return pdfFormat;
	}

	public void setPdfFormat(String pdfFormat) {
		this.pdfFormat = pdfFormat;
	}

	public boolean isBindOfProducer() {
		return isBindOfProducer;
	}

	public void setBindOfProducer(boolean isBindOfProducer) {
		this.isBindOfProducer = isBindOfProducer;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public char getPackageFlag() {
		return packageFlag;
	}

	public void setPackageFlag(char packageFlag) {
		this.packageFlag = packageFlag;
	}

	public Double getRiskIndex() {
		return riskIndex;
	}

	public void setRiskIndex(Double riskIndex) {
		this.riskIndex = riskIndex;
	}

	public Date getRiskIndexDate() {
		return riskIndexDate;
	}

	public void setRiskIndexDate(Date riskIndexDate) {
		this.riskIndexDate = riskIndexDate;
	}

	public SampleProduct() {}
	
	public SampleProduct(SampleProduct product) {
		this.barcode = product.getBarcode();
		this.name = product.getName();
		this.status = product.getStatus();
		this.format = product.getFormat();
		this.expiration = product.getExpiration();
		this.expirationDate = product.getExpirationDate();
		this.sampleBusinessBrand = product.getSampleBusinessBrand();
		this.category = product.getCategory();
		this.imgUrl = product.getImgUrl();
		//this.proAttachments = product.getProAttachments();
		this.unit = product.getUnit();
		//this.regularity=product.getRegularity();
	}

	public SampleProduct(Long id, String name, Long businessBrandId, String format, String barcode, String cstm, String ingredient,String businessBrandName) {
		this.id = id;
		this.name = name;
		this.sampleBusinessBrand = new SampleBusinessBrand();
		this.sampleBusinessBrand.setId(businessBrandId);
		this.sampleBusinessBrand.setName(businessBrandName);
		this.format = format;
		this.barcode = barcode;
		this.cstm = cstm;
		this.ingredient = ingredient;
	}

	public SampleProduct(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/*public void removeResources(Set<Resource> removes) {
		for(Resource resource : removes){
			this.proAttachments.remove(resource);
		}
	}

	public void addResources(Set<Resource> adds) {
		for(Resource resource : adds){
			this.proAttachments.add(resource);
		}
	}*/
	
   public String getSecondCategoryCode() {
        return secondCategoryCode;
    }

    public void setSecondCategoryCode(String secondCategoryCode) {
        this.secondCategoryCode = secondCategoryCode;
    }

	public char getNutriStatus() {
		return nutriStatus;
	}

	public void setNutriStatus(char nutriStatus) {
		this.nutriStatus = nutriStatus;
	}

	public Boolean getRisk_succeed() {
		return risk_succeed;
	}

	public void setRisk_succeed(Boolean risk_succeed) {
		this.risk_succeed = risk_succeed;
	}

	public String getTestPropertyName() {
		return testPropertyName;
	}

	public void setTestPropertyName(String testPropertyName) {
		this.testPropertyName = testPropertyName;
	}

	public String getRiskFailure() {
		return riskFailure;
	}

	public void setRiskFailure(String riskFailure) {
		this.riskFailure = riskFailure;
	}

	public String getSelectedCustomerIds() {
		return selectedCustomerIds;
	}

	public void setSelectedCustomerIds(String selectedCustomerIds) {
		this.selectedCustomerIds = selectedCustomerIds;
	}

	public QsNoAndFormatVo getQs_info() {
		return qs_info;
	}

	public void setQs_info(QsNoAndFormatVo qs_info) {
		this.qs_info = qs_info;
	}

	public String getNetContent() {
		return netContent;
	}

	public void setNetContent(String netContent) {
		this.netContent = netContent;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public ImportedProduct getImportedProduct() {
		return importedProduct;
	}

	public void setImportedProduct(ImportedProduct importedProduct) {
		this.importedProduct = importedProduct;
	}

	public boolean isEnshrine() {
		return isEnshrine;
	}

	public void setEnshrine(boolean isEnshrine) {
		this.isEnshrine = isEnshrine;
	}

	public List<Resource> getImgList() {
		return imgList;
	}

	public void setImgList(List<Resource> imgList) {
		this.imgList = imgList;
	}

	public List<ProductRecommendUrl> getProUrlList() {
		return proUrlList;
	}

	public void setProUrlList(List<ProductRecommendUrl> proUrlList) {
		this.proUrlList = proUrlList;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

}