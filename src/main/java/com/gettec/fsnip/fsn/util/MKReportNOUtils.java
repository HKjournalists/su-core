package com.gettec.fsnip.fsn.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * 
 * @author Hui Zhang
 * 
 */
public class MKReportNOUtils {

	@SuppressWarnings("unused")
	private static final String ALGORITHM_FOR_SHA = "SHA";
	private static final String ALGORITHM_FOR_MD5 = "MD5";
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String getFormattedText(byte[] bytes) {
		StringBuilder buf = new StringBuilder(12);
		for (int idx = 0; idx < 6; idx++) {
			buf.append(DIGITS[(bytes[idx] >> 4) & 0x0f]);
			buf.append(DIGITS[bytes[idx] & 0x0f]);
		}
		return buf.toString();
	}

	public static String encryptString(String str) {
		if (str == null || str.isEmpty() || str.trim().isEmpty()) {
			return null;
		}
		byte[] byteStr = str.getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_FOR_MD5);
			md.update(byteStr);
			return getFormattedText(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String checksum(byte[] bytes){
		return encrypt(bytes, "SHA-1");
	}
	public static String encrypt(byte[] bytes) {
		return encrypt(bytes, "SHA-1");
	}
	
	public static String encrypt(byte[] bytes, String alg) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance(alg);
			crypt.reset();
			crypt.update(bytes);
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	public static boolean hasFullSize(String inStr){  
        try {
			if(inStr.getBytes("UTF-8").length != inStr.length())  {  
			     return true;  
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}                                  
        return false;  
	}
	
	/**
	 * 对报告编号进行处理（避免因包含特殊字符导致上传或下载失败）
	 * @param serviceOrder 报告编号
	 * @param uploadPath 上传文件所在的文件夹名称
	 * @return
	 */
	public static String convertCharacter(String serviceOrder, String uploadPath) {
		/* 1. uploadPath为空 */
		if(uploadPath == null || uploadPath.equals("")){
			uploadPath = serviceOrder;
		}
		/* 2. uploadPath包含中文 */
		if(MKReportNOUtils.hasFullSize(uploadPath)){
			uploadPath = encryptString(uploadPath);
		}
		/* 3. uploadPath包含特殊字符 */
		if(uploadPath.contains("\\s")){ uploadPath = uploadPath.replaceAll("\\s", ""); }
		if(uploadPath.contains(":")){ uploadPath = uploadPath.replace(":", ""); }
		if(uploadPath.contains("/")){ uploadPath = uploadPath.replace("/", "");}
		if(uploadPath.contains("\\")){ uploadPath = uploadPath.replace("\\", ""); }
		if(uploadPath.contains(";")){ uploadPath = uploadPath.replace(";", ""); }
		if(uploadPath.contains("#")){ uploadPath = uploadPath.replace("#", ""); }
		if(uploadPath.contains("%")){ uploadPath = uploadPath.replace("%", ""); }
		if(uploadPath.contains("-")){ uploadPath = uploadPath.replace("-", ""); }
		if(uploadPath.contains(",")){ uploadPath = uploadPath.replace(",", ""); }
		if(uploadPath.contains("[")){ uploadPath = uploadPath.replace("[", ""); }
		if(uploadPath.contains("]")){ uploadPath = uploadPath.replace("]", ""); }
		if(uploadPath.contains("&")){ uploadPath = uploadPath.replace("&", ""); }
		if(uploadPath.contains(".")){ uploadPath = uploadPath.replace(".", ""); }
		if(uploadPath.contains("*")){ uploadPath = uploadPath.replace("*", ""); }
		if(uploadPath.contains("{")){ uploadPath = uploadPath.replace("{", ""); }
		if(uploadPath.contains("}")){ uploadPath = uploadPath.replace("}", ""); }
		if(uploadPath.equals("")){
			uploadPath = "temp";
		}
		/* 4. 返回 */
		return uploadPath;
	}
	
	/*public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "中";
		System.out.println(str.getBytes("UTF-8").length + "\t" + str.length());
	}*/
}
