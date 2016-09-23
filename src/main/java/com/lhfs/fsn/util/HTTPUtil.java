package com.lhfs.fsn.util;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class HTTPUtil {
	
	// servletRequest
	public static HttpServletRequest getHttpServletRequest () {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return sra.getRequest();
	}
	
	// session
	public static HttpSession session () {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true); // true == allow create
	}
	
	// request URL
	public static String getReqURL (HttpServletRequest request) {
		if (request == null) {
			return getHttpServletRequest().getRequestURL().toString();
		}
		return request.getRequestURL().toString();
	}
	
	// request URI
	public static String getReqURI (HttpServletRequest request) {
		if (request == null) {
			return getHttpServletRequest().getRequestURI().toString();
		}
		return request.getRequestURI().toString();
	}
	
	// context path: like /fsn-core
	public static String getReqContextPath (HttpServletRequest request) {
		if (request == null) {
			return getHttpServletRequest().getContextPath();
		}
		return request.getContextPath();
	}
	
	// return: http or https
	public static String getReqScheme (HttpServletRequest request) {
		if (request == null) {
			return getHttpServletRequest().getScheme();
		}
		return request.getScheme();
	}
	
	// return: server name like 'localhost' or '192.168.1.11' or 'fsn.fsnip.com'
	public static String getReqServerName (HttpServletRequest request) {
		if (request == null) {
			return getHttpServletRequest().getServerName();
		}
		return request.getServerName();
	}
	
	// return: server port like '8080' or '80'
	public static int getReqServerPort (HttpServletRequest request) {
		if (request == null) {
			return getHttpServletRequest().getServerPort();
		}
		return request.getServerPort();
	}
	
	// 组装请求信息，生成项目的base路径
	public static String getServerBasePath (HttpServletRequest request) {
		if (request != null) {
			request = getHttpServletRequest();
		}
		int port = getReqServerPort(request);
		StringBuffer sBuff = new StringBuffer();
		sBuff.append(getReqScheme(request)).append("://").append(getReqServerName(request))
			.append(port == 80?"":(":"+port)).append(getReqContextPath(request));
		return sBuff.toString();
	}
	
	public static ArrayList<MultipartFile> getMultipartFiles (HttpServletRequest request) {
		ArrayList<MultipartFile> files = new ArrayList<MultipartFile>();
		// 设置上下方文  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 检查form是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
        	MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        	Iterator<String> iter = multiRequest.getFileNames();
        	while (iter.hasNext()) {
        		MultipartFile file = multiRequest.getFile(iter.next());
        		if (file != null) {  
                    files.add(file); 
                }
        	}
        }
        return files;
	}
	
}
