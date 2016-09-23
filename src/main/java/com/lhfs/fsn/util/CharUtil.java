package com.lhfs.fsn.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * @ClassName: CharUtil 
 * @Description: TODO(对字符串做处理，包括中文判断和剔除，标点符号的判断) 
 * @author 段福友
 * @date 2013-6-4 上午10:55:10 
 */
public class CharUtil {
 
	/**
	 * 根据Unicode编码完美的判断中文汉字和符号
	 * @param c
	 * @return
	 */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
 
    /**
     * 完整的判断中文汉字和符号
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
 
    /**
     * 只能判断部分CJK字符（CJK统一汉字）
     * @param str
     * @return
     */
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }
 
    /**
     * 只能判断部分CJK字符（CJK统一汉字）
     * @param str
     * @return
     */
    public static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }
    
    /**
     * 删除字符串中不是中文的字符
     * @param strName
     * @return
     */
    public static String filterNonCN(String strName){
		String regex = "^[\u4e00-\u9fa5]*$";//判断中文正则表达式
		strName = strName.replaceAll("\\w+", "");//删除数字、英文字母或下滑线正则表达式
		if(!strName.matches(regex)){
			int length = strName.length();
			for(int i = 0;i < length;i++){
				String temp = String.valueOf(strName.charAt(i));
				if(!temp.matches(regex)){
					strName = strName.replace(temp, "");
					i = i - 1;
					length = strName.length();
				}
			}
		}
		return strName;
	}
    
    /**
     * 变量编码转换
     * @param p
     * @param c
     * @return
     * @throws UnsupportedEncodingException
     */
 	public static String changeURLCode(String p, String c) throws UnsupportedEncodingException{
 		return java.net.URLDecoder.decode(p, c);
 	}
}
