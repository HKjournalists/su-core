package com.gettec.fsnip.fsn.model.product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.erp.base.Unit;
import com.gettec.fsnip.fsn.model.market.MkTempProduct;
import com.gettec.fsnip.fsn.model.test.TestResult;
import com.gettec.fsnip.fsn.web.controller.JsonDateDeserializer;
import com.gettec.fsnip.fsn.web.controller.JsonDateSerializer;

/**
 * ProductInstance Entity<br>
 * 
 * 
 * @author Ryan Wang
 */
@Entity(name = "product_instance")
public class ProductInstance extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4806499771963635303L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "batch_serial_no", length = 50)
	private String batchSerialNo;   // 批次

	@Column(name = "serial", length = 50)
	private String serial;
	
	@Column(name = "production_date")
	private Date productionDate;   // 生产日期
	
	@Column(name = "expiration_date")
	private Date expirationDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="original_id", nullable=true)
	private ProductInstance original;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="producer_id", nullable=true)
	private BusinessUnit producer;
	
	/**
	 * 记录当前报告的qs号
	 * @author ZhangHui 2015/6/16
	 */
	@Column(name = "qs_no")
	private String qs_no = "";

	@Transient
	private List<TestResult> testResults;
	
	@Transient
	private Integer like;
	
	@Transient
	private Integer complainant;
	
	@Transient
	private Double rateAvg;
	
	@Transient
	private String proDateStr; //生产日期字符窜格式，用来处理“0000-00-00”
	
	public BusinessUnit getProducer() {
		return producer;
	}

	public void setProducer(BusinessUnit producer) {
		this.producer = producer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getProductionDate() {
		return productionDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getExpirationDate() {
		return expirationDate;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductInstance getOriginal() {
		return original;
	}

	public void setOriginal(ProductInstance original) {
		this.original = original;
	}

	public List<TestResult> getTestResults() {
		return testResults;
	}

	public void setTestResults(List<TestResult> testResults) {
		this.testResults = testResults;
	}

	public Integer getLike() {
		return like;
	}

	public void setLike(Integer like) {
		this.like = like;
	}

	public Integer getComplainant() {
		return complainant;
	}

	public void setComplainant(Integer complainant) {
		this.complainant = complainant;
	}

	public Double getRateAvg() {
		return rateAvg;
	}

	public void setRateAvg(Double rateAvg) {
		this.rateAvg = rateAvg;
	}

	public String getProDateStr() {
		return proDateStr;
	}

	public void setProDateStr(String proDateStr) {
		this.proDateStr = proDateStr;
	}

	public String getQs_no() {
		return qs_no;
	}

	public void setQs_no(String qs_no) {
		this.qs_no = qs_no;
	}

	public ProductInstance() {
		super();
	}

	public ProductInstance(String batchSerialNo, String serial,
			Date productionDate, Date expirationDate, Product product,
			ProductInstance original, List<TestResult> testResults,
			Integer like, Integer complainant, Double rateAvg) {
		super();
		this.batchSerialNo = batchSerialNo;
		this.serial = serial;
		this.productionDate = productionDate;
		this.expirationDate = expirationDate;
		this.product = product;
		this.original = original;
		this.testResults = testResults;
		this.like = like;
		this.complainant = complainant;
		this.rateAvg = rateAvg;
	}

	public ProductInstance(MkTempProduct tempProduct) {
		String proDate = tempProduct.getProDate();
		this.proDateStr = proDate;
		proDate = (("0000-00-00".equals(proDate)||"".equals(proDate)) ? null : proDate);
		try {
			this.productionDate =  proDate == null ? null:new SimpleDateFormat("yyyy-MM-dd").parse(proDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.batchSerialNo = tempProduct.getBatchNo();
		
		this.product = new Product();
		this.product.setBusinessBrand(new BusinessBrand());
		this.product.getBusinessBrand().setName(tempProduct.getBrand());
		this.product.setBarcode(tempProduct.getBarCode());
		this.product.setName(tempProduct.getName());
		this.product.setFormat(tempProduct.getModelNo());
		this.product.setStatus(tempProduct.getProductStatus());
		this.product.setExpirationDate(tempProduct.getExpirDay());
		this.product.setExpiration(tempProduct.getExpiration());
		this.product.setUnit(new Unit(tempProduct.getMinUnit()));
	}

	public ProductInstance(ProductInstance sample) {
		this.productionDate = sample.getProductionDate();
		this.batchSerialNo = sample.getBatchSerialNo();
	}
}
