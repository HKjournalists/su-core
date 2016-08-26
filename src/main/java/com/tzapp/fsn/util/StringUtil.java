package com.tzapp.fsn.util;

import java.net.MalformedURLException;
import java.net.URL;

import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.UploadUtil;

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
	 * 生成相应大小的图片
	 * 
	 * @param coverimg
	 * @param width
	 * @param height
	 * @return
	 */
	public static String getImgPath(String coverimg, int width, int height) {
		if(UploadUtil.IsOss()){
			try {
				URL url=new URL(coverimg);
				String filePath=url.getPath();
				String prefix=filePath.substring(0, 1);
				if(prefix.equals("/")){
					filePath=filePath.substring(1, filePath.length());
				}
				UploadUtil uploadUtil=new UploadUtil();
				if(!coverimg.contains(PropertiesUtil.getProperty("oss.url"))&&coverimg.contains("fsnrec.com")){
				}else if(coverimg.contains(PropertiesUtil.getProperty("oss.url"))&&coverimg.contains("%")){
					filePath=filePath.replaceFirst("%\\d+w_\\d+h\\..*","");
				}
				if(width!=0&&height!=0){
					filePath+="@"+width+"w_"+height+"h.jpg";
				}
				return uploadUtil.getOssSignUrl(filePath);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		}else{
			String host = "qa.fsnrec.com,stg.fsnrec.com,fsnrec.com:8080";
			int flag = 0;
			String defaultImg = "http://qa.fsnrec.com/viewimage/portal/img/product/temp/temp/300x300.jpg"; // 默认图片地址
			if (StringUtil.isEmpty(coverimg)) {
				return defaultImg;
			}
			if(width<=0||height<=0){
				return coverimg;
			}
			if (!coverimg.startsWith("http")) {
				return coverimg;
			}
			String http = coverimg.substring(7);
			String head = http.substring(0, http.indexOf("/"));
			String xurl = coverimg.replace("http://" + head, "");
			try {
				//URL url = new URL(coverimg);
				for (int i = 0; i < host.split(",").length; i++) {
					if (head.equals(host.split(",")[i])) {
						flag = 1;
					}
				}
				if (flag == 0) {
					return coverimg;
				}
			} catch (Exception e) {
				return "";
			}
			if (xurl.lastIndexOf(".") == -1) {
				return coverimg;
			}
			xurl = xurl.substring(0, xurl.lastIndexOf("."));
			if (coverimg.lastIndexOf(".") == -1) {
				return coverimg;
			}
			String xend = coverimg.substring(coverimg.lastIndexOf("."));
			String h = String.valueOf(height);
			if (coverimg.indexOf("viewimage") > -1) {
				coverimg = coverimg.substring(0, coverimg.lastIndexOf("/")) + "/"
						+ width + "x" + h+"_80_1" + xend;
			} else {
				coverimg = "http://" + head + "/viewimage" + xurl + "/" + width
						+ "x" + h +"_80_1"+ xend;
			}
			return coverimg;
		}
	} 
}
