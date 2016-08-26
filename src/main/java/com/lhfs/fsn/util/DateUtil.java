package com.lhfs.fsn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	// 格式化日期
	public static Long formatDateToString(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date formatted = sdf.parse(date);
		return formatted.getTime();
	}
	
	// 格式化日期
	public static String formatDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String formatted = sdf.format(date);
		return formatted;
	}

	// 格式化日期
	public static String dateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatted = sdf.format(date);
		return formatted;
	}
	// 格式化日期
		public static String dateFormatYYYYMMDD(Date date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formatted = sdf.format(date);
			return formatted;
		}
	 // 格式验证
		public static Boolean matchesYYYYMMDD(String startDate) {
			boolean matches = startDate.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}");
			return matches;
		}
    //格式化日期文件名
	public static String dateFormat2(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String formatted = sdf.format(date);
		return formatted;
	}

	/**
	 * 比较date1，date2与date的距离，如果date2离date近返回false否则返回true
	 *
	 * @param date1
	 * @param date 格式为yyyy-MM-dd的字符串
	 * @param date2
	 * @return
	 * @throws ParseException
	 *
	 */
	public static boolean compareDate(Date date1, String date,
			Date date2) {
		try {
			String date1s = dateFormatYYYYMMDD(date1);
			String date2s = dateFormatYYYYMMDD(date2);
			Long date1l = formatDateToString(date1s);
			Long date2l=formatDateToString(date2s);
			Long datel=formatDateToString(date);
			if(Math.abs((date1l-datel))>=Math.abs((date2l-datel))){
				return false;
			}else{
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}
}
