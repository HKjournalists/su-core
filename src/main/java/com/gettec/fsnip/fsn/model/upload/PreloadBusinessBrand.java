package com.gettec.fsnip.fsn.model.upload;

import java.util.List;
import com.gettec.fsnip.fsn.model.business.BusinessBrand;
import com.gettec.fsnip.fsn.model.business.BusinessUnit;
/**
 * 
 * @author Kxo
 *
 * store preload data for brand
 */
public class PreloadBusinessBrand {
	// store the original brand data if id exists;
	private BusinessBrand brand;
	// store all the unit for user to picker
	private List<BusinessUnit> unitList;
	
	// getters & setters
	public BusinessBrand getBrand() {
		return brand;
	}
	public void setBrand(BusinessBrand brand) {
		this.brand = brand;
	}
	public List<BusinessUnit> getUnitList() {
		return unitList;
	}
	public void setUnitList(List<BusinessUnit> unitList) {
		this.unitList = unitList;
	}
	
	
	
}
