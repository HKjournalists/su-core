package com.gettec.fsnip.fsn.recycle;

import java.util.Map;
import java.util.TreeMap;

public enum Process_mode implements PersistEnum<Process_mode> {
	//处理方式枚举类
	RETURN_GOODS("退货","1"),DESTROY("销毁","2");
	private static final Map<String, Process_mode> map = new TreeMap<String, Process_mode>();
	
	static {
        map.put(RETURN_GOODS.getValue(), RETURN_GOODS);
        map.put(DESTROY.getValue(), DESTROY);
    }
	
	private String name;//原因
	private String value;//值
	
	@Override
	public String getPersistedValue() {
		return getValue();
	}

	@Override
	public Process_mode returnEnum(String persistedValue) {
		return map.get(persistedValue);
	}

	@Override
	public Map<String, Process_mode> getAllValueMap() {
		return map;
	}
	Process_mode(String name,String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

	public static Map<String, Process_mode> getMap() {
		return map;
	}
	
}