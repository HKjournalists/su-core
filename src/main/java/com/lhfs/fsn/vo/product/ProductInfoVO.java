package com.lhfs.fsn.vo.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.product.BusinessCertification;

/**
 * 食安监产品VO
 * @author YongHuang
 */
public class ProductInfoVO {
    private Long id;
    private String name;  // 产品名称
    private String status;  // 商品状态
    private String format;   // 规格'
    private String regularity;  //  执行标准
    private String barcode;  //  条形码
    private String businessBrand;  // 商标
    private String unit;//unit 单位
    private String category;    //  产品类别
    private String expirationDate;  // 保质天数
    private String expiration;  // 保质期
    private String businessName;  // 所属企业名称
    private String address;//企业地址
    private String licenseNo;//营业执照号
    private String telephone;//电话
    private String contact;//联系人
    private Long orgId;//组织机构ID
    private String batch;  // 批次
    private List<Certification> productCertification; // 认证信息（无）  
    private String ingredient;   // 配料（无）
    private String des; //产品简介
    private String imgUrlList; //产品图片
    private Boolean reportOverDate; //报告是否过期

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
    public String getBusinessBrand() {
        return businessBrand;
    }
    public void setBusinessBrand(String businessBrand) {
        this.businessBrand = businessBrand;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    public List<Certification> getProductCertification() {
        return productCertification;
    }
    public void setProductCertification(List<Certification> productCertification) {
        this.productCertification = productCertification;
    }
    public String getImgUrlList() {
        return imgUrlList;
    }
    public void setImgUrlList(String imgUrlStr) {
        if (imgUrlStr != null) {
            this.imgUrlList = imgUrlStr.split("\\|")[0];
        }
    }
    public void setProductCertification(Set<BusinessCertification> listOfCertification) {
        if (listOfCertification.size() > 0 ) {
            productCertification = productCertification != null ? productCertification : new ArrayList<Certification>();
            for(BusinessCertification businessCertification : listOfCertification) {
            	if(businessCertification.getCertResource() != null){
            		businessCertification.getCert().setDocumentUrl(businessCertification.getCertResource().getUrl()); //认证的大图标
            	}
                this.productCertification.add(businessCertification.getCert());//认证的小图标
            }
        }
    }
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
    public String getExpiration() {
        return expiration;
    }
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getBatch() {
        return batch;
    }
    public void setBatch(String batch) {
        this.batch = batch;
    }
	public Boolean getReportOverDate() {
		return reportOverDate;
	}
	public void setReportOverDate(Boolean reportOverDate) {
		this.reportOverDate = reportOverDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
