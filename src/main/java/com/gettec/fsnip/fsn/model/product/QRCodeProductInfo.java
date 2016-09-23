package com.gettec.fsnip.fsn.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.gettec.fsnip.fsn.model.common.Model;
import com.gettec.fsnip.fsn.model.market.Resource;

/**
 * 二维码产品信息
 * @author TangXin
 *
 */
@Entity(name="qrcode_product_info")
public class QRCodeProductInfo extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	@Column(name="inner_code")
	private String innerCode; //内部码
	
	@Column(name="product_area_code")
	private String productAreaCode; //产品所属区域
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="kms_label_id",referencedColumnName = "kms_label_id", nullable=true)
	private KmsLabel kmsLabel;  //kms提供的产品标签
	
	@Column(name="serial_number")
	private Long serialNumber; //产品流水号，不同企业从0计数
	
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="resource_id", nullable=true)
	private Resource resource;  //二维码图片资源
	
	@Transient
	private String cityCode; //城市代码
	
	@Transient
	private String enterpriseName; //生产企业名称
	
	@Transient
	private String proAreaName; //产品所属区域名称

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInnerCode() {
		return innerCode;
	}

	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}

	public String getProductAreaCode() {
		return productAreaCode;
	}

	public void setProductAreaCode(String productAreaCode) {
		this.productAreaCode = productAreaCode;
	}

	public KmsLabel getKmsLabel() {
		return kmsLabel;
	}

	public void setKmsLabel(KmsLabel kmsLabel) {
		this.kmsLabel = kmsLabel;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProAreaName() {
		return proAreaName;
	}

	public void setProAreaName(String proAreaName) {
		this.proAreaName = proAreaName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}
}
