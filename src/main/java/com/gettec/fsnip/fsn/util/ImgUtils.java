package com.gettec.fsnip.fsn.util;

import java.net.MalformedURLException;
import java.net.URL;

public class ImgUtils {
	/**
	 * 生成相应大小的图片
	 * @param coverimg
	 * @param width
	 * @param height
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getImgPath(String coverimg, int width, int height) {
		String host = "qa.fsnrec.com,fsnrec.com:8080,stg.fsnrec.com";	//支持缩放域名
		int flag = 0;
		String defaultImg = "http://qa.fsnrec.com/viewimage/portal/img/product/temp/temp/300x300.jpg";//默认图片地址
		boolean isOSS = UploadUtil.IsOss();
		if ("".equals(coverimg) || coverimg==null) {
			return defaultImg;
		}
		if (!coverimg.startsWith("http")) {
			return coverimg;
		}
		String http = coverimg.substring(7);
		String head = http.substring(0, http.indexOf("/"));
		String xurl = coverimg.replace("http://" + head, "");
		try {
			URL url = new URL(coverimg);
			for(int i=0;i<host.split(",").length;i++){
				if(head.equals(host.split(",")[i])){
					flag = 1;
				}
			}
			if (flag == 0) {
				return coverimg;
			}
		} catch (MalformedURLException e) {
			return "";
		}
		if(xurl.lastIndexOf(".")==-1){
			return coverimg;
		}
		xurl = xurl.substring(0, xurl.lastIndexOf("."));
		if(coverimg.lastIndexOf(".") == -1){
			return coverimg;
		}
		String xend = coverimg.substring(coverimg.lastIndexOf("."));
		String w = String.valueOf(width);
		String h = String.valueOf(height);
		/* 如果路径里面已经报告 viewimage ，则不需再动态添加 */
		String viewStr = (coverimg.contains(head+"/viewimage/") ? "" : "/viewimage");
		coverimg = isOSS ? coverimg + "@"+w+"w_"+h+"h_90Q" +xend : "http://" + head + viewStr + xurl + "/" + w + "x" + h + xend;
		return coverimg;
	}

}
