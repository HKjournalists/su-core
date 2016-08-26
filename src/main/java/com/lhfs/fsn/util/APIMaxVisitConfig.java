package com.lhfs.fsn.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口访问并发量配置
 * @author tangxin 2016/6/29
 *
 */
public class APIMaxVisitConfig {
	
	/**
	 * API 访问的并发量
	 * key 值为 AbstractSignVerify 的实现类的包名+类名+抽象类的方法名（verifyAndDoPassHandle）
	 */
	public static final Map<String, Integer> API_MAX_VIS_MAP = new HashMap<String, Integer>();
}
