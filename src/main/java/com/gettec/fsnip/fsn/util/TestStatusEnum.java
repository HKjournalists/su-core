package com.gettec.fsnip.fsn.util;

public enum TestStatusEnum {

	/*OPEN(0, "Open"),
	IN_PROGRESS(1, "In-progress"),
	COMPLETED(2, "Completed");*/
	OPEN(0, "新建"),
	IN_PROGRESS(1, "处理中"),
	COMPLETED(2, "完成");
	
	private int code;
	
	private String name;

	private TestStatusEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public static TestStatusEnum getRole(int code){
		for(TestStatusEnum r : TestStatusEnum.values()){
			if(r.getCode() == code){
				return r;
			}
		}
		return OPEN;
	}
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
}
