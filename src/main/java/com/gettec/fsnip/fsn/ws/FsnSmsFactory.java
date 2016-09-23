package com.gettec.fsnip.fsn.ws;

import java.util.Date;
import com.fsnip.distribution.core.org.tempuri.WebServiceLocator;
import com.fsnip.distribution.core.org.tempuri.WebServiceSoap;
import com.gettec.fsnip.fsn.util.FsnUtil;
import com.gettec.fsnip.fsn.ws.vo.Message;
/**
 * 短信服务工厂类 
 * @author changqiang wu
 *
 */
public class FsnSmsFactory {
	/**
	 * 服务类 wcq
	 * @return
	 * @throws Exception
	 */
	public WebServiceSoap getWebServiceLocator()throws Exception{
		try{
			WebServiceLocator webService=new WebServiceLocator();
			WebServiceSoap serviceSoap =webService.getWebServiceSoap();
			return serviceSoap;
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
		
	}
	/**
	 * 查询余额接口 wcq 20141112
	 * @param sn 序列号
	 * @param psd 密码
	 * @return
	 * @throws Exception
	 */
	public String getBalance(String sn,String psd)throws Exception{
		try{
			if(FsnUtil.isNull(sn)){
				throw new Exception("产品序列号为空,请核对!");
			}
			if(FsnUtil.isNull(psd)){
				throw new Exception("产品密码为空,请核对!");
			}
			FsnSmsFactory factory=new FsnSmsFactory();
			WebServiceSoap webService=factory.getWebServiceLocator();
			String password=FsnUtil.md5_32(sn+psd);
			String banlace=webService.balance(sn, password.toUpperCase());
			return banlace;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	/**
	 * 短信发送接口  wcq   20141112 
	 * 返回负数代表失败  具体参考接口文档   返回字符串代码成功    多天短信用逗号隔开 例如 18600152775,18600162632
	 * 另外Rrid有值 则返回Rrid的值代表成功  默认不建议放值 为空即可
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String mt(Message message)throws Exception{
		try{
			if(FsnUtil.isNull(message.getMobile())){
				throw new Exception("短信发送手机号为空,请核对!");
			}
			if(FsnUtil.isNull(message.getContent())){
				throw new Exception("短信发送内容为空,请核对!");
			}
			if(FsnUtil.isNull(message.getSn())){
				throw new Exception("产品序列号为空,请核对!");
			}
			if(FsnUtil.isNull(message.getPwd())){
				throw new Exception("产品密码为空,请核对!");
			}
			if(FsnUtil.isNull(FsnUtil.LIMS_SMS_SIGN)){
				throw new Exception("短信签名为空,请核对!");
			}
			if(FsnUtil.isNull(message.getStime())){
				message.setStime(FsnUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			if(FsnUtil.isNull(message.getExt())){
				message.setExt("");
			}
			if(FsnUtil.isNull(message.getRrid())){
				message.setRrid("");
			}
			FsnSmsFactory factory=new FsnSmsFactory();
			WebServiceSoap webService=factory.getWebServiceLocator();
			String password=FsnUtil.md5_32(message.getSn()+message.getPwd());
			String messages=webService.mt(message.getSn(), password.toUpperCase(), message.getMobile(), 
					message.getContent()+FsnUtil.LIMS_SMS_SIGN, message.getExt(), message.getStime(),message.getRrid());
			return messages;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	/**
	 * 接收短信 wcq
	 * @param sn
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public String mo(String sn,String pwd)throws Exception{
		try{
			if(FsnUtil.isNull(sn)){
				throw new Exception("产品序列号为空,请核对!");
			}
			if(FsnUtil.isNull(pwd)){
				throw new Exception("产品密码为空,请核对!");
			}
			FsnSmsFactory factory=new FsnSmsFactory();
			WebServiceSoap webService=factory.getWebServiceLocator();
			String password=FsnUtil.md5_32(sn+pwd);
			String mo=webService.mo(sn, password.toUpperCase());
			return mo;
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * 个性化群发短信 建议一次最多提交1000个  短信内容支持长短信
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String gxmt(Message message)throws Exception{
		try{
			if(FsnUtil.isNull(message.getMobile())){
				throw new Exception("短信发送手机号为空,请核对!");
			}
			if(FsnUtil.isNull(message.getContent())){
				throw new Exception("短信发送内容为空,请核对!");
			}
			if(FsnUtil.isNull(message.getSn())){
				throw new Exception("产品序列号为空,请核对!");
			}
			if(FsnUtil.isNull(message.getPwd())){
				throw new Exception("产品密码为空,请核对!");
			}
			if(FsnUtil.isNull(message.getStime())){
				message.setStime(FsnUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			if(FsnUtil.isNull(FsnUtil.LIMS_SMS_SIGN)){
				throw new Exception("短信签名为空,请核对!");
			}
			if(FsnUtil.isNull(message.getExt())){
				message.setExt("");
			}
			if(FsnUtil.isNull(message.getRrid())){
				message.setRrid("");
			}
			FsnSmsFactory factory=new FsnSmsFactory();
			WebServiceSoap webService=factory.getWebServiceLocator();
			String password=FsnUtil.md5_32(message.getSn()+message.getPwd());
			String messages=webService.mt(message.getSn(), password.toUpperCase(), message.getMobile(), 
					message.getContent()+FsnUtil.LIMS_SMS_SIGN, message.getExt(), message.getStime(),message.getRrid());
			return messages;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	/**
	 * 修改密码
	 * @param sn 序列号
	 * @param Pwd  旧密码
	 * @param newpwd 新密码
 	 * @return
	 * @throws Exception
	 */
	public String  UDPPwd(String sn,String Pwd,String newpwd)throws Exception{
		try{
			if(FsnUtil.isNull(sn)){
				throw new Exception("产品序列号为空,请核对!");
			}
			if(FsnUtil.isNull(Pwd)){
				throw new Exception("旧密码为空,请核对!");
			}
			if(FsnUtil.isNull(newpwd)){
				throw new Exception("新密码为空,请核对!");
			}
			FsnSmsFactory factory=new FsnSmsFactory();
			WebServiceSoap webService=factory.getWebServiceLocator();
			String ordPwd=sn+Pwd;
			String newPwd=sn+newpwd;
			String messagePwd=webService.UDPPwd(sn, ordPwd.toUpperCase(), newPwd.toUpperCase());
			return messagePwd;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	/**
	 * 充值
	 * @param Sn 序列号
	 * @param pwd 密码
	 * @param cardno 充值卡号
	 * @param cardpwd 充值卡密码
	 * @return
	 */
	public String ChargUp(String sn,String pwd,String cardno,String cardpwd){
		try{
			if(FsnUtil.isNull(sn)){
				throw new Exception("产品序列号为空,请核对!");
			}
			if(FsnUtil.isNull(pwd)){
				throw new Exception("密码为空,请核对!");
			}
			if(FsnUtil.isNull(cardno)){
				throw new Exception("充值卡号为空,请核对!");
			}
			if(FsnUtil.isNull(cardpwd)){
				throw new Exception("充值卡密码为空,请核对!");
			}
			String passWord=(sn+pwd).toUpperCase();
			FsnSmsFactory factory=new FsnSmsFactory();
			WebServiceSoap webService=factory.getWebServiceLocator();
			String chargUp=webService.chargUp(sn, passWord, cardno, cardpwd);
			return chargUp;
		}catch(Exception e){
			return e.getMessage();
		}

	}
	public static void main(String args[]){
		FsnSmsFactory factory=new FsnSmsFactory();
		try {
			StringBuffer str=new StringBuffer();
			str.append("ceshi141125001您好：");
			str.append("欢迎您公司贵州xx生产企业141125001注册成为食品安全与营养云平台的生产企业级用户。\r\n");
			str.append("您的账户是：ceshi141125001\r\n");
			str.append("初始密码为：hui123456\r\n");
			str.append("登录地址为：http://qaenterprise.fsnip.com\r\n");
			str.append("感谢您的关注和支持！\r\n");
			str.append("请勿直接回复本邮件，如有任何疑问，联系食品安全与营养（贵州）信息科技有限公司。");
			System.out.println(str.toString());
			WebServiceSoap webService=factory.getWebServiceLocator();
			String password=FsnUtil.md5_32("^3^d1-1^");
			String message=webService.mt("SDK-WSS-010-07501", password.toUpperCase(), 
					"18275045227", str.toString(), "", 
					FsnUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "");
			System.out.println(FsnUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"------"+message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
