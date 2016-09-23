package com.gettec.fsnip.fsn.vo.product.erp;

/**
 * 引进产品时，保存销往企业（用于前台封装使用）
 * @author Zhanghui 2015/4/8
 */
public class ProductLeadVO {
	private String barcode;
	private String selectedCustomerIds;
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSelectedCustomerIds() {
		return selectedCustomerIds;
	}
	public void setSelectedCustomerIds(String selectedCustomerIds) {
		this.selectedCustomerIds = selectedCustomerIds;
	}
}
