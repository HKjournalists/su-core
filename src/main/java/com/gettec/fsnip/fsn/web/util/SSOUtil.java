package com.gettec.fsnip.fsn.web.util;

import java.util.*;
import java.io.*;

public class SSOUtil {
	private static Properties p = new Properties();
	
	private final static String ENV_KEY = "sso.env";
	private final static String ENV_DEV = "dev";
	private final static String SSO_LOGOUT_URL ="sso.%env%.logout.url";
	private final static String SSO_APPLICATION ="sso.%env%.application";
	
	public final static String LOGOUT = "LOGOUT";
	public final static String APPLICATION = "APPLICATION";
	
	static{
		InputStream ins = SSOUtil.class.getResourceAsStream("/META-INF/properties/SSO.properties");
		try {
			p.load(ins);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getPropety(String key){
		String env = p.getProperty(ENV_KEY);
		if(key.equals(LOGOUT)){
			key = SSO_LOGOUT_URL.replace("%env%", env);
		}else if(key.equals(APPLICATION)){
			key = SSO_APPLICATION.replace("%env%", env);
		}
		return p.getProperty(key);
	}
	
	public static boolean isDev(){
		String env = p.getProperty(ENV_KEY);
		return env.equals(ENV_DEV);
	}
}
