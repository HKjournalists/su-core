package com.tzquery.fsn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StringUtil {

	public static boolean isNotEmpty(Object o){
		if(o!=null&&!"".equals(o)){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Object o){
		if(o==null||"".equals(o)){
			return true;
		}
		return false;
	}
	
	/**
	 * 日期字符串格式化为给你格式的日期字符串
	 * @author ChenXiaolin 2015-12-03
	 * @param dateStr
	 * @param type
	 * @return
	 */
	public static String datestrToDataStr(String dateStr,String type){
		SimpleDateFormat sdfNew = new SimpleDateFormat("yyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		String date = "";
		if(isNotEmpty(dateStr)){
			try {
				if(dateStr.length()<19){
					date = sdfNew.format(sdfNew.parse(dateStr))+" 00:00:00";
				}else{
					date = sdf.format(sdf.parse(dateStr));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
}
