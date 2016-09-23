package com.lhfs.fsn.vo.product;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.product.NutritionReport;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategoryInfo;
import com.gettec.fsnip.fsn.model.product.ProductInstance;
import com.gettec.fsnip.fsn.model.test.TestRptJson;

/**
 * 大众门户VO
 * @author Administrator
 */
public class ProductVo {
	private Long id;
	private String name;
	private String status;
	private String format;
	private String regularity;
	private String barcode;
	private String note;
	private String ICB_category;
	private BusinessBrand businessBrand;
	private List<ProductInstance> productInstances;
	private ProductInstance latestProductInstance;
	private Float qscoreSelf;
	private Float qscoreCensor;
	private Float qscoreSample;
	private String imgUrl;
	private Integer selfCount;
	private Integer censorCount;
	private Integer sampleCount;
	private String des;
	private String cstm;
	private String category;
	private String feature;
	private String ingredient;
	private String characteristic; // 产品特色
	private String expirationDate; //产品保质期
	private String regularityCat; //标准分类
	private String trademark; //注册商标
	private String factoryInspection; //出厂检验方式
	private String saleArea; //销售区域
	private String isExport; //是否为出口产品
	private String licenseNo; //许可证号
	private String licenseUnit; //发证单位
	private String licenseStart; //发证日期
	private String license_end; //有效期至
	private String licenseStatus; //证书状态
	private String manuStatus; //生产状态
	private String verifyStatus; //审核状态
	private List<Certification> productCertification;
	private String faceType;
	private boolean hasSelf;
	private boolean hasCensor;
	private boolean hasSample;
	private String[] imgUrlList;
	private TestRptJson selfRpt;
	private TestRptJson censorRpt;
	private TestRptJson sampleRpt;
	@JsonIgnore 
	private NutritionReport report;
	
	public ProductVo(Product p){
		this.setBarcode(p.getBarcode());
		this.setBusinessBrand(p.getBusinessBrand());
		this.setCategory(p.getCategory().getName());
		this.setCensorCount(p.getCensorCount());
		this.setCensorRpt(p.getCensorRpt());
		this.setCharacteristic(p.getCharacteristic());
		this.setCstm(p.getCstm());
		this.setDes(p.getDes());
		this.setExpirationDate(p.getExpirationDate());
		this.setFaceType(p.getFaceType());
		this.setFeature(p.getFeature());
		this.setFormat(p.getFormat());
		this.setHasCensor(p.isHasCensor());
		this.setHasSample(p.isHasSample());
		this.setHasSelf(p.isHasSelf());
		this.setICB_category(p.getICB_category());
		this.setId(p.getId());
		this.setImgUrl(p.getImgUrl());
		this.setImgUrlList(p.getImgUrlList());
		this.setIngredient(p.getIngredient());
		this.setLatestProductInstance(p.getLatestProductInstance());
		this.setName(p.getName());
		this.setNote(p.getNote());
		this.setProductInstances(p.getProductInstances());
		this.setQscoreCensor(p.getQscoreCensor());
		this.setQscoreSample(p.getQscoreSample());
		this.setQscoreSelf(p.getQscoreSelf());
		String regularitys = "";
		for(ProductCategoryInfo regularity : p.getRegularity()){
			if(regularity.getName()==null||regularity.getName()==""){
				continue;
			}else{
				regularitys = regularitys+regularity.getName()+";";
			}
		}
		this.setRegularity(regularitys);
		this.setSampleCount(p.getSampleCount());
		this.setSampleRpt(p.getSampleRpt());
		this.setSelfCount(p.getSelfCount());
		this.setSelfRpt(p.getSelfRpt());
		this.setStatus(p.getStatus());
	}
	
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
	public Integer getSampleCount() {
		return sampleCount;
	}
	public void setSampleCount(Integer sampleCount) {
		this.sampleCount = sampleCount;
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
	public List<Certification> getProductCertification() {
		return productCertification;
	}
	public void setProductCertification(List<Certification> productCertification) {
		this.productCertification = productCertification;
	}
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
	public String getRegularity() {
		return regularity;
	}
	public void setRegularity(String regularity) {
		this.regularity = regularity;
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
	public BusinessBrand getBusinessBrand() {
		return businessBrand;
	}
	public void setBusinessBrand(BusinessBrand businessBrand) {
		this.businessBrand = businessBrand;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public NutritionReport getReport() {
		return report;
	}
	@JsonIgnore
	public void setReport(NutritionReport report) {
		this.report = report;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getRegularityCat() {
		return regularityCat;
	}
	public void setRegularityCat(String regularityCat) {
		this.regularityCat = regularityCat;
	}
	public String getTrademark() {
		return trademark;
	}
	public void setTrademark(String trademark) {
		this.trademark = trademark;
	}
	public String getFactoryInspection() {
		return factoryInspection;
	}
	public void setFactoryInspection(String factoryInspection) {
		this.factoryInspection = factoryInspection;
	}
	public String getSaleArea() {
		return saleArea;
	}
	public void setSaleArea(String saleArea) {
		this.saleArea = saleArea;
	}
	public String getIsExport() {
		return isExport;
	}
	public void setIsExport(String isExport) {
		this.isExport = isExport;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getLicenseUnit() {
		return licenseUnit;
	}
	public void setLicenseUnit(String licenseUnit) {
		this.licenseUnit = licenseUnit;
	}
	public String getLicenseStart() {
		return licenseStart;
	}
	public void setLicenseStart(String licenseStart) {
		this.licenseStart = licenseStart;
	}
	public String getLicense_end() {
		return license_end;
	}
	public void setLicense_end(String license_end) {
		this.license_end = license_end;
	}
	public String getLicenseStatus() {
		return licenseStatus;
	}
	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}
	public String getManuStatus() {
		return manuStatus;
	}
	public void setManuStatus(Integer manuStatus) {
		if(manuStatus == null || manuStatus == 0)
			this.manuStatus = "未审核";
		else
			this.manuStatus = "审核通过";
	}
	public String getVerifyStatus() {
		return verifyStatus;
	}
	public void setVerifyStatus(Integer verifyStatus) {
		if(verifyStatus == null || verifyStatus ==0)
			this.verifyStatus = "未审核";
		else
			this.verifyStatus = "审核通过";
	}
}
