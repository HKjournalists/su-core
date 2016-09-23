package com.gettec.fsnip.fsn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class FilterUtils {
	public static String getConditionStr(String fieldName,String mark,String value) {
		String new_mark = null;
		String value_=value;
		try{
			Long.parseLong(value.trim());
			value_=value;
		}catch(NumberFormatException nfe){
			if(mark.equals("gte")||mark.equals("gt")||mark.equals("lt")){
				return null;
			}
			value_="'"+value+"'";
		}
		if(mark.equals("eq")){
			new_mark = " = '"+value+"'";
		}else if(mark.equals("neq")){
			new_mark = " <> "+value_;
		}else if(mark.equals("gte")){
			new_mark = " >= "+value;
		}else if(mark.equals("gt")){
			new_mark = " > "+value;
		}else if(mark.equals("lte")){
			new_mark = " <= "+value;
		}else if(mark.equals("lt")){
			new_mark = " < "+value;
		}else if(mark.equals("startswith")){
			new_mark = " like '"+value+"%' ";
		}else if(mark.equals("endswith")){
			new_mark = " like '%"+value+"' ";
		}else if(mark.equals("contains")){
			new_mark = " like '%"+value+"%' ";
		}else if(mark.equals("doesnotcontain")){
			new_mark = " not like '%"+value+"%' ";
		}
		return fieldName+new_mark;
	}
	
	
	public static String getConditionStr(String fieldName,String mark,String value, boolean isChar) {
		String new_mark = null;
		if(mark.equals("eq")){
			new_mark = " = "+(isChar?"'":"")+value+(isChar?"'":"");
		}else if(mark.equals("neq")){
			new_mark = " <> "+(isChar?"'":"")+value+(isChar?"'":"");
		}else if(mark.equals("gte")){
			new_mark = " >= "+value;
		}else if(mark.equals("gt")){
			new_mark = " > "+value;
		}else if(mark.equals("lte")){
			new_mark = " <= "+value;
		}else if(mark.equals("lt")){
			new_mark = " < "+value;
		}else if(mark.equals("startswith")){
			new_mark = " like '"+value+"%' ";
		}else if(mark.equals("endswith")){
			new_mark = " like '%"+value+"' ";
		}else if(mark.equals("contains")){
			new_mark = " like '%"+value+"%' ";
		}else if(mark.equals("doesnotcontain")){
			new_mark = " not like '%"+value+"%' ";
		}
		return fieldName+new_mark;
	}
	
	public static String getConditionTime(String fieldName,String mark,String value) {
		String conditionTime = fieldName;
		SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss z", Locale.ENGLISH); 
		Date date = null;
		try {
			date = sf.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(mark.equals("eq")){
			conditionTime = conditionTime+" > "+sdf.format(date)+" AND "+fieldName;
			Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(date); 
		    calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
		    date=calendar.getTime(); 
		    conditionTime = conditionTime+" < "+sdf.format(date);
		}else if(mark.equals("neq")){
			conditionTime = conditionTime+" < "+sdf.format(date)+" AND ";
			Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(date); 
		    calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
		    date=calendar.getTime(); 
		    conditionTime = conditionTime+" > "+sdf.format(date)+"";
		}else if(mark.equals("gte")||mark.equals("gt")){
			conditionTime = conditionTime + " > "+sdf.format(date);
		}else if(mark.equals("lte")||mark.equals("lt")){
			conditionTime = conditionTime +  " < "+sdf.format(date);
		}
		return conditionTime;
	}
	public static String FieldConfigure(List<Long> searchId,String fieldName){
		if(searchId.size() == 0){
			return fieldName+" = 0";
		}
		String returnString = null;
		try {
			returnString = "("+fieldName+" = " +searchId.get(0)+")";
		} catch (Exception e) {
			System.out.println(e);
		}
		if(searchId.size()<2){
			return returnString;
		}else{
			for(int i=1;i<searchId.size();i++){
				returnString = returnString+" or ("+fieldName+" = "  + searchId.get(i)+")";
			}
		}
		return "("+returnString+")";
	}
	
	public static String FieldConfigureStr(List<String> searchId, String fieldName) {
		if(searchId == null || searchId.isEmpty()){
			return fieldName+" = 0";
		}
		String returnString = null;
		try {
			returnString = "("+fieldName+" = '" +searchId.get(0)+"')";
		} catch (Exception e) {
			System.out.println(e);
		}
		if(searchId.size()<2){
			return returnString;
		}else{
			for(int i=1;i<searchId.size();i++){
				returnString = returnString+" or ("+fieldName+" = '"  + searchId.get(i)+"')";
			}
		}
		return "("+returnString+")";
	}
	
//	public static String FieldConfigure2(List<Long> searchId,String fieldName){
//		if(searchId.size() == 0){
//			return fieldName+" = 0";
//		}
//		return fieldName + " IN ( " + StringUtils.join(searchId, ", ")+ " ) ";
//	}
}
