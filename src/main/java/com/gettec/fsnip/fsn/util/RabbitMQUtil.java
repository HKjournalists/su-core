package com.gettec.fsnip.fsn.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * @author zouhao	zouhao619@gmai.com
 * 2016-6-6
 */
public class RabbitMQUtil {
	private static String username;
	private static String password;
	private static String host;
	private static int port;
	private Channel channel;
	private Connection connection;
	private static String queueName;
	static{
		username=PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_USERNAME);
		password=PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_PASSWORD);
		host=PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_HOST);
		port=Integer.valueOf(PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_PORT));
		queueName=PropertiesUtil.getProperty(SystemDefaultInterface.RABBIT_MQ_QUEUE);
	}
	public Channel connect(){
		try{
			//Create a connection factory
			ConnectionFactory factory = new ConnectionFactory();
			//hostname of your rabbitmq server
			factory.setHost(host);
			factory.setUsername(username);
			factory.setPassword(password);
			factory.setPort(port);
			factory.setVirtualHost("/");
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queueName, true, false, false, null);
			return channel;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}	
