package com.gettec.fsnip.fsn.transfer;

import java.util.List;

import com.gettec.fsnip.fsn.model.business.BusinessUnit;
import com.gettec.fsnip.fsn.model.product.Product;

public class ProductTransfer {
	public static Product transfer(Product product){
		if(product==null){
			return null; 
		}
		if(product.getProducer()!=null){
			BusinessUnit businessUnit=BusinessUnitTransfer.transfer(product.getProducer());
			product.setProducer(businessUnit);
		}
		if(product.getBusinessBrand()!=null&&product.getBusinessBrand().getBusinessUnit()!=null){
			BusinessUnit bu=BusinessUnitTransfer.transfer(product.getBusinessBrand().getBusinessUnit());
			product.getBusinessBrand().setBusinessUnit(bu);
		}
		return product;
	}
	public static List<Product> transfer(List<Product> productList){
		for(Product p:productList){
			transfer(p);
		}
		return productList;
	}
}
