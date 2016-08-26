package com.lhfs.fsn.vo.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gettec.fsnip.fsn.model.market.Resource;
import com.lhfs.fsn.vo.product.ProductSimpleVO;

public class BussinessUnitVO {

	private Long id;
	
	private String licenseNo; //营业执照号
	
	private String name;  // 企业名称
	
	private String address;	 //地址
	
	private String type;     //企业类型
	
	private String personInCharge;  // 法定代表人
	
	private List<String> licAttachments;  // 营业执照证
	
	private List<String> orgAttachments;  // 组织机构证
	
    private List<ProductSimpleVO> productList; //主营商品
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public List<String> getLicAttachments() {
		return licAttachments;
	}
	public void setLicAttachments(List<String> licAttachments) {
		this.licAttachments = licAttachments;
	}
	
	public List<String> getOrgAttachments() {
		return orgAttachments;
	}
	public void setOrgAttachments(List<String> orgAttachments) {
		this.orgAttachments = orgAttachments;
	}

	public void setLicImageUrl(Set<Resource> imageUrl) {
        if (imageUrl.size() > 0 ) {
            for(Resource mkTestResource : imageUrl) {
            	licAttachments = licAttachments != null ? licAttachments:new ArrayList<String>(); 
            	licAttachments.add(mkTestResource.getUrl());
            }
        }
    }
	
	public void setOrgImageUrl(Set<Resource> imageUrl) {
        if (imageUrl.size() > 0 ) {
            for(Resource mkTestResource : imageUrl) {
                orgAttachments = orgAttachments != null ? orgAttachments:new ArrayList<String>(); 
            	orgAttachments.add(mkTestResource.getUrl()); 
            }
        }
    }
    public List<ProductSimpleVO> getProductList() {
        return productList;
    }
    public void setProductList(List<ProductSimpleVO> productList) {
        this.productList = productList;
    }
}
