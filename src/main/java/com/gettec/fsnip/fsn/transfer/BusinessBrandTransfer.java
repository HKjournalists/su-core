package com.gettec.fsnip.fsn.transfer;

import java.util.List;

import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;

public class BusinessBrandTransfer {
	public static BusinessBrand transfer(BusinessBrand businessBrand){
		BusinessUnit businessUnit=BusinessUnitTransfer.transfer(businessBrand.getBusinessUnit());
		businessBrand.setBusinessUnit(businessUnit);
		return businessBrand;
	}
	public static List<BusinessBrand> transfer(List<BusinessBrand> businessBrandList){
		for(BusinessBrand b:businessBrandList){
			transfer(b);
		}
		return businessBrandList;
	}
}
