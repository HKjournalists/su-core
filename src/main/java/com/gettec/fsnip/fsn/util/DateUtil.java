package com.gettec.fsnip.fsn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateUtil {

	public static Date truncateTime(Date date){
		return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
	}
	
	public static String md(Date date){
		return new SimpleDateFormat("M/d").format(date);
	}
	public static String yyyymmdd(Date date){
		return new SimpleDateFormat("yyyy/MM/dd").format(date);
	}
	
	public static Date addDays(Date date, int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
		return cal.getTime();
	}
	
	public static String addDays(String date, int days) throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date endDt = dateFormat.parse(date);
		Calendar cd = Calendar.getInstance(); 
	    cd.setTime(endDt);//设置当前日期  
	    cd.add(Calendar.DATE, days); //日期加N天  
	    return dateFormat.format(cd.getTime());
	}

	public static Date StringToDate(String sDate, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(sDate);
		return date;
	}
	
	public static Date str2Date(String strDate)
	{
		if(strDate == null)return null;
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		   Date date = null; 
		   try { 
		        date = format.parse(strDate); 
		   } catch (ParseException e) { 
		        return null;
		   } 
		   return date; 
	}
}
