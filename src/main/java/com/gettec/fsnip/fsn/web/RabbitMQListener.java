package com.gettec.fsnip.fsn.web;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gettec.fsnip.fsn.other.DealToProblemOther;
import com.gettec.fsnip.fsn.other.ProductOther;
import com.gettec.fsnip.fsn.service.deal.DealToProblemService;
import com.gettec.fsnip.fsn.service.product.ProductService;
import com.gettec.fsnip.fsn.util.PropertiesUtil;
import com.gettec.fsnip.fsn.util.RabbitMQUtil;
import com.gettec.fsnip.fsn.util.SystemDefaultInterface;
import com.rabbitmq.client.Channel;
public class RabbitMQListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("begin.......MQ");
		RabbitMQUtil rabbitMQUtil=new RabbitMQUtil();
		Channel channel=rabbitMQUtil.connect();
		WebApplicationContext applicationContext =    WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());    
		this.setServiceObject(applicationContext,channel);
		
	}
	/**
	 * 所有的Service注入都在以下方法里面完成
	 * @param applicationContext
	 */
	private void setServiceObject(WebApplicationContext applicationContext,Channel channel) {
		/**
	     * 获取产品类型的service
	     */
		ProductService productService=(ProductService)applicationContext.getBean("productService"); 
		ProductOther productOther=new ProductOther();
		productOther.setProductService(productService);
		
	    /**
	     * 获取问题类型的service
	     */
		DealToProblemService dealToProblemService=(DealToProblemService)applicationContext.getBean("dealToProblemService"); 
		DealToProblemOther dealToProblemOther=new DealToProblemOther();
		dealToProblemOther.setDealToProblemService(dealToProblemService);
		
		if(channel!=null){
			try {
				channel.basicConsume(PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_QUEUE),true,productOther);
				channel.basicConsume(PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_SUPERVISE_JG_FEN),true,dealToProblemOther);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
   
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("end.......MQ");
	}

}
