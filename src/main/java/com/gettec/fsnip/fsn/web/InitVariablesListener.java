package com.gettec.fsnip.fsn.web;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * @author Sam Zhou
 *
 */
public class InitVariablesListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		String env = sce.getServletContext().getInitParameter("ENV").toUpperCase();
		if(env == null || env.trim().equals("")){
			env = "DEV";
		}
		try{
			Properties properties = new Properties();
			properties.load(getClass().getResourceAsStream("/META-INF/properties/CAS_" + env + ".properties"));
			
			for(String key:properties.stringPropertyNames()){
				if(properties.get(key) != null){
//					System.out.println(key + " : " + properties.getProperty(key));
					System.setProperty(key, properties.getProperty(key));
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

}
