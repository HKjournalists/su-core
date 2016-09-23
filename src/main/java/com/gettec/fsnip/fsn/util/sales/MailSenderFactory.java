package com.gettec.fsnip.fsn.util.sales;

import java.io.File;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.gettec.fsnip.fsn.util.FileUtils;

/**
 * 消息系统邮件发送工厂
 * @author tangxin 2015-05-10
 *
 */
public class MailSenderFactory {

	@Autowired 
	private JavaMailSender javaMailSender;
	  
    public void setJavaMailSender(JavaMailSender javaMailSender) {  
        this.javaMailSender = javaMailSender; 
    }  
  
  
    /** 
     * 简单邮件发送 
     * @param bmd 
     */  
    public void senderSimpleMail(BaseMailDefined bmd){ 
        SimpleMailMessage msg = new SimpleMailMessage();  
            msg.setFrom("10000@fsnip.com");  
            msg.setText(bmd.getText());  
            msg.setTo(bmd.getTo());  
            msg.setSubject(bmd.getSubject());  
            javaMailSender.send(msg);  
    }  
      
    /** 
     * 发送添加附件的邮件 
     * @param bmd 
     * @throws MessagingException 
     */  
    public void senderMimeMail(BaseMailDefined bmd, File file) throws MessagingException{ 
    	if(file == null){
    		senderSimpleMail(bmd);
    	}else{
    		MimeMessage msg = javaMailSender.createMimeMessage();  
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);  
            helper.setTo(bmd.getTo());  
            helper.setText(bmd.getText());  
            helper.setFrom("10000@fsnip.com");
            helper.setSubject(bmd.getSubject());  
            helper.setSentDate(new Date());
            /* 附件后缀名 */
            String exp = FileUtils.getExtension(file.getName());
            //携带附件  
            FileSystemResource fsr = new FileSystemResource(file);
            helper.addAttachment("企业电子资料"+exp, fsr);
            javaMailSender.send(msg);  
    	}
    }  
      
    /** 
     * 发送内嵌图片或html的邮件 
     * @param bmd 
     * @throws MessagingException 
     */  
    public void senderHtmlMail(BaseMailDefined bmd, File file) throws MessagingException{ 
    	if(file == null) {
    		senderSimpleMail(bmd);
    	} else {
    		MimeMessage msg = javaMailSender.createMimeMessage(); 
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);  
            helper.setTo(bmd.getTo());  
            helper.setText(bmd.getText());  
            helper.setFrom("10000@fsnip.com");  
            helper.setSubject(bmd.getSubject());  
            helper.setSentDate(new Date());
            //内嵌式图片或html样式  
            helper.setText("<html><body><img src='cid:identifier1235'></body></html>", true);  
            FileSystemResource res = new FileSystemResource(file);  
            helper.addInline("identifier1235", res);  
            javaMailSender.send(msg); 
    	}
    }  
}
