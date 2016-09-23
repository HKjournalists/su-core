package com.gettec.fsnip.fsn.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	private static Map<String,String> propertiesList = new HashMap<String,String>();
	static {
		readProperties("/META-INF/fsn.properties");
	}
	private static void readProperties(String propertiesFile){
		InputStream in = PropertiesUtil.class.getResourceAsStream(propertiesFile);
		Properties properties = new Properties();
	    try {
			properties.load(in);
			Enumeration<?> en = properties.keys();
            while(en.hasMoreElements()){  
                String key = en.nextElement().toString();  
                propertiesList.put(key, properties.getProperty(key));
            }
            in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getProperty(String key){
		return propertiesList.get(key);
	}
	/**
	 * 判断当前登录企业录报告时是否需要填写生产日期
	 * @param orgId 当前登录企业组织机构ID
	 * @return true:必填  false:非必填
	 * @author 龙宪真  2017/07/17
	 */
	public static boolean getProDateIsRequired(Long orgId){
		if(orgId==null){
			return true;
		}
		String val=propertiesList.get("fsn.get.businessUnit.report.proDateNotRequired.OrgIds");
		String[] orgIds=val.split(";");
		String oId=Long.toString(orgId);
		boolean isRequired=true;
		for(String s:orgIds){
			if(oId.equals(s)){
				isRequired=false;
				break;
			}
		}
		return isRequired;
	}
}
