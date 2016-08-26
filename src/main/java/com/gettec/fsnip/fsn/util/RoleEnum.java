package com.gettec.fsnip.fsn.util;

public enum RoleEnum {

	/*ANONYMOUS(-1, "Anonymous"),
	ADMIN(1, "Admin"),
	BUSINESS(2, "Business"),
	FDA(3, "FDA"),
	TEST_LAB(4, "TestLab");*/
	ANONYMOUS(-1, "Anonymous", "匿名"),
	ADMIN(1, "Admin", "管理员"),
	BUSINESS(2, "Business", "企业"),
	FDA(3, "FDA", "食品与药物管理局"),
	TEST_LAB(4, "TestLab", "测试中心");
	
	private int code;
	
	private String name;
	
	private String alias;

	private RoleEnum(int code, String alias, String name) {
		this.code = code;
		this.alias =alias;
		this.name = name;
	}

	public static RoleEnum getRole(int code){
		for(RoleEnum r : RoleEnum.values()){
			if(r.getCode() == code){
				return r;
			}
		}
		return ANONYMOUS;
	}
	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}
}
