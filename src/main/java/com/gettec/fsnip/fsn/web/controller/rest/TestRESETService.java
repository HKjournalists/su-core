package com.gettec.fsnip.fsn.web.controller.rest;

import static com.gettec.fsnip.fsn.web.IWebUtils.JSON;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.gettec.fsnip.fsn.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

@Controller
@RequestMapping("/zouhao")
public class TestRESETService {
	@RequestMapping(method = RequestMethod.GET, value = "")
	public View test() throws IOException{
		RabbitMQUtil rabbitMQUtil=new RabbitMQUtil();
		Channel channel=rabbitMQUtil.connect();
		channel.exchangeDeclare("jg", "direct",true);
		channel.queueDeclare("jg", true, false, false, null);  
		channel.queueBind("jg","jg","jg_key");
		JSONObject json=new JSONObject();
		json.put("a","ccc");
		channel.basicPublish("jg","jg_key", MessageProperties.BASIC, json.toString().getBytes());  
		return JSON;
	}
}
