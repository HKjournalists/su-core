package com.gettec.fsnip.fsn.web.controller;

import javax.servlet.http.HttpServletResponse;

public class CorsSupport {

	private final static boolean DEV_ENV = true;
	
	private final static String ORIGIN = "*";
	private final static String ALLOW_METHOD = "GET,POST,PUT,DELETE,OPTIONS";
	private final static String ALLOW_HEADER = "accept, origin, content-type";
	
	public static void setHeader(HttpServletResponse response, String requestMethod){
		if(DEV_ENV){
			response.addHeader("Access-Control-Allow-Methods", ALLOW_METHOD);
			response.addHeader("Access-Control-Allow-Origin", ORIGIN);
			if(requestMethod.equals("POST") || requestMethod.equals("PUT") 
					|| requestMethod.equals("DELETE") || requestMethod.equals("OPTIONS")){
				
				response.addHeader("Access-Control-Request-Method", requestMethod);
				response.addHeader("Access-Control-Request-Headers",ALLOW_HEADER);
			}
		}
	}
}
