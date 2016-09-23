package com.lhfs.fsn.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

/**
 * String 常用工具类
 * @author 余有华
 *
 */

public class StringUtil {
	
	private static final String UN_REVIEW= "未审核";
	private static final String PASSED= "审核通过";
	private static final String RETURENED= "审核退回";
	//判断字符串是否为空
	public static boolean isBlank(Object str){
          return str==null || "".equals(str.toString().trim());
	}
	
	public static String getUTF8Code(String str) throws UnsupportedEncodingException{
		if(!isBlank(str) && java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode(str)){
			 return new String(str.getBytes("ISO-8859-1"),"UTF-8");
		}
       return str;
	}
	
	
	//判断字符串字符是否全部为数字
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	//产生随机颜色编码
	public static String generateColor(){
		String base = "abcdef0123456789"; 
		Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < 6; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }
	    return "#".concat(sb.toString());
	}
	
	//数组转换成String 用于SQL中的in条件
	public static String arraytToString(String[] str){
		String temp = "";
		for(int i=0;i<str.length;i++){
			temp += str[i]+",";
		}
		return isBlank(temp)? temp :temp.substring(0,temp.length()-1);
	}
	
	//数组转换成String 用于SQL中的in条件
		public static String arraytToString2(String[] str){
			String[] temp = str[0].split(",");
			String a = "";
			for(int i=0;i<temp.length;i++){
				a += "'"+temp[i]+"',";
			}
			return (isBlank(a)? a :a.substring(0,a.length()-1));
		}
		
	//数组转换成String 用于SQL中的in条件
	public static String arraytToLike(String[] str){
		String[] temp = str[0].split(",");
		String like = " or code like ";
		String a = "";
		for(int i=0;i<temp.length;i++){
			a += "'"+temp[i]+"%'"+like;
		}
		return isBlank(a)? a :a.substring(0,a.length()-like.length());
	}
	
	public static String getInterval(String interval){
		if("month".equals(interval)){
			return "%m";
		}else if("week".equals(interval)){
			return "%u";
		}else{
			return "%m%d";
		}
	}
	
	/**
	 * 转换String[] 为 int[]
	 * @param strs
	 * @return
	 */
	public static int[] convArrStringToInt (String[] strs) {
		if (isBlank(strs)) {
			return null;
		}
		int arrLen = strs.length;
		boolean status = true; // 状态
		int[] is = new int[arrLen];
		for (int i=0; i<arrLen; i++) {
			
			if(!isNumeric(strs[i])) {
				status = false;
				break;
			}
			
			is[i] = Integer.parseInt(strs[i]);
		}
		
		return status?is:null;
	}
	
	// 变量编码转换
	 	public static String changeURLCode(String p, String c) {
	 		try {
				p = URLDecoder.decode(p, c);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
	 		return p;
	 	}
	 	/**
	 	 * 审核状态
	 	 * @param status
	 	 * @return
	 	 */
		public static String getStatus(Integer status){
	 		switch (status) {
			case 0:
				return UN_REVIEW;
			case 1:
				return PASSED;
			case 2:
				return RETURENED;
			default:
				return "";
			}
	 	}
	
	
}
