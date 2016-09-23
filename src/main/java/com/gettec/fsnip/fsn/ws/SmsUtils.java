package com.gettec.fsnip.fsn.ws;

import com.gettec.fsnip.fsn.util.FsnUtil;
import com.gettec.fsnip.fsn.ws.vo.Message;

public class SmsUtils {
	private Message msg;//短信内容
	 public SmsUtils(Message msg){
	  this.msg=msg;
	 }
	
	 /*异步发送短信
	  * 
	  */
	 public void SyncSendSms(){  //开始请求
		  final Thread  t=new Thread(new Runnable(){
		   public void run(){
			   SendSms();
		   }}
		  );
		  t.start();
	 }
	 
     /*
      * 同步发送短信
      * 
      */
	 public void SendSms()
	 {
		  	FsnSmsFactory factory = new FsnSmsFactory();
	    	Message message = new Message();
	        message.setSn(FsnUtil.LIMS_SMS_SN);
	        message.setPwd(FsnUtil.LIMS_SMS_PWD);
	        message.setMobile(msg.getMobile());
	        message.setContent(msg.getContent());
	        String smessage;
			try {
				smessage = factory.mt(message);
			    if(!FsnUtil.isNull(smessage)){
		        	if(FsnUtil.isNumeric(smessage)){
		        		if(smessage.length()<=4){
		        			System.out.println(FsnUtil.getSmsError(smessage));
		        		}
		        	}else{
		        		System.out.println(smessage);
		        	}
		        }
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	 }
	
}
	

