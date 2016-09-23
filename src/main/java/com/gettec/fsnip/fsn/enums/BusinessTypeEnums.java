package com.gettec.fsnip.fsn.enums;

public enum BusinessTypeEnums {

	PURCHASE_ORDER(1,"T_BUSS_PURCHASE_ORDER","采购订单"),
	PURCHASE_BILLING(2,"T_BUSS_PURCHASE_BILLING","采购单"),
	SALES_ORDER(3,"T_BUSS_SALES_ORDER","销售订单"),
	SALES_BILLING(4,"T_BUSS_SALES_BILLING","销售单"),
	IN_STORAGE(5,"T_BUSS_IN_STORAGE_RECORD","入库"),
	OUT_STORAGE(6,"T_BUSS_OUT_STORAGE_RECORD","出库"),
	FLITTING_ORDER(7,"T_BUSS_FLITTING_ORDER","调拨");
	
	
	private int id;
	private String tableName;
	private String typeName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * @param id
	 * @param tableName
	 */
	private BusinessTypeEnums(int id, String tableName, String typeName) {
		this.id = id;
		this.tableName = tableName;
		this.typeName = typeName;
	}
	
	public static BusinessTypeEnums getEnumByID(int id){
		BusinessTypeEnums[] values = BusinessTypeEnums.values();
		for(BusinessTypeEnums item : values){
			if(item.getId() == id)
				return item;
		}
		return null;
	}
}
