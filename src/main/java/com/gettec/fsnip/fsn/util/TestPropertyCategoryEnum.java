package com.gettec.fsnip.fsn.util;

public enum TestPropertyCategoryEnum {

	NUTRITIONAL_INGREDIENT ("1001", "营养成份"),
	TRACE_ELEMENT("1002", "微量元素");
	
	private String code;
	
	private String name;

	private TestPropertyCategoryEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static TestPropertyCategoryEnum getRole(String code){
		for(TestPropertyCategoryEnum r : TestPropertyCategoryEnum.values()){
			if(r.getCode().equals(code)){
				return r;
			}
		}
		return null;
	}
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
}
