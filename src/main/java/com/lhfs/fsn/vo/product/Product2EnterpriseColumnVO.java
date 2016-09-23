package com.lhfs.fsn.vo.product;

import java.util.List;

import com.gettec.fsnip.fsn.model.base.Certification;


/**
 * 食安监产品基本信息VO
 * @author YongHuang
 */
public class Product2EnterpriseColumnVO {
    private Long id;
    private String name;  // 产品名称
    private String imgUrl; //产品图片
    private Long reportCount; //报告数量
    private List<Certification> productCertification; // 认证信息
    
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
	public Long getReportCount() {
		return reportCount;
	}
	public void setReportCount(Long reportCount) {
		this.reportCount = reportCount;
	}
    public List<Certification> getProductCertification() {
        return productCertification;
    }
    public void setProductCertification(List<Certification> productCertification) {
        this.productCertification = productCertification;
    }
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Product2EnterpriseColumnVO(Long id, String name, String imgUrl,
			Long reportCount, List<Certification> productCertification) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.reportCount = reportCount;
		this.productCertification = productCertification;
	}
	public Product2EnterpriseColumnVO() {
		super();
	}
}
