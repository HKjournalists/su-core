package com.gettec.fsnip.fsn.recycle;

import java.util.Map;
import java.util.TreeMap;

public enum Recycle_reason implements PersistEnum<Recycle_reason> {
	//回收原因枚举类
	COMPULSORY_RECALL("国家强制召回","compulsory_recall"),ADVENT("产品临期","advent"),
	BUYER_RETURN("购货商退货","buyer_return"),OTHER("其他","other");
	private static final Map<String, Recycle_reason> map = new TreeMap<String, Recycle_reason>();
	
	static {
        map.put(COMPULSORY_RECALL.getValue(), COMPULSORY_RECALL);
        map.put(ADVENT.getValue(), ADVENT);
        map.put(BUYER_RETURN.getValue(), BUYER_RETURN);
        map.put(OTHER.getValue(), OTHER);
    }
	
	private String name;//原因
	private String value;//值
	
	@Override
	public String getPersistedValue() {
		return getValue();
	}

	@Override
	public Recycle_reason returnEnum(String persistedValue) {
		return map.get(persistedValue);
	}

	@Override
	public Map<String, Recycle_reason> getAllValueMap() {
		return map;
	}

	Recycle_reason(String name,String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	public static Map<String, Recycle_reason> getMap() {
		return map;
	}
}