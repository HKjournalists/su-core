package com.gettec.fsnip.fsn.web;



import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

/**
 * 
 * @author Rongshen Xie
 *
 */
public class LogServletContextListener extends ContextLoaderListener{

	@Override
	public void contextDestroyed(ServletContextEvent event) {
//		System.out.println("关闭web");
		ThreadLocalUtil.getClient().close();
		super.contextDestroyed(event);
	}

}


