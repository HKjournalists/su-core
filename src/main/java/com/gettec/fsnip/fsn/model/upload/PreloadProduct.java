package com.gettec.fsnip.fsn.model.upload;

import java.util.List;

import com.gettec.fsnip.fsn.model.base.Certification;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.product.Product;
import com.gettec.fsnip.fsn.model.product.ProductCategory;

/**
 * 
 * @author Kxo
 *
 * store preload data for product
 */
public class PreloadProduct {
	private Product product;
	private List<BusinessBrand> brandList;
	private List<Certification> certificationList;
	private List<ProductCategory> categoryList;
	
	public List<ProductCategory> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<ProductCategory> categoryList) {
		this.categoryList = categoryList;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public List<BusinessBrand> getBrandList() {
		return brandList;
	}
	public void setBrandList(List<BusinessBrand> brandList) {
		this.brandList = brandList;
	}
	public List<Certification> getCertificationList() {
		return certificationList;
	}
	public void setCertificationList(List<Certification> certificationList) {
		this.certificationList = certificationList;
	}
	
	
}
