package com.gettec.fsnip.fsn.util;

import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_PRODUCT_PATH;
import static com.gettec.fsnip.fsn.util.SystemDefaultInterface.FSN_FTP_UPLOAD_WEB_PRODUCT_PATH;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.jasig.cas.client.util.CommonUtils;

import com.gettec.fsnip.sso.client.util.AccessUtils;
import com.lhfs.fsn.util.HTTPUtil;

public class HttpUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static String getResponseFromServer(URL constructedUrl,final HostnameVerifier hostnameVerifier, final String encoding) {
	       URLConnection conn = null;
	       try {
	           conn = constructedUrl.openConnection();
	           if (conn instanceof HttpsURLConnection) {
	              ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
	           }
	           final BufferedReader in;
	           if (CommonUtils.isEmpty(encoding)) {
	              in = new BufferedReader(new InputStreamReader(
	                     conn.getInputStream()));
	           } else {
	              in = new BufferedReader(new InputStreamReader(
	                     conn.getInputStream(), encoding));
	           }
	           String line;
	           final StringBuilder stringBuffer = new StringBuilder(255);
	           while ((line = in.readLine()) != null) {
	              stringBuffer.append(line);
	              stringBuffer.append("\n");
	           }
	           return stringBuffer.toString();
	       } catch (final Exception e) {
	           throw new RuntimeException(e);
	       } finally {
	           if (conn != null && conn instanceof HttpURLConnection) {
	              ((HttpURLConnection) conn).disconnect();
	           }
	       }
	 }
	/**
	 * 根据不同环境fsn的域名获取sso域名
	 * @return
	 */
	public static String getSSOHostname(){
		String hostname = HTTPUtil.getReqServerName(null);
		String SSO_HOSTNAME ="";
		if ("qaenterprise.fsnip.com".equals(hostname)) {
			SSO_HOSTNAME = "http://qasso.fsnip.com";		
		} else if ("stgenterprise.fsnip.com".equals(hostname)){
			SSO_HOSTNAME = "http://stgsso.fsnip.com";			
		} else if ("enterprise.fsnip.com".equals(hostname)) {
			SSO_HOSTNAME = "http://sso.fsnip.com";			
		} else {
			SSO_HOSTNAME = "http://stgsso.fsnip.com";	
//			SSO_HOSTNAME = "http://localhost:9090/sso";	
		}
		return SSO_HOSTNAME;
	}
	
	/**
	 * 根据不同环境fsn的域名获取WDA域名
	 * @return
	 */
	public static String getWDAHostname(){
		String hostname = HTTPUtil.getReqServerName(null);
		String WDA_HOSTNAME ="";
		if ("qaenterprise.fsnip.com".equals(hostname)) {
			WDA_HOSTNAME = "http://qawda.fsnip.com";		
		} else if ("stgenterprise.fsnip.com".equals(hostname)){
			WDA_HOSTNAME = "http://stgwda.fsnip.com";			
		} else if ("enterprise.fsnip.com".equals(hostname)) {
			WDA_HOSTNAME = "http://wda.fsnip.com";			
		} else {
			WDA_HOSTNAME = "http://localhost:8089";		
		}
		return WDA_HOSTNAME;
	}
	
	/**
	 * 根据不同环境fsn的域名获取Portal域名
	 * @return
	 */
	public static String getPortalHostname(){
		String hostname = HTTPUtil.getReqServerName(null);
		String Portal_HOSTNAME ="";
		if ("qaenterprise.fsnip.com".equals(hostname)) {
		    Portal_HOSTNAME = "http://qaportal.fsnip.com";		
		} else if ("stgenterprise.fsnip.com".equals(hostname)){
		    Portal_HOSTNAME = "http://stgportal.fsnip.com";			
		} else if ("enterprise.fsnip.com".equals(hostname)) {
		    Portal_HOSTNAME = "www.fsnip.com";			
		} else {
		    //Portal_HOSTNAME = "http://localhost:8089";		
		    //Portal_HOSTNAME = "http://192.168.1.47:8080";	//冯涛涛	
		    Portal_HOSTNAME = "http://qaportal.fsnip.com";		
		}
		return Portal_HOSTNAME;
	}
	
	/**
     * 根据不同环境fsn的域名获取FDAMS域名
     * @return
     */
    public static String getFDAMSHostname(){
        String hostname = HTTPUtil.getReqServerName(null);
        String WDA_HOSTNAME ="";
        if ("qaenterprise.fsnip.com".equals(hostname)) {
            WDA_HOSTNAME = "http://qafdams.fsnip.com";      
        } else if ("stgenterprise.fsnip.com".equals(hostname)){
            WDA_HOSTNAME = "http://stgfdams.fsnip.com";         
        } else if ("enterprise.fsnip.com".equals(hostname)) {
            WDA_HOSTNAME = "http://fdams.fsnip.com";            
        } else {
            WDA_HOSTNAME = "http://localhost:8089";     
        }
        return WDA_HOSTNAME;
    }
	
	public static String send(String url, String method, Object param) {
		URL targetUrl = null;
		HttpURLConnection urlCon = null;
		if (param != null && param instanceof String) {
			url = (!url.endsWith("?")) ? (url + "?" + param) : (url + param);
		}
		try {
			targetUrl = new URL(url);
			urlCon = (HttpURLConnection) targetUrl.openConnection();
			urlCon.setRequestMethod(method);
			if (param != null && (param instanceof JSONObject || param instanceof JSON || param instanceof String)&&!"GET".equals(method)) {//
				urlCon.setDoOutput(true);
				urlCon.setRequestProperty("Content-type", "application/json");
				urlCon.getOutputStream().write(param.toString().getBytes(AccessUtils.ENCODE_UTF_8));
				urlCon.getOutputStream().flush();
				urlCon.getOutputStream().close();
			}
			InputStream in = urlCon.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, AccessUtils.ENCODE_UTF_8));
			String line = rd.readLine();
			StringBuffer jsonResult = new StringBuffer();
			while (line != null) {
				jsonResult.append(line);
				line = rd.readLine();
			}
			rd.close();
			in.close();
			return jsonResult.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (urlCon != null)
				urlCon.disconnect();
		}
		return null;
	}
	
	/**
	 * 根据网络资源地址，获取图片，并上传至ftp
	 * @param fileUrl   网络资源地址
	 * @param savePath  上传地址
	 * @author ZhangHui 2015/4/27
	 */
	public static String getPictureFtpUrl(String fileUrl, String filePathName) {
		try {
			if(fileUrl==null || filePathName==null){ return null; }
			if("".equals(fileUrl) || "".equals(filePathName)){ return null; }
            /* 将网络资源地址赋值给url */
            URL url = new URL(fileUrl);
             
            /* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            
            /* 上传至ftp */
			String fileName = filePathName + sdf.format(new Date()) + (new Random().nextInt(1000) + 1) + ".jpg";
			String ftpPath = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_PRODUCT_PATH) + "/" + filePathName;
			UploadUtil uploadUtil = new UploadUtil();
			boolean success_upload = uploadUtil.uploadFile(in, ftpPath, fileName);
			String ftpUrl = null;
			if(success_upload){
				if(UploadUtil.IsOss()){
					ftpUrl = uploadUtil.getOssSignUrl(ftpPath+"/"+fileName);
				}else{
					ftpUrl = PropertiesUtil.getProperty(FSN_FTP_UPLOAD_WEB_PRODUCT_PATH) + "/" + filePathName + "/" + fileName;
				}
			}
			
            connection.disconnect();
            /* 返回 */
            return ftpUrl;
        } catch (Exception e) {
            return null;
        }
	}
	/**
	 * 获取客户端IP地址
	 * @author LongXianZhen 2015/06/04
	 */
	public static String getIpAddr(HttpServletRequest request) { 
	       String ip = request.getHeader("x-forwarded-for"); 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("WL-Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getRemoteAddr(); 
	       } 
	       return ip; 
	   } 
}