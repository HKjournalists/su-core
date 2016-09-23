package com.gettec.fsnip.fsn.enums;

public enum SimpleModelTypeEnums {

	BUSINESS_TYPE(1),
	CUSTOMER_TYPE(2), // 客户
	PROVIDER_TYPE(3), // 供应商
	UNIT(4), // 单位
	IN_STORAGE_TYPE(5), // 
	OUT_STORAGE_TYPE(6),
	MERCHANDISE_TYPE(7),
	MERCHANDISE_CATEGORY(8),
	REASON_FOR_RETURN_GOODS(9),
	MERCHANDISE_INFO(10),
	ORDER_TYPE(11),  //单别
	RECEIVE_TYPE(12); //收货单
	
	private int type;

	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	/**
	 * @param type
	 */
	private SimpleModelTypeEnums(int type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @param type
	 * @return SimpleModelTypeEnums
	 */
	public static SimpleModelTypeEnums getModelTypeByType(int type){
		SimpleModelTypeEnums[] types = SimpleModelTypeEnums.values();
		for(SimpleModelTypeEnums item : types){
			if(item.getType() == type){
				return item;
			}
		}
		return BUSINESS_TYPE;
	}
}
