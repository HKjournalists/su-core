package com.gettec.fsnip.fsn.util.sales;

import java.util.List;

/**
 * 基础邮件信息
 * @author tangxin 2015-05-10
 *
 */
public class BaseMailDefined {  
    private String to;  //收件人  
    private String from; //发件人
    private String subject; //主题
    private String text; //正文
    private List<Long> attachments; //附件资料id集合
    
    public String getText() {
       return text;  
   }  

   public String getTo() {  
       return to;  
   }  

   public String getFrom() {  
       return from;  
   }  

   public String getSubject() {  
       return subject;  
   }  

   public void setText(String text) {  
       this.text = text;  
   }  

   public void setTo(String to) {    
        this.to = to;    
    }    
    
    public void setFrom(String from) {    
        this.from = from;    
    }    
    
    public void setSubject(String subject) {    
        this.subject = subject;    
    }

	public List<Long> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Long> attachments) {
		this.attachments = attachments;
	}

	public BaseMailDefined(){
		super();
	}
	
	public BaseMailDefined(String to, String from, String subject, String text,
			List<Long> attachments) {
		super();
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.text = text;
		this.attachments = attachments;
	}    
    
}  
