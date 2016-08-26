package com.lhfs.fsn.vo.product;

import java.util.List;

import com.gettec.fsnip.fsn.model.base.Certification;


/**
 * 食安监产品基本信息VO
 * @author YongHuang
 */
public class ProductSimpleVO {
    private Long id;
    private String name;  // 产品名称
    private String barcode; 
    private String image; //产品图片
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String imgUrlStr) {
        if (imgUrlStr != null) {
            this.image = imgUrlStr.split("\\|")[0];
        }
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

}
