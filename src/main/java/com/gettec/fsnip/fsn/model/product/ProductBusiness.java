package com.gettec.fsnip.fsn.model.product;

import java.util.HashSet;
import java.util.Set;

import com.gettec.fsnip.fsn.model.market.Resource;

public class ProductBusiness {
	private Long productId;
	
	private String businessName;
	
	private ProductBusinessLicense ProductBusine;
	
	private Set<Resource> aryProBusiness = new HashSet<Resource>();  // 营业执照图片
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public ProductBusinessLicense getProductBusine() {
		return ProductBusine;
	}
	public void setProductBusine(ProductBusinessLicense productBusine) {
		ProductBusine = productBusine;
	}
	public Set<Resource> getAryProBusiness() {
		return aryProBusiness;
	}
	public void setAryProBusiness(Set<Resource> aryProBusiness) {
		this.aryProBusiness = aryProBusiness;
	}
	

}
